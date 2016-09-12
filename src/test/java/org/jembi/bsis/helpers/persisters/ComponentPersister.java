package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aUserPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.component.Component;

public class ComponentPersister extends AbstractEntityPersister<Component> {

  @Override
  public Component deepPersist(Component component, EntityManager entityManager) {

    if (component.getDonation() != null) {
      aDonationPersister().deepPersist(component.getDonation(), entityManager);
    }

    if (component.getLocation() != null) {
      aLocationPersister().deepPersist(component.getLocation(), entityManager);
    }
    
    if (component.getCreatedBy() != null) {
      aUserPersister().deepPersist(component.getCreatedBy(), entityManager);
    }
    
    if (component.getParentComponent() != null) {
      deepPersist(component.getParentComponent(), entityManager);
    }
    
    return persist(component, entityManager);
  }

}
