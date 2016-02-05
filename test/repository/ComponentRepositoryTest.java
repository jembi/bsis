package repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.component.Component;
import model.component.ComponentStatus;
import model.componentmovement.ComponentStatusChange;
import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeType;
import model.componenttype.ComponentType;
import model.donation.Donation;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the ComponentRepository
 */
public class ComponentRepositoryTest extends DBUnitContextDependentTestSuite {
	
	@Autowired
	ComponentRepository componentRepository;
	
	@Autowired
	DonationRepository donationRepository;
	
	@Autowired
	ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
	
	@Override
	protected IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ComponentRepositoryDataset.xml");
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
	}
	
	// TODO: test the following methods
	//findMatchingComponentsForRequest
	//findNumberOfIssuedComponents
	//addComponentCombination
	
	@Test
	public void testGetAllComponents() throws Exception {
		List<Component> all = componentRepository.getAllComponents();
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 7 Components", 7, all.size());
	}
	
	@Test
	public void testGetComponents() throws Exception {
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<Component> all = componentRepository.getComponents(fromDate, toDate);
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 7 Components", 7, all.size());
	}
	
	@Test
	public void testGetComponentsFromComponentIds() throws Exception {
		String[] componentIds = new String[] { "1", "2", "4" };
		List<Component> all = componentRepository.getComponentsFromComponentIds(componentIds);
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 3 Components", 3, all.size());
	}
	
	@Test
	@Ignore("Bug - problem in HSQL: could not resolve property: donationIdentificationNumber of: model.component.Component [SELECT c FROM model.component.Component c WHERE c.donationIdentificationNumber = :donationIdentificationNumber and c.isDeleted = :isDeleted]")
	public void isComponentCreatedFalse() throws Exception {
		boolean created = componentRepository.isComponentCreated("4444444");
		Assert.assertFalse("Component not created", created);
	}
	
	@Test
	@Ignore("Bug - problem in HSQL: could not resolve property: donationIdentificationNumber of: model.component.Component [SELECT c FROM model.component.Component c WHERE c.donationIdentificationNumber = :donationIdentificationNumber and c.isDeleted = :isDeleted]")
	public void isComponentCreatedTrue() throws Exception {
		boolean created = componentRepository.isComponentCreated("3333333");
		Assert.assertTrue("Component created", created);
	}
	
	@Test
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.component.Component [SELECT c FROM model.component.Component c where c.isDeleted = :isDeleted and c.isIssued= :isIssued]")
	public void testGetAllUnissuedComponents() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedComponents();
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 3 Components", 3, all.size());
	}
	
	@Test
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.component.Component [SELECT c FROM model.component.Component c where c.isDeleted = :isDeleted and c.isIssued= :isIssued]")
	public void testGetAllUnissuedComponentsAboRhd() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedComponents("PROCESSED", "A", "+");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.component.Component [SELECT p FROM model.component.Component p where p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate]")
	public void testGetAllUnissuedThirtyFiveDayComponents() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedThirtyFiveDayComponents();
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Ignore("Bug - error in HQL: could not resolve property: type of: model.component.Component [SELECT p FROM model.component.Component p where p.type = :componentType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate]")
	public void testGetAllUnissuedThirtyFiveDayComponentsWithParameters() throws Exception {
		List<Component> all = componentRepository.getAllUnissuedThirtyFiveDayComponents("PROCESSED", "A", "+");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	@Ignore("There appears to be a bug in the HQL: could not resolve property: type of: model.component.Component")
	public void testGetAllComponentsWithComponentTypeQUARANTINED() throws Exception {
		List<Component> all = componentRepository.getAllComponents("QUARANTINED");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 3 Components", 3, all.size());
	}
	
	@Test
	@Ignore("There appears to be a bug in the HQL: could not resolve property: type of: model.component.Component")
	public void testGetAllComponentsWithComponentTypeEXPIRED() throws Exception {
		List<Component> all = componentRepository.getAllComponents("EXPIRED");
		Assert.assertNotNull("There are Components", all);
		Assert.assertEquals("There are 0 Components", 0, all.size());
	}
	
	@Test
	public void testFindComponentWithId() throws Exception {
		Component one = componentRepository.findComponent(1l);
		Assert.assertNotNull("There is a Component with id 1", one);
		Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
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
	public void testFindComponentByComponentNumber() throws Exception {
		Component component = componentRepository.findComponent("5555555-0011");
		Assert.assertNotNull("Found Component by componentIdentificationNumber", component);
		Assert.assertEquals("Found correct Component", Long.valueOf(7), component.getId());
	}
	
	@Test
	public void testFindComponentsByDIN() throws Exception {
		List<Component> all = componentRepository.findComponentsByDonationIdentificationNumber("1111111");
		Assert.assertNotNull("There are Components with DIN 111111", all);
		Assert.assertFalse("There are Components with DIN 111111", all.isEmpty());
		Assert.assertEquals("There are 2 Components with DIN 1111111", 2, all.size());
	}
	
	@Test
	public void testFindComponentsByDINUnknown() throws Exception {
		List<Component> all = componentRepository.findComponentsByDonationIdentificationNumber("1111112");
		Assert.assertNotNull("Does not return an null", all);
		Assert.assertTrue("There are 0 Components with DIN 111112", all.isEmpty());
	}
	
	@Test
	public void testFindComponentByDINAndComponentTypeId() throws Exception {
		Component one = componentRepository.findComponent("1111111", "1");
		Assert.assertNotNull("There is a Component with DIN 1111111", one);
		Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	public void testFindComponentByDINAndComponentTypeIdUnknown() throws Exception {
		Component one = componentRepository.findComponent("1111112", "1");
		Assert.assertNull("There is no a Component with DIN 1111112", one);
	}
	
	@Test
	public void testFindComponentByComponentTypes() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.PROCESSED);
		List<Long> componentTypeIds = new ArrayList<Long>();
		componentTypeIds.add(1l);
		List<Component> all = componentRepository.findComponentByComponentTypes(componentTypeIds, status, pagingParams);
		Assert.assertNotNull("Does not return an null", all);
		Assert.assertEquals("There are 3 'Whole Blood Single Pack - CPDA' Components", 3, all.size());
	}
	
	@Test
	public void testFindComponentByComponentTypesNone() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.QUARANTINED);
		List<Long> componentTypeIds = new ArrayList<Long>();
		componentTypeIds.add(1l);
		List<Component> all = componentRepository.findComponentByComponentTypes(componentTypeIds, status, pagingParams);
		Assert.assertNotNull("Does not return a null list", all);
		Assert.assertTrue("There are no Components", all.isEmpty());
	}
	
	@Test
	public void testFindComponentByDINAndStatus() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.QUARANTINED);
		status.add(ComponentStatus.PROCESSED);
		List<Component> all = componentRepository.findComponentByDonationIdentificationNumber("1111111", status, pagingParams);
		Assert.assertNotNull("There is a Component with DIN 1111111", all);
		Assert.assertFalse("There is a Component with DIN 1111111", all.isEmpty());
		Assert.assertEquals("There should be two components", 2, all.size());
	}
	
	@Test
	public void testFindComponentByDINAndStatusNone() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.EXPIRED);
		List<Component> all = componentRepository.findComponentByDonationIdentificationNumber("1111111", status, pagingParams);
		Assert.assertTrue("There should be 0 components", all.isEmpty());
	}
	
	@Test
	public void testFindComponentByDINAndStatusUnknown() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.QUARANTINED);
		status.add(ComponentStatus.PROCESSED);
		List<Component> all = componentRepository.findComponentByDonationIdentificationNumber("1111112", status, pagingParams);
		Assert.assertTrue("There should be 0 components", all.isEmpty());
	}
	
	@Test
	public void testFindAnyComponentDIN() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<Component> all = componentRepository.findAnyComponent("1111111", null, null, null, null, pagingParams);
		Assert.assertNotNull("There is a Component with DIN 1111111", all);
		Assert.assertFalse("There is a Component with DIN 1111111", all.isEmpty());
		Assert.assertEquals("There should be 2 components", 2, all.size());
	}
	
	@Test
	public void testFindAnyComponentDINAndStatus() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.QUARANTINED);
		List<Component> all = componentRepository.findAnyComponent("1111111", null, status, null, null, pagingParams);
		Assert.assertNotNull("There are matching components", all);
		Assert.assertEquals("There should be 1 components", 1, all.size());
	}
	
	@Test
	public void testFindAnyComponentQuarantinedType1() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ComponentStatus> status = new ArrayList<ComponentStatus>();
		status.add(ComponentStatus.QUARANTINED);
		List<Long> componentTypeIds = new ArrayList<Long>();
		componentTypeIds.add(1l);
		List<Component> all = componentRepository.findAnyComponent(null, componentTypeIds, status, null, null, pagingParams);
		Assert.assertNotNull("There aren't matching components", all);
		Assert.assertTrue("There should be 0 components", all.isEmpty());
	}
	
	@Test
	public void testFindAnyComponentBetweenDates() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<Component> all = componentRepository.findAnyComponent(null, null, null, start, end, pagingParams);
		Assert.assertNotNull("There are matching components", all);
		Assert.assertEquals("There should be 7 components", 7, all.size());
	}
	
	@Test
	@Ignore("Bug - error in the HQL:  org.hibernate.QueryException: could not resolve property: componentType of: model.componenttype.ComponentType [SELECT p FROM model.componenttype.ComponentType p where p.componentType = :componentTypeName]")
	public void testFindComponentTypeByComponentTypeName() throws Exception {
		ComponentType one = componentRepository.findComponentTypeByComponentTypeName("Whole Blood Single Pack - CPDA");
		Assert.assertNotNull("ComponentType match", one);
		Assert.assertEquals("ComponentType is correct", "0011", one.getComponentTypeNameShort());
	}
	
	@Test
	public void testFindComponentTypeByComponentTypeId() throws Exception {
		ComponentType one = componentRepository.findComponentTypeBySelectedComponentType(1l);
		Assert.assertNotNull("ComponentType match", one);
		Assert.assertEquals("ComponentType is correct", "Whole Blood Single Pack - CPDA", one.getComponentTypeName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	@Transactional
	public void testFindComponentTypeByComponentTypeIdUnknown() throws Exception {
		componentRepository.findComponentTypeBySelectedComponentType(123l);
	}
	
	@Test
	public void testGetComponentStatusChanges() throws Exception {
		Component discardedComponent = componentRepository.findComponent(6l);
		List<ComponentStatusChange> changes = componentRepository.getComponentStatusChanges(discardedComponent);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ComponentStatusChange", 1, changes.size());
		Assert.assertEquals("Component was DISCARDED", "DISCARDED", changes.get(0).getNewStatus().toString());
	}
	
	@Test
	public void testGetComponentStatusChangesNone() throws Exception {
		Component quarantinedComponent = componentRepository.findComponent(4l);
		List<ComponentStatusChange> changes = componentRepository.getComponentStatusChanges(quarantinedComponent);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertTrue("0 ComponentStatusChange", changes.isEmpty());
	}
	
	@Test
	public void testFindNumberOfDiscardedComponentsDaily() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> venues = new ArrayList<String>();
		venues.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB+");
		bloodGroups.add("AB-");
		Map<String, Map<Long, Long>> results = componentRepository.findNumberOfDiscardedComponents(donationDateFrom,
		    donationDateTo, "daily", venues, bloodGroups);
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
	public void testFindNumberOfDiscardedComponentsMonthly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> venues = new ArrayList<String>();
		venues.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB+");
		bloodGroups.add("AB-");
		bloodGroups.add("O-");
		bloodGroups.add("O+");
		Map<String, Map<Long, Long>> results = componentRepository.findNumberOfDiscardedComponents(donationDateFrom,
		    donationDateTo, "monthly", venues, bloodGroups);
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
	public void testFindNumberOfDiscardedComponentsYearly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2014-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> venues = new ArrayList<String>();
		venues.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB-");
		bloodGroups.add("AB+");
		Map<String, Map<Long, Long>> results = componentRepository.findNumberOfDiscardedComponents(donationDateFrom,
		    donationDateTo, "yearly", venues, bloodGroups);
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
	// FIXME: issue with package dependencies here - ComponentRepository uses UtilController to retrieve the logged in user.  
	public void testDiscardComponent() throws Exception {
		ComponentStatusChangeReason discardReason = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(5l);
		componentRepository.discardComponent(1l, discardReason, "junit");
		Component component = componentRepository.findComponent(1l);
		List<ComponentStatusChange> changes = componentRepository.getComponentStatusChanges(component);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ComponentStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ComponentStatusChangeType", ComponentStatusChangeType.DISCARDED, changes.get(0)
		        .getStatusChangeType());
	}
	
	@Test
	// FIXME: issue with package dependencies here - ComponentRepository uses UtilController to retrieve the logged in user.  
	public void testReturnComponent() throws Exception {
		ComponentStatusChangeReason returnReason = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(7l);
		componentRepository.returnComponent(1l, returnReason, "junit");
		Component component = componentRepository.findComponent(1l);
		List<ComponentStatusChange> changes = componentRepository.getComponentStatusChanges(component);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ComponentStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ComponentStatusChangeType", ComponentStatusChangeType.RETURNED, changes.get(0)
		        .getStatusChangeType());
	}
	
	@Test
	// FIXME: issue with package dependencies here - ComponentRepository uses UtilController to retrieve the logged in user.  
	public void testSplitComponent() throws Exception {
		boolean split = componentRepository.splitComponent(2l, 2);
		Assert.assertTrue("Split was successful", split);
		Component component = componentRepository.findComponent(2l);
		Assert.assertEquals("Status is changed to SPLIT", ComponentStatus.SPLIT, component.getStatus());
		List<ComponentStatusChange> changes = componentRepository.getComponentStatusChanges(component);
		Assert.assertEquals("1 ComponentStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ComponentStatusChangeType", ComponentStatusChangeType.SPLIT, changes.get(0)
		        .getStatusChangeType());
		List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber("1111111");
		// FIXME: I'm not sure that numComponentsAfterSplitting is correctly named - expected only 1 extra component to be created after the split
		Assert.assertEquals("4 components after split", 4, components.size());
	}
	
	@Test
	public void testSplitComponentTwice() throws Exception {
		componentRepository.splitComponent(2l, 1);
		List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber("1111111");
		for (Component p : components) {
			if (p.getStatus() == ComponentStatus.SPLIT) {
				boolean split = componentRepository.splitComponent(p.getId(), 1);
				Assert.assertFalse("Cannot split a component that has already been split", split);
			}
		}
	}
	
	@Test
	public void testSplitComponentUnknown() throws Exception {
		boolean split = componentRepository.splitComponent(123l, 2);
		Assert.assertFalse("Unknown component", split);
	}
	
	@Test
	public void testUpdateComponent() throws Exception {
		Component componentToUpdate = componentRepository.findComponent(2l);
		componentToUpdate.setComponentIdentificationNumber("junit123");
		componentRepository.updateComponent(componentToUpdate);
		Component updatedComponent = componentRepository.findComponent(2l);
		Assert.assertEquals("Component has been updated", "junit123", updatedComponent.getComponentIdentificationNumber());
	}
	
	@Test
	public void testUpdateExpiryStatus() throws Exception {
		// create expirable component
		Component componentToExpire = componentRepository.findComponent(2l);
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_YEAR, -3);
		componentToExpire.setExpiresOn(today.getTime());
		componentToExpire.setStatus(ComponentStatus.AVAILABLE);
		Component expiringComponent = componentRepository.updateComponent(componentToExpire);
		// run test
		componentRepository.updateExpiryStatus();
		// check component has been expired
		Component expiredComponent = componentRepository.findComponent(expiringComponent.getId());
		Assert.assertEquals("Component has been expired", ComponentStatus.EXPIRED, expiredComponent.getStatus());
	}
	
	@Test
	public void testSetComponentStatusToProcessed() throws Exception {
		componentRepository.setComponentStatusToProcessed(2l);
		Component processedComponent = componentRepository.findComponent(2l);
		Assert.assertEquals("Component has been processed", ComponentStatus.PROCESSED, processedComponent.getStatus());
	}
	
	@Test
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
		Assert.assertEquals("Component has been QUARANTINED", ComponentStatus.QUARANTINED, processedComponent.getStatus());
	}
	
	@Test
	public void testUpdateComponentInternalFieldsProcessed() throws Exception {
		Component component = componentRepository.findComponent(1l);
		boolean updatedComponent = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("PROCESSED component is not updated", updatedComponent);
	}
	
	@Test
	public void testUpdateComponentInternalFieldsDiscarded() throws Exception {
		Component component = componentRepository.findComponent(6l);
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("DISCARDED component is not updated", updatedComponentStatus);
	}
	
	@Test
	public void testUpdateComponentInternalFieldsIssued() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(1l);
		component.setStatus(ComponentStatus.ISSUED);
		// run test
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("ISSUED component is not updated", updatedComponentStatus);
	}
	
	@Test
	public void testUpdateComponentInternalFieldsUsed() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(1l);
		component.setStatus(ComponentStatus.USED);
		// run test
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("USED component is not updated", updatedComponentStatus);
	}
	
	@Test
	public void testUpdateComponentInternalFieldsSplit() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(1l);
		component.setStatus(ComponentStatus.SPLIT);
		// run test
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertFalse("SPLIT component is not updated", updatedComponentStatus);
	}
	
	@Test
	public void testUpdateComponentInternalFieldsQuarantined() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(4l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -10);
		component.setExpiresOn(cal.getTime());
		// run test
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertTrue("QUARANTINED component status is changed", updatedComponentStatus);
		Assert.assertEquals("Component status is actually EXPIRED", ComponentStatus.EXPIRED, component.getStatus());
	}
	
	@Test
	public void testUpdateComponentInternalFieldsSafe() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(4l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 10);
		component.setExpiresOn(cal.getTime());
		// run test
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertTrue("SAFE component status is changed", updatedComponentStatus);
		Assert.assertEquals("Component status is actually AVAILABLE", ComponentStatus.AVAILABLE, component.getStatus());
	}
	
	@Test
	public void testUpdateComponentInternalFieldsUnSafe() throws Exception {
		// setup test
		Component component = componentRepository.findComponent(7l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 10);
		component.setExpiresOn(cal.getTime());
		// run test
		boolean updatedComponentStatus = componentRepository.updateComponentInternalFields(component);
		Assert.assertTrue("UNSAFE component status is changed", updatedComponentStatus);
		Assert.assertEquals("Component status is actually UNSAFE", ComponentStatus.UNSAFE, component.getStatus());
	}
	
	@Test
	@Ignore("Bug - this won't work if there are foreign key references such as ComponentStatusChange entries...")
	public void testDeleteAllComponents() throws Exception {
		componentRepository.deleteAllComponents();
		List<Component> all = componentRepository.getAllComponents();
		Assert.assertNotNull("Doesn't return null object", all);
		Assert.assertTrue("No components left", all.isEmpty());
	}
	
	@Test
	public void testDeleteComponent() throws Exception {
		componentRepository.deleteComponent(1l);
		Component deletedComponent = componentRepository.findComponent(1l);
		Assert.assertNotNull("Component isn't actually deleted", deletedComponent);
		Assert.assertTrue("Component is marked as isDeleted", deletedComponent.getIsDeleted());
	}
	
	@Test
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
