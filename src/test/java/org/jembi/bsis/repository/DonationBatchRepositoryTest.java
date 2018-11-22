package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DonationBatchRepository
 */
public class DonationBatchRepositoryTest extends DBUnitContextDependentTestSuite {

  private static final UUID DONATION_BATCH_ID_1 = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870681");
  private static final UUID DONATION_BATCH_ID_2 = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870682");
  private static final UUID DONATION_BATCH_ID_5 = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870685");
  private static final UUID NON_EXISTANT_DONATION_BATCH_ID = UUID.fromString("99e71397-acc9-b7da-8cc5-34e6d7870681");

  @Autowired
  DonationBatchRepository donationBatchRepository;

  @Autowired
  LocationRepository locationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DonationBatchRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testFindDonationBatchById() throws Exception {
    DonationBatch one = donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID_1);
    Assert.assertNotNull("There is a donation batch with the id 1", one);
    Assert.assertEquals("The donation batch has the number 'B0215000000'", "B0215000000", one.getBatchNumber());
  }

  @Test
  public void testFindDonationBatchByIdEmpty() throws Exception {
    DonationBatch five = donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID_5);
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
    List<Donation> donations = donationBatchRepository.findDonationsInBatch(DONATION_BATCH_ID_1);
    Assert.assertNotNull("There donations in the batch with id 1", donations);
    Assert.assertEquals("There is 1 donation in the batch with id 1", 1, donations.size());
  }

  @Test
  public void testAddDonationBatches() throws Exception {
    Location location = locationRepository.findLocationByName("Maseru");

    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch()
        .withBatchNumber("JUNIT123")
        .withVenue(location)
        .withDonationBatchDate(new Date())
        .withLastUpdatedDate(new Date())
        .thatIsNotDeleted()
        .thatIsClosed()
        .withDonationBatchDate(new Date())
        .withNotes("Testing 123")
        .build();

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
    List<UUID> locationIds = new ArrayList<UUID>();

    UUID locationId1 = UUID.fromString("55321456-eeee-1234-b5b1-123412348891");
    locationIds.add(locationId1);
    List<DonationBatch> batches = donationBatchRepository.findDonationBatches(true, locationIds, null, null);
    Assert.assertNotNull("There are batches in Maseru", batches);
    Assert.assertEquals("There are 1 donation batches in Maseru", 1, batches.size());
  }

  @Test
  public void testFindDonationBatchesWithDates() throws Exception {
    List<UUID> locationIds = new ArrayList<UUID>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String startDate = "2015-03-01 22:00:00";
    String endDate = "2015-03-04 22:00:00";
    List<DonationBatch> batches = donationBatchRepository.findDonationBatches(false, locationIds, df.parse(startDate), df.parse(endDate));
    Assert.assertNotNull("There are batches in this date range", batches);
    Assert.assertEquals("There are 3 donation batches in this date range", 3, batches.size());
  }

  @Test
  public void testDonationBatchHasComponentBatch() throws Exception {
    UUID componentBatchId = UUID.fromString("11e71ebf-1226-8bdb-9fc7-28f10e1b4901");
    DonationBatch one = donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID_1);
    ComponentBatch componentBatch = one.getComponentBatch();
    Assert.assertEquals("ComponentBatch is there", componentBatchId, componentBatch.getId());
  }
  
  @Test
  public void testVerifyDonationBatchExists() throws Exception {
    boolean exists = donationBatchRepository.verifyDonationBatchExists(DONATION_BATCH_ID_1);
    Assert.assertTrue("DonationBatch exists", exists);
  }
  
  @Test
  public void testVerifyDonationBatchDoesntExist() throws Exception {
    boolean exists = donationBatchRepository.verifyDonationBatchExists(NON_EXISTANT_DONATION_BATCH_ID);
    Assert.assertFalse("DonationBatch doesn't exist", exists);
  }
  
  @Test
  public void testFindUnassignedDonationBatchesForComponentBatch() throws Exception {
    List<DonationBatch> donationBatches = donationBatchRepository.findUnassignedDonationBatchesForComponentBatch();
    Assert.assertNotNull("DonationBatches returned", donationBatches);
    Assert.assertEquals("DonationBatches is correct size", 0, donationBatches.size());
  }
  
  @Test
  public void findUnassignedDonationBatchesForComponentBatchWithComponents() throws Exception {
    // create test data
    DonationBatch donationBatch1 = donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID_1);
    DonationBatch donationBatch2 = donationBatchRepository.findDonationBatchById(DONATION_BATCH_ID_2);
    Donation donation1 = donationBatch1.getDonations().get(0);
    Donation donation2 = donationBatch2.getDonations().get(0);
    Donation donation3 = donationBatch2.getDonations().get(1);
    ComponentType intialComponent = donation1.getPackType().getComponentType();
    // assigned to component batch
    Component component1 = aComponent().withComponentType(intialComponent).withDonation(donation1).buildAndPersist(entityManager);
    donation1.setComponents(Arrays.asList(component1));
    // not assigned to component batch
    Component component2 = aComponent().withComponentType(intialComponent).withDonation(donation2).buildAndPersist(entityManager);
    donation2.setComponents(Arrays.asList(component2));
    Component component3 = aComponent().withComponentType(intialComponent).withDonation(donation3).buildAndPersist(entityManager);
    donation3.setComponents(Arrays.asList(component3));
    donationBatchRepository.updateDonationBatch(donationBatch1);
    donationBatchRepository.updateDonationBatch(donationBatch2);

    List<DonationBatch> unassigned = donationBatchRepository.findUnassignedDonationBatchesForComponentBatch();

    Assert.assertNotNull("TShould not return a null list", unassigned);
    Assert.assertEquals("There is 1 unassigned donation batch", 1, unassigned.size());
    DonationBatch donationBatch = unassigned.get(0);
    Assert.assertEquals("Correct unassigned donation batch", DONATION_BATCH_ID_2, donationBatch.getId());
  }
}
