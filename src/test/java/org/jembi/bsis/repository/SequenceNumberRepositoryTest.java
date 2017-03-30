package org.jembi.bsis.repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the SequenceNumberRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class SequenceNumberRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {

  @Autowired
  SequenceNumberRepository sequenceNumberRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/SequenceNumberRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  private IDatabaseConnection getConnection() throws SQLException {
    IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
    DatabaseConfig config = connection.getConfig();
    config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
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
  public void testGetNextDonationIdentificationNumber() throws Exception {
    String next = sequenceNumberRepository.getNextDonationIdentificationNumber();
    Assert.assertEquals("next DIN is correct", "C000003", next);
    next = sequenceNumberRepository.getNextDonationIdentificationNumber();
    Assert.assertEquals("next DIN is correct", "C000004", next);
  }

  @Test
  public void testGetNextRequestNumber() throws Exception {
    String next = sequenceNumberRepository.getNextRequestNumber();
    Assert.assertEquals("next RequestNumber is correct", "R000005", next);
    next = sequenceNumberRepository.getNextRequestNumber();
    Assert.assertEquals("next RequestNumber is correct", "R000006", next);
  }

  @Test
  public void testGetNextDonorNumber() throws Exception {
    String next = sequenceNumberRepository.getNextDonorNumber();
    Assert.assertEquals("next DonorNumber is correct", "000003", next);
    next = sequenceNumberRepository.getNextDonorNumber();
    Assert.assertEquals("next DonorNumber is correct", "000004", next);
  }

  @Test
  public void testGetBatchDonationIdentificationNumbers() throws Exception {
    List<String> next = sequenceNumberRepository.getBatchDonationIdentificationNumbers(25);
    Assert.assertNotNull("Next batch DINs exist", next);
    Assert.assertEquals("next batch DINs are correct", 25, next.size());
    Assert.assertEquals("next batch DINs are correct", "C000003", next.get(0));
    Assert.assertEquals("next batch DINs are correct", "C000004", next.get(1));
  }

  @Test
  public void testGetSequenceNumber() throws Exception {
    String next = sequenceNumberRepository.getSequenceNumber("Test", "testNumber");
    Assert.assertEquals("next test number is correct", "000023", next);
  }

  @Test
  public void testGetNextBatchNumber() throws Exception {
    String next = sequenceNumberRepository.getNextBatchNumber();
    Assert.assertEquals("next batchNumber is correct", "000002", next);
  }

  @Test
  public void testGetNextTestBatchNumber() throws Exception {
    String next = sequenceNumberRepository.getNextTestBatchNumber();
    Assert.assertEquals("next test batchNumber is correct", "000000", next);
    next = sequenceNumberRepository.getNextTestBatchNumber();
    Assert.assertEquals("next test batchNumber is correct", "000001", next);
  }
}
