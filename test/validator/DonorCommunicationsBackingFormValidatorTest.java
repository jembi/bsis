/**
 * Commented on issue #209[Adapt Bsis To Expose Rest Services]
 * 
 * package validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import model.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

import repository.DonorCommunicationsRepository;
import backingform.DonorCommunicationsBackingForm;
import backingform.validator.DonorCommunicationsBackingFormValidator;
import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**applicationContextTest.xml")
@WebAppConfiguration
public class DonorCommunicationsBackingFormValidatorTest {
	
	@Autowired
	DonorCommunicationsRepository donorCommunicationsRepository;
	@Autowired
	private UtilController utilController;
	
	private BindException errors;
	private Validator validator;
	private DonorCommunicationsBackingFormValidator donorCommunicationsBackingFormValidator;
	
	@Before
	public void init() {
		try {
			validator = new DonorCommunicationsBackingFormValidator();
			donorCommunicationsBackingFormValidator = new DonorCommunicationsBackingFormValidator(
					validator, utilController);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  Should fail if no blood group or donor panel is specified
	 *  
	 *  validate donorCommunicationsBackingFormValidator.
	 
	@Test
	public void validate_shouldFailIfNoBloodGroupAndVenueSpecified() {
		
 		List<Location> venues = null;
		List<String> bloodGroups = new ArrayList<String>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
				venues, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
		
		errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
		donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
		if(errors.hasErrors())
		{
			assertEquals("Validation fails at 2 points - no donor panel selected and no blood group selected",2,errors.getErrorCount());
			assertNotNull("Validation error exists - no Donor Panel selected", errors.getFieldError("venueErrorMessage"));
			assertNotNull("Validation error exists - no Blood Group selected", errors.getFieldError("bloodGroupErrorMessage"));
		}
	}
	
	/**
	 *  Should fail if no donor panel specified
	 *  
	 *  validate donorCommunicationsBackingFormValidator.
	 
	@Test
	public void validate_shouldFailIfNoVenueSpecified() {
		
 		List<Location> venues = null;
		List<String> bloodGroups = new ArrayList<String>();
		//Search with  BloodGroup 'O+'
		bloodGroups.add("O+");
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
				venues, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
		
		errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
		donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
		if(errors.hasErrors())
		{
			assertEquals("Validation fails at 1 points - no donor panel selected",1,errors.getErrorCount());
			assertNotNull("Validation error exists - no Donor Panel selected", errors.getFieldError("venueErrorMessage"));
		}
	}
	
	/**
	 *  Should fail if no blood group specified
	 *  
	 *  validate donorCommunicationsBackingFormValidator.
	 
	@Test
	public void validate_shouldFailIfNoBloodGroupSpecified() {
		
 		List<Location> venues = new ArrayList<Location>();
		List<String> bloodGroups = new ArrayList<String>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		//Search with donor panel id 3
		long[] id = { 3 };
		
		venues    =  createDonerPanelListForSearch(id);
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
				venues, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
		
		errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
		donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
		if(errors.hasErrors())
		{
			assertEquals("Validation fails at 1 points - no blood group selected",1,errors.getErrorCount());
			assertNotNull("Validation error exists - no Blood Group selected", errors.getFieldError("bloodGroupErrorMessage"));
		}
	}
	
	
	private DonorCommunicationsBackingForm setValueInDonorCommunicationsBackingForm(List<Location> venues ,List<String> bloodGroups,
			String clinicDate ,String lastDonationFromDate ,String lastDonationToDate ,String anyBloodGroup )
	{
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = new DonorCommunicationsBackingForm();
		donorCommunicationsBackingForm.setVenues(venues);
		donorCommunicationsBackingForm.setBloodGroups(bloodGroups);
		donorCommunicationsBackingForm.setClinicDate(clinicDate);
		donorCommunicationsBackingForm.setLastDonationFromDate(lastDonationFromDate);
		donorCommunicationsBackingForm.setLastDonationToDate(lastDonationToDate);
		donorCommunicationsBackingForm.setAnyBloodGroup(anyBloodGroup);
		
		return donorCommunicationsBackingForm;
	}
	
	private List<Location> createDonerPanelListForSearch(long[] id) {
		List<Location> venue = new ArrayList<Location>();

		for (long locId : id) {
			Location location = new Location();
			location.setId(locId);
			venue.add(location);
		}
		return venue;
	}
}
* */
