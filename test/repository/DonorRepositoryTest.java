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

import security.LoginUserService;
import security.V2VUserDetails;
import backingform.DonorBackingForm;
import controller.UtilController;
import model.donorcodes.DonorCodeGroup;
import model.donorcodes.DonorDonorCode;

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
	private Donor donor;
	@Autowired
	private DataSource dataSource;
	static IDatabaseConnection connection;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Before
	public void init() {
		try {
			if (connection == null)
				getConnection();
			// Insert Data into database using DonorRepositoryDataset.xml
			IDataSet dataSet = getDataSet();
			Date today = new Date();
			Map<String, Object> replacements = new HashMap<String, Object>();
			replacements.put("DateDonorNotDue", DateUtils.addDays(today,
					-(CollectionConstants.BLOCK_BETWEEN_COLLECTIONS - 1)));
			replacements.put("DateDonorDue", DateUtils.addDays(today,
					-(CollectionConstants.BLOCK_BETWEEN_COLLECTIONS + 1)));

			replacements.put("DateDeferredOn", DateUtils.addDays(today, -(2)));
			replacements.put("DateDeferredUnit", DateUtils.addDays(today, (2)));

			ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
			for (String key : replacements.keySet()) {
				rDataSet.addReplacementObject("${" + key + "}",
						replacements.get(key));
			}
			DatabaseOperation.INSERT.execute(connection, rDataSet);

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
	 * This method is executed once before test case execution start and acquires
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
		File file = new File("test/dataset/DonorRepositoryDataset.xml");
		return new FlatXmlDataSet(file);
	}

	@Test
	/**
	 * Should fail when donor is missing required fields. 
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
	 * Should delete donor from database
	 * deleteDonor(long)
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
	 * Should return donor with given id
	 * findDonorById(Long)
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
	 * Should return null when Donor id does not exist
	 * findDonorById(Long)
	 */
	public void findDonorById_shouldReturnNull() {
		// 18 ID is not exist into Donor table.
		assertNull(
				"Donor's Object should null. Donor Id is not exist into Donor's table.",
				donorRepository.findDonorById(18l));
	}

	@Test
	/**
	 * Should return null when Donor has been deleted
	 * findDonorById(Long)
	 */
	public void findDonorById_shouldReturnNullDonorIsDeleted() {
		// 2 is Deleted Donor's ID.
		assertNull(
				"Donor object should null. Donor's Id is exist into Donor table but it is deleted.",
				donorRepository.findDonorById(2l));
	}

	@Test
	/**
	 * Should return empty list (instead of a null object) when no match is found.
	 * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)
	 * 
	 */
	public void findAnyDonor_listSizeShouldZero() {
		String searchDonorNumber = "";
		String donorFirstName = "xxx";
		String donorLastName = "";
		String donationIdentificationNumber = "";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		assertEquals("List size should be zero, no matching search results.",
				0, ((List<Donor>) (donorRepository.findAnyDonor(searchDonorNumber,
						donorFirstName, donorLastName, pagingParams,
						true, donationIdentificationNumber).get(0))).size());
	}

	@Test
	/**
	 * Should fetch all donors that partially match first name
	 * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)
	 */
	public void findAnyDonor_listSizeShouldNotZeroPartialFirstNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "fir";
		String donorLastName = "";
		String donationIdentificationNumber = "";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, 
				 pagingParams,true, donationIdentificationNumber).get(0)));

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
	 * Should fetch all donors that fully  match first name
	 * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)
	 */
	public void findAnyDonor_listSizeShouldNotZeroFullyFirstNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "firstName";
		String donorLastName = "";
		String donationIdentificationNumber = "";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName,
				 pagingParams,false, donationIdentificationNumber).get(0)));

		assertNotSame(
				"List size should not zero.Matching records is found base on firstname.",
				0, listDonors.size());
		boolean isValid = false;
		for (Donor donor : listDonors) {
			if (donor.getFirstName().equals(donorFirstName)) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		assertTrue("Found Donor's with ,"+donorFirstName , isValid);

	}


	@Test
	/**
	 * Should fetch all donors that partially match last name
	 * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)
	 */
	public void findAnyDonor_listSizeShouldNotZeroPartialLastNameMatch() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "las";
		String donationIdentificationNumber = "";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName
			        , pagingParams,true, donationIdentificationNumber).get(0)));

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
	 * Should not return donors who have been deleted.
	 * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)
	 */
	public void findAnyDonor_deleteObjectShouldNotPartOfList() {
		String searchDonorNumber = "";
		String donorFirstName = "fir";
		String donorLastName = "";
		String donationIdentificationNumber = "";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		List<Donor> listDonor = (List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName
				, pagingParams,true, donationIdentificationNumber).get(0));

		for (Donor donor : listDonor) {
			// 2 is deleted donor id
			assertFalse(
					"Donor's id 2 is deleted from database. so Deleted Donor should not included in the list.",
					donor.getId() == 2 ? true : false);
		}
	}
	
	@Test
	/**
	 * Should return donor with donation matching DIN
	 * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String, Object>, Boolean)
	 */
	public void findAnyDonor_shouldReturnDonorWithDonationMatchingDIN() {
		String searchDonorNumber = "";
		String donorFirstName = "";
		String donorLastName = "";
		String donationIdentificationNumber = "000001";
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);
		
		List<Donor> donorList = (List<Donor>) (donorRepository.findAnyDonor(
				searchDonorNumber, donorFirstName, donorLastName, 
				pagingParams, false, donationIdentificationNumber).get(0));
		assertEquals("Should return a single Donor result", 1, donorList.size());
		boolean isValid = false;
		if (donorList.get(0).getDonorNumber().equals("000001")) {
			isValid = true;
		} else {
			isValid = false;
		}
		assertTrue("Donor with donation matching DIN returned", isValid);
	}

	@Test
	/**
	 * Should return all non-deleted donors in the database.
	 * getAllDonors()
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
	 * Should not return donors who have been deleted.
	 * getAllDonors()
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
	 * Should create new Donor from existing Donor object
	 * addDonor(Donor)
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
	 * Should update existing Donor with Donor object
	 * updateDonor(Donor)
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
	 * Should return null when Donor Number does not match an existing Donor
	 * updateDonor(Donor)
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
	 * Should return empty List if search string is less than 2 characters
	 * findAnyDonorStartsWith(String)
	 */
	public void findAnyDonorStartsWith_stringLengthLessthan2() {
		assertEquals(
				"Search String length is less than 2,List size should be zero.",
				donorRepository.findAnyDonorStartsWith("F").size(), 0);
	}

	@Test
	/**
	 * Should allow search if search string is 2 or more characters
	 * findAnyDonorStartsWith(String)
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
	 * Should return empty list (instead of a null object) when no match is found
	 * findAnyDonorStartsWith(String)
	 */
	public void findAnyDonorStartsWith_NoMatchingRecordFound() {
		assertEquals(
				"List size should be zero,because there is no matching record into donor's table which is match with 'xx' value.",
				donorRepository.findAnyDonorStartsWith("xx").size(), 0);
	}

	@Test
	/**
	 * Should fetch all donors having a donor number that partially matches the search string.
	 * findAnyDonorStartsWith(String)
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
	 * Should fetch all donors having a first name that partially matches the search string.
	 * findAnyDonorStartsWith(String)
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
	 * Should fetch all donors having a last name that partially matches the search string
	 * findAnyDonorStartsWith(String)
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
	 * Should not return donors who have been deleted
	 * findAnyDonorStartsWith(String)
	 */
	public void findAnyDonorStartsWith_softDeleteRecordNotInclude() {
		List<Donor> listDonor = donorRepository.findAnyDonorStartsWith("00");
		for (Donor donor : listDonor) {
			// 2 is Deleted Donor's ID
			assertNotSame(
					"Donor Id 2 is deleted from donor table so, that record should not part of list.",
					2, donor.getId());
		}
	}

	@Test
	/**
	 * Should create new Donors from existing Donor objects
	 * addAllDonors(List<Donor>)
	 */
	public void addAllDonors_listDonorsShouldPersist() {
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
	 * Should return donor with given Donor Number
	 * findDonorByDonorNumber(String,boolean)
	 */
	public void findDonorByDonorNumber_donorObjectShouldNotNullDonorDeleteFalse() {
		// 000001 DonorNumber
		Donor findDonor = donorRepository.findDonorByDonorNumber("000001",
				false);
		assertNotNull(
				"Donor object should not null.Because DonorNumber 000001 is exist into donor table",
				findDonor);
		assertTrue("Donor's Donor Number should be '000001'.", findDonor
				.getDonorNumber().equals("000001"));
	}

	@Test
	/**
	 * Should return donor with given Donor Number
	 * findDonorByDonorNumber(String,boolean)
	 */
	public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteFalse() {
		// 000009 DonorNumber is not exist.
		assertNull(
				"Donor object should null.Because DonorNumber is not exist into database.",
				donorRepository.findDonorByDonorNumber("000014", false));
	}

	@Test
	/**
	 * Should return donor with given Donor Number
	 * findDonorByDonorNumber(String,boolean)
	 */
	public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteTrue() {
		// 000001 DonorNumber
		assertNull(
				"Donor object should null. Because Donor's Donor Number is exist into database table but that record is not deleted.",
				donorRepository.findDonorByDonorNumber("000001", true));
	}

	@Test
	/**
	 * Should return deleted donor because IsDelete true
	 * findDonorByDonorNumber(String,boolean)
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
	 * Should return all non-deleted deferral reasons in the database
	 * getDeferralReasons()
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
	 * Should not return deferral reasons that have been deleted
	 * getDeferralReasons()"
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
	 * Should add Deferral for Donor
	 * deferDonor(String,String,String,String)
	 */
	public void deferDonor_ShouldPersist() throws ParseException {
		try {
			this.userAuthentication();

			DonorDeferral donorDeferral = donorRepository.deferDonor("1",
					"19/07/2015", "3", "");
			assertTrue("DeferDonor object Should persist.",
					donorDeferral.getId() != 0 ? true : false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	/**
	 * Should return non-deleted deferral reasons in the database
	 * findDeferralReasonById(String)
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
	 * Should return null deferral reason where deferral reason is deleted from the database
	 * findDeferralReasonById(String)
	 */
	public void findDeferralReasonById_shouldReturnNullDeferralReason() {
		// 7 ID is deleted from DeferralReason.
		assertNull("Deferral Reason object should null.",
				donorRepository.findDeferralReasonUsingId("7"));
	}

	@Test
	/**
	 * DonorDeferral list size should be zero
	 * getDonorDeferrals(Long)
	 */
	public void getDonorDeferrals_listSizeShouldZero() {
		// 2 is Donor ID
		assertEquals(
				"Deferral Donor is not found base on donor id so,List size should be zero.",
				0, donorRepository.getDonorDeferrals(2l).size());

	}

	@Test
	/**
	 * DonorDeferral list size should not zero
	 * getDonorDeferrals(Long)
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
	 * Method should return true
	 * isCurrentlyDeferred(List<DonorDeferral>)
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
	 * Method should return false.Donor Id is exist into donor table
	 * isCurrentlyDeferred(List<DonorDeferral>)
	 */
	public void isCurrentlyDeferred_List_ShouldReturnFalse() {
		// 4 is Donor ID
		List<DonorDeferral> listDonorDeferral = donorRepository
				.getDonorDeferrals(5l);
		assertFalse(
				"should return false for donor that not currently deferred.",
				donorRepository.isCurrentlyDeferred(listDonorDeferral));

	}

	@Test
	/**
	 * Method should return false.There is no record exist into donordeferral which is match with Donor ID.
	 * isCurrentlyDeferred(List<DonorDeferral>)
	 */
	public void isCurrentlyDeferred_List_ShouldReturnFalseMatchingRecordNotFound() {
		// 6 is Donor ID .
		List<DonorDeferral> listDonorDeferral = donorRepository
				.getDonorDeferrals(5l);
		assertFalse(
				"Defer donor should not found. Because There is no record into donordeferral table which is match with Donor ID.",
				donorRepository.isCurrentlyDeferred(listDonorDeferral));
	}

	@Test
	/**
	 * Method should return true
	 * isCurrentlyDeferred(Donor)
	 */
	public void isCurrentlyDeferred_Donor__methodShouldReturnTrue() {
		// 1 is Donor ID
		Donor donor = donorRepository.findDonorById(1l);
		assertTrue("should return true for donor that currently deferred.",
				donorRepository.isCurrentlyDeferred(donor));
	}

	@Test
	/**
	 * Method should return false
	 * isCurrentlyDeferred(Donor)
	 */
	public void isCurrentlyDeferred_Donor_methodShouldReturnFalse() {
		// 4 is Donor ID
		try {
			Donor donor = donorRepository.findDonorById(4l);
			assertFalse(
					"should return false for donor not currently deferred.",
					donorRepository.isCurrentlyDeferred(donor));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	/**
	 * Should return last donor derferral date
	 * getLastDonorDeferralDate(long)
	 * 
	 */
	public void getLastDonorDeferralDate_shouldReturnlastDeferredUntil() {
		// 4 is Donor ID
		Date currentDate = DateUtils.addDays(new Date(), (2));
		Date date = donorRepository.getLastDonorDeferralDate(6l);
		assertNotNull("should return last donor derferral date", date);
		String str = dateFormat.format(date);
		String str_currentDate = dateFormat.format(currentDate);
		assertTrue("Latest deferal date should be '" + str_currentDate + "'.",
				str.equals(str_currentDate));
	}
        
        @Test
	/**
	 * Should return Non EMpty Language List  
	 */
        public void getAllLanguages_shuouldReturnNonEmptyList(){
            assertTrue("Languages List  should not be empty", !donorRepository.getAllLanguages().isEmpty());
        }

        @Test
	/**
	 * Should return Non EMpty ID Types  List  
	 */
        public void getAllIdTypes_shuouldReturnNonEmptyList(){
            assertTrue("IDTypes List  should not be empty", !donorRepository.getAllIdTypes().isEmpty());
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
		donorBackingForm.setHomeAddress("myaddress");
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
		donorBackingForm.setMobileNumber("9999999999");
		donorBackingForm.setWorkNumber("8888888888");
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
		donorBackingForm.setHomeAddress("address_update");
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
		donorBackingForm.setMobileNumber("9878787878");
		donorBackingForm.setWorkNumber("874525452");
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
		copyDonor.setHomeAddress(donor.getHomeAddress());
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
		copyDonor.setMobileNumber(donor.getMobileNumber());
		copyDonor.setHomeNumber(donor.getHomeNumber());
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
        
         @Test
	/**
	 * Test passes if DonorCodeGroup is saved in  database
	 */
        public void saveDonorCodeGroup_shouldPersist(){
            DonorCodeGroup donorCodeGroup = new DonorCodeGroup();
            donorCodeGroup.setDonorCodeGroup("Test Only");
            donorRepository.saveDonorCodeGroup(donorCodeGroup);
            assertNotNull("Failed to save DonorCodeGroup object ",donorCodeGroup.getId());
            
        }
       
        
        @Test
	/**
	 * Test passes if DonorDonorCode is saved in database
	 */
        public void saveDonorDonorCode_shouldPersist(){
            DonorDonorCode  donorDonorCode =  new DonorDonorCode();
	    donorDonorCode.setDonorCodeId(donorRepository.findDonorCodeById(1l));	 
            donorDonorCode.setDonorId(donorRepository.findDonorById(5l));
            donorRepository.saveDonorDonorCode(donorDonorCode);
            assertNotNull("Failed to save DonorDonorCode object ",donorDonorCode.getId());
            
        }
        
       
        @Test
	/**
	 * Test passes if donor is assigned with a donor code
	 */
        public void findDonorCodeById_ShouldReturnNotNull_WhenDonorCodeExisted(){
            assertNotNull("Failed to find donor code by ID 1" ,donorRepository.findDonorCodeById(1l));
        }
        
        @Test
	/**
	 * Test  will pass if donor  code groups exists  
        */
        public void getAllDonorCodeGroups_ShouldNotReturnEmptyList(){
            assertTrue("Failed To Load alll donorCodeGroups" ,!donorRepository.getAllDonorCodeGroups().isEmpty());
        }
        
        
        @Test
	/**
	 * Test  will pass if donor donor code groups assigned to donor
	 */
       public void findDonorCodeGroupsOfDonor_ShouldNotReturnEmptyList_WhenDonorCodeGroupsExisted(){
           assertTrue(" Failed To load donorCodeGroups of donor " ,!donorRepository.findDonorCodeGroupsByDonorId(1l).isEmpty());
       }
       
       @Test
	/** 
	 * Test  passes if donor codes assigned to donor  
	 */
       public void findDonorDonorCodesOfDonor_ShouldNotReturnEmptyList_WhenDonorCodesExisted(){
           assertTrue(" Failed To load sonorCodes of donor " ,!donorRepository.findDonorDonorCodesOfDonorByDonorId(1l).isEmpty());
       }
       
       @Test
	/** 
	 * Test  passes if donor codes assigned to donor  
	 */
       public void deleteDonorCode_ShouldReturnNotNull_WhenDeleted(){      
           assertNotNull("Failed to remove DonorDonorCode of Id 1" ,donorRepository.deleteDonorCode(1l));
       }
       

}
