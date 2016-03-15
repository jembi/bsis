package repository;

import java.util.*;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

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

    List<DonationBatch> donationBatches = testBatch.getDonationBatches();
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

  public TestBatch findTestBatchById(Long id) throws NoResultException {
    TypedQuery<TestBatch> query = entityManager.createQuery(
        "SELECT t FROM TestBatch t WHERE t.id = :id", TestBatch.class);
    query.setParameter("id", id);
    TestBatch testBatch = query.getSingleResult();
    return testBatch;
  }

  public List<TestBatch> findTestBatches(List<TestBatchStatus> statuses, Date startDate, Date endDate) {

    String queryStr = "SELECT t FROM TestBatch t WHERE t.isDeleted = :deleted ";

    if (statuses != null) {
      queryStr += "AND t.status IN :statuses ";
    }

    if (startDate != null) {
      queryStr += "AND t.modificationTracker.createdDate >= :startDate ";
    }

    if (endDate != null) {
      queryStr += "AND t.modificationTracker.createdDate <= :endDate ";
    }

    TypedQuery<TestBatch> query = entityManager.createQuery(queryStr, TestBatch.class);

    query.setParameter("deleted", false);

    if (statuses != null) {
      query.setParameter("statuses", statuses);
    }

    if (startDate != null) {
      query.setParameter("startDate", startDate);
    }

    if (endDate != null) {
      query.setParameter("endDate", endDate);
    }

    return query.getResultList();

  }

  public void deleteTestBatch(Long id) {
    TestBatch testBatch = findTestBatchById(id);
    testBatch.setIsDeleted(true);
    List<DonationBatch> donationBatches = testBatch.getDonationBatches();
    if (donationBatches != null) {
      for (DonationBatch donationBatch : donationBatches) {
        donationBatch.setTestBatch(null); // remove association
      }
    }
    entityManager.merge(testBatch);
  }

  public List<TestBatch> getRecentlyClosedTestBatches(Integer numOfResults) {
    String queryStr = "SELECT tb FROM TestBatch tb "
        + "WHERE status = :status  ORDER BY lastUpdated DESC";
    TypedQuery<TestBatch> query = entityManager.createQuery(queryStr, TestBatch.class);
    query.setParameter("status", TestBatchStatus.CLOSED);
    query.setMaxResults(numOfResults);
    return query.getResultList();
  }
}