package repository.bloodtesting;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import model.bloodtesting.WellType;
import model.collectedsample.CollectedSample;
import model.microtiterplate.MachineReading;
import model.microtiterplate.MicrotiterPlate;
import model.microtiterplate.PlateSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.CollectedSampleRepository;
import repository.WellTypeRepository;
import repository.events.ApplicationContextProvider;
import repository.events.BloodTestsUpdatedEvent;
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

  @Autowired
  private WellTypeRepository wellTypeRepository;

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
      Map<Long, Map<Long, String>> bloodTestResultsMap, boolean saveIfUninterpretable) {

    Map<Long, CollectedSample> collectedSamplesMap = new HashMap<Long, CollectedSample>();
    Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForCollections = new HashMap<Long, BloodTestingRuleResult>();
    List<Long> collectionsWithUninterpretableResults = new ArrayList<Long>();
    Date testedOn = new Date();
    Map<Long, Map<Long, String>> errorMap = validateTestResultValues(bloodTestResultsMap);
    if (errorMap.isEmpty()) {
      for (Long collectionId : bloodTestResultsMap.keySet()) {
        Map<Long, String> bloodTestResultsForCollection = bloodTestResultsMap.get(collectionId);
        CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
        BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(collectedSample, bloodTestResultsForCollection);
        collectedSamplesMap.put(collectedSample.getId(), collectedSample);
        bloodTestRuleResultsForCollections.put(collectedSample.getId(), ruleResult);

        if (ruleResult.getAboUninterpretable() ||
            ruleResult.getRhUninterpretable() ||
            ruleResult.getTtiUninterpretable()) {
          if (saveIfUninterpretable) {
            saveBloodTestResultsToDatabase(bloodTestResultsForCollection, collectedSample, testedOn, ruleResult);
          }
          else {
            Map<Long, String> uninterpretable = new HashMap<Long, String>();
            collectionsWithUninterpretableResults.add(collectionId);
            uninterpretable.put((long)-1, "Test results are uninterpretable");
            errorMap.put(collectionId, uninterpretable);
          }
        } else {
          saveBloodTestResultsToDatabase(bloodTestResultsForCollection, collectedSample, testedOn, ruleResult);
        }

      }
      em.flush();
    }

    Map<String, Object> results = new HashMap<String, Object>();
    results.put("collections", collectedSamplesMap);
    results.put("bloodTestingResults", bloodTestRuleResultsForCollections);
    results.put("collectionsWithUninterpretableResults", collectionsWithUninterpretableResults);
    results.put("errors", errorMap);

    return results;
  }

  private void saveBloodTestResultsToDatabase(Map<Long, String> bloodTestResultsForCollection,
      CollectedSample collectedSample,
      Date testedOn, BloodTestingRuleResult ruleResult) {
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

    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    BloodTestsUpdatedEvent bloodTestsUpdatedEvent;
    bloodTestsUpdatedEvent = new BloodTestsUpdatedEvent("10", Arrays.asList(collectedSample, ruleResult));
    bloodTestsUpdatedEvent.setCollectedSample(collectedSample);
    bloodTestsUpdatedEvent.setBloodTestingRuleResult(ruleResult);
    applicationContext.publishEvent(bloodTestsUpdatedEvent);
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

  private boolean isResultValidForBloodTest(BloodTest test, String result) {
    if (StringUtils.isBlank(result)) {
      return false;
    }
    List<String> validResults = Arrays.asList(test.getValidResults().split(","));
    if (!validResults.contains(result)) {
      return false;
    }
    return true;
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

  public BloodTest findBloodTestById(Integer bloodTestId) {
    String queryStr = "SELECT bt FROM BloodTest bt WHERE " +
        "bt.id=:bloodTestId";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("bloodTestId", bloodTestId);
    return query.getSingleResult();
  }

  public Map<String, Object> saveTTIResultsOnPlate(Map<String, Map<String, Object>> ttiResultsMap,
      Long ttiTestId) {

    Map<String, Object> results = new HashMap<String, Object>();
    Map<String, List<String>> errorsByWellNumber = new HashMap<String, List<String>>();
    Map<String, Long> collectionIdByWellNumber = new HashMap<String, Long>();
    Map<Long, String> errorsByCollectionId = new HashMap<Long, String>();

    Map<Long, CollectedSample> collectionIdMap = new HashMap<Long, CollectedSample>();

    Map<Long, MachineReading> machineReadingsForCollections = new HashMap<Long, MachineReading>();
    List<MachineReading> specialMachineReadings = new ArrayList<MachineReading>();

    BloodTest bloodTest = findBloodTestById(ttiTestId.intValue());

    PlateSession plateSession = new PlateSession();
    Date testedOn = new Date();
    plateSession.setPlateUsedOn(testedOn);

    Map<Long, Map<Long, String>> bloodTestResultsMap = new HashMap<Long, Map<Long,String>>(); 

    boolean errorsFound = false;
    
    for (String rowNum : ttiResultsMap.keySet()) {
      Map<String, Object> rowData = ttiResultsMap.get(rowNum);
      for (String colNum : rowData.keySet()) {

        String wellNumber = rowNum + "," + colNum;

        MachineReading machineReading = new MachineReading();
        Map<String, String> wellData = (Map<String, String>) rowData.get(colNum);

        // store well type in machine configuration
        Integer wellTypeId = Integer.parseInt(wellData.get("welltype"));
        WellType wellType = wellTypeRepository.getWellTypeById(wellTypeId);
        machineReading.setWellType(wellType);

        machineReading.setRowNumber(Integer.parseInt(rowNum));
        machineReading.setColumnNumber(Integer.parseInt(colNum));
        machineReading.setPlateSession(plateSession);
        try {
          if (StringUtils.isNotBlank(wellData.get("machineReading")))
            machineReading.setMachineReading(new BigDecimal(wellData.get("machineReading")));
        } catch (NumberFormatException ex) {
          ex.printStackTrace();
          errorsFound = true;
          addErrorToWell(errorsByWellNumber, wellNumber, "Invalid value for machine reading");
        }

        if (wellType.getRequiresSample()) {
          String collectionNumber = wellData.get("collectionNumber");
          CollectedSample collection = collectedSampleRepository.findCollectedSampleByCollectionNumber(collectionNumber);
          if (collection == null) {
            addErrorToWell(errorsByWellNumber, wellNumber, "Invalid collection number");
            errorsFound = true;
          }
          else {
            String result = wellData.get("testResult");
            collectionIdMap.put(collection.getId(), collection);
            if (bloodTestResultsMap.containsKey(collection.getId())) {
              errorsFound = true;
              addErrorToWell(errorsByWellNumber, wellNumber, "Duplicate collection number");
              errorsByCollectionId.put(collection.getId(), "Duplicate collection number");
              collectionIdByWellNumber.put(wellNumber, collection.getId());
            }
            if (!isResultValidForBloodTest(bloodTest, result)) {
              errorsFound = true;
              addErrorToWell(errorsByWellNumber, wellNumber, "Invalid test result specified");
            }
            Map<Long, String> resultsForCollection = new HashMap<Long, String>();
            resultsForCollection.put(ttiTestId, result);
            bloodTestResultsMap.put(collection.getId(), resultsForCollection);
            machineReadingsForCollections.put(collection.getId(), machineReading);
          }
        } else {
          specialMachineReadings.add(machineReading);
        }
      }
    }

    Map<Long, BloodTestingRuleResult> bloodTestRuleResultsForCollections = new HashMap<Long, BloodTestingRuleResult>();

    if (!errorsFound) {
      em.persist(plateSession);
      // first determine whether there are any uninterpretable results
      for (Long collectionId : collectionIdMap.keySet()) {
        Map<Long, String> bloodTestResultsForCollection = bloodTestResultsMap.get(collectionId);
        MachineReading machineReading = machineReadingsForCollections.get(collectionId);
        CollectedSample collectedSample = collectionIdMap.get(collectionId);
        BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(collectedSample, bloodTestResultsForCollection);
        bloodTestRuleResultsForCollections.put(collectionId, ruleResult);
        BloodTestResult btResult = saveBloodTestResultToDatabase(new Long(ttiTestId),
            bloodTestResultsForCollection.get(ttiTestId), collectedSample,
            testedOn, ruleResult);
        // no need to worry about uninterpretable results here
        machineReading.setBloodTestResult(btResult);
        em.persist(machineReading);
      }
    }

    results.put("collections", collectionIdMap);
    results.put("bloodTestingResults", bloodTestRuleResultsForCollections);
    results.put("errorsFound", errorsFound);
    results.put("errorsByCollectionId", errorsByCollectionId);
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

  private BloodTestResult saveBloodTestResultToDatabase(Long testId, String testResult,
      CollectedSample collectedSample, Date testedOn,
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
    // collectedSample.getBloodTypingResults() will not
    // contain this result
    btResult.setCollectedSample(collectedSample);
    btResult.setTestedOn(testedOn);
    btResult.setNotes("");
    btResult.setResult(testResult);
    em.persist(btResult);
    em.refresh(btResult);
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    BloodTestsUpdatedEvent bloodTestsUpdatedEvent;
    bloodTestsUpdatedEvent = new BloodTestsUpdatedEvent("10", Arrays.asList(collectedSample, ruleResult));
    bloodTestsUpdatedEvent.setCollectedSample(collectedSample);
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
  }

  public void deactivateTests(BloodTestContext context) {
    String queryStr = "UPDATE BloodTest set isActive=:isActive WHERE context=:context";
    Query query = em.createQuery(queryStr);
    query.setParameter("isActive", false);
    query.setParameter("context", context);
    query.executeUpdate();
  }
}
