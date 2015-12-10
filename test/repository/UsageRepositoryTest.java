package repository;

import model.usage.ComponentUsage;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Test using DBUnit to test the UsageRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class UsageRepositoryTest {

  @Autowired
  UsageRepository usageRepository;

  @Autowired
  ComponentRepository componentRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/UsageRepositoryDataset.xml");
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
  public void testFindUsageById() throws Exception {
    ComponentUsage one = usageRepository.findUsageById(1l);
    Assert.assertNotNull("There is a ComponentUsage", one);
    Assert.assertEquals("ComponentUsage is correct", "Lucky", one.getPatientName());
  }

  @Test
  public void testFindComponentUsage() throws Exception {
    ComponentUsage usage = usageRepository.findComponentUsage("3333333-0011");
    Assert.assertNotNull("ComponentUsage found", usage);
    Assert.assertEquals("ComponentUsage correct", Long.valueOf(3), usage.getId());
  }

  @Test
  public void testFindUsageByComponentIdentificationNumber() throws Exception {
    ComponentUsage usage = usageRepository.findUsageByComponentIdentificationNumber("3333333-0011");
    Assert.assertNotNull("ComponentUsage found", usage);
    Assert.assertEquals("ComponentUsage correct", Long.valueOf(3), usage.getId());
  }

  @Test
  public void testFindUsageByIdUnknown() throws Exception {
    ComponentUsage one = usageRepository.findUsageById(123l);
    Assert.assertNull("There is no ComponentUsage", one);
  }

  @Test
  public void testUpdateUsage() throws Exception {
    ComponentUsage one = usageRepository.findUsageById(1l);
    one.setHospital("Junit hospital");
    usageRepository.saveUsage(one);
    ComponentUsage savedOne = usageRepository.findUsageById(1l);
    Assert.assertEquals("ComponentUsage is updated", "Junit hospital", savedOne.getHospital());
  }

  @Test
  public void testAddUsage() throws Exception {
    ComponentUsage one = new ComponentUsage();
    one.setHospital("Junit hospital");
    one.setComponent(componentRepository.findComponent(6l)); // note: this component is actually discarded
    one.setUsageDate(new Date());
    ComponentUsage savedOne = usageRepository.addUsage(one);
    Assert.assertNotNull("Saved ComponentUsage has an id", savedOne.getId());
    ComponentUsage retrievedOne = usageRepository.findUsageById(savedOne.getId());
    Assert.assertNotNull("There is a ComponentUsage", retrievedOne);
    Assert.assertEquals("ComponentUsage is updated", "Junit hospital", retrievedOne.getHospital());
  }

  @Test
  public void testDeleteUsage() throws Exception {
    usageRepository.deleteUsage("3333333-0011");
    ComponentUsage one = usageRepository.findUsageById(3l);
    Assert.assertNotNull("There is a ComponentUsage", one);
    Assert.assertTrue("ComponentUsage is deleted", one.getIsDeleted());
  }

  @Test
  public void testDeleteAllUsages() throws Exception {
    usageRepository.deleteAllUsages();
    ComponentUsage one = usageRepository.findUsageById(1l);
    Assert.assertNull("There is no ComponentUsage", one);
  }

  @Test
  @Ignore("Bug - error in HSQL: dateUsed of: model.usage.ComponentUsage [SELECT u FROM model.usage.ComponentUsage u WHERE (u.component.componentIdentificationNumber = :componentIdentificationNumber OR u.useIndication IN (:useIndications)) AND (u.dateUsed BETWEEN :dateUsedFrom AND :dateUsedTo) AND (u.isDeleted= :isDeleted)]")
  public void testFindAnyUsageMatching() throws Exception {
    List<String> usages = new ArrayList<>();
    usages.add("Operation");
    usages.add("Emergency");
    usageRepository.findAnyUsageMatching("3333333-0011", "2015-01-01", "2015-10-10", usages);
  }
}
