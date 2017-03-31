package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DonationTypeRepository
 */
public class DonationTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DonationTypeRepository donationTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DonationTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
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
    UUID donationTypeId = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870681");
    DonationType one = donationTypeRepository.getDonationTypeById(donationTypeId);
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
    UUID donationTypeId = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870682");
    DonationType two = donationTypeRepository.getDonationTypeById(donationTypeId);
    Assert.assertNotNull("There is a donationType named 'Voluntary'", two);
    two.setIsDeleted(true);
    donationTypeRepository.update(two);
    DonationType savedTwo = donationTypeRepository.getDonationTypeById(donationTypeId);
    Assert.assertTrue("donation type is deleted", savedTwo.getIsDeleted());
  }

  @Test
  public void testSaveDonationType() throws Exception {
    DonationType toBeSaved = new DonationType();
    toBeSaved.setDonationType("Junit");

    donationTypeRepository.save(toBeSaved);

    DonationType saved = donationTypeRepository.getDonationType("Junit");
    Assert.assertNotNull("There is a donationType named 'Junit'", saved);
  }
}
