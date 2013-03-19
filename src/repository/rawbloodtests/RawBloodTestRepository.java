package repository.rawbloodtests;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.microtiterplate.MicrotiterPlate;
import model.rawbloodtest.RawBloodTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.CollectedSampleRepository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
@Transactional
public class RawBloodTestRepository {

  @PersistenceContext
  EntityManager em;

  @Autowired
  private CollectedSampleRepository collectedSampleRepository;

  public MicrotiterPlate getPlate(String plateKey) {
    String queryStr = "SELECT p from MicrotiterPlate p " +
    		"WHERE p.plateKey=:plateKey";
    TypedQuery<MicrotiterPlate> query = em.createQuery(queryStr, MicrotiterPlate.class);
    query.setParameter("plateKey", plateKey);
    return query.getSingleResult();
  }

  public List<RawBloodTest> getRawBloodTestsForPlate(String plateKey) {
    String queryStr = "SELECT b from RawBloodTest b WHERE " +
        "b.plateUsedForTesting.plateKey=:plateKey";
    TypedQuery<RawBloodTest> query = em.createQuery(queryStr, RawBloodTest.class);
    query.setParameter("plateKey", plateKey);
    List<RawBloodTest> bloodTests = query.getResultList();
    Collections.sort(bloodTests, new Comparator<RawBloodTest>() {
      public int compare(RawBloodTest b1, RawBloodTest b2) {
        return b1.getRankOnPlate() - b2.getRankOnPlate();
      }
    });
    return bloodTests;
  }

  public Map<String, List<RawBloodTest>> saveBloodTypingResults(
      String rawBloodTests) {

    ObjectMapper mapper = new ObjectMapper(); 
    try {
      System.out.println(rawBloodTests);
      @SuppressWarnings("unchecked")
      Map<Long, Map<Integer, String>> rawBloodTestMap = mapper.readValue(rawBloodTests, HashMap.class);
      for (Long collectionId : rawBloodTestMap.keySet()) {
        Map<Integer, String> rawBloodTestResults = rawBloodTestMap.get(collectionId);
        CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(collectionId);
        for (Integer rawBloodTestId : rawBloodTestResults.keySet()) {
          RawBloodTest rawBloodTest = findRawBloodTestById(rawBloodTestId);
        }
      }
      System.out.println(rawBloodTestMap);
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

  private RawBloodTest findRawBloodTestById(Integer rawBloodTestId) {
    String queryStr = "SELECT r FROM RawBloodTest r WHERE r.id=:rawBloodTestId and r.isActive=:isActive";
    TypedQuery<RawBloodTest> query = em.createQuery(queryStr, RawBloodTest.class);
    query.setParameter("rawBloodTestId", rawBloodTestId);
    query.setParameter("isActive", false);
    return query.getSingleResult();
  }
}
