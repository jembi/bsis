package repository.rawbloodtests;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.microtiterplate.MicrotiterPlate;
import model.rawbloodtest.RawBloodTest;
import model.rawbloodtest.RawBloodTestGroup;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class RawBloodTestRepository {

  @PersistenceContext
  EntityManager em;

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
    // TODO Auto-generated method stub
    return null;
  }
}
