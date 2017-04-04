package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.DeferralExportDTO;
import org.jembi.bsis.dto.DeferredDonorsDTO;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.springframework.stereotype.Repository;

@Repository
public class DonorDeferralRepository extends AbstractRepository<DonorDeferral> {

  public DonorDeferral findDonorDeferralById(UUID donorDeferralId) throws NoResultException {
    return entityManager.createNamedQuery(
        DonorDeferralNamedQueryConstants.NAME_FIND_DONOR_DEFERRAL_BY_ID,
        DonorDeferral.class)
        .setParameter("donorDeferralId", donorDeferralId)
        .setParameter("voided", false)
        .getSingleResult();
  }
  
  public List<DeferredDonorsDTO> countDeferredDonors(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(DonorDeferralNamedQueryConstants.NAME_COUNT_DEFERRALS_BY_VENUE_DEFERRAL_REASON_AND_GENDER, 
        DeferredDonorsDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("deferralDeleted", false)
        .setParameter("deferralReasonDeleted", false)
        .setParameter("donorDeleted", false)
        .getResultList();
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

  public int countCurrentDonorDeferralsForDonor(UUID donorId) {

    return entityManager.createNamedQuery(
        DonorDeferralNamedQueryConstants.NAME_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR,
        Number.class)
        .setParameter("donorId", donorId)
        .setParameter("voided", false)
        .setParameter("permanentDuration", DurationType.PERMANENT)
        .setParameter("currentDate", new Date())
        .getSingleResult()
        .intValue();
  }

  public int countDonorDeferralsForDonorOnDate(UUID donorId, Date date) {

    return entityManager.createNamedQuery(
        DonorDeferralNamedQueryConstants.NAME_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR,
        Number.class)
        .setParameter("donorId", donorId)
        .setParameter("voided", false)
        .setParameter("permanentDuration", DurationType.PERMANENT)
        .setParameter("currentDate", date)
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
  
  public List<DeferralExportDTO> findDeferralsForExport() {
    return entityManager.createNamedQuery(
        DonorDeferralNamedQueryConstants.NAME_FIND_DEFERRALS_FOR_EXPORT,
        DeferralExportDTO.class)
        .setParameter("voided", false)
        .getResultList();
  }

}
