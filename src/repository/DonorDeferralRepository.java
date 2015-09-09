package repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.donor.Donor;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DonorDeferralRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // TODO: Test
    public int countDonorDeferralsForDonor(Donor donor) {

        return entityManager.createNamedQuery(
                DonorDeferralNamedQueryConstants.NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR,
                Number.class)
                .setParameter("donor", donor)
                .setParameter("voided", false)
                .getSingleResult()
                .intValue();
    }

}
