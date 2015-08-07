package repository.bloodtesting;

import backingform.BloodTestBackingForm;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestContext;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.TSVFileHeaderName;
import model.bloodtesting.TTIStatus;
import model.bloodtesting.WellType;
import model.bloodtesting.rules.BloodTestSubCategory;
import model.bloodtesting.rules.BloodTestingRule;
import model.bloodtesting.rules.DonationField;
import model.donation.Donation;
import model.microtiterplate.MachineReading;
import model.microtiterplate.MicrotiterPlate;
import model.microtiterplate.PlateSession;
import model.user.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationRepository;
import repository.DonationBatchRepository;
import repository.GenericConfigRepository;
import repository.WellTypeRepository;
import repository.events.ApplicationContextProvider;
import repository.events.BloodTestsUpdatedEvent;
import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class BloodTestingRepository {

	private static final Logger LOGGER = Logger
			.getLogger(BloodTestingRepository.class);

	@PersistenceContext
	EntityManager em;

	@Autowired
	private DonationRepository donationRepository;
	
	@Autowired
	private DonationBatchRepository donationBatchRepository;	

	@Autowired
	private BloodTestingRuleEngine ruleEngine;

	@Autowired
	private WellTypeRepository wellTypeRepository;

	@Autowired
	private GenericConfigRepository genericConfigRepository;

	public MicrotiterPlate getPlate(String plateKey) {
		String queryStr = "SELECT p from MicrotiterPlate p "
				+ "WHERE p.plateKey=:plateKey";
		TypedQuery<MicrotiterPlate> query = em.createQuery(queryStr,
				MicrotiterPlate.class);
		query.setParameter("plateKey", plateKey);
		return query.getSingleResult();
	}

	public List<BloodTest> getBloodTypingTests() {
		String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.category=:category";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("isActive", true);
		query.setParameter("category", BloodTestCategory.BLOODTYPING);
		List<BloodTest> bloodTests = query.getResultList();
		return bloodTests;
	}

	public List<BloodTest> getBloodTTITests() {
		String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.category=:category";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("isActive", true);
		query.setParameter("category", BloodTestCategory.TTI);
		List<BloodTest> bloodTests = query.getResultList();
		return bloodTests;
	}

	public List<BloodTest> getBloodTestsOfType(BloodTestType type) {
		return getBloodTestsOfTypes(Arrays.asList(type));
	}

	public List<BloodTest> getBloodTestsOfTypes(List<BloodTestType> types) {
		String queryStr = "SELECT b FROM BloodTest b WHERE "
				+ "b.bloodTestType IN (:types) AND " + "b.isActive=:isActive";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("types", types);
		query.setParameter("isActive", true);
		List<BloodTest> bloodTests = query.getResultList();
		return bloodTests;
	}

	public Map<String, Object> saveBloodTestingResults(
			Map<Long, Map<Long, String>> bloodTestResultsMap,
			boolean saveIfUninterpretable) {

		Map<Long, Donation> donationsMap = new HashMap<Long, Donation>();
		Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForDonations = new HashMap<Long, BloodTestingRuleResult>();
		List<Long> donationsWithUninterpretableResults = new ArrayList<Long>();
		Date testedOn = new Date();
		Map<Long, Map<Long, String>> errorMap = validateTestResultValues(bloodTestResultsMap);
		if (errorMap.isEmpty()) {
			for (Long donationId : bloodTestResultsMap.keySet()) {
				Map<Long, String> bloodTestResultsForDonation = bloodTestResultsMap
						.get(donationId);
				Donation donation = donationRepository
						.findDonationById(donationId);
				BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
						donation, bloodTestResultsForDonation);
				donationsMap.put(donation.getId(),
						donation);
				bloodTestRuleResultsForDonations.put(donation.getId(),
						ruleResult);

				if (ruleResult.getAboUninterpretable()
						|| ruleResult.getRhUninterpretable()
						|| ruleResult.getTtiUninterpretable()) {
					if (saveIfUninterpretable) {
						saveBloodTestResultsToDatabase(
								bloodTestResultsForDonation, donation,
								testedOn, ruleResult);
					} else {
						Map<Long, String> uninterpretable = new HashMap<Long, String>();
						donationsWithUninterpretableResults.add(donationId);
						uninterpretable.put((long) -1,
								"Test results are uninterpretable");
						errorMap.put(donationId, uninterpretable);
					}
				} else {
					saveBloodTestResultsToDatabase(
							bloodTestResultsForDonation, donation,
							testedOn, ruleResult);
				}

			}
			em.flush();
		}

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("donations", donationsMap);
		results.put("bloodTestingResults", bloodTestRuleResultsForDonations);
		results.put("donationsWithUninterpretableResults",
				donationsWithUninterpretableResults);
		results.put("errors", errorMap);

		return results;
	}
	
	public Map<String, Object> saveBloodTestingResults(
			Long donationId,
			Map<Long, String> bloodTypingTestResults,
			boolean saveIfUninterpretable) {

		Donation donation = new Donation();
		BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
		Boolean uninterpretableResults = false;
		Date testedOn = new Date();
		Map<Long, String> errorMap = validateTestResultValues(donationId, bloodTypingTestResults);
		if (errorMap.isEmpty()) {
			Map<Long, String> bloodTestResultsForDonation = bloodTypingTestResults;
			donation = donationRepository
					.findDonationById(donationId);
			ruleResult = ruleEngine.applyBloodTests(
					donation, bloodTestResultsForDonation);

			if (ruleResult.getAboUninterpretable()
					|| ruleResult.getRhUninterpretable()
					|| ruleResult.getTtiUninterpretable()) {
				if (saveIfUninterpretable) {
					saveBloodTestResultsToDatabase(
							bloodTestResultsForDonation, donation,
							testedOn, ruleResult);
				} else {
					uninterpretableResults = true;
					errorMap.put(donationId, "Test results are uninterpretable");
				}
			} else {
				saveBloodTestResultsToDatabase(
						bloodTestResultsForDonation, donation,
						testedOn, ruleResult);
			}
			em.flush();
		}

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("donation", donation);
		results.put("bloodTestingResults", ruleResult);
		results.put("uninterpretableResults",
				uninterpretableResults);
		results.put("errors", errorMap);

		return results;
	}
	
	public Map<Long, String> validateTestResultValues(Long donationId,
			Map<Long, String> bloodTypingTestResults) {

		Map<String, BloodTest> allBloodTestsMap = new HashMap<String, BloodTest>();
		for (BloodTest bloodTypingTest : getAllBloodTests()) {
			allBloodTestsMap.put(bloodTypingTest.getId().toString(),
					bloodTypingTest);
		}

		Map<Long, String> errorMap = new HashMap<Long, String>();

		Map<Long, String> testsForDonation = bloodTypingTestResults;
		for (Long testId : testsForDonation.keySet()) {
			String result = testsForDonation.get(testId);
			BloodTest test = allBloodTestsMap.get(testId.toString());
			if (test == null) {
				addError(errorMap, donationId, testId,
						"Invalid test");
			}
			if (StringUtils.isBlank(result) && !test.getIsEmptyAllowed()) {
				addError(errorMap, donationId, testId,
						"No value specified");
			}
			List<String> validResults = Arrays.asList(test
					.getValidResults().split(","));
			if (!validResults.contains(result)) {
				addError(errorMap, donationId, testId,
						"Invalid value specified");
			}
		}

		return errorMap;
	}
	
	private void saveBloodTestResultsToDatabase(
			Map<Long, String> bloodTestResultsForDonation,
			Donation donation, Date testedOn,
			BloodTestingRuleResult ruleResult) {
		for (Long testId : bloodTestResultsForDonation.keySet()) {
			BloodTestResult btResult = new BloodTestResult();
			BloodTest bloodTest = new BloodTest();
			// the only reason we are using Long in the parameter is that
			// jsp uses Long for all numbers. Using an integer makes it
			// difficult
			// to compare Integer and Long values in the jsp conditionals
			// specially when iterating through the list of results
			bloodTest.setId(testId.intValue());
			btResult.setBloodTest(bloodTest);
			// not updating the inverse relation which means the
			// donation.getBloodTypingResults() will not
			// contain this result
			btResult.setDonation(donation);
			btResult.setTestedOn(testedOn);
			btResult.setNotes("");
			btResult.setResult(bloodTestResultsForDonation.get(testId));
			em.persist(btResult);
		}

		ApplicationContext applicationContext = ApplicationContextProvider
				.getApplicationContext();
		BloodTestsUpdatedEvent bloodTestsUpdatedEvent;
		bloodTestsUpdatedEvent = new BloodTestsUpdatedEvent("10",
				Arrays.asList(donation, ruleResult));
		bloodTestsUpdatedEvent.setDonation(donation);
		bloodTestsUpdatedEvent.setBloodTestingRuleResult(ruleResult);
		applicationContext.publishEvent(bloodTestsUpdatedEvent);
	}

	public Map<Long, Map<Long, String>> validateTestResultValues(
			Map<Long, Map<Long, String>> bloodTypingTestResults) {

		Map<String, BloodTest> allBloodTestsMap = new HashMap<String, BloodTest>();
		for (BloodTest bloodTypingTest : getAllBloodTests()) {
			allBloodTestsMap.put(bloodTypingTest.getId().toString(),
					bloodTypingTest);
		}

		Map<Long, Map<Long, String>> errorMap = new HashMap<Long, Map<Long, String>>();

		for (Long donationId : bloodTypingTestResults.keySet()) {
			Map<Long, String> testsForDonation = bloodTypingTestResults
					.get(donationId);
			for (Long testId : testsForDonation.keySet()) {
				String result = testsForDonation.get(testId);
				BloodTest test = allBloodTestsMap.get(testId.toString());
				if (test == null) {
					addErrorToMap(errorMap, donationId, testId,
							"Invalid test");
					continue;
				}
				if (StringUtils.isBlank(result) && !test.getIsEmptyAllowed()) {
					addErrorToMap(errorMap, donationId, testId,
							"No value specified");
				}
				List<String> validResults = Arrays.asList(test
						.getValidResults().split(","));
				if (!validResults.contains(result)) {
					addErrorToMap(errorMap, donationId, testId,
							"Invalid value specified");
				}
			}
		}

		return errorMap;
	}

	private boolean isResultValidForBloodTest(BloodTest test, String result) {
		if (StringUtils.isBlank(result)) {
			return false;
		}
		List<String> validResults = Arrays.asList(test.getValidResults().split(
				","));
		if (!validResults.contains(result)) {
			return false;
		}
		return true;
	}

	public List<BloodTest> getAllBloodTests() {
		String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("isActive", true);
		List<BloodTest> bloodTests = query.getResultList();
		return bloodTests;
	}

	public List<BloodTest> getAllBloodTestsIncludeInactive() {
		String queryStr = "SELECT b FROM BloodTest b";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		List<BloodTest> bloodTests = query.getResultList();
		return bloodTests;
	}

	private void addErrorToMap(Map<Long, Map<Long, String>> errorMap,
			Long donationId, Long testId, String errorMessage) {
		Map<Long, String> errorsForDonation = errorMap.get(donationId);
		if (errorsForDonation == null) {
			errorsForDonation = new HashMap<Long, String>();
			errorMap.put(donationId, errorsForDonation);
		}
		errorsForDonation.put(testId, errorMessage);
	}
	
	private void addError(Map<Long, String> errorMap,
			Long donationId, Long testId, String errorMessage) {
		if (errorMap == null) {
			errorMap = new HashMap<Long, String>();
		}
		errorMap.put(testId, errorMessage);
	}

	public Map<String, Object> getAllTestsStatusForDonations(
			List<String> donationIds) {
		// linked hashmap is required to ensure that results are returned in the
		// same order as inserted
		Map<Long, Donation> donationsMap = new LinkedHashMap<Long, Donation>();
		Map<Long, BloodTestingRuleResult> bloodTypingResultsForDonations = new LinkedHashMap<Long, BloodTestingRuleResult>();

		for (String donationIdStr : donationIds) {
			Long donationId = Long.parseLong(donationIdStr);
			Donation donation = donationRepository
					.findDonationById(donationId);
			BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
					donation, new HashMap<Long, String>());
			donationsMap.put(donation.getId(), donation);
			bloodTypingResultsForDonations.put(donation.getId(),
					ruleResult);
		}

		Map<String, Object> results = new HashMap<String, Object>();
		results.put("donations", donationsMap);
		results.put("bloodTestingResults", bloodTypingResultsForDonations);
		return results;
	}
	
	public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatches(
			List<Integer> donationBatchIds) {

		List<BloodTestingRuleResult> bloodTestingRuleResults = new ArrayList<BloodTestingRuleResult>();

		for (Integer donationBatchId : donationBatchIds) {
			List<Donation> donations = donationBatchRepository.findDonationsInBatch(donationBatchId);
			
			for (Donation donation : donations) {

			BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
					donation, new HashMap<Long, String>());
			bloodTestingRuleResults.add(ruleResult);
			}
		}
		
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
	
	public List<BloodTestResult> getBloodTestResultsForDonation(
			Long donationId) {
		String queryStr = "SELECT bt FROM BloodTestResult bt WHERE "
				+ "bt.donation.id=:donationId";
		TypedQuery<BloodTestResult> query = em.createQuery(queryStr,
				BloodTestResult.class);
		query.setParameter("donationId", donationId);
		List<BloodTestResult> bloodTestResults = query.getResultList();
		return bloodTestResults;
	}

	public Map<Integer, BloodTestResult> getRecentTestResultsForDonation(
			Long donationId) {
		String queryStr = "SELECT bt FROM BloodTestResult bt WHERE "
				+ "bt.donation.id=:donationId";
		TypedQuery<BloodTestResult> query = em.createQuery(queryStr,
				BloodTestResult.class);
		query.setParameter("donationId", donationId);
		List<BloodTestResult> bloodTestResults = query.getResultList();
		Map<Integer, BloodTestResult> recentBloodTestResults = new HashMap<Integer, BloodTestResult>();
		for (BloodTestResult bt : bloodTestResults) {
			Integer bloodTestId = bt.getBloodTest().getId();
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

	public BloodTest findBloodTestById(Integer bloodTestId) {
		String queryStr = "SELECT bt FROM BloodTest bt WHERE "
				+ "bt.id=:bloodTestId";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("bloodTestId", bloodTestId);
		return query.getSingleResult();
	}

	public BloodTest findBloodTestWithWorksheetTypesById(Integer bloodTestId) {
		String queryStr = "SELECT bt FROM BloodTest bt LEFT JOIN FETCH bt.worksheetTypes WHERE "
				+ "bt.id=:bloodTestId";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("bloodTestId", bloodTestId);
		return query.getSingleResult();
	}

	public Map<String, Object> saveTTIResultsOnPlate(
			Map<String, Map<String, Object>> ttiResultsMap, Long ttiTestId) {

		Map<String, Object> results = new HashMap<String, Object>();
		Map<String, List<String>> errorsByWellNumber = new HashMap<String, List<String>>();
		Map<String, Long> donationIdByWellNumber = new HashMap<String, Long>();
		Map<Long, String> errorsByDonationId = new HashMap<Long, String>();

		Map<Long, Donation> donationIdMap = new HashMap<Long, Donation>();

		Map<Long, MachineReading> machineReadingsForDonations = new HashMap<Long, MachineReading>();
		List<MachineReading> specialMachineReadings = new ArrayList<MachineReading>();

		BloodTest bloodTest = findBloodTestById(ttiTestId.intValue());

		PlateSession plateSession = new PlateSession();
		Date testedOn = new Date();
		plateSession.setPlateUsedOn(testedOn);

		Map<Long, Map<Long, String>> bloodTestResultsMap = new HashMap<Long, Map<Long, String>>();

		boolean errorsFound = false;

		for (String rowNum : ttiResultsMap.keySet()) {
			Map<String, Object> rowData = ttiResultsMap.get(rowNum);
			for (String colNum : rowData.keySet()) {

				String wellNumber = rowNum + "," + colNum;

				MachineReading machineReading = new MachineReading();
				@SuppressWarnings("unchecked")
				Map<String, String> wellData = (Map<String, String>) rowData
						.get(colNum);

				if (wellData.isEmpty())
					continue;

				// store well type in machine configuration
				Integer wellTypeId = Integer.parseInt(wellData.get("welltype"));
				WellType wellType = wellTypeRepository
						.getWellTypeById(wellTypeId);
				machineReading.setWellType(wellType);

				machineReading.setRowNumber(Integer.parseInt(rowNum));
				machineReading.setColumnNumber(Integer.parseInt(colNum));
				machineReading.setPlateSession(plateSession);
				try {
					if (StringUtils.isNotBlank(wellData.get("machineReading")))
						machineReading.setMachineReading(new BigDecimal(
								wellData.get("machineReading")));
				} catch (NumberFormatException ex) {

					LOGGER.error(ex.getMessage() + ex.getStackTrace());
					errorsFound = true;
					addErrorToWell(errorsByWellNumber, wellNumber,
							"Invalid value for machine reading");
				}

				if (wellType.getRequiresSample()) {
					String donationIdentificationNumber = wellData.get("donationIdentificationNumber");
					Donation donation = donationRepository
							.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
					if (donation == null) {
						addErrorToWell(errorsByWellNumber, wellNumber,
								"Invalid donation identification number");
						errorsFound = true;
					} else {
						String result = wellData.get("testResult");
						donationIdMap.put(donation.getId(), donation);
						if (bloodTestResultsMap.containsKey(donation.getId())) {
							errorsFound = true;
							addErrorToWell(errorsByWellNumber, wellNumber,
									"Duplicate donation identification number");
							errorsByDonationId.put(donation.getId(),
									"Duplicate donation identification number");
							donationIdByWellNumber.put(wellNumber,
									donation.getId());
						}
						if (!isResultValidForBloodTest(bloodTest, result)) {
							errorsFound = true;
							addErrorToWell(errorsByWellNumber, wellNumber,
									"Invalid test result specified");
						}
						Map<Long, String> resultsForDonation = new HashMap<Long, String>();
						resultsForDonation.put(ttiTestId, result);
						bloodTestResultsMap.put(donation.getId(),
								resultsForDonation);
						machineReadingsForDonations.put(donation.getId(),
								machineReading);
					}
				} else {
					specialMachineReadings.add(machineReading);
				}
			}
		}

		Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForDonations = new HashMap<Long, BloodTestingRuleResult>();

		if (!errorsFound) {
			em.persist(plateSession);
			// first determine whether there are any uninterpretable results
			for (Long donationId : donationIdMap.keySet()) {
				Map<Long, String> bloodTestResultsForDonation = bloodTestResultsMap
						.get(donationId);
				MachineReading machineReading = machineReadingsForDonations
						.get(donationId);
				Donation donation = donationIdMap
						.get(donationId);
				BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
						donation, bloodTestResultsForDonation);
				bloodTestRuleResultsForDonations
						.put(donationId, ruleResult);
				BloodTestResult btResult = saveBloodTestResultToDatabase(
						new Long(ttiTestId),
						bloodTestResultsForDonation.get(ttiTestId),
						donation, testedOn, ruleResult);
				// no need to worry about uninterpretable results here
				btResult.setMachineReading(machineReading);
				// bidirectional relationship with blood test result as the
				// owning side
				// must set in both directions and persist the machine reading
				// first
				// otherwise there will be a transient object in the entity
				// manager
				machineReading.setBloodTestResult(btResult);
				// must persist
				em.persist(machineReading);
				em.merge(btResult);
			}
		}

		results.put("donations", donationIdMap);
		results.put("bloodTestingResults", bloodTestRuleResultsForDonations);
		results.put("errorsFound", errorsFound);
		results.put("errorsByDonationId", errorsByDonationId);
		results.put("errorsByWellNumber", errorsByWellNumber);
		return results;
	}

	private void addErrorToWell(Map<String, List<String>> errorsByWellNumber,
			String wellNumber, String errorMessage) {
		if (!errorsByWellNumber.containsKey(wellNumber)) {
			errorsByWellNumber.put(wellNumber, new ArrayList<String>());
		}
		errorsByWellNumber.get(wellNumber).add(errorMessage);
	}

	private BloodTestResult saveBloodTestResultToDatabase(Long testId,
			String testResult, Donation donation, Date testedOn,
			BloodTestingRuleResult ruleResult) {

		BloodTestResult btResult = new BloodTestResult();
		BloodTest bloodTest = new BloodTest();
		// the only reason we are using Long in the parameter is that
		// jsp uses Long for all numbers. Using an integer makes it difficult
		// to compare Integer and Long values in the jsp conditionals
		// specially when iterating through the list of results
		bloodTest.setId(testId.intValue());
		btResult.setBloodTest(bloodTest);
		// not updating the inverse relation which means the
		// donation.getBloodTypingResults() will not
		// contain this result
		btResult.setDonation(donation);
		btResult.setTestedOn(testedOn);
		btResult.setNotes("");
		btResult.setResult(testResult);
		em.persist(btResult);
		em.refresh(btResult);
		ApplicationContext applicationContext = ApplicationContextProvider
				.getApplicationContext();
		BloodTestsUpdatedEvent bloodTestsUpdatedEvent;
		bloodTestsUpdatedEvent = new BloodTestsUpdatedEvent("10",
				Arrays.asList(donation, ruleResult));
		bloodTestsUpdatedEvent.setDonation(donation);
		bloodTestsUpdatedEvent.setBloodTestingRuleResult(ruleResult);
		applicationContext.publishEvent(bloodTestsUpdatedEvent);
		return btResult;
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

	public List<BloodTestingRule> getAllBloodTypingRules() {
		return getBloodTypingRules(false);
	}

	public List<BloodTestingRule> getBloodTypingRules(boolean onlyActiveRules) {
		String queryStr = "SELECT r FROM BloodTestingRule r WHERE r.category=:category AND "
				+ "r.context=:context";
		if (onlyActiveRules) {
			queryStr = queryStr + " AND r.isActive=:isActive";
		}
		TypedQuery<BloodTestingRule> query = em.createQuery(queryStr,
				BloodTestingRule.class);
		BloodTestContext context = genericConfigRepository
				.getCurrentBloodTypingContext();
		query.setParameter("category", BloodTestCategory.BLOODTYPING);
		query.setParameter("context", context);
		if (onlyActiveRules)
			query.setParameter("isActive", true);
		return query.getResultList();
	}

	public BloodTestingRule getBloodTestingRuleById(Integer ruleId) {
		String queryStr = "SELECT r from BloodTestingRule r WHERE r.id=:ruleId";
		TypedQuery<BloodTestingRule> query = em.createQuery(queryStr,
				BloodTestingRule.class);
		query.setParameter("ruleId", ruleId);
		return query.getSingleResult();
	}

     public BloodTestingRule saveBloodTypingRule(
            BloodTestingRule bloodTestingRule) {
         bloodTestingRule.setIsActive(Boolean.TRUE);
         em.persist(bloodTestingRule);
         return bloodTestingRule;
    }

    public BloodTestingRule updateBloodTypingRule(BloodTestingRule bloodTestingRule){
        return em.merge(bloodTestingRule);
    }

	public void deleteBloodTestingRule(Integer ruleId) {
		String queryStr = "UPDATE BloodTestingRule r SET isActive=:isActive WHERE r.id=:ruleId";
		Query query = em.createQuery(queryStr);
		query.setParameter("isActive", false);
		query.setParameter("ruleId", ruleId);
		query.executeUpdate();
		em.flush();
	}
        
	public void saveBloodTest(BloodTestBackingForm form) {
		BloodTest bt = form.getBloodTest();
		bt.setIsEmptyAllowed(false);
//		bt.setNegativeResults("");
//		bt.setPositiveResults("");
//		bt.setValidResults("+,-");
		bt.setRankInCategory(1);
		bt.setIsActive(true);
                BloodTestCategory category = bt.getCategory();
		if (category.equals(BloodTestCategory.BLOODTYPING)) {
			bt.setBloodTestType(BloodTestType.ADVANCED_BLOODTYPING);
			bt.setContext(genericConfigRepository
					.getCurrentBloodTypingContext());
			em.persist(bt);
		} else {
			bt.setBloodTestType(BloodTestType.BASIC_TTI);
			bt.setContext(BloodTestContext.RECORD_TTI_TESTS);
			Integer numConfirmtatoryTests = 0;
			if (form.getNumberOfConfirmatoryTests()!=null)
				numConfirmtatoryTests = form.getNumberOfConfirmatoryTests();
			List<Integer> pendingTestIds = new ArrayList<Integer>();
			em.persist(bt);
			em.refresh(bt);

			BloodTestingRule ttiSafeRule = new BloodTestingRule();
			ttiSafeRule.setBloodTestsIds("" + bt.getId());
			ttiSafeRule.setPattern("-");
			ttiSafeRule.setDonationFieldChanged(DonationField.TTISTATUS);
			ttiSafeRule.setNewInformation(TTIStatus.TTI_SAFE.toString());
			ttiSafeRule.setExtraInformation("");
			ttiSafeRule.setContext(BloodTestContext.RECORD_TTI_TESTS);
			ttiSafeRule.setCategory(BloodTestCategory.TTI);
			ttiSafeRule.setSubCategory(BloodTestSubCategory.TTI);
			ttiSafeRule.setPendingTestsIds("");
			ttiSafeRule.setMarkSampleAsUnsafe(false);
			ttiSafeRule.setIsActive(true);
			em.persist(ttiSafeRule);

			for (int i = 1; i <= numConfirmtatoryTests; ++i) {
				BloodTest confirmatoryBloodTest = new BloodTest();
				confirmatoryBloodTest.setTestName(bt.getTestName()
						+ " Confirmatory " + i);
				confirmatoryBloodTest.setTestNameShort(bt.getTestNameShort()
						+ " Conf " + i);
				confirmatoryBloodTest.setCategory(category);
				confirmatoryBloodTest
						.setBloodTestType(BloodTestType.CONFIRMATORY_TTI);
				confirmatoryBloodTest
						.setContext(BloodTestContext.RECORD_TTI_TESTS);
				confirmatoryBloodTest.setIsEmptyAllowed(false);
				confirmatoryBloodTest.setNegativeResults("");
				confirmatoryBloodTest.setPositiveResults("");
				confirmatoryBloodTest.setValidResults("+,-");
				confirmatoryBloodTest.setRankInCategory(1);
				confirmatoryBloodTest.setIsActive(true);
				em.persist(confirmatoryBloodTest);
				em.refresh(confirmatoryBloodTest);
				pendingTestIds.add(i);

				BloodTestingRule confirmatoryTestRule = new BloodTestingRule();
				confirmatoryTestRule.setBloodTestsIds(""
						+ confirmatoryBloodTest.getId());
				confirmatoryTestRule.setPattern("+");
				confirmatoryTestRule
						.setDonationFieldChanged(DonationField.TTISTATUS);
				confirmatoryTestRule.setNewInformation(TTIStatus.TTI_UNSAFE
						.toString());
				confirmatoryTestRule.setExtraInformation("");
				confirmatoryTestRule
						.setContext(BloodTestContext.RECORD_TTI_TESTS);
				confirmatoryTestRule.setCategory(BloodTestCategory.TTI);
				confirmatoryTestRule.setSubCategory(BloodTestSubCategory.TTI);
				confirmatoryTestRule.setPendingTestsIds("");
				confirmatoryTestRule.setMarkSampleAsUnsafe(false);
				confirmatoryTestRule.setIsActive(true);
				em.persist(confirmatoryTestRule);
			}

			BloodTestingRule ttiUnsafeRule = new BloodTestingRule();
			ttiUnsafeRule.setBloodTestsIds("" + bt.getId());
			ttiUnsafeRule.setPattern("+");
			ttiUnsafeRule.setDonationFieldChanged(DonationField.TTISTATUS);
			ttiUnsafeRule.setNewInformation(TTIStatus.TTI_UNSAFE.toString());
			ttiUnsafeRule.setExtraInformation("");
			ttiUnsafeRule.setContext(BloodTestContext.RECORD_TTI_TESTS);
			ttiUnsafeRule.setCategory(BloodTestCategory.TTI);
			ttiUnsafeRule.setSubCategory(BloodTestSubCategory.TTI);
			ttiUnsafeRule.setPendingTestsIds(StringUtils.join(pendingTestIds,
					","));
			ttiUnsafeRule.setMarkSampleAsUnsafe(false);
			ttiUnsafeRule.setIsActive(true);
			em.persist(ttiUnsafeRule);
		}
	}
        
        /**
         * TODO - To be improved
         * issue - #225
         */
        public BloodTest updateBloodTest(BloodTestBackingForm backingObject){
            return em.merge(backingObject.getBloodTest());
        }


	public void deactivateBloodTest(Integer bloodTestId) {
		BloodTest bt = findBloodTestById(bloodTestId);
		bt.setIsActive(false);
		em.merge(bt);
	}

	public void activateBloodTest(Integer bloodTestId) {
		BloodTest bt = findBloodTestById(bloodTestId);
		bt.setIsActive(true);
		em.merge(bt);
	}
        
       

	public List<BloodTestingRule> getTTIRules(boolean onlyActiveRules) {
		String queryStr = "SELECT r FROM BloodTestingRule r WHERE r.category=:category";
		if (onlyActiveRules) {
			queryStr = queryStr + " AND r.isActive=:isActive";
		}
		TypedQuery<BloodTestingRule> query = em.createQuery(queryStr,
				BloodTestingRule.class);
		query.setParameter("category", BloodTestCategory.TTI);
		if (onlyActiveRules)
			query.setParameter("isActive", true);
		return query.getResultList();
	}

	public Map<String, Map<Long, Long>> findNumberOfPositiveTests(
			List<String> ttiTests, Date donationDateFrom,
			Date donationDateTo, String aggregationCriteria,
			List<String> panels) throws ParseException {
		TypedQuery<Object[]> query = em
				.createQuery(
						"SELECT count(t), d.donationDate, t.bloodTest.testNameShort FROM BloodTestResult t join t.bloodTest bt join t.donation d WHERE "
								+ "bt.id IN (:ttiTestIds) AND "
								+ "t.result != :positiveResult AND "
								+ "d.donorPanel.id IN (:panelIds) AND "
								+ "d.donationDate BETWEEN :donationDateFrom AND :donationDateTo "
								+ "GROUP BY bt.testNameShort, d.donationDate",
						Object[].class);

		List<Long> panelIds = new ArrayList<Long>();
	    if (panels != null) {
	      for (String panel : panels) {
	    	panelIds.add(Long.parseLong(panel));
	      }
	    } else {
	      panelIds.add((long)-1);
	    }

		List<Integer> ttiTestIds = new ArrayList<Integer>();
		if (ttiTests != null) {
			for (String ttiTest : ttiTests) {
				ttiTestIds.add(Integer.parseInt(ttiTest));
			}
		} else {
			ttiTestIds.add(-1);
		}

		query.setParameter("panelIds", panelIds);
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
		Date lowerDate =  resultDateFormat.parse(resultDateFormat
					.format(donationDateFrom));
		Date upperDate =  resultDateFormat.parse(resultDateFormat
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
				System.out.println(formattedDate);
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

	public List<BloodTest> getBasicTTITests() {
		String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.bloodTestType=:bloodTestType AND b.category=:category";
		TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
		query.setParameter("isActive", true);
		query.setParameter("bloodTestType", BloodTestType.BASIC_TTI);
		query.setParameter("category", BloodTestCategory.TTI);
		List<BloodTest> bloodTests = query.getResultList();
		return bloodTests;
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

	public User getUser(Integer id) {
		String queryStr = "SELECT u FROM User u WHERE u.id=:id";
		TypedQuery<User> query = em.createQuery(queryStr, User.class);
		query.setParameter("id", id);
		User user = query.getSingleResult();
		return user;
	}

	public void saveTestResultsToDatabase(
			List<TSVFileHeaderName> tSVFileHeaderNameList) {
		for (TSVFileHeaderName ts : tSVFileHeaderNameList) {
			
			Donation cs = donationRepository
					.findDonationByDonationIdentificationNumber(ts.getSID());
			if (cs != null){
				
				try{
					
					Map<Long, Map<Long, String>> bloodTestResultsMap = new HashMap<Long, Map<Long, String>>();
					Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForDonations = new HashMap<Long, BloodTestingRuleResult>();
	
					BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
							cs,	new HashMap<Long, String>());
					bloodTestRuleResultsForDonations.put(cs.getId(), ruleResult);
					
					saveBloodTestResultToDatabase(Long.valueOf(ts.getAssayNumber()),
							ts.getInterpretation(), cs, ts.getCompleted(), ruleResult);
				
				}
				catch(Exception ex){
					System.out.println("Cannot save TTI Test Result to DB");
			    	ex.printStackTrace();
				}
				
			}
			
		}
	}
}
