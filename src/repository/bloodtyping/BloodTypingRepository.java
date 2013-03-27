package repository.bloodtyping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtyping.BloodTypingTest;
import model.bloodtyping.BloodTypingTestType;
import model.collectedsample.CollectedSample;
import model.microtiterplate.MicrotiterPlate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.CollectedSampleRepository;
import viewmodel.BloodTypingRuleResult;

@Repository
@Transactional
public class BloodTypingRepository {

  @PersistenceContext
  EntityManager em;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private BloodTypingRuleEngine ruleEngine;

  public MicrotiterPlate getPlate(String plateKey) {
    String queryStr = "SELECT p from MicrotiterPlate p " +
    		"WHERE p.plateKey=:plateKey";
    TypedQuery<MicrotiterPlate> query = em.createQuery(queryStr, MicrotiterPlate.class);
    query.setParameter("plateKey", plateKey);
    return query.getSingleResult();
  }

  public List<BloodTypingTest> getBloodTypingTests() {
    String queryStr = "SELECT b FROM BloodTypingTest b WHERE b.isActive=:isActive";
    TypedQuery<BloodTypingTest> query = em.createQuery(queryStr, BloodTypingTest.class);
    query.setParameter("isActive", true);
    List<BloodTypingTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public List<BloodTypingTest> getBloodTypingTestsOfType(BloodTypingTestType type) {
    return getBloodTypingTestsOfTypes(Arrays.asList(type));
  }

  public List<BloodTypingTest> getBloodTypingTestsOfTypes(List<BloodTypingTestType> types) {
    String queryStr = "SELECT b FROM BloodTypingTest b WHERE " +
        "b.bloodTypingTestType IN (:types) AND " +
    		"b.isActive=:isActive";
    TypedQuery<BloodTypingTest> query = em.createQuery(queryStr, BloodTypingTest.class);
    query.setParameter("types", types);
    query.setParameter("isActive", true);
    List<BloodTypingTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public Map<String, Object> saveBloodTypingResults(
      Map<Long, Map<Long, String>> bloodTypingTestResultsMap) {

    Map<Long, CollectedSample> collectedSamplesMap = new HashMap<Long, CollectedSample>();
    Map<Long, BloodTypingRuleResult> bloodTypingResultsForCollections = new HashMap<Long, BloodTypingRuleResult>(); 

    Map<Long, Map<Long, String>> errorMap = validateValuesInWells(bloodTypingTestResultsMap);
    for (Long collectionId : bloodTypingTestResultsMap.keySet()) {
      Map<Long, String> bloodTypingTestResults = bloodTypingTestResultsMap.get(collectionId);
      CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
      BloodTypingRuleResult ruleResult = ruleEngine.applyBloodTypingTests(collectedSample, bloodTypingTestResults);
      collectedSamplesMap.put(collectedSample.getId(), collectedSample);
      bloodTypingResultsForCollections.put(collectedSample.getId(), ruleResult);
    }

    Map<String, Object> results = new HashMap<String, Object>();
    results.put("collections", collectedSamplesMap);
    results.put("bloodTypingResults", bloodTypingResultsForCollections);
    results.put("errors", errorMap);
    return results;
  }

  public Map<Long, Map<Long, String>> validateValuesInWells(Map<Long, Map<Long, String>> bloodTypingTestResults) {

    Map<String, BloodTypingTest> allBloodTypingTestsMap = new HashMap<String, BloodTypingTest>(); 
    for (BloodTypingTest bloodTypingTest : getBloodTypingTests()) {
      allBloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }

    Map<Long, Map<Long,String>> errorMap = new HashMap<Long, Map<Long,String>>();

    for (Long collectionId : bloodTypingTestResults.keySet()) {
      Map<Long, String> testsForCollection = bloodTypingTestResults.get(collectionId);
      for (Long testId : testsForCollection.keySet()) {
        String result = testsForCollection.get(testId);
        BloodTypingTest test = allBloodTypingTestsMap.get(testId.toString());
        if (test == null) {
          addErrorToMap(errorMap, collectionId, testId, "Invalid test");
          continue;
        }
        if (StringUtils.isBlank(result) && !test.getIsEmptyAllowed()) {
          addErrorToMap(errorMap, collectionId, testId, "No value specified");
        }
        List<String> validResults = Arrays.asList(test.getValidResults().split(","));
        if (!validResults.contains(result)) {
          addErrorToMap(errorMap, collectionId, testId, "Invalid value specified");
        }
      }
    }

    return errorMap;
  }

  private void addErrorToMap(Map<Long, Map<Long, String>> errorMap,
      Long collectionId, Long testId, String errorMessage) {
    Map<Long, String> errorsForCollection = errorMap.get(collectionId);
    if (errorsForCollection == null) {
      errorsForCollection = new HashMap<Long, String>();
      errorMap.put(collectionId, errorsForCollection);
    }
    errorsForCollection.put(testId, errorMessage);
  }
}
