package repository;

import java.io.File;
import java.util.List;

import model.address.ContactMethodType;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the ContactMethod Repository
 */
public class ContactMethodTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  ContactMethodTypeRepository contactMethodTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/ContactMethodTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAllContactMethodTypes() throws Exception {
    List<ContactMethodType> all = contactMethodTypeRepository.getAllContactMethodTypes();
    Assert.assertNotNull("There are contact method types defined", all);
    Assert.assertEquals("There are 6 contact method types defined", 6, all.size());
  }

  @Test
  public void testSaveContactMethod() throws Exception {
    ContactMethodType im = new ContactMethodType();
    im.setContactMethodType("IM");
    contactMethodTypeRepository.saveContactMethod(im);

    List<ContactMethodType> all = contactMethodTypeRepository.getAllContactMethodTypes();
    Assert.assertNotNull("There are contact method types defined", all);
    Assert.assertEquals("There are now 7 contact method types defined", 7, all.size());
  }
}
