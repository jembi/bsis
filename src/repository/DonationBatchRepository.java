package repository;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import model.collectedsample.CollectedSample;
import model.donationbatch.DonationBatch;
import model.donationbatch.DonationBatchSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DonationBatchRepository {

  @PersistenceContext
  EntityManager em;

  public DonationBatchRepository() {
  }

  public DonationBatch findDonationBatchByIdEager(Integer batchId) {
    String queryString = "SELECT b FROM DonationBatch b LEFT JOIN FETCH b.donationCenter LEFT JOIN FETCH b.donationSite " +
                         "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    DonationBatch b = query.setParameter("batchId", batchId).getSingleResult();
    return b;
  }

  public DonationBatch findDonationBatchById(Integer batchId) {
    String queryString = "SELECT b FROM DonationBatch b " +
                         "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchId", batchId).getSingleResult();
  }

  public DonationBatch findDonationBatchByBatchNumber(String batchNumber) {
    String queryString = "SELECT b FROM DonationBatch b " +
        "WHERE b.batchNumber = :batchNumber and b.isDeleted = :isDeleted";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchNumber", batchNumber).getSingleResult();
  }

  public DonationBatch findDonationBatchByBatchNumberIncludeDeleted(String batchNumber) {
    String queryString = "SELECT b FROM DonationBatch b " +
        "WHERE b.batchNumber = :batchNumber";
    TypedQuery<DonationBatch> query = em.createQuery(queryString, DonationBatch.class);
    DonationBatch batch = null;
    try {
      batch = query.setParameter("batchNumber", batchNumber).getSingleResult();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return batch;
  }

  public DonationBatch addDonationBatch(DonationBatch donationBatch) {
    em.persist(donationBatch);
    em.flush();
    em.refresh(donationBatch);
    return donationBatch;
  }
  
    public DonationBatch updateDonationBatch(DonationBatch donationBatch) {
    em.merge(donationBatch);
    return donationBatch;
  }
  

  public List<DonationBatch> findDonationBatches(String collectionNumber,
      List<Long> centerIds, List<Long> siteIds,Date startDate,Date endDate) {
    String queryStr = "";
    if (StringUtils.isNotBlank(collectionNumber)) {
      queryStr = "SELECT b from DonationBatch b , CollectedSample c " +
                   "WHERE c.collectionNumber=:collectionNumber AND c.donationBatch = b.id AND "+
                   "b.donationCenter.id IN (:centerIds) AND " +
                   "b.donationSite.id IN (:siteIds) AND " +
                   "b.isDeleted=:isDeleted";
    }
    else {
      queryStr = "SELECT b from DonationBatch b " +
                 "WHERE b.donationCenter.id IN (:centerIds) AND " +
                 "b.donationSite.id IN (:siteIds) AND " +
                 "b.isDeleted=:isDeleted";
    }
    
    if(!(startDate==null && endDate==null))
       queryStr += " AND b.modificationTracker.createdDate BETWEEN :startDate AND :endDate";
    
    TypedQuery<DonationBatch> query = em.createQuery(queryStr, DonationBatch.class);
    if (StringUtils.isNotBlank(collectionNumber))
      query.setParameter("collectionNumber", collectionNumber);
    
    if(!(startDate==null && endDate==null)){
      query.setParameter("startDate", startDate);
      query.setParameter("endDate", endDate);

     }
    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public Set<String> findDonationsInBatch(Integer batchId) {
    DonationBatch donationBatch = findDonationBatchByIdEager(batchId);
    Set<String> collectionNumbers = new HashSet<String>();
    for (CollectedSample c : donationBatch.getDonationsInBatch()) {
      collectionNumbers.add(c.getCollectionNumber());
    }
    return collectionNumbers;
  }
  
  public Long numberOfDonationsInBatch(Integer batchId){
      TypedQuery<Long> query = em.createQuery("SELECT COUNT(*) FROM CollectedSample c where donationBatch_id = :id", Long.class);
      query.setParameter("id", batchId);
      return query.getSingleResult();
  }
  
  public List<CollectedSample> findAllDonationsOfBatchById(Integer batchId){
      TypedQuery<CollectedSample> query = em.createQuery("SELECT C FROM CollectedSample C where donationBatch_id = :id", CollectedSample.class);
      query.setParameter("id", batchId);
      return query.getResultList();
  }
  
  public void addDonationBatchSession(DonationBatchSession donationBatchSession){
      em.persist(donationBatchSession);
  }
  
  public void deleteDonateBatchSession(Integer id ){
         Query query = em.createQuery("DELETE FROM DonationBatchSession where donationBatch_id = :id");
         query.setParameter("id", id);
         query.executeUpdate();
  }
  
  public DonationBatchSession getCurrenrDonationBatchSession(){
         TypedQuery<DonationBatchSession> query = em.createQuery("SELECT dbc from DonationBatchSession dbc",DonationBatchSession.class);
         try{
         return query.getSingleResult();
         }catch(NoResultException exception){
             exception.printStackTrace();
             return null;
         }
  }
}
  

