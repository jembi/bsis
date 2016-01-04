package service;

import java.util.Calendar;
import java.util.Date;

import model.donation.Donation;
import model.donor.Donor;
import model.packtype.PackType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationRepository;

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
   * Sets the Donor's next due date based on the specified donation.
   * 
   * NOTE: If the specified Donation is not the latest Donation, then an invalid "due to donate"
   * date will be set. This method is used for situations such as Donor Merging when the new Donor
   * has not yet been persisted.
   * 
   * @param donor Donor to update
   * @param donation Donation latest
   */
  public void setDonorDueToDonate(Donor donor, Donation donation) {
    PackType packType = donation.getPackType();
    int periodBetweenDays = packType.getPeriodBetweenDonations();
    Calendar dueToDonateDate = Calendar.getInstance();
    dueToDonateDate.setTime(donation.getDonationDate());
    dueToDonateDate.add(Calendar.DAY_OF_YEAR, periodBetweenDays);
    if (donor.getDueToDonate() == null || dueToDonateDate.getTime().after(donor.getDueToDonate())) {
      donor.setDueToDonate(dueToDonateDate.getTime());
    }
  }
	
	/**
	 * Determines the date of the Donor's first Donation. NOTE: does not persist the changes to the
	 * Donor.
	 * 
	 * @param donor Donor who made the specified Donation
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
	 * @param donor Donor who made the specified Donation
	 * @param donation Donation made by the specified Donor
	 */
	public void setDonorDateOfLastDonation(Donor donor, Donation donation) {
		Date dateOfLastDonation = donor.getDateOfLastDonation();
		if (dateOfLastDonation == null || donation.getDonationDate().after(dateOfLastDonation)) {
			donor.setDateOfLastDonation(donation.getDonationDate());
		}
	}
}
