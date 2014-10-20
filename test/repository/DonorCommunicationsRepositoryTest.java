package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import model.collectedsample.CollectionConstants;
import model.location.Location;
import model.util.BloodGroup;
import utils.CustomDateFormatter;

import org.apache.commons.lang.time.DateUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.ReplacementDataSet;
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
import org.springframework.transaction.annotation.Transactional; 


import security.LoginUserService;
import security.V2VUserDetails;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
public class DonorCommunicationsRepositoryTest {
	
	@Autowired
	DonorRepository donorRepository;	
	@Autowired
	private DataSource dataSource;
	static IDatabaseConnection connection;
	
	@Autowired
	DonorCommunicationsRepository donorCommunicationsRepository;
		
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;
		
	@Before
	public void init() {
		try {
			if (connection == null)
				getConnection();
			// Insert Data into database using DonorCommunicationsRepositoryDataset.xml
			IDataSet dataSet = getDataSet();
			
			Date today = new Date();
			Map<String, Object> replacements = new HashMap<String, Object>();
			
			replacements.put("today", today);
			replacements.put("twoWeeksAgo", DateUtils.addDays(today, -(14)));
			replacements.put("oneMonthAgo", DateUtils.addDays(today, -(30)));
			replacements.put("threeMonthsAgo", DateUtils.addDays(today, -(90)));
			replacements.put("sixMonthsAhead", DateUtils.addDays(today, (180)));
			
			ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
			for (String key : replacements.keySet()) {
				rDataSet.addReplacementObject("${" + key + "}",
						replacements.get(key));
			}
			DatabaseOperation.INSERT.execute(connection, rDataSet);
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

	@SuppressWarnings("deprecation")
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DonorCommunicationsRepositoryDataset.xml");
		return new FlatXmlDataSet(file);
	}

	
	/**
	* should return empty list when no results are found
	* 
	* findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	* List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	*/
	@Test
	public void findDonors_shouldReturnEmptyListWhenNoResultsFound() {
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		boolean anyBloodGroup = false ;
		//Search with donor panel id 4
		long[] id = { 4 };
		//Search with  BloodGroup 'AB-'
		String[] bloodGroupStrArray = { "AB-" };
		Map<String, Object> pagingParams = createPagingParamsMap();

		donorPanels    =  createDonorPanelList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		
		assertEquals("List size should be zero, no matching search results.",
				0,	donorCommunicationsRepository.findDonors(donorPanels,clinicDate, lastDonationFromDate, 
						lastDonationToDate, bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor).size());
	}
	
	/**
	 * should return list of donors matching specified Donor Panel and Blood Group criteria
	 * 
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsMatchingGivenCriteria() {
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		boolean anyBloodGroup = false;
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		Map<String, Object> pagingParams = createPagingParamsMap();

		donorPanels    =  createDonorPanelList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		List<Donor> results = new ArrayList<Donor>();
		
		results = donorCommunicationsRepository.findDonors(donorPanels, clinicDate, lastDonationFromDate, 
				lastDonationToDate, bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);
		
	    assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, results.size());
	    
	    boolean isvalid = true;
		for(Donor donor:results){
			if(!((donor.getBloodAbo()+donor.getBloodRh()).equals("O+")  || (donor.getBloodAbo()+donor.getBloodRh()).equals("A+"))
					&& (donor.getDonorPanel().getId() == 3 || donor.getDonorPanel().getId() == 1))
			{
				isvalid = false;
				break;
			}
		}
		assertTrue("Donors in list should match the DonorPanel and BloodGroup search criteria used.",isvalid);
	}
	
	/**
	 * Should return donors who are due to donate on clinicDate
	 *
	 * findDonorFromDonorCommunication(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsDueToDonateOnClinicDate() throws ParseException {
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		//Search with clinicDate 
		String clinicDate = CustomDateFormatter.format(new Date());
		String clinicDateToCheckdeferredDonor = CustomDateFormatter.format(new Date());
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		
		donorPanels   =  createDonorPanelList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		Map<String, Object> pagingParams = createPagingParamsMap();

		List<Donor> results = new ArrayList<Donor>();
		results = donorCommunicationsRepository.findDonors(donorPanels, getEligibleDonationDate(clinicDate), lastDonationFromDate,
				lastDonationToDate, bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);

		assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, results.size());
		
		boolean isvalid = true;
		for(Donor donor:results){
			if(donor.getDateOfLastDonation().after(CustomDateFormatter.parse(getEligibleDonationDate(clinicDate))))
			{
				isvalid = false;
				break;
			}
		}
		assertTrue("Donors in list should be due to donate on clinicDate", isvalid);
	} 	
	
	/**
	  * should return donors who donated during Date Of Last Donation period
	  * 
	  * findDonorFromDonorCommunication(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	  * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsWhoDonatedDuringDateOfLastDonationPeriod() throws ParseException{

		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Search with lastDonationDate period
		String lastDonationFromDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (-365)));
		String lastDonationToDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (0)));
		boolean anyBloodGroup = false;

		Map<String, Object> pagingParams = createPagingParamsMap();
		donorPanels    =  createDonorPanelList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		
		List<Donor> results = new ArrayList<Donor>();
		results = donorCommunicationsRepository.findDonors(donorPanels,	clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);

		assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, results.size());
		
		boolean isvalid = true;
		for(Donor donor:results){
			if(donor.getDateOfLastDonation().after(CustomDateFormatter.parse(lastDonationToDate))  
					|| donor.getDateOfLastDonation().before(CustomDateFormatter.parse(lastDonationFromDate)))
			{
				isvalid = false;
				break;
			}
		}
		assertTrue("Donors in list should have date of last donation between lastDonationFromDate and lastDonationToDate",isvalid);
	}
	
	@Test
	/**
	 *  Should not return donors who have been deleted 
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnDeletedDonors() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Specify date of last donation period
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		donorPanels = createDonorPanelList(id);
		bloodGroups = createBloodGroupList(bloodGroupStrArray);
		//List<Object> results = new ArrayList<Object>();

		List<Donor> donors = donorCommunicationsRepository.findDonors(donorPanels, getEligibleDonationDate(clinicDate), lastDonationFromDate, 
				lastDonationToDate, bloodGroups, anyBloodGroup, pagingParams, clinicDate);		
		
		for (Donor donor : donors) {
			// Donor with ID=5 is set as deleted
			assertFalse(
					"Deleted Donor should not be included in the list of donor results.",
					donor.getId() == 5 ? true : false);
			assertFalse("Donors included in the list of donor results should not be marked as deleted.",
					donor.getIsDeleted());
		}
	}

	@Test
	@Transactional
	/**
	 *  should not return donors who will be currently deferred when specifying only Donor Panels and Blood Groups
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnCurrentlyDeferredDonors() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Specify date of last donation period
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		donorPanels = createDonorPanelList(id);
		bloodGroups = createBloodGroupList(bloodGroupStrArray);
		
		List<Donor> donors = donorCommunicationsRepository.findDonors(donorPanels, getEligibleDonationDate(clinicDate), lastDonationFromDate, 
				lastDonationToDate, bloodGroups, anyBloodGroup, pagingParams, clinicDate);	
		
		assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, donors.size());
		
		boolean isvalid = true;
		for(Donor donor : donors){			
			for (DonorDeferral deferral : donor.getDeferrals()){
				if(deferral.getDeferredUntil().after(new Date())){
					isvalid = false;
					break;
				}
			}	
		}
		assertTrue("Donors in list should not be currently deferred",isvalid);
		
	}
	
	@Test
	@Transactional
	/**
	 *  should not return donors who will be currently deferred when specifying Date Of Last Donation period
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnCurrentlyDeferredDonorsWithLastDonationPeriodSpecified() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Specify date of last donation period
		String lastDonationFromDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (-30)));
		String lastDonationToDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (30)));
		boolean anyBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		donorPanels = createDonorPanelList(id);
		bloodGroups = createBloodGroupList(bloodGroupStrArray);
		
		List<Donor> donors = donorCommunicationsRepository.findDonors(donorPanels, getEligibleDonationDate(clinicDate), lastDonationFromDate, 
				lastDonationToDate,	bloodGroups, anyBloodGroup, pagingParams, clinicDate);	
		
		assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, donors.size());
		
		boolean isvalid = true;
		for(Donor donor : donors){
			for (DonorDeferral deferral : donor.getDeferrals()){
				if(deferral.getDeferredUntil().after(new Date())){
					isvalid = false;
					break;
				}
			}	
		}
		assertTrue("Donors in list should not be currently deferred",isvalid);
		
	}
	
	@Test
	@Transactional
	/**
	 *  should not return donors who will be deferred on date specified in Clinic Date
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnDonorsDeferredOnClinicDate() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> donorPanels = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (60)));
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		String lastDonationFromDate ="";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		donorPanels    =  createDonorPanelList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);

		List<Donor> donors = donorCommunicationsRepository.findDonors(donorPanels, getEligibleDonationDate(clinicDate), lastDonationFromDate, 
				lastDonationToDate,	bloodGroups, anyBloodGroup, pagingParams, clinicDate);	

		assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, donors.size());
		
		boolean isvalid = true;
		for(Donor donor : donors){
			for (DonorDeferral deferral : donor.getDeferrals()){
				if(deferral.getDeferredUntil().after(CustomDateFormatter.parse(clinicDate))){
					isvalid = false;
					break;
				}
			}	
		}
		assertTrue("Donors in list should not be deferred at date of Clinic Date",isvalid);
		
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
	
	private List<Location> createDonorPanelList(long[] id) {
		List<Location> donorPanel = new ArrayList<Location>();

		for (long locId : id) {
			Location location = new Location();
			location.setId(locId);
			donorPanel.add(location);
		}
		return donorPanel;
	}
	
	private List<BloodGroup> createBloodGroupList(String[] bloodGroupStrArray) {
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		for (String bloodGrpStr : bloodGroupStrArray) {
			BloodGroup bloodGroup = new BloodGroup();
			bloodGroup = BloodGroup.getBloodgroups().get(bloodGrpStr.toLowerCase());
			bloodGroups.add(bloodGroup);
		}
		return bloodGroups;
	}
	
	private Map<String, Object> createPagingParamsMap() {

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", 0);
		pagingParams.put("sortColumnId", 0);
		pagingParams.put("length", 10);
		pagingParams.put("sortDirection", "asc");

		return pagingParams;
	}
	
	private String getEligibleDonationDate(String clinicDate) throws ParseException {
		if(!clinicDate.trim().equalsIgnoreCase(""))
		{
			return CustomDateFormatter.format(DateUtils.addDays(CustomDateFormatter.parse(clinicDate), -(CollectionConstants.BLOCK_BETWEEN_COLLECTIONS)));
		}
		else return "";
	 }
	 
	 private long getDonorListSizeWithoutAnyCriteria() {
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = true;
		Map<String, Object> pagingParams = createPagingParamsMap();

		List<Donor> results = new ArrayList<Donor>();
		results = donorCommunicationsRepository.findDonors(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);
		return  (long) results.size();
	}
}