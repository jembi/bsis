package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donation.Donation;
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
  LocationRepository locationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/TestBatchRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
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
  public void testFindTestBatchesMatchOnMultipleStatusesOnly() throws Exception {
    TestBatchStatus status1 = TestBatchStatus.CLOSED;
    TestBatchStatus status2 = TestBatchStatus.OPEN;
    List<TestBatch> testBatches = testBatchRepository.findTestBatches(Arrays.asList(status1, status2), null, null, null);
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertEquals("TestBatch matched on status", 2, testBatches.size());
  }

  @Test
  public void testDeleteTestBatch() throws Exception {
    UUID testBatchId = UUID.fromString("640eb339-c815-48c6-81d7-0f225d3f2701");
    testBatchRepository.deleteTestBatch(testBatchId);
    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    assertThat(testBatch.getIsDeleted(), is(true));
    Donation donation = entityManager.find(Donation.class, UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1"));
    assertThat(donation.getTestBatch(), nullValue());
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
}