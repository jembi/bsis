package org.jembi.bsis.repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
    File file = new File("src/test/resources/dataset/ComponentRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testFindComponentWithId() throws Exception {
    Component one = componentRepository.findComponent(1l);
    Assert.assertNotNull("There is a Component with id 1", one);
    Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
    Assert.assertNotNull("There is a ComponentBatch", one.getComponentBatch());
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
    List<Component> one = componentRepository.findComponentsByDINAndType("1111111", 1);
    Assert.assertNotNull("There is a Component with DIN 1111111", one.get(0));
    Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.get(0).getDonationIdentificationNumber());
  }

  @Test
  public void testFindComponentByDINAndComponentTypeIdUnknown() throws Exception {
    List<Component> one = componentRepository.findComponentsByDINAndType("1111112", 1);
    Assert.assertEquals("There is no a Component with DIN 1111112", 0, one.size());
  }

  @Test
  public void testFindAnyComponentDIN() throws Exception {
    List<Component> all = componentRepository.findAnyComponent("1111111", null, null, null, null);
    Assert.assertNotNull("There is a Component with DIN 1111111", all);
    Assert.assertFalse("There is a Component with DIN 1111111", all.isEmpty());
    Assert.assertEquals("There should be 2 components", 2, all.size());
  }

  @Test
  public void testFindAnyComponentDINAndStatus() throws Exception {
    List<ComponentStatus> status = new ArrayList<ComponentStatus>();
    status.add(ComponentStatus.QUARANTINED);
    List<Component> all = componentRepository.findAnyComponent("1111111", null, status, null, null);
    Assert.assertNotNull("There are matching components", all);
    Assert.assertEquals("There should be 1 components", 1, all.size());
  }

  @Test
  public void testFindAnyComponentQuarantinedType1() throws Exception {
    List<ComponentStatus> status = new ArrayList<ComponentStatus>();
    status.add(ComponentStatus.QUARANTINED);
    List<Long> componentTypeIds = new ArrayList<Long>();
    componentTypeIds.add(1l);
    List<Component> all = componentRepository.findAnyComponent(null, componentTypeIds, status, null, null);
    Assert.assertNotNull("There aren't matching components", all);
    Assert.assertTrue("There should be 0 components", all.isEmpty());
  }

  @Test
  public void testFindAnyComponentBetweenDates() throws Exception {
    Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
    Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
    List<Component> all = componentRepository.findAnyComponent(null, null, null, start, end);
    Assert.assertNotNull("There are matching components", all);
    Assert.assertEquals("There should be 7 components", 7, all.size());
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
  public void testUpdateComponent() throws Exception {
    Component componentToUpdate = componentRepository.findComponent(2l);
    componentToUpdate.setComponentCode("junit123");
    componentRepository.updateComponent(componentToUpdate);
    Component updatedComponent = componentRepository.findComponent(2l);
    Assert.assertEquals("Component has been updated", "junit123", updatedComponent.getComponentCode());
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
}
