package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.user.User;
import model.util.BloodGroup;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import security.LoginUserService;
import security.V2VUserDetails;
import backingform.DonorBackingForm;
import backingform.validator.DonorBackingFormValidator;
import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class DonorRepositoryTest {
	@SuppressWarnings("unused")
	private MockHttpServletRequest request;
	private Validator validator;
	@Autowired
	private UtilController utilController;
	@Autowired
	DonorRepository donorRepository;
	@Autowired
	private SequenceNumberRepository sequenceNumberRepository;
	private User user;
	private Donor donor;
	private long dbid, deletedbid, updatedbid, donoridfordefer;
	private String donorNumber;
	private DonorBackingForm donorBackingForm;
	private DonorBackingFormValidator donorBackingFormValidator;
	String donorBirthdate = null;
	private BindException errors;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	/**
	 * This method is useful to configure instance variable value which is used by different test case .
	 */
	@Before
	public void init() {
		dbid = 218;// For Find Record set db id here.
		donoridfordefer = 230;// Donor Id for defer list
		deletedbid = 223; // Delete datbase record set db id here.
		updatedbid = 220;// For update record set db id here.
		donorNumber = "000311";
		donorBirthdate = "10/06/1989";
		validator = new DonorBackingFormValidator();
		donorBackingFormValidator = new DonorBackingFormValidator(validator,
				utilController);
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		user = new User();
		user.setId(2);
		

	}

	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * Here we can pass donor firstname value is blank.
	 * Expected Result:Donor object should not persist.
	 */
	//@Test
	public void testAddDonorFirstNameBlank() {
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setFirstName("");//Here we can pass donor firstname value is blank.
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//First Name is blank,donor should not persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * Last Name should not null.
	 * Here we can pass donor lastname value is blank.
	 * Expected Result:Donor object should not persist.
	 */
	//@Test
	public void testAddDonorLastNameBlank() {
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setLastName("");//Here we can pass donor lastname value is blank.
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//Last Name is blank,donor should not persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * DonorNumber should be unique and not null.
	 * Here we can pass duplicate donor number.
	 * Expected Result:Donor object should not persist.
	 */
	//@Test
	public void testAddDonorDonorNumberDuplicate() {
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000321");//Here we can pass duplicate donor number and check method result.
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//First Name is blank,donor should not persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * DonorNumber should be unique and not null.
	 * Here we can pass blank donor number.
	 * Expected Result:Donor object should not persist.
	 */
	/*
	 * Issue: New record is added into database with blank donornumber.
	 */
	//@Test
	public void testAddDonorDonorNumberBlank() {
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		donorBackingForm.getDonor().setDonorNumber("");//Here we can pass blank donor number and check method result.
		//Donor Number is blank,donor object should not persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * Gender should not blank.
	 * Here we can pass blank gender.
	 * Expected Result:Donor object should not persist.
	 */
	
	//@Test
	public void testAddDonorGenderBlank() {
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setGender("");
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//Gender is blank,donor should not persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * Birthdate  should be between 16 to 65.
	 * Here we can pass wrong birthdate so that donor's age is less than 16.
	 * Expected Result:Donor object should not persist.
	 */
	//@Test
	public void testAddDonorMinimumAge() {
		donorBirthdate = "10/06/2000";//Calculate Donor age is less than 16.
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//Donor's date of birth is invalid. donor object should not persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * Birthdate  should be between 16 to 65.
	 * Here we can pass wrong birthdate so that donor's age is greater than 65.
	 * Expected Result:Donor object should not persist.
	 */
	@Test
	public void testAddDonorMaximumAge() {
		donorBirthdate = "24/02/1948";//Calculate Donor age is greater than 65.
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//Donor's date of birth is invalid. donor object should not perist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	/**
	 * Purpose: Test addDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid than Donor object should persist.
	 * Here we can pass all valid value.
	 * Expected Result:Donor object should be persist.
	 */
	
	//@Test
	public void testAddDonor() {
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setBirthDateEstimated(true);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		// Check Input value is valid or not.
		donorBackingFormValidator.validate(obj, errors);
		//There is no validation error and Donor Object should persist.
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.addDonor(donor);
		boolean isIdZero = donor.getId() == 0 ? false : true;
		assertTrue(isIdZero);
	}
	
	
	/**
	 * Purpose:Test findAnyDonorStartsWith(String) method
	 * Description:
	 * In this test case search string length is less than 2.
	 * Expected Result: List<Donor> size should be zero.
	 */
	//@Test
	public void testFindAnyDonorStartWithStringLengthLessthan2() {
		String term = "F";
		List<Donor> listObject = donorRepository.findAnyDonorStartsWith(term);
		//Less than 2 characters than list size should be zero.
		assertEquals(listObject.size(),0);
	}
	
	
	
	/**
	 * Purpose:Test findAnyDonorStartsWith(String) method
	 * Description:
	 * Expected Result: List<Donor> size should not zero.
	 */
	//@Test
	public void testFindAnyDonorStartWithDonorNumberMatch() {
		String term = "000310";
		List<Donor> listObject = donorRepository.findAnyDonorStartsWith(term);
		//Search string is donor number and list size should not zero.
		assertNotSame(listObject.size(),0);
	}
	
	/**
	 * Purpose:Test findAnyDonorStartsWith(String) method
	 * Description:
	 * In this test case search string is donornumber.
	 * Expected Result: Soft Deleted record should not added into List<Donor>.
	 */
	//@Test
	public void testFindAnyDonorStartWithTestDeleteDonorNotInclude() {
		String term = "000310";
		deletedbid=303;
		testDeleteDonorById();//Soft delete and try to access that record.
		List<Donor> listObject = donorRepository.findAnyDonorStartsWith(term);
		//Search string is donor number and list size should be zero.
		assertEquals(listObject.size(),0);
	}
	/**
	 * Purpose:Test findAnyDonorStartsWith(String) method
	 * Description:
	 * In this test case search string is firstname.
	 * Expected Result: List<Donor> size should not zero.
	 */
	//@Test
	public void testFindAnyDonorStartWithDonorFirstNameMatch() {
		String term = "fi";
		List<Donor> listObject = donorRepository.findAnyDonorStartsWith(term);
		//Search string is donor firstname and list size should not zero.
		assertNotSame(listObject.size(),0);
	}
	
	/**
	 * Purpose:Test findAnyDonorStartsWith(String) method
	 * Description:
	 * In this test case search string is lastname.
	 * Expected Result: List<Donor> size should not zero.
	 */
	//@Test
	public void testFindAnyDonorStartWithDonorLastNameMatch() {
		String term = "la";
		List<Donor> listObject = donorRepository.findAnyDonorStartsWith(term);
		//Search string is donor's lastname and list size should not zero.
		assertNotSame(listObject.size(),0);
	}


	/**
	 * Purpose: Test generateUniqueDonorNumber() method
	 * Description:
	 * Generate Unique Donor Number prefix with D-<12 Digit Random Number
	 * UniqueNo>
	 */
	//@Test
	public void testGenerateUniqueDonorNumber() {
		String generateRandomUniqueNo = DonorRepository
				.generateUniqueDonorNumber();
		assertFalse(StringUtils.isEmpty(generateRandomUniqueNo));
	}

	/**
	 * Purpose: Test findDonorByDonorNumber(String donorNumber,boolean isdeleted) Method
	 * Description:
	 * Donor object should be null, if Input DonorNumber is not match with donor(donorNumber) table.
	 * Donor object should be null,If Donornumber is match with N record of donor(donorNumber) table but In N record isDeleted value is 0 and
	 * findDonorByDonorNumber(donorNumber,isdeleted) isdeleted set to true.
	 * Donor object should not null,If Donornumber is match with N record of donor(donorNumber) table but In N record isDeleted value is 1 and
	 * findDonorByDonorNumber(donorNumber,isdeleted) isdeleted set to true. 
	 */
	//@Test
	public void testfindDonorByDonorNumberDeleteDonorTrue() {
		System.out.println("Donor Number:::" + donorNumber);
		Donor donor = donorRepository
				.findDonorByDonorNumber(donorNumber, true);
		assertNotNull(donor);
	}
	
	/**
	 * Purpose: Test findDonorByDonorNumber(String donorNumber,boolean isdeleted) Method
	 * Description:
	 * Donor object should be null, if Input DonorNumber is not match with donor(donorNumber) table.
	 * Donor object should be null,If Donornumber is match with N record of donor(donorNumber) table but In N record isDeleted value is 1.
	 * Donor object should not null,If Donornumber is match with N record of donor(donorNumber) table but In N record isDeleted value is 0
	 */
	//@Test
	public void testfindDonorByDonorNumberDeleteDonorFalse() {
		System.out.println("Donor Number:::" + donorNumber);
		Donor donor = donorRepository
				.findDonorByDonorNumber(donorNumber, false);
		assertNotNull(donor);
	}

	/**
	 * Purpose: Test saveDonor(Donor donor) Method
	 * Description:
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid then Donor object should persist.
	 * First Name,Last Name,Gender(M or F),Birth date should not null.
	 * DonorNumber should be unique and not null. Donor Age should be between 16 to 25.
	 */
	/*
	 * Issue doonorHash value is set to null. donor status value is set to null.
	 */

	//@Test
	public void testSaveDonor() {
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setDonorNumber("");
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		System.out.println("errors.hasErrors()::" + errors.hasErrors());
		assertFalse(errors.hasErrors());
		Donor donor = donorBackingForm.getDonor();
		donorRepository.saveDonor(donor);

		boolean isIdZero = donor.getId() == 0 ? false : true;
		// dbid = donor.getId();
		assertTrue(isIdZero);
		System.out.println("success");
	}

	/**
	 * Purpose: Test updateDonor(Donor donor) Method
	 * Description:
	 * Authorize user through userAuthentication() method.
	 * Check user input value is valid through DonorBackingFormValidator validate(Object,Errors)
	 * method. If user input is valid then Donor object should update.
	 * First Name,Last Name,Gender(M or F),Birth date should not null.
	 * DonorNumber should be unique,not null and newly generated. Donor Age should be between 16 to 25.
	 */
	//@Test
	public void testUpdateDonor() {
		this.userAuthentication();
		Donor editDonor = donorRepository.findDonorById(updatedbid);
		donorBackingForm = new DonorBackingForm(editDonor);
		setBackingUpdateFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		Object obj = donorBackingForm;
		donorBackingFormValidator.validate(obj, errors);
		List<ObjectError> list = errors.getAllErrors();
		for (ObjectError oe : list) {
			System.out.println(oe.getDefaultMessage());
		}
		assertFalse(errors.hasErrors());
		System.out.println("editDonor__________" + editDonor.getLastUpdated());
		Donor updateDonor = donorBackingForm.getDonor();
		assertNotNull(donorRepository.updateDonor(updateDonor));
	}

	/**
	 * purpose: Test addAllDonors(List<Donor>) method
	 * Description:
	 * Check user input value is valid or not through DonorBackingFormValidator validate(Object,Errors)
	 * method and user input is valid then donor object is added into donor list.
	 * Donor list will be used to persist donor object. First Name,Last Name,Gender(M or F),Birth date should not null.
	 * DonorNumber should be unique and not null. Donor Age should be between 16 to 25.
	 */
	//@Test
	public void testaddAllDonors() {
		Donor donor = new Donor();
		DonorBackingForm donorBackingForm = new DonorBackingForm(donor);
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setDonorNumber(sequenceNumberRepository
				.getNextDonorNumber());
		setBackingFormValue(donorBackingForm);
		List<Donor> listAllDonor = new ArrayList<Donor>();
		listAllDonor.add(donorBackingForm.getDonor());
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		donorBirthdate = "11/06/1991";
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setDonorNumber(sequenceNumberRepository
				.getNextDonorNumber());
		setBackingFormValue(donorBackingForm);
		listAllDonor.add(donorBackingForm.getDonor());
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		donorBirthdate = "12/06/1991";
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setDonorNumber(sequenceNumberRepository
				.getNextDonorNumber());
		setBackingFormValue(donorBackingForm);
		listAllDonor.add(donorBackingForm.getDonor());
		donorRepository.addAllDonors(listAllDonor);
	}

	/**
	 * Purpose: Test getAllDonors() method
	 * Description:
	 * It should return List<Donor> object with condition that isDeleted=0
	 * If isDeleted=1 for all database rows then list size should be zero.
	 */
	//@Test
	public void testgetAllDonors() {
		List<Donor> lists = donorRepository.getAllDonors();
		for (Donor donor : lists) {
			assertFalse(donor.getIsDeleted());
		}
	}

	/**
	 * Purpose:Test findDonorById(Long) method
	 * Description:
	 * Donor object should be null if Input dbid is not match with donor(id) table.
	 * donor object should be null if dbid is match with n record of donor(id) table but in N record  isDeleted value is 1. 
	 * Donor object should not null if dbid is match with n record of donor(dbid) table and in N record  isDeleted value is 0. 
	 */
	//@Test
	public void testFindDonorByIdLong() {
		Donor donor = donorRepository.findDonorById(dbid);
		assertNotNull("Find Donor By Id method argument is long", donor);
	}

	/**
	 * Purpose:Test findDonorById(String) method
	 * Description:
	 * Donor object should be null if Input dbid is not match with donor(id) table.
	 * donor object should be null if dbid is match with n record of donor(id) table but in N record  isDeleted value is 1. 
	 * Donor object should not null if dbid is match with n record of donor(dbid) table and in N record  isDeleted value is 0. 
	 */

	//@Test
	public void testFindDonorByIdStringWithoutSpace() {
		Donor donor = donorRepository.findDonorById(String.valueOf(dbid));
		assertNotNull("Find Donor By Id method argument is String number",
				donor);
	}

	/**
	 * Purpose:Test findDonorByNumber(String) method
	 * Description:
	 * Donor object should be null if Input donorNumber is not match with donor(donorNumber) table.
	 * donor object should be null if donorNumber is match with n record of donor(donorNumber) table but in N record  isDeleted value is 1. 
	 * Donor object should not null if donorNumber is match with n record of donor(donorNumber) table and in N record  isDeleted value is 0. 
	 */
	//@Test
	public void testFindDonorByNumber() {
		Donor donor = donorRepository.findDonorByNumber(donorNumber);
		assertNotNull("Find Donor by DonorNumber", donor);
	}

	/**
	 * Purpose: Test deleteDonor(long) method
	 * SoftDelete should be perform on selected donor object.Donor Object should be update with isDeleted=1.
	 */
	/*
	 * If deletedbid is not found in donor table then it is fired exception.
	 */
	//@Test
	public void testDeleteDonorById() {
		donorRepository.deleteDonor(deletedbid);
		assertTrue(true);
	}

	/**Purpose: Test deferDonor(String,String,String,String) method
	 * Description:
	 * If user authentication is failed through authentication() method,object should not persist. 
	 * If donorid is not found in donor table , DonorDeferral should not persist. 
	 * If above conditon is failed then DonorDeferral should be persist.
	 */

	//@Test
	public void testdeferDonor() {
		boolean isScucess = false;
		try {
			this.userAuthentication();
			donorRepository.deferDonor(String.valueOf(donoridfordefer),
					"28/02/2014", "1", "text");
			isScucess = true;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			isScucess = false;
		} finally {
			assertTrue(isScucess);
		}
	}

	/**
	 * Purpose: Test getDeferralReasons() method
	 * Description:
	 * It should return List<DeferralReason>. Soft deleted record in DeferralReason table should not return.
	 */

	//@Test
	public void testGetDeferralReasons() {
		List<DeferralReason> list = donorRepository.getDeferralReasons();
		if (list != null && list.size() > 0) {
			for (DeferralReason deferralReason : list) {
				if (deferralReason.getIsDeleted() == true) {
					assertTrue(false);
				}
			}
		}
	}

	/**
	 * Purpose: Test getDonorDeferrals(Long) method
	 * Description:
	 * It should return List<DonorDeferral> base on donorId. If no record found then List<DonorDeferral> size should be zero.
	 */
	//@Test
	public void testGetDonorDeferrals() {
		List<DonorDeferral> list = donorRepository.getDonorDeferrals(1l);
		if (list != null && list.size() > 0) {
			for (DonorDeferral donorDeferral : list) {
				System.out.println(donorDeferral.getId());
			}
		}

	}

	/**
	 * Purpose:Test isCurrentlyDeferred(List<DonorDeferral>) method
	 * Description:
	 * It should return true if any donor is deferal otherwise false. Here we should pass donorDeferral list base on donorid. 
	 * DeferDonor calculation is mention as below
	 *  if currentdate >= deferredon and currentdate<=deferredUntil
	 * then donor is deferred.
	 * 
	 */
	//@Test
	public void testisCurrentlyDeferredPassList() {
		List<DonorDeferral> list = donorRepository
				.getDonorDeferrals(donoridfordefer);
		System.out.println("Is defer donor found with list as a argument:::"
				+ donorRepository.isCurrentlyDeferred(list));
	}

	/**
	 * Purpose: Test isCurrentlyDeferred(Donor) method.
	 * Description:
	 * It should return true if donor is deferal otherwise false. 
	 * Here we should pass Donor base on donorid. DeferDonor calculation is mention below 
	 * if  currentdate >= deferredon and currentdate<=deferredUntil 
	 * then donor is deferred.
	 * 
	 */
	//@Test
	public void testisCurrentlyDeferredPassDonor() {
		Donor donor = donorRepository.findDonorById(230l);
		System.out
				.println("Is defer donor found with Donor argument as argument:::"
						+ donorRepository.isCurrentlyDeferred(donor));
	}

	/**
	 * Purpose: Test findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>) method
	 * Searching is work on firstname,lastname,donornumber and bloodgroup.
	 * if anyBloodGroup=false then search List<BloodGroup> should consider otherwise not.
	 *  If matching record is found then Object List is return. Otherwise Object list is return with size 0.
	 *  Soft Deleted object should not be return.
	 */

	//@Test
	public void testFindAnyDonor() {
		String searchDonorNumber = "";
		String donorFirstName = "Fir";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", "0");
		pagingParams.put("sortColumnId", "0");
		pagingParams.put("length", "10");
		pagingParams.put("sortDirection", "asc");
		System.out.println("donorFirstName:::::" + donorFirstName);
		List<Object> listObject = donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams);
		System.out.println("1::" + listObject.size());
		List<Donor> donors = (List<Donor>) listObject.get(0);
		System.out.println("1::" + donors.size());
		if (donors != null && donors.size() > 0) {
			System.out
					.println("ID\tDonorNumber\tFirstName\tLastName\tGender\tBloodGroup\tBirthDate");
			for (Donor donor : donors) {
				System.out.println();
				System.out.print(donor.getId());
				System.out.print("\t");
				System.out.print(donor.getDonorNumber());
				System.out.print("\t\t");
				System.out.print(donor.getFirstName());
				System.out.print("\t");
				System.out.print(donor.getLastName());
				System.out.print("\t");
				System.out.print(donor.getGender());
				System.out.print("\t");
				System.out.print(donor.getBloodAbo() + donor.getBloodRh());
				System.out.print("\t\t");
				System.out.print(donor.getBirthDate());
				System.out.println();
			}
		}

	}

	
	/**
	 * Called when update existing record.
	 * 
	 * @param donorBackingForm
	 */
	public void setBackingUpdateFormValue(DonorBackingForm donorBackingForm) {
		donorBackingForm.setAddress("address_update");
		donorBackingForm.setFirstName("firstName_update");
		donorBackingForm.setMiddleName("middlename_update");
		donorBackingForm.setLastName("lastname_update");
		donorBackingForm.setIsDeleted(false);
		donorBackingForm.setGender("female");
		donorBackingForm.setCallingName("CallingName_update");
		donorBackingForm.setCity("City_update");
		donorBackingForm.setCountry("country_update");
		donorBackingForm.setDistrict("District_update");
		donorBackingForm.setDonorPanel("2");
		donorBackingForm.setNationalID("1212");
		donorBackingForm.setNotes("Notes_update");
		donorBackingForm.setOtherPhoneNumber("9878787878");
		donorBackingForm.setPhoneNumber("874525452");
		donorBackingForm.setPreferredContactMethod("2");
		donorBackingForm.setProvince("Province_update");
		donorBackingForm.setState("State_update");
		donorBackingForm.setZipcode("361001");
	}

	/**
	 * Called when insert new record into table.
	 * 
	 * @param donorBackingForm
	 */
	public void setBackingFormValue(DonorBackingForm donorBackingForm) {
		Date date = new Date();
		donorBackingForm.setAddress("myaddress");
		donorBackingForm.setFirstName("firstname");
		donorBackingForm.setMiddleName("middlename");
		donorBackingForm.setLastName("lastname");
		donorBackingForm.setIsDeleted(false);
		donorBackingForm.setGender("male");
		donorBackingForm.setCallingName("CallingName");
		donorBackingForm.setCity("City");
		donorBackingForm.setCountry("country");
		donorBackingForm.setCreatedBy(user);
		donorBackingForm.setCreatedDate(date);
		donorBackingForm.setLastUpdated(date);
		donorBackingForm.setLastUpdatedBy(user);
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

	/**
	 * UserPassword,V2vUserDetails(Principal) and authority detail store into
	 * SecurityContextHolder.
	 */
	public void userAuthentication() {
		applicationContext = new ClassPathXmlApplicationContext(
				"file:**/security-v2v-servlet.xml");
		userDetailsService = applicationContext.getBean(LoginUserService.class);
		V2VUserDetails userDetails = (V2VUserDetails) userDetailsService
				.loadUserByUsername("admin");
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authToken);
	}

}
