package org.jembi.bsis.repository;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.UUID;

/**
 * Test using DBUnit to test the Donation Repository
 */
public class DonationRepositoryTest extends DBUnitContextDependentTestSuite {

  private static final UUID DONATION_ID_1 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");
  @Autowired
  DonationRepository donationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DonationRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testFindDonationById() throws Exception {
    Donation donation = donationRepository.findDonationById(DONATION_ID_1);
    Assert.assertNotNull("There is a donation with id 1", donation);
    Assert.assertEquals("The donation has a DIN of 1234567", "1234567", donation.getDonationIdentificationNumber());
  }

  @Test
  public void testFindDonationByDIN() throws Exception {
    Donation donation = donationRepository.findDonationByDonationIdentificationNumber("1234567");
    Assert.assertNotNull("There is a donation with DIN 1234567", donation);
    Assert.assertEquals("The donation has a DIN of 1234567", "1234567", donation.getDonationIdentificationNumber());
  }

  @Test
  @Ignore("This test will fail - see above test")
  public void testVerifyDonationIdentificationNumbers() throws Exception {
    // this test will fail as soon as one of the DINs in the list is unknown - it will throw an
    // exception and the method will not return any results for the other DINs.
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindDonationByIdUnknown() throws Exception {
    donationRepository.findDonationById(UUID.randomUUID());
  }

  @Test
  public void testAddDonation() throws Exception {
    Donation newDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(DONATION_ID_1);
    newDonation.setId(existingDonation.getId());
    newDonation.setVenue(existingDonation.getVenue());
    newDonation.setDonor(existingDonation.getDonor());
    newDonation.setId(null);
    newDonation.setPackType(existingDonation.getPackType());
    newDonation.setDonationIdentificationNumber("JUNIT123");
    Calendar today = Calendar.getInstance();
    newDonation.setCreatedDate(today.getTime());
    newDonation.setBleedEndTime(today.getTime());
    today.add(Calendar.MINUTE, -15);
    newDonation.setBleedStartTime(today.getTime());
    donationRepository.save(newDonation);
    Donation savedDonation = donationRepository.findDonationByDonationIdentificationNumber("JUNIT123");
    Assert.assertNotNull("Found new donation", savedDonation);
    Assert.assertNotNull("Donor date of lastDonation has been set", savedDonation.getDonor().getDateOfLastDonation());
  }

  @Test(expected = javax.persistence.PersistenceException.class)
  public void testAddDonationWithSameDIN_shouldThrow() throws Exception {
    Donation newDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(DONATION_ID_1);
    newDonation.setId(existingDonation.getId());
    newDonation.setVenue(existingDonation.getVenue());
    newDonation.setDonor(existingDonation.getDonor());
    donationRepository.save(newDonation);
    // should fail because DIN already exists
  }

  @Test
  public void testUpdateDonation() throws Exception {
    Donation existingDonation = donationRepository.findDonationById(DONATION_ID_1);
    existingDonation.setDonorWeight(new BigDecimal(123));
    donationRepository.update(existingDonation);
    Donation updatedDonation = donationRepository.findDonationById(DONATION_ID_1);
    Assert.assertEquals("donor weight was updataed", new BigDecimal(123), updatedDonation.getDonorWeight());
  }

}
