package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import model.returnform.ReturnForm;

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
