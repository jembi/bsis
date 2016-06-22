package org.jembi.bsis.repository;

import java.io.File;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DonorDeferralRepository
 */
public class DonorDeferralRepositoryTest extends DBUnitContextDependentTestSuite {

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
  public void testGetById() throws Exception {
    DonorDeferral deferral = donorDeferralRepository.findDonorDeferralById(1l);
    Assert.assertNotNull("There is a deferral", deferral);
    Assert.assertEquals("Correct deferral returned", "High risk behaviour", deferral.getDeferralReason().getReason());
  }
  
  @Test
  public void testGetById_VenueSetCorrectly() throws Exception {
    DonorDeferral deferral = donorDeferralRepository.findDonorDeferralById(1l);
    Assert.assertNotNull("There is a deferral", deferral);
    Assert.assertNotNull("There is a venue", deferral.getVenue());
    Assert.assertEquals("Correct venue returned", "location 1", deferral.getVenue().getName());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testGetByIdDoesNotExist() throws Exception {
    donorDeferralRepository.findDonorDeferralById(123l);
  }

  @Test
  public void testCountDonorDeferralsForDonor() throws Exception {
    Donor donor = donorRepository.findDonorById(1l);
    int count = donorDeferralRepository.countDonorDeferralsForDonor(donor);
    Assert.assertEquals("Donor has 2 deferrals", 2, count);
  }
}
