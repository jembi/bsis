/**
 * 
 */
package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.donor.Donor;
import model.donordeferral.DonorDeferral;
import model.location.Location;
import model.util.BloodGroup;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
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

/**
 * Bellow test Cases about findDonors method to check
 * find donor functionality of Donor Communication
 **/

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
	
	private static final  SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("dd/MM/yyyy"); 
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;
	
	@Before
	public void init() {
		try {
			if (connection == null)
				getConnection();
			// Insert Data into database using DonorDataset.xml
			IDataSet dataSet = getDataSet();
			DatabaseOperation.INSERT.execute(connection, dataSet);
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

	@SuppressWarnings("deprecation")
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DonorCommunicationsRepositoryDataset.xml");
		return new FlatXmlDataSet(file);
	}

	
	/**
	*should return empty list when no results are found
	
	* nors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	* List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	*/
	@Test
	public void findDonors_shouldReturnEmptyListWhenNoResultsFound() {
		List<Location> donorPanel = new ArrayList<Location>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String anyBloodGroup = "false";
		//Search with donor panel id 4
		long[] id = { 4 };
		//Search with  BloodGroup 'AB-'
		String[] bloodGroupStrArray = { "AB-" };
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		donorPanel    =  createDonerPanelListForSearch(id);
		bloodGroups  =  createBloodGroupListForSearch(bloodGroupStrArray);
		List<Object> results = new ArrayList<Object>();
		
		results = donorCommunicationsRepository.findDonors(donorPanel,clinicDate, lastDonationFromDate, 
				lastDonationToDate,bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
	    assertEquals("List size should be zero due to no search criteria match.",0, donors.size());
	}
	
	/**
	 * should return list of donors matching specified Donor Panel and Blood Group criteria
	 * 
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsMatchingGivenCriteria() {
		List<Location> donorPanel = new ArrayList<Location>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String anyBloodGroup = "false";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		donorPanel    =  createDonerPanelListForSearch(id);
		bloodGroups  =  createBloodGroupListForSearch(bloodGroupStrArray);
		List<Object> results = new ArrayList<Object>();
		
		results = donorCommunicationsRepository.findDonors(donorPanel,clinicDate, lastDonationFromDate, 
				lastDonationToDate,bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);
	
		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		
	    assertNotSame("List size should grater than zero due to search criteria match.",0, donors.size());
	    
	    boolean isvalid = false;
		for(Donor donor:donors){
			if(((donor.getBloodAbo()+donor.getBloodRh()).equals("O+")  || (donor.getBloodAbo()+donor.getBloodRh()).equals("A+"))
					&& (donor.getDonorPanel().getId() == 3 || donor.getDonorPanel().getId() == 1)){
				isvalid = true;
			}else{
				isvalid = false;
				break;
			}
		}
		assertTrue("Donors having blood Group 'O+' or 'A+' and donor panel id '1' or '3'",isvalid);
	}
	
	/**
	 * Should return donors who are due to donate on clinicDate, 
	 *
	 * findDonorFromDonorCommunication(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsDueToDonateOnClinicDate() throws ParseException {
			List<Location> donorPanel = new ArrayList<Location>();
			List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
			//Search with donor panel id 3 and 1
			long[] id = { 1 , 3 };
			//Search with  BloodGroup 'A+' and 'O+'
			String[] bloodGroupStrArray = { "A+","O+" };
			//Search with clinicDate '01/03/2014' 
			String clinicDate = "01/03/2014";
			String clinicDateToCheckdeferredDonor = "01/03/2014";
			String lastDonationFromDate = "";
			String lastDonationToDate = "";
			String anyBloodGroup = "false";
			
			donorPanel    =  createDonerPanelListForSearch(id);
			bloodGroups  =  createBloodGroupListForSearch(bloodGroupStrArray);
			Map<String, Object> pagingParams = createPagingParamsMapForSearch();

			List<Object> results = new ArrayList<Object>();
			results = donorCommunicationsRepository.findDonors(
					donorPanel, getEligibleDonorDate(clinicDate), lastDonationFromDate,
					lastDonationToDate, bloodGroups, anyBloodGroup,
					pagingParams, clinicDateToCheckdeferredDonor);

			@SuppressWarnings("unchecked")
			List<Donor> donors = (List<Donor>) results.get(0);
			
			assertNotSame("Should return donors who are due to donate on clinicDate '01/03/2014'",0, results.get(1));
			
			boolean isvalid = false;
			for(Donor donor:donors){
				if(donor.getDateOfLastDonation().before(DATE_FORMATER.parse(getEligibleDonorDate(clinicDate)))){
					isvalid = true;
				}else{
					isvalid = false;
					break;
				}
			}
			assertTrue("Donors who are due to donate on clinicDate",isvalid);
		} 	
	
	/**
	  * should return donors who donated during Date Of Last Donation period
	  * 
	  * findDonorFromDonorCommunication(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	  * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	@Test
	public void findDonors_shouldReturnDonorsWhoDonatedDuringDateOfLastDonationPeriod() throws ParseException{

		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String clinicDateToCheckdeferredDonor = "";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Search with  lastDonationDate from  '01/01/2013' to  '27/02/2014'
		String lastDonationFromDate = "01/01/2013";
		String lastDonationToDate = "27/02/2014";
		String anyBloodGroup = "false";

		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		donorPanel    =  createDonerPanelListForSearch(id);
		bloodGroups  =  createBloodGroupListForSearch(bloodGroupStrArray);
		
		List<Object> results = new ArrayList<Object>();
		results = donorCommunicationsRepository.findDonors(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		
		assertNotSame("Should return donors who donated during the period between lastDonationFromDate and lastDonationToDate",0, results.get(1));
		
		boolean isvalid = false;
		for(Donor donor:donors){
			if(donor.getDateOfLastDonation().before(DATE_FORMATER.parse(lastDonationToDate))  
					&& donor.getDateOfLastDonation().after(DATE_FORMATER.parse(lastDonationFromDate))){
				isvalid = true;
			}else{
				isvalid = false;
				break;
			}
		}
		assertTrue("Donors who donated during the period between lastDonationFromDate and lastDonationToDate",isvalid);
	}
	
	@Test
	/**
	 *  Should not return donors who have been deleted 
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnDeletedDonors() {
		//getDonorListSizeWithoutAnyCriteria() method return result count of donors without any criteria
		long donorListSizeBeforDonorDelete = getDonorListSizeWithoutAnyCriteria();
		long donorListSizeAfterDonorDelete = 0;
		//00003  Deleted DonorNumber 
		Donor donor = donorRepository.findDonorByDonorNumberIncludeDeleted("000003");		
		donorRepository.deleteDonor(donor.getId());
		donorListSizeAfterDonorDelete = getDonorListSizeWithoutAnyCriteria();
		assertNotSame("donor list size is not same after deleting the donor",donorListSizeBeforDonorDelete,donorListSizeAfterDonorDelete);
		
		Donor donorObj = donorRepository.findDonorByDonorNumberIncludeDeleted("000003");
		assertTrue("Donor having donorNumber ='00003' is deleted",donorObj.getIsDeleted());
	}

	@Test
	/**
	 *  should not return donors who will be currently deferred when specifying Date Of Last Donation period
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnCurrentlyDeferredDonors() throws ParseException{
		
		//Set value for making donor deferral
		String deferUntil = "07/07/2015";
		String deferralReasonId = "1";
		String deferralReasonText = "deferralReasonText";
		String deferredDonorId = "3";
		
		//Set value to find donors
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		//Search with  lastDonationDate from  '11/02/2014' to  '25/02/2014'
		String lastDonationFromDate ="11/02/2014";
		String lastDonationToDate = "25/02/2014";
		String anyBloodGroup = "false";
		
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		donorPanel    =  createDonerPanelListForSearch(id);
		bloodGroups  =  createBloodGroupListForSearch(bloodGroupStrArray);
		List<Object> results = new ArrayList<Object>();

		results = donorCommunicationsRepository.findDonors(donorPanel, getEligibleDonorDate(clinicDate), lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, clinicDate);

		@SuppressWarnings("unchecked")
		List<Donor> donorList = (List<Donor>) results.get(0);
		boolean isvalid = true;
		for(Donor donor:donorList){
			List<DonorDeferral> donorDeferralLst = donorRepository.getDonorDeferrals(donor.getId());
			for(DonorDeferral donorDeferral :donorDeferralLst)
			{
				if(donorDeferral.getDeferredUntil().before(DATE_FORMATER.parse(lastDonationToDate))  
						&& donorDeferral.getDeferredUntil().after(DATE_FORMATER.parse(lastDonationFromDate))){
					isvalid = false;
					break;
				}else{
					isvalid = true;
					}
				assertTrue("Donor's  DeferredUntil date Is less than lastDonationToDate",isvalid);
			}			
		}

		List<DonorDeferral> donorDeferralLst = donorRepository.getDonorDeferrals(2L);
		for(DonorDeferral donorDeferral :donorDeferralLst)
		{
			if(donorDeferral.getDeferredUntil().before(DATE_FORMATER.parse(lastDonationToDate))  
					&& donorDeferral.getDeferredUntil().after(DATE_FORMATER.parse(lastDonationFromDate))){
				isvalid = false;
				break;
			}else{
				isvalid = true;
				}
			assertTrue("Donor's  DeferredUntil date Is not less than lastDonationToDate",isvalid);
		}			
		int  donorListSizeBeforeDonorDeffered =donorList.size();		
			if(donorRepository.getDonorDeferrals(Long.parseLong(deferredDonorId)) == null || donorRepository.getDonorDeferrals(Long.parseLong(deferredDonorId)).isEmpty())
			{
				this.userAuthentication();
				donorRepository.deferDonor(deferredDonorId, deferUntil, deferralReasonId, deferralReasonText);
				List<DonorDeferral> donorDeferralLstAfterMakingDonorDeffered = donorRepository.getDonorDeferrals(Long.parseLong(deferredDonorId));
				if(!donorDeferralLstAfterMakingDonorDeffered.isEmpty())
				{
					assertSame("Donor having id = 3 is deffered",Long.parseLong(deferredDonorId),donorDeferralLstAfterMakingDonorDeffered.get(0).getDeferredDonor().getId());
				}
				results = donorCommunicationsRepository.findDonors(donorPanel, getEligibleDonorDate(clinicDate), lastDonationFromDate, lastDonationToDate,
						bloodGroups, anyBloodGroup, pagingParams, clinicDate);
				
				int donorListSizeAfterDonorDeffered = ((List<Donor>)results.get(0)).size();
				assertNotSame("donor list size is not same after donor Deffered",donorListSizeBeforeDonorDeffered,donorListSizeAfterDonorDeffered);
			}
	}
	
	@Test
	/**
	 *  should not return donors who will be deferred on date specified in Clinic Date
	 *  
	 * findDonors(List<Location> donorPanel, String clinicDate, String lastDonationFromDate, String lastDonationToDate,
	 * List<BloodGroup> bloodGroups, String anyBloodGroup, Map<String, Object> pagingParams, String clinicDateToCheckdeferredDonor)
	 */
	public void findDonors_shouldNotReturnDonorsDeferredOnClinicDate() throws ParseException{
		
		//Set value for making donor deferral
		String deferUntil = "23/05/2015";
		String deferralReasonId = "1";
		String deferralReasonText = "deferralReasonText";
		String deferredDonorId = "3";
		
		//Set value to find donors
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "22/05/2014";
		//Search with donor panel id 3 and 1
		long[] id = { 1 , 3 };
		//Search with  BloodGroup 'A+' and 'O+'
		String[] bloodGroupStrArray = { "A+","O+" };
		
		String lastDonationFromDate ="";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		donorPanel    =  createDonerPanelListForSearch(id);
		bloodGroups  =  createBloodGroupListForSearch(bloodGroupStrArray);
		List<Object> results = new ArrayList<Object>();

		results = donorCommunicationsRepository.findDonors(donorPanel, getEligibleDonorDate(clinicDate), lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, clinicDate);

		@SuppressWarnings("unchecked")
		List<Donor> donorList = (List<Donor>) results.get(0);
		boolean isvalid = true;
		for(Donor donor:donorList){
			List<DonorDeferral> donorDeferralLst = donorRepository.getDonorDeferrals(donor.getId());
			for(DonorDeferral donorDeferral :donorDeferralLst)
			{
				if(donorDeferral.getDeferredUntil().after(DATE_FORMATER.parse(clinicDate))  ){
					isvalid = false;
					break;
				}else{
					isvalid = true;
					}
				assertTrue("Donor's  DeferredUntil date Is less than clinicDate",isvalid);
			}			
		}

		List<DonorDeferral> donorDeferralLst = donorRepository.getDonorDeferrals(2L);
		for(DonorDeferral donorDeferral :donorDeferralLst)
		{
			if(donorDeferral.getDeferredUntil().after(DATE_FORMATER.parse(clinicDate)) ){
				isvalid = false;
				break;
			}else{
				isvalid = true;
				}
			assertTrue("Donor's  DeferredUntil date Is grater than clinicDate",isvalid);
		}			
		int  donorListSizeBeforeDonorDeffered =donorList.size();		
			if(donorRepository.getDonorDeferrals(Long.parseLong(deferredDonorId)) == null || donorRepository.getDonorDeferrals(Long.parseLong(deferredDonorId)).isEmpty())
			{
				this.userAuthentication();
				donorRepository.deferDonor(deferredDonorId, deferUntil, deferralReasonId, deferralReasonText);
				List<DonorDeferral> donorDeferralLstAfterMakingDonorDeffered = donorRepository.getDonorDeferrals(Long.parseLong(deferredDonorId));
				if(!donorDeferralLstAfterMakingDonorDeffered.isEmpty())
				{
					assertSame("Donor having id = 3 is deffered",Long.parseLong(deferredDonorId),donorDeferralLstAfterMakingDonorDeffered.get(0).getDeferredDonor().getId());
				}
				results = donorCommunicationsRepository.findDonors(donorPanel, getEligibleDonorDate(clinicDate), lastDonationFromDate, lastDonationToDate,
						bloodGroups, anyBloodGroup, pagingParams, clinicDate);
				
				int donorListSizeAfterDonorDeffered = ((List<Donor>)results.get(0)).size();
				assertNotSame("donor list size is not same after donor Deffered on 23/02/2014(same as search ClinicDate)",donorListSizeBeforeDonorDeffered,donorListSizeAfterDonorDeffered);
			}
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
	private List<Location> createDonerPanelListForSearch(long[] id) {
		List<Location> donorPanel = new ArrayList<Location>();

		for (long locId : id) {
			Location location = new Location();
			location.setId(locId);
			donorPanel.add(location);
		}
		return donorPanel;
	}
	private Map<String, Object> createPagingParamsMapForSearch() {

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", 0);
		pagingParams.put("sortColumnId", 0);
		pagingParams.put("length", 10);
		pagingParams.put("sortDirection", "asc");

		return pagingParams;
	}
	private List<BloodGroup> createBloodGroupListForSearch(
			String[] bloodGroupStrArray) {
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		for (String bloodGrpStr : bloodGroupStrArray) {
			BloodGroup bloodGroup = new BloodGroup();
			bloodGroup = BloodGroup.getBloodgroups().get(bloodGrpStr.toLowerCase());
			bloodGroups.add(bloodGroup);
		}
		return bloodGroups;
	}
	
	 private static String getEligibleDonorDate(String clinicDate) throws ParseException
	  {
		  Calendar cal = Calendar.getInstance();
		  if(!clinicDate.trim().equalsIgnoreCase(""))
		  {
			  Date dateObj = DATE_FORMATER.parse(clinicDate);
			  @SuppressWarnings({ "unused", "deprecation" })
			  Date clinicDt = new Date(clinicDate);
			  cal .setTime(dateObj);
			  cal.add(Calendar.DATE, -56);
		  }
		 return !clinicDate.trim().equalsIgnoreCase("") ? DATE_FORMATER.format(cal.getTime()) : "";
	  }
	 
	 private long getDonorListSizeWithoutAnyCriteria() {
			List<Location> donorPanel = new ArrayList<Location>();
			List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
			String clinicDate = "";
			String clinicDateToCheckdeferredDonor = "";
			String lastDonationFromDate = "";
			String lastDonationToDate = "";
			String anyBloodGroup = "true";
			Map<String, Object> pagingParams = createPagingParamsMapForSearch();

			List<Object> results = new ArrayList<Object>();
			results = donorCommunicationsRepository.findDonors(donorPanel,
					clinicDate, lastDonationFromDate, lastDonationToDate,
					bloodGroups, anyBloodGroup, pagingParams, clinicDateToCheckdeferredDonor);
			return  (Long) results.get(1);
		}
}