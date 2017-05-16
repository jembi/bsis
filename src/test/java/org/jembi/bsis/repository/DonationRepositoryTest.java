package org.jembi.bsis.repository;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
  public void testGetDonations() throws Exception {
    Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
    Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-10");
    List<Donation> all = donationRepository.getDonations(start, end);
    Assert.assertNotNull("There are donations", all);
    Assert.assertEquals("There are 5 donations", 5, all.size());
  }

  @Test
  public void testGetDonationsNone() throws Exception {
    Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2012-02-01");
    Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2012-02-10");
    List<Donation> all = donationRepository.getDonations(start, end);
    Assert.assertNotNull("There are no donations but list is not null", all);
    Assert.assertEquals("There are 0 donations", 0, all.size());
  }

  @Test
  public void testFilterDonationsWithBloodTypingResults() throws Exception {
    List<Donation> donations = new ArrayList<Donation>();
    donations.add(donationRepository.findDonationById(UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1")));
    donations.add(donationRepository.findDonationById(UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a2")));
    donations.add(donationRepository.findDonationById(UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a3")));
    Map<UUID, BloodTestingRuleResult> result = donationRepository.filterDonationsWithBloodTypingResults(donations);
    Assert.assertEquals("There are two donations with completed tests", 2, result.size());
    for (BloodTestingRuleResult r : result.values()) {
      if (r.getDonation().getDonationIdentificationNumber().equals("1234567")) {
        Assert.assertEquals("O type blood match", "O", r.getBloodAbo());
      } else if (r.getDonation().getDonationIdentificationNumber().equals("1212129")) {
        Assert.assertEquals("A type blood match", "A", r.getBloodAbo());
      }
    }
  }

  @Test
  public void testAddDonation() throws Exception {
    Donation newDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(DONATION_ID_1);
    newDonation.setId(existingDonation.getId());
    newDonation.copy(existingDonation);
    newDonation.setId(null); // don't want to override, just save time with the copy
    newDonation.setDonationIdentificationNumber("JUNIT123");
    Calendar today = Calendar.getInstance();
    newDonation.setCreatedDate(today.getTime());
    newDonation.setBleedEndTime(today.getTime());
    today.add(Calendar.MINUTE, -15);
    newDonation.setBleedStartTime(today.getTime());
    donationRepository.saveDonation(newDonation);
    Donation savedDonation = donationRepository.findDonationByDonationIdentificationNumber("JUNIT123");
    Assert.assertNotNull("Found new donation", savedDonation);
    Assert.assertNotNull("Donor date of lastDonation has been set", savedDonation.getDonor().getDateOfLastDonation());
  }

  @Test(expected = javax.persistence.PersistenceException.class)
  public void testAddDonationWithSameDIN_shouldThrow() throws Exception {
    Donation updatedDonation = new Donation();
    Donation existingDonation = donationRepository.findDonationById(DONATION_ID_1);
    updatedDonation.setId(existingDonation.getId());
    updatedDonation.copy(existingDonation);
    updatedDonation.setId(null); // don't want to override, just save time with the copy
    donationRepository.saveDonation(updatedDonation);
    // should fail because DIN already exists
  }

  @Test
  public void testUpdateDonation() throws Exception {
    Donation existingDonation = donationRepository.findDonationById(DONATION_ID_1);
    existingDonation.setDonorWeight(new BigDecimal(123));
    donationRepository.updateDonation(existingDonation);
    Donation updatedDonation = donationRepository.findDonationById(DONATION_ID_1);
    Assert.assertEquals("donor weight was updataed", new BigDecimal(123), updatedDonation.getDonorWeight());
  }

}
