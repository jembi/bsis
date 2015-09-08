package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.donor.Donor;
import model.util.Gender;

import org.apache.commons.lang3.StringUtils;

/**
 * Class that implements an algorithm to identify duplicate Donors.
 */
public class DuplicateDonorService {
	
	/**
	 * Identifies the duplicate donors matching on first name, last name, gender and date of birth.
	 * 
	 * @param donors List<Donor> list of donors to check
	 * @return Map<List<Donor>> map of duplicate donors found, will not be null or contain nulls
	 */
	public Map<String, List<Donor>> findDuplicateDonors(List<Donor> donors) {
		Map<String, List<Donor>> duplicateDonors = new HashMap<String, List<Donor>>();
		int duplicateCounter = 0;
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
					duplicateCounter++;
					// remove the found duplicates from the potential list
					for (Donor d3 : duplicates) {
						potentialDuplicates.remove(d3);
					}
					// add the new duplicates to the duplicate map
					duplicates.add(d1);
					duplicateDonors.put(String.valueOf(duplicateCounter), duplicates);
				}
			}
		}
		return duplicateDonors;
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
