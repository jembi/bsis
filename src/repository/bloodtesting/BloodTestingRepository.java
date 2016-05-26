package repository.bloodtesting;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.TTIStatus;
import model.bloodtesting.rules.BloodTestingRule;
import model.donation.Donation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dto.BloodTestResultDTO;
import repository.BloodTestResultNamedQueryConstants;
import repository.DonationBatchRepository;
import repository.DonationRepository;
import viewmodel.BloodTestResultViewModel;
import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class BloodTestingRepository {

  private static final Logger LOGGER = Logger.getLogger(BloodTestingRepository.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private BloodTestingRuleEngine ruleEngine;

  public List<BloodTest> getBloodTypingTests() {
    String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.category=:category";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("isActive", true);
    query.setParameter("category", BloodTestCategory.BLOODTYPING);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public List<BloodTest> getBloodTestsOfType(BloodTestType type) {
    return getBloodTestsOfTypes(Arrays.asList(type));
  }

  private List<BloodTest> getBloodTestsOfTypes(List<BloodTestType> types) {
    String queryStr = "SELECT b FROM BloodTest b WHERE "
        + "b.bloodTestType IN (:types) AND " + "b.isActive=:isActive";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("types", types);
    query.setParameter("isActive", true);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  /**
   * Save the BloodTestingRuleResult and update the Donation blood ABO/Rh and blood typing statuses
   *
   * @param bloodTestResultsForDonation Map of test results with the BloodTest identifier as the
   *                                    key
   * @param donation                    Donation associated with the test results
   * @param testedOn                    Date the tests were done
   * @param ruleResult                  BloodTestingRuleResult from the BloodTestingRulesEngine
   * @param reEntry                     boolean true if the results are the re-entry and false if the results are first entry
   */
  public void saveBloodTestResultsToDatabase(
      Map<Long, String> bloodTestResultsForDonation,
      Donation donation, Date testedOn,
      BloodTestingRuleResult ruleResult,
      boolean reEntry) {

    Map<Long, BloodTestResult> mostRecentTestResults = getRecentTestResultsForDonation(donation.getId());
    for (Long testId : bloodTestResultsForDonation.keySet()) {
      BloodTestResult btResult = mostRecentTestResults.get(testId);
      updateOrCreateBloodTestResult(btResult, testId, bloodTestResultsForDonation.get(testId), donation, testedOn, reEntry);
    }
    if (reEntry && ruleResult != null) {
      updateDonationWithTestResults(donation, ruleResult);
      em.persist(donation);
    }
  }

  private BloodTestResult updateOrCreateBloodTestResult(BloodTestResult btResult, Long testId, String testResult,
      Donation donation, Date testedOn, boolean reEntry) {

    if (btResult == null) {
      btResult = new BloodTestResult();
      BloodTest bloodTest = findBloodTestById(testId);
      btResult.setBloodTest(bloodTest);
      // not updating the inverse relation which means the
      // donation.getBloodTypingResults() will not
      // contain this result
      btResult.setDonation(donation);
      btResult.setTestedOn(testedOn);
      btResult.setNotes("");
      btResult.setResult(testResult);
      // re-entry is not always required for initial tests, depends on the implementation, and it's
      // controlled from the frontend
      btResult.setReEntryRequired(!reEntry);
    } else {
      if (!testResult.equals(btResult.getResult())) {
        btResult.setResult(testResult);
        // re-entry is only required if the initial test result is being modified
        btResult.setReEntryRequired(!reEntry);
      } else {
        // only clear the re-entry required flag if the update is a re-entry
        if (btResult.getReEntryRequired() && reEntry) { 
          btResult.setReEntryRequired(false);
        }
      }
    }
    em.persist(btResult);
    return btResult;
  }

  public List<BloodTest> findActiveBloodTests() {

    return em.createQuery(
        "SELECT b " +
            "FROM BloodTest b " +
            "WHERE b.isActive = :isActive ",
            BloodTest.class)
        .setParameter("isActive", true)
        .getResultList();
  }

  public List<BloodTest> getAllBloodTestsIncludeInactive() {
    String queryStr = "SELECT b FROM BloodTest b";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatches(
      List<Long> donationBatchIds) {

    List<BloodTestingRuleResult> bloodTestingRuleResults = new ArrayList<BloodTestingRuleResult>();

    for (Long donationBatchId : donationBatchIds) {
      List<Donation> donations = donationBatchRepository.findDonationsInBatch(donationBatchId);

      for (Donation donation : donations) {

        if (!donation.getPackType().getTestSampleProduced()) {
          // This donation did not produce a test sample so skip it
          continue;
        }

        BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
            donation, new HashMap<Long, String>());
        bloodTestingRuleResults.add(ruleResult);
      }
    }

    return bloodTestingRuleResults;
  }

  public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatchesByBloodTestType(List<Long> donationBatchIds,
      BloodTestType bloodTestType) {

    List<BloodTestingRuleResult> bloodTestingRuleResults = getAllTestsStatusForDonationBatches(donationBatchIds);
    List<BloodTestingRuleResult> filteredRuleResults = new ArrayList<BloodTestingRuleResult>();
    for (BloodTestingRuleResult result : bloodTestingRuleResults) {
      Map<String, BloodTestResultViewModel> modelMap = result.getRecentTestResults();
      Map<String, BloodTestResultViewModel> filteredModelMap = new HashMap<String, BloodTestResultViewModel>();
      for (String key : modelMap.keySet()) {
        BloodTestResultViewModel model = modelMap.get(key);
        if (model.getTestResult().getBloodTest().getBloodTestType().equals(bloodTestType)) {
          filteredModelMap.put(key, model);
        }
      }
      result.setRecentTestResults(filteredModelMap);
      filteredRuleResults.add(result);
    }
    bloodTestingRuleResults = filteredRuleResults;

    return bloodTestingRuleResults;
  }

  public BloodTestingRuleResult getAllTestsStatusForDonation(
      Long donationId) {
    Donation donation = donationRepository
        .findDonationById(donationId);
    return ruleEngine.applyBloodTests(donation,
        new HashMap<Long, String>());
  }

  public List<BloodTest> getTTITests() {
    String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.category=:category";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("isActive", true);
    query.setParameter("category", BloodTestCategory.TTI);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public Map<Long, BloodTestResult> getRecentTestResultsForDonation(
      Long donationId) {
    String queryStr = "SELECT bt FROM BloodTestResult bt WHERE "
        + "bt.donation.id=:donationId";
    TypedQuery<BloodTestResult> query = em.createQuery(queryStr,
        BloodTestResult.class);
    query.setParameter("donationId", donationId);
    List<BloodTestResult> bloodTestResults = query.getResultList();
    Map<Long, BloodTestResult> recentBloodTestResults = new HashMap<Long, BloodTestResult>();
    for (BloodTestResult bt : bloodTestResults) {
      Long bloodTestId = bt.getBloodTest().getId();
      BloodTestResult existingBloodTestResult = recentBloodTestResults
          .get(bloodTestId);
      if (existingBloodTestResult == null) {
        recentBloodTestResults.put(bloodTestId, bt);
      } else if (existingBloodTestResult.getTestedOn().before(
          bt.getTestedOn())) {
        // before is very important here
        recentBloodTestResults.put(bloodTestId, bt);
      }
    }
    return recentBloodTestResults;
  }

  private BloodTest findBloodTestById(Long bloodTestId) {
    String queryStr = "SELECT bt FROM BloodTest bt WHERE "
        + "bt.id=:bloodTestId";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("bloodTestId", bloodTestId);
    return query.getSingleResult();
  }

  public void activateTests(BloodTestContext context) {
    String queryStr = "UPDATE BloodTest set isActive=:isActive WHERE context=:context";
    Query query = em.createQuery(queryStr);
    query.setParameter("isActive", true);
    query.setParameter("context", context);
    query.executeUpdate();
    queryStr = "UPDATE BloodTestingRule set isActive=:isActive WHERE context=:context";
    query = em.createQuery(queryStr);
    query.setParameter("isActive", true);
    query.setParameter("context", context);
    query.executeUpdate();
  }

  public void deactivateTests(BloodTestContext context) {
    String queryStr = "UPDATE BloodTest set isActive=:isActive WHERE context=:context";
    Query query = em.createQuery(queryStr);
    query.setParameter("isActive", false);
    query.setParameter("context", context);
    query.executeUpdate();
    queryStr = "UPDATE BloodTestingRule set isActive=:isActive WHERE context=:context";
    query = em.createQuery(queryStr);
    query.setParameter("isActive", false);
    query.setParameter("context", context);
    query.executeUpdate();
  }
  
  public List<BloodTestResultDTO> findTTIPrevalenceReportIndicators(Date startDate, Date endDate) {
    return em.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE,
        BloodTestResultDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("deleted", false)
        .setParameter("bloodTestType", BloodTestType.BASIC_TTI)
        .setParameter("result", "POS")
        .getResultList();
  }

  public Map<String, Map<Long, Long>> findNumberOfPositiveTests(
      List<String> ttiTests, Date donationDateFrom,
      Date donationDateTo, String aggregationCriteria,
      List<String> venues) throws ParseException {
    TypedQuery<Object[]> query = em
        .createQuery(
            "SELECT count(t), d.donationDate, t.bloodTest.testNameShort FROM BloodTestResult t join t.bloodTest bt join t.donation d WHERE "
                + "bt.id IN (:ttiTestIds) AND "
                + "t.result != :positiveResult AND "
                + "d.venue.id IN (:venueIds) AND "
                + "d.donationDate BETWEEN :donationDateFrom AND :donationDateTo "
                + "GROUP BY bt.testNameShort, d.donationDate",
            Object[].class);

    List<Long> venueIds = new ArrayList<Long>();
    if (venues != null) {
      for (String venue : venues) {
        venueIds.add(Long.parseLong(venue));
      }
    } else {
      venueIds.add((long) -1);
    }

    List<Long> ttiTestIds = new ArrayList<Long>();
    if (ttiTests != null) {
      for (String ttiTest : ttiTests) {
        ttiTestIds.add(Long.parseLong(ttiTest));
      }
    } else {
      ttiTestIds.add(-1l);
    }

    query.setParameter("venueIds", venueIds);
    query.setParameter("ttiTestIds", ttiTestIds);
    query.setParameter("positiveResult", "+");

    Map<String, Map<Long, Long>> resultMap = new HashMap<String, Map<Long, Long>>();
    TypedQuery<BloodTest> bloodTestQuery = em.createQuery(
        "SELECT t FROM BloodTest t WHERE t.id IN :ttiTestIds",
        BloodTest.class);
    bloodTestQuery.setParameter("ttiTestIds", ttiTestIds);
    for (BloodTest bt : bloodTestQuery.getResultList()) {
      resultMap.put(bt.getTestNameShort(), new HashMap<Long, Long>());
    }

    query.setParameter("donationDateFrom", donationDateFrom);
    query.setParameter("donationDateTo", donationDateTo);

    // decide date format based on aggregation criteria
    DateFormat resultDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    int incrementBy = Calendar.DAY_OF_YEAR;
    if (aggregationCriteria.equals("monthly")) {
      incrementBy = Calendar.MONTH;
      resultDateFormat = new SimpleDateFormat("01/MM/yyyy");
    } else if (aggregationCriteria.equals("yearly")) {
      incrementBy = Calendar.YEAR;
      resultDateFormat = new SimpleDateFormat("01/01/yyyy");
    }

    List<Object[]> resultList = query.getResultList();

    Calendar gcal = new GregorianCalendar();
    Date lowerDate = resultDateFormat.parse(resultDateFormat
        .format(donationDateFrom));
    Date upperDate = resultDateFormat.parse(resultDateFormat
        .format(donationDateTo));

    // initialize the counter map storing (date, count) for each blood test
    // counts should be set to 0
    for (String bloodTestName : resultMap.keySet()) {
      Map<Long, Long> m = resultMap.get(bloodTestName);
      gcal.setTime(lowerDate);
      while (gcal.getTime().before(upperDate)
          || gcal.getTime().equals(upperDate)) {
        m.put(gcal.getTime().getTime(), (long) 0);
        gcal.add(incrementBy, 1);
      }
    }

    for (Object[] result : resultList) {
      Date d = (Date) result[1];
      Date formattedDate = resultDateFormat.parse(resultDateFormat
          .format(d));
      Long utcTime = formattedDate.getTime();
      Map<Long, Long> m = resultMap.get(result[2]);
      if (m.containsKey(utcTime)) {
        Long newVal = m.get(utcTime) + (Long) result[0];
        m.put(utcTime, newVal);
      } else {
        m.put(utcTime, (Long) result[0]);
      }

    }
    return resultMap;
  }

  /**
   * Retrieve a full list of the active Blood Testing Rules.
   *
   * @return List<BloodTestingRule> list of rules, should not be null although this is not guaranteed
   */
  public List<BloodTestingRule> getActiveBloodTestingRules() {
    String queryStr = "SELECT r FROM BloodTestingRule r WHERE isActive=:isActive";
    TypedQuery<BloodTestingRule> query = em.createQuery(queryStr, BloodTestingRule.class);
    query.setParameter("isActive", true);
    return query.getResultList();
  }

  /**
   * Compare two strings and check that they are either both empty, or they are equal.
   * 
   * @param first First string
   * @param second First string
   * @return true if they are empty or equal, otherwise false.
   */
  private boolean bothEmptyOrEquals(String first, String second) {

    if (StringUtils.isEmpty(first)) {
      return StringUtils.isEmpty(second);
    }

    return first.equals(second);
  }

  /**
   * FIXME: this method belongs in the BloodTestsService and has replaced the BloodTestsUpdatedEvent
   * Because there are many references in this repository class, to minimise changes, it was added here.
   */
  public boolean updateDonationWithTestResults(Donation donation, BloodTestingRuleResult ruleResult) {
    boolean donationUpdated = false;

    String oldExtraInformation = donation.getExtraBloodTypeInformation();
    String newExtraInformation = addNewExtraInformation(oldExtraInformation, ruleResult.getExtraInformation());

    String oldBloodAbo = donation.getBloodAbo();
    String newBloodAbo = ruleResult.getBloodAbo();

    String oldBloodRh = donation.getBloodRh();
    String newBloodRh = ruleResult.getBloodRh();

    TTIStatus oldTtiStatus = donation.getTTIStatus();
    TTIStatus newTtiStatus = ruleResult.getTTIStatus();

    BloodTypingStatus oldBloodTypingStatus = donation.getBloodTypingStatus();
    BloodTypingStatus newBloodTypingStatus = ruleResult.getBloodTypingStatus();

    BloodTypingMatchStatus oldBloodTypingMatchStatus = donation.getBloodTypingMatchStatus();
    BloodTypingMatchStatus newBloodTypingMatchStatus = ruleResult.getBloodTypingMatchStatus();

    if (!bothEmptyOrEquals(newExtraInformation, oldExtraInformation) || !bothEmptyOrEquals(newBloodAbo, oldBloodAbo)
        || !bothEmptyOrEquals(newBloodRh, oldBloodRh) || !Objects.equals(newTtiStatus, oldTtiStatus)
        || !Objects.equals(newBloodTypingStatus, oldBloodTypingStatus)
        || !Objects.equals(oldBloodTypingMatchStatus, newBloodTypingMatchStatus)) {
      donation.setExtraBloodTypeInformation(newExtraInformation);
      donation.setBloodAbo(newBloodAbo);
      donation.setBloodRh(newBloodRh);
      donation.setTTIStatus(ruleResult.getTTIStatus());
      donation.setBloodTypingStatus(ruleResult.getBloodTypingStatus());
      donation.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());

      donationUpdated = true;
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Updating Donation '" + donation.getId() + "' with Abo/Rh="
          + donation.getBloodAbo() + donation.getBloodRh() + " TTIStatus="
          + donation.getTTIStatus() + " BloodTypingStatus=" + donation.getBloodTypingStatus()
          + " " + donation.getBloodTypingMatchStatus());
    }

    return donationUpdated;
  }

  /**
   * FIXME: this method also belongs in the BloodTestsService (see above updateDonationWithTestResults)
   */
  private String addNewExtraInformation(String donationExtraInformation, Set<String> extraInformationNewSet) {
    String newExtraInformation;
    Set<String> oldExtraInformationSet = new HashSet<String>();
    if (StringUtils.isNotBlank(donationExtraInformation)) {
      oldExtraInformationSet.addAll(Arrays.asList(donationExtraInformation.split(",")));
      extraInformationNewSet.removeAll(oldExtraInformationSet); // remove duplicates
      newExtraInformation = donationExtraInformation + StringUtils.join(extraInformationNewSet, ",");
    } else {
      newExtraInformation = StringUtils.join(extraInformationNewSet, ",");
    }
    return newExtraInformation;
  }
}
