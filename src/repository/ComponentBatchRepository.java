package repository;

import java.util.List;

import model.componentbatch.ComponentBatch;
import model.componentbatch.ComponentBatchStatus;

import org.springframework.stereotype.Repository;

import scala.actors.threadpool.Arrays;

@Repository
public class ComponentBatchRepository extends AbstractRepository<ComponentBatch> {
  
  public ComponentBatch findByIdIncludeDeleted(Long id) {
    return entityManager.find(ComponentBatch.class, id);
  }

  public List<ComponentBatch> findByStatus(ComponentBatchStatus... excludeStatuses) {
    return entityManager.createNamedQuery(
        ComponentBatchNamedQueryConstants.NAME_FIND_COMPONENT_BATCHES_BY_STATUS, ComponentBatch.class)
        .setParameter("statuses", Arrays.asList(excludeStatuses))
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
}
