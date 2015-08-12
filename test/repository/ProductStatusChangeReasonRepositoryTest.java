package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeReasonCategory;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the ProductStatusChangeReasonRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class ProductStatusChangeReasonRepositoryTest {
	
	@Autowired
	ProductStatusChangeReasonRepository productStatusChangeReasonRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ProductStatusChangeReasonRepositoryDataset.xml");
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
	
	@After
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
	@Transactional
	public void testGetAll() throws Exception {
		List<ProductStatusChangeReason> all = productStatusChangeReasonRepository.getAllProductStatusChangeReasons();
		Assert.assertNotNull("There are ProductStatusChangeReason", all);
		Assert.assertEquals("There are 11 ProductStatusChangeReason", 11, all.size());
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChangeReasons() throws Exception {
		List<ProductStatusChangeReason> all = productStatusChangeReasonRepository
		        .getProductStatusChangeReasons(ProductStatusChangeReasonCategory.DISCARDED);
		Assert.assertNotNull("There are DISCARDED ProductStatusChangeReason", all);
		Assert.assertEquals("There are 6 DISCARDED ProductStatusChangeReason", 6, all.size());
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChangeReasonsNone() throws Exception {
		List<ProductStatusChangeReason> all = productStatusChangeReasonRepository
		        .getProductStatusChangeReasons(ProductStatusChangeReasonCategory.ISSUED);
		Assert.assertNotNull("Doesn't return a null list", all);
		Assert.assertEquals("There are 0 ISSUED ProductStatusChangeReason", 0, all.size());
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChangeReasonById() throws Exception {
		ProductStatusChangeReason one = productStatusChangeReasonRepository.getProductStatusChangeReasonById(1);
		Assert.assertNotNull("There is a ProductStatusChangeReason", one);
		Assert.assertEquals("ProductStatusChangeReason matches", ProductStatusChangeReasonCategory.DISCARDED,
		    one.getCategory());
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChangeReasonByIdUnknown() throws Exception {
		ProductStatusChangeReason one = productStatusChangeReasonRepository.getProductStatusChangeReasonById(123);
		Assert.assertNull("There is no ProductStatusChangeReason", one);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - getAllProductStatusChangeReasonsAsMap only returns one ProductStatusChangeReason per ProductStatusChangeReasonCategory")
	public void testGetAllProductStatusChangeReasonsAsMap() throws Exception {
		Map<ProductStatusChangeReasonCategory, ProductStatusChangeReason> result = productStatusChangeReasonRepository
		        .getAllProductStatusChangeReasonsAsMap();
		Assert.assertNotNull("Does not return a null map", result);
		Assert.assertEquals("There are 3 ProductStatusChangeReasonCategory with ProductStatusChangeReason", 3, result.size());
	}
}
