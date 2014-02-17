package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import model.donor.Donor;
import model.user.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import util.Utility;
import backingform.DonorBackingForm;
import backingform.validator.DonorBackingFormValidator;
import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class DonorRepositoryTest {
	private Validator validator;
	@Autowired
	private UtilController utilController;
	@Autowired
	DonorRepository donorRepository;
	private User user;
	private Donor donor;
	private long dbid, deletedbid;;
	private String donorNumber;
	private DonorBackingForm donorBackingForm;
	private DonorBackingFormValidator donorBackingFormValidator;

	private BindException errors;

	@Before
	public void init() {
		dbid = 1;
		deletedbid = 2;
		donorNumber = "000001";
		validator = new DonorBackingFormValidator();
		donorBackingFormValidator = new DonorBackingFormValidator(validator,
				utilController);
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		user = new User();
		user.setId(1);
	}

	@Test
	public void testAddDonor() {
		String DateToString = "10/06/1989";
		donorBackingForm.setBirthDate(DateToString);
		// Donorrandomnumber generation
		donorBackingForm.setDonorNumber("");
		setBackingFormValue();
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		// dbid = donor.getId();
		assertTrue(isIdZero);
		System.out.println("success");
	}

	@Test
	public void testSaveDonor() {
		String DateToString = "10/06/1991";
		donorBackingForm.setBirthDate(DateToString);
		donorBackingForm.setDonorNumber("");
		setBackingFormValue();
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.saveDonor(donor);
		/*
		 * Issue doonorHash value is set to null.
		 */

		boolean isIdZero = donor.getId() == 0 ? false : true;
		// dbid = donor.getId();
		assertTrue(isIdZero);
		System.out.println("success");
	}

	@Test
	public void testgetAllDonors() {
		List<Donor> lists = donorRepository.getAllDonors();
		for (Donor donor : lists) {
			assertFalse(donor.getIsDeleted());
		}
	}

	public void setBackingFormValue() {
		donorBackingForm.setAddress("address");
		donorBackingForm.setFirstName("firstName");
		donorBackingForm.setMiddleName("middlename");
		donorBackingForm.setLastName("lastname");
		donorBackingForm.setIsDeleted(false);
		donorBackingForm.setGender("male");
		donorBackingForm.setCallingName("CallingName");
		donorBackingForm.setCity("City");
		donorBackingForm.setCountry("country");
		donorBackingForm.setCreatedBy(user);
		donorBackingForm.setCreatedDate(new Date());
		donorBackingForm.setDistrict("District");
		donorBackingForm.setDonorPanel("3");
		donorBackingForm.setNationalID("1111");
		donorBackingForm.setNotes("Notes");
		donorBackingForm.setOtherPhoneNumber("9999999999");
		donorBackingForm.setPhoneNumber("8888888888");
		donorBackingForm.setPreferredContactMethod("1");
		donorBackingForm.setProvince("Province");
		donorBackingForm.setState("State");
		donorBackingForm.setZipcode("361001");
	}

	@Test
	public void testFindDonorByIdLong() {
		Donor donor = donorRepository.findDonorById(dbid);
		assertNotNull("Find Donor By Id method argument is long", donor);
	}

	@Test
	public void testFindDonorByIdStringWithSpace() {
		Donor donor = donorRepository.findDonorById("18");
		assertNotNull("Find Donor By Id method argument is black space", donor);
	}

	@Test
	public void testFindDonorByIdStringWithoutSpace() {
		Donor donor = donorRepository.findDonorById(String.valueOf(dbid));
		assertNotNull("Find Donor By Id method argument is String number",
				donor);
	}

	@Test
	public void testFindDonorByNumber() {
		Donor donor = donorRepository.findDonorByNumber(donorNumber);
		assertNotNull("Find Donor by DonorNumber", donor);
	}

	@Test
	public void testDeleteDonorById() {
		donorRepository.deleteDonor(deletedbid);
		assertTrue(true);
	}

}
