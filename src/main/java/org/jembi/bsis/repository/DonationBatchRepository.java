package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationBatchRepository {

  @PersistenceContext
  EntityManager em;

  public DonationBatchRepository() {
  }

  public DonationBatch findDonationBatchByIdEager(UUID batchId) {
    String queryString = "SELECT distinct b FROM DonationBatch b LEFT JOIN FETCH b.donations LEFT JOIN FETCH b.venue " +
        "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    DonationBatch b = query.setParameter("batchId", batchId).getSingleResult();
    return b;
  }

  public DonationBatch findDonationBatchById(UUID batchId) {
    String queryString = "SELECT distinct b FROM DonationBatch b LEFT JOIN FETCH b.donations " +
        "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchId", batchId).getSingleResult();
  }

  public DonationBatch findDonationBatchByBatchNumber(String batchNumber) throws NoResultException, NonUniqueResultException {
    String queryString = "SELECT distinct b FROM DonationBatch b LEFT JOIN FETCH b.donations " +
        "WHERE b.batchNumber = :batchNumber and b.isDeleted = :isDeleted";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    try {
      return query.setParameter("batchNumber", batchNumber).getSingleResult();
    } catch (NoResultException ex) {
      throw new NoResultException("No DonationBatch Exists with ID :" + batchNumber);
    }
  }

  public DonationBatch
  findDonationBatchByBatchNumberIncludeDeleted(String batchNumber) {
    String queryString = "SELECT distinct b FROM DonationBatch b LEFT JOIN FETCH b.donations " +
        "WHERE b.batchNumber = :batchNumber";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    DonationBatch batch = null;
    try {
      batch = query.setParameter("batchNumber", batchNumber).getSingleResult();
    } catch (Exception ex) {
    }
    return batch;

  }

  public void addDonationBatch(DonationBatch donationBatch) {
    em.persist(donationBatch);
    em.flush();
    em.refresh(donationBatch);
  }


  @Transactional(propagation = Propagation.MANDATORY)
  public DonationBatch updateDonationBatch(DonationBatch donationBatch) {
    return em.merge(donationBatch);
  }


  public List<DonationBatch> findDonationBatches(Boolean isClosed, List<UUID> venueIds, Date startDate, Date endDate) {
    String queryStr = "SELECT distinct b from DonationBatch b LEFT JOIN FETCH b.donations WHERE b.isDeleted=:isDeleted ";
    if (!venueIds.isEmpty()) {
      queryStr += "AND b.venue.id IN (:venueIds) ";
    }

    if (startDate != null) {
      queryStr += "AND b.donationBatchDate >= :startDate ";
    }

    if (endDate != null) {
      queryStr += "AND b.donationBatchDate <= :endDate ";
    }

    if (isClosed != null) {
      queryStr += "AND b.isClosed=:isClosed ";
    }

    TypedQuery<DonationBatch> query = em.createQuery(queryStr, DonationBatch.class);
    query.setParameter("isDeleted", false);
    if (startDate != null) {
      query.setParameter("startDate", startDate);
    }
    if (endDate != null) {
      query.setParameter("endDate", endDate);
    }
    if (!venueIds.isEmpty()) {
      query.setParameter("venueIds", venueIds);
    }
    if (isClosed != null) {
      query.setParameter("isClosed", isClosed);
    }

    return query.getResultList();
  }

  public List<Donation> findDonationsInBatch(UUID batchId) {
    DonationBatch donationBatch = findDonationBatchByIdEager(batchId);
    List<Donation> donations = new ArrayList<Donation>();
    for (Donation c : donationBatch.getDonations()) {
      donations.add(c);
    }
    return donations;
  }


  public List<DonationBatch> getRecentlyClosedDonationBatches(Integer numOfResults) {

    String queryStr = "SELECT b FROM DonationBatch b "
        + "WHERE isClosed = true  ORDER BY lastUpdated DESC";
    TypedQuery<DonationBatch> query = em.createQuery(queryStr, DonationBatch.class);
    query.setMaxResults(numOfResults);
    return query.getResultList();

  }

  public int countOpenDonationBatches() {
    return em.createNamedQuery(
        DonationBatchQueryConstants.NAME_COUNT_DONATION_BATCHES,
        Number.class)
        .setParameter("closed", false)
        .setParameter("deleted", false)
        .getSingleResult()
        .intValue();
  }
  
  public boolean verifyDonationBatchExists(UUID id) {
    return em.createNamedQuery(DonationBatchQueryConstants.NAME_VERIFY_DONATION_BATCH_WITH_ID_EXISTS, Boolean.class)
        .setParameter("id", id)
        .setParameter("deleted", false)
        .getSingleResult();
  }
  
  public List<DonationBatch> findUnassignedDonationBatchesForComponentBatch() {
    return em.createNamedQuery(DonationBatchQueryConstants.NAME_FIND_UNASSIGNED_DONATION_BATCHES_WITH_COMPONENTS, 
        DonationBatch.class).getResultList();
  }
  
  public ComponentBatch findComponentBatchByDonationbatchId(UUID donationBatchId) {
    ComponentBatch componentBatch = null;
    try {
      componentBatch = em.createNamedQuery(       
      DonationBatchQueryConstants.NAME_FIND_COMPONENTBATCH_BY_DONATIONBATCH_ID, ComponentBatch.class)
      .setParameter("donationBatchId", donationBatchId)
      .getSingleResult();
    } catch(NoResultException e) {
      
    }
    return componentBatch;  
  }
}
