package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import model.collectionbatch.CollectionBatch;

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
    System.out.println(b.getCollectionCenter());
    return b;
  }

  public CollectionBatch findCollectionBatchById(Integer batchId) {
    String queryString = "SELECT b FROM CollectionBatch b " +
    		                 "WHERE b.id = :batchId and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchId", batchId).getSingleResult();
  }

  public CollectionBatch findCollectionBatchByBatchNumber(String batchNumber) {
    String queryString = "SELECT b FROM CollectionBatch b " +
        "WHERE b.batchNumber = :batchNumber and b.isDeleted = :isDeleted";
    TypedQuery<CollectionBatch> query = em.createQuery(queryString, CollectionBatch.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    return query.setParameter("batchNumber", batchNumber).getSingleResult();
  }

  public CollectionBatch addCollectionBatch(CollectionBatch collectionBatch) {
    em.persist(collectionBatch);
    em.flush();
    em.refresh(collectionBatch);
    return collectionBatch;
  }

  public List<CollectionBatch> findCollectionBatches(String batchNumber,
      List<Long> centerIds, List<Long> siteIds) {
    // TODO Auto-generated method stub
    return null;
  }
}
