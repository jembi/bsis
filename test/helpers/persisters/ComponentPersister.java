package helpers.persisters;

import model.component.Component;

import javax.persistence.EntityManager;

import static helpers.persisters.EntityPersisterFactory.aDonationPersister;

public class ComponentPersister extends AbstractEntityPersister<Component> {

  @Override
  public Component deepPersist(Component component, EntityManager entityManager) {
    if (component.getDonation() != null) {
      aDonationPersister().deepPersist(component.getDonation(), entityManager);
    }
    return persist(component, entityManager);
  }

}
