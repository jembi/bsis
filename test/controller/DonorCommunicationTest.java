/**
 * 
 */
package controller;

import static org.junit.Assert.*;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import controller.UtilController;
import repository.DonorRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/v2v-servlet.xml")
@WebAppConfiguration
public class DonorCommunicationTest {

	@Autowired
	DonorRepository donorRepository;

	@Autowired
	UtilController utilController;

	/*
	 * Bellow test Cases about findDonorFromDonorCommunication method to check find donor functionality of Donor Communication
	 */
	
	@Test
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
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertEquals("Return Empty list should have 0 elements", Long.valueOf(0), Long.valueOf(donors.size()));
	}
	
	@Test
	public void returnDonorsWithBloodGroupCriteriaTest() {
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		String[] bloodGroupStrArray = {"o-"};
		
		bloodGroups = createBloodGroupListForSearch(bloodGroupStrArray);
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertNotSame("Should return donors with blood groups that match the criteria in bloodGroups ",Long.valueOf(0), Long.valueOf(donors.size()));
	}
	
	@Test
	public void returnDonorsNotMatchBloodGroupCriteriaTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		String[] bloodGroupStrArray = {"a-"};
		
		bloodGroups = createBloodGroupListForSearch(bloodGroupStrArray);
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertEquals("Should not return donors not matching the criteria stipulated in bloodGroups",Long.valueOf(0), Long.valueOf(donors.size()));
	}
	
	@Test
	public void returnDonorWithDonorPanelCriteriaTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String anyBloodGroup = null;
		long[] id = { 3 , 7 };
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		
		donorPanel = createDonerPanelListForSearch(id);
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertNotSame("Should return donors that are part of Donor Panels matching the criteria in donorPanel", Long.valueOf(0), Long.valueOf(donors.size()));
	}
	
	@Test
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
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertEquals("should not return donors not matching the criteria stipulated in donorPanel", Long.valueOf(0), Long.valueOf(donors.size()));
	}
	
	@Test
	public void returnDonorWhoDueToDonateTest() {

		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "27/2/2014";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertNotSame("Should return donors who are due to donate on clinicDate", Long.valueOf(0), Long.valueOf(donors.size()));
	}
	
	@Test
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
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		assertNotSame("Should return donors who donated during the period between lastDonationFromDate and lastDonationToDate", Long.valueOf(0), Long.valueOf(donors.size()));
	}
	/**
	 * 
	 */
	@Test
	public void returnNotDeletedDonorsTest(){
	try{	
		Donor donor = donorRepository.findDonorById(5L);
		int donorListSizeBeforDonorDelete = getDonorWithoutAnyCriteria().size();
		donor.setId(0L);
		donorRepository.deleteDonor(donor.getId());
		int donorListSizeAfterDonorDelete = getDonorWithoutAnyCriteria().size();
		//
		assertNotSame("donor list size is not same after deleting the donor", donorListSizeBeforDonorDelete, donorListSizeAfterDonorDelete);
	}catch(Exception e){
		e.printStackTrace();
	}
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
	
	private List<BloodGroup> createBloodGroupListForSearch(String[] bloodGroupStrArray) {
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		for(String bloodGrpStr : bloodGroupStrArray){
			BloodGroup bloodGroup = new BloodGroup();
			bloodGroup = BloodGroup.getBloodgroups().get(bloodGrpStr);
			bloodGroups.add(bloodGroup);
		}
		return bloodGroups;
	}
	
	private List<Donor> getDonorWithoutAnyCriteria()
	{
		List<Location> donorPanel = new ArrayList<Location>();
		List<BloodGroup> bloodGroups = new ArrayList<BloodGroup>();
		String clinicDate = "";
		String lastDonationFromDate = "";
		String lastDonationToDate = "";
		String anyBloodGroup = "false";
		
		Map<String, Object> pagingParams = createPagingParamsMapForSearch();
		
		List<Object> results = new ArrayList<Object>();
		results = donorRepository.findDonorFromDonorCommunication(donorPanel,
				clinicDate, lastDonationFromDate, lastDonationToDate,
				bloodGroups, anyBloodGroup, pagingParams);

		@SuppressWarnings("unchecked")
		List<Donor> donors = (List<Donor>) results.get(0);
		@SuppressWarnings("unused")
		Long totalRecords = (Long) results.get(1);
		
		return donors;
	}
}
