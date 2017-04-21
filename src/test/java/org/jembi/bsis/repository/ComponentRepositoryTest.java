package org.jembi.bsis.repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
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
    UUID componentId = UUID.fromString("11e71f4f-8efe-921b-9fc7-28f10e1b4901");
    Component one = componentRepository.findComponent(componentId);
    Assert.assertNotNull("There is a Component with id 1", one);
    Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
    Assert.assertNotNull("There is a ComponentBatch", one.getComponentBatch());
  }

  @Test
  public void testFindComponentById() throws Exception {
    UUID componentId = UUID.fromString("11e71f4f-8efe-921b-9fc7-28f10e1b4901");
    Component one = componentRepository.findComponentById(componentId);
    Assert.assertNotNull("There is a Component with id 1", one);
    Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
  }

  @Test
  @Transactional
  public void testFindComponentByIdUnknown() throws Exception {
    Component one = componentRepository.findComponent(UUID.randomUUID());
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
    List<Component> one = componentRepository.findComponentsByDINAndType("1111111",
        UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    Assert.assertNotNull("There is a Component with DIN 1111111", one.get(0));
    Assert.assertEquals("Component is linked to the correct Donation", "1111111", one.get(0).getDonationIdentificationNumber());
  }

  @Test
  public void testFindComponentByDINAndComponentTypeIdUnknown() throws Exception {
    List<Component> one = componentRepository.findComponentsByDINAndType("1111112",
        UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    Assert.assertEquals("There is no a Component with DIN 1111112", 0, one.size());
  }

  @Test
  public void testFindAnyComponentQuarantinedType1() throws Exception {
    ComponentStatus status = ComponentStatus.QUARANTINED;
    List<UUID> componentTypeIds = new ArrayList<UUID>();
    componentTypeIds.add(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    List<Component> all = componentRepository.findAnyComponent(componentTypeIds, status, null, null, null);
    Assert.assertNotNull("There aren't matching components", all);
    Assert.assertTrue("There should be 0 components", all.isEmpty());
  }

  @Test
  public void testFindAnyComponentBetweenDates() throws Exception {
    Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
    Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
    List<Component> all = componentRepository.findAnyComponent(null, null, start, end, null);
    Assert.assertNotNull("There are matching components", all);
    Assert.assertEquals("There should be 7 components", 7, all.size());
  }

  @Test
  public void testUpdateComponent() throws Exception {
    UUID componentId = UUID.fromString("11e71f4f-8efe-921b-9fc7-28f10e1b4902");
    Component componentToUpdate = componentRepository.findComponent(componentId);
    componentToUpdate.setComponentCode("junit123");
    componentRepository.update(componentToUpdate);
    Component updatedComponent = componentRepository.findComponent(componentId);
    Assert.assertEquals("Component has been updated", "junit123", updatedComponent.getComponentCode());
  }
}
