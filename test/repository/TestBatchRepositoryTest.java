package repository;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the TestBatchRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class TestBatchRepositoryTest {

    @Autowired
    TestBatchRepository testBatchRepository;

    @Autowired
    DonationBatchRepository donationBatchRepository;

    @Autowired
    private DataSource dataSource;

    private IDataSet getDataSet() throws Exception {
        File file = new File("test/dataset/TestBatchRepositoryDataset.xml");
        return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
    }

    private IDatabaseConnection getConnection() throws SQLException {
        IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
        DatabaseConfig config = connection.getConfig();
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
        return connection;
    }

    @Before
    public void init() throws Exception {
        IDatabaseConnection connection = getConnection();
        try {
            IDataSet dataSet = getDataSet();
            DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
        } finally {
            connection.close();
        }
    }

    @AfterTransaction
    public void after() throws Exception {
        IDatabaseConnection connection = getConnection();
        try {
            IDataSet dataSet = getDataSet();
            DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
        } finally {
            connection.close();
        }
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
        testBatchRepository.updateTestBatch(testBatch);
        TestBatch updatedTestBatch = testBatchRepository.findTestBatchById(2l);
        Assert.assertEquals("TestBatch status is correct", TestBatchStatus.RELEASED, updatedTestBatch.getStatus());
    }

    @Test
    public void testSaveTestBatch() throws Exception {
        TestBatch testBatch = new TestBatch();
        List<DonationBatch> donationBatches = new ArrayList<>();
        donationBatches.add(donationBatchRepository.findDonationBatchById(3));
        testBatch.setDonationBatches(donationBatches);
        TestBatch savedTestBatch = testBatchRepository.saveTestBatch(testBatch, "1234567");
        Assert.assertNotNull("Saved TestBatch has an id", savedTestBatch.getId());
        TestBatch retrievedTestBatch = testBatchRepository.findTestBatchById(savedTestBatch.getId());
        Assert.assertNotNull("Saved TestBatch is found", retrievedTestBatch);
        Assert.assertEquals("TestBatch status is correct", TestBatchStatus.OPEN, retrievedTestBatch.getStatus());
        Assert.assertEquals("TestBatch batchNumber is correct", "1234567", retrievedTestBatch.getBatchNumber());
        DonationBatch updatedDonationBatch = donationBatchRepository.findDonationBatchById(3);
        Assert.assertNotNull("DonationBatch was linked to TestBatch", updatedDonationBatch.getTestBatch());
    }

    @Test
    public void testGetRecentlyClosedTestBatches() throws Exception {
        List<TestBatch> all = testBatchRepository.getRecentlyClosedTestBatches(1);
        Assert.assertNotNull("Does not return a null list", all);
        Assert.assertEquals("One TestBatch returned", 1, all.size());
    }

    @Test
    public void testGetRecentlyClosedTestBatchesWithLimit() throws Exception {
        // creating an extra closed batch
        TestBatch testBatch = testBatchRepository.findTestBatchById(2l);
        testBatch.setStatus(TestBatchStatus.CLOSED);
        testBatchRepository.updateTestBatch(testBatch);
        // only get 1 closed test batch now
        List<TestBatch> all = testBatchRepository.getRecentlyClosedTestBatches(1);
        Assert.assertNotNull("Does not return a null list", all);
        Assert.assertEquals("One TestBatch returned", 1, all.size());
    }
}
