package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.sql.DataSource;

import model.address.Address;
import model.address.AddressType;
import model.address.Contact;
import model.donation.DonationConstants;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.donor.DuplicateDonorBackup;
import model.donordeferral.DeferralReason;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.user.User;

import org.apache.commons.lang.time.DateUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
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
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import security.BsisUserDetails;
import security.LoginUserService;
import viewmodel.DonorSummaryViewModel;
import backingform.DonorBackingForm;
import controller.UtilController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@Transactional
public class DonorRepositoryTest {

    @Autowired
    private UtilController utilController;
    @Autowired
    DonorRepository donorRepository;
    private User user;
    private Long userDbId = 1l;
    private DonorBackingForm donorBackingForm;
    String donorBirthdate = null;
    ApplicationContext applicationContext = null;
    UserDetailsService userDetailsService;
    private Donor donor;
    @Autowired
    private DataSource dataSource;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private IDataSet getDataSet() throws Exception {
        File file = new File("test/dataset/DonorRepositoryDataset.xml");
        return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
    }

    private IDatabaseConnection getConnection() throws Exception {
    	IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
		DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
        return connection;
    }

    @Before
    public void init() throws Exception {
    	IDatabaseConnection connection = getConnection();
        try {
            // Insert Data into database using DonorRepositoryDataset.xml
            IDataSet dataSet = getDataSet();
            Date today = new Date();
            Map<String, Object> replacements = new HashMap<String, Object>();
            replacements.put("DateDonorNotDue", DateUtils.addDays(today,
                    -(DonationConstants.BLOCK_BETWEEN_DONATIONS - 1)));
            replacements.put("DateDonorDue", DateUtils.addDays(today,
                    -(DonationConstants.BLOCK_BETWEEN_DONATIONS + 1)));

            replacements.put("DateDeferredOn", DateUtils.addDays(today, -(2)));
            replacements.put("DateDeferredUnit", DateUtils.addDays(today, (2)));

            ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
            for (String key : replacements.keySet()) {
                rDataSet.addReplacementObject("${" + key + "}",
                        replacements.get(key));
            }
            DatabaseOperation.CLEAN_INSERT.execute(connection, rDataSet);

            donor = new Donor();
            donorBackingForm = new DonorBackingForm(donor);
        } finally {
			try {
				connection.close();
			}
			catch (SQLException e) {}
        }
    }

	@AfterTransaction
	public void after() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			IDataSet dataSet = getDataSet();
			DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
		}
		finally {
			connection.close();
		}
	}

    @Test
    /**
     * Should fail when donor is missing required fields. saveDonor(Donor)
     */
    public void saveDonor_shouldPersist() {
        donorBackingForm = new DonorBackingForm();
        setBackingFormValue(donorBackingForm);
        // New DonorNumber(000005) which is assigned to Donor Persist Object
        donorBackingForm.setDonorNumber("000005");
        donorRepository.saveDonor(donorBackingForm.getDonor());
        assertTrue(
                "Donor's Id should not zero. Once Donor object should persist,new Id is generated and assigned to Donor.",
                donorBackingForm.getDonor().getId() != 0);
    }

    @Test
    /**
     * Should return donor with given id findDonorById(Long)
     */
    public void findDonorById_shouldReturnDonor() {
        // 1 is Donor's ID.
        Donor findDonor = donorRepository.findDonorById(1l);
        assertNotNull(
                "Donor Object should not null.Donor Id is exist into Donor's table.",
                findDonor);
        assertTrue("Donor's id value should be 1.",
                findDonor.getId() == 1);
    }

    
    /**
     * Expects NoResultException to be thrown  when Donor id does not exist findDonorById(Long)
     */
    @Test(expected = NoResultException.class)
    public void findDonorById_shouldExpectNoResultException() {
        // 18 ID is not exist into Donor table.
        donorRepository.findDonorById(18l);
    }

    
    /**
     *  Expects NoResultException to be thrown when Donor has been deleted findDonorById(Long)
     */
    @Test(expected = NoResultException.class)
    public void findDonorById_shouldExpectNoResultExceptionWhenDonorIsDeleted() {
        // 2 is Deleted Donor's ID.
        donorRepository.findDonorById(2l);
    }

    @Test
    /**
     * Should return empty list (instead of a null object) when no match is
     * found.
     * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
     * Object>, Boolean)
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
                        true, donationIdentificationNumber))).size());
    }

    @Test
    /**
     * Should fetch all donors that partially match first name
     * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
     * Object>, Boolean)
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
                pagingParams, true, donationIdentificationNumber)));

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
     * Should fetch all donors that fully match first name
     * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
     * Object>, Boolean)
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
                pagingParams, false, donationIdentificationNumber)));

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
        assertTrue("Found Donor's with ," + donorFirstName, isValid);

    }

    @Test
    /**
     * Should fetch all donors that partially match last name
     * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
     * Object>, Boolean)
     */
    public void findAnyDonor_listSizeShouldNotZeroPartialLastNameMatch() {
        String searchDonorNumber = "";
        String donorFirstName = "";
        String donorLastName = "las";
        String donationIdentificationNumber = "";
        Map<String, Object> pagingParams = new HashMap<String, Object>();
        setPaginationParam(pagingParams);

        List<Donor> listDonors = ((List<Donor>) (donorRepository.findAnyDonor(
                searchDonorNumber, donorFirstName, donorLastName, pagingParams, true, donationIdentificationNumber)));

        assertNotSame(
                "List size should not zero.Matching records is found base on lastname.",
                0, listDonors.size());
        boolean isValid = false;
        for (Donor donor : listDonors) {
            isValid = donor.getLastName().startsWith("las");
        }
        assertTrue("Donor's Last Name should be start with 'las'.", isValid);
    }

    @Test
    /**
     * Should not return donors who have been deleted.
     * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
     * Object>, Boolean)
     */
    public void findAnyDonor_deleteObjectShouldNotPartOfList() {
        String searchDonorNumber = "";
        String donorFirstName = "fir";
        String donorLastName = "";
        String donationIdentificationNumber = "";
        Map<String, Object> pagingParams = new HashMap<String, Object>();
        setPaginationParam(pagingParams);

        List<Donor> listDonor = (List<Donor>) (donorRepository.findAnyDonor(
                searchDonorNumber, donorFirstName, donorLastName, pagingParams, true, donationIdentificationNumber));

        for (Donor donor : listDonor) {
            // 2 is deleted donor id
            assertFalse(
                    "Donor's id 2 is deleted from database. so Deleted Donor should not included in the list.",
                    donor.getId() == 2);
        }
    }

    @Test
    /**
     * Should return donor with donation matching DIN
     * findAnyDonor(String,String,String,List<BloodGroup>,String,Map<String,
     * Object>, Boolean)
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
                pagingParams, false, donationIdentificationNumber));
        assertEquals("Should return a single Donor result", 1, donorList.size());
        boolean isValid = false;
        isValid = donorList.get(0).getDonorNumber().equals("000001");
        assertTrue("Donor with donation matching DIN returned", isValid);
    }

    @Test
    /**
     * Should return all non-deleted donors in the database. getAllDonors()
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
     * Should not return donors who have been deleted. getAllDonors()
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
     * Should create new Donor from existing Donor object addDonor(Donor)
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
                newDonor.getId() != 0);
    }

    @Test
    /**
     * Should update existing Donor with Donor object updateDonor(Donor)
     */
    public void updateDonor_shouldReturnNotNull() {
        // 1 is Donor Id.
        Donor editDonor = donorRepository.findDonorById(1l);
        donorBackingForm = new DonorBackingForm(editDonor);
        setBackingUpdateFormValue(donorBackingForm);
        assertNotNull("Donor Object should update.",
                donorRepository.updateDonorDetails(donorBackingForm.getDonor()));

    }

    
    /**
     * Expects NoResultException to be thrown  when Donor Number does not match an existing Donor
     * updateDonor(Donor)
     */
    @Test(expected = NoResultException.class)
    public void updateDonor_shouldExpectNoResultException() {
        Donor editDonor = new Donor();
        editDonor.setId(-1l);
        donorBackingForm = new DonorBackingForm(editDonor);
        setBackingUpdateFormValue(donorBackingForm);
        donorRepository.updateDonorDetails(donorBackingForm.getDonor());
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
     * Should return empty list (instead of a null object) when no match is
     * found findAnyDonorStartsWith(String)
     */
    public void findAnyDonorStartsWith_NoMatchingRecordFound() {
        assertEquals(
                "List size should be zero,because there is no matching record into donor's table which is match with 'xx' value.",
                donorRepository.findAnyDonorStartsWith("xx").size(), 0);
    }

    @Test
    /**
     * Should fetch all donors having a donor number that partially matches the
     * search string. findAnyDonorStartsWith(String)
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
     * Should fetch all donors having a first name that partially matches the
     * search string. findAnyDonorStartsWith(String)
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
     * Should fetch all donors having a last name that partially matches the
     * search string findAnyDonorStartsWith(String)
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
        donorBirthdate = "1991-06-11";
        donorBackingForm.setBirthDate(donorBirthdate);
        donorBackingForm.setDonorNumber("000008");
        setBackingFormValue(donorBackingForm);
        listAllDonor.add(donorBackingForm.getDonor());
        donor = new Donor();
        donorBackingForm = new DonorBackingForm(donor);
        donorBirthdate = "1991-06-12";
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

    /**
     * Expects null when donor not exists
     * findDonorByDonorNumber(String,boolean)
     */
    @Test
    public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteFalse() {
        // 000009 DonorNumber is not exist.
         assertNull("Donor with donor number 000014 should return null",
        donorRepository.findDonorByDonorNumber("000014", false));
    }


    /**
     * Expects null  when donor with given donor number doesn't exist
     * findDonorByDonorNumber(String,boolean)
     */
    @Test
    public void findDonorByDonorNumber_donorObjectShouldNullDonorDeleteTrue() {
        //000001 donor number
        assertNull("Donor with donor number 000001 should return null",
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
     * Should add Deferral for Donor deferDonor(String,String,String,String)
     */
    public void deferDonor_ShouldPersist() throws ParseException {
    	DonorDeferral donorDeferral = new DonorDeferral();
    	donorDeferral.setDeferredDonor(donorRepository.findDonorById(1l));
    	donorDeferral.setDeferredUntil(dateFormat.parse("2015-07-19"));
    	donorDeferral.setDeferralReason(donorRepository.findDeferralReasonById("3"));
        donorRepository.deferDonor(donorDeferral);
        assertTrue("DeferDonor object Should persist.", donorDeferral.getId() != 0);
    }

    @Test
    /**
     * Should return non-deleted deferral reasons in the database
     * findDeferralReasonById(String)
     */
    public void findDeferralReasonById_shouldReturnNoneDeletedDeferralReason() {
        // 1 is DeferralReason ID.
        DeferralReason deferralReason = donorRepository
                .findDeferralReasonById("1");
        assertNotNull("DeferralReason's object should not null.",
                deferralReason);
        assertTrue("Deferral's Reason Id should be 1.",
                deferralReason.getId() == 1);

    }

    
    /**
     * Should return null deferral reason where deferral reason is deleted from
     * the database findDeferralReasonById(String)
     */
    @Test(expected = NoResultException.class)
    public void findDeferralReasonById_shouldExpectNoResultExceptionWhenDeferralReason() {
        // 7 ID is deleted from DeferralReason.
        donorRepository.findDeferralReasonById("7");
    }

    @Test
    /**
     * DonorDeferral list size should be zero getDonorDeferrals(Long)
     */
    public void getDonorDeferrals_listSizeShouldZero() {
        // 2 is Donor ID
        assertEquals(
                "Deferral Donor is not found base on donor id so,List size should be zero.",
                0, donorRepository.getDonorDeferrals(2l).size());

    }

    @Test
    /**
     * DonorDeferral list size should not zero getDonorDeferrals(Long)
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
                    .getDeferredDonor().getId() == 1);
        }

    }

    @Test
    /**
     * Should return last donor derferral date getLastDonorDeferralDate(long)
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
    public void getAllLanguages_shuouldReturnNonEmptyList() {
        assertTrue("Languages List  should not be empty", !donorRepository.getAllLanguages().isEmpty());
    }

    @Test
    /**
     * Should return Non EMpty ID Types List
     */
    public void getAllIdTypes_shuouldReturnNonEmptyList() {
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
        Location l = new Location();
        l.setId(Long.parseLong("1"));
        AddressType a = new AddressType();
        a.setId(Long.parseLong("1"));
        donorBirthdate = "1991-06-11";
        donorBackingForm.setAddress(new Address());
        donorBackingForm.setContact(new Contact());
        donorBackingForm.setHomeAddressLine1("myaddress");
        donorBackingForm.setFirstName("firstname");
        donorBackingForm.setMiddleName("middlename");
        donorBackingForm.setLastName("lastname");
        donorBackingForm.setIsDeleted(false);
        donorBackingForm.setGender("male");
        donorBackingForm.setCallingName("CallingName");
        donorBackingForm.setHomeAddressCity("City");
        donorBackingForm.setHomeAddressCountry("country");
        donorBackingForm.setHomeAddressLine1("homeAddressLine1");
        user = new User();
        user.setId(userDbId);
        donorBackingForm.setHomeAddressDistrict("District");
        donorBackingForm.setVenue(l);
        donorBackingForm.setIdNumber("1111");
        donorBackingForm.setNotes("Notes");
        donorBackingForm.setMobileNumber("9999999999");
        donorBackingForm.setWorkNumber("8888888888");
        donorBackingForm.setPreferredAddressType(a);
        donorBackingForm.setHomeAddressProvince("Province");
        donorBackingForm.setHomeAddressState("State");
        donorBackingForm.setHomeAddressZipcode("361001");
        donorBackingForm.setBirthDate(donorBirthdate);
        donorBackingForm.setBirthDateEstimated(true);

    }

    /**
     * Called when update existing record.
     *
     * @param donorBackingForm
     */
    public void setBackingUpdateFormValue(DonorBackingForm donorBackingForm) {

        if (donorBackingForm.getAddress() != null) {
            donorBackingForm.setAddress(donorBackingForm.getAddress());
        } else {
            donorBackingForm.setAddress(new Address());
        }

        if (donorBackingForm.getContact() != null) {
            donorBackingForm.setContact(donorBackingForm.getContact());
        } else {
            donorBackingForm.setContact(new Contact());
        }
        
        Location l = new Location();
        l.setId(Long.parseLong("2"));
        AddressType a = new AddressType();
        a.setId(Long.parseLong("2"));
        donorBackingForm.setHomeAddressLine1("address_update");
        donorBackingForm.setFirstName("firstName_update");
        donorBackingForm.setMiddleName("middlename_update");
        donorBackingForm.setLastName("lastname_update");
        donorBackingForm.setIsDeleted(false);
        donorBackingForm.setGender("female");
        donorBackingForm.setCallingName("CallingName_update");
        donorBackingForm.setHomeAddressCity("City_update");
        donorBackingForm.setHomeAddressCountry("country_update");
        donorBackingForm.setHomeAddressDistrict("District_update");
        donorBackingForm.setVenue(l);
        donorBackingForm.setIdNumber("1212");
        donorBackingForm.setNotes("Notes_update");
        donorBackingForm.setMobileNumber("9878787878");
        donorBackingForm.setWorkNumber("874525452");
        donorBackingForm.setPreferredAddressType(a);
        donorBackingForm.setHomeAddressProvince("Province_update");
        donorBackingForm.setHomeAddressState("State_update");
        donorBackingForm.setHomeAddressZipcode("361001");
    }

    public Donor copyDonor(Donor donor) {
        Donor copyDonor = new Donor();
        Address address = new Address();
        Contact contact = new Contact();

        address.setHomeAddressLine1(donor.getAddress().getHomeAddressLine1());
        address.setHomeAddressCity(donor.getAddress().getHomeAddressCity());
        address.setHomeAddressCountry(donor.getAddress().getHomeAddressCountry());
        address.setHomeAddressDistrict(donor.getAddress().getHomeAddressDistrict());
        address.setHomeAddressState(donor.getAddress().getHomeAddressState());
        address.setHomeAddressProvince(donor.getAddress().getHomeAddressProvince());
        address.setHomeAddressZipcode(donor.getAddress().getHomeAddressZipcode());

        contact.setMobileNumber(donor.getContact().getMobileNumber());
        contact.setHomeNumber(donor.getContact().getHomeNumber());

        copyDonor.setAddress(address);
        copyDonor.setContact(contact);

        copyDonor.setAge(donor.getAge());
        copyDonor.setBirthDate(donor.getBirthDate());
        copyDonor.setBirthDateEstimated(donor.getBirthDateEstimated());
        copyDonor.setBirthDateInferred(donor.getBirthDateInferred());
        copyDonor.setBloodAbo(donor.getBloodAbo());
        copyDonor.setCallingName(donor.getCallingName());
        copyDonor.setDateOfLastDonation(donor.getDateOfLastDonation());
        copyDonor.setDonorHash(donor.getDonorHash());
        copyDonor.setVenue(donor.getVenue());
        copyDonor.setDonorStatus(donor.getDonorStatus());
        copyDonor.setFirstName(donor.getFirstName());
        copyDonor.setGender(donor.getGender());
        copyDonor.setLastName(donor.getLastName());
        copyDonor.setMiddleName(donor.getMiddleName());
        copyDonor.setIdNumber(donor.getIdNumber());
        copyDonor.setNotes(donor.getNotes());
        return copyDonor;
    }

    /**
     * UserPassword,BsisUserDetails(Principal) and authority detail store into
     * SecurityContextHolder.
     */
    public void userAuthentication() {
        applicationContext = new ClassPathXmlApplicationContext(
                "file:**/security-bsis-servlet.xml");
        userDetailsService = applicationContext.getBean(LoginUserService.class);
        BsisUserDetails userDetails = (BsisUserDetails) userDetailsService
                .loadUserByUsername("admin");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }


    @Test
    /**
     * Test passes on retrieving address of donor
     */
    public void getDonorAddress_ShouldReturnNotNull() {
        assertNotNull("Expected : Address Type  but Found : NULL",
                donorRepository.findDonorById(1l).getAddress());
    }

    @Test
    /**
     * Test passes on retrieving Contact of donor
     */
    public void getDonorContact_ShouldReturnNotNull() {
        assertNotNull("Expected : Contact Type but Found : NULL",
                donorRepository.findDonorById(1l).getContact());
    }

//    @Test
//    /**
//     * Passes if Non Empty Contact List returned
//     */
//    public void getDonorIdNumbers_ShouldReturnNotEmptyList() {
//        assertTrue("Expected : ID Number Set but Found : Empty Set",
//                donorRepository.findDonorById(1l).getIdNumbers().size() > 0);
//    }

    @Test
    /**
     *Test Passes if non Empty Language List returned 
     */
    public void getAllLanguages_ShouldReturnNonEmptyList() {
        assertTrue("Expected : Languages Set  but FOund : Empty Set",
                donorRepository.getAllLanguages().size() > 0);
    }
    
    @Test
    /**
     *Test Passes if non Empty Language List returned 
     */
    public void getAllIdTypes_ShouldReturnNonEmptyList() {
        assertTrue("Expected : ID Type Set  but FOund : Empty Set",
                donorRepository.getAllIdTypes().size() > 0);
    }
    
    @Test
    public void testFindDonorsByNumbersEmptyList() throws Exception {
    	List<Donor> donors = donorRepository.findDonorsByNumbers(new ArrayList<String>());
    	Assert.assertNotNull("Not null list was returned", donors);
    	Assert.assertEquals("No Donors were returned", 0, donors.size());
    }
    
    @Test
    public void testFindDonorsByNumbersNullList() throws Exception {
    	List<Donor> donors = donorRepository.findDonorsByNumbers(null);
    	Assert.assertNotNull("Not null list was returned", donors);
    	Assert.assertEquals("No Donors were returned", 0, donors.size());
    }
    
    @Test
    public void testFindDonorsByNumbersDoesntExist() throws Exception {
    	List<String> donorNumbers = new ArrayList<String>();
    	donorNumbers.add("xxxxxx");
    	List<Donor> donors = donorRepository.findDonorsByNumbers(donorNumbers);
    	Assert.assertNotNull("Not null list was returned", donors);
    	Assert.assertEquals("No Donors were returned", 0, donors.size());
    }
    
    @Test
    public void testFindDonorsByNumbers() throws Exception {
    	List<String> donorNumbers = new ArrayList<String>();
    	donorNumbers.add("000001");
    	donorNumbers.add("000003");
    	List<Donor> donors = donorRepository.findDonorsByNumbers(donorNumbers);
    	Assert.assertNotNull("Not null list was returned", donors);
    	Assert.assertEquals("Two Donors were returned", 2, donors.size());
    	Assert.assertEquals("donorNumber matches", "000001", donors.get(0).getDonorNumber());
    	Assert.assertEquals("donorNumber matches", "000003", donors.get(1).getDonorNumber());
    }
    
    @Test
    public void addMergedDonor() throws Exception {
    	List<DuplicateDonorBackup> backupLogs = new ArrayList<DuplicateDonorBackup>();
    	backupLogs.add(new DuplicateDonorBackup("1234567", "000001", 1l, null));
    	backupLogs.add(new DuplicateDonorBackup("1234567", "000001", null, 1l));
    	Donor oldDonor = donorRepository.findDonorByDonorNumber("000001", false);
    	List<Donor> donorsToMerge = new ArrayList<Donor>();
    	donorsToMerge.add(oldDonor);
    	Donor newDonor = new Donor();
    	newDonor.setDonorNumber("1234567");
    	newDonor.setFirstName("Merged");
    	newDonor.setLastName("Donor");
    	newDonor.setDonations(oldDonor.getDonations());
    	oldDonor.setDonations(null);
    	newDonor.setDeferrals(oldDonor.getDeferrals());
    	oldDonor.setDeferrals(null);
    	Donor savedDonor = donorRepository.addMergedDonor(newDonor, donorsToMerge, backupLogs);
    	Assert.assertNotNull("Id has been set", savedDonor.getId());
    	Assert.assertNotNull("Donations have been moved", savedDonor.getDonations());
    	Assert.assertEquals("Donations have been moved", 1, savedDonor.getDonations().size());
    	Assert.assertNotNull("Deferrals have been moved", savedDonor.getDeferrals());
    	Assert.assertEquals("Deferrals have been moved", 3, savedDonor.getDeferrals().size());
    	oldDonor = donorRepository.findDonorByDonorNumber("000001", false); // will return all statues
    	Assert.assertNull("Donations have been moved", oldDonor.getDonations());
    	Assert.assertNull("Deferrals have been moved", oldDonor.getDeferrals());
    }
    
    @Test
    public void testFindDonorByNumberExcludeMerged() throws Exception {
    	Donor donor = donorRepository.findDonorByDonorNumber("000001", false);
    	donor.setDonorStatus(DonorStatus.MERGED);
    	donorRepository.saveDonor(donor);
    	donor = donorRepository.findDonorByDonorNumber("000001", false, new DonorStatus[] { DonorStatus.MERGED });
    	Assert.assertNull("Donor was not found", donor);
    }
    
    @Test
    public void testFindDonorSummaryByDonorNumber() throws Exception {
    	DonorSummaryViewModel donorSummary = donorRepository.findDonorSummaryByDonorNumber("000001");
    	Assert.assertNotNull("Donor was found", donorSummary);
    	Assert.assertEquals("Donor was found", "firstName", donorSummary.getFirstName());
    	
    }
    
    @Test(expected = javax.persistence.NoResultException.class)
    public void testFindDonorSummaryByDonorNumberMerged() throws Exception {
    	Donor donor = donorRepository.findDonorByDonorNumber("000001", false);
    	donor.setDonorStatus(DonorStatus.MERGED);
    	donorRepository.saveDonor(donor);
    	DonorSummaryViewModel donorSummary = donorRepository.findDonorSummaryByDonorNumber("000001");
    }
    
//    @Test
//    /**
//     *Test Passes if Id Number saved In database
//     */
//    public void SaveIdNumber_shouldPersist(){
//        Donor donor = new Donor();
//        donor.setId(1l);
//        IdType idType = new IdType();
//        idType.setId(1l);
//        IdNumber idNumber = new IdNumber();
//        idNumber.setIdNumber("123");
//        idNumber.setDonorId(donor);
//        idNumber.setIdType(idType);
//        donorRepository.saveIdNumber(idNumber);
//        assertNotNull("Expected : INT but Found :  NULL" ,idNumber.getId());
//    }
}
