package repository;

import model.packtype.PackType;
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
 * Test using DBUnit to test the PackTypeRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class PackTypeRepositoryTest {

  @Autowired
  PackTypeRepository packTypeRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/PackTypeRepositoryDataset.xml");
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
  public void testGetAllPackTypes() throws Exception {
    List<PackType> all = packTypeRepository.getAllPackTypes();
    Assert.assertNotNull("There are pack types defined", all);
    Assert.assertEquals("There are 9 pack types defined", 9, all.size());
  }

  @Test
  public void testGetAllEnabledPackTypes() throws Exception {
    List<PackType> all = packTypeRepository.getAllEnabledPackTypes();
    Assert.assertNotNull("There are pack types enabled", all);
    Assert.assertEquals("There are 8 pack types enabled", 8, all.size());
  }

  @Test
  public void testFindPackTypeByName() throws Exception {
    PackType one = packTypeRepository.findPackTypeByName("Single");
    Assert.assertNotNull("There is a pack type named 'Single'", one);
    Assert.assertEquals("There is a pack type named 'Single'", "Single", one.getPackType());
  }

  @Test
  public void testUpdatePackType() throws Exception {
    PackType two = packTypeRepository.findPackTypeByName("Double");
    Assert.assertNotNull("There is a pack types named 'Double'", two);
    two.setCountAsDonation(false);
    two.setPeriodBetweenDonations(0);
    packTypeRepository.updatePackType(two);
    PackType savedTwo = packTypeRepository.findPackTypeByName("Double");
    Assert.assertNotNull("There is a pack type named 'Double'", savedTwo);
    Assert.assertEquals("Is not counted as a donation", false, savedTwo.getCountAsDonation());
    Assert.assertEquals("0 days between donations", new Integer(0), savedTwo.getPeriodBetweenDonations());
  }

  @Test
  public void testSavePackType() throws Exception {
    PackType toBeSaved = new PackType();
    toBeSaved.setPackType("Junit");
    toBeSaved.setPeriodBetweenDonations(123);
    toBeSaved.setCountAsDonation(true);

    packTypeRepository.savePackType(toBeSaved);

    PackType saved = packTypeRepository.findPackTypeByName("Junit");
    Assert.assertNotNull("There is a pack type named 'Double'", saved);
    Assert.assertEquals("Is counted as a donation", true, saved.getCountAsDonation());
    Assert.assertEquals("123 days between donations", new Integer(123), saved.getPeriodBetweenDonations());
  }
}
