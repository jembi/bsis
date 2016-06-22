package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.anAdverseEventTypePersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.adverseevent.AdverseEvent;

public class AdverseEventPersister extends AbstractEntityPersister<AdverseEvent> {

  @Override
  public AdverseEvent deepPersist(AdverseEvent adverseEvent, EntityManager entityManager) {

    if (adverseEvent.getType() != null) {
      anAdverseEventTypePersister().deepPersist(adverseEvent.getType(), entityManager);
    }

    return persist(adverseEvent, entityManager);
  }

}
