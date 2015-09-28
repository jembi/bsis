/**
 * 
 * commented on issue #209 [Adapt BSIS to Expose Rest Services]**
 * 
package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.donor.Donor;
import model.location.Location;
import model.util.BloodGroup;

import org.apache.commons.lang3.StringUtils;

import viewmodel.DonorViewModel;

public class DonorCommunicationsBackingForm {

	private Donor donor;

	private List<BloodGroup> bloodGroups;
        
        @JsonIgnore
	private List<Location> venues;

	private String clinicDate;

	private String lastDonationFromDate;

	private String lastDonationToDate;

	private String anyBloodGroup;

	private boolean createDonorSummaryView;

	private String anyVenue;

	private String venueErrorMessage;

	private String bloodGroupErrorMessage;

	public DonorCommunicationsBackingForm() {
		donor = new Donor();
	}

	public DonorCommunicationsBackingForm(Donor donor) {
		this.donor = donor;
	}

        @JsonIgnore
	public DonorViewModel getDonorViewModel() {
		return new DonorViewModel(donor);
	}

	public String toString() {
		return donor.toString();
	}

	public String getVenue() {
		Location venue = donor.getVenue();
		if (venue == null || venue.getId() == null)
			return null;
		return venue.getId().toString();
	}

	private static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile(",");

	public void setVenue(String venue) {
		if (StringUtils.isBlank(venue)) {
			donor.setVenue(null);
		} else {
			try {
				List<Location> panels = new ArrayList<Location>();
				String[] venueStr = COMMA_SPLIT_PATTERN.split(venue);
				for (String venueId : venueStr) {
					Location l = new Location();
					l.setId(Long.parseLong(venueId));
					panels.add(l);
					donor.setVenue(l);
				}
				venues = panels;
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
				donor.setVenue(null);
			}
		}
	}

	public String getClinicDate() {
		return clinicDate;
	}

	public void setClinicDate(String clinicDate) {
		this.clinicDate = clinicDate;
	}

	public String getLastDonationFromDate() {
		return lastDonationFromDate;
	}

	public void setLastDonationFromDate(String lastDonationFromDate) {
		this.lastDonationFromDate = lastDonationFromDate;
	}

	public String getLastDonationToDate() {
		return lastDonationToDate;
	}

	public void setLastDonationToDate(String lastDonationToDate) {
		this.lastDonationToDate = lastDonationToDate;
	}

	public List<Location> getVenues() {
		return venues;
	}

	public void setVenues(List<Location> venues) {
		this.venues = venues;
	}

	public String getAnyBloodGroup() {
		return anyBloodGroup;
	}

	public void setAnyBloodGroup(String anyBloodGroup) {
		this.anyBloodGroup = anyBloodGroup;
	}

	public List<BloodGroup> getBloodGroups() {
		return bloodGroups;
	}

	public void setBloodGroups(List<String> bloodGroups) {
		this.bloodGroups = new ArrayList<BloodGroup>();
		for (String bg : bloodGroups) {
			this.bloodGroups.add(new BloodGroup(bg));
		}
	}
	
	public boolean getCreateDonorSummaryView() {
		return createDonorSummaryView;
	}

	public void setCreateDonorSummaryView(boolean createDonorSummaryView) {
		this.createDonorSummaryView = createDonorSummaryView;
	}

	public String getAnyVenue() {
		return anyVenue;
	}

	public void setAnyVenue(String anyVenue) {
		this.anyVenue = anyVenue;
	}

	public String getVenueErrorMessage() {
		return venueErrorMessage;
	}

	public void setVenueErrorMessage(String venueErrorMessage) {
		this.venueErrorMessage = venueErrorMessage;
	}

	public String getBloodGroupErrorMessage() {
		return bloodGroupErrorMessage;
	}

	public void setBloodGroupErrorMessage(String bloodGroupErrorMessage) {
		this.bloodGroupErrorMessage = bloodGroupErrorMessage;
	}
}
*/