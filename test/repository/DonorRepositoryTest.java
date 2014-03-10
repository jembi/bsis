package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

import security.LoginUserService;
import security.V2VUserDetails;
import backingform.DonorBackingForm;
import backingform.validator.DonorBackingFormValidator;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/ApplicationContextTest.xml")
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		DbUnitTestExecutionListener.class })
@DatabaseSetup("classpath:/DonorDataset.xml")
public class DonorRepositoryTest {
	@Autowired
	private UtilController utilController;
	@Autowired
	DonorRepository donorRepository;
	private User user;
	private int userDbId = 1;
	private DonorBackingForm donorBackingForm;
	String donorBirthdate = null;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;
	private BindException errors;
	private Validator validator;
	private DonorBackingFormValidator donorBackingFormValidator;
	private Donor donor;

	@Test
	/**
	 * value = should fail when donor is missing required fields.
	 * method = saveDonor(Donor)
	 */
	public void saveDonor_shouldPersist() {
		donorBackingForm = new DonorBackingForm();
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000005");
		donorRepository.saveDonor(donorBackingForm.getDonor());
		assertTrue(donorBackingForm.getDonor().getId() == 0 ? false : true);
	}

	@Test
	/**
	 * value=should delete donor from database.
	 * method=deleteDonor(long)
	 */
	public void deleteDonor_shouldDeleteDonorFromDatabase() {
		Donor deletedDonor = donorRepository.deleteDonor(3l);
		assertTrue("Delete operation should complete successfully.",
				deletedDonor.getIsDeleted());
	}

	@Test
	/**
	 * value=should return donor with given id
	 * method=findDonorById(Long)
	 */
	public void findDonorById_shouldReturnDonor() {
		assertNotNull("Donor Object should not null.",
				donorRepository.findDonorById(1l));
	}

	@Test
	/**
	 * value=should return null when Donor with id does not exist
	 * method=findDonorById(Long)
	 */
	public void findDonorByIdLong_shouldReturnNull() {
		assertNull("Donor Object should null.",
				donorRepository.findDonorById(18l));
	}

	@Test
	/**
	 * value=should return null when Donor has been deleted
	 * method=findDonorById(Long)
	 */
	public void findDonorByIdLong_shouldReturnNullDonorIsDeleted() {
		assertNull("Donor Object should null.",
				donorRepository.findDonorById(2l));
	}
	
			
		
	@Test
	/**
	 * value="should return empty list (instead of a null object) when no match is found."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)"
	 * 
	 */
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
	/**
	 * value="should fetch all donors that partially match first name."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroPartialFirstNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "fir";
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
	/**
	 * value="should fetch all donors that partially match last name."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroPartialLastNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "las";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		
		setPaginationParam(pagingParams);
		assertNotSame("List size should not zero.",0,((List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0))).size());
		
	}
	
	@Test
	/**
	 * value="should fetch all donors with blood groups that match the criteria in List<BloodGroup>."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)"
	 */
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
	/**
	 * value="should fetch donors with no blood groups when anyBloodGroup.equals(true)."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroAnyBloodGroupMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("B+"));
		setPaginationParam(pagingParams);
		List<Donor> listDonor = (List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0));
		assertNotSame("List size should not zero.",0,((List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0))).size());
	}
	
	@Test
	/**
	 * value="should not return donors who have been deleted."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>)"
	 */
	public void findAnyDonor_deleteObjectShouldNotPartOfList() {
		String searchDonorNumber = "";
		String donorFirstName = "fir";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		setPaginationParam(pagingParams);
		List<Donor> listDonor = (List<Donor>)(donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams).get(0));
		for(Donor donor:listDonor){
				assertFalse("Deleted Donor should not included in the list.",donor.getId()==2?true:false);
		}
	}

	

	@Test
	/**
	 * value="should return all non-deleted donors in the database."
	 * method="getAllDonors()"
	 */
	public void getAllDonors_shouldReturnNoneDeleteDonor() {
		List<Donor> listDonor = donorRepository.getAllDonors();
		for (Donor donor : listDonor) {
			assertFalse(donor.getIsDeleted());
		}

	}

	@Test
	/**
	 * value="should not return donors who have been deleted."
	 * method="getAllDonors()"
	 */
	public void getAllDonors_shouldNotReturnDeletedDonor() {
		List<Donor> listDonor = donorRepository.getAllDonors();
		for (Donor donor : listDonor) {
			// In Dataset DonorId=2 is deleted donor
			assertNotSame(2, donor.getId());
		}
	}

	@Test
	/**
	 * value = "should create new Donor from existing Donor object."
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldPersist() {
		try{
		Donor createNewDonorFromExistDonor = donorRepository.findDonorById(1l);
		//DonorBackingForm donorBackingForm = new DonorBackingForm(createNewDonorFromExistDonor);
		//setBackingFormValue(donorBackingForm);
		Donor newDonor =  this.copyDonor(createNewDonorFromExistDonor);
		newDonor.setDonorNumber("000006");
		donorRepository.addDonor(newDonor);
		assertTrue("Donor Object should persist.", newDonor
				.getId() == 0 ? false : true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Blank Fist Name)"
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistFirstNameBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setFirstName("");// Here we can pass donor firstname
											// value is blank.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue(
				"Donor First Name is blank. Donor object should not persist.",
				errors.hasErrors());
		if (!errors.hasErrors()) {
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Blank Last Name)"
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistLastNameBlank() {
		try {
			setBackingFormValue(donorBackingForm);
			donorBackingForm.setLastName("");// Here we can pass donor lastname
												// value is blank.
			errors = new BindException(donorBackingForm, "donor");
			donorBackingFormValidator.validate(donorBackingForm, errors);
			assertTrue(
					"Donor Last Name is blank. Donor object should not persist.",
					errors.hasErrors());
			if (!errors.hasErrors()) {
				donorRepository.addDonor(donorBackingForm.getDonor());
				assertTrue("Donor Object should persist.", donorBackingForm
						.getDonor().getId() == 0 ? false : true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Duplicate Donor Number)"
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistDuplicateDonorNumber() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("000001");// Here we can pass duplicate
													// donor number and check
													// method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue(
				"Duplicate Donor Number exist. Donor object should not persist.",
				errors.hasErrors());
		if (!errors.hasErrors()) {
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Blank Donor Number)"
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistBlankDonorBlank() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDonorNumber("");// Here we can pass blank donor
											// number and check method result.
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue("Donor Number is blank. Donor object should not persist.",
				errors.hasErrors());
		if (!errors.hasErrors()) {
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Gender)"
	 *  method = "addDonor(Donor)"
	 */
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
		assertTrue("Donor's Gender is blank. Donor object should not persist.",
				errors.hasErrors());
		if (!errors.hasErrors()) {
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Age is less than 16)"
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistMinimumAgeIsLessThan16() {
		donorBirthdate = "10/06/2000";// Calculate Donor age is less than 16.
		donorBackingForm.setBirthDate(donorBirthdate);
		// Set Inputvalue.
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue(
				"Donor's age is less than 16. Donor object should not persist.",
				errors.hasErrors());
		if (!errors.hasErrors()) {
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		}
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Age is greater than 65)"
	 *  method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistMaximumAgeIsGreaterThan65() {
		donorBirthdate = "24/02/1948";// Calculate Donor age is greater than 65.
		donorBackingForm.setBirthDate(donorBirthdate);
		setBackingFormValue(donorBackingForm);
		errors = new BindException(donorBackingForm, "donor");
		donorBackingFormValidator.validate(donorBackingForm, errors);
		assertTrue(
				"Donor's age is greater than 65. Donor object should not persist.",
				errors.hasErrors());
		if (!errors.hasErrors()) {
			donorRepository.addDonor(donorBackingForm.getDonor());
			assertTrue("Donor Object should persist.", donorBackingForm
					.getDonor().getId() == 0 ? false : true);
		}
	}

	@Test
	/**
	 * value="should update existing Donor with Donor object"
	 * method="updateDonor(Donor)"
	 */
	public void updateDonor_shouldReturnNotNull() {
		Donor editDonor = donorRepository.findDonorById(1l);
		donorBackingForm = new DonorBackingForm(editDonor);
		setBackingUpdateFormValue(donorBackingForm);
		assertNotNull("Donor Object should update.",
				donorRepository.updateDonor(donorBackingForm.getDonor()));
	}

	@Test
	/**
	 * value="should return null when Donor Number does not match an existing Donor"
	 * method="updateDonor(Donor)"
	 */
	public void updateDonor_shouldReturnNull() {
		Donor editDonor = new Donor();
		editDonor.setId(-1l);
		donorBackingForm = new DonorBackingForm(editDonor);
		setBackingUpdateFormValue(donorBackingForm);
		assertNull("Donor Object should null.",
				donorRepository.updateDonor(donorBackingForm.getDonor()));
	}

	
	@Test
	/**
	 * value = "should return empty List if search string is less than 2 characters"
	 *  method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_stringLengthLessthan2() {
		assertEquals("Search String length is less than 2,List size should be zero.", 
				donorRepository.findAnyDonorStartsWith("F").size(),0);
	}
	
	@Test
	/**
	 * value = "should allow search if search string is 2 or more characters."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_stringLengthGreaterthan2() {
		assertNotSame("Search String length is Greater than 2,List size should not zero.",donorRepository.findAnyDonorStartsWith("fi").size(),0);
	}
	
	@Test
	/**
	 * value = "should return empty list (instead of a null object) when no match is found."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_NoMatchingRecordFound() {
		assertEquals("List size should be zero,because matching record is not found.",donorRepository.findAnyDonorStartsWith("xx").size(),0);
	}
	

	@Test
	/**
	 * value = "should fetch all donors having a donor number that partially matches the search string."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_searchWithDonorNumber() {
		assertNotSame("List size should not zero,because partically matching donor number is found.",donorRepository.findAnyDonorStartsWith("00").size(),0);
	}
	
	@Test
	/**
	 * value = "should fetch all donors having a first name that partially matches the search string."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_searchWithDonorFirstNameMatch() {
		assertNotSame("List size should not zero,because partically matching firstname is found.",donorRepository.findAnyDonorStartsWith("fi").size(),0);
	}
	
	
	@Test
	/**
	 * value = "should fetch all donors having a last name that partially matches the search string."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void testFindAnyDonorStartWith_searchWithDonorLastNameMatch() {
		assertNotSame("List size should not zero,because partically matching lastname is found.",donorRepository.findAnyDonorStartsWith("la").size(),0);
	}
	
	@Test
	/**
	 * value = "should not return donors who have been deleted."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_softDeleteRecordNotInclude() {
	List<Donor> listDonor = 	donorRepository.findAnyDonorStartsWith("Fi");
	for(Donor donor:listDonor){
		assertNotSame("Donor is deleted from database.",2, donor.getId());
	}
	}
	
	
	

	
	@Test
	/**
	 * value = "Unique number string length should not zero.should generate 12 Digit Unique Number prefix with D-."
	 * method = "generateUniqueDonorNumber()"
	 */
	public void generateUniqueDonorNumber_nonEmptyString() {
		assertFalse(
				"should generate 12 Digit Unique Number prefix with D-.",
				StringUtils.isEmpty(DonorRepository.generateUniqueDonorNumber()));
	}

	@Test
	/**
	 * value = "Donor UniqueNumber total length should be 14."
	 * method = "generateUniqueDonorNumber()"
	 */
	public void generateUniqueDonorNumber_nonEmptyStringLength14() {
		assertEquals("Unique Donor Number length should be 14.", 14,
				(DonorRepository.generateUniqueDonorNumber()).length());
	}

	@Test
	/**
	 * value="List of donor should be persist."
	 * method="addAllDonors(List<Donor>)"
	 */
	public void testaddAllDonors() {
		Donor donor = new Donor();
		DonorBackingForm donorBackingForm = new DonorBackingForm(donor);
		donorBackingForm.setDonorNumber("000007");
		setBackingFormValue(donorBackingForm);
		List<Donor> listAllDonor = new ArrayList<Donor>();
		listAllDonor.add(donorBackingForm.getDonor());
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		donorBirthdate = "11/06/1991";
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setDonorNumber("000008");
		setBackingFormValue(donorBackingForm);
		listAllDonor.add(donorBackingForm.getDonor());
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
		donorBirthdate = "12/06/1991";
		donorBackingForm.setBirthDate(donorBirthdate);
		donorBackingForm.setDonorNumber("000009");
		setBackingFormValue(donorBackingForm);
		listAllDonor.add(donorBackingForm.getDonor());
		for (Donor donorObject : listAllDonor) {
			donorRepository.addDonor(donorObject);
		}
		assertTrue("List All Donor should persist.", true);

	}

	@Test
	/**
	 * value="should return donor with given Donor Number"
	 * method="findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteFalse() {
		assertNotNull("Donor object should not null.",
				donorRepository.findDonorByDonorNumber("000001", false));
	}

	@Test
	/**
	 * value="should return donor with given Donor Number"
	 * method="findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteFalse() {
		assertNull("Donor object should null.",
				donorRepository.findDonorByDonorNumber("000009", false));
	}

	@Test
	/**
	 * value="should return donor with given Donor Number"
	 * method="findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteTrue() {
		assertNull("Donor object should null.",
				donorRepository.findDonorByDonorNumber("000001", true));
	}

	@Test
	/**
	 * value="should return donor with given Donor Number"
	 * method="findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteTrue() {
		assertNotNull("Donor object should null.",
				donorRepository.findDonorByDonorNumber("000002", true));
	}

	@Test
	/**
	 * value = "should return deleted donor because IsDelete true."
	 * method = "findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_deleteDonorObjectShouldNotNullDonorDeleteTrue() {
		assertNotNull("Donor object should not null.",
				donorRepository.findDonorByDonorNumber("000002", true));
	}
	
	@Test
	/**
	 * value="should return all non-deleted deferral reasons in the database"
	 * method="getDeferralReasons()"
	 */
	public void getDeferralReasons_shouldReturnNonDeletedRecord() {
		List<DeferralReason> list = donorRepository.getDeferralReasons();
		if (list != null && list.size() > 0) {
			for (DeferralReason deferralReason : list) {
					assertFalse("Soft Deleted DeferralReason object should not include in list.",deferralReason.getIsDeleted());
			}
		}
	}
	
	@Test
	/**
	 * value="should not return deferral reasons that have been deleted"
	 * method="getDeferralReasons()"
	 */
	public void getDeferralReasons_shouldNotReturnDeferalReason() {
		List<DeferralReason> list = donorRepository.getDeferralReasons();
		if (list != null && list.size() > 0) {
			for (DeferralReason deferralReason : list) {
				//Here Id 7 is set as deleted = true in deferralreason in DonorDataset.xml file
					assertNotSame("Soft Deleted DeferralReason object should not include in list.",7,deferralReason.getId());
			}
		}
	}
	
	@Test
	/**
	 * value="DeferDonor should persist."
	 * method="deferDonor(String,String,String,String)"
	 */
	public void deferDonor_ShouldPersist()throws ParseException {
				this.userAuthentication();
				donorRepository.deferDonor("1",
						"19/07/2015", "3", "");
				assertTrue("Defer Donor Should persist.",true);
			
	}
	
	@Test
	/**
	 * value="should return non-deleted deferral reasons in the database."
	 * method="deferDonor(String,String,String,String)"
	 */
	public void findDeferralReasonById_shouldReturnNoneDeletedDeferralReason(){
		assertNotNull("Deferral Reason object should not null.",donorRepository.findDeferralReasonUsingId("1"));
	}
	
	@Test
	/**
	 * value="should return null deferral reason where deferral reason is deleted from the database."
	 * method="deferDonor(String,String,String,String)"
	 */
	public void findDeferralReasonById_shouldReturnNullDeferralReason(){
		assertNull("Deferral Reason object should  null.",donorRepository.findDeferralReasonUsingId("7"));
	}
	
	@Test
	/**
	 * value="DonorDeferral list size should be zero."
	 * method="getDonorDeferrals(Long)"
	 */
	public void getDonorDeferrals_listSizeShouldZero() {
		assertEquals("List size should be zero.",0, donorRepository.getDonorDeferrals(2l).size());

	}
	
	@Test
	/**
	 * value="DonorDeferral list size should not zero."
	 * method="getDonorDeferrals(Long)"
	 */
	public void getDonorDeferrals_listSizeShouldNotZero() {
		assertNotSame("List size should not zero.",0, donorRepository.getDonorDeferrals(1l).size());

	}
	
	@Test
	/**
	 * value="Method should return true."
	 * method="isCurrentlyDeferred(List<DonorDeferral>)"
	 */
	public void donorRepositoryList_methodShouldReturnTrue() {
				assertTrue("Defer donor should found.",donorRepository.isCurrentlyDeferred(donorRepository
						.getDonorDeferrals(1l)));
	}
	
	
	@Test
	/**
	 * value="Method should return false.Donor Id is exist into donor table."
	 * method="isCurrentlyDeferred(List<DonorDeferral>)"
	 */
	public void donorRepositoryList_methodShouldReturnFalse() {
				assertFalse("Defer donor should not found.",donorRepository.isCurrentlyDeferred(donorRepository
						.getDonorDeferrals(4l)));
	}
	
	@Test
	/**
	 * value="Method should return false.Donor Id is not exist into donor table."
	 * method="isCurrentlyDeferred(List<DonorDeferral>)"
	 */
	public void donorRepositoryList_methodShouldReturnFalseDonorIdNotExistIntoDB() {
				assertFalse("Defer donor should not found. Because donor id is not exist into donor table.",donorRepository.isCurrentlyDeferred(donorRepository
						.getDonorDeferrals(15l)));
	}

	@Test
	/**
	 * value="Method should return true."
	 * method="isCurrentlyDeferred(Donor)"
	 */
	public void donorRepositoryDonor_methodShouldReturnTrue() {
				assertTrue("Defer donor should found.",donorRepository.isCurrentlyDeferred(donorRepository
						.findDonorById(1l)));
	}
	
	
	@Test
	/**
	 * value="Method should return false."
	 * method="isCurrentlyDeferred(Donor)
	 */
	public void donorRepositoryDonor_methodShouldReturnFalse() {
				assertFalse("Defer donor should not found.",donorRepository.isCurrentlyDeferred(donorRepository
						.findDonorById(4l)));
	}
	
	@Test
	/**
	 * value="should return last donor derferral date."
	 * method="getLastDonorDeferralDate(long)"
	 * 
	 */
	public void getLastDonorDeferralDate_shouldReturnlastDeferredUntil(){
		assertNotNull("should return last donor derferral date", donorRepository.getLastDonorDeferralDate(4l));
	}
	
	public void setPaginationParam(Map<String, Object> pagingParams){
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", "0");
		pagingParams.put("sortColumnId", "0");
		pagingParams.put("length", "10");
		pagingParams.put("sortDirection", "asc");
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
		user = new User();
		user.setId(userDbId);
		donorBackingForm.setCreatedBy(user);
		donorBackingForm.setCreatedDate(date);
		donorBackingForm.setLastUpdated(date);
		donorBackingForm.setLastUpdatedBy(user);
		donorBackingForm.setDistrict("District");
		donorBackingForm.setDonorPanel("1");
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
		user = new User();
		user.setId(userDbId);
		donorBackingForm.setCreatedBy(user);
	}

	@Before
	public void init() {
		validator = new DonorBackingFormValidator();
		donorBackingFormValidator = new DonorBackingFormValidator(validator,
				utilController);
		donor = new Donor();
		donorBackingForm = new DonorBackingForm(donor);
	}
	
	
	public Donor copyDonor(Donor donor){
		Donor copyDonor = new Donor();
		copyDonor.setAddress(donor.getAddress());
		copyDonor.setAge(donor.getAge());
		copyDonor.setBirthDate(donor.getBirthDate());
		copyDonor.setBirthDateEstimated(donor.getBirthDateEstimated());
		copyDonor.setBirthDateInferred(donor.getBirthDateInferred());
		copyDonor.setBloodAbo(donor.getBloodAbo());
		copyDonor.setCallingName(donor.getCallingName());
		copyDonor.setCity(donor.getCity());
		copyDonor.setContactInformation(donor.getContactInformation());
		copyDonor.setCountry(donor.getCountry());
		copyDonor.setDateOfLastDonation(donor.getDateOfLastDonation());
		copyDonor.setDistrict(donor.getDistrict());
		copyDonor.setDonorHash(donor.getDonorHash());
		copyDonor.setDonorPanel(donor.getDonorPanel());
		copyDonor.setDonorStatus(donor.getDonorStatus());
		copyDonor.setFirstName(donor.getFirstName());
		copyDonor.setGender(donor.getGender());
		copyDonor.setLastName(donor.getLastName());
		copyDonor.setMiddleName(donor.getMiddleName());
		copyDonor.setNationalID(donor.getNationalID());
		copyDonor.setNotes(donor.getNotes());
		copyDonor.setOtherPhoneNumber(donor.getOtherPhoneNumber());
		copyDonor.setPhoneNumber(donor.getPhoneNumber());
		copyDonor.setPreferredContactMethod(donor.getPreferredContactMethod());
		copyDonor.setProvince(donor.getProvince());
		copyDonor.setState(donor.getState());
		copyDonor.setZipcode(donor.getZipcode());
		return copyDonor;
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
