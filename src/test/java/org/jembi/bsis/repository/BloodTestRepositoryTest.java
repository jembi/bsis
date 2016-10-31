package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.bloodtesting.BloodTestRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the BloodTestingRepository
 */
public class BloodTestRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  BloodTestRepository bloodTestRepository;

  @Autowired
  DonationRepository donationRepository;
    
    @Override
    protected IDataSet getDataSet() throws Exception {
        File file = new File("src/test/resources/dataset/BloodTestingRepositoryDataset.xml");
        return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
    }

  @Test
  public void testGetBloodTypingTests() throws Exception {
    List<BloodTest> bloodTests = bloodTestRepository.getBloodTypingTests();
    Assert.assertNotNull("Blood tests exist", bloodTests);
    Assert.assertFalse("Blood tests exist", bloodTests.isEmpty());
    for (BloodTest bt : bloodTests) {
      Assert.assertEquals("Only blood typing tests are returned", BloodTestCategory.BLOODTYPING, bt.getCategory());
    }
  }

  @Test
  public void testGetTestsOfTypeBasicBloodTyping() throws Exception {
    List<BloodTest> bloodTests = bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING);
    Assert.assertNotNull("Blood tests exist", bloodTests);
    Assert.assertFalse("Blood tests exist", bloodTests.isEmpty());
    for (BloodTest bt : bloodTests) {
      Assert.assertEquals("Only advanced blood typing tests are returned", BloodTestType.BASIC_BLOODTYPING, bt.getBloodTestType());
      bt.getBloodTestType();
    }
  }
}
