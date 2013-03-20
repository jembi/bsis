package repository.bloodtyping;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

  public Map<String, List<BloodTypingTest>> saveBloodTypingResults(
      String bloodTypingTests) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      System.out.println(bloodTypingTests);
      @SuppressWarnings("unchecked")
      Map<String, Map<String, String>> bloodTypingTestMap = mapper.readValue(bloodTypingTests, HashMap.class);
      System.out.println(bloodTypingTestMap);
      for (String collectionId : bloodTypingTestMap.keySet()) {
        Long collectionIdLong = Long.parseLong(collectionId);
        Map<String, String> bloodTypingTestResults = bloodTypingTestMap.get(collectionId);
        CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionIdLong);
        Map<String, Object> result = ruleEngine.applyBloodTypingTests(collectedSample, bloodTypingTestResults);
        System.out.println("Collection: " + collectedSample.getCollectionNumber());
        System.out.println("Blood Typing Result: " + result);
      }
      System.out.println(bloodTypingTestMap);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch(NumberFormatException e) {
      e.printStackTrace();
    }

    return null;
  }

  private BloodTypingTest findRawBloodTestById(Integer rawBloodTestId) {
    String queryStr = "SELECT r FROM RawBloodTest r WHERE r.id=:rawBloodTestId and r.isActive=:isActive";
    TypedQuery<BloodTypingTest> query = em.createQuery(queryStr, BloodTypingTest.class);
    query.setParameter("rawBloodTestId", rawBloodTestId);
    query.setParameter("isActive", false);
    return query.getSingleResult();
  }

  public Map<String, Map<String, String>> validateValuesInWells(String bloodTypingTests) {

    Map<String, BloodTypingTest> allBloodTypingTestsMap = new HashMap<String, BloodTypingTest>(); 
    for (BloodTypingTest bloodTypingTest : getBloodTypingTests()) {
      allBloodTypingTestsMap.put(bloodTypingTest.getId().toString(), bloodTypingTest);
    }

    ObjectMapper mapper = new ObjectMapper();
    System.out.println(bloodTypingTests);
    @SuppressWarnings("unchecked")
    Map<String, Map<String, String>> bloodTypingTestMap = null;
    try {
      bloodTypingTestMap = mapper.readValue(bloodTypingTests, HashMap.class);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(bloodTypingTestMap);

    Map<String, Map<String,String>> errorMap = new HashMap<String, Map<String,String>>();

    for (String collectionId : bloodTypingTestMap.keySet()) {
      Map<String, String> testsForCollection = bloodTypingTestMap.get(collectionId);
      for (String testId : testsForCollection.keySet()) {
        String result = testsForCollection.get(testId);
        BloodTypingTest test = allBloodTypingTestsMap.get(testId);
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

  private void addErrorToMap(Map<String, Map<String, String>> errorMap,
      String collectionId, String testId, String errorMessage) {
    Map<String, String> errorsForCollection = errorMap.get(collectionId);
    if (errorsForCollection == null) {
      errorsForCollection = new HashMap<String, String>();
      errorMap.put(collectionId, errorsForCollection);
    }
    errorsForCollection.put(testId, errorMessage);
  }
}
