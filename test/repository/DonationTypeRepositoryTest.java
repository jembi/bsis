package repository;

import model.donationtype.DonationType;
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

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 * Test using DBUnit to test the DonationTypeRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DonationTypeRepositoryTest {

  @Autowired
  DonationTypeRepository donationTypeRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/DonationTypeRepositoryDataset.xml");
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
  public void testGetAll() throws Exception {
    List<DonationType> all = donationTypeRepository.getAllDonationTypes();
    Assert.assertNotNull("There are donation types defined", all);
    Assert.assertEquals("There are 4 donation types that aren't deleted", 4, all.size());
  }

  @Test
  public void testGetAllTrue() throws Exception {
    List<DonationType> all = donationTypeRepository.getAllDonationTypes(true);
    Assert.assertNotNull("There are donation types defined", all);
    Assert.assertEquals("There are 5 donation types in total", 5, all.size());
  }

  @Test
  public void testGetAllFalse() throws Exception {
    List<DonationType> all = donationTypeRepository.getAllDonationTypes(false);
    Assert.assertNotNull("There are donation types defined", all);
    Assert.assertEquals("There are 4 donation types that aren't deleted", 4, all.size());
  }

  @Test
  public void testGetDonationTypeById() throws Exception {
    DonationType one = donationTypeRepository.getDonationTypeById(1);
    Assert.assertNotNull("There is a donation types with id 1", one);
    Assert.assertEquals("There is a donation type named 'Voluntary'", "Voluntary", one.getDonationType());
  }

  @Test
  public void testgetDonationType() throws Exception {
    DonationType one = donationTypeRepository.getDonationType("Voluntary");
    Assert.assertNotNull("There is a donation type called Voluntary", one);
  }

  @Test
  public void testUpdateDonationType() throws Exception {
    DonationType two = donationTypeRepository.getDonationTypeById(1);
    Assert.assertNotNull("There is a donationType named 'Voluntary'", two);
    two.setIsDeleted(true);
    donationTypeRepository.updateDonationType(two);
    DonationType savedTwo = donationTypeRepository.getDonationTypeById(1);
    Assert.assertTrue("donation type is deleted", savedTwo.getIsDeleted());
  }

  @Test
  public void testSaveDonationType() throws Exception {
    DonationType toBeSaved = new DonationType();
    toBeSaved.setDonationType("Junit");

    donationTypeRepository.saveDonationType(toBeSaved);

    DonationType saved = donationTypeRepository.getDonationType("Junit");
    Assert.assertNotNull("There is a donationType named 'Junit'", saved);
  }
}
