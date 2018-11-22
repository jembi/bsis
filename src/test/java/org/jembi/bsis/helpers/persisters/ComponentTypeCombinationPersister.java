package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aComponentTypePersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;

public class ComponentTypeCombinationPersister extends AbstractEntityPersister<ComponentTypeCombination> {

  @Override
  public ComponentTypeCombination deepPersist(ComponentTypeCombination componentTypeCombination,
      EntityManager entityManager) {
    if (componentTypeCombination.getComponentTypes() != null) {
      for (ComponentType componentType : componentTypeCombination.getComponentTypes()) {
        aComponentTypePersister().deepPersist(componentType, entityManager);
      }
    }

    if (componentTypeCombination.getSourceComponentTypes() != null) {
      for (ComponentType sourceComponentType : componentTypeCombination.getSourceComponentTypes()) {
        aComponentTypePersister().deepPersist(sourceComponentType, entityManager);
      }
    }

    return persist(componentTypeCombination, entityManager);
  }

}
