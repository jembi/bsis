package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.bloodtesting.BloodTestContext;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the GenericConfigRepository
 */
public class GenericConfigRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  GenericConfigRepository genericConfigRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/GenericConfigRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetConfigProperties() throws Exception {
    Map<String, String> all = genericConfigRepository.getConfigProperties("labsetup");
    Assert.assertNotNull("There are GenericConfigs", all);
    Assert.assertEquals("There are 22 labsetup GenericConfig", 22, all.size());
    Assert.assertTrue("The labsetup GenericConfig contains crossmatchProcedure",
        all.keySet().contains("crossmatchProcedure"));
  }

  @Test
  public void testFindGenericConfigById() throws Exception {
    List<String> propertyOwners = new ArrayList<String>();
    propertyOwners.add("donationRequirements");
    propertyOwners.add("componentReleaseRequirements");
    Map<String, String> all = genericConfigRepository.getConfigProperties(propertyOwners);
    Assert.assertNotNull("There are GenericConfigs", all);
    Assert.assertEquals("There are 10 GenericConfig", 10, all.size());
    Assert.assertTrue("The GenericConfig contains donorRecordRequired", all.keySet().contains("donorRecordRequired"));
    Assert.assertTrue("The GenericConfig contains daysBetweenConsecutiveDonations",
        all.keySet().contains("daysBetweenConsecutiveDonations"));
  }

  @Test
  public void testFindGenericConfigByIdUnknown() throws Exception {
    Map<String, String> all = genericConfigRepository.getConfigProperties("junit");
    Assert.assertNotNull("Does not return a null list", all);
    Assert.assertTrue("It is an empty list", all.isEmpty());
  }

  @Test
  public void testGetCurrentBloodTypingContext() throws Exception {
    BloodTestContext context = genericConfigRepository.getCurrentBloodTypingContext();
    Assert.assertNotNull("BloodTestContext was found", context);
    Assert.assertEquals(BloodTestContext.RECORD_BLOOD_TYPING_TESTS, context);
  }

  @Test
  public void testUpdate() throws Exception {
    Map<String, String> all = genericConfigRepository.getConfigProperties("labsetup");
    Assert.assertNotNull("There are GenericConfigs", all);
    String value = all.get("recordUsage");
    value = "false";
    all.put("recordUsage", value);
    genericConfigRepository.updateConfigProperties("labsetup", all);
    Map<String, String> allSaved = genericConfigRepository.getConfigProperties("labsetup");
    String updatedValue = allSaved.get("recordUsage");
    Assert.assertEquals("recordUsage is false", "false", updatedValue);
  }

  @Test
  public void testUpdateWorksheetProperties() throws Exception {
    Map<String, String> all = genericConfigRepository.getConfigProperties("donationsWorksheet");
    Assert.assertNotNull("There are GenericConfigs", all);
    String value = all.get("rowHeight");
    value = "40";
    all.put("rowHeight", value);
    genericConfigRepository.updateWorksheetProperties(all);
    Map<String, String> allSaved = genericConfigRepository.getConfigProperties("donationsWorksheet");
    String updatedValue = allSaved.get("rowHeight");
    Assert.assertEquals("recordUsage is 40", "40", updatedValue);
  }
}
