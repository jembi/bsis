package repository;

import java.io.File;
import java.util.List;

import model.compatibility.CrossmatchType;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the CrossmatchType Repository
 */
public class CrossmatchTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  CrossmatchTypeRepository crossmatchTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/CrossmatchTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAllCrossmatchTypes() throws Exception {
    List<CrossmatchType> all = crossmatchTypeRepository.getAllCrossmatchTypes();
    Assert.assertNotNull("There are crossmatchtypes defined", all);

    Assert.assertEquals("There are 2 crossmatch types defined", 2, all.size());
  }

  @Test
  public void testIsCrossmatchTypeValid() throws Exception {
    boolean matched = crossmatchTypeRepository.isCrossmatchTypeValid("Anti Human Globulin");
    Assert.assertTrue("Crossmatch type called 'Anti Human Globulin' exists", matched);
  }

  @Test
  @Ignore("The saveAllCrossmatchTypes method fails with an error: 'detached entity passed to persist'")
  public void testSaveAllCrossmatchTypes() throws Exception {
    List<CrossmatchType> all = crossmatchTypeRepository.getAllCrossmatchTypes();
    Assert.assertNotNull("There are crossmatchtypes defined", all);

    CrossmatchType toBeSaved = new CrossmatchType();
    toBeSaved.setId(3l); // if this isn't set then the entity will not be saved
    toBeSaved.setCrossmatchType("Junit");
    all.add(toBeSaved);

    crossmatchTypeRepository.saveAllCrossmatchTypes(all);

    List<CrossmatchType> allSaved = crossmatchTypeRepository.getAllCrossmatchTypes();
    Assert.assertEquals("There are now 3 crossmatch types defined", 3, allSaved.size());
  }
}
