package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aDonationPersister;

import javax.persistence.EntityManager;

import model.component.Component;

public class ComponentPersister extends AbstractEntityPersister<Component> {

    @Override
    public Component deepPersist(Component component, EntityManager entityManager) {
        if (component.getDonation() != null) {
            aDonationPersister().deepPersist(component.getDonation(), entityManager);
        }
        return persist(component, entityManager);
    }

}
