package org.jembi.bsis.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.TypedQuery;

import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentbatch.ComponentBatchStatus;
import org.springframework.stereotype.Repository;

@Repository
public class ComponentBatchRepository extends AbstractRepository<ComponentBatch> {
  
  public ComponentBatch findByIdIncludeDeleted(UUID id) {
    return entityManager.find(ComponentBatch.class, id);
  }

  public List<ComponentBatch> findByStatus(ComponentBatchStatus... statuses) {
    return entityManager.createNamedQuery(
        ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCHES_BY_STATUS, ComponentBatch.class)
        .setParameter("statuses", Arrays.asList(statuses))
        .setParameter("isDeleted", false)
        .getResultList();
  }
  
  public ComponentBatch findById(UUID id) {
    return entityManager.createNamedQuery(
        ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCH_BY_ID, ComponentBatch.class)
        .setParameter("id", id)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }
  
  public ComponentBatch findByIdEager(UUID id) {
    return entityManager.createNamedQuery(
        ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCH_BY_ID_EAGER, ComponentBatch.class)
        .setParameter("id", id)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }

  public List<ComponentBatch> findComponentBatches(Date startCollectionDate, Date endCollectionDate) {
    String queryStr = "SELECT cb FROM ComponentBatch cb "
        + "LEFT JOIN FETCH cb.donationBatches WHERE cb.isDeleted=:isDeleted ";

    if (startCollectionDate != null) {
      queryStr += "AND cb.collectionDate >= :startDate ";
    }
    if (endCollectionDate != null) {
      queryStr += "AND cb.collectionDate <= :endDate ";
    }

    queryStr += "ORDER BY cb.collectionDate DESC";

    TypedQuery<ComponentBatch> query = entityManager.createQuery(queryStr, ComponentBatch.class);
    query.setParameter("isDeleted", false);
    if (startCollectionDate != null) {
      query.setParameter("startDate", startCollectionDate);
    }
    if (endCollectionDate != null) {
      query.setParameter("endDate", endCollectionDate);
    }
    return query.getResultList();
  }
}
