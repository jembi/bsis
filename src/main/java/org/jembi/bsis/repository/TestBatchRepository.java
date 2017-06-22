package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.constant.TestBatchNamedQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TestBatchRepository extends AbstractRepository<TestBatch> {

  public TestBatch saveTestBatch(TestBatch testBatch, String testBatchNumber) {
    testBatch.setIsDeleted(false);
    testBatch.setBatchNumber(testBatchNumber);
    testBatch.setStatus(TestBatchStatus.OPEN);
    updateDonationWithTestBatch(testBatch);
    return testBatch;
  }

  public void updateDonationWithTestBatch(TestBatch testBatch) {

    Set<DonationBatch> donationBatches = testBatch.getDonationBatches();
    if (donationBatches != null && !donationBatches.isEmpty()) {
      entityManager.persist(testBatch);
      for (DonationBatch donationBatch : donationBatches) {
        donationBatch.setTestBatch(testBatch);
        entityManager.merge(donationBatch);
      }
    }

  }

  public List<TestBatch> getAllTestBatch() {
    TypedQuery<TestBatch> query = entityManager.createQuery(
        "SELECT t FROM TestBatch t WHERE t.isDeleted= :isDeleted",
        TestBatch.class);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public TestBatch findTestBatchById(UUID id) throws NoResultException {
    TypedQuery<TestBatch> query = entityManager.createQuery(
        "SELECT t FROM TestBatch t WHERE t.id = :id", TestBatch.class);
    query.setParameter("id", id);
    TestBatch testBatch = query.getSingleResult();
    return testBatch;
  }

  public List<TestBatch> findTestBatches(List<TestBatchStatus> statuses, Date startDate, Date endDate, UUID locationId) {

    boolean includeStartDate = true;
    if (startDate == null) {
      includeStartDate = false;
    }

    boolean includeEndDate = true;
    if (endDate == null) {
      includeEndDate = false;
    }

    boolean includeLocation = true;
    if (locationId == null) {
      includeLocation = false;
    }
    
    boolean includeStatuses = true;
    if (statuses == null) {
      includeStatuses = false;
    }

    return entityManager.createNamedQuery(
        TestBatchNamedQueryConstants.NAME_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION, TestBatch.class)
        .setParameter("deleted", false)
        .setParameter("includeStatuses", includeStatuses)
        .setParameter("statuses", statuses)
        .setParameter("includeStartDate", includeStartDate)
        .setParameter("startDate", startDate)
        .setParameter("includeEndDate", includeEndDate)
        .setParameter("endDate", endDate)
        .setParameter("includeLocation", includeLocation)
        .setParameter("locationId", locationId)
        .getResultList();
  }

  public void deleteTestBatch(UUID id) {
    TestBatch testBatch = findTestBatchById(id);
    testBatch.setIsDeleted(true);
    Set<DonationBatch> donationBatches = testBatch.getDonationBatches();
    if (donationBatches != null) {
      for (DonationBatch donationBatch : donationBatches) {
        donationBatch.setTestBatch(null); // remove association
      }
    }
    entityManager.merge(testBatch);
  }
}