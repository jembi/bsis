package repository;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectedsample.CollectedSample;
import model.collectionbatch.CollectionBatch;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CollectionBatchRepository {

  @PersistenceContext
  EntityManager em;

  public CollectionBatchRepository() {
  }

  public CollectionBatch findCollectionBatchByIdEager(Integer batchId) {
    String queryString = "SELECT distinct b FROM CollectionBatch b LEFT JOIN FETCH b.collectionsInBatch LEFT JOIN FETCH b.donorPanel " +
                         "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    CollectionBatch b = query.setParameter("batchId", batchId).getSingleResult();
    return b;
  }

  public CollectionBatch findCollectionBatchById(Integer batchId) {
    String queryString = "SELECT distinct b FROM CollectionBatch b LEFT JOIN FETCH b.collectionsInBatch " +
                         "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchId", batchId).getSingleResult();
  }

  public CollectionBatch findCollectionBatchByBatchNumber(String batchNumber) throws NoResultException,NonUniqueResultException {
    String queryString = "SELECT distinct b FROM CollectionBatch b LEFT JOIN FETCH b.collectionsInBatch " +
        "WHERE b.batchNumber = :batchNumber and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    try{
    return query.setParameter("batchNumber", batchNumber).getSingleResult();
    }catch(NoResultException ex){
        throw new NoResultException("No DonationBatch Exists with ID :"+ batchNumber);
    }
  }

  /*
  public CollectionBatch
         findCollectionBatchByBatchNumberIncludeDeleted(String batchNumber)throws NoResultException, NonUniqueResultException{
    String queryString = "SELECT b FROM CollectionBatch b " +
        "WHERE b.batchNumber = :batchNumber";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    CollectionBatch batch = null;
    batch = query.setParameter("batchNumber", batchNumber).getSingleResult();
    return batch;
  }
  */
  
  public CollectionBatch
	  findCollectionBatchByBatchNumberIncludeDeleted(String batchNumber){
	String queryString = "SELECT distinct b FROM CollectionBatch b LEFT JOIN FETCH b.collectionsInBatch " +
	 "WHERE b.batchNumber = :batchNumber";
	TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
	CollectionBatch batch = null;
	try{
	batch = query.setParameter("batchNumber", batchNumber).getSingleResult();
	}catch(Exception ex){}
	return batch;
	
  }

  public void addCollectionBatch(CollectionBatch collectionBatch) {
    em.persist(collectionBatch);
    em.flush();
    em.refresh(collectionBatch);
  }
  
  public CollectionBatch updateCollectionBatch(CollectionBatch collectionBatch)throws IllegalArgumentException{
      CollectionBatch existingBatch = findCollectionBatchById(collectionBatch.getId());
      existingBatch.copy(collectionBatch);
      existingBatch.setIsClosed(collectionBatch.getIsClosed());
      return em.merge(existingBatch);
  }
  

  public List<CollectionBatch> findCollectionBatches(Boolean isClosed,
      List<Long> donorPanelIds) {
    String queryStr = "SELECT distinct b from CollectionBatch b LEFT JOIN FETCH b.collectionsInBatch WHERE b.isDeleted=:isDeleted ";
    if(!donorPanelIds.isEmpty()){
    	queryStr += "AND b.donorPanel.id IN (:donorPanelIds) ";
    }
    if(isClosed != null){
    	queryStr +=    "AND b.isClosed=:isClosed";
    }
    
    TypedQuery<CollectionBatch> query = em.createQuery(queryStr, CollectionBatch.class);
    query.setParameter("isDeleted", false);
    if(!donorPanelIds.isEmpty()){
    	query.setParameter("donorPanelIds", donorPanelIds);
    }
    if(isClosed != null){
    	query.setParameter("isClosed", isClosed);
    }
    
    return query.getResultList();
  }
  
  public List<CollectionBatch> findUnassignedCollectionBatches() {
    String queryStr = "SELECT distinct b from CollectionBatch b LEFT JOIN FETCH b.collectionsInBatch WHERE b.isDeleted=:isDeleted " +
    	"AND b.isClosed=:isClosed " + 
    	"AND b.testBatch=null";

    TypedQuery<CollectionBatch> query = em.createQuery(queryStr, CollectionBatch.class);
    query.setParameter("isDeleted", false);
    query.setParameter("isClosed", true);
   
    
    return query.getResultList();
  }
  
  public List<CollectedSample> findCollectionsInBatch(Integer batchId) {
    CollectionBatch collectionBatch = findCollectionBatchByIdEager(batchId);
    List<CollectedSample> collectedSamples = new ArrayList<CollectedSample>();
    for (CollectedSample c : collectionBatch.getCollectionsInBatch()) {
    	collectedSamples.add(c);
    }
    return collectedSamples;
  }
}
