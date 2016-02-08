package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.bloodtesting.WellType;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the WellTypeRepository
 */
public class WellTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  WellTypeRepository wellTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/WellTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<WellType> all = wellTypeRepository.getAllWellTypes();
    Assert.assertNotNull("There are WellTypes defined", all);
    Assert.assertEquals("There are 4 WellTypes defined", 4, all.size());
  }

  @Test
  public void testIsWellTypeValid() throws Exception {
    boolean valid = wellTypeRepository.isWellTypeValid("Sample");
    Assert.assertTrue("There is a matching WellType", valid);
  }

  @Test
  public void testIsWellTypeValidFalse() throws Exception {
    boolean valid = wellTypeRepository.isWellTypeValid("Junit");
    Assert.assertFalse("There is no matching WellType", valid);
  }

  @Test
  public void testGetWellTypeById() throws Exception {
    WellType one = wellTypeRepository.getWellTypeById(1l);
    Assert.assertNotNull("There is a WellType defined", one);
    Assert.assertEquals("WellType is correct", "Sample", one.getWellType());
  }

  @Test
  public void testGetWellTypeByIdUnknown() throws Exception {
    WellType one = wellTypeRepository.getWellTypeById(123l);
    Assert.assertNull("There is no WellType defined", one);
  }

  @Test
  public void testSaveAllWellTypesUpdate() throws Exception {
    WellType oldOne = wellTypeRepository.getWellTypeById(1l);
    oldOne.setWellType("oldSample");
    List<WellType> all = new ArrayList<WellType>();
    all.add(oldOne);
    wellTypeRepository.saveAllWellTypes(all);
    WellType savedOldOne = wellTypeRepository.getWellTypeById(1l);
    Assert.assertEquals("WellType is updated", "oldSample", savedOldOne.getWellType());
  }

  @Test
  public void testSaveAllWellTypesAdd() throws Exception {
    WellType newOne = new WellType();
    newOne.setRequiresSample(true);
    newOne.setWellType("Junit");
    newOne.setIsDeleted(false);
    List<WellType> all = new ArrayList<WellType>();
    all.add(newOne);
    wellTypeRepository.saveAllWellTypes(all);
    List<WellType> allSaved = wellTypeRepository.getAllWellTypes();
    Assert.assertEquals("There are 5 WellTypes defined", 5, allSaved.size());
  }
}
