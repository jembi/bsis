package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.collectedsample.CollectionConstants;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.user.User;
import model.util.BloodGroup;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;

import security.LoginUserService;
import security.V2VUserDetails;
import backingform.DonorBackingForm;
import backingform.validator.DonorBackingFormValidator;
import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
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
	@Autowired
	private DataSource dataSource;
	static IDatabaseConnection connection;

	@Before
	public void init() {
		try {
			if (connection == null)
				getConnection();
			// Insert Data into database using DonorDataset.xml
			IDataSet dataSet = getDataSet();
			Date today = new Date();
			Map<String, Object> replacements = new HashMap<String, Object>();
			replacements.put("DateDonorNotDue", DateUtils.addDays(today,
					-(CollectionConstants.BLOCK_BETWEEN_COLLECTIONS - 1)));
			replacements.put("DateDonorDue", DateUtils.addDays(today,
					-(CollectionConstants.BLOCK_BETWEEN_COLLECTIONS + 1)));
			ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
			for (String key : replacements.keySet()) {
				rDataSet.addReplacementObject("${" + key + "}",
						replacements.get(key));
			}
			DatabaseOperation.INSERT.execute(connection, rDataSet);
			validator = new DonorBackingFormValidator();
			donorBackingFormValidator = new DonorBackingFormValidator(
					validator, utilController);
			donor = new Donor();
			donorBackingForm = new DonorBackingForm(donor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() throws Exception {
		// Remove data from database
		DatabaseOperation.DELETE_ALL.execute(connection, getDataSet());
	}

	/**
	 * This method is execute once before test case execution start and acquire
	 * datasource from spring context and create new dbunit IDatabaseConnection.
	 * This method is also useful to set HSQLDB datatypefactory.
	 */
	private void getConnection() {
		try {
			connection = new DatabaseDataSourceConnection(dataSource);
			DatabaseConfig config = connection.getConfig();
			// Set HSQLDB datatypefactory
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					new HsqldbDataTypeFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private IDataSet getDataSet() throws Exception {
		File file = new File("test/DonorDataset.xml");
		return new FlatXmlDataSet(file);
	}

	@Test
	/**
	 * value = should fail when donor is missing required fields. method =
	 * saveDonor(Donor)
	 */
	public void saveDonor_shouldPersist() {
		donorBackingForm = new DonorBackingForm();
		setBackingFormValue(donorBackingForm);
		// New DonorNumber(000005) which is assigned to Donor Persist Object
		donorBackingForm.setDonorNumber("000005");
		donorRepository.saveDonor(donorBackingForm.getDonor());
		assertTrue(
				"Donor's Id should not zero. Once Donor object should persist,new Id is generated and assigned to Donor.",
				donorBackingForm.getDonor().getId() == 0 ? false : true);
	}

	@Test
	/**
	 * value=should delete donor from database. method=deleteDonor(long)
	 */
	public void deleteDonor_shouldDeleteDonorFromDatabase() {
		// 3 is Donor's ID.
		Donor deletedDonor = donorRepository.deleteDonor(3l);
		assertTrue("Delete operation should complete successfully.",
				deletedDonor.getIsDeleted());
		assertTrue("Deleted Donor's id value should be 3.",
				deletedDonor.getId() == 3 ? true : false);
	}

	@Test
	/**
	 * value=should return donor with given id method=findDonorById(Long)
	 */
	public void findDonorById_shouldReturnDonor() {
		// 1 is Donor's ID.
		Donor findDonor = donorRepository.findDonorById(1l);
		assertNotNull(
				"Donor Object should not null.Donor Id is exist into Donor's table.",
				findDonor);
		assertTrue("Donor's id value should be 1.",
				findDonor.getId() == 1 ? true : false);
	}

	@Test
	/**
	 * value=should return null when Donor with id does not exist
	 * method=findDonorById(Long)
	 */
	public void findDonorById_shouldReturnNull() {
		// 18 ID is not exist into Donor table.
		assertNull(
				"Donor's Object should null. Donor Id is not exist into Donor's table.",
				donorRepository.findDonorById(18l));
	}

	@Test
	/**
	 * value=should return null when Donor has been deleted
	 * method=findDonorById(Long)
	 */
	public void findDonorById_shouldReturnNullDonorIsDeleted() {
		// 2 is Deleted Donor's ID.
		assertNull(
				"Donor object should null. Donor's Id is exist into Donor table but it is deleted.",
				donorRepository.findDonorById(2l));
	}

	@Test
	/**
	 * value=
	 * "should return empty list (instead of a null object) when no match is found."
	 * method=
	 * "findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
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
		assertEquals("List size should be zero.Matching records is not found.",
				0,
				((List<Donor>) (donorRepository.findAnyDonor(searchDonorNumber,
						donorFirstName, donorLastName, bloodGroups,
						anyBloodGroup, pagingParams, false).get(0))).size());
	}

	@Test
	/**
	 * value="should fetch all donors that partially match first name." method=
	 * "findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroPartialFirstNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "fir";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		setPaginationParam(pagingParams);
		List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams, false).get(0)));
		assertNotSame(
				"List size should not zero.Matching records is found base on firstname.",
				0, listDonors.size());
		boolean isValid = false;
		for (Donor donor : listDonors) {
			if (donor.getFirstName().startsWith("fir")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's First Name should be start with 'fir'.", isValid);

	}

	@Test
	/**
	 * value="should fetch all donors that partially match last name." method=
	 * "findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroPartialLastNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "las";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();

		setPaginationParam(pagingParams);
		List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams, false).get(0)));
		assertNotSame(
				"List size should not zero.Matching records is found base on lastname.",
				0, listDonors.size());
		boolean isValid = false;
		for (Donor donor : listDonors) {
			if (donor.getLastName().startsWith("las")) {
				isValid = true;
			} else {
				isValid = false;
			}
		}
		assertTrue("Donor's Last Name should be start with 'las'.", isValid);
	}

	@Test
	/**
	 * value=
	 * "should fetch all donors with blood groups that match the criteria in List<BloodGroup>."
	 * method=
	 * "findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroMatchBloodGroup() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "false";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		bloodGroups.add(new BloodGroup("A+"));
		bloodGroups.add(new BloodGroup("A-"));
		setPaginationParam(pagingParams);
		List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams, false).get(0)));
		assertNotSame(
				"List size should not zero.Matching records is found base on bloodgroup criteria.",
				0, listDonors.size());
		boolean isValid = false;
		for (Donor donor : listDonors) {
			if ((donor.getBloodAbo() + donor.getBloodRh()).equals("A+")
					|| (donor.getBloodAbo() + donor.getBloodRh()).equals("A-")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's blood group should be 'A+ and A-.'.", isValid);
	}

	@Test
	/**
	 * value=
	 * "should fetch donors with no blood groups when anyBloodGroup.equals(true)."
	 * method=
	 * "findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroAnyBloodGroupMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		// B+ bloodgroup is not exist into Donor's table.
		bloodGroups.add(new BloodGroup("B+"));
		setPaginationParam(pagingParams);
		List<Donor> listDonor = (List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams, false).get(0));
		assertNotSame(
				"Any blood group condition is true. So Donor's blood group search criteria is not consider. And List size should not zero.",
				0, listDonor.size());
	}

	@Test
	/**
	 * value="should only return donors that are due to donate when dueToDonate is set to true."
	 * method="findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
	 */
	public void findAnyDonor_listSizeShouldNotZeroDueToDonateSetTrue() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		setPaginationParam(pagingParams);
		List<Donor> listDonor = (List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams, true).get(0));
		assertNotSame(
				"DueToDonate condition is true,matching record should exist and donor list size should not zero.",
				0, listDonor.size());
		boolean isValid = false;
		for (Donor donor : listDonor) {
			if ((donor.getDonorNumber().equals("000004") || donor
					.getDonorNumber().equals("000017"))) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's Donor Number should be 000004 and 000017.", isValid);
	}

	@Test
	/**
	 * value="should not return donors who have been deleted." method=
	 * "findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)"
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
		List<Donor> listDonor = (List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, bloodGroups,
				anyBloodGroup, pagingParams, false).get(0));
		for (Donor donor : listDonor) {
			// 2 is deleted donor id
			assertFalse(
					"Donor's id 2 is deleted from database. so Deleted Donor should not included in the list.",
					donor.getId() == 2 ? true : false);
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
			assertFalse("Deleted Donor should not part of donor list.",
					donor.getIsDeleted());
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
			// 2 is deleted donor id.
			assertNotSame(
					"Donor's id 2 is deleted from database. so Deleted Donor should not included in the list.",
					2, donor.getId());
		}
	}

	@Test
	/**
	 * value = "should create new Donor from existing Donor object." method =
	 * "addDonor(Donor)"
	 */
	public void addDonor_shouldPersist() {
		// 1 is Donor's Id.
		Donor createNewDonorFromExistDonor = donorRepository.findDonorById(1l);
		Donor newDonor = this.copyDonor(createNewDonorFromExistDonor);
		// New DonorNumber(000006) which is assigned to Donor Persist Object.
		newDonor.setDonorNumber("000006");
		donorRepository.addDonor(newDonor);

		assertTrue(
				"Donor's Id should not zero. Once Donor should persist,new Id is generated and assigned to Donor.",
				newDonor.getId() == 0 ? false : true);
	}

	@Test
	/**
	 * value = "should fail donor is missing required fields(Blank Fist Name)"
	 * method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistFirstNameBlank() {
		setBackingFormValue(donorBackingForm);
		// Here we can pass donor firstname value is blank.
		donorBackingForm.setFirstName("");
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
	 * method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistLastNameBlank() {
		setBackingFormValue(donorBackingForm);
		// Here we can pass donor lastname value is blank.
		donorBackingForm.setLastName("");
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
	}

	@Test
	/**
	 * value =
	 * "should fail donor is missing required fields(Duplicate Donor Number)"
	 * method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistDuplicateDonorNumber() {
		setBackingFormValue(donorBackingForm);
		// Here we can pass duplicate DonorNumber(000001)
		donorBackingForm.setDonorNumber("000001");
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
	 * value =
	 * "should fail donor is missing required fields(Blank Donor Number)" method
	 * = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistBlankDonorBlank() {
		setBackingFormValue(donorBackingForm);
		// DonorNumber value is blank.
		donorBackingForm.setDonorNumber("");
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
	 * value = "should fail donor is missing required fields(Gender)" method =
	 * "addDonor(Donor)"
	 */
	public void addDonor_ShouldNotPersistBlankGender() {
		Date date = new Date();
		donorBackingForm.setDayOfMonth("10");
		donorBackingForm.setMonth("06");
		donorBackingForm.setYear("2000");
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
	 * value =
	 * "should fail donor is missing required fields(Age is less than 16)"
	 * method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistMinimumAgeIsLessThan16() {
		donorBackingForm.setBirthDate(donorBirthdate);
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
	 * value =
	 * "should fail donor is missing required fields(Age is greater than 65)"
	 * method = "addDonor(Donor)"
	 */
	public void addDonor_shouldNotPersistMaximumAgeIsGreaterThan65() {
		setBackingFormValue(donorBackingForm);
		donorBackingForm.setDayOfMonth("24");
		donorBackingForm.setMonth("02");
		donorBackingForm.setYear("1948");
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
		// 1 is Donor Id.
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
	 * value="should return empty List if search string is less than 2 characters"
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_stringLengthLessthan2() {
		assertEquals(
				"Search String length is less than 2,List size should be zero.",
				donorRepository.findAnyDonorStartsWith("F").size(), 0);
	}

	@Test
	/**
	 * value = "should allow search if search string is 2 or more characters."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_stringLengthGreaterthan2() {
		List<Donor> searchResultList = donorRepository
				.findAnyDonorStartsWith("fi");
		assertNotSame(
				"Search String length is Greater than 2,List size should not zero.",
				searchResultList.size(), 0);
		boolean isValid = false;
		for (Donor donor : searchResultList) {
			if (donor.getFirstName().startsWith("fi")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's First Name should be start with 'fi'.", isValid);
	}

	@Test
	/**
	 * value = "should return empty list (instead of a null object) when no match is found."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_NoMatchingRecordFound() {
		assertEquals(
				"List size should be zero,because there is no matching record into donor's table which is match with 'xx' value.",
				donorRepository.findAnyDonorStartsWith("xx").size(), 0);
	}

	@Test
	/**
	 * value = "should fetch all donors having a donor number that partially matches the search string."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_searchWithDonorNumber() {
		List<Donor> searchResultList = donorRepository
				.findAnyDonorStartsWith("00");
		assertNotSame(
				"List size should not zero,because partically matching donor number is found.",
				searchResultList.size(), 0);
		boolean isValid = false;
		for (Donor donor : searchResultList) {
			if (donor.getDonorNumber().startsWith("00")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's Donor Number should be start with '00'.", isValid);
	}

	@Test
	/**
	 * value = "should fetch all donors having a first name that partially matches the search string."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_searchWithDonorFirstNameMatch() {
		List<Donor> searchResultList = donorRepository
				.findAnyDonorStartsWith("fi");
		assertNotSame(
				"List size should not zero,because partically matching firstname is found.",
				searchResultList.size(), 0);
		boolean isValid = false;
		for (Donor donor : searchResultList) {
			if (donor.getFirstName().startsWith("fi")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's First Name should be start with 'fi'.", isValid);
	}

	@Test
	/**
	 * value =
	 * "should fetch all donors having a last name that partially matches the search string."
	 * method = "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_searchWithDonorLastNameMatch() {
		List<Donor> searchResultList = donorRepository
				.findAnyDonorStartsWith("la");
		assertNotSame(
				"List size should not zero,because partically matching lastname is found.",
				searchResultList.size(), 0);
		boolean isValid = false;
		for (Donor donor : searchResultList) {
			if (donor.getLastName().startsWith("la")) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Donor's Last Name should be start with 'la'.", isValid);
	}

	@Test
	/**
	 * value = "should not return donors who have been deleted." method =
	 * "findAnyDonorStartsWith(String)"
	 */
	public void findAnyDonorStartsWith_softDeleteRecordNotInclude() {
		List<Donor> listDonor = donorRepository.findAnyDonorStartsWith("00");
		for (Donor donor : listDonor) {
			//2 is Deleted Donor's ID
			assertNotSame(
					"Donor Id 2 is deleted from donor table so, that record should not part of list.",
					2, donor.getId());
		}
	}

	@Test
	/**
	 * value =
	 * "Unique number string length should not zero.should generate 12 Digit Unique Number prefix with D-."
	 * method = "generateUniqueDonorNumber()"
	 */
	public void generateUniqueDonorNumber_nonEmptyString() {
		assertFalse(
				"should generate 12 Digit Unique Number prefix with D-.",
				StringUtils.isEmpty(DonorRepository.generateUniqueDonorNumber()));
	}

	@Test
	/**
	 * value = "Donor UniqueNumber total length should be 14." method =
	 * "generateUniqueDonorNumber()"
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
	public void addDonor_listDonorShouldPersist() {
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
		// 000001 DonorNumber
		Donor findDonor = donorRepository.findDonorByDonorNumber("000001",
				false);
		assertNotNull(
				"Donor object should not null.Because DonorNumber 000001 is exist into donor table",
				findDonor);
		assertTrue("Donor's Donor Number should be '000001'.",
				findDonor.getDonorNumber().equals("000001"));
	}

	@Test
	/**
	 * value="should return donor with given Donor Number"
	 * method="findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteFalse() {
		// 000009 DonorNumber is not exist.
		assertNull(
				"Donor object should null.Because DonorNumber is not exist into database.",
				donorRepository.findDonorByDonorNumber("000009", false));
	}

	@Test
	/**
	 * value="should return donor with given Donor Number"
	 * method="findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteTrue() {
		// 000001 DonorNumber
		assertNull(
				"Donor object should null. Because Donor's Donor Number is exist into database table but that record is not deleted.",
				donorRepository.findDonorByDonorNumber("000001", true));
	}

	@Test
	/**
	 * value = "should return deleted donor because IsDelete true." method =
	 * "findDonorByDonorNumber(String,boolean)"
	 */
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteTrue() {
		// 000002 Deleted DonorNumber
		Donor deletedDonor = donorRepository.findDonorByDonorNumber("000002",
				true);
		assertNotNull(
				"Donor's object should not null. Because matching donor is deleted and method argument is isDeleted=true",
				deletedDonor);
		assertTrue("Donor's donor number should be '000002'.", deletedDonor
				.getDonorNumber().equals("000002"));
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
				assertFalse(
						"Soft Deleted DeferralReason object should not include in list.",
						deferralReason.getIsDeleted());
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
				// 7 ID is deleted from DeferralReason.
				assertNotSame(
						"Soft Deleted DeferralReason object should not include in list.",
						7, deferralReason.getId());
			}
		}
	}

	@Test
	/**
	 * value="DeferDonor should persist."
	 * method="deferDonor(String,String,String,String)"
	 */
	public void deferDonor_ShouldPersist() throws ParseException {
		this.userAuthentication();
		donorRepository.deferDonor("1", "19/07/2015", "3", "");
		assertTrue("DeferDonor object Should persist.", true);
	}

	@Test
	/**
	 * value="should return non-deleted deferral reasons in the database."
	 * method="findDeferralReasonById(String)"
	 */
	public void findDeferralReasonById_shouldReturnNoneDeletedDeferralReason() {
		// 1 is DeferralReason ID.
		DeferralReason deferralReason = donorRepository
				.findDeferralReasonUsingId("1");
		assertNotNull("DeferralReason's object should not null.",
				deferralReason);
		assertTrue("Deferral's Reason Id should be 1.",
				deferralReason.getId() == 1 ? true : false);

	}

	@Test
	/**
	 * value=
	 * "should return null deferral reason where deferral reason is deleted from the database."
	 * method="findDeferralReasonById(String)"
	 */
	public void findDeferralReasonById_shouldReturnNullDeferralReason() {
		// 7 ID is deleted from DeferralReason.
		assertNull("Deferral Reason object should null.",
				donorRepository.findDeferralReasonUsingId("7"));
	}

	@Test
	/**
	 * value="DonorDeferral list size should be zero."
	 * method="getDonorDeferrals(Long)"
	 */
	public void getDonorDeferrals_listSizeShouldZero() {
		// 2 is Donor ID
		assertEquals(
				"Deferral Donor is not found base on donor id so,List size should be zero.",
				0, donorRepository.getDonorDeferrals(2l).size());

	}

	@Test
	/**
	 * value="DonorDeferral list size should not zero."
	 * method="getDonorDeferrals(Long)"
	 */
	public void getDonorDeferrals_listSizeShouldNotZero() {
		// 1 is Donor ID
		List<DonorDeferral> listDonorDeferral = donorRepository
				.getDonorDeferrals(1l);
		assertNotSame(
				"Deferral Donor is found base on donor id so,List size should not zero.",
				0, listDonorDeferral.size());
		for (DonorDeferral donorDeferral : listDonorDeferral) {
			assertTrue("DonorDeferral's Donor Id should be 1.", donorDeferral
					.getDeferredDonor().getId() == 1 ? true : false);
		}

	}

	@Test
	/**
	 * value="Method should return true."
	 * method="isCurrentlyDeferred(List<DonorDeferral>)"
	 */
	public void isCurrentlyDeferred_list_methodShouldReturnTrue() {
		// 1 is Donor ID
		List<DonorDeferral> listDonorDeferral = donorRepository
				.getDonorDeferrals(1l);
		assertTrue("should return true for donor that currently deferred.",
				donorRepository.isCurrentlyDeferred(listDonorDeferral));

	}

	@Test
	/**
	 * value="Method should return false.Donor Id is exist into donor table."
	 * method="isCurrentlyDeferred(List<DonorDeferral>)"
	 */
	public void isCurrentlyDeferred_List_ShouldReturnFalse() {
		// 4 is Donor ID
		List<DonorDeferral> listDonorDeferral = donorRepository
				.getDonorDeferrals(4l);
		assertFalse("should return false for donor that not currently deferred.",
				donorRepository.isCurrentlyDeferred(listDonorDeferral));

	}

	@Test
	/**
	 * value=
	 * "Method should return false.There is no record exist into donordeferral which is match with Donor ID."
	 * method="isCurrentlyDeferred(List<DonorDeferral>)"
	 */
	public void isCurrentlyDeferred_List_ShouldReturnFalseMatchingRecordNotFound() {
		// 6 is Donor ID .
		List<DonorDeferral> listDonorDeferral = donorRepository
				.getDonorDeferrals(6l);
		assertFalse(
				"Defer donor should not found. Because There is no record into donordeferral table which is match with Donor ID.",
				donorRepository.isCurrentlyDeferred(listDonorDeferral));
	}

	@Test
	/**
	 * value="Method should return true." method="isCurrentlyDeferred(Donor)"
	 */
	public void isCurrentlyDeferred_Donor__methodShouldReturnTrue() {
		// 1 is Donor ID
		Donor donor = donorRepository.findDonorById(1l);
		assertTrue("should return true for donor that currently deferred.",
				donorRepository.isCurrentlyDeferred(donor));
	}

	@Test
	/**
	 * value="Method should return false." method="isCurrentlyDeferred(Donor)
	 */
	public void isCurrentlyDeferred_Donor_methodShouldReturnFalse() {
		// 4 is Donor ID
		Donor donor = donorRepository.findDonorById(4l);
		assertFalse("should return false for donor not currently deferred.",
				donorRepository.isCurrentlyDeferred(donor));
	}

	@Test
	/**
	 * value="should return last donor derferral date."
	 * method="getLastDonorDeferralDate(long)"
	 * 
	 */
	public void getLastDonorDeferralDate_shouldReturnlastDeferredUntil() {
		// 4 is Donor ID
		Date date = donorRepository.getLastDonorDeferralDate(4l);
		assertNotNull("should return last donor derferral date", date);
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String str = dateFormat.format(date);
		assertTrue("Latest deferal date should be '2014-02-27'.",
				str.equals("2014-02-27"));
	}

	public void setPaginationParam(Map<String, Object> pagingParams) {
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
		donorBackingForm.setDayOfMonth("10");
		donorBackingForm.setMonth("06");
		donorBackingForm.setYear("2000");
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

	public Donor copyDonor(Donor donor) {
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
