package repository.bloodtyping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtyping.BloodTypingTest;
import model.bloodtyping.BloodTypingTestResult;
import model.bloodtyping.rules.BloodTypingRule;
import model.collectedsample.CollectedSample;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTypingRuleEngine {

  @PersistenceContext
  private EntityManager em;

  public List<BloodTypingTest> applyBloodTypingTests(CollectedSample collectedSample, Map<String, String> bloodTypingTestResults) {

    List<BloodTypingTest> pendingTests = new ArrayList<BloodTypingTest>();

    String queryStr = "SELECT r FROM BloodTypingRule r WHERE isActive=:isActive";
    TypedQuery<BloodTypingRule> query = em.createQuery(queryStr, BloodTypingRule.class);

    query.setParameter("isActive", true);
    List<BloodTypingRule> rules = query.getResultList();

    Map<String, String> availableTests = new HashMap<String, String>();

    for (BloodTypingTestResult t : collectedSample.getBloodTypingTestResults()) {
      availableTests.put(t.getId().toString(), t.getResult());
    }

    for (String extraTestId : bloodTypingTestResults.keySet()) {
      // for rule comparison we are overwriting existing test results with new test results
      availableTests.put(extraTestId, bloodTypingTestResults.get(extraTestId));
    }

    System.out.println(availableTests);

    for (BloodTypingRule rule : rules) {

      String pattern = rule.getPattern();
      boolean patternMatch = true;
      int indexInPattern = 0;

      List<String> missingTestIdsForRule = new ArrayList<String>();
      List<String> testIds = Arrays.asList(rule.getBloodTypingTestIds().split(","));

      String inputPattern = "";
      for (String testId : testIds) {
        indexInPattern = indexInPattern+1;
        String actualResult = availableTests.get(testId); 
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

      System.out.println();
      if (patternMatch) {
        System.out.println("Pattern matched for rule.");
        System.out.println("test ids: " + rule.getBloodTypingTestIds() + ", " +
        		               "pattern: " + rule.getPattern() + ", " +
        		               "input pattern: " + inputPattern);
        System.out.println("extra tests to be done: " + rule.getExtraTestsIds());
        System.out.println("Changes to result: " +
                            rule.getPart() + ", " + rule.getNewInformation() + ", " +
                            rule.getExtraInformation() + ", " + rule.getMarkSampleAsUnsafe());
      } else {
        System.out.println("Pattern did not match");
        System.out.println("test ids: " + rule.getBloodTypingTestIds() + ", " +
            "pattern: " + rule.getPattern() + ", " +
            "input pattern: " + inputPattern);
        System.out.println("Missing tests: " + missingTestIdsForRule);
      }
    }

    return pendingTests;
  }

}
