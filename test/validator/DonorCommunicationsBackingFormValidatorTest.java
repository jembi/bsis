/**
 * Commented on issue #209[Adapt Bsis To Expose Rest Services]
 * <p>
 * package validator;
 * <p>
 * import static org.junit.Assert.assertEquals;
 * import static org.junit.Assert.assertNotNull;
 * <p>
 * import java.util.ArrayList;
 * import java.util.List;
 * <p>
 * import model.location.Location;
 * <p>
 * import org.junit.Before;
 * import org.junit.Test;
 * import org.junit.runner.RunWith;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.test.context.ContextConfiguration;
 * import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 * import org.springframework.test.context.web.WebAppConfiguration;
 * import org.springframework.validation.BindException;
 * import org.springframework.validation.Validator;
 * <p>
 * import repository.DonorCommunicationsRepository;
 * import backingform.DonorCommunicationsBackingForm;
 * import backingform.validator.DonorCommunicationsBackingFormValidator;
 * import controller.UtilController;
 *
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @ContextConfiguration(locations = "file:**applicationContextTest.xml")
 * @WebAppConfiguration public class DonorCommunicationsBackingFormValidatorTest {
 * @Autowired DonorCommunicationsRepository donorCommunicationsRepository;
 * @Autowired private UtilController utilController;
 * <p>
 * private BindException errors;
 * private Validator validator;
 * private DonorCommunicationsBackingFormValidator donorCommunicationsBackingFormValidator;
 * @Before public void init() {
 * try {
 * validator = new DonorCommunicationsBackingFormValidator();
 * donorCommunicationsBackingFormValidator = new DonorCommunicationsBackingFormValidator(
 * validator, utilController);
 * } catch (Exception e) {
 * e.printStackTrace();
 * }
 * }
 * <p>
 * /**
 * Should fail if no blood group or venue is specified
 * <p>
 * validate donorCommunicationsBackingFormValidator.
 * @Test public void validate_shouldFailIfNoBloodGroupAndVenueSpecified() {
 * <p>
 * List<Location> venues = null;
 * List<String> bloodGroups = new ArrayList<String>();
 * String clinicDate = "";
 * String lastDonationFromDate = "";
 * String lastDonationToDate = "";
 * String anyBloodGroup = "false";
 * <p>
 * DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
 * venues, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
 * <p>
 * errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
 * donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
 * if(errors.hasErrors())
 * {
 * assertEquals("Validation fails at 2 points - no venue selected and no blood group selected",2,errors.getErrorCount());
 * assertNotNull("Validation error exists - no Venue selected", errors.getFieldError("venueErrorMessage"));
 * assertNotNull("Validation error exists - no Blood Group selected", errors.getFieldError("bloodGroupErrorMessage"));
 * }
 * }
 * <p>
 * /**
 * Should fail if no venue specified
 * <p>
 * validate donorCommunicationsBackingFormValidator.
 * @Test public void validate_shouldFailIfNoVenueSpecified() {
 * <p>
 * List<Location> venues = null;
 * List<String> bloodGroups = new ArrayList<String>();
 * //Search with  BloodGroup 'O+'
 * bloodGroups.add("O+");
 * String clinicDate = "";
 * String lastDonationFromDate = "";
 * String lastDonationToDate = "";
 * String anyBloodGroup = "false";
 * <p>
 * DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
 * venues, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
 * <p>
 * errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
 * donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
 * if(errors.hasErrors())
 * {
 * assertEquals("Validation fails at 1 points - no venue selected",1,errors.getErrorCount());
 * assertNotNull("Validation error exists - no Venue selected", errors.getFieldError("venueErrorMessage"));
 * }
 * }
 * <p>
 * /**
 * Should fail if no blood group specified
 * <p>
 * validate donorCommunicationsBackingFormValidator.
 * @Test public void validate_shouldFailIfNoBloodGroupSpecified() {
 * <p>
 * List<Location> venues = new ArrayList<Location>();
 * List<String> bloodGroups = new ArrayList<String>();
 * String clinicDate = "";
 * String lastDonationFromDate = "";
 * String lastDonationToDate = "";
 * String anyBloodGroup = "false";
 * //Search with venue id 3
 * long[] id = { 3 };
 * <p>
 * venues    =  createVenuesListForSearch(id);
 * DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
 * venues, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
 * <p>
 * errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
 * donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
 * if(errors.hasErrors())
 * {
 * assertEquals("Validation fails at 1 points - no blood group selected",1,errors.getErrorCount());
 * assertNotNull("Validation error exists - no Blood Group selected", errors.getFieldError("bloodGroupErrorMessage"));
 * }
 * }
 * <p>
 * <p>
 * private DonorCommunicationsBackingForm setValueInDonorCommunicationsBackingForm(List<Location> venues ,List<String> bloodGroups,
 * String clinicDate ,String lastDonationFromDate ,String lastDonationToDate ,String anyBloodGroup )
 * {
 * DonorCommunicationsBackingForm donorCommunicationsBackingForm = new DonorCommunicationsBackingForm();
 * donorCommunicationsBackingForm.setVenues(venues);
 * donorCommunicationsBackingForm.setBloodGroups(bloodGroups);
 * donorCommunicationsBackingForm.setClinicDate(clinicDate);
 * donorCommunicationsBackingForm.setLastDonationFromDate(lastDonationFromDate);
 * donorCommunicationsBackingForm.setLastDonationToDate(lastDonationToDate);
 * donorCommunicationsBackingForm.setAnyBloodGroup(anyBloodGroup);
 * <p>
 * return donorCommunicationsBackingForm;
 * }
 * <p>
 * private List<Location> createVenuesListForSearch(long[] id) {
 * List<Location> venue = new ArrayList<Location>();
 * <p>
 * for (long locId : id) {
 * Location location = new Location();
 * location.setId(locId);
 * venue.add(location);
 * }
 * return venue;
 * }
 * }
 */
