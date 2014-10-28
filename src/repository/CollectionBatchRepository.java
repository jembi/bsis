package repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    String queryString = "SELECT b FROM CollectionBatch b LEFT JOIN FETCH b.collectionCenter LEFT JOIN FETCH b.collectionSite " +
                         "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    CollectionBatch b = query.setParameter("batchId", batchId).getSingleResult();
    return b;
  }

  public CollectionBatch findCollectionBatchById(Integer batchId) {
    String queryString = "SELECT b FROM CollectionBatch b " +
                         "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchId", batchId).getSingleResult();
  }

  public CollectionBatch findCollectionBatchByBatchNumber(String batchNumber) throws NoResultException,NonUniqueResultException {
    String queryString = "SELECT b FROM CollectionBatch b " +
        "WHERE b.batchNumber = :batchNumber and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    try{
    return query.setParameter("batchNumber", batchNumber).getSingleResult();
    }catch(NoResultException ex){
        throw new NoResultException("No DonatchBatch Exists with ID :"+ batchNumber);
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
	String queryString = "SELECT b FROM CollectionBatch b " +
	 "WHERE b.batchNumber = :batchNumber";
	TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
	CollectionBatch batch = null;
	try{
	batch = query.setParameter("batchNumber", batchNumber).getSingleResult();
	}catch(Exception ex){}
	return batch;
	
  }

  public CollectionBatch addCollectionBatch(CollectionBatch collectionBatch) {
    em.persist(collectionBatch);
    em.flush();
    em.refresh(collectionBatch);
    return collectionBatch;
  }

  public List<CollectionBatch> findCollectionBatches(String batchNumber,
      List<Long> centerIds, List<Long> siteIds) {
    String queryStr = "";
    if (StringUtils.isNotBlank(batchNumber)) {
      queryStr = "SELECT b from CollectionBatch b " +
                   "WHERE b.batchNumber=:batchNumber AND " +
                   "b.collectionCenter.id IN (:centerIds) AND " +
                   "b.collectionSite.id IN (:siteIds) AND " +
                   "b.isDeleted=:isDeleted";
    }
    else {
      queryStr = "SELECT b from CollectionBatch b " +
                 "WHERE b.collectionCenter.id IN (:centerIds) AND " +
                 "b.collectionSite.id IN (:siteIds) AND " +
                 "b.isDeleted=:isDeleted";
    }
    
    TypedQuery<CollectionBatch> query = em.createQuery(queryStr, CollectionBatch.class);
    if (StringUtils.isNotBlank(batchNumber))
      query.setParameter("batchNumber", batchNumber);
    query.setParameter("centerIds", centerIds);
    query.setParameter("siteIds", siteIds);
    query.setParameter("isDeleted", false);
    return query.getResultList();
  }

  public Set<String> findCollectionsInBatch(Integer batchId) {
    CollectionBatch collectionBatch = findCollectionBatchByIdEager(batchId);
    Set<String> collectionNumbers = new HashSet<String>();
    for (CollectedSample c : collectionBatch.getCollectionsInBatch()) {
      collectionNumbers.add(c.getCollectionNumber());
    }
    return collectionNumbers;
  }
}
