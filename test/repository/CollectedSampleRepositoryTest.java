package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.bloodbagtype.BloodBagType;
import model.collectedsample.CollectedSample;
import model.collectedsample.CollectionConstants;
import model.collectionbatch.CollectionBatch;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.user.User;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import repository.bloodtesting.BloodTypingStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
public class CollectedSampleRepositoryTest {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private CollectedSampleRepository collectedSampleRepository;
	private CollectedSample collectedSample;
	static IDatabaseConnection connection;
	private User user;
	private int userDbId = 1;
	public CollectedSampleRepositoryTest() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void init() {
		try {
			collectedSample = new CollectedSample();
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
			replacements.put("DateDeferredOn", DateUtils.addDays(today,
					-(2)));
			replacements.put("DateDeferredUnit", DateUtils.addDays(today,
					(2)));
			ReplacementDataSet rDataSet = new ReplacementDataSet(dataSet);
			for (String key : replacements.keySet()) {
				rDataSet.addReplacementObject("${" + key + "}",
						replacements.get(key));
			}
			DatabaseOperation.INSERT.execute(connection, rDataSet);
		} catch (Exception e) {
		}
	}

	@After 
	public  void  after() throws Exception {
		// Remove data from database
		try{
		//DatabaseOperation.DELETE.execute(connection, getDataSet());
		collectedSampleRepository.clearData();
		}catch(Exception e){
			e.printStackTrace();
		}
		
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

	private  IDataSet getDataSet() throws Exception {
		File file = new File(
				"test/dataset/CollectedSampleRepositoryDataset.xml");
		  final boolean enableColumnSensing = true;
		return new FlatXmlDataSet(file, false, enableColumnSensing);
	}
	
	
	@Test
	/**
	 * Should return CollectedSample with given Id findCollectedSampleById()
	 */
	public void findCollectedSampleById_ShouldReturnCollectedSampleWithGivenId() {
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleById(1l);
		assertNotNull(collectedSample);
		assertTrue("CollectedSample Id's value should be 1.",
				collectedSample.getId() == 1 ? true : false);
	}

	@Test
	/**
	 * Should return null when CollectedSample with Id does not exist
	 * findCollectedSampleById()
	 */
	public void findCollectedSampleById_ShouldReturnNullWhenCollectedSampleDoesNotExist() {
		// 1 is CollectedSample ID value
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleById(11l);
		assertNull(collectedSample);
	}

	@Test
	/**
	 * Should return null when CollectedSample has been deleted
	 * findCollectedSampleById()
	 */
	public void findCollectedSampleById_ShouldReturnNullWhenCollectedSampleIsDeleted() {
		// 2 is Deleted collectedSample's ID.
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleById(2l);
		assertNull(collectedSample);
	}

	/**
	 * Should return empty list when no search criteria is specified
	 * findCollectedSamples
	 * (String,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnEmptyListWhenNoSearchCriteriaSpecified() {
		String collectionNumber = "";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);

		List<Long> siteIds = new ArrayList<Long>();
		siteIds.add((long) -1);

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should be zero because search criteria is not specified here.",
				list.size() == 0 ? true : false);
	}

	/**
	 * Should return empty list when no match is found.
	 * findCollectedSamples(String
	 * ,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnEmptyListSizeWhenNoMatchIsFound() {
		String collectionNumber = "-111111";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);
		String[] bloodBagTypeID = { "1", "2", "3" };
		for (String bloodBagTypeId : bloodBagTypeID) {
			bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
		}

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);
		String[] centerID = { "1", "2", "3", "4", "5", "6", "7" };
		for (String center : centerID) {
			centerIds.add(Long.parseLong(center));
		}

		List<Long> siteIds = new ArrayList<Long>();
		String[] siteId = { "2", "3", "5", "7" };
		siteIds.add((long) -1);
		for (String site : siteId) {
			siteIds.add(Long.parseLong(site));
		}

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should be zero because matching record is not found.",
				list.size() == 0 ? true : false);
	}

	/**
	 * Should return all CollectedSamples with matching DINs.
	 * findCollectedSamples
	 * (String,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnCollectedSamplesWithMatchingDINs() {
		String collectionNumber = "D000001";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);
		String[] bloodBagTypeID = { "1", "2", "3" };
		for (String bloodBagTypeId : bloodBagTypeID) {
			bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
		}

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);
		String[] centerID = { "1", "2", "3", "4", "5", "6", "7" };
		for (String center : centerID) {
			centerIds.add(Long.parseLong(center));
		}

		List<Long> siteIds = new ArrayList<Long>();
		String[] siteId = { "2", "3", "5", "7" };
		siteIds.add((long) -1);
		for (String site : siteId) {
			siteIds.add(Long.parseLong(site));
		}

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should be zero because matching record is not found.",
				list.size() != 0 ? true : false);

		for (CollectedSample collectedSample : list) {
			if (collectedSample.getCollectionNumber().equals("D000001") == false) {
				assertTrue(
						"CollectedSample collectionNumber value should be 'D000001'.",
						true);
			}
		}
	}

	/**
	 * Should return all CollectedSamples with matching blood bag types.
	 * findCollectedSamples
	 * (String,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnCollectedSamplesWithMatchingBloodBagTypes() {
		String collectionNumber = "";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);
		bloodBagTypeIds.add(2);

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);
		String[] centerID = { "1", "2", "3", "4", "5", "6", "7" };
		for (String center : centerID) {
			centerIds.add(Long.parseLong(center));
		}

		List<Long> siteIds = new ArrayList<Long>();
		String[] siteId = { "2", "3", "5", "7" };
		siteIds.add((long) -1);
		for (String site : siteId) {
			siteIds.add(Long.parseLong(site));
		}

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should not zero because matching record is found.",
				list.size() != 0 ? true : false);

		for (CollectedSample collectedSample : list) {
			if (collectedSample.getBloodBagType().getId() != 2) {
				assertTrue("CollectedSample's bloodBagType value should be 2.",
						true);
			}
		}
	}

	/**
	 * Should return all CollectedSamples from matching Collection Centres.
	 * findCollectedSamples
	 * (String,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnCollectedSampleFromMatchingCollectionCentres() {
		String collectionNumber = "D000001";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);
		String[] bloodBagTypeID = { "1", "2", "3" };
		for (String bloodBagTypeId : bloodBagTypeID) {
			bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
		}

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);
		// 2 is CollectionCenter ID
		centerIds.add(2l);

		List<Long> siteIds = new ArrayList<Long>();
		String[] siteId = { "2", "3", "5", "7" };
		siteIds.add((long) -1);
		for (String site : siteId) {
			siteIds.add(Long.parseLong(site));
		}

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should not zero because matching record is found.",
				list.size() != 0 ? true : false);

		for (CollectedSample collectedSample : list) {
			if (collectedSample.getCollectionCenter().getId() != 2) {
				assertTrue(
						"In CollectedSample CollectionCenter ID value should be 2.",
						true);
			}
		}
	}

	/**
	 * Should return all CollectedSamples from matching Collection Sites.
	 * findCollectedSamples
	 * (String,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnCollectedSampleFromMatchingCollectionSites() {
		String collectionNumber = "";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);
		String[] bloodBagTypeID = { "1", "2", "3" };
		for (String bloodBagTypeId : bloodBagTypeID) {
			bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
		}

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);
		centerIds.add(2l);
		// 3 is CollectionSite ID
		List<Long> siteIds = new ArrayList<Long>();
		siteIds.add(3l);

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should not zero because matching record is found.",
				list.size() != 0 ? true : false);

		for (CollectedSample collectedSample : list) {
			if (collectedSample.getCollectionSite().getId() != 3) {
				assertTrue(
						"In CollectedSample CollectionSite's Id  value should be 3.",
						true);
			}
		}
	}

	/**
	 * Should return CollectedSamples collected during 'Date Collected Between'
	 * period findCollectedSamples
	 * (String,List<Integer>,List<Long>,List<Long>,String,String
	 * ,Boolean,Map<String,Object>)
	 */
	@Test
	public void findCollectedSamples_shouldReturnCollectedSamplesCollectedDuringSpecifiedPeriod() {
		String collectionNumber = "";
		String dateCollectedFrom = "20/03/2014";
		String dateCollectedTo = "27/03/2014";
		boolean includeTestedCollections = false;
		List<Integer> bloodBagTypeIds = new ArrayList<Integer>();
		bloodBagTypeIds.add(-1);
		String[] bloodBagTypeID = { "1", "2", "3" };
		for (String bloodBagTypeId : bloodBagTypeID) {
			bloodBagTypeIds.add(Integer.parseInt(bloodBagTypeId));
		}

		List<Long> centerIds = new ArrayList<Long>();
		centerIds.add((long) -1);
		String[] centerID = { "1", "2", "3", "4", "5", "6", "7" };
		for (String center : centerID) {
			centerIds.add(Long.parseLong(center));
		}

		List<Long> siteIds = new ArrayList<Long>();
		String[] siteId = { "2", "3", "5", "7" };
		siteIds.add((long) -1);
		for (String site : siteId) {
			siteIds.add(Long.parseLong(site));
		}

		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);

		@SuppressWarnings("unchecked")
		List<CollectedSample> list = (List<CollectedSample>) (collectedSampleRepository
				.findCollectedSamples(collectionNumber, bloodBagTypeIds,
						centerIds, siteIds, dateCollectedFrom, dateCollectedTo,
						includeTestedCollections, pagingParams)).get(0);
		assertTrue(
				"List size should not zero because matching record is found.",
				list.size() != 0 ? true : false);

		for (CollectedSample collectedSample : list) {
			if (collectedSample.getId() != 6) {
				assertTrue(
						"In CollectedSample collection date between 2014-03-19 to 2014-03-21.",
						true);
			}
		}
	}

	
	/**
	 * Should return all non-deleted CollectedSamples.
	 * getAllCollectedSamples ()
	 */
	@Test
	public void getAllCollectedSamples_shouldReturnAllNonDeletedCollectedSamples() {
		List<CollectedSample> listCollectedSample = collectedSampleRepository
				.getAllCollectedSamples();
		for (CollectedSample collectedSample : listCollectedSample) {
			assertFalse(
					"Deleted CollectedSample should not part of collectedsample list.",
					collectedSample.getIsDeleted());
		}
	}

	
	/**
	 * Should not return CollectedSamples that have been deleted.
	 * getAllCollectedSamples ()
	 */
	@Test
	public void getAllCollectedSamples_shouldNotReturnDeletedCollectedSamples() {
		List<CollectedSample> listCollectedSample = collectedSampleRepository
				.getAllCollectedSamples();
		for (CollectedSample collectedSample : listCollectedSample) {
			// 2 is deleted CollectedSample id.
			assertNotSame(
					"CollectedSample's id 2 is deleted from database. so Deleted Collectedsample should not included in the list.",
					2, collectedSample.getId());
		}
	}
	
	/**
	 * Should delete CollectedSample
	 * deleteCollectedSample(long)
	 */
	@Test
	public void deleteCollectedSample_ShouldDeleteCollectedSample(){
		// 3 is CollectedSample's ID.
		CollectedSample collectedSample = collectedSampleRepository.deleteCollectedSample(3l);
				assertTrue("Delete operation should complete successfully.",
						collectedSample.getIsDeleted());
				assertTrue("Deleted CollectedSample's id value should be 3.",
						collectedSample.getId() == 3 ? true : false);
	}
	
	@Test
	public void addCollectedSample_shouldInsertNewCollectedSample(){
		try{
			this.setValue(collectedSample);
			collectedSampleRepository.addCollectedSample(collectedSample);
			assertTrue("CollectedSample's Id should not zero. Once CollectedSample object should persist,new Id is generated and assigned to CollectedSample.",
					collectedSample.getId()!=0?true:false);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("outside method addCollectedSample_shouldInsertNewCollectedSample");
		
	}
	
	public void setPaginationParam(Map<String, Object> pagingParams) {
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", "0");
		pagingParams.put("sortColumnId", "0");
		pagingParams.put("length", "10");
		pagingParams.put("sortDirection", "asc");
	}
	
	public void setValue(CollectedSample collectedSample){
		Date date = new Date();
		collectedSample.setBloodAbo("");
		BloodBagType bloodBagType = new BloodBagType();
		bloodBagType.setId(2);
		collectedSample.setBloodBagType(bloodBagType);
		collectedSample.setBloodPressureDiastolic(56);
		collectedSample.setBloodPressureSystolic(42);
		collectedSample.setBloodRh("");
		//collectedSample.setBloodTestResults(bloodTestResults);
		collectedSample.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
		collectedSample.setCollectedOn(date);
		CollectionBatch collectionBatch = new CollectionBatch();
		collectionBatch.setId(1);
		collectedSample.setCollectionBatch(collectionBatch);
		Location collectionCenter = new Location();
		collectionCenter.setId(4l);
		collectedSample.setCollectionCenter(collectionCenter);
		collectedSample.setCollectionNumber("D000004");
		Location collectionSite = new Location();
		collectionSite.setId(5l);
		collectedSample.setCollectionSite(collectionSite);
		user = new User();
		user.setId(userDbId);
		collectedSample.setCreatedBy(user);
		collectedSample.setCreatedDate(date);
		collectedSample.setDonationCreatedBy(user);
		DonationType donationType = new DonationType();
		donationType.setId(2);
		collectedSample.setDonationType(donationType);
		Donor donor = new Donor();
		donor.setId(4l);
		collectedSample.setDonor(donor);
		collectedSample.setDonorPulse(60);
		BigDecimal donorWeight = new BigDecimal(60);
		collectedSample.setDonorWeight(donorWeight);
		collectedSample.setExtraBloodTypeInformation("");
		BigDecimal haemoglobinCount = new BigDecimal(14);
		collectedSample.setHaemoglobinCount(haemoglobinCount);
		collectedSample.setNotes("");
		
		
	}

}
