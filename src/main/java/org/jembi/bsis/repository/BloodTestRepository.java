package org.jembi.bsis.repository;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.constant.BloodTestNamedQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestRepository extends AbstractRepository<BloodTest> {

  public List<BloodTest> getBloodTypingTests() {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS_BY_CATEGORY, BloodTest.class)
        .setParameter("category", BloodTestCategory.BLOODTYPING)
        .setParameter("isActive", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }

  public List<BloodTest> getBloodTestsOfType(BloodTestType type) {
    return getBloodTestsOfTypes(Arrays.asList(type));
  }

  private List<BloodTest> getBloodTestsOfTypes(List<BloodTestType> types) {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS_BY_TYPE, BloodTest.class)
        .setParameter("types", types)
        .setParameter("isActive", true)
        .setParameter("isDeleted", false)
        .getResultList();
  }
  
  public boolean isUniqueTestName(Long id, String testName) {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_VERIFY_UNIQUE_BLOOD_TEST, Boolean.class)
        .setParameter("id", id)
        .setParameter("testName", testName)
        .getSingleResult();
  }

  public List<BloodTest> getBloodTests(boolean includeInactive, boolean includeDeleted) {   
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_GET_BLOOD_TESTS, BloodTest.class)
        .setParameter("includeInactive", includeInactive)
        .setParameter("includeDeleted", includeDeleted)
        .getResultList();
  }

  public BloodTest findBloodTestById(Long bloodTestId) {
    return entityManager.createNamedQuery(BloodTestNamedQueryConstants.NAME_FIND_BLOOD_TEST_BY_ID, BloodTest.class)
        .setParameter("bloodTestId", bloodTestId)
        .getSingleResult();
  }
}
