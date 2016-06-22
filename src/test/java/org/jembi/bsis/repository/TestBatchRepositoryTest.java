package org.jembi.bsis.repository;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.TestBatchRepository;
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
    TestBatch testBatch = testBatchRepository.findTestBatchById(1l);
    Assert.assertNotNull("TestBatch defined", testBatch);
    Assert.assertEquals("TestBatch is correct", "000000", testBatch.getBatchNumber());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindTestBatchByIdUnknown() throws Exception {
    testBatchRepository.findTestBatchById(123l);
  }

  @Test
  public void testFindTestBatchesNone() throws Exception {
    TestBatchStatus status = TestBatchStatus.RELEASED;
    String createdAfterDate = "2015-07-10 00:00:00";
    String createdBeforeDate = "2015-07-11 23:59:59";
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    List<TestBatch> testBatches = testBatchRepository.findTestBatches(Arrays.asList(status),
        df.parse(createdAfterDate), df.parse(createdBeforeDate));
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
        df.parse(createdBeforeDate));
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertEquals("TestBatch matched on date", 2, testBatches.size());
  }

  @Test
  public void testFindTestBatchesMatchOnStatusOnly() throws Exception {
    TestBatchStatus status = TestBatchStatus.CLOSED;
    List<TestBatch> testBatches = testBatchRepository.findTestBatches(Arrays.asList(status), null, null);
    Assert.assertNotNull("TestBatch not null", testBatches);
    Assert.assertEquals("TestBatch matched on status", 1, testBatches.size());
  }

  @Test
  public void testDeleteTestBatch() throws Exception {
    testBatchRepository.deleteTestBatch(1l);
    TestBatch testBatch = testBatchRepository.findTestBatchById(1l);
    Assert.assertTrue("TestBatch is deleted", testBatch.getIsDeleted());
  }

  @Test
  public void testUpdateTestBatch() throws Exception {
    TestBatch testBatch = testBatchRepository.findTestBatchById(2l);
    testBatch.setStatus(TestBatchStatus.RELEASED);
    testBatchRepository.update(testBatch);
    TestBatch updatedTestBatch = testBatchRepository.findTestBatchById(2l);
    Assert.assertEquals("TestBatch status is correct", TestBatchStatus.RELEASED, updatedTestBatch.getStatus());
  }

  @Test
  public void testSaveTestBatch() throws Exception {
    TestBatch testBatch = new TestBatch();
    List<DonationBatch> donationBatches = new ArrayList<DonationBatch>();
    donationBatches.add(donationBatchRepository.findDonationBatchById(3l));
    testBatch.setDonationBatches(donationBatches);
    TestBatch savedTestBatch = testBatchRepository.saveTestBatch(testBatch, "1234567");
    Assert.assertNotNull("Saved TestBatch has an id", savedTestBatch.getId());
    TestBatch retrievedTestBatch = testBatchRepository.findTestBatchById(savedTestBatch.getId());
    Assert.assertNotNull("Saved TestBatch is found", retrievedTestBatch);
    Assert.assertEquals("TestBatch status is correct", TestBatchStatus.OPEN, retrievedTestBatch.getStatus());
    Assert.assertEquals("TestBatch batchNumber is correct", "1234567", retrievedTestBatch.getBatchNumber());
    DonationBatch updatedDonationBatch = donationBatchRepository.findDonationBatchById(3l);
    Assert.assertNotNull("DonationBatch was linked to TestBatch", updatedDonationBatch.getTestBatch());
  }

}
