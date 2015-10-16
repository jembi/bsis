package repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import model.donor.Donor;
import model.donordeferral.DonorDeferral;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DonorDeferralRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional(propagation = Propagation.MANDATORY)
    public void save(DonorDeferral donorDeferral) {
        entityManager.persist(donorDeferral);
    }
    
    public DonorDeferral findDonorDeferralById(Long donorDeferralId) throws NoResultException {
        return entityManager.createNamedQuery(
            "QUERY_FIND_DONOR_DEFERRAL_BY_ID", 
            DonorDeferral.class)
            .setParameter("donorDeferralId", donorDeferralId)
            .setParameter("voided", false)
            .getSingleResult();
    }

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
