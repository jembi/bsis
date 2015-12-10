package helpers.persisters;

import model.adverseevent.AdverseEvent;

import javax.persistence.EntityManager;

import static helpers.persisters.EntityPersisterFactory.anAdverseEventTypePersister;

public class AdverseEventPersister extends AbstractEntityPersister<AdverseEvent> {

  @Override
  public AdverseEvent deepPersist(AdverseEvent adverseEvent, EntityManager entityManager) {

    if (adverseEvent.getType() != null) {
      anAdverseEventTypePersister().deepPersist(adverseEvent.getType(), entityManager);
    }

    return persist(adverseEvent, entityManager);
  }

}
