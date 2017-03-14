package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aUserPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aComponentTypePersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.component.Component;

public class ComponentPersister extends AbstractEntityPersister<Component> {

  @Override
  public Component deepPersist(Component component, EntityManager entityManager) {

    if (component.getDonation() != null) {
      aDonationPersister().deepPersist(component.getDonation(), entityManager);
    }

    if (component.getComponentType() != null) {
      aComponentTypePersister().deepPersist(component.getComponentType(), entityManager);
    }

    if (component.getLocation() != null) {
      aLocationPersister().deepPersist(component.getLocation(), entityManager);
    }
    
    if (component.getCreatedBy() != null) {
      aUserPersister().deepPersist(component.getCreatedBy(), entityManager);
    }
    
    if (!component.isInitialComponent()) {
      deepPersist(component.getParentComponent(), entityManager);
    }
    
    return persist(component, entityManager);
  }

}
