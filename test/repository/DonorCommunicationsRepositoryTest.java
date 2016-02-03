package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.donor.Donor;
import model.donor.DonorStatus;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.util.BloodGroup;

import org.apache.commons.lang.time.DateUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import utils.CustomDateFormatter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DonorCommunicationsRepositoryTest {
	
	@Autowired
	DonorRepository donorRepository;	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	DonorCommunicationsRepository donorCommunicationsRepository;
		
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DonorCommunicationsRepositoryDataset.xml");
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
	}

	private IDatabaseConnection getConnection() throws SQLException {
		IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
		DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
		return connection;
	}
		
	@Before
	public void init() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			// Insert Data into database using DonorCommunicationsRepositoryDataset.xml
			IDataSet dataSet = getDataSet();
			
			Date today = new Date();
			Map<String, Object> replacements = new HashMap<String, Object>();
			
			replacements.put("today", today);
			replacements.put("yesterday", DateUtils.addDays(today, -(1)));
			replacements.put("twoWeeksAgo", DateUtils.addDays(today, -(14)));
			replacements.put("oneMonthAgo", DateUtils.addDays(today, -(30)));
			replacements.put("threeMonthsAgo", DateUtils.addDays(today, -(90)));
			replacements.put("threeMonthsAhead", DateUtils.addDays(today, (90)));
			replacements.put("sixMonthsAhead", DateUtils.addDays(today, (180)));
			
			ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
			for (String key : replacements.keySet()) {
				rDataSet.addReplacementObject("${" + key + "}",
						replacements.get(key));
			}
			DatabaseOperation.CLEAN_INSERT.execute(connection, rDataSet);
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
	
	/**
	* should return empty list when no results are found
	* 
	* findDonors(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	* List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	*/
	@Test
	public void findDonors_shouldReturnEmptyListWhenNoResultsFound() throws ParseException {
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		boolean anyBloodGroup = false ;
		boolean noBloodGroup = false;
		//Search with venue id 4
		long[] id = { 4 };
		//Search with  BloodGroup 'AB-'
		String[] bloodGroupStrArray = { "AB-" };
		Map<String, Object> pagingParams = createPagingParamsMap();

		venues    =  createVenueList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
                assertEquals("List size should be zero, no matching search results.",
                        0, donorCommunicationsRepository.findDonors(venues,clinicDate, lastDonationFromDate,
                                lastDonationToDate, bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDateToCheckdeferredDonor).size());
            
	}
	
	/**
	 * should return list of donors matching specified Venue and Blood Group criteria
	 * 
	 * findDonors(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsMatchingGivenCriteria()throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		Map<String, Object> pagingParams = createPagingParamsMap();

		venues    =  createVenueList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		List<Donor> results = new ArrayList<Donor>();
		
                results = donorCommunicationsRepository.findDonors(venues, clinicDate, lastDonationFromDate,
                        lastDonationToDate, bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);
	    assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, results.size());
	    
	    boolean isvalid = true;
		for(Donor donor:results){
			if(!((donor.getBloodAbo()+donor.getBloodRh()).equals("O+")  || (donor.getBloodAbo()+donor.getBloodRh()).equals("A+"))
					&& (donor.getVenue().getId() == 3 || donor.getVenue().getId() == 1))
			{
				isvalid = false;
				break;
			}
		}
		assertTrue("Donors in list should match the Venue and BloodGroup search criteria used.",isvalid);
	}
	
	/**
	 * Should return donors who are due to donate on clinicDate
	 *
	 * findDonorFromDonorCommunication(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsDueToDonateOnClinicDate() throws ParseException {
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		//Search with clinicDate 
		String clinicDate = CustomDateFormatter.format(new Date());
		String clinicDateToCheckdeferredDonor = CustomDateFormatter.format(new Date());
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;
		
		venues   =  createVenueList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		Map<String, Object> pagingParams = createPagingParamsMap();

		List<Donor> results = new ArrayList<Donor>();
		results = donorCommunicationsRepository.findDonors(venues, clinicDate, lastDonationFromDate,
				lastDonationToDate, bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);

		assertNotSame("List size should be greater than zero, with donors matching search criteria.", 0, results.size());
		
		boolean isvalid = true;
		for(Donor donor:results){
			if(donor.getDueToDonate().after(CustomDateFormatter.parse(clinicDate)))
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
	  * findDonorFromDonorCommunication(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	  * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsWhoDonatedDuringDateOfLastDonationPeriod() throws ParseException{

		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Search with lastDonationDate period
		String lastDonationFromDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (-365)));
		String lastDonationToDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (0)));
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;

		Map<String, Object> pagingParams = createPagingParamsMap();
		venues    =  createVenueList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);
		
		List<Donor> results = new ArrayList<Donor>();
		results = donorCommunicationsRepository.findDonors(venues,	clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);

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
	 * findDonors(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnDeletedDonors() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Specify date of last donation period
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		venues = createVenueList(id);
		bloodGroups = createBloodGroupList(bloodGroupStrArray);
		//List<Object> results = new ArrayList<Object>();

		List<Donor> donors = donorCommunicationsRepository.findDonors(venues, clinicDate, lastDonationFromDate,
				lastDonationToDate, bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDate);		
		
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
	public void findDonors_shouldNotReturnMergedDonors() throws ParseException{
		List<Donor> donors = donorCommunicationsRepository.findDonors(createVenueList(new long[] { 1, 3 }), "", "", "",
		    createBloodGroupList(new String[] { "A+", "O+" }), false, false, createPagingParamsMap(), "");
		
		for (Donor donor : donors) {
			// Donor with ID=6 is set as status merged
			assertFalse("Merged Donor should not be included in the list of donor results.", donor.getId() == 6 ? true
			        : false);
			assertFalse("Donors included in the list of donor results should not be merged.",
			    donor.getDonorStatus() == DonorStatus.MERGED);
		}
	}

	@Test
	/**
	 *  should not return donors who will be currently deferred when specifying only Venues and Blood Groups
	 *  
	 * findDonors(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnCurrentlyDeferredDonors() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Specify date of last donation period
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		venues = createVenueList(id);
		bloodGroups = createBloodGroupList(bloodGroupStrArray);
		
		List<Donor> donors = donorCommunicationsRepository.findDonors(venues, clinicDate, lastDonationFromDate,
				lastDonationToDate, bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDate);	
		
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
	/**
	 *  should not return donors who will be currently deferred when specifying Date Of Last Donation period
	 *  
	 * findDonors(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnCurrentlyDeferredDonorsWithLastDonationPeriodSpecified() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Specify date of last donation period
		String lastDonationFromDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (-30)));
		String lastDonationToDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (30)));
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		venues = createVenueList(id);
		bloodGroups = createBloodGroupList(bloodGroupStrArray);
		
		List<Donor> donors = donorCommunicationsRepository.findDonors(venues, clinicDate, lastDonationFromDate,
				lastDonationToDate,	bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDate);	
		
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
	/**
	 *  should not return donors who will be deferred on date specified in Clinic Date
	 *  
	 * findDonors(List<Location> venue, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnDonorsDeferredOnClinicDate() throws ParseException{
		
		//Set values to use for findDonors() method parameters
		List<Location> venues = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = CustomDateFormatter.format(DateUtils.addDays(new Date(), (60)));
		//Search with venue id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		String lastDonationFromDate ="";
		String lastDonationToDate = "";
		boolean anyBloodGroup = false;
		boolean noBloodGroup = false;
		
		Map<String, Object> pagingParams = createPagingParamsMap();
		venues    =  createVenueList(id);
		bloodGroups  =  createBloodGroupList(bloodGroupStrArray);

		List<Donor> donors = donorCommunicationsRepository.findDonors(venues, clinicDate, lastDonationFromDate,
				lastDonationToDate,	bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDate);	

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
	
	private List<Location> createVenueList(long[] id) {
		List<Location> venue = new ArrayList<Location>();

		for (long locId : id) {
			Location location = new Location();
			location.setId(locId);
			venue.add(location);
		}
		return venue;
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
	 
	 private long getDonorListSizeWithoutAnyCriteria() throws ParseException {
		List<Location> venue = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		boolean anyBloodGroup = true;
		boolean noBloodGroup = false;
		Map<String, Object> pagingParams = createPagingParamsMap();

		List<Donor> results = new ArrayList<Donor>();
                results = donorCommunicationsRepository.findDonors(venue,
                        clinicDate, lastDonationFromDate, lastDonationToDate,
                        bloodGroups, anyBloodGroup, noBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);
            
		return  (long) results.size();
	}
}