package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aLocationPersister;
import javax.persistence.EntityManager;
import model.donor.Donor;

public class DonorPersister extends AbstractEntityPersister<Donor> {

    @Override
    public Donor deepPersist(Donor donor, EntityManager entityManager) {
        
        if (donor.getDonorPanel() != null) {
            aLocationPersister().deepPersist(donor.getDonorPanel(), entityManager);
        }
        
        return persist(donor, entityManager);
    }

}
