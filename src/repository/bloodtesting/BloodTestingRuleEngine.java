package repository.bloodtesting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.rules.BloodTestRule;
import model.collectedsample.CollectedSample;
import model.testresults.TTIStatus;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class BloodTestingRuleEngine {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private BloodTestingRepository bloodTestingRepository;
  
  /**
   * Apply blood typing rules to blood typing tests (combination of what is present in the database and
   * those passed as parameter.
   * @param collectedSample Blood Typing results for which collection
   * @param bloodTestResults map of blood typing test id to result. Only character allowed in the result.
   *                               multiple characters should be mapped to negative/positive (TODO)
   *                               Assume validation of results already done.
   * @return Result of applying the rules. The following values should be present in the map
   *         bloodAbo (what changes should be made to blood abo after applying these rules),\
   *         bloodRh (what changes should be made to blood rh),
   *         extra (extra information that should be added to the blood type like weak A),
   *         pendingTests (comma separated list of blood typing tests that must be done to
   *                       determine the blood type),
   *         testResults (map of blood typing test id to blood typing test either stored or
   *                      those passed to this function or those already stored in the database),
   *         bloodTypingStatus (enum BloodTypingStatus indicates if complete typing information is available),
   *         storedTestResults (what blood typing results are actually stored in the database,
   *                            a subset of testResults)
   */
  public BloodTestingRuleResult applyBloodTests(CollectedSample collectedSample, Map<Long, String> bloodTestResults) {

    String queryStr = "SELECT r FROM BloodTestRule r WHERE isActive=:isActive";
    TypedQuery<BloodTestRule> query = em.createQuery(queryStr, BloodTestRule.class);

    query.setParameter("isActive", true);
    List<BloodTestRule> rules = query.getResultList();

    Map<String, String> storedTestResults = new TreeMap<String, String>();

    for (BloodTestResult t : collectedSample.getBloodTestResults()) {
      storedTestResults.put(t.getBloodTest().getId().toString(), t.getResult());
    }

    Map<String, String> availableTestResults = new TreeMap<String, String>();
    availableTestResults.putAll(storedTestResults);
    for (Long extraTestId : bloodTestResults.keySet()) {
      // for rule comparison we are overwriting existing test results with new test results
      availableTestResults.put(extraTestId.toString(), bloodTestResults.get(extraTestId));
    }

    System.out.println("available test results:" + availableTestResults);

    Set<String> bloodAboChanges = new HashSet<String>();
    Set<String> bloodRhChanges = new HashSet<String>();
    Set<String> ttiStatusChanges = new HashSet<String>();
    Set<String> extraInformation = new HashSet<String>();
    List<String> pendingTestsIds = new ArrayList<String>();

    for (BloodTestRule rule : rules) {

      String pattern = rule.getPattern();
      boolean patternMatch = true;
      int indexInPattern = 0;

      List<String> missingTestIdsForRule = new ArrayList<String>();
      List<String> testIds = Arrays.asList(rule.getBloodTestsIds().split(","));

      String inputPattern = "";
      for (String testId : testIds) {
        indexInPattern = indexInPattern+1;
        String actualResult = availableTestResults.get(testId); 
        if (actualResult == null) {
          missingTestIdsForRule.add(testId);
          inputPattern += "?";
          patternMatch = false;
          continue;
        }
        String expectedResult = pattern.substring(indexInPattern-1,indexInPattern);
        inputPattern += actualResult;
        if (!expectedResult.equals(actualResult)) {
          patternMatch = false;
        }
      }

//      System.out.println();
      if (patternMatch) {
        System.out.println("Pattern matched for rule.");
//        System.out.println("test ids: " + rule.getBloodTypingTestIds() + ", " +
//        		               "pattern: " + rule.getPattern() + ", " +
//        		               "input pattern: " + inputPattern);
//        System.out.println("extra tests to be done: " + rule.getExtraTestsIds());
//        System.out.println("Changes to result: " +
//                            rule.getPart() + ", " + rule.getNewInformation() + ", " +
//                            rule.getExtraInformation() + ", " + rule.getMarkSampleAsUnsafe());
//
        switch (rule.getCollectionFieldChanged()) {
          case BLOODABO:
            bloodAboChanges.add(rule.getNewInformation());
            break;
          case BLOODRH:
            bloodRhChanges.add(rule.getNewInformation());
            break;
          case TTISTATUS:
            ttiStatusChanges.add(rule.getNewInformation());
            break;
          case EXTRA:
            extraInformation.add(rule.getNewInformation());
            break;
        }

        if (StringUtils.isNotBlank(rule.getExtraInformation())) 
          extraInformation.add(rule.getExtraInformation());

        // find extra tests for ABO
        if (StringUtils.isNotBlank(rule.getExtraTestsIds())) {
          for (String extraTestId : rule.getExtraTestsIds().split(",")) {
            if (!availableTestResults.containsKey(extraTestId)) {
              pendingTestsIds.add(extraTestId);
            }
          }
        }
      } else {
//        System.out.println("Pattern did not match");
//        System.out.println("test ids: " + rule.getBloodTypingTestIds() + ", " +
//            "pattern: " + rule.getPattern() + ", " +
//            "input pattern: " + inputPattern);
//        System.out.println("Missing tests: " + missingTestIdsForRule);
      }
    }

    String bloodAbo = "";
    String bloodRh = "";

    BloodTypingStatus bloodTypingStatus = BloodTypingStatus.NOT_DONE;

    int numBloodTypingTests = 0;
    for (BloodTest bt : bloodTestingRepository.getBloodTypingTests()) {
      if (availableTestResults.containsKey((bt.getId().toString())))
          numBloodTypingTests++;
    }

    // if even one blood typing test is done then we cannot say
    // blood typing status is NOT_DONE
    if (numBloodTypingTests > 0)
      bloodTypingStatus = BloodTypingStatus.NO_MATCH;

    if (bloodAboChanges.size() > 1 || bloodRhChanges.size() > 1) {
      bloodTypingStatus = BloodTypingStatus.AMBIGUOUS;
    }
    else {
      if (bloodAboChanges.size() == 1)
        bloodAbo = bloodAboChanges.iterator().next();
      if (bloodRhChanges.size() == 1)
        bloodRh = bloodRhChanges.iterator().next();
    }
    if (bloodAboChanges.isEmpty() || bloodRhChanges.isEmpty()) {
      if (pendingTestsIds.size() > 0)
        bloodTypingStatus = BloodTypingStatus.PENDING_TESTS;
    }
    if (bloodAboChanges.size() == 1 && bloodRhChanges.size() == 1) {
      bloodTypingStatus = BloodTypingStatus.COMPLETE;
    }

    System.out.println(ttiStatusChanges);
    TTIStatus ttiStatus = TTIStatus.NOT_DONE;
    if (!ttiStatusChanges.isEmpty()) {
      if (ttiStatusChanges.contains(TTIStatus.TTI_UNSAFE.toString())) {
        ttiStatus = TTIStatus.TTI_UNSAFE;
      }
      else if (ttiStatusChanges.size() == 1 &&
               ttiStatusChanges.contains(TTIStatus.TTI_SAFE.toString())) {
        ttiStatus = TTIStatus.TTI_SAFE;
      }
    }

    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setAllBloodAboChanges(bloodAboChanges);
    ruleResult.setAllBloodRhChanges(bloodRhChanges);
    ruleResult.setBloodAbo(bloodAbo);
    ruleResult.setBloodRh(bloodRh);
    ruleResult.setExtraInformation(extraInformation);
    ruleResult.setPendingTestsIds(pendingTestsIds);
    ruleResult.setAvailableTestResults(availableTestResults);
    ruleResult.setBloodTypingStatus(bloodTypingStatus);
    ruleResult.setStoredTestResults(storedTestResults);

    ruleResult.setTTIStatusChanges(ttiStatusChanges);
    ruleResult.setTTIStatus(ttiStatus);

    return ruleResult;
  }

}
