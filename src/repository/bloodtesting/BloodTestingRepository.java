package repository.bloodtesting;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.BloodTestResult;
import model.collectedsample.CollectedSample;
import model.microtiterplate.MicrotiterPlate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.CollectedSampleRepository;
import viewmodel.BloodTestingRuleResult;

@Repository
@Transactional
public class BloodTestingRepository {

  @PersistenceContext
  EntityManager em;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  @Autowired
  private BloodTestingRuleEngine ruleEngine;

  public MicrotiterPlate getPlate(String plateKey) {
    String queryStr = "SELECT p from MicrotiterPlate p " +
    		"WHERE p.plateKey=:plateKey";
    TypedQuery<MicrotiterPlate> query = em.createQuery(queryStr, MicrotiterPlate.class);
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
    String queryStr = "SELECT b FROM BloodTest b WHERE " +
        "b.bloodTestType IN (:types) AND " +
    		"b.isActive=:isActive";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("types", types);
    query.setParameter("isActive", true);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public Map<String, Object> saveBloodTestingResults(
      Map<Long, Map<Long, String>> bloodTestResultsMap) {

    Map<Long, CollectedSample> collectedSamplesMap = new HashMap<Long, CollectedSample>();
    Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForCollections = new HashMap<Long, BloodTestingRuleResult>(); 
    Date testedOn = new Date();
    Map<Long, Map<Long, String>> errorMap = validateTestResultValues(bloodTestResultsMap);
    if (errorMap.isEmpty()) {
      for (Long collectionId : bloodTestResultsMap.keySet()) {
        Map<Long, String> bloodTestResultsForCollection = bloodTestResultsMap.get(collectionId);
        CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
        BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(collectedSample, bloodTestResultsForCollection);
        collectedSamplesMap.put(collectedSample.getId(), collectedSample);
        bloodTestRuleResultsForCollections.put(collectedSample.getId(), ruleResult);

        collectedSample = updateCollectionStatus(collectedSample, ruleResult);

        for (Long testId : bloodTestResultsForCollection.keySet()) {
          BloodTestResult btResult = new BloodTestResult();
          BloodTest bloodTest = new BloodTest();
          // the only reason we are using Long in the parameter is that
          // jsp uses Long for all numbers. Using an integer makes it difficult
          // to compare Integer and Long values in the jsp conditionals
          // specially when iterating through the list of results
          bloodTest.setId(testId.intValue());
          btResult.setBloodTest(bloodTest);
          // not updating the inverse relation which means the
          // collectedSample.getBloodTypingResults() will not
          // contain this result
          btResult.setCollectedSample(collectedSample);
          btResult.setTestedOn(testedOn);
          btResult.setNotes("");
          btResult.setResult(bloodTestResultsForCollection.get(testId));
          em.persist(btResult);
        }
      }
      em.flush();
    }

    Map<String, Object> results = new HashMap<String, Object>();
    results.put("collections", collectedSamplesMap);
    results.put("bloodTestingResults", bloodTestRuleResultsForCollections);
    results.put("errors", errorMap);
    return results;
  }

  private CollectedSample updateCollectionStatus(
      CollectedSample collection, BloodTestingRuleResult ruleResult) {

    String bloodAboNew = ruleResult.getBloodAbo();
    String bloodRhNew = ruleResult.getBloodRh();
    Set<String> extraInformationNew = ruleResult.getExtraInformation();

    String extraInformation = collection.getExtraBloodTypeInformation();

    Set<String> extraInformationOld = new HashSet<String>();
    if (StringUtils.isNotBlank(extraInformation)) {
      extraInformationOld.addAll(Arrays.asList(extraInformation.split(",")));
      // extra information is a field to which we add more information
      // do not store duplicate information in this field
      extraInformationNew.removeAll(extraInformationOld);
      collection.setExtraBloodTypeInformation(extraInformation + StringUtils.join(extraInformationNew, ","));
    }
    else {
      collection.setExtraBloodTypeInformation(StringUtils.join(extraInformationNew, ","));
    }

    collection.setBloodAbo(bloodAboNew);
    collection.setBloodRh(bloodRhNew);

    collection.setTTIStatus(ruleResult.getTTIStatus());

    collection.setBloodTypingStatus(ruleResult.getBloodTypingStatus());

    collection = em.merge(collection);

    return collection;
  }

  public Map<Long, Map<Long, String>> validateTestResultValues(Map<Long, Map<Long, String>> bloodTypingTestResults) {

    Map<String, BloodTest> allBloodTestsMap = new HashMap<String, BloodTest>(); 
    for (BloodTest bloodTypingTest : getAllBloodTests()) {
      allBloodTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }

    Map<Long, Map<Long,String>> errorMap = new HashMap<Long, Map<Long,String>>();

    for (Long collectionId : bloodTypingTestResults.keySet()) {
      Map<Long, String> testsForCollection = bloodTypingTestResults.get(collectionId);
      for (Long testId : testsForCollection.keySet()) {
        String result = testsForCollection.get(testId);
        BloodTest test = allBloodTestsMap.get(testId.toString());
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

  private List<BloodTest> getAllBloodTests() {
    String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("isActive", true);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
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

  public Map<String, Object> getAllTestsStatusForCollections(List<String> collectionIds) {
    // linked hashmap is required to ensure that results are returned in the same order as inserted
    Map<Long, CollectedSample> collectedSamplesMap = new LinkedHashMap<Long, CollectedSample>();
    Map<Long, BloodTestingRuleResult> bloodTypingResultsForCollections = new LinkedHashMap<Long, BloodTestingRuleResult>(); 

    for (String collectionIdStr : collectionIds) {
      Long collectionId = Long.parseLong(collectionIdStr);
      CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
      BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(collectedSample, new HashMap<Long, String>());
      collectedSamplesMap.put(collectedSample.getId(), collectedSample);
      bloodTypingResultsForCollections.put(collectedSample.getId(), ruleResult);
    }

    Map<String, Object> results = new HashMap<String, Object>();
    results.put("collections", collectedSamplesMap);
    results.put("bloodTestingResults", bloodTypingResultsForCollections);
    return results;
  }

  public BloodTestingRuleResult getAllTestsStatusForCollection(Long collectionId) {
    CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
    return ruleEngine.applyBloodTests(collectedSample, new HashMap<Long, String>());
  }

  public List<BloodTest> getTTITests() {
    String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.category=:category";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("isActive", true);
    query.setParameter("category", BloodTestCategory.TTI);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public Map<Integer, BloodTestResult> getRecentTestResultsForCollection(Long collectedSampleId) {
    String queryStr = "SELECT bt FROM BloodTestResult bt WHERE " +
    		"bt.collectedSample.id=:collectedSampleId";
    TypedQuery<BloodTestResult> query = em.createQuery(queryStr, BloodTestResult.class);
    query.setParameter("collectedSampleId", collectedSampleId);
    List<BloodTestResult> bloodTestResults = query.getResultList();
    Map<Integer, BloodTestResult> recentBloodTestResults = new HashMap<Integer, BloodTestResult>();
    for (BloodTestResult bt : bloodTestResults) {
      Integer bloodTestId = bt.getBloodTest().getId();
      BloodTestResult existingBloodTestResult = recentBloodTestResults.get(bloodTestId);
      if (existingBloodTestResult == null) {
        recentBloodTestResults.put(bloodTestId, bt);
      } else if (existingBloodTestResult.getTestedOn().before(bt.getTestedOn())) {
        // before is very important here
          recentBloodTestResults.put(bloodTestId, bt);
      }
    }
    return recentBloodTestResults;
  }
}
