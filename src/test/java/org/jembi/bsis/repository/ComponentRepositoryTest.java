package org.jembi.bsis.repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
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
  public void testUpdateComponent() throws Exception {
    Component componentToUpdate = componentRepository.findComponent(2l);
    componentToUpdate.setComponentCode("junit123");
    componentRepository.update(componentToUpdate);
    Component updatedComponent = componentRepository.findComponent(2l);
    Assert.assertEquals("Component has been updated", "junit123", updatedComponent.getComponentCode());
  }

  @Test
  public void testAddComponent() throws Exception {
    Component newComponent = new Component();
    Donation newDonation = new Donation();
    Component existingComponent = componentRepository.findComponent(1l);
    newComponent.setId(existingComponent.getId());
    newComponent.copy(existingComponent);
    newComponent.setStatus(ComponentStatus.QUARANTINED);
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
    componentRepository.save(newComponent);
    List<Component> components = componentRepository.findComponentsByDonationIdentificationNumber("7654321");
    Assert.assertEquals("A new component was added", 1, components.size());
  }
}
