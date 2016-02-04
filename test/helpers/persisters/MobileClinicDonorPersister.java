package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import model.donor.MobileClinicDonor;

public class MobileClinicDonorPersister extends AbstractEntityPersister<MobileClinicDonor> {

    @Override
    public MobileClinicDonor deepPersist(MobileClinicDonor mobileClinicDonor, EntityManager entityManager) {
        
        if (mobileClinicDonor.getVenue() != null) {
            aLocationPersister().deepPersist(mobileClinicDonor.getVenue(), entityManager);
        }
        
        return persist(mobileClinicDonor, entityManager);
    }

}
