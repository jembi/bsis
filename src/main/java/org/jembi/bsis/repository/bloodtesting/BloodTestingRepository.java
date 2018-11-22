package org.jembi.bsis.repository.bloodtesting;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestingRepository {

  private static final Logger LOGGER = Logger.getLogger(BloodTestingRepository.class);

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestRepository bloodTestRepository;

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
      Map<UUID, String> bloodTestResultsForDonation,
      Donation donation, Date testedOn,
      BloodTestingRuleResult ruleResult,
      boolean reEntry) {

    Map<UUID, BloodTestResult> mostRecentTestResults = getRecentTestResultsForDonation(donation.getId());
    for (UUID testId : bloodTestResultsForDonation.keySet()) {
      BloodTestResult btResult = mostRecentTestResults.get(testId);
      updateOrCreateBloodTestResult(btResult, testId, bloodTestResultsForDonation.get(testId), donation, testedOn, reEntry);
    }
    if (reEntry && ruleResult != null) {
      updateDonationWithTestResults(donation, ruleResult);
      em.persist(donation);
    }
  }

  private BloodTestResult updateOrCreateBloodTestResult(BloodTestResult btResult, UUID testId, String testResult,
      Donation donation, Date testedOn, boolean reEntry) {

    if (btResult == null) {
      btResult = new BloodTestResult();
      BloodTest bloodTest = bloodTestRepository.findBloodTestById(testId);
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

  public Map<UUID, BloodTestResult> getRecentTestResultsForDonation(
      UUID donationId) {
    String queryStr = "SELECT btr FROM BloodTestResult btr WHERE "
        + "btr.donation.id=:donationId AND btr.isDeleted = :testOutcomeDeleted "
        + "AND btr.bloodTest.isActive= :isActive AND btr.bloodTest.isDeleted= :isDeleted";
    TypedQuery<BloodTestResult> query = em.createQuery(queryStr,
        BloodTestResult.class);
    query.setParameter("donationId", donationId);
    query.setParameter("testOutcomeDeleted", false);
    query.setParameter("isActive",true);
    query.setParameter("isDeleted", false);
    List<BloodTestResult> bloodTestResults = query.getResultList();
    Map<UUID, BloodTestResult> recentBloodTestResults = new HashMap<>();
    for (BloodTestResult bt : bloodTestResults) {
      UUID bloodTestId = bt.getBloodTest().getId();
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

    Titre oldTitre = donation.getTitre();
    Titre newTitre = ruleResult.getTitre();

    if (!bothEmptyOrEquals(newBloodAbo, oldBloodAbo)
        || !bothEmptyOrEquals(newBloodRh, oldBloodRh) || !Objects.equals(newTtiStatus, oldTtiStatus)
        || !Objects.equals(newBloodTypingStatus, oldBloodTypingStatus)
        || !Objects.equals(oldBloodTypingMatchStatus, newBloodTypingMatchStatus)
        || !Objects.equals(oldTitre, newTitre)) {
      donation.setBloodAbo(newBloodAbo);
      donation.setBloodRh(newBloodRh);
      donation.setTTIStatus(ruleResult.getTTIStatus());
      donation.setBloodTypingStatus(ruleResult.getBloodTypingStatus());
      donation.setBloodTypingMatchStatus(ruleResult.getBloodTypingMatchStatus());
      donation.setTitre(ruleResult.getTitre());

      donationUpdated = true;
    }

    if (LOGGER.isInfoEnabled()) {
      LOGGER.info("Updating Donation '" + donation.getId() + "' with Abo/Rh="
          + donation.getBloodAbo() + donation.getBloodRh() + " TTIStatus="
          + donation.getTTIStatus() + " Titre=" + donation.getTitre() + " BloodTypingStatus=" 
          + donation.getBloodTypingStatus() + " " + donation.getBloodTypingMatchStatus());
    }

    return donationUpdated;
  }

}