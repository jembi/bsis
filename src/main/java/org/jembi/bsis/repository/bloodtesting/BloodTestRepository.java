package org.jembi.bsis.repository.bloodtesting;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.AbstractRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestRepository extends AbstractRepository<BloodTest> {

  @PersistenceContext
  private EntityManager em;

  // FIXME: This query doesn't take into account active but deleted tests
  public List<BloodTest> getBloodTypingTests() {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS_BY_CATEGORY, BloodTest.class)
        .setParameter("category", BloodTestCategory.BLOODTYPING)
        .setParameter("isActive", true)
        .getResultList();
  }

  public List<BloodTest> getBloodTestsOfType(BloodTestType type) {
    return getBloodTestsOfTypes(Arrays.asList(type));
  }

  // FIXME: This query doesn't take into account deleted but active tests
  private List<BloodTest> getBloodTestsOfTypes(List<BloodTestType> types) {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS_BY_TYPE, BloodTest.class)
        .setParameter("types", types)
        .setParameter("isActive", true)
        .getResultList();
  }

  public List<BloodTest> findActiveBloodTests() {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_ACTIVE_NOT_DELETED_BLOOD_TESTS, BloodTest.class)
        .setParameter("isActive", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }
  
  public boolean isUniqueTestName(Long id, String testName) {
    // passing null as the ID parameter does not work because the IDs in mysql are never null. So if
    // id is null, the below rather uses -1 which achieves the same result in the case of this
    // query.
    return em.createQuery("SELECT count(b) = 0 " +
        "FROM BloodTest b " +
        "WHERE b.testName = :testName " +
        "AND b.id != :id ", Boolean.class)
        .setParameter("id", id != null ? id : -1L)
        .setParameter("testName", testName)
        .getSingleResult();
  }

  // FIXME: this method should be renamed/refactored because it returns all inactive and deleted blood tests
  public List<BloodTest> getAllBloodTestsIncludeInactive() {   
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_ALL_BLOOD_TESTS, BloodTest.class)
        .getResultList();
  }

  public BloodTest findBloodTestById(Long bloodTestId) {
    String queryStr = "SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("bloodTestId", bloodTestId);
    return query.getSingleResult();
  }
}
