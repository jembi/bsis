package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import viewmodel.BloodTestingRuleResult;

/**
 * Test using DBUnit to test the BloodTestingRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class BloodTestingRepositoryTest {

  @Autowired
  BloodTestingRepository bloodTestingRepository;

  @Autowired
  DonationRepository donationRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/BloodTestingRepositoryDataset.xml");
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
  public void testGetBloodTypingTests() throws Exception {
    List<BloodTest> bloodTests = bloodTestingRepository.getBloodTypingTests();
    Assert.assertNotNull("Blood tests exist", bloodTests);
    Assert.assertFalse("Blood tests exist", bloodTests.isEmpty());
    for (BloodTest bt : bloodTests) {
      Assert.assertEquals("Only blood typing tests are returned", BloodTestCategory.BLOODTYPING, bt.getCategory());
    }
  }

  @Test
  public void testGetTtiTests() throws Exception {
    List<BloodTest> bloodTests = bloodTestingRepository.getTTITests();
    Assert.assertNotNull("Blood tests exist", bloodTests);
    Assert.assertFalse("Blood tests exist", bloodTests.isEmpty());
    for (BloodTest bt : bloodTests) {
      Assert.assertEquals("Only TTI tests are returned", BloodTestCategory.TTI, bt.getCategory());
    }
  }

  @Test
  public void testGetTestsOfTypeAdvancedBloodTyping() throws Exception {
    List<BloodTest> bloodTests = bloodTestingRepository.getBloodTestsOfType(BloodTestType.ADVANCED_BLOODTYPING);
    Assert.assertNotNull("Blood tests exist", bloodTests);
    Assert.assertTrue("Blood tests exist", bloodTests.isEmpty());
  }

  @Test
  public void testGetTestsOfTypeBasicBloodTyping() throws Exception {
    List<BloodTest> bloodTests = bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING);
    Assert.assertNotNull("Blood tests exist", bloodTests);
    Assert.assertFalse("Blood tests exist", bloodTests.isEmpty());
    for (BloodTest bt : bloodTests) {
      Assert.assertEquals("Only advanced blood typing tests are returned", BloodTestType.BASIC_BLOODTYPING,
          bt.getBloodTestType());
    }
  }

  @Test
  public void testDoubleEntryRequiredAfterTTIEdit() throws Exception {

    // Update test 17 to POS and check that the doubleEntryRequired field is updated to true only
    // for that test. All tests are NEG to start with.
    Donation donation = donationRepository.findDonationById(8l);
    Map<Long, String> stringResults = new HashMap<Long, String>();
    stringResults.put(17L, "POS");
    stringResults.put(20L, "NEG");
    stringResults.put(23L, "NEG");
    stringResults.put(26L, "NEG");
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    ruleResult.setExtraInformation(new HashSet<String>());
    bloodTestingRepository.saveBloodTestResultsToDatabase(stringResults, donation, new Date(), ruleResult);

    Map<Long, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    for (Long key : newResults.keySet()) {
      BloodTestResult result = (BloodTestResult) newResults.get(key);
      if (result.getBloodTest().getCategory().equals(BloodTestCategory.TTI)) {
        if (result.getBloodTest().getId() == 17) {
          Assert.assertEquals("Field doubleEntryRequired is set to true for test 17", true,
              result.getDoubleEntryRequired());
        } else {
          Assert.assertEquals("Field doubleEntryRequired is false", false, result.getDoubleEntryRequired());
        }
      }
    }
  }
}
