package org.jembi.bsis.repository;

import java.io.File;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DonorDeferralRepository
 */
public class DonorDeferralRepositoryTest extends DBUnitContextDependentTestSuite {

  private static final UUID DONOR_DEFERRAL_ID = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870681");

  private static final UUID NON_EXISTENT_DONOR_DEFERRAL_ID = UUID.fromString("99e71397-acc9-b7da-8cc5-34e6d7870681");

  @Autowired
  DonorDeferralRepository donorDeferralRepository;

  @Autowired
  DonorRepository donorRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DonorDeferralRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testGetById() {
    DonorDeferral deferral = donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID);
    Assert.assertNotNull("There is a deferral", deferral);
    Assert.assertEquals("Correct deferral returned", "High risk behaviour", deferral.getDeferralReason().getReason());
  }
  
  @Test
  public void testGetById_VenueSetCorrectly() {
    DonorDeferral deferral = donorDeferralRepository.findDonorDeferralById(DONOR_DEFERRAL_ID);
    Assert.assertNotNull("There is a deferral", deferral);
    Assert.assertNotNull("There is a venue", deferral.getVenue());
    Assert.assertEquals("Correct venue returned", "location 1", deferral.getVenue().getName());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testGetByIdDoesNotExist() {
    donorDeferralRepository.findDonorDeferralById(NON_EXISTENT_DONOR_DEFERRAL_ID);
  }

  @Test
  public void testCountDonorDeferralsForDonor() {
    UUID donorId = UUID.fromString("11e7161c-b6f7-8931-8b6b-28f10e1b4951");
    Donor donor = donorRepository.findDonorById(donorId);
    int count = donorDeferralRepository.countDonorDeferralsForDonor(donor);
    Assert.assertEquals("Donor has 2 deferrals", 2, count);
  }
}
