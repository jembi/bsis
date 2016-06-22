package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aComponentTypePersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.packtype.PackType;

public class PackTypePersister extends AbstractEntityPersister<PackType> {

  @Override
  public PackType deepPersist(PackType packType, EntityManager entityManager) {
    if (packType.getComponentType() != null) {
      aComponentTypePersister().deepPersist(packType.getComponentType(), entityManager);
    }
    return persist(packType, entityManager);
  }

}
