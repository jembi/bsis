/**
 * commented on issue #209 [Adapt BSIS to Expose Rest Services]**
 * <p>
 * package backingform;
 * <p>
 * import com.fasterxml.jackson.annotation.JsonIgnore;
 * import java.util.ArrayList;
 * import java.util.List;
 * import java.util.regex.Pattern;
 * <p>
 * import model.donor.Donor;
 * import model.location.Location;
 * import model.util.BloodGroup;
 * <p>
 * import org.apache.commons.lang3.StringUtils;
 * <p>
 * import viewmodel.DonorViewModel;
 * <p>
 * public class DonorCommunicationsBackingForm {
 * <p>
 * private Donor donor;
 * <p>
 * private List<BloodGroup> bloodGroups;
 *
 * @JsonIgnore private List<Location> venues;
 * <p>
 * private String clinicDate;
 * <p>
 * private String lastDonationFromDate;
 * <p>
 * private String lastDonationToDate;
 * <p>
 * private String anyBloodGroup;
 * <p>
 * private boolean createDonorSummaryView;
 * <p>
 * private String anyVenue;
 * <p>
 * private String venueErrorMessage;
 * <p>
 * private String bloodGroupErrorMessage;
 * <p>
 * public DonorCommunicationsBackingForm() {
 * donor = new Donor();
 * }
 * <p>
 * public DonorCommunicationsBackingForm(Donor donor) {
 * this.donor = donor;
 * }
 * @JsonIgnore public DonorViewModel getDonorViewModel() {
 * return new DonorViewModel(donor);
 * }
 * <p>
 * public String toString() {
 * return donor.toString();
 * }
 * <p>
 * public String getVenue() {
 * Location venue = donor.getVenue();
 * if (venue == null || venue.getId() == null)
 * return null;
 * return venue.getId().toString();
 * }
 * <p>
 * private static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile(",");
 * <p>
 * public void setVenue(String venue) {
 * if (StringUtils.isBlank(venue)) {
 * donor.setVenue(null);
 * } else {
 * try {
 * List<Location> venues = new ArrayList<Location>();
 * String[] venueStr = COMMA_SPLIT_PATTERN.split(venue);
 * for (String venueId : venueStr) {
 * Location l = new Location();
 * l.setId(Long.parseLong(venueId));
 * venues.add(l);
 * donor.setVenue(l);
 * }
 * venues = venues;
 * } catch (NumberFormatException ex) {
 * ex.printStackTrace();
 * donor.setVenue(null);
 * }
 * }
 * }
 * <p>
 * public String getClinicDate() {
 * return clinicDate;
 * }
 * <p>
 * public void setClinicDate(String clinicDate) {
 * this.clinicDate = clinicDate;
 * }
 * <p>
 * public String getLastDonationFromDate() {
 * return lastDonationFromDate;
 * }
 * <p>
 * public void setLastDonationFromDate(String lastDonationFromDate) {
 * this.lastDonationFromDate = lastDonationFromDate;
 * }
 * <p>
 * public String getLastDonationToDate() {
 * return lastDonationToDate;
 * }
 * <p>
 * public void setLastDonationToDate(String lastDonationToDate) {
 * this.lastDonationToDate = lastDonationToDate;
 * }
 * <p>
 * public List<Location> getVenues() {
 * return venues;
 * }
 * <p>
 * public void setVenues(List<Location> venues) {
 * this.venues = venues;
 * }
 * <p>
 * public String getAnyBloodGroup() {
 * return anyBloodGroup;
 * }
 * <p>
 * public void setAnyBloodGroup(String anyBloodGroup) {
 * this.anyBloodGroup = anyBloodGroup;
 * }
 * <p>
 * public List<BloodGroup> getBloodGroups() {
 * return bloodGroups;
 * }
 * <p>
 * public void setBloodGroups(List<String> bloodGroups) {
 * this.bloodGroups = new ArrayList<BloodGroup>();
 * for (String bg : bloodGroups) {
 * this.bloodGroups.add(new BloodGroup(bg));
 * }
 * }
 * <p>
 * public boolean getCreateDonorSummaryView() {
 * return createDonorSummaryView;
 * }
 * <p>
 * public void setCreateDonorSummaryView(boolean createDonorSummaryView) {
 * this.createDonorSummaryView = createDonorSummaryView;
 * }
 * <p>
 * public String getAnyVenue() {
 * return anyVenue;
 * }
 * <p>
 * public void setAnyVenue(String anyVenue) {
 * this.anyVenue = anyVenue;
 * }
 * <p>
 * public String getVenueErrorMessage() {
 * return venueErrorMessage;
 * }
 * <p>
 * public void setVenueErrorMessage(String venueErrorMessage) {
 * this.venueErrorMessage = venueErrorMessage;
 * }
 * <p>
 * public String getBloodGroupErrorMessage() {
 * return bloodGroupErrorMessage;
 * }
 * <p>
 * public void setBloodGroupErrorMessage(String bloodGroupErrorMessage) {
 * this.bloodGroupErrorMessage = bloodGroupErrorMessage;
 * }
 * }
 */