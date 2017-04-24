package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the PackTypeRepository
 */
public class PackTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  private PackTypeRepository packTypeRepository;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/PackTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAllPackTypes() throws Exception {
    List<PackType> all = packTypeRepository.getAllPackTypes();
    Assert.assertNotNull("There are pack types defined", all);
    Assert.assertEquals("There are 9 pack types defined", 9, all.size());
  }

  @Test
  public void testGetAllEnabledPackTypes() throws Exception {
    List<PackType> all = packTypeRepository.getAllEnabledPackTypes();
    Assert.assertNotNull("There are pack types enabled", all);
    Assert.assertEquals("There are 8 pack types enabled", 8, all.size());
  }

  @Test
  public void testFindPackTypeByName() throws Exception {
    PackType one = packTypeRepository.findPackTypeByName("Single");
    Assert.assertNotNull("There is a pack type named 'Single'", one);
    Assert.assertEquals("There is a pack type named 'Single'", "Single", one.getPackType());
  }

  @Test
  public void testUpdatePackType() throws Exception {
    PackType two = packTypeRepository.findPackTypeByName("Double");
    Assert.assertNotNull("There is a pack types named 'Double'", two);
    two.setCountAsDonation(false);
    two.setPeriodBetweenDonations(0);
    packTypeRepository.updatePackType(two);
    PackType savedTwo = packTypeRepository.findPackTypeByName("Double");
    Assert.assertNotNull("There is a pack type named 'Double'", savedTwo);
    Assert.assertEquals("Is not counted as a donation", false, savedTwo.getCountAsDonation());
    Assert.assertEquals("0 days between donations", new Integer(0), savedTwo.getPeriodBetweenDonations());
  }

  @Test
  public void testSavePackType() throws Exception {
    ComponentType componentType = componentTypeRepository.getComponentTypeById(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    PackType toBeSaved = new PackType();
    toBeSaved.setPackType("Junit");
    toBeSaved.setPeriodBetweenDonations(123);
    toBeSaved.setCountAsDonation(true);
    toBeSaved.setComponentType(componentType);

    packTypeRepository.savePackType(toBeSaved);

    PackType saved = packTypeRepository.findPackTypeByName("Junit");
    Assert.assertNotNull("There is a pack type named 'Double'", saved);
    Assert.assertEquals("Is counted as a donation", true, saved.getCountAsDonation());
    Assert.assertEquals("123 days between donations", new Integer(123), saved.getPeriodBetweenDonations());
  }
}
