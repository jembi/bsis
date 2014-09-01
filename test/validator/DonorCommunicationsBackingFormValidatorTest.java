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
	public void validate_shouldFailIfNoBloodGroupAndDonorPanelSpecified() {
		
 		List<Location> donorPanels = null;
		List<String> bloodGroups = new ArrayList<String>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
				donorPanels, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
		
		errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
		donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
		if(errors.hasErrors())
		{
			assertEquals("Validation fails at 2 points - no donor panel selected and no blood group selected",2,errors.getErrorCount());
			assertNotNull("Validation error exists - no Donor Panel selected", errors.getFieldError("donorPanelErrorMessage"));
			assertNotNull("Validation error exists - no Blood Group selected", errors.getFieldError("bloodGroupErrorMessage"));
		}
	}
	
	/**
	 *  Should fail if no donor panel specified
	 *  
	 *  validate donorCommunicationsBackingFormValidator.
	 
	@Test
	public void validate_shouldFailIfNoDonorPanelSpecified() {
		
 		List<Location> donorPanels = null;
		List<String> bloodGroups = new ArrayList<String>();
		//Search with  BloodGroup 'O+'
		bloodGroups.add("O+");
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
				donorPanels, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
		
		errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
		donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
		if(errors.hasErrors())
		{
			assertEquals("Validation fails at 1 points - no donor panel selected",1,errors.getErrorCount());
			assertNotNull("Validation error exists - no Donor Panel selected", errors.getFieldError("donorPanelErrorMessage"));
		}
	}
	
	/**
	 *  Should fail if no blood group specified
	 *  
	 *  validate donorCommunicationsBackingFormValidator.
	 
	@Test
	public void validate_shouldFailIfNoBloodGroupSpecified() {
		
 		List<Location> donorPanels = new ArrayList<Location>();
		List<String> bloodGroups = new ArrayList<String>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		//Search with donor panel id 3
		long[] id = { 3 };
		
		donorPanels    =  createDonerPanelListForSearch(id);
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = setValueInDonorCommunicationsBackingForm(
				donorPanels, bloodGroups, clinicDate, lastDonationFromDate,lastDonationToDate, anyBloodGroup);
		
		errors = new BindException(donorCommunicationsBackingForm, "donorCommunicationsBackingForm");
		donorCommunicationsBackingFormValidator.validate(donorCommunicationsBackingForm, errors);
		if(errors.hasErrors())
		{
			assertEquals("Validation fails at 1 points - no blood group selected",1,errors.getErrorCount());
			assertNotNull("Validation error exists - no Blood Group selected", errors.getFieldError("bloodGroupErrorMessage"));
		}
	}
	
	
	private DonorCommunicationsBackingForm setValueInDonorCommunicationsBackingForm(List<Location> donorPanels ,List<String> bloodGroups,
			String clinicDate ,String lastDonationFromDate ,String lastDonationToDate ,String anyBloodGroup )
	{
		DonorCommunicationsBackingForm donorCommunicationsBackingForm = new DonorCommunicationsBackingForm();
		donorCommunicationsBackingForm.setDonorPanels(donorPanels);
		donorCommunicationsBackingForm.setBloodGroups(bloodGroups);
		donorCommunicationsBackingForm.setClinicDate(clinicDate);
		donorCommunicationsBackingForm.setLastDonationFromDate(lastDonationFromDate);
		donorCommunicationsBackingForm.setLastDonationToDate(lastDonationToDate);
		donorCommunicationsBackingForm.setAnyBloodGroup(anyBloodGroup);
		
		return donorCommunicationsBackingForm;
	}
	
	private List<Location> createDonerPanelListForSearch(long[] id) {
		List<Location> donorPanel = new ArrayList<Location>();

		for (long locId : id) {
			Location location = new Location();
			location.setId(locId);
			donorPanel.add(location);
		}
		return donorPanel;
	}
}
* */
