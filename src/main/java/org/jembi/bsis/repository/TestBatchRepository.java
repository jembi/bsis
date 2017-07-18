package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.constant.TestBatchNamedQueryConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TestBatchRepository extends AbstractRepository<TestBatch> {

  public TestBatch findTestBatchById(UUID id) throws NoResultException {
    TypedQuery<TestBatch> query = entityManager.createQuery(
        "SELECT t FROM TestBatch t WHERE t.id = :id", TestBatch.class);
    query.setParameter("id", id);
    TestBatch testBatch = query.getSingleResult();
    return testBatch;
  }

  public List<TestBatch> findTestBatches(List<TestBatchStatus> statuses, Date startDate, Date endDate, UUID locationId) {

    boolean includeStartDateCheck = true;
    if (startDate == null) {
      includeStartDateCheck = false;
    }

    boolean includeEndDateCheck = true;
    if (endDate == null) {
      includeEndDateCheck = false;
    }

    boolean includeAllLocations = false;
    if (locationId == null) {
      includeAllLocations = true;
    }
    
    boolean includeAllStatuses = false;
    if (statuses == null) {
      includeAllStatuses = true;
    }

    return entityManager.createNamedQuery(
        TestBatchNamedQueryConstants.NAME_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION, TestBatch.class)
        .setParameter("deleted", false)
        .setParameter("includeAllStatuses", includeAllStatuses)
        .setParameter("statuses", statuses)
        .setParameter("includeStartDateCheck", includeStartDateCheck)
        .setParameter("startDate", startDate)
        .setParameter("includeEndDateCheck", includeEndDateCheck)
        .setParameter("endDate", endDate)
        .setParameter("includeAllLocations", includeAllLocations)
        .setParameter("locationId", locationId)
        .getResultList();
  }

  public void deleteTestBatch(UUID id) {
    TestBatch testBatch = findTestBatchById(id);
    testBatch.setIsDeleted(true);
    if (testBatch.getDonations() != null) {
      for (Donation donation : testBatch.getDonations()) {
        donation.setTestBatch(null); // remove association
      }
    }
    entityManager.merge(testBatch);
  }
}