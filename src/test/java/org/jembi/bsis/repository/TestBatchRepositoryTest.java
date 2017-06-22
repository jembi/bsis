package org.jembi.bsis.repository;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the TestBatchRepository
 */
public class TestBatchRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  TestBatchRepository testBatchRepository;

  @Autowired
  DonationBatchRepository donationBatchRepository;

  @Autowired
  LocationRepository locationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/TestBatchRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testGetAllTestBatches() throws Exception {
    List<TestBatch> all = testBatchRepository.getAllTestBatch();
    Assert.assertNotNull("There are TestBatches defined", all);
    Assert.assertEquals("There are 2 TestBatches", 2, all.size());
  }

  @Test
  public void testFindTestBatchById() throws Exception {
    UUID testBatchId = UUID.fromString("640eb339-c815-48c6-81d7-0f225d3f2701");
    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    Assert.assertNotNull("TestBatch defined", testBatch);
    Assert.assertEquals("TestBatch is correct", "000000", testBatch.getBatchNumber());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindTestBatchByIdUnknown() throws Exception {
    testBatchRepository.findTestBatchById(UUID.randomUUID());
  }

  @Test
  public void testFindTestBatchesNone() throws Exception {
    TestBatchStatus status = TestBatchStatus.RELEASED;
    String createdAfterDate = "2015-07-10 00:00:00";
    String createdBeforeDate = "2015-07-11 23:59:59";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    UUID locationId = UUID.randomUUID();

    List<TestBatch> testBatches = testBatchRepository.findTestBatches(Arrays.asList(status),
        df.parse(createdAfterDate), df.parse(createdBeforeDate), locationId);
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertTrue("TestBatch is empty", testBatches.isEmpty());
  }

  @Test
  public void testFindTestBatchesMatchOnDateOnly() throws Exception {
    List<TestBatchStatus> statuses = null;
    String createdAfterDate = "2015-08-10 00:00:00";
    String createdBeforeDate = "2015-08-13 23:59:59";

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    List<TestBatch> testBatches = testBatchRepository.findTestBatches(statuses, df.parse(createdAfterDate),
        df.parse(createdBeforeDate), null);
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertEquals("TestBatch matched on date", 2, testBatches.size());
  }

  @Test
  public void testFindTestBatchesMatchOnLocationOnly() throws Exception {
    UUID locationId = UUID.fromString("55321456-eeee-1234-b5b1-123412348812");

    List<TestBatch> testBatches = testBatchRepository.findTestBatches(null, null, null, locationId);
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertEquals("TestBatch matched on location", 2, testBatches.size());
  }

  @Test
  public void testFindTestBatchesMatchOnStatusOnly() throws Exception {
    TestBatchStatus status = TestBatchStatus.CLOSED;
    List<TestBatch> testBatches = testBatchRepository.findTestBatches(Arrays.asList(status), null, null, null);
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertEquals("TestBatch matched on status", 1, testBatches.size());
  }

  @Test
  public void testDeleteTestBatch() throws Exception {
    UUID testBatchId = UUID.fromString("640eb339-c815-48c6-81d7-0f225d3f2701");
    testBatchRepository.deleteTestBatch(testBatchId);
    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    Assert.assertTrue("TestBatch is deleted", testBatch.getIsDeleted());
  }

  @Test
  public void testUpdateTestBatch() throws Exception {
    UUID testBatchId = UUID.fromString("640eb339-c815-48c6-81d7-0f225d3f2702");
    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    testBatch.setStatus(TestBatchStatus.RELEASED);
    testBatchRepository.update(testBatch);
    TestBatch updatedTestBatch = testBatchRepository.findTestBatchById(testBatchId);
    Assert.assertEquals("TestBatch status is correct", TestBatchStatus.RELEASED, updatedTestBatch.getStatus());
  }

  @Test
  public void testSaveTestBatch() throws Exception {
    TestBatch testBatch = new TestBatch();
    UUID donationBatchId = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870683");
    Set<DonationBatch> donationBatches = new HashSet<>();
    donationBatches.add(donationBatchRepository.findDonationBatchById(donationBatchId));
    testBatch.setDonationBatches(donationBatches);
    UUID locationId = UUID.fromString("55321456-eeee-1234-b5b1-123412348811");
    Location location = locationRepository.getLocation(locationId);
    testBatch.setLocation(location);
    TestBatch savedTestBatch = testBatchRepository.saveTestBatch(testBatch, "123456");
    Assert.assertNotNull("Saved TestBatch has an id", savedTestBatch.getId());
    TestBatch retrievedTestBatch = testBatchRepository.findTestBatchById(savedTestBatch.getId());
    Assert.assertNotNull("Saved TestBatch is found", retrievedTestBatch);
    Assert.assertEquals("TestBatch status is correct", TestBatchStatus.OPEN, retrievedTestBatch.getStatus());
    Assert.assertEquals("TestBatch batchNumber is correct", "123456", retrievedTestBatch.getBatchNumber());
    DonationBatch updatedDonationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
    Assert.assertNotNull("DonationBatch was linked to TestBatch", updatedDonationBatch.getTestBatch());
  }

}
