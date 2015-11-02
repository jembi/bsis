package service;

import java.util.Calendar;
import java.util.Date;

import model.donation.Donation;
import model.donor.Donor;
import model.packtype.PackType;

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
	
	/**
	 * Determines when the Donor is next due to donate given a Donation. NOTE: does not persist the
	 * changes to the Donor.
	 * 
	 * @param donor Donor who made the specified Donation
	 * @param donation Donation made by the specified Donor
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
