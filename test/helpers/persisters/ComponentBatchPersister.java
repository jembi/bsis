package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import model.componentbatch.ComponentBatch;

public class ComponentBatchPersister extends AbstractEntityPersister<ComponentBatch> {

  @Override
  public ComponentBatch deepPersist(ComponentBatch componentBatch, EntityManager entityManager) {
    if (componentBatch.getLocation() != null) {
      aLocationPersister().deepPersist(componentBatch.getLocation(), entityManager);
    }
    return persist(componentBatch, entityManager);
  }

}
