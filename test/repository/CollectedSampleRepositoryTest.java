package repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import model.worksheet.Worksheet;

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
import utils.DeleteIncludeStatus;

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
			replacements.put("DateDeferredOn", DateUtils.addDays(today, -(2)));
			replacements.put("DateDeferredUnit", DateUtils.addDays(today, (2)));
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
	public void after() throws Exception {
		// Remove data from database
		try {
			// DatabaseOperation.DELETE.execute(connection, getDataSet());
			collectedSampleRepository.clearData();
		} catch (Exception e) {
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

	private IDataSet getDataSet() throws Exception {
		File file = new File(
				"test/dataset/CollectedSampleRepositoryDataset.xml");
		final boolean enableColumnSensing = true;
		return new FlatXmlDataSet(file, false, enableColumnSensing);
	}

	/**
	 * Should insert new CollectedSample. addCollectedSample(CollectSample)
	 */
	@Test
	public void addCollectedSample_shouldInsertNewCollectedSample() {
		this.setValue(collectedSample);
		collectedSampleRepository.addCollectedSample(collectedSample);
		assertTrue(
				"CollectedSample's Id should not zero. Once CollectedSample object should persist,new Id is generated and assigned to CollectedSample.",
				collectedSample.getId() != 0 ? true : false);
	}

	/**
	 * Insert List of CollectedSample.
	 * addAllCollectedSamples(List<CollectedSample>).
	 */
	@Test
	public void addCollectedSamples_shouldPersistCollectedSampleList() {
		List<CollectedSample> collectedSampleList = new ArrayList<CollectedSample>();
		this.setValue(collectedSample);
		collectedSample.setCollectionNumber("000001");
		collectedSampleList.add(collectedSample);
		collectedSample = new CollectedSample();
		this.setValue(collectedSample);
		collectedSample.setCollectionNumber("000003");
		collectedSampleList.add(collectedSample);
		collectedSample = new CollectedSample();
		this.setValue(collectedSample);
		collectedSample.setCollectionNumber("000004");
		collectedSampleList.add(collectedSample);
		List<CollectedSample> returncollectedSampleList = collectedSampleRepository
				.addAllCollectedSamples(collectedSampleList);
		assertTrue("List size should not zero.",
				returncollectedSampleList.size() != 0 ? true : false);
	}

	/**
	 * Should update existing CollectedSample with CollectedSample object.
	 * updateCollectedSample(CollectedSample)
	 */
	@Test
	public void updateCollectedSample_shouldUpdateExistingCollectedSample() {
		this.updateValue(collectedSample);
		collectedSample.setId(1l);
		CollectedSample UpdatedCollectedSample = collectedSampleRepository
				.updateCollectedSample(collectedSample);
		assertNotNull("CollectedSample object should updated.",
				UpdatedCollectedSample);
		assertTrue("Collected Sample Object value should be 1.",
				UpdatedCollectedSample.getId() == 1 ? true : false);

	}

	/**
	 * Should return null when existing CollectedSample is null.
	 * updateCollectedSample(CollectedSample)
	 */
	@Test
	public void updateCollectedSample_shouldReturnNullWhenExistingCollectedSampleIsNull() {
		this.updateValue(collectedSample);
		collectedSample.setId(11l);
		CollectedSample UpdatedCollectedSample = collectedSampleRepository
				.updateCollectedSample(collectedSample);
		assertNull("CollectedSample object should null.",
				UpdatedCollectedSample);

	}

	/**
	 * Should return CollectedSample with given Id findCollectedSampleById()
	 */
	@Test
	public void findCollectedSampleById_ShouldReturnCollectedSampleWithGivenId() {
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleById(1l);
		assertNotNull(collectedSample);
		assertTrue("CollectedSample Id's value should be 1.",
				collectedSample.getId() == 1 ? true : false);
	}

	/**
	 * Should return null when CollectedSample with Id does not exist
	 * findCollectedSampleById()
	 */
	@Test
	public void findCollectedSampleById_ShouldReturnNullWhenCollectedSampleDoesNotExist() {
		// 1 is CollectedSample ID value
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleById(11l);
		assertNull(collectedSample);
	}

	/**
	 * Should return null when CollectedSample has been deleted
	 * findCollectedSampleById()
	 */
	@Test
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
	 * Should return all non-deleted CollectedSamples. getAllCollectedSamples ()
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
	 * Should delete CollectedSample deleteCollectedSample(long)
	 */
	@Test
	public void deleteCollectedSample_ShouldDeleteCollectedSample() {
		// 3 is CollectedSample's ID.
		CollectedSample collectedSample = collectedSampleRepository
				.deleteCollectedSample(3l);
		assertTrue("Delete operation should complete successfully.",
				collectedSample.getIsDeleted());
		assertTrue("Deleted CollectedSample's id value should be 3.",
				collectedSample.getId() == 3 ? true : false);
	}

	/**
	 * Should return default date value ('31/12/1970') when dateCollectFrom is
	 * null. getDateCollectedFromOrDefault
	 */
	@Test
	public void getDateCollectedFromOrDefault_shouldReturnDefaultDateWhenDateCollectedFromIsNull() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// 1 = getDateCollectedFromOrDefault()
		// 2 = getDateCollectedToOrDefault()
		String finalValue = dateFormat.format(collectedSampleRepository
				.testFromAndToDate("", 1));
		System.out.println(finalValue);
		assertTrue("From Date value should be  '31/12/1970'",
				finalValue.equals("31/12/1970") ? true : false);
	}

	/**
	 * Should return Date parsed from input String.
	 * getDateCollectedFromOrDefault(String)
	 */
	@Test
	public void getDateCollectedFromOrDefault_shouldReturnDateParsedFromInputString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// 1 = getDateCollectedFromOrDefault()
		// 2 = getDateCollectedToOrDefault()
		String finalValue = dateFormat.format(collectedSampleRepository
				.testFromAndToDate("29/03/2014", 1));
		System.out.println(finalValue);
		assertTrue("From Date value should be  '29/03/2014'",
				finalValue.equals("29/03/2014") ? true : false);
	}

	/**
	 * SShould return default date value('31/12/1970') , when Search Parameter
	 * DateCollectTo date value is null. getDateCollectedToOrDefault(String)
	 */
	@Test
	public void getDateCollectedToOrDefault_shouldReturnDefaultDateWhenToDateNull() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// 1 = getDateCollectedFromOrDefault()
		// 2 = getDateCollectedToOrDefault()
		String currentDate = dateFormat.format(new Date());
		String finalValue = dateFormat.format(collectedSampleRepository
				.testFromAndToDate("", 2));
		System.out.println(finalValue);
		assertTrue("To Date value should be '" + currentDate + "",
				finalValue.equals(currentDate) ? true : false);
	}

	/**
	 * Should return Date parsed from input String.
	 * getDateCollectedToOrDefault(String)
	 */
	@Test
	public void getDateCollectedToOrDefault_shouldReturnDateParsedFromInputString() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// 1 = getDateCollectedFromOrDefault()
		// 2 = getDateCollectedToOrDefault()
		String finalValue = dateFormat.format(collectedSampleRepository
				.testFromAndToDate("29/03/2014", 2));
		System.out.println(finalValue);
		assertTrue("To Date value should be  '29/03/2014'",
				finalValue.equals("29/03/2014") ? true : false);
	}

	/**
	 * Should Return CollectedSample Object.
	 * findCollectedSampleByCollectionNumber(String,DeleteIncludeStatus)
	 */
	@Test
	public void findCollectedSampleByCollectionNumber_ShouldNotNullWhenCollectedSampleNotDeleteAndDeletedRecordNotInclude() {
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleByCollectionNumber("D000001",
						DeleteIncludeStatus.DELETE_RECORD_NOT_INCLUDE);
		assertNotNull("CollectedSample Object should not null.",
				collectedSample);
		assertTrue(
				"CollectedSample CollectionNumber value should be 'D000001'.",
				collectedSample.getCollectionNumber().equals("D000001") ? true
						: false);
	}
	
	/**
	 * Should Return CollectedSample Object.
	 * findCollectedSampleByCollectionNumber(String,DeleteIncludeStatus)
	 */
	@Test
	public void findCollectedSampleByCollectionNumber_ShouldNotNullWhenCollectedSampleDeletedAndDeletedRecordInclude() {
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleByCollectionNumber("D000002",
						DeleteIncludeStatus.DELETE_RECORD_INCLUDE);
		assertNotNull("CollectedSample Object should not null.",
				collectedSample);
		assertTrue(
				"CollectedSample CollectionNumber value should be 'D000002'.",
				collectedSample.getCollectionNumber().equals("D000002") ? true
						: false);
	}
	/**
	 * Should Return null CollectedSample Object.
	 * findCollectedSampleByCollectionNumber(String,DeleteIncludeStatus)
	 */
	@Test
	public void findCollectedSampleByCollectionNumber_ShouldNotNullWhenCollectedSampleDeletedAndDeleteRecordNotInclude(){
		CollectedSample collectedSample = collectedSampleRepository
				.findCollectedSampleByCollectionNumber("D000002",
						DeleteIncludeStatus.DELETE_RECORD_NOT_INCLUDE);
		assertNull("CollectedSample Object should  null.",
				collectedSample);
	}
/**
 * It should persist CollectedSample with Worksheet.
 * 	saveToWorksheet(String,List<Integer>,List<Long>,List<Long>,String,String,Boolean,String)
 * @throws Exception
 */
	@Test
	public void saveToWorksheet_shouldPersistWorksheet() throws Exception
	{
		String collectionNumber = "D000001";
		String dateCollectedFrom = "";
		String dateCollectedTo = "";
		boolean includeTestedCollections = false;
		String worksheetNumber="W0314000000";
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
		collectedSampleRepository.saveToWorksheet(collectionNumber, bloodBagTypeIds, centerIds, siteIds, dateCollectedFrom, dateCollectedTo, includeTestedCollections, worksheetNumber);
		assertTrue("It should persist worksheet with collectedSample.",true);
	}
	/**
	 * Should return Null Worksheet.
	 * findWorksheet(String)
	 */
	@Test
	public void findCollectionsInWorksheet_ShouldReturnNullWorksheetWhenWorkSheetNumberNotExist()
	{
		
		assertNull(collectedSampleRepository.findWorksheet("-1"));
	}
	
	/**
	 * Should return Worksheet object.
	 * findCollectionsInWorksheet(String)
	 */
	@Test
	public void findCollectionsInWorksheet_ShouldReturnWorksheetWhenWorkSheetNumberExist()
	{
		//W0314000000 is worksheetNumber
		String worksheetnumber="W0314000000";
		Worksheet worksheet = collectedSampleRepository.findWorksheet(worksheetnumber); 
		assertNotNull("Should return Worksheet Object.",worksheet);
		assertTrue("Worksheetnumber value in Worksheet Object should be '"+worksheetnumber+"'",worksheet.getWorksheetNumber().equals(worksheetnumber));
		
	}
	
	/**
	 * Should return null CollectedSample List.
	 * findCollectionsInWorksheet(String)
	 */
	@Test
	public void findCollectionsInWorksheet_List_CollectedSample_ShouldReturnNullCollectedListSizeWhenMatchingWorkSheetNotExist(){
		//Worksheet is not exist into dataset.
		String worksheetnumber="-1";
		List<CollectedSample> collectedSampleList = collectedSampleRepository.findCollectionsInWorksheet(worksheetnumber);
		assertNull("CollectedSample List size should be zero.",collectedSampleList);
	}
	
	/**
	 * Should return CollectedSample List.
	 * findCollectionsInWorksheet(String)
	 */
	@Test
	public void findCollectionsInWorksheet_List_CollectedSample_ShouldReturnCollectedListSizeWhenMatchingWorkSheetExist(){
		//W0314000000 is worksheetNumber
				String worksheetnumber="W0314000000";
		List<CollectedSample> collectedSampleList = collectedSampleRepository.findCollectionsInWorksheet(worksheetnumber);
		assertTrue("CollectedSample List size should not zero.",collectedSampleList.size()!=0?true:false);
		for(CollectedSample collectedSample:collectedSampleList){
			if(collectedSample.getId()!=1){
				assertTrue("CollectedSample Id's should be 1.",false);
			}
		}
	}
	
	/**
	 * Should return empty CollectedSample List.
	 * findCollectionsInWorksheet(Long)
	 */
	@Test
	public void findCollectionsInWorksheet_List_Object_ShouldReturnNullCollectedListSizeWhenMatchingWorkSheetNotExist(){
		
		//10 worksheetID is not exist.
		long worksheetId=10l;
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);
		@SuppressWarnings("unchecked")
		List<CollectedSample> collectedSampleList = (List<CollectedSample>)(collectedSampleRepository.findCollectionsInWorksheet(worksheetId,pagingParams)).get(0);
		assertTrue("CollectedSample List size should be zero.",collectedSampleList.size()==0?true:false);
	}
	
	/**
	 * Should return CollectedSample List.
	 * findCollectionsInWorksheet(String)
	 */
	@Test
	public void findCollectionsInWorksheet_List_Object_ShouldReturnCollectedListSizeWhenMatchingWorkSheetExist(){
		//1 is worksheet ID.
		long worksheetId=1l;
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		setPaginationParam(pagingParams);
		@SuppressWarnings("unchecked")
		List<CollectedSample> collectedSampleList = (List<CollectedSample>)(collectedSampleRepository.findCollectionsInWorksheet(worksheetId,pagingParams)).get(0);
		assertTrue("CollectedSample List size should not zero.",collectedSampleList.size()!=0?true:false);
		for(CollectedSample collectedSample:collectedSampleList){
			if(collectedSample.getId()!=1){
				assertTrue("CollectedSample Id's should be 1.",false);
			}
		}
	}
	/**
	 * Should return zero CollectedSample Count
	 * getTotalCollectionsInWorksheet(long)
	 */
	@Test
	public void getTotalCollectionsInWorksheet_ShouldReturnZeroWhenWorkSheetIdNotExist(){
		//10 worksheetid is not exist.
		long worksheetid=10;
		assertTrue("CollectedSample count should be zero.",(collectedSampleRepository.getTotalCollectUsingWorkSheetId(worksheetid))==0?true:false);
	}
	
	/**
	 * Should return  CollectedSample Count
	 * getTotalCollectionsInWorksheet(long)
	 */
	@Test
	public void getTotalCollectionsInWorksheet_ShouldReturnCollectedSampleCountWhenWorkSheetIdExist(){
		//2 is Worksheet ID.
		long worksheetid=2;
		
		assertTrue("CollectedSample count should be 2.",(collectedSampleRepository.getTotalCollectUsingWorkSheetId(worksheetid))==2?true:false);
	}
	
	public void setPaginationParam(Map<String, Object> pagingParams) {
		pagingParams.put("sortColumn", "id");
		pagingParams.put("start", "0");
		pagingParams.put("sortColumnId", "0");
		pagingParams.put("length", "10");
		pagingParams.put("sortDirection", "asc");
	}

	public void setValue(CollectedSample collectedSample) {
		Date date = new Date();
		collectedSample.setBloodAbo("");
		BloodBagType bloodBagType = new BloodBagType();
		bloodBagType.setId(2);
		collectedSample.setBloodBagType(bloodBagType);
		collectedSample.setBloodPressureDiastolic(56);
		collectedSample.setBloodPressureSystolic(42);
		collectedSample.setBloodRh("");
		// collectedSample.setBloodTestResults(bloodTestResults);
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

	public void updateValue(CollectedSample collectedSample) {
		Date date = new Date();
		collectedSample.setBloodAbo("");
		BloodBagType bloodBagType = new BloodBagType();
		bloodBagType.setId(3);
		collectedSample.setBloodBagType(bloodBagType);
		collectedSample.setBloodPressureDiastolic(58);
		collectedSample.setBloodPressureSystolic(44);
		collectedSample.setBloodRh("");
		// collectedSample.setBloodTestResults(bloodTestResults);
		collectedSample.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
		collectedSample.setCollectedOn(date);
		CollectionBatch collectionBatch = new CollectionBatch();
		collectionBatch.setId(2);
		collectedSample.setCollectionBatch(collectionBatch);
		Location collectionCenter = new Location();
		collectionCenter.setId(6l);
		collectedSample.setCollectionCenter(collectionCenter);

		Location collectionSite = new Location();
		collectionSite.setId(7l);
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
		donor.setId(3l);
		collectedSample.setDonor(donor);
		collectedSample.setDonorPulse(60);
		BigDecimal donorWeight = new BigDecimal(63);
		collectedSample.setDonorWeight(donorWeight);
		collectedSample.setExtraBloodTypeInformation("");
		BigDecimal haemoglobinCount = new BigDecimal(16);
		collectedSample.setHaemoglobinCount(haemoglobinCount);
		collectedSample.setNotes("");

	}

}
