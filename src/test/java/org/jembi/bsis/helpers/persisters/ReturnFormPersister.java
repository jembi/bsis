package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.returnform.ReturnForm;

public class ReturnFormPersister extends AbstractEntityPersister<ReturnForm> {

  @Override
  public ReturnForm deepPersist(ReturnForm returnForm, EntityManager entityManager) {
    if (returnForm.getReturnedFrom() != null) {
      aLocationPersister().deepPersist(returnForm.getReturnedFrom(), entityManager);
    }
    if (returnForm.getReturnedTo() != null) {
      aLocationPersister().deepPersist(returnForm.getReturnedTo(), entityManager);
    }
    return persist(returnForm, entityManager);
  }

}
