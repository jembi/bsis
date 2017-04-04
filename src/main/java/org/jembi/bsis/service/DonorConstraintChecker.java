package org.jembi.bsis.service;

import javax.persistence.NoResultException;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonorConstraintChecker {

  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonationRepository donationRepository;
  @Autowired
  private DonorDeferralRepository donorDeferralRepository;
  @Autowired
  private DonorDeferralStatusCalculator donorDeferralStatusCalculator;

  public boolean canDeleteDonor(UUID donorId) throws NoResultException {
    Donor donor = donorRepository.findDonorById(donorId);

    if (donor.getNotes() != null && !donor.getNotes().isEmpty()) {
      return false;
    }

    if (donationRepository.countDonationsForDonor(donor) > 0) {
      return false;
    }

    if (donorDeferralRepository.countDonorDeferralsForDonor(donor) > 0) {
      return false;
    }

    return true;
  }

  public boolean isDonorEligibleToDonate(UUID donorId) {

    Donor donor = donorRepository.findDonorById(donorId);

    if (donor.getDonations() != null) {

      for (Donation donation : donor.getDonations()) {

        PackType packType = donation.getPackType();

        if (!packType.getCountAsDonation()) {
          // Don't check period between donations if it doesn't count as a donation
          continue;
        }

        // Work out the next allowed donation date
        DateTime nextDonationDate = new DateTime(donation.getDonationDate())
            .plusDays(packType.getPeriodBetweenDonations())
            .withTimeAtStartOfDay();

        // Check if the next allowed donation date is after today
        if (nextDonationDate.isAfter(new DateTime().withTimeAtStartOfDay())) {
          return false;
        }
      }
    }

    if (donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donorId)) {
      return false;
    }

    return true;
  }

  public boolean isDonorDeferred(UUID donorId) {
    return donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donorId);
  }

  public boolean isDonorEligibleToDonateOnDate(UUID donorId, Date date) {
    // check when the Donor last made a Donation and when they are due to donate again
    Date latestDueToDonateDate = donationRepository.findLatestDueToDonateDateForDonor(donorId);
    if (latestDueToDonateDate != null) {
      DateTime nextDonationDate = new DateTime(latestDueToDonateDate).withTimeAtStartOfDay();
      // Check if the next allowed donation date is after the specified date
      if (nextDonationDate.isAfter(new DateTime(date).withTimeAtStartOfDay())) {
        return false;
      }
    }

    // check if the Donor has any outstanding temporary or permanent deferrals
    if (donorDeferralStatusCalculator.isDonorDeferredOnDate(donorId, date)) {
      return false;
    }

    return true;
  }

}
