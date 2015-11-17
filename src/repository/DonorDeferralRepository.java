package repository;

import java.util.Date;
import javax.persistence.NoResultException;
import java.util.List;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;

import org.springframework.stereotype.Repository;

@Repository
public class DonorDeferralRepository extends AbstractRepository<DonorDeferral> {
    
    public DonorDeferral findDonorDeferralById(Long donorDeferralId) throws NoResultException {
        return entityManager.createNamedQuery(
            DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRAL_BY_ID, 
            DonorDeferral.class)
            .setParameter("donorDeferralId", donorDeferralId)
            .setParameter("voided", false)
            .getSingleResult();
    }

    public int countDonorDeferralsForDonor(Donor donor) {

        return entityManager.createNamedQuery(
                DonorDeferralNamedQueryConstants.NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR,
                Number.class)
                .setParameter("donor", donor)
                .setParameter("voided", false)
                .getSingleResult()
                .intValue();
    }
    
    public int countCurrentDonorDeferralsForDonor(Donor donor) {

        return entityManager.createNamedQuery(
                DonorDeferralNamedQueryConstants.NAME_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR,
                Number.class)
                .setParameter("donor", donor)
                .setParameter("voided", false)
                .setParameter("permanentDuration", DurationType.PERMANENT)
                .setParameter("currentDate", new Date())
                .getSingleResult()
                .intValue();
    }

    public List<DonorDeferral> findDonorDeferralsForDonorByDeferralReason(Donor donor, DeferralReason deferralReason) {

      return entityManager.createNamedQuery(
          DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON,
          DonorDeferral.class)
          .setParameter("donor", donor)
          .setParameter("deferralReason", deferralReason)
          .setParameter("voided", false)
          .getResultList();
    }

}
