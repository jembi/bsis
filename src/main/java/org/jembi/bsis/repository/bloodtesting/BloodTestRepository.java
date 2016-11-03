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

  public List<BloodTest> getBloodTypingTests() {
    String queryStr = "SELECT b FROM BloodTest b WHERE b.isActive=:isActive AND b.category=:category";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("isActive", true);
    query.setParameter("category", BloodTestCategory.BLOODTYPING);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public List<BloodTest> getBloodTestsOfType(BloodTestType type) {
    return getBloodTestsOfTypes(Arrays.asList(type));
  }

  private List<BloodTest> getBloodTestsOfTypes(List<BloodTestType> types) {
    String queryStr = "SELECT b FROM BloodTest b WHERE " + "b.bloodTestType IN (:types) AND " + "b.isActive=:isActive";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("types", types);
    query.setParameter("isActive", true);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public List<BloodTest> findActiveBloodTests() {
    return em.createQuery("SELECT b " 
        + "FROM BloodTest b " 
        + "WHERE b.isActive = :isActive "
        + "AND b.isDeleted = :isDeleted ",
        BloodTest.class)
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
    String queryStr = "SELECT b FROM BloodTest b";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    List<BloodTest> bloodTests = query.getResultList();
    return bloodTests;
  }

  public BloodTest findBloodTestById(Long bloodTestId) {
    String queryStr = "SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId";
    TypedQuery<BloodTest> query = em.createQuery(queryStr, BloodTest.class);
    query.setParameter("bloodTestId", bloodTestId);
    return query.getSingleResult();
  }
}
