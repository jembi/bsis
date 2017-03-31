package org.jembi.bsis.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.utils.SecurityUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DonorDeferralCRUDService {

  private static final Logger LOGGER = Logger.getLogger(DonorDeferralCRUDService.class);

  public static final Date PERMANENT_DEFERRAL_DATE = new Date(4102444800000L); // 2100-01-01

  @Autowired
  private DonorDeferralRepository donorDeferralRepository;
  @Autowired
  private DeferralReasonRepository deferralReasonRepository;
  @Autowired
  private DateGeneratorService dateGeneratorService;
  @Autowired
  private DeferralConstraintChecker deferralConstraintChecker;
  
  public DonorDeferral createDeferral(DonorDeferral donorDeferral) {
    donorDeferralRepository.save(donorDeferral);
    return donorDeferral;
  }

  public DonorDeferral createDeferralForDonorWithVenueAndDeferralReasonType(Donor donor, Location venue, DeferralReasonType deferralReasonType)
      throws NoResultException, NonUniqueResultException {

    LOGGER.info("Creating deferral for donor: " + donor + " at venue " + venue);

    // Look up deferral reason
    DeferralReason deferralReason = deferralReasonRepository.findDeferralReasonByType(deferralReasonType);

    boolean permanentDeferral = deferralReason.getDurationType() == DurationType.PERMANENT;

    if (permanentDeferral) {

      List<DonorDeferral> donorDeferrals = donorDeferralRepository.findDonorDeferralsForDonorByDeferralReason(
          donor, deferralReason);

      if (donorDeferrals.size() > 0) {
        // The donor already has a permanent deferral of this type so return it
        return donorDeferrals.get(0);
      }
    }

    DonorDeferral donorDeferral = new DonorDeferral();
    donorDeferral.setVenue(venue);
    donorDeferral.setDeferredDonor(donor);
    donorDeferral.setDeferralReason(deferralReason);
    donorDeferral.setIsVoided(Boolean.FALSE);

    Date now = dateGeneratorService.generateDate();
    donorDeferral.setDeferralDate(now);

    if (permanentDeferral) {
      donorDeferral.setDeferredUntil(PERMANENT_DEFERRAL_DATE);
    } else {
      Date deferredUntilDate = new DateTime(now).plusDays(deferralReason.getDefaultDuration()).toDate();
      donorDeferral.setDeferredUntil(deferredUntilDate);
    }

    donorDeferralRepository.save(donorDeferral);
    return donorDeferral;
  }

  public DonorDeferral findDeferralById(UUID donorDeferralId) throws IllegalStateException, NoResultException {
    DonorDeferral donorDeferral = donorDeferralRepository.findDonorDeferralById(donorDeferralId);
    return donorDeferral;
  }

  public void deleteDeferral(UUID donorDeferralId) throws IllegalStateException, NoResultException {
    DonorDeferral donorDeferral = findDeferralById(donorDeferralId);
    if (donorDeferral == null) {
      throw new IllegalStateException("DonorDeferral with id " + donorDeferralId
          + " does not exist (or has already been deleted).");
    }
    donorDeferral.setIsVoided(Boolean.TRUE);
    donorDeferral.setVoidedDate(new Date());
    donorDeferral.setVoidedBy(SecurityUtils.getCurrentUser());
    donorDeferralRepository.update(donorDeferral);
  }

  public DonorDeferral updateDeferral(DonorDeferral deferral) {
    if (!deferralConstraintChecker.canEditDonorDeferral(deferral.getId())) {
      throw new IllegalStateException("Cannot edit deferral with constraints");
    }
    DonorDeferral existingDeferral = donorDeferralRepository.findDonorDeferralById(deferral.getId());
    existingDeferral.setDeferralReason(deferral.getDeferralReason());
    existingDeferral.setDeferredUntil(deferral.getDeferredUntil());
    existingDeferral.setDeferralReasonText(deferral.getDeferralReasonText());
    existingDeferral.setVenue(deferral.getVenue());
    return donorDeferralRepository.update(existingDeferral);
  }

  public DonorDeferral endDeferral(UUID donorDeferralId, String comment) {
    if (!deferralConstraintChecker.canEndDonorDeferral(donorDeferralId)) {
      throw new IllegalStateException("Cannot end deferral with constraints");
    }
    DonorDeferral deferral = findDeferralById(donorDeferralId);
    if (deferral == null) {
      throw new IllegalStateException("DonorDeferral with id " + donorDeferralId
          + " does not exist (or has already been deleted).");
    }
    appendComment(deferral, comment);
    deferral.setDeferredUntil(new Date());
    return donorDeferralRepository.update(deferral);
  }

  protected void appendComment(DonorDeferral deferral, String comment) {
    if (StringUtils.isEmpty(deferral.getDeferralReasonText())) {
      deferral.setDeferralReasonText(comment);
    } else {
      StringBuilder newComment = new StringBuilder(deferral.getDeferralReasonText().trim());
      if (!deferral.getDeferralReasonText().trim().endsWith(".")) {
        newComment.append(".");
      }
      newComment.append(" ");
      newComment.append(comment);
      deferral.setDeferralReasonText(newComment.toString());
    }
  }
}
