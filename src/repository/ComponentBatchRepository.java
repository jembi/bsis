package repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;

@Repository
public class ComponentBatchRepository extends AbstractRepository<ComponentBatch> {
  
  public ComponentBatch findByIdIncludeDeleted(Long id) {
    return entityManager.find(ComponentBatch.class, id);
  }

  public List<ComponentBatch> findByStatus(ComponentBatchStatus... statuses) {
    return entityManager.createNamedQuery(
        ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCHES_BY_STATUS, ComponentBatch.class)
        .setParameter("statuses", Arrays.asList(statuses))
        .setParameter("isDeleted", false)
        .getResultList();
  }
  
  public ComponentBatch findById(Long id) {
    return entityManager.createNamedQuery(
        ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCH_BY_ID, ComponentBatch.class)
        .setParameter("id", id)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }

  public List<ComponentBatch> findComponentBatches(Date startDate, Date endDate) {
    String queryStr = "SELECT cb from ComponentBatch cb WHERE cb.isDeleted=:isDeleted ";

    if (startDate != null) {
      queryStr += "AND cb.modificationTracker.createdDate >= :startDate ";
    }
    if (endDate != null) {
      queryStr += "AND cb.modificationTracker.createdDate <= :endDate ";
    }

    TypedQuery<ComponentBatch> query = entityManager.createQuery(queryStr, ComponentBatch.class);
    query.setParameter("isDeleted", false);
    if (startDate != null) {
      query.setParameter("startDate", startDate);
    }
    if (endDate != null) {
      query.setParameter("endDate", endDate);
    }
    return query.getResultList();
  }
}
