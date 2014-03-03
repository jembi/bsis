/**
 * 
 */
package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.donor.Donor;
import model.location.Location;
import model.util.BloodGroup;

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

import repository.DonorRepository;
import security.LoginUserService;
import security.V2VUserDetails;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class DonorCommunicationTest {

	@Autowired
	DonorRepository donorRepository;

	@Autowired
	UtilController utilController;
	ApplicationContext applicationContext = null;
	UserDetailsService userDetailsService;

	/**
	 * Bellow test Cases about findDonorFromDonorCommunication method to check
	 * find donor functionality of Donor Communication
	 **/

	@Test
	@Verifies(value = "should return empty list (instead of a null object) when no results are found", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnEmptyListWhenNoRecordFoundTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String anyBloodGroup = null;
		long[] id = { 4 };
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		donorPanel = createDonerPanelListForSearch(id);
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertEquals("Return Empty list should have 0 elements",
				Long.valueOf(0), Long.valueOf(donors.size()));
	}

	@Test
	@Verifies(value = "Should return donors with blood groups that match the criteria in bloodGroups", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnDonorsWithBloodGroupCriteriaTest() {
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		String[] bloodGroupStrArray = { "o-" };

		bloodGroups = createBloodGroupListForSearch(bloodGroupStrArray);
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");
 
		Long totalRecords = (Long) results.get(1);
		assertNotSame(
				"Should return the list of donors with blood groups that match the criteria in bloodGroups ",
				Long.valueOf(0), totalRecords);
		assertNotNull("list of donors with blood groups that match the criteria in bloodGroups is not null or empty ", totalRecords);
	}

	@Test
	@Verifies(value = "Should not return donors not matching the criteria stipulated in bloodGroups", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnDonorsNotMatchBloodGroupCriteriaTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		String[] bloodGroupStrArray = { "a-" };

		bloodGroups = createBloodGroupListForSearch(bloodGroupStrArray);
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");

		Long totalRecords = (Long) results.get(1);
		assertEquals(
				"Should not return donors not matching the criteria stipulated in bloodGroups",
				Long.valueOf(0), totalRecords);
	}

	@Test
	@Verifies(value = "Should return donors that are part of Donor Panels matching the criteria in donorPanel", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnDonorWithDonorPanelCriteriaTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String anyBloodGroup = null;
		long[] id = { 3, 7 };
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		donorPanel = createDonerPanelListForSearch(id);
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");

		Long totalRecords = (Long) results.get(1);
		assertNotSame(
				"Should return the list of donors that are part of Donor Panels matching the criteria in donorPanel",
				Long.valueOf(0), totalRecords);
	}

	@Test
	@Verifies(value = "Should not return donors not matching the criteria stipulated in donorPanel", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnDonorNotMatchDonorPanelCriteriaTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String anyBloodGroup = null;
		long[] id = { 4 };
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		donorPanel = createDonerPanelListForSearch(id);
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");

		Long totalRecords = (Long) results.get(1);
		assertEquals(
				"Should not return donors not matching the criteria stipulated in donorPanel",
				Long.valueOf(0), totalRecords);
	}

	@Test
	@Verifies(value = "Should return donors who are due to donate on clinicDate", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnDonorWhoDueToDonateTest() {
		try {
			List<Location> donorPanel = new ArrayList<Location>();
			List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
			String clinicDate = "27/2/2015";
			String lastDonationFromDate = "";
			String lastDonationToDate = "";
			String anyBloodGroup = "false";

			Map<String, Object> pagingParams = createPagingParamsMapForSearch();

			List<Object> results = new ArrayList<Object>();
			results = donorRepository.findDonorFromDonorCommunication(
					donorPanel, clinicDate, lastDonationFromDate,
					lastDonationToDate, bloodGroups, anyBloodGroup,
					pagingParams, clinicDate);

			Long totalRecords = (Long) results.get(1);
			assertNotSame(
					"Should return donors who are due to donate on clinicDate",
					Long.valueOf(0), totalRecords);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@Verifies(value = "Should return donors who donated during the period between lastDonationFromDate and lastDonationToDate", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnDonorWhoDonateBetweenSpecificPeriodTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "01/01/2013";
		String lastDonationToDate = "27/02/2014";
		String anyBloodGroup = "false";

		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertNotSame(
				"Should return donors who donated during the period between lastDonationFromDate and lastDonationToDate",
				Long.valueOf(0), Long.valueOf(donors.size()));
	}

	@Test
	@Verifies(value = "Should not return donors who have been deleted", method = "findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnNotDeletedDonorsTest() {
		try{
		long donorListSizeBeforDonorDelete = getDonorListSizeWithoutAnyCriteria();
		long donorListSizeAfterDonorDelete = 0;
		Donor donor = donorRepository.findDonorByDonorNumberIncludeDeleted("000005");
		if(!donor.getIsDeleted())
		{
			donorRepository.deleteDonor(donor.getId());
			donorListSizeAfterDonorDelete = getDonorListSizeWithoutAnyCriteria();
			assertNotSame("donor list size is not same after deleting the donor",donorListSizeBeforDonorDelete,donorListSizeAfterDonorDelete);
		}
		else
		{
			donorListSizeAfterDonorDelete = getDonorListSizeWithoutAnyCriteria();
			assertSame("donor list size is same as donor is already deleted",donorListSizeBeforDonorDelete,donorListSizeAfterDonorDelete);
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	@Verifies(value="should not return donors who will be deferred on the date of the clinic, or for part of the period selected",method="findDonorFromDonorCommunication(List<Location>,String,String,String,List<BloodGroup>,String ,Map<String, Object> )")
	public void returnNotDefferedDonorOnSpeficClinicDateTest()
	{
		String deferUntil = "05/3/2014";
		String deferralReasonId = "1";
		String deferralReasonText = "deferralReasonText";
		long donorListSizeBeforDonorDeffered = getDonorTocheckDefferedTestCase();
		long donorListSizeAfterDonorDeffered = 0;
		try {
			userAuthentication();
			donorRepository.deferDonor("2", deferUntil, deferralReasonId, deferralReasonText);
			donorListSizeAfterDonorDeffered = getDonorTocheckDefferedTestCase();
			assertNotSame("donor list size is not same after donor Deffered",donorListSizeBeforDonorDeffered,donorListSizeAfterDonorDeffered);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void userAuthentication() {
		  applicationContext = new ClassPathXmlApplicationContext("file:**/security-v2v-servlet.xml");
		  userDetailsService = applicationContext.getBean(LoginUserService.class);
		  V2VUserDetails userDetails = (V2VUserDetails) userDetailsService.loadUserByUsername("admin");
		  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
		    userDetails, userDetails.getPassword(),userDetails.getAuthorities()); 
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
			bloodGroup = BloodGroup.getBloodgroups().get(bloodGrpStr);
			bloodGroups.add(bloodGroup);
		}
		return bloodGroups;
	}

	private long getDonorListSizeWithoutAnyCriteria() {
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "true";
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams, "");
		Long totalRecords = (Long) results.get(1);
		return totalRecords;
	}
	
	private long getDonorTocheckDefferedTestCase()
	{
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "03/03/2014";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";

		Map<String, Object> pagingParams = createPagingParamsMapForSearch();

		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(
				donorPanel, clinicDate, lastDonationFromDate,
				lastDonationToDate, bloodGroups, anyBloodGroup,
				pagingParams, clinicDate);

		return (Long) results.get(1);
	}
}
