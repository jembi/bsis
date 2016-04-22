package repository;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import model.componentbatch.ComponentBatch;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.location.Location;
import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the DonationBatchRepository
 */
public class DonationBatchRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DonationBatchRepository donationBatchRepository;

  @Autowired
  LocationRepository locationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/DonationBatchRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testFindDonationBatchById() throws Exception {
    DonationBatch one = donationBatchRepository.findDonationBatchById(1l);
    Assert.assertNotNull("There is a donation batch with the id 1", one);
    Assert.assertEquals("The donation batch has the number 'B0215000000'", "B0215000000", one.getBatchNumber());
  }

  @Test
  public void testFindDonationBatchByIdEmpty() throws Exception {
    DonationBatch five = donationBatchRepository.findDonationBatchById(5l);
    Assert.assertNotNull("There is a donation batch with the id 5", five);
    Assert.assertEquals("The donation batch has the number 'B0215000005'", "B0215000005", five.getBatchNumber());
  }

  @Test
  public void testGetRecentlyClosedDonationBatches() throws Exception {
    List<DonationBatch> closed = donationBatchRepository.getRecentlyClosedDonationBatches(5);
    Assert.assertNotNull("There are recently closed donation batches", closed);
    // NOTE: This includes deleted donation batches.
    Assert.assertEquals("There are 3 recently closed donation batches", 3, closed.size());
  }

  @Test
  public void testFindDonationBatchByBatchNumber() throws Exception {
    DonationBatch one = donationBatchRepository.findDonationBatchByBatchNumber("B0215000000");
    Assert.assertNotNull("There is a donation batch with the number 'B0215000000'", one);
    Assert.assertEquals("The donation batch has the number 'B0215000000'", "B0215000000", one.getBatchNumber());
  }

  @Test
  public void testFindDonationsInBatch() throws Exception {
    List<Donation> donations = donationBatchRepository.findDonationsInBatch(1l);
    Assert.assertNotNull("There donations in the batch with id 1", donations);
    Assert.assertEquals("There is 1 donation in the batch with id 1", 1, donations.size());
  }

  @Test
  public void testAddDonationBatches() throws Exception {
    DonationBatch donationBatch = new DonationBatch();
    donationBatch.setBatchNumber("JUNIT123");
    donationBatch.setCreatedDate(new Date());
    donationBatch.setLastUpdated(new Date());
    donationBatch.setIsDeleted(false);
    donationBatch.setIsClosed(true);
    donationBatch.setNotes("Testing 123");
    Location location = locationRepository.findLocationByName("Maseru");
    donationBatch.setVenue(location);
    donationBatchRepository.addDonationBatch(donationBatch);

    DonationBatch savedDonationBatch = donationBatchRepository.findDonationBatchByBatchNumber("JUNIT123");
    Assert.assertNotNull("There is a donation batch with the number 'JUNIT123'", savedDonationBatch);
    Assert.assertEquals("The donation batch has the number 'JUNIT123'", "JUNIT123", savedDonationBatch.getBatchNumber());
  }

  @Test
  public void testUpdateDonationBatch() throws Exception {
    DonationBatch one = donationBatchRepository.findDonationBatchByBatchNumber("B0215000000");
    Assert.assertNotNull("There is a donation batch with the number 'B0215000000'", one);
    one.setNotes("Testing 123");
    donationBatchRepository.updateDonationBatch(one);

    DonationBatch savedOne = donationBatchRepository.findDonationBatchByBatchNumber("B0215000000");
    Assert.assertEquals("The donation batch has updated notes", "Testing 123", savedOne.getNotes());
  }

  @Test
  public void testFindUnassignedDonationBatches() throws Exception {
    List<DonationBatch> unassigned = donationBatchRepository.findUnassignedDonationBatches();
    Assert.assertNotNull("Should not return a null list", unassigned);
    Assert.assertEquals("There are no unassigned donation batches", 0, unassigned.size());

    // create an unassigned batch
    DonationBatch donationBatch = new DonationBatch();
    donationBatch.setBatchNumber("JUNIT123");
    donationBatch.setCreatedDate(new Date());
    donationBatch.setLastUpdated(new Date());
    donationBatch.setIsDeleted(false);
    donationBatch.setIsClosed(true);
    donationBatchRepository.addDonationBatch(donationBatch);

    unassigned = donationBatchRepository.findUnassignedDonationBatches();
    Assert.assertNotNull("TShould not return a null list", unassigned);
    Assert.assertEquals("There is 1 unassigned donation batch", 1, unassigned.size());
    DonationBatch savedDonationBatch = unassigned.get(0);
    Assert.assertEquals("The donation batch has the number 'JUNIT123'", "JUNIT123", savedDonationBatch.getBatchNumber());
  }

  @Test
  public void testFindDonationBatchByBatchNumberIncludeDeleted() throws Exception {
    try {
      donationBatchRepository.findDonationBatchByBatchNumber("B0715000000");
      Assert.fail("Donation batch with the number 'B0415000000' is deleted");
    } catch (NoResultException e) {
      // expected exception
    }
    DonationBatch two = donationBatchRepository.findDonationBatchByBatchNumberIncludeDeleted("B0715000000");
    Assert.assertNotNull("There is a donation batch with the number 'B0715000000'", two);
    Assert.assertEquals("The donation batch has the number 'B0715000000'", "B0715000000", two.getBatchNumber());
  }

  @Test
  public void testFindDonationBatches() throws Exception {
    List<Long> locationIds = new ArrayList<Long>();
    locationIds.add(1L);
    List<DonationBatch> batches = donationBatchRepository.findDonationBatches(true, locationIds, null, null);
    Assert.assertNotNull("There are batches in Maseru", batches);
    Assert.assertEquals("There are 1 donation batches in Maseru", 1, batches.size());
  }

  @Test
  public void testFindDonationBatchesWithDates() throws Exception {
    List<Long> locationIds = new ArrayList<Long>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String startDate = "2015-03-01 22:00:00";
    String endDate = "2015-03-04 22:00:00";
    List<DonationBatch> batches = donationBatchRepository.findDonationBatches(false, locationIds, df.parse(startDate), df.parse(endDate));
    Assert.assertNotNull("There are batches in this date range", batches);
    Assert.assertEquals("There are 2 donation batches in this date range", 2, batches.size());
  }

  @Test
  public void testDonationBatchHasComponentBatch() throws Exception {
    DonationBatch one = donationBatchRepository.findDonationBatchById(1l);
    ComponentBatch componentBatch = one.getComponentBatch();
    Assert.assertEquals("There are 5 boxes on component batch", 5, componentBatch.getBloodTransportBoxCount().intValue());
  }
}
