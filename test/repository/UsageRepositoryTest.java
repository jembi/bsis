package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.usage.ComponentUsage;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the UsageRepository
 */
public class UsageRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  UsageRepository usageRepository;

  @Autowired
  ComponentRepository componentRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/UsageRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testFindUsageById() throws Exception {
    ComponentUsage one = usageRepository.findUsageById(1l);
    Assert.assertNotNull("There is a ComponentUsage", one);
    Assert.assertEquals("ComponentUsage is correct", "Lucky", one.getPatientName());
  }

  @Test
  public void testFindComponentUsage() throws Exception {
    ComponentUsage usage = usageRepository.findComponentUsage("3333333-0011");
    Assert.assertNotNull("ComponentUsage found", usage);
    Assert.assertEquals("ComponentUsage correct", Long.valueOf(3), usage.getId());
  }

  @Test
  public void testFindUsageByComponentCode() throws Exception {
    ComponentUsage usage = usageRepository.findUsageByComponentCode("3333333-0011");
    Assert.assertNotNull("ComponentUsage found", usage);
    Assert.assertEquals("ComponentUsage correct", Long.valueOf(3), usage.getId());
  }

  @Test
  public void testFindUsageByIdUnknown() throws Exception {
    ComponentUsage one = usageRepository.findUsageById(123l);
    Assert.assertNull("There is no ComponentUsage", one);
  }

  @Test
  public void testUpdateUsage() throws Exception {
    ComponentUsage one = usageRepository.findUsageById(1l);
    one.setHospital("Junit hospital");
    usageRepository.saveUsage(one);
    ComponentUsage savedOne = usageRepository.findUsageById(1l);
    Assert.assertEquals("ComponentUsage is updated", "Junit hospital", savedOne.getHospital());
  }

  @Test
  public void testAddUsage() throws Exception {
    ComponentUsage one = new ComponentUsage();
    one.setHospital("Junit hospital");
    one.setComponent(componentRepository.findComponent(6l)); // note: this component is actually discarded
    one.setUsageDate(new Date());
    ComponentUsage savedOne = usageRepository.addUsage(one);
    Assert.assertNotNull("Saved ComponentUsage has an id", savedOne.getId());
    ComponentUsage retrievedOne = usageRepository.findUsageById(savedOne.getId());
    Assert.assertNotNull("There is a ComponentUsage", retrievedOne);
    Assert.assertEquals("ComponentUsage is updated", "Junit hospital", retrievedOne.getHospital());
  }

  @Test
  public void testDeleteUsage() throws Exception {
    usageRepository.deleteUsage("3333333-0011");
    ComponentUsage one = usageRepository.findUsageById(3l);
    Assert.assertNotNull("There is a ComponentUsage", one);
    Assert.assertTrue("ComponentUsage is deleted", one.getIsDeleted());
  }

  @Test
  public void testDeleteAllUsages() throws Exception {
    usageRepository.deleteAllUsages();
    ComponentUsage one = usageRepository.findUsageById(1l);
    Assert.assertNull("There is no ComponentUsage", one);
  }

  @Test
  @Ignore("Bug - error in HSQL: dateUsed of: model.usage.ComponentUsage [SELECT u FROM model.usage.ComponentUsage u WHERE (u.component.componentCode = :componentCode OR u.useIndication IN (:useIndications)) AND (u.dateUsed BETWEEN :dateUsedFrom AND :dateUsedTo) AND (u.isDeleted= :isDeleted)]")
  public void testFindAnyUsageMatching() throws Exception {
    List<String> usages = new ArrayList<String>();
    usages.add("Operation");
    usages.add("Emergency");
    usageRepository.findAnyUsageMatching("3333333-0011", "2015-01-01", "2015-10-10", usages);
  }
}
