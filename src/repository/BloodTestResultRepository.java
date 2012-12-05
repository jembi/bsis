package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.bloodtest.BloodTestResult;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestResultRepository {

  @PersistenceContext
  private EntityManager em;

  public void saveBloodTest(BloodTestResult bloodTestResult) {
    em.persist(bloodTestResult);
    em.flush();
  }

  public BloodTestResult findBloodTestResultById(Long bloodTestResultId) {
    try {
      String queryString = "SELECT t FROM BloodTestAllowedResult t WHERE t.id = :bloodTestResultId";
      TypedQuery<BloodTestResult> query = em.createQuery(queryString, BloodTestResult.class);
      return query.setParameter("bloodTestResultId", bloodTestResultId).getSingleResult();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public List<BloodTestResult> getAllBloodTestResults() {
    try {
      String queryString = "SELECT t FROM BloodTestResult t";
      TypedQuery<BloodTestResult> query = em.createQuery(queryString, BloodTestResult.class);
      return query.getResultList();
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public boolean isBloodTestResultValid(Long id) {
    try {
      String queryString = "SELECT t FROM BloodTestResult t where id=:id";
      TypedQuery<BloodTestResult> query = em.createQuery(queryString, BloodTestResult.class);
      query.setParameter("id", id);
      if (query.getSingleResult() != null)
        return true;
      else
        return false;
    } catch (NoResultException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}
