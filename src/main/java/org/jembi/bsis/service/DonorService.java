package org.jembi.bsis.service;

import java.util.Calendar;
import java.util.Date;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service to provide Donor related functionality that doesn't result in CRUD operations
 *
 * @see DonorCRUDService
 */
@Transactional
@Service
public class DonorService {

  @Autowired
  private DonationRepository donationRepository;

  /**
   * Determines when the Donor is next due to donate based on all of their Donations.
   *
   * @param donor Donor to update
   */
  public void setDonorDueToDonate(Donor donor) {
    Date dueToDonateDate = donationRepository.findLatestDueToDonateDateForDonor(donor.getId());
    donor.setDueToDonate(dueToDonateDate);
  }

  /**
   * Determines the date of the Donor's first Donation. NOTE: does not persist the changes to the
   * Donor.
   *
   * @param donor    Donor who made the specified Donation
   * @param donation Donation made by the specified Donor
   */
  public void setDonorDateOfFirstDonation(Donor donor, Donation donation) {
    if (donor.getDateOfFirstDonation() == null) {
      donor.setDateOfFirstDonation(donation.getDonationDate());
    }
  }

  /**
   * Determines the date of the Donor's last Donation NOTE: does not persist the changes to the
   * Donor.
   *
   * @param donor    Donor who made the specified Donation
   * @param donation Donation made by the specified Donor
   */
  public void setDonorDateOfLastDonation(Donor donor, Donation donation) {
    Date dateOfLastDonation = donor.getDateOfLastDonation();
    if (dateOfLastDonation == null || donation.getDonationDate().after(dateOfLastDonation)) {
      donor.setDateOfLastDonation(donation.getDonationDate());
    }
  }
}
