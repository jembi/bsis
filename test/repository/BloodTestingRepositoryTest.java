package repository;

import java.io.File;
import java.util.List;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestType;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import repository.bloodtesting.BloodTestingRepository;
import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the BloodTestingRepository
 */
public class BloodTestingRepositoryTest extends DBUnitContextDependentTestSuite {
	
	@Autowired
	BloodTestingRepository bloodTestingRepository;
	
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
        Assert.assertEquals("Only advanced blood typing tests are returned", BloodTestType.BASIC_BLOODTYPING, bt.getBloodTestType());
      }   
    }
}
