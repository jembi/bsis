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
		assertTrue("Donor First Name is blank.", errors.hasErrors());
	}
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Last Name)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistLastNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setLastName("");//Here we can pass donor lastname value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Last Name is blank.",errors.hasErrors());
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Duplicate Donor Number)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistDuplicateDonorNumber() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000292");//Here we can pass duplicate donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Duplicate Donor Number exist.",errors.hasErrors());
	}
	
	
	
	@Test
	@Verifies(value = "Donor Number is blank. Donor Object should persist.", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistBlankDonorBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("");//Here we can pass blank donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Number is blank. Donor should persist.",errors.hasErrors());
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
		assertTrue("Donor's Gender is blank.",errors.hasErrors());
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
		assertTrue("Donor's age is less than 16.",errors.hasErrors());
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Age is greater than 65)", method = "addDonor(Donor)")
	public void addDonor_shouldNotPersistMaximumAgeIsGreaterThan65() {
		donorBirthdate = "24/02/1948";//Calculate Donor age is greater than 65.
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's age is greater than 65.",errors.hasErrors());
	}
	
	
	
	@Test
	@Verifies(value = "Donor Object should persist.", method = "addDonor(Donor)")
	public void addPersis_shouldPersist() {
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setFirstName("Add Donor");
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertFalse("Donor Object validation is Failed.",errors.hasErrors());
		donorRepository.addDonor( donorBackingForm.getDonor());
		assertTrue("Donor Object should persist.",donor.getId() == 0 ? false : true);
	}
	
	
	
	@Test
	@Verifies(value = "should return empty List , search string is less than 2 characters.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_stringLengthLessthan2() {
		assertEquals("Search String is less than 2,List size should be zero.", 
				donorRepository.findAnyDonorStartsWith("Fll").size(),0);
	}
	
	@Test
	@Verifies(value = "should allow search if search string is 2 or more characters.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_stringLengthGreaterthan2() {
		assertNotSame("Search String is Greater than 2,List size should not zero.",donorRepository.findAnyDonorStartsWith("Fi").size(),0);
	}
	
	@Test
	@Verifies(value = "should return empty list (instead of a null object) when no match is found.", method = "findAnyDonorStartsWith(String)")
	public void findAnyDonorStartsWith_NoMatchingRecordFound() {
		assertEquals("List size should not zero,because matching record is found.",donorRepository.findAnyDonorStartsWith("xx").size(),0);
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
		assertTrue("Donor First Name is blank.",errors.hasErrors());
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Blank Last Name)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistLastNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setLastName("");//Here we can pass donor lastname value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Last Name is blank.",errors.hasErrors());
	}
	
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Duplicate Donor Number)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistDuplicateDonorNumber() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000321");//Here we can pass duplicate donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Duplicate Donor Number exist.",errors.hasErrors());
	}
	
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Duplicate Donor Number)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistBlankDonorBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("");//Here we can pass blank donor number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor object should not persist because donornumber is blank.",errors.hasErrors());
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
		assertTrue("Donor's Gender is blank.",errors.hasErrors());
	}
	
	
	@Test
	@Verifies(value = "should fail donor is missing required fields(Age is Less than 16)", method = "saveDonor(Donor)")
	public void saveDonor_shouldNotPersistMinimumAgeIsLessThan16() {
		donorBirthdate = "10/06/2000";//Calculate Donor age is less than 16.
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor's age is less than 16.",errors.hasErrors());
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
	}
	
	
	@Test
	@Verifies(value = "Donor Object should persist.", method = "saveDonor(Donor)")
	public void saveDonor_shouldPersist() {
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setFirstName("Save Donor");
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertFalse("Donor Object validation is Failed.",errors.hasErrors());
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
			Donor editDonor = donorRepository.findDonorById(675l);
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
				donor = donorRepository.findDonorById(676l);
			assertNull("Donor Object should null.",donor);
			}catch(Exception e){
				assertNull("Donor Object should null.",donor);
			}
		}
		
		
		
		@Test
		@Verifies(value="should return donor with given Donor Number",method="findDonorByNumber(String)")
		public void findDonorByNumber_shouldReturnDonorObject() {
			assertNotNull(donorRepository.findDonorByNumber("001406"));
		}
		
		@Test
		@Verifies(value="should return null with given Donor Number",method="findDonorByNumber(String)")
		public void findDonorByNumber_shouldReturnNull() {
			Donor fetchDonor = null;
			try{
				fetchDonor = donorRepository.findDonorByNumber("-1");
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
				fetchDonor = donorRepository.findDonorByNumber("-1");
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
				assertFalse("Soft Delete operation is completed successfully.",isDelete);
			}
		}
		
		
		@Test
		@Verifies(value="should delete donor from database.",method="deleteDonor(long)")
		public void testdeferDonorDonorIdInvalid() {
			boolean isScucess = false;
			try {
				this.userAuthentication();
				donoridfordefer=-1;
				donorRepository.deferDonor(String.valueOf(donoridfordefer),
						"28/02/2014", "2", "text");
				isScucess = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				isScucess = false;
			}catch(Exception e){
				isScucess = false;
			}finally {
				assertFalse(isScucess);
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
				assertFalse("DeferDonor should not persist. Donor Id is invlid.",isScucess);
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
		
		/**Purpose: Test deferDonor(String,String,String,String) method
		 * Description:
		 * If user authentication is failed through authentication() method,object should not persist. 
		 * Expected Value:
		 * DonorDeferal object should  persist.
		 */

		//@Test
		public void testdeferDonor() {
			boolean isScucess = false;
			try {
				this.userAuthentication();
				donoridfordefer=338;
				donorRepository.deferDonor(String.valueOf(donoridfordefer),
						"28/06/2014", "1", "");
				isScucess = true;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println(" Ex. testdeferDonor()");
				isScucess = false;
			}catch(Exception e){
				System.out.println(" Ex. testdeferDonor()");
				isScucess = false;
			}finally {
				assertTrue(isScucess);
			}
		}

		/**
		 * Purpose: Test getDonorDeferrals(Long) method
		 * Description:
		 * Here we can pass Invalid donorId,so that list size should be zero.
		 * Expected Result:
		 * List size should be zero.
		 */
		//@Test
		public void testGetDonorDeferralsDonorIdIsInvalid() {
			List<DonorDeferral> list = donorRepository.getDonorDeferrals(0l);
			//Here donor Id is invalid so that list size should be zero.
			assertEquals(0, list.size());

		}
		
		/**
		 * Purpose: Test getDonorDeferrals(Long) method
		 * Description:
		 * Here we can pass Invalid donorId,so that list size should be none zero.
		 * Expected Result:
		 * List size should be none zero.
		 */
		//@Test
		public void testGetDonorDeferralsDonorIdIsValid() {
			List<DonorDeferral> list = donorRepository.getDonorDeferrals(230l);
			assertNotSame(0, list.size());

		}
		
		/**
		 * Purpose: Test getDeferralReasons() method
		 * Description:
		 * It should return List<DeferralReason>. Soft deleted record in DeferralReason table should not part of List<DeferralReason>.
		 * Expected Result:
		 * Soft Deleted DeferralReason object should not part of List<DeferralReason>.
		 */

		//@Test
		public void testGetDeferralReasons() {
			List<DeferralReason> list = donorRepository.getDeferralReasons();
			if (list != null && list.size() > 0) {
				for (DeferralReason deferralReason : list) {
					//Soft Delete DeferralReason object should not part of List<DeferralReason>.
						assertFalse(deferralReason.getIsDeleted());
				}
			}
		}
		
	/**
	 * purpose: Test addAllDonors(List<Donor>) method
	 * Description:
	 * Check user input value is valid or not through DonorBackingFormValidator validate(Object,Errors)
	 * method and user input is valid then donor object is added into donor list.
	 * Donor list will be used to persist donor object. First Name,Last Name,Gender(M or F),Birth date should not null.
	 * DonorNumber should be unique and not null. Donor Age should be between 16 to 25.
	 */
	////@Test
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
	 * Purpose:Test isCurrentlyDeferred(List<DonorDeferral>) method
	 * Description:
	 * Here DeferralDonorId is invalid so that List<DonorDeferral> size should be zero and can not findout iscurrentlyDefferal or not. 
	 * DeferDonor calculation is mention as below
	 *  if currentdate >= deferredon and currentdate<=deferredUntil
	 * Expected Result:
	 * List<DonorDeferral> size should be zero.
	 * 
	 */
	//@Test
	public void testisCurrentlyDeferredInValidDonorId() {
		try{
			List<DonorDeferral> list = donorRepository
					.getDonorDeferrals(-1l);
			//Here donorId is invalid so that list size should be zero.
			assertEquals(0, list.size());
			////Below code part is never execute.
			if(list.size()>0)
			{
				assertTrue(donorRepository.isCurrentlyDeferred(list));
			}
		}catch(Exception e){
			assertFalse(false);
		}
	}
	
	/**
	 * Purpose:Test isCurrentlyDeferred(List<DonorDeferral>) method
	 * Description:
	 * Here DeferralDonorId is valid but no matching record is found in donordefferal so that List<DonorDeferral> size should be zero. 
	 * DeferDonor calculation is mention as below
	 *  if currentdate >= deferredon and currentdate<=deferredUntil
	 * Expected Result:
	 * List<DonorDeferral> size should be zero.
	 * 
	 */
	//@Test
	public void testisCurrentlyDeferredValidDonorId() {
		List<DonorDeferral> list = donorRepository
				.getDonorDeferrals(451l);
		//Here donorId is valid but no matching record is found in donordefferal table so that list size should be zero.
		assertEquals(0, list.size());
		////Below code part is never execute.
		if(list.size()>0)
		{
			assertTrue(donorRepository.isCurrentlyDeferred(list));
		}
	}
	
	/**
	 * Purpose:Test isCurrentlyDeferred(List<DonorDeferral>) method
	 * Description:
	 * Here DeferralDonorId is valid and matching record is found in donordef so that List<DonorDeferral> size should not zero. 
	 * And deferal donor should be found. 
	 * DeferDonor calculation is mention as below
	 *  if currentdate >= deferredon and currentdate<=deferredUntil
	 * Expected Result:
	 * List<DonorDeferral> size should not zero. And dereral donor isCurrentlyDeferred(List<DonorDeferral>) method should return true.
	 */
	//@Test
	public void testisCurrentlyDeferredValidDonorIdAndDeferalDonorFound() {
		List<DonorDeferral> list = donorRepository
				.getDonorDeferrals(216l);
		//Here donorId is valid and  List<DonorDeferral> size should not zero.
		assertNotSame(0, list.size());
			//Here record is found in DonorDeferral table and isCurrentlyDeferred(List<DonorDeferral>) method should return true.
			assertTrue(donorRepository.isCurrentlyDeferred(list));
	}
	
	/**
	 * Purpose:Test isCurrentlyDeferred(List<DonorDeferral>) method
	 * Description:
	 * Here DeferralDonorId is valid and matching record is found in donordef so that List<DonorDeferral> size should be none zero. 
	 * And deferal donor should not found. 
	 * DeferDonor calculation is mention as below
	 *  if currentdate >= deferredon and currentdate<=deferredUntil
	 * Expected Result:
	 * List<DonorDeferral> size should not zero. And dereral donor isCurrentlyDeferred(List<DonorDeferral>) method should return false.
	 * 
	 */
	//@Test
	public void testisCurrentlyDeferredValidDonorIdAndDeferalDonorNotFound() {
		List<DonorDeferral> list = donorRepository
				.getDonorDeferrals(337l);
		//Here donorId is invalid so that list size should be zero.
		assertNotSame(0, list.size());
			//Here record is found in DonorDeferral table but isCurrentlyDeferred(List<DonorDeferral>) method should return false.
			assertFalse(donorRepository.isCurrentlyDeferred(list));
	}


	/**
	 * Purpose: Test isCurrentlyDeferred(Donor) method.
	 * Description:
	 * Expected Result:
	 * Here we can pass Invalid DonorId donor object should null.
	 */
	//@Test
	public void testisCurrentlydeferredInValidDonorId() {
		Donor donor = donorRepository.findDonorById(-1l);
		//DororId is invalid donor object should null.
		assertNull(donor);
		//Below code part is never execute.
		if(donor!=null){
		assertTrue(donorRepository.isCurrentlyDeferred(donor));
		}
	}
	
	/**
	 * Purpose: Test isCurrentlyDeferred(Donor) method.
	 * Description:
	 * Expected Result:
	 * DororId is valid but donor object is softdelete so donor object should null.
	 */
	//@Test
	public void testisCurrentlydeferredDeleteDonorId() {
		try{
		Donor donor = donorRepository.findDonorById(-1l);
		//DororId is valid but donor object is softdelete so donor object should null.
				assertNull(donor);
		if(donor!=null){
			assertTrue(donorRepository.isCurrentlyDeferred(donor));
			}
		}catch(Exception e){
			assertFalse(false);
		}
	}
	
	
	/**
	 * Purpose: Test isCurrentlyDeferred(Donor) method.
	 * Description:
	 * Expected Result:
	 * DororId is valid but donor object is softdelete so donor object should null.
	 */
	//@Test
	public void testisCurrentlydeferredValidDonorIdNoRecordFoundIndDeferalDonor() {
		try{
			Donor donor = donorRepository.findDonorById(-1l);
			//DororId is valid  donor object should not null.
					assertNotNull(donor);
				//Donor Id is valid but no matching record found in Donordeferral table so that DonorDeferral object is null. 
					//And isCurrentlyDeferred() method return false. 
				assertFalse(donorRepository.isCurrentlyDeferred(donor));
		}catch(Exception e){
			assertFalse(false);
		}
		}



	/**
	 * Purpose: Test findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>) method
	 * Searching is work on firstname,lastname,donornumber and bloodgroup.
	 * if anyBloodGroup=false then search List<BloodGroup> should consider otherwise not.
	 *  If matching record is found then Object List is return. Otherwise Object list is return with size 0.
	 *  Soft Deleted object should not be return.
	 */
	
	public void setPaginationParam(Map<String, Object> pagingParams){
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", "0");
		pagingParams.put("sortColumnId", "0");
		pagingParams.put("length", "10");
		pagingParams.put("sortDirection", "asc");
	}

	//////@Test
	public void testFindAnyDonor() {
		String searchDonorNumber = "";
		String donorFirstName = "Fir";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		setPaginationParam(pagingParams);
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
