package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aComponentStatusChangeReasonPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.componentmovement.ComponentStatusChange;

public class ComponentStatusChangePersister extends AbstractEntityPersister<ComponentStatusChange> {

  @Override
  public ComponentStatusChange deepPersist(ComponentStatusChange statusChange, EntityManager entityManager) {
    
    if (statusChange.getStatusChangeReason() != null) {
      aComponentStatusChangeReasonPersister().deepPersist(statusChange.getStatusChangeReason(), entityManager);
    }
    
    return persist(statusChange, entityManager);
  }

}
