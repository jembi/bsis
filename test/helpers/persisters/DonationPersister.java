package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aDonorPersister;
import static helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import model.donation.Donation;

public class DonationPersister extends AbstractEntityPersister<Donation> {

    @Override
    public Donation deepPersist(Donation donation, EntityManager entityManager) {
        if (donation.getDonor() != null) {
            aDonorPersister().deepPersist(donation.getDonor(), entityManager);
        }
        
        if (donation.getDonorPanel() != null) {
            aLocationPersister().deepPersist(donation.getDonorPanel(), entityManager);
        }
        
        return persist(donation, entityManager);
    }

}
