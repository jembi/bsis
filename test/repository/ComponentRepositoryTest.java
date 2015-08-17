package repository;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.component.Component;
import model.component.ProductStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeType;

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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import security.BsisUserDetails;

/**
 * Test using DBUnit to test the ProductRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class ComponentRepositoryTest {
	
	@Autowired
	ComponentRepository componentRepository;
	
	@Autowired
	DonationRepository donationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductStatusChangeReasonRepository productStatusChangeReasonRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ProductRepositoryDataset.xml");
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
			initSpringSecurityUser(); // must be done after data has been inserted successfully
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
	
	public void initSpringSecurityUser() throws Exception {
		BsisUserDetails user = new BsisUserDetails(userRepository.findUserById(1));
		TestingAuthenticationToken auth = new TestingAuthenticationToken(user, "Credentials");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	// TODO: test the following methods
	//findMatchingComponentsForRequest
	//findNumberOfIssuedComponents
	//addComponentCombination
	
	@Test
	@Transactional
	public void testGetAllComponents() throws Exception {
		List<Component> all = componentRepository.getAllComponents();
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 7 Components", 7, all.size());
	}
	
	@Test
	@Transactional
	public void testGetComponents() throws Exception {
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<Component> all = componentRepository.getComponents(fromDate, toDate);
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 7 Components", 7, all.size());
	}
	
	@Test
	@Transactional
	public void testGetComponentsFromComponentIds() throws Exception {
		String[] componentIds = new String[] { "1", "2", "4" };
		List<Component> all = componentRepository.getComponentsFromComponentIds(componentIds);
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 3 Components", 3, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - problem in HSQL: could not resolve property: donationIdentificationNumber of: model.component.Component [SELECT c FROM model.component.Component c WHERE c.donationIdentificationNumber = :donationIdentificationNumber and c.isDeleted = :isDeleted]")
	public void isComponentCreatedFalse() throws Exception {
		boolean created = componentRepository.isComponentCreated("4444444");
		Assert.assertFalse("Component not created", created);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - problem in HSQL: could not resolve property: donationIdentificationNumber of: model.component.Component [SELECT c FROM model.component.Component c WHERE c.donationIdentificationNumber = :donationIdentificationNumber and c.isDeleted = :isDeleted]")
	public void isComponentCreatedTrue() throws Exception {
		boolean created = componentRepository.isComponentCreated("3333333");
		Assert.assertTrue("Component created", created);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.component.Component [SELECT c FROM model.component.Component c where c.isDeleted = :isDeleted and c.isIssued= :isIssued]")
	public void testGetAllUnissuedComponents() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedComponents();
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 3 Components", 3, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.component.Component [SELECT c FROM model.component.Component c where c.isDeleted = :isDeleted and c.isIssued= :isIssued]")
	public void testGetAllUnissuedComponentsAboRhd() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedComponents("PROCESSED", "A", "+");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.component.Component [SELECT p FROM model.component.Component p where p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate]")
	public void testGetAllUnissuedThirtyFiveDayComponents() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedThirtyFiveDayComponents();
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: type of: model.component.Component [SELECT p FROM model.component.Component p where p.type = :componentType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate]")
	public void testGetAllUnissuedThirtyFiveDayComponentsWithParameters() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedThirtyFiveDayComponents("PROCESSED", "A", "+");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("There appears to be a bug in the HQL: could not resolve property: type of: model.component.Component")
	public void testGetAllComponentsWithComponentTypeQUARANTINED() throws Exception {
		List<Component> all = componentRepository.getAllComponents("QUARANTINED");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 3 Components", 3, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("There appears to be a bug in the HQL: could not resolve property: type of: model.component.Component")
	public void testGetAllComponentsWithComponentTypeEXPIRED() throws Exception {
		List<Component> all = componentRepository.getAllComponents("EXPIRED");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Transactional
	public void testFindComponentWithId() throws Exception {
		Component one = componentRepository.findComponent(1l);
		Assert.assertNotNull("There is a Component with id 1", one);
		Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testFindComponentById() throws Exception {
		Component one = componentRepository.findComponentById(1l);
		Assert.assertNotNull("There is a Component with id 1", one);
		Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testFindComponentByIdUnknown() throws Exception {
		Component one = componentRepository.findComponent(1111l);
		Assert.assertNull("There is no Component with id 1111", one);
	}
	
	@Test
	@Transactional
	@Ignore("A bug - there is no attribute of Component called productNumber")
	public void testFindComponentByProductNumber() throws Exception {
		componentRepository.findComponent("123");
	}
	
	@Test
	@Transactional
	public void testFindComponentsByDIN() throws Exception {
		List<Component> all = componentRepository.findComponentsByDonationIdentificationNumber("1111111");
		Assert.assertNotNull("There are Components with DIN 111111", all);
		Assert.assertFalse("There are Components with DIN 111111", all.isEmpty());
		Assert.assertEquals("There are 2 Components with DIN 1111111", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testFindComponentsByDINUnknown() throws Exception {
		List<Component> all = componentRepository.findComponentsByDonationIdentificationNumber("1111112");
		Assert.assertNotNull("Does not return an null", all);
		Assert.assertTrue("There are 0 Components with DIN 111112", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindComponentByDINAndComponentTypeId() throws Exception {
		Component one = componentRepository.findComponent("1111111", "1");
		Assert.assertNotNull("There is a Component with DIN 1111111", one);
		Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testFindComponentByDINAndComponentTypeIdUnknown() throws Exception {
		Component one = componentRepository.findComponent("1111112", "1");
		Assert.assertNull("There is no a Component with DIN 1111112", one);
	}
	
	@Test
	@Transactional
	public void testFindComponentByComponentTypes() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.PROCESSED);
		List<Integer> componentTypeIds = new ArrayList<Integer>();
		componentTypeIds.add(1);
		List<Component> all = componentRepository.findComponentByComponentTypes(componentTypeIds, status, pagingParams);
		Assert.assertNotNull("Does not return an null", all);
		Assert.assertEquals("There are 3 'Whole Blood Single Pack - CPDA' Components", 3, all.size());
	}
	
	@Test
	@Transactional
	public void testFindComponentByComponentTypesNone() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		List<Integer> componentTypeIds = new ArrayList<Integer>();
		componentTypeIds.add(1);
		List<Component> all = componentRepository.findComponentByComponentTypes(componentTypeIds, status, pagingParams);
		Assert.assertNotNull("Does not return a null list", all);
		Assert.assertTrue("There are no Components", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindComponentByDINAndStatus() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		status.add(ProductStatus.PROCESSED);
		List<Component> all = componentRepository.findComponentByDonationIdentificationNumber("1111111", status, pagingParams);
		Assert.assertNotNull("There is a Component with DIN 1111111", all);
		Assert.assertFalse("There is a Component with DIN 1111111", all.isEmpty());
		Assert.assertEquals("There should be two components", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testFindComponentByDINAndStatusNone() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.EXPIRED);
		List<Component> all = componentRepository.findComponentByDonationIdentificationNumber("1111111", status, pagingParams);
		Assert.assertTrue("There should be 0 components", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindComponentByDINAndStatusUnknown() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		status.add(ProductStatus.PROCESSED);
		List<Component> all = componentRepository.findComponentByDonationIdentificationNumber("1111112", status, pagingParams);
		Assert.assertTrue("There should be 0 components", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindAnyComponentDIN() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<Component> all = componentRepository.findAnyComponent("1111111", null, null, null, null, pagingParams);
		Assert.assertNotNull("There is a Component with DIN 1111111", all);
		Assert.assertFalse("There is a Component with DIN 1111111", all.isEmpty());
		Assert.assertEquals("There should be 2 components", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testFindAnyComponentDINAndStatus() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		List<Component> all = componentRepository.findAnyComponent("1111111", null, status, null, null, pagingParams);
		Assert.assertNotNull("There are matching components", all);
		Assert.assertEquals("There should be 1 components", 1, all.size());
	}
	
	@Test
	@Transactional
	public void testFindAnyComponentQuarantinedType1() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		List<Integer> componentTypeIds = new ArrayList<Integer>();
		componentTypeIds.add(1);
		List<Component> all = componentRepository.findAnyComponent(null, componentTypeIds, status, null, null, pagingParams);
		Assert.assertNotNull("There aren't matching components", all);
		Assert.assertTrue("There should be 0 components", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindAnyComponentBetweenDates() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<Component> all = componentRepository.findAnyComponent(null, null, null, start, end, pagingParams);
		Assert.assertNotNull("There are matching components", all);
		Assert.assertEquals("There should be 7 components", 7, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in the HQL:  org.hibernate.QueryException: could not resolve property: componentType of: model.componenttype.ComponentType [SELECT p FROM model.componenttype.ComponentType p where p.componentType = :componentTypeName]")
	public void testFindComponentTypeByComponentTypeName() throws Exception {
		ComponentType one = componentRepository.findComponentTypeByComponentTypeName("Whole Blood Single Pack - CPDA");
		Assert.assertNotNull("ComponentType match", one);
		Assert.assertEquals("ComponentType is correct", "0011", one.getComponentTypeNameShort());
	}
	
	@Test
	@Transactional
	public void testFindComponentTypeByComponentTypeId() throws Exception {
		ComponentType one = componentRepository.findComponentTypeBySelectedComponentType(1);
		Assert.assertNotNull("ComponentType match", one);
		Assert.assertEquals("ComponentType is correct", "Whole Blood Single Pack - CPDA", one.getComponentTypeName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	@Transactional
	public void testFindComponentTypeByComponentTypeIdUnknown() throws Exception {
		componentRepository.findComponentTypeBySelectedComponentType(123);
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChanges() throws Exception {
		Component discardedComponent = componentRepository.findComponent(6l);
		List<ProductStatusChange> changes = componentRepository.getComponentStatusChanges(discardedComponent);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Component was DISCARDED", "DISCARDED", changes.get(0).getNewStatus().toString());
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChangesNone() throws Exception {
		Component quarantinedComponent = componentRepository.findComponent(4l);
		List<ProductStatusChange> changes = componentRepository.getComponentStatusChanges(quarantinedComponent);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertTrue("0 ProductStatusChange", changes.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindNumberOfDiscardedComponentsDaily() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> panels = new ArrayList<String>();
		panels.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB+");
		bloodGroups.add("AB-");
		Map<String, Map<Long, Long>> results = componentRepository.findNumberOfDiscardedComponents(donationDateFrom,
		    donationDateTo, "daily", panels, bloodGroups);
		Assert.assertEquals("2 blood types searched", 2, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("11/08/2015");
		Map<Long, Long> abPlusResults = results.get("AB+");
		Assert.assertEquals("3 days in the searched period", 3, abPlusResults.size());
		Long abPlus11thResults = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ discarded donations", new Long(1), abPlus11thResults);
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinus11thResults = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations", new Long(0), abMinus11thResults);
	}
	
	@Test
	@Transactional
	public void testFindNumberOfDiscardedComponentsMonthly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> panels = new ArrayList<String>();
		panels.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB+");
		bloodGroups.add("AB-");
		bloodGroups.add("O-");
		bloodGroups.add("O+");
		Map<String, Map<Long, Long>> results = componentRepository.findNumberOfDiscardedComponents(donationDateFrom,
		    donationDateTo, "monthly", panels, bloodGroups);
		Assert.assertEquals("4 blood types searched", 4, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/08/2015");
		Map<Long, Long> abPlusResults = results.get("AB+");
		Assert.assertEquals("1 month in the searched period", 1, abPlusResults.size());
		Long abPlusAugResults = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ discarded donations", new Long(1), abPlusAugResults);
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinusAugResults = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations", new Long(0), abMinusAugResults);
	}
	
	@Test
	@Transactional
	public void testFindNumberOfDiscardedComponentsYearly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2014-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> panels = new ArrayList<String>();
		panels.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB-");
		bloodGroups.add("AB+");
		Map<String, Map<Long, Long>> results = componentRepository.findNumberOfDiscardedComponents(donationDateFrom,
		    donationDateTo, "yearly", panels, bloodGroups);
		Assert.assertEquals("2 blood types searched", 2, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015");
		Map<Long, Long> abPlusResults = results.get("AB+");
		Assert.assertEquals("2 year in the searched period", 2, abPlusResults.size());
		Long abPlus2015Results = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ discarded donations", new Long(1), abPlus2015Results);
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinus2015Results = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations", new Long(0), abMinus2015Results);
	}
	
	@Test
	@Transactional
	// FIXME: issue with package dependencies here - ComponentRepository uses UtilController to retrieve the logged in user.  
	public void testDiscardComponent() throws Exception {
		ProductStatusChangeReason discardReason = productStatusChangeReasonRepository.getProductStatusChangeReasonById(5);
		componentRepository.discardComponent(1l, discardReason, "junit");
		Component component = componentRepository.findComponent(1l);
		List<ProductStatusChange> changes = componentRepository.getComponentStatusChanges(component);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ProductStatusChangeType", ProductStatusChangeType.DISCARDED, changes.get(0)
		        .getStatusChangeType());
	}
	
	@Test
	@Transactional
	// FIXME: issue with package dependencies here - ComponentRepository uses UtilController to retrieve the logged in user.  
	public void testReturnComponent() throws Exception {
		ProductStatusChangeReason returnReason = productStatusChangeReasonRepository.getProductStatusChangeReasonById(7);
		componentRepository.returnComponent(1l, returnReason, "junit");
		Component component = componentRepository.findComponent(1l);
		List<ProductStatusChange> changes = componentRepository.getComponentStatusChanges(component);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ProductStatusChangeType", ProductStatusChangeType.RETURNED, changes.get(0)
		        .getStatusChangeType());
	}
	
	@Test
	@Transactional
	// FIXME: issue with package dependencies here - ComponentRepository uses UtilController to retrieve the logged in user.  
	public void testSplitComponent() throws Exception {
		boolean split = componentRepository.splitComponent(2l, 2);
		Assert.assertTrue("Split was successful", split);
		Component component = componentRepository.findComponent(2l);
		Assert.assertEquals("Status is changed to SPLIT", ProductStatus.SPLIT, component.getStatus());
		List<ProductStatusChange> changes = componentRepository.getComponentStatusChanges(component);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ProductStatusChangeType", ProductStatusChangeType.SPLIT, changes.get(0)
		        .getStatusChangeType());
		List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber("1111111");
		// FIXME: I'm not sure that numComponentsAfterSplitting is correctly named - expected only 1 extra component to be created after the split
		Assert.assertEquals("4 components after split", 4, components.size());
	}
	
	@Test
	@Transactional
	public void testSplitComponentTwice() throws Exception {
		componentRepository.splitComponent(2l, 1);
		List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber("1111111");
		for (Component p : components) {
			if (p.getStatus() == ProductStatus.SPLIT) {
				boolean split = componentRepository.splitComponent(p.getId(), 1);
				Assert.assertFalse("Cannot split a component that has already been split", split);
			}
		}
	}
	
	@Test
	@Transactional
	public void testSplitComponentUnknown() throws Exception {
		boolean split = componentRepository.splitComponent(123l, 2);
		Assert.assertFalse("Unknown component", split);
	}
	
	@Test
	@Transactional
	public void testUpdateComponent() throws Exception {
		Component componentToUpdate = componentRepository.findComponent(2l);
		componentToUpdate.setComponentIdentificationNumber("junit123");
		componentRepository.updateComponent(componentToUpdate);
		Component updatedComponent = componentRepository.findComponent(2l);
		Assert.assertEquals("Component has been updated", "junit123", updatedComponent.getComponentIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testUpdateExpiryStatus() throws Exception {
		// create expirable component
		Component componentToExpire = componentRepository.findComponent(2l);
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_YEAR, -3);
		componentToExpire.setExpiresOn(today.getTime());
		componentToExpire.setStatus(ProductStatus.AVAILABLE);
		Component expiringComponent = componentRepository.updateComponent(componentToExpire);
		// run test
		componentRepository.updateExpiryStatus();
		// check component has been expired
		Component expiredComponent = componentRepository.findComponent(expiringComponent.getId());
		Assert.assertEquals("Component has been expired", ProductStatus.EXPIRED, expiredComponent.getStatus());
	}
	
	@Test
	@Transactional
	public void testSetProductStatusToProcessed() throws Exception {
		componentRepository.setProductStatusToProcessed(2l);
		Component processedComponent = componentRepository.findComponent(2l);
		Assert.assertEquals("Component has been processed", ProductStatus.PROCESSED, processedComponent.getStatus());
	}
	
	@Test
	@Transactional
	@Ignore("It seems impossible to get components in a NULL status - by default it is set to QUARANTINED")
	public void testUpdateQuarantineStatus() throws Exception {
		// create component with null status
		Component newComponent = new Component();
		Donation newDonation = new Donation();
		Component existingComponent = componentRepository.findComponent(1l);
		newComponent.setId(existingComponent.getId());
		newComponent.copy(existingComponent);
		newComponent.setId(null); // don't want to override the existing component
		newDonation.setId(existingComponent.getDonation().getId());
		newDonation.copy(existingComponent.getDonation());
		newDonation.setId(null); // don't want to override the existing donation
		newDonation.setDonationIdentificationNumber("7654321");
		Calendar today = Calendar.getInstance();
		newDonation.setCreatedDate(today.getTime());
		newDonation.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation);
		newComponent.setDonation(newDonation);
		newComponent.setStatus(null);
		Component savedComponent = componentRepository.addComponent(newComponent);
		savedComponent = componentRepository.findComponent(savedComponent.getId());
		Assert.assertNull("Cleared status", savedComponent.getStatus()); // check that status is null before test
		// run test
		componentRepository.updateQuarantineStatus();
		// assert that the test was successful
		Component processedComponent = componentRepository.findComponent(savedComponent.getId());
		Assert.assertEquals("Component has been QUARANTINED", ProductStatus.QUARANTINED, processedComponent.getStatus());
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsProcessed() throws Exception {
		Component component = componentRepository.findComponent(1l);
		boolean updatedComponent = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("PROCESSED component is not updated", updatedComponent);
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsDiscarded() throws Exception {
		Component component = componentRepository.findComponent(6l);
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("DISCARDED component is not updated", updatedProductStatus);
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsIssued() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(1l);
		component.setStatus(ProductStatus.ISSUED);
		// run test
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("ISSUED component is not updated", updatedProductStatus);
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsUsed() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(1l);
		component.setStatus(ProductStatus.USED);
		// run test
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("USED component is not updated", updatedProductStatus);
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsSplit() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(1l);
		component.setStatus(ProductStatus.SPLIT);
		// run test
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("SPLIT component is not updated", updatedProductStatus);
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsQuarantined() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(4l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -10);
		component.setExpiresOn(cal.getTime());
		// run test
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertTrue("QUARANTINED component status is changed", updatedProductStatus);
		Assert.assertEquals("Component status is actually EXPIRED", ProductStatus.EXPIRED, component.getStatus());
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsSafe() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(4l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 10);
		component.setExpiresOn(cal.getTime());
		// run test
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertTrue("SAFE component status is changed", updatedProductStatus);
		Assert.assertEquals("Component status is actually AVAILABLE", ProductStatus.AVAILABLE, component.getStatus());
	}
	
	@Test
	@Transactional
	public void testUpdateComponentInternalFieldsUnSafe() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(7l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 10);
		component.setExpiresOn(cal.getTime());
		// run test
		boolean updatedProductStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertTrue("UNSAFE component status is changed", updatedProductStatus);
		Assert.assertEquals("Component status is actually UNSAFE", ProductStatus.UNSAFE, component.getStatus());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - this won't work if there are foreign key references such as ProductStatusChange entries...")
	public void testDeleteAllComponents() throws Exception {
		componentRepository.deleteAllComponents();
		List<Component> all = componentRepository.getAllComponents();
		Assert.assertNotNull("Doesn't return null object", all);
		Assert.assertTrue("No components left", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testDeleteComponent() throws Exception {
		componentRepository.deleteComponent(1l);
		Component deletedComponent = componentRepository.findComponent(1l);
		Assert.assertNotNull("Component isn't actually deleted", deletedComponent);
		Assert.assertTrue("Component is marked as isDeleted", deletedComponent.getIsDeleted());
	}
	
	@Test
	@Transactional
	public void testAddComponent() throws Exception {
		Component newComponent = new Component();
		Donation newDonation = new Donation();
		Component existingComponent = componentRepository.findComponent(1l);
		newComponent.setId(existingComponent.getId());
		newComponent.copy(existingComponent);
		newComponent.setId(null); // don't want to override, just save time with a copy
		newDonation.setId(existingComponent.getDonation().getId());
		newDonation.copy(existingComponent.getDonation());
		newDonation.setId(null); // don't want to override, just save time with a copy
		newDonation.setDonationIdentificationNumber("7654321");
		Calendar today = Calendar.getInstance();
		newDonation.setCreatedDate(today.getTime());
		newDonation.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation);
		newComponent.setDonation(newDonation);
		componentRepository.addComponent(newComponent);
		List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber("7654321");
		Assert.assertEquals("A new component was added", 1, components.size());
	}
	
	@Test
	@Transactional
	public void testAddAllComponents() throws Exception {
		Component newComponent1 = new Component();
		Donation newDonation1 = new Donation();
		Component existingComponent1 = componentRepository.findComponent(1l);
		newComponent1.setId(existingComponent1.getId());
		newComponent1.copy(existingComponent1);
		newComponent1.setId(null); // don't want to overwrite the old component
		newDonation1.setId(existingComponent1.getDonation().getId());
		newDonation1.copy(existingComponent1.getDonation());
		newDonation1.setId(null); // don't want to overwrite the old donation
		newDonation1.setDonationIdentificationNumber("7654321");
		Calendar today = Calendar.getInstance();
		newDonation1.setCreatedDate(today.getTime());
		newDonation1.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation1.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation1);
		newComponent1.setDonation(newDonation1);
		
		Component newComponent2 = new Component();
		Donation newDonation2 = new Donation();
		Component existingComponent2 = componentRepository.findComponent(1l);
		newComponent2.setId(existingComponent2.getId());
		newComponent2.copy(existingComponent2);
		newComponent2.setId(null); // don't want to overwrite the old component
		newDonation2.setId(existingComponent2.getDonation().getId());
		newDonation2.copy(existingComponent2.getDonation());
		newDonation2.setId(null); // don't want to overwrite the old donation
		newDonation2.setDonationIdentificationNumber("7654320");
		newDonation2.setCreatedDate(today.getTime());
		newDonation2.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation2.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation2);
		newComponent2.setDonation(newDonation2);
		
		List<Component> newComponents = new ArrayList<Component>();
		newComponents.add(newComponent1);
		newComponents.add(newComponent2);
		componentRepository.addAllComponents(newComponents);
		
		List<Component> components1 = componentRepository.findComponentsByDonationIdentificationNumber("7654321");
		Assert.assertEquals("A new component was added", 1, components1.size());
		List<Component> components2 = componentRepository.findComponentsByDonationIdentificationNumber("7654320");
		Assert.assertEquals("A new component was added", 1, components2.size());
	}
}
