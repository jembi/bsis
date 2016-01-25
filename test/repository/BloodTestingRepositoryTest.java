package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestType;

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

import repository.bloodtesting.BloodTestingRepository;

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
		}
		finally {
			connection.close();
		}
	}
	
	@AfterTransaction
	public void after() throws Exception {
		IDatabaseConnection connection = getConnection();
		try {
			IDataSet dataSet = getDataSet();
			DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
		}
		finally {
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
        Assert.assertEquals("Only advanced blood typing tests are returned", BloodTestType.BASIC_BLOODTYPING, bt.getBloodTestType());
      }   
    }
}
