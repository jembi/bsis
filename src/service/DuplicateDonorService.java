package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.donation.Donation;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
import model.donordeferral.DonorDeferral;
import model.util.Gender;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonorRepository;
import repository.SequenceNumberRepository;

/**
 * Service that provides functionality in order to identify and merge duplicate Donors
 */
@Transactional
@Service
public class DuplicateDonorService {
	
	@Autowired
	private DonorRepository donorRepository;
	
	@Autowired
	private SequenceNumberRepository sequenceNumberRepository;
	
	/**
	 * Completes the merge of the list of donors and also persists the new donor, the updates to the
	 * existing donors and the backup logs necessary for restoring merged donors.
	 * 
	 * @param newDonor Donor with the details selected by the user merging
	 * @param donorNumbers List of donorNumbers for the Donors that are being merged into the newDonor
	 * @return Donor new donor
	 */
	public Donor mergeAndSaveDonors(Donor newDonor, List<String> donorNumbers) {
		// set donor number for the new donor
		newDonor.setDonorNumber(sequenceNumberRepository.getNextDonorNumber());
		// get the list of donors to merge
		List<Donor> donorsToMerge = donorRepository.findDonorsByNumbers(donorNumbers);
		// do the merge
		List<DuplicateDonorBackup> backupLogs = mergeDonors(newDonor, donorsToMerge);
		// save the new donor, the merged donors and the backup logs
		Donor savedDonor = donorRepository.addMergedDonor(newDonor, donorsToMerge, backupLogs);
		
		return savedDonor;
	}
	
	/**
	 * Complete the merge of the list of donors by changing the status and moving Donations and
	 * Deferrals to the specified new donor.
	 * 
	 * @param newDonor Donor with the details selected by the user merging
	 * @param donors List of Donors that are being merged into the newDonor
	 * @return List of DuplicateDonorBackup records that can be used to rollback a merge
	 */
	public List<DuplicateDonorBackup> mergeDonors(Donor newDonor, List<Donor> donors) {
		String newDonorNumber = newDonor.getDonorNumber();
		// combine Donations and Deferrals and create a backup log
		List<Donation> combinedDonations = new ArrayList<Donation>();
		List<DonorDeferral> combinedDeferrals = new ArrayList<DonorDeferral>();
		List<DuplicateDonorBackup> backupLog = new ArrayList<DuplicateDonorBackup>();
		if (donors != null) {
			for (Donor donor : donors) {
				donor.setDonorStatus(DonorStatus.MERGED);
				String donorNumber = donor.getDonorNumber();
				List<Donation> donorDonations = donor.getDonations();
				if (donorDonations != null) {
					for (Donation donation : donorDonations) {
						if (donation != null) {
							combinedDonations.add(donation);
							backupLog.add(new DuplicateDonorBackup(newDonorNumber, donorNumber, donation.getId(), null));
						}
						
					}
				}
				List<DonorDeferral> deferrals = donor.getDeferrals();
				if (deferrals != null) {
					for (DonorDeferral deferral : deferrals) {
						if (deferral != null) {
							combinedDeferrals.add(deferral);
							backupLog.add(new DuplicateDonorBackup(newDonorNumber, donorNumber, null, deferral.getId()));
						}
					}
				}
				donor.setDeferrals(null);
				donor.setDonations(null);
			}
		}
		newDonor.setDonations(combinedDonations);
		newDonor.setDeferrals(combinedDeferrals);
		return backupLog;
	}
	
	/**
	 * Identifies the duplicate donors matching on first name, last name, gender and date of birth.
	 * 
	 * @param donors List<Donor> list of donors to check
	 * @return Map<List<Donor>> map of duplicate donors found, will not be null or contain nulls
	 */
	public Map<String, List<Donor>> findDuplicateDonors(List<Donor> donors) {
		Map<String, List<Donor>> duplicateDonors = new HashMap<String, List<Donor>>();
		if (donors != null) {
			List<Donor> potentialDuplicates = new ArrayList<Donor>(donors);
			for (int i = 0; i < potentialDuplicates.size(); i++) {
				Donor d1 = potentialDuplicates.get(i);
				List<Donor> duplicates = new ArrayList<Donor>();
				// find the duplicates starting from the next element in the list of Donors
				for (int j = i + 1; j < potentialDuplicates.size(); j++) {
					Donor d2 = potentialDuplicates.get(j);
					if (match(d1, d2)) {
						duplicates.add(d2);
					}
				}
				if (!duplicates.isEmpty()) {
					// remove the found duplicates from the potential list
					for (Donor d3 : duplicates) {
						potentialDuplicates.remove(d3);
					}
					// add the new duplicates to the duplicate map
					duplicates.add(d1);
					duplicateDonors.put(String.valueOf(d1.getDonorNumber()), duplicates);
				}
			}
		}
		return duplicateDonors;
	}
	
	/**
	 * Identifies the donors that are probably a duplicate of the specified donor
	 * 
	 * @param donor Donor on which to match
	 * @param donors List<Donor> list of donors to check
	 * @return List<Donor> list of suspected duplicate of the specified donor
	 */
	public List<Donor> findDuplicateDonors(Donor donor, List<Donor> donors) {
		List<Donor> duplicates = new ArrayList<Donor>();
		for (int i = 0; i < donors.size(); i++) {
			Donor donor2 = donors.get(i);
			if (match(donor, donor2)) {
				duplicates.add(donor2);
			}
		}
		return duplicates;
	}
	
	/**
	 * Determines if two donors are a match. If both Donors are null, then they are matched.
	 * 
	 * @param donor1 Donor first donor to match
	 * @param donor2 Donor second donor to match
	 * @return boolean true if the donors are considered duplicates
	 */
	public boolean match(Donor donor1, Donor donor2) {
		if (donor1 == null && donor2 == null) {
			return true;
		}
		if (donor1 == null || donor2 == null) {
			return false;
		}
		if (!StringUtils.equalsIgnoreCase(donor1.getFirstName(), donor2.getFirstName())) {
			return false;
		}
		if (!StringUtils.equalsIgnoreCase(donor1.getLastName(), donor2.getLastName())) {
			return false;
		}
		if (!equals(donor1.getGender(), donor2.getGender())) {
			return false;
		}
		if (!equals(donor1.getBirthDate(), donor2.getBirthDate())) {
			return false;
		}
		return true;
	}
	
	private boolean equals(Gender gender1, Gender gender2) {
		if ((gender1 == null && gender2 == null) || (gender1 != null && gender1.equals(gender2))) {
			return true;
		}
		return false;
	}
	
	private boolean equals(Date dob1, Date dob2) {
		if (dob1 == null && dob2 == null) {
			return true;
		}
		if (dob1 == null || dob2 == null) {
			return false;
		}
		SimpleDateFormat dobFormatter = new SimpleDateFormat("yyyy-MM-dd");
		String dob1String = dobFormatter.format(dob1);
		String dob2String = dobFormatter.format(dob2);
		return StringUtils.equals(dob1String, dob2String);
	}
}
