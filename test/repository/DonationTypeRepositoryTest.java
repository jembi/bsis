package repository;

import java.io.File;
import java.util.List;

import model.donationtype.DonationType;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the DonationTypeRepository
 */
public class DonationTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DonationTypeRepository donationTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/DonationTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<DonationType> all = donationTypeRepository.getAllDonationTypes();
    Assert.assertNotNull("There are donation types defined", all);
    Assert.assertEquals("There are 4 donation types that aren't deleted", 4, all.size());
  }

  @Test
  public void testGetAllTrue() throws Exception {
    List<DonationType> all = donationTypeRepository.getAllDonationTypes(true);
    Assert.assertNotNull("There are donation types defined", all);
    Assert.assertEquals("There are 5 donation types in total", 5, all.size());
  }

  @Test
  public void testGetAllFalse() throws Exception {
    List<DonationType> all = donationTypeRepository.getAllDonationTypes(false);
    Assert.assertNotNull("There are donation types defined", all);
    Assert.assertEquals("There are 4 donation types that aren't deleted", 4, all.size());
  }

  @Test
  public void testGetDonationTypeById() throws Exception {
    DonationType one = donationTypeRepository.getDonationTypeById(1l);
    Assert.assertNotNull("There is a donation types with id 1", one);
    Assert.assertEquals("There is a donation type named 'Voluntary'", "Voluntary", one.getDonationType());
  }

  @Test
  public void testgetDonationType() throws Exception {
    DonationType one = donationTypeRepository.getDonationType("Voluntary");
    Assert.assertNotNull("There is a donation type called Voluntary", one);
  }

  @Test
  public void testUpdateDonationType() throws Exception {
    DonationType two = donationTypeRepository.getDonationTypeById(1l);
    Assert.assertNotNull("There is a donationType named 'Voluntary'", two);
    two.setIsDeleted(true);
    donationTypeRepository.updateDonationType(two);
    DonationType savedTwo = donationTypeRepository.getDonationTypeById(1l);
    Assert.assertTrue("donation type is deleted", savedTwo.getIsDeleted());
  }

  @Test
  public void testSaveDonationType() throws Exception {
    DonationType toBeSaved = new DonationType();
    toBeSaved.setDonationType("Junit");

    donationTypeRepository.saveDonationType(toBeSaved);

    DonationType saved = donationTypeRepository.getDonationType("Junit");
    Assert.assertNotNull("There is a donationType named 'Junit'", saved);
  }
}
