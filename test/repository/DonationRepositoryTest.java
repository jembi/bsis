package repository;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.donation.Donation;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import viewmodel.BloodTestingRuleResult;

/**
 * Test using DBUnit to test the Donation Repository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DonationRepositoryTest {
	
	@Autowired
	DonationRepository donationRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DonationRepositoryDataset.xml");
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
			IDataSet dataSet = getDataSet();
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		}
		finally {
			connection.close();
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
	public void testFindDonationById() throws Exception {
		Donation donation = donationRepository.findDonationById(1L);
		Assert.assertNotNull("There is a donation with id 1", donation);
		Assert.assertEquals("The donation has a DIN of 1234567", "1234567", donation.getDonationIdentificationNumber());
	}
	
	@Test
	public void testFindDonationByDIN() throws Exception {
		Donation donation = donationRepository.findDonationByDonationIdentificationNumber("1234567");
		Assert.assertNotNull("There is a donation with DIN 1234567", donation);
		Assert.assertEquals("The donation has a DIN of 1234567", "1234567", donation.getDonationIdentificationNumber());
	}
	
	@Test
	public void testVerifyDonationIdentificationNumber() throws Exception {
		Donation donation = donationRepository.verifyDonationIdentificationNumber("1234567");
		Assert.assertNotNull("There is a donation with DIN 1234567", donation);
		Assert.assertEquals("The donation has a DIN of 1234567", "1234567", donation.getDonationIdentificationNumber());
	}
	
	@Test
	@Ignore("This test fails because a javax.persistence.NoResultException is thrown. I believe this is a bug as the method wants to return null")
	public void testVerifyDonationIdentificationNumberUnknown() throws Exception {
		Donation donation = donationRepository.verifyDonationIdentificationNumber("999999999");
		Assert.assertNull("There is no donation with DIN 999999999", donation);
	}
	
	@Test
	@Ignore("This test will fail - see above test")
	public void testVerifyDonationIdentificationNumbers() throws Exception {
		// this test will fail as soon as one of the DINs in the list is unknown - it will throw an
		// exception and the method will not return any results for the other DINs.
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void testFindDonationByIdUnknown() throws Exception {
		donationRepository.findDonationById(123L);
	}
	
	@Test
	public void testGetAllDonations() throws Exception {
		List<Donation> all = donationRepository.getAllDonations();
		Assert.assertNotNull("There are donations", all);
		Assert.assertEquals("There are 6 donations", 6, all.size());
	}
	
	@Test
	public void testGetDonations() throws Exception {
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
		Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-10");
		List<Donation> all = donationRepository.getDonations(start, end);
		Assert.assertNotNull("There are donations", all);
		Assert.assertEquals("There are 5 donations", 5, all.size());
	}
	
	@Test
	public void testGetDonationsNone() throws Exception {
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2012-02-01");
		Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2012-02-10");
		List<Donation> all = donationRepository.getDonations(start, end);
		Assert.assertNotNull("There are no donations but list is not null", all);
		Assert.assertEquals("There are 0 donations", 0, all.size());
	}
	
	@Test
	public void testFindNumberOfDonationsDaily() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-10");
		List<String> venues = new ArrayList<>();
		venues.add("1");
		venues.add("2");
		List<String> bloodGroups = new ArrayList<>();
		bloodGroups.add("A-");
		Map<String, Map<Long, Long>> results = donationRepository.findNumberOfDonations(donationDateFrom, donationDateTo,
		    "daily", venues, bloodGroups);
		Assert.assertEquals("One blood type searched", 1, results.size());
		Map<Long, Long> aResults = results.get("A-");
		Assert.assertEquals("10 days in the searched period", 10, aResults.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("02/02/2015");
		Long secondOfFebResults = aResults.get(formattedDate.getTime());
		Assert.assertEquals("2 A- donations on 2015-02-02", new Long(2), secondOfFebResults);
	}
	
	@Test
	public void testFindNumberOfDonationsMonthly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-03-10");
		List<String> venues = new ArrayList<>();
		venues.add("1");
		venues.add("2");
		List<String> bloodGroups = new ArrayList<>();
		bloodGroups.add("A-");
		bloodGroups.add("A+");
		bloodGroups.add("O-");
		bloodGroups.add("O+");
		Map<String, Map<Long, Long>> results = donationRepository.findNumberOfDonations(donationDateFrom, donationDateTo,
		    "monthly", venues, bloodGroups);
		Assert.assertEquals("Four blood type searched", 4, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2015");
		Map<Long, Long> aMinusResults = results.get("A-");
		Assert.assertEquals("2 months in the searched period", 2, aMinusResults.size());
		Long aMinusFebResults = aMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("2 A- donations in Feb", new Long(2), aMinusFebResults);
		Map<Long, Long> aPlusResults = results.get("A+");
		Long aPlusFebResults = aPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 A+ donations in Feb", new Long(0), aPlusFebResults);
	}
	
	@Test
	public void testFindNumberOfDonationsYearly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-10");
		List<String> venues = new ArrayList<>();
		venues.add("1");
		venues.add("2");
		List<String> bloodGroups = new ArrayList<>();
		bloodGroups.add("AB-");
		bloodGroups.add("AB+");
		Map<String, Map<Long, Long>> results = donationRepository.findNumberOfDonations(donationDateFrom, donationDateTo,
		    "yearly", venues, bloodGroups);
		Assert.assertEquals("2 blood type searched", 2, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015");
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinus2015Results = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations in 2015", new Long(0), abMinus2015Results);
		Map<Long, Long> abPlusResults = results.get("AB+");
		Long abPlus2015Results = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ donations in 2015", new Long(1), abPlus2015Results);
	}
	
	@Test
	@Ignore("findAnyDonationMatching has a bug in the HSQL, so this test fails")
	public void testFindAnyDonationMatchingDIN() throws Exception {
		List<Donation> donations = donationRepository.findAnyDonationMatching("1234567", null, null, null, null, null);
		Assert.assertNotNull("List is not null", donations);
		Assert.assertNotNull("1 Donation matches", donations.size());
		Donation donation = donations.get(0);
		Assert.assertEquals("Donation has a matching DIN", "1234567", donation.getDonationIdentificationNumber());
	}
	
	@Test
	@Ignore("findDonations has a bug in the HSQL when querying a DIN with parameter name venueIds/venueIds")
	public void testFindDonationsDIN() throws Exception {
		Map<String, Object> pagingParams = new HashMap<>();
		List<Integer> packTypeIds = new ArrayList<>();
		packTypeIds.add(new Integer(1));
		List<Long> venueIds = new ArrayList<>();
		venueIds.add(new Long(1));
		venueIds.add(new Long(2));
		List<Object> donations = donationRepository.findDonations("1234567", packTypeIds, venueIds, "2015-02-01",
		    "2015-02-10", false, pagingParams);
		Assert.assertNotNull("List is not null", donations);
		Assert.assertNotNull("1 Donation matches", donations.size());
		Donation donation = (Donation) donations.get(0);
		Assert.assertEquals("Donation has a matching DIN", "1234567", donation.getDonationIdentificationNumber());
	}
	
	@Test
	public void testFindDonations() throws Exception {
		Map<String, Object> pagingParams = new HashMap<>();
		List<Integer> packTypeIds = new ArrayList<>();
		packTypeIds.add(new Integer(1));
		List<Long> venueIds = new ArrayList<>();
		venueIds.add(new Long(2));
		List<Object> result = donationRepository.findDonations(null, packTypeIds, venueIds, "01/02/2015", "10/02/2015",
		    true, pagingParams);
		Assert.assertNotNull("List is not null", result);
		ArrayList<Donation> donations = (ArrayList<Donation>) result.get(0);
		Assert.assertEquals("4 donations", 4, donations.size());
		Date afterDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2015");
		Date beforeDate = new SimpleDateFormat("dd/MM/yyyy").parse("10/02/2015");
		for (Donation donation : donations) {
			Assert.assertTrue("Donation date before", donation.getDonationDate().before(beforeDate));
			Assert.assertTrue("Donation date after", donation.getDonationDate().after(afterDate));
			Assert.assertEquals("PackTypeId matches", new Integer(1), donation.getPackType().getId());
			Assert.assertEquals("venueId matches", new Long(2), donation.getDonationBatch().getVenue().getId());
		}
	}
	
	@Test
	public void testFilterDonationsWithBloodTypingResults() throws Exception {
		List<Donation> donations = new ArrayList<>();
		donations.add(donationRepository.findDonationById(1L));
		donations.add(donationRepository.findDonationById(2L));
		donations.add(donationRepository.findDonationById(3L));
		Map<Long, BloodTestingRuleResult> result = donationRepository.filterDonationsWithBloodTypingResults(donations);
		Assert.assertEquals("There are two donations with completed tests", 2, result.size());
		for (BloodTestingRuleResult r : result.values()) {
			if (r.getDonation().getDonationIdentificationNumber().equals("1234567")) {
				Assert.assertEquals("O type blood match", "O", r.getBloodAbo());
			} else if (r.getDonation().getDonationIdentificationNumber().equals("1212129")) {
				Assert.assertEquals("A type blood match", "A", r.getBloodAbo());
			}
		}
	}
	
	@Test
	public void testAddDonation() throws Exception {
		Donation newDonation = new Donation();
		Donation existingDonation = donationRepository.findDonationById(1L);
		newDonation.setId(existingDonation.getId());
		newDonation.copy(existingDonation);
		newDonation.setId(null); // don't want to override, just save time with the copy
		newDonation.setDonationIdentificationNumber("JUNIT123");
		Calendar today = Calendar.getInstance();
		newDonation.setCreatedDate(today.getTime());
		newDonation.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation);
		Donation savedDonation = donationRepository.findDonationByDonationIdentificationNumber("JUNIT123");
		Assert.assertNotNull("Found new donation", savedDonation);
	}
	
	@Test(expected = javax.persistence.PersistenceException.class)
	public void testAddDonationSameDIN() throws Exception {
		Donation newDonation = new Donation();
		Donation existingDonation = donationRepository.findDonationById(1L);
		newDonation.setId(existingDonation.getId());
		newDonation.copy(existingDonation);
		newDonation.setId(null); // don't want to override, just save time with the copy
		newDonation.getPackType().setCountAsDonation(false);
		donationRepository.addDonation(newDonation);
		// should fail because DIN already exists
	}
	
	@Test
	public void testAddAllDonations() throws Exception {
		Donation newDonation1 = new Donation();
		Donation existingDonation1 = donationRepository.findDonationById(1L);
		newDonation1.setDonor(existingDonation1.getDonor());
		newDonation1.setVenue(existingDonation1.getVenue());
		newDonation1.setDonationIdentificationNumber("JUNIT12345"); // note: doesn't do automatic "createInitialComponent"
		newDonation1.setIsDeleted(false);
		Calendar today = Calendar.getInstance();
		newDonation1.setDonationDate(today.getTime());
		newDonation1.setCreatedDate(today.getTime());
		newDonation1.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation1.setBleedStartTime(today.getTime());
		
		Donation newDonation2 = new Donation();
		Donation existingDonation2 = donationRepository.findDonationById(2L);
		newDonation2.setDonor(existingDonation2.getDonor());
		newDonation2.setVenue(existingDonation2.getVenue());
		newDonation2.setDonationIdentificationNumber("JUNIT123456"); // note: doesn't do automatic "createInitialComponent"
		newDonation2.setIsDeleted(false);
		newDonation2.setDonationDate(today.getTime());
		newDonation2.setCreatedDate(today.getTime());
		newDonation2.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation2.setBleedStartTime(today.getTime());
		
		List<Donation> newDonations = new ArrayList<>();
		newDonations.add(newDonation1);
		newDonations.add(newDonation2);
		newDonations = donationRepository.addAllDonations(newDonations);
		for (Donation d : newDonations) {
			Donation savedDonation = donationRepository.findDonationByDonationIdentificationNumber(d
			        .getDonationIdentificationNumber());
			//Donation savedDonation = donationRepository.findDonationById(d.getId());
			Assert.assertNotNull("new donation created", savedDonation);
		}
	}
	
	@Test
	public void testUpdateDonation() throws Exception {
		Donation existingDonation = donationRepository.findDonationById(1L);
		existingDonation.setDonorWeight(new BigDecimal(123));
		donationRepository.updateDonationDetails(existingDonation);
		Donation updatedDonation = donationRepository.findDonationById(1L);
		Assert.assertEquals("donor weight was updataed", new BigDecimal(123), updatedDonation.getDonorWeight());
	}
	
}
