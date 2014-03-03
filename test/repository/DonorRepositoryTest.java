package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
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
import org.springframework.validation.Validator;

import security.LoginUserService;
import security.V2VUserDetails;
import util.Verifies;
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
		updatedbid = 224;// For update record set db id here.
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

	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Fist Name)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistFirstNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setFirstName("");//Here we can pass donor firstname value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor First Name is blank. Donor object should not persist.", errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Last Name)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistLastNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setLastName("");//Here we can pass donor lastname value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Last Name is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Duplicate Donor Number)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistDuplicateDonorNumber() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000292");//Here we can pass duplicate donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Duplicate Donor Number exist. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Donor Number).", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistBlankDonorBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("");//Here we can pass blank donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Number is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
		
	@Test
	@Verifies(value = "should fail donor is missing required fields(Gender)", method = "addDonor(Donor)")
	public void addDonor_ShouldNotPersistBlankGender() {
		Date date = new Date();
		donorBackingForm.setAddress("myaddress");
		donorBackingForm.setFirstName("firstname");
		donorBackingForm.setMiddleName("middlename");
		donorBackingForm.setLastName("lastname");
		donorBackingForm.setIsDeleted(false);
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
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's Gender is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Age is Less than 16)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistMinimumAgeIsLessThan16() {
		donorBirthdate = "10/06/2000";//Calculate Donor age is less than 16.
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's age is less than 16. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	//@Test
	@Verifies(value = "should fail donor is missing required fields(Age is greater than 65)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistMaximumAgeIsGreaterThan65() {
		donorBirthdate = "24/02/1948";//Calculate Donor age is greater than 65.
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's age is greater than 65. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.addDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	
	@Test
	@Verifies(value = "Donor Object should persist.", method = "addDonor(Donor)")
	public void addDonor_shouldPersist() {
		setBackingFormValue(donorBackingForm);
		donorRepository.addDonor( donorBackingForm.getDonor());
		assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
	}
	
	
	
	@Test
	@Verifies(value = "should return empty List , search string is less than 2 characters.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_stringLengthLessthan2() {
		assertEquals("Search String length is less than 2,List size should be zero.", 
				donorRepository.findAnyDonorStartsWith("F").size(),0);
	}
	
	@Test
	@Verifies(value = "should allow search if search string is 2 or more characters.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_stringLengthGreaterthan2() {
		assertNotSame("Search String length is Greater than 2,List size should not zero.",donorRepository.findAnyDonorStartsWith("Fi").size(),0);
	}
	
	@Test
	@Verifies(value = "should return empty list (instead of a null object) when no match is found.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_NoMatchingRecordFound() {
		assertEquals("List size should be zero,because matching record is not found.",donorRepository.findAnyDonorStartsWith("xx").size(),0);
	}
	

	@Test
	@Verifies(value = "should fetch all donors having a donor number that partially matches the search string.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_searchWithDonorNumber() {
		assertNotSame("List size should not zero,because partically matching donor number is found.",donorRepository.findAnyDonorStartsWith("00").size(),0);
	}
	
	
	@Test
	@Verifies(value = "should not include soft deleted record.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_softDeleteRecordNotInclude() {
		donorRepository.deleteDonor(228l);
		assertEquals("List size should be zero because matching donornumber record is softdeleted.",donorRepository.findAnyDonorStartsWith("000233").size(),0);
	}
	
	
	@Test
	@Verifies(value = "should fetch all donors having a first name that partially matches the search string.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_searchWithDonorFirstNameMatch() {
		assertNotSame("List size should not zero,because partically matching firstname is found.",donorRepository.findAnyDonorStartsWith("fi").size(),0);
	}
	
	
	@Test
	@Verifies(value = "should fetch all donors having a last name that partially matches the search string.", method = "findAnyDonorStartsWith(String)")
	public void testFindAnyDonorStartWith_searchWithDonorLastNameMatch() {
		assertNotSame("List size should not zero,because partically matching lastname is found.",donorRepository.findAnyDonorStartsWith("la").size(),0);
	}


	
	@Test
	@Verifies(value = "should generate 12 Digit Unique Number prefix with D-.", method = "generateUniqueDonorNumber()")
	public void generateUniqueDonorNumber_nonEmptyString() {
		assertFalse("should generate 12 Digit Unique Number prefix with D-.",StringUtils.isEmpty(DonorRepository
				.generateUniqueDonorNumber()));
	}
	
	@Test
	@Verifies(value = "Donor UniqueNumber total length should be 14.", method = "generateUniqueDonorNumber()")
	public void generateUniqueDonorNumber_nonEmptyStringLength14() {
		assertEquals("Unique Donor Number length should be 14.",14,(DonorRepository
				.generateUniqueDonorNumber()).length());
	}
	
	
	@Test
	@Verifies(value="should return null when Donor with given Donor Number does not exist",method="findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_donorObjectShouldNull() {
		assertNull("Donor object should null.",donorRepository.findDonorByDonorNumber("-1", true));
	}
	
	

	@Test
	@Verifies(value="should return deleted donor because IsDelete true.",method="findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_deleteDonorObjectShouldNotNullDonorDeleteTrue() {
		assertNotNull("Donor object should not null.",donorRepository.findDonorByDonorNumber("000231", true));
	}
	
	@Test
	@Verifies(value="should return null, donor has deleted and IsDelete false.",method="findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_deleteDonorObjectShouldNotNullDonorDeleteFalse() {
		assertNull("Donor object should return null.",donorRepository.findDonorByDonorNumber("000231", false));
	}
	
	@Test
	@Verifies(value="should return  donor because IsDeleted false.",method="findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeletefalse() {
		assertNotNull("Donor object should not null.",donorRepository.findDonorByDonorNumber("000232", false));
	}
	
	@Test
	@Verifies(value="should return null,because IsDeleted true.",method="findDonorByDonorNumber(String,boolean)")
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteTrue() {
		assertNull("Donor object should  null.",donorRepository.findDonorByDonorNumber("000232", true));
	}
	
	

	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Fist Name)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistFirstNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setFirstName("");//Here we can pass donor firstname value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor First Name is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Last Name)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistLastNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setLastName("");//Here we can pass donor lastname value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Last Name is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Duplicate Donor Number)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistDuplicateDonorNumber() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000321");//Here we can pass duplicate donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Duplicate Donor Number exist. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Donor Number)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistBlankDonorBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("");//Here we can pass blank donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donornumber is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Gender)", method = "saveDonor(Donor)")
	public void saveDonor_ShouldNotPersistBlankGender() {
		Date date = new Date();
		donorBackingForm.setAddress("myaddress");
		donorBackingForm.setFirstName("firstname");
		donorBackingForm.setMiddleName("middlename");
		donorBackingForm.setLastName("lastname");
		donorBackingForm.setIsDeleted(false);
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
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's Gender is blank. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Age is Less than 16)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistMinimumAgeIsLessThan16() {
		donorBirthdate = "10/06/2000";//Calculate Donor age is less than 16.
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's age is less than 16. Donor object should not persist.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Age is greater than 65)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistMaximumAgeIsGreaterThan65() {
		donorBirthdate = "24/02/1948";//Calculate Donor age is greater than 65.
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's age is greater than 65.",errors.hasErrors());
		if(!errors.hasErrors()){
			donorRepository.saveDonor( donorBackingForm.getDonor());
			assertTrue("Donor Object should persist. Donor object should not persist.",donor.getId() == 0 ? false : true);
		}
	}
	
	
	@Test
	@Verifies(value = "Donor Object should persist.", method = "saveDonor(Donor)")
	public void saveDonor_shouldPersist() {
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		donorRepository.saveDonor(donorBackingForm.getDonor());
		assertTrue(donorBackingForm.getDonor().getId() == 0 ? false : true);
	}
	
		@Test
		@Verifies(value="Donor object should null when Donor Number does match with an existing Donor",method="updateDonor(Donor)")
		public void updateDonor_shouleReturnNull() {
			Donor editDonor=null;
			try{
			this.userAuthentication();
			editDonor = donorRepository.findDonorById(-1l);
			assertNull("Should return null because donor's Id does not exist.",editDonor);
			}catch(Exception w){
				assertNull("Should return null because donor's Id does not exist.",editDonor);
			}
		}
		
		@Test
		@Verifies(value="Donor object should not null when Donor Number match with an existing Donor",method="updateDonor(Donor)")
		public void updateDonor_shouleReturnNotNull() {
			this.userAuthentication();
			Donor editDonor = donorRepository.findDonorById(722l);
			donorBackingForm = new DonorBackingForm(editDonor);
			setBackingUpdateFormValue(donorBackingForm);
			errors = new BindException(donorBackingForm, "donor");
			donorBackingFormValidator.validate(donorBackingForm, errors);
			assertFalse("Donor Object validtion is fail.",errors.hasErrors());
			assertNotNull("Donor Object should update.",donorRepository.updateDonor(donorBackingForm.getDonor()));
		}
	
	
		
		@Test
		@Verifies(value="should return all non-deleted donors in the database",method="getAllDonors()")
		public void testgetAllDonors() {
			List<Donor> lists = donorRepository.getAllDonors();
			for (Donor donor : lists) {
				//If soft deleted object is added into list then test case failed.
				assertFalse("Soft Deleted donor id should not return.",donor.getIsDeleted());
			}
		}
		
		
		@Test
		@Verifies(value="should return donor with given id",method="findDonorById(Long)")
		public void findDonorByIdLong_shouldReturnDonor() {
			assertNotNull("Donor Object should not null.",donorRepository.findDonorById(675l));
		}
		
		@Test
		@Verifies(value="should return null when Donor with id does not exist",method="findDonorById(Long)")
		public void findDonorByIdLong_shouldReturnNull() {
			Donor donor = null;
			try{
				donor = donorRepository.findDonorById(-1l);
			assertNull("Donor Object should null.",donor);
			}catch(Exception e){
				assertNull("Donor Object should null.",donor);
			}
		}
		
		@Test
		@Verifies(value="should return null when Donor has been deleted",method="findDonorById(Long)")
		public void testFindDonorByIdLong_shouldReturnNullDonorIsDeleted() {
			Donor donor = null;
			try{
				donorRepository.deleteDonor(676l);
				donor = donorRepository.findDonorById(676l);
			assertNull("Donor Object should null.",donor);
			}catch(Exception e){
				assertNull("Donor Object should null.",donor);
			}
		}
		
		@Test
		@Verifies(value="should return donor with given id",method="findDonorById(String)")
		public void findDonorByString_shouldReturnDonor() {
			assertNotNull("Donor Object should not null.",donorRepository.findDonorById(String.valueOf("675")));
		}
		
		@Test
		@Verifies(value="should return null when Donor with id does not exist",method="findDonorById(String)")
		public void findDonorByIdString_shouldReturnNull() {
			Donor donor = null;
			try{
				donor = donorRepository.findDonorById(String.valueOf("-1"));
			assertNull("Donor Object should null.",donor);
			}catch(Exception e){
				assertNull("Donor Object should null.",donor);
			}
		}
		
		@Test
		@Verifies(value="should return null when Donor has been deleted",method="findDonorById(String)")
		public void findDonorByIdString_shouldReturnNullDonorIsDeleted() {
			Donor donor = null;
			try{
				donorRepository.deleteDonor(676l);
				donor = donorRepository.findDonorById(String.valueOf("676"));
			assertNull("Donor Object should null.",donor);
			}catch(Exception e){
				assertNull("Donor Object should null.",donor);
			}
		}
		
		
		
		@Test
		@Verifies(value="should return donor when given Donor Number is not match.",method="findDonorByNumber(String)")
		public void findDonorByNumber_shouldReturnDonorObject() {
			assertNotNull(donorRepository.findDonorByNumber("001406"));
		}
		
		@Test
		@Verifies(value="should return null when given Donor Number is not match.",method="findDonorByNumber(String)")
		public void findDonorByNumber_shouldReturnNull() {
			Donor fetchDonor = null;
			try{
				fetchDonor = donorRepository.findDonorByNumber("001356");
				assertNull("Donor Object should null.",fetchDonor);
			}catch(Exception e){
				assertNull("Donor Object should null.",fetchDonor);
			}
			
		}
		
		@Test
		@Verifies(value="should return null when Donor has been deleted",method="findDonorByNumber(String)")
		public void findDonorByNumber_shouldReturnNullDonorIsDeleted() {
			Donor fetchDonor = null;
			try{
				fetchDonor = donorRepository.findDonorByNumber("001356");
				assertNull("Donor Object should null.",fetchDonor);
			}catch(Exception e){
				assertNull("Donor Object should null.",fetchDonor);
			}
			
		}
		
		
		
		@Test
		@Verifies(value="should not delete donor from database because Donor Id is invalid.",method="deleteDonor(long)")
		public void deleteDonor_donorIdInValid() {
			boolean isDelete = true;
			try{
			donorRepository.deleteDonor(-1l);
			}catch(Exception e){
				isDelete = false;
			}finally{
				assertFalse("Soft Deleted operation is failed.",isDelete);
			}
		}
		
		
		@Test
		@Verifies(value="should delete donor from database.",method="deleteDonor(long)")
		public void deleteDonor_shouldSoftDeleteDonorFromDatabase() {
			boolean isDelete = true;
			try{
			donorRepository.deleteDonor(226l);
			}catch(Exception e){
				isDelete = false;
			}finally{
				assertFalse("SoftDelete operation is completed successfully.",isDelete);
			}
		}
		
		
		
		
		@Test
		@Verifies(value="DeferDonor should not persist.Invalid Donor Id.",method="deferDonor(String,String,String,String)")
		public void deferDonor_ShouldNotPersistInvalidDonorId() {
			boolean isScucess = false;
			try {
				this.userAuthentication();
				donorRepository.deferDonor("1001",
						"28/02/2013", "1", "text");
				isScucess = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				isScucess = false;
			}catch(Exception e){
				e.printStackTrace();
				isScucess = false;
			}finally {
				assertFalse("DeferDonor should not persist. Donor Id is invalid.",isScucess);
			}
		}
		
		@Test
		@Verifies(value="DeferDonor should not persist.Invalid Reason Id.",method="deferDonor(String,String,String,String)")
		public void deferDonor_ShouldNotPersistInvalidReasonId() {
			boolean isScucess = false;
			try {
				this.userAuthentication();
				donorRepository.deferDonor("1001",
						"28/02/2013", "15", "text");
				isScucess = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				isScucess = false;
			}catch(Exception e){
				e.printStackTrace();
				isScucess = false;
			}finally {
				assertFalse("DeferDonor should not persist. Invalid Reason Id.",isScucess);
			}
		}
		
		@Test
		@Verifies(value="DeferDonor should not persist.Invalid Donor Until Date.",method="deferDonor(String,String,String,String)")
		public void deferDonor_ShouldNotPersistInvalidUntilDate() {
			boolean isScucess = false;
			try {
				this.userAuthentication();
				donorRepository.deferDonor("682",
						"28/02/2012", "1", "text");
				isScucess = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				isScucess = false;
			}catch(Exception e){
				e.printStackTrace();
				isScucess = false;
			}finally {
				assertFalse("DeferDonor should not persist. Until Date is invlid.",isScucess);
			}
		}
		
		@Test
		@Verifies(value="DeferDonor should persist.",method="deferDonor(String,String,String,String)")
		public void deferDonor_ShouldPersist() {
			boolean isScucess = false;
			try {
				this.userAuthentication();
				donorRepository.deferDonor("491",
						"19/07/2015", "3", "");
				isScucess = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				isScucess = false;
			}catch(Exception e){
				isScucess = false;
			}finally {
				assertTrue("Defer Donor Should persist.",isScucess);
			}
		}

		@Test
		@Verifies(value="DonorDeferral list size should be zero.",method="getDonorDeferrals(Long)")
		public void getDonorDeferrals_listSizeShouldZero() {
			assertEquals("List size should be zero.",0, donorRepository.getDonorDeferrals(-230l).size());

		}
		
		@Test
		@Verifies(value="DonorDeferral list size should not zero.",method="getDonorDeferrals(Long)")
		public void getDonorDeferrals_listSizeShouldNotZero() {
			assertNotSame("List size should not zero.",0, donorRepository.getDonorDeferrals(230l).size());

		}
		
		@Test
		@Verifies(value="Soft Deleted DeferralReason should not include in list.",method="testGetDeferralReasons()")
		public void testGetDeferralReasons() {
			List<DeferralReason> list = donorRepository.getDeferralReasons();
			if (list != null && list.size() > 0) {
				for (DeferralReason deferralReason : list) {
						assertFalse("Soft Deleted DeferralReason object should not include in list.",deferralReason.getIsDeleted());
				}
			}
		}
		
	
	@Test
	@Verifies(value="List of donor should be persist.",method="addAllDonors(List<Donor>)")
	public void testaddAllDonors() {
		boolean isAdded = false;
			try{
			Donor donor = new Donor();
			DonorBackingForm donorBackingForm = new DonorBackingForm(donor);
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
			for(Donor donorObject:listAllDonor){
				donorRepository.addDonor(donorObject);
			}
			isAdded = true;
			}catch(Exception e){
				isAdded = false;
			}finally{
				assertTrue("List All Donor should persist.",isAdded);
			}
		
	}
	@Test
	@Verifies(value="Method should return true.",method="isCurrentlyDeferred(List<DonorDeferral>)")
	public void donorRepositoryList_methodShouldReturnTrue() {
				assertTrue("Defer donor should found.",donorRepository.isCurrentlyDeferred(donorRepository
						.getDonorDeferrals(335l)));
	}
	
	
	@Test
	@Verifies(value="Method should return true.",method="isCurrentlyDeferred(List<DonorDeferral>)")
	public void donorRepositoryList_methodShouldReturnFalse() {
				assertFalse("Defer donor should not found.",donorRepository.isCurrentlyDeferred(donorRepository
						.getDonorDeferrals(-451l)));
	}
	
	


	@Test
	@Verifies(value="Method should return true.",method="isCurrentlyDeferred(Donor)")
	public void donorRepositoryDonor_methodShouldReturnTrue() {
				assertTrue("Defer donor should found.",donorRepository.isCurrentlyDeferred(donorRepository
						.findDonorById(335l)));
	}
	
	
	@Test
	@Verifies(value="Method should return true.",method="isCurrentlyDeferred(Donor)")
	public void donorRepositoryDonor_methodShouldReturnFalse() {
				assertFalse("Defer donor should not found.",donorRepository.isCurrentlyDeferred(donorRepository
						.findDonorById(-335l)));
	}


	
	
	public void setPaginationParam(Map<String, Object> pagingParams){
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", "0");
		pagingParams.put("sortColumnId", "0");
		pagingParams.put("length", "10");
		pagingParams.put("sortDirection", "asc");
	}

	@Test
	@Verifies(value="List size should not zero.",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_listSizeShouldNotZero() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		setPaginationParam(pagingParams);
		assertNotSame("List size should not zero.",0,donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).size());
	}
	
		
	@Test
	@Verifies(value="should return empty list (instead of a null object) when no match is found.",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_listSizeShouldZero() {
		String searchDonorNumber = "";
		String donorFirstName = "xxx";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		setPaginationParam(pagingParams);
		assertEquals("List size should be zero.",0,((List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0))).size());
		
	}
	
	@Test
	@Verifies(value="should fetch all donors that partially match first name.",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_listSizeShouldNotZeroPartialFirstNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "Fir";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		setPaginationParam(pagingParams);
		assertNotSame("List size should not zero.",0,((List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0))).size());
		
	}
	
	@Test
	@Verifies(value="should fetch all donors that partially match last name.",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_listSizeShouldNotZeroPartialLastNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "Las";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		
		setPaginationParam(pagingParams);
		assertNotSame("List size should not zero.",0,((List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0))).size());
		
	}
	
	@Test
	@Verifies(value="should fetch all donors with blood groups that match the criteria in List<BloodGroup>.",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_listSizeShouldNotZeroMatchBloodGroup() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "false";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		setPaginationParam(pagingParams);
		assertNotSame("List size should not zero.",0,((List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0))).size());
		
	}
	
	@Test
	@Verifies(value="should fetch donors with no blood groups when anyBloodGroup.equals(true).",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_listSizeShouldNotZeroAnyBloodGroupMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		setPaginationParam(pagingParams);
		List<Donor> listDonor = (List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0));
	}
	
	@Test
	@Verifies(value="should not return donors who have been deleted.",method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)")
	public void findAnyDonor_deleteObjectShouldNotPartOfList() {
		String searchDonorNumber = "";
		String donorFirstName = "fir";
		String donorLastName = "";
		String anyBloodGroup = "true";
		deletedbid=743;
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		donorRepository.deleteDonor(deletedbid);
		setPaginationParam(pagingParams);
		List<Donor> listDonor = (List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0));
		for(Donor donor:listDonor){
				assertFalse("Deleted Donor should not included in the list.",donor.getId()==deletedbid?true:false);
		}
	}

	@Test
	@Verifies(value="should return last donor derferral date.",method="getLastDonorDeferralDate(long)")
	public void getLastDonorDeferralDate_shouldReturnlastDeferredUntil(){
		assertNotNull("should return last donor derferral date", donorRepository.getLastDonorDeferralDate(682l));
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
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setBirthDateEstimated(true);
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
