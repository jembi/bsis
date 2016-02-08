package repository;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import suites.DBUnitContextDependentTestSuite;
import viewmodel.BloodTestingRuleResult;

/**
 * Test using DBUnit to test the BloodTestingRepository
 */
public class BloodTestingRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  BloodTestingRepository bloodTestingRepository;

  @Autowired
  DonationRepository donationRepository;
    
    @Override
    protected IDataSet getDataSet() throws Exception {
        File file = new File("test/dataset/BloodTestingRepositoryDataset.xml");
        return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
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
