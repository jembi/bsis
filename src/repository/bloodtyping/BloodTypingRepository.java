package repository.bloodtyping;

import java.io.IOException;
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
      String rawBloodTests) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      System.out.println(rawBloodTests);
      @SuppressWarnings("unchecked")
      Map<String, Map<String, String>> bloodTypingTestMap = mapper.readValue(rawBloodTests, HashMap.class);
      System.out.println(bloodTypingTestMap);
      for (String collectionId : bloodTypingTestMap.keySet()) {
        Long collectionIdLong = Long.parseLong(collectionId);
        Map<String, String> bloodTypingTestResults = bloodTypingTestMap.get(collectionId);
        CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionIdLong);
        ruleEngine.applyBloodTypingTests(collectedSample, bloodTypingTestResults);
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
}
