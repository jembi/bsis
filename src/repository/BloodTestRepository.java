package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.testresults.BloodTest;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestRepository {
  @PersistenceContext
  private EntityManager em;

  public void saveBloodTest(BloodTest bloodTest) {
    em.persist(bloodTest);
    em.flush();
  }

  public BloodTest findBloodTestById(Long bloodTestId) {
    try {
      String queryString = "SELECT t FROM BloodTest t WHERE t.id = :bloodTestId";
      TypedQuery<BloodTest> query = em.createQuery(queryString, BloodTest.class);
      return query.setParameter("bloodTestId", bloodTestId).getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public List<BloodTest> getAllBloodTests() {
    try {
      String queryString = "SELECT t FROM BloodTest t";
      TypedQuery<BloodTest> query = em.createQuery(queryString, BloodTest.class);
      return query.getResultList();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
