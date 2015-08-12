package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.producttype.ProductType;
import model.producttype.ProductTypeCombination;
import model.producttype.ProductTypeTimeUnits;

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
 * Test using DBUnit to test the ProductTypeRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class ProductTypeRepositoryTest {
	
	@Autowired
	ProductTypeRepository productTypeRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ProductTypeRepositoryDataset.xml");
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
		List<ProductType> all = productTypeRepository.getAllProductTypes();
		Assert.assertNotNull("There are ProductTypes", all);
		Assert.assertEquals("There are 16 ProductTypes", 16, all.size());
	}
	
	@Test
	@Transactional
	public void testGetAllProductTypesIncludeDeleted() throws Exception {
		List<ProductType> all = productTypeRepository.getAllProductTypesIncludeDeleted();
		Assert.assertNotNull("There are ProductTypes", all);
		Assert.assertEquals("There are 17 ProductTypes (including deleted)", 17, all.size());
	}
	
	@Test
	@Transactional
	public void testIsProductTypeValidTrue() throws Exception {
		boolean valid = productTypeRepository.isProductTypeValid("Whole Blood Single Pack - CPDA");
		Assert.assertTrue("Is a valid ProductType", valid);
	}
	
	@Test
	@Transactional
	public void testIsProductTypeValidFalse() throws Exception {
		boolean valid = productTypeRepository.isProductTypeValid("Test");
		Assert.assertFalse("Is not a valid ProductType", valid);
	}
	
	@Test
	@Transactional
	public void testIsProductTypeValidDeleted() throws Exception {
		boolean valid = productTypeRepository.isProductTypeValid("Ignore me");
		Assert.assertFalse("Is not a valid ProductType", valid);
	}
	
	@Test
	@Transactional
	public void testGetProductTypeById() throws Exception {
		ProductType one = productTypeRepository.getProductTypeById(1);
		Assert.assertNotNull("There is a ProductType", one);
		Assert.assertEquals("ProductType matches", "Whole Blood Single Pack - CPDA", one.getProductTypeName());
	}
	
	@Test
	@Transactional
	public void testGetProductTypeByIdUnknown() throws Exception {
		ProductType one = productTypeRepository.getProductTypeById(123);
		Assert.assertNull("There is no ProductType", one);
	}
	
	@Test
	@Transactional
	@Ignore("Bug in HSQL: could not resolve property: productType of: model.producttype.ProductType [SELECT pt from model.producttype.ProductType pt where pt.productType=:productTypeName]")
	public void testGetProductTypeByName() throws Exception {
		ProductType one = productTypeRepository.getProductTypeByName("Whole Blood Single Pack - CPDA");
		Assert.assertNotNull("There is a ProductType", one);
		Assert.assertEquals("ProductType matches", new Integer(1), one.getId());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testDeactivateProductType() throws Exception {
		productTypeRepository.deactivateProductType(1);
		ProductType productType = productTypeRepository.getProductTypeById(1); // includes deleted
		Assert.assertNotNull("ProductType is found", productType);
		Assert.assertTrue("ProductType is deleted", productType.getIsDeleted());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testActivateProductType() throws Exception {
		productTypeRepository.activateProductType(17);
		ProductType productType = productTypeRepository.getProductTypeById(17);
		Assert.assertNotNull("ProductType is found", productType);
		Assert.assertFalse("ProductType is not deleted", productType.getIsDeleted());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testSaveComponentType() throws Exception {
		ProductType productType = new ProductType();
		productType.setProductTypeName("Junit");
		productType.setProductTypeNameShort("j");
		productType.setExpiresAfter(1);
		productType.setExpiresAfterUnits(ProductTypeTimeUnits.DAYS);
		productType.setHasBloodGroup(true);
		productType.setIsDeleted(false);
		ProductType savedProductType = productTypeRepository.saveComponentType(productType);
		Assert.assertNotNull("ProductType id has been set", savedProductType.getId());
		ProductType retrievedProductType = productTypeRepository.getProductTypeById(savedProductType.getId());
		Assert.assertNotNull("ProductType has been saved", retrievedProductType);
		Assert.assertEquals("ProductType has been saved", "Junit", retrievedProductType.getProductTypeName());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testUpdateComponentType() throws Exception {
		ProductType existingProductType = productTypeRepository.getProductTypeById(1);
		existingProductType.setDescription("Junit");
		productTypeRepository.updateComponentType(existingProductType);
		ProductType updatedProductType = productTypeRepository.getProductTypeById(1);
		Assert.assertEquals("Description has been updated", "Junit", updatedProductType.getDescription());
	}
	
	@Test
	@Transactional
	// FIXME: I am not so sure this method is working as expected as it returns the pediProductType not the parent and
	// it will fail with a NPE if the ProductType with id=1 does not have a pediProductType defined.
	public void testGetAllParentProductTypes() throws Exception {
		List<ProductType> all = productTypeRepository.getAllParentProductTypes();
		Assert.assertNotNull("There are parent ProductTypes", all);
		Assert.assertEquals("There is 1 ProductTypes", 1, all.size());
	}
	
	@Test
	@Transactional
	//FIXME: it's not very clear what this method does
	public void testGetProductTypeByIdList() throws Exception {
		List<ProductType> all = productTypeRepository.getProductTypeByIdList(1);
		Assert.assertNotNull("There are ProductTypes", all);
		Assert.assertEquals("There is 1 Pedi ProductTypes", 1, all.size());
	}
	
	@Test
	@Transactional
	public void testGetAllProductTypeCombinations() throws Exception {
		List<ProductTypeCombination> all = productTypeRepository.getAllProductTypeCombinations();
		Assert.assertNotNull("There are ProductTypeCombination", all);
		Assert.assertEquals("There are 10 ProductTypeCombination", 10, all.size());
	}
	
	@Test
	@Transactional
	public void testGetAllProductTypeCombinationsIncludeDeleted() throws Exception {
		List<ProductTypeCombination> all = productTypeRepository.getAllProductTypeCombinationsIncludeDeleted();
		Assert.assertNotNull("There are ProductTypeCombination", all);
		Assert.assertEquals("There are 11 ProductTypeCombination incl. deleted", 11, all.size());
	}
	
	@Test
	@Transactional
	public void testGetProductTypeCombinationById() throws Exception {
		ProductTypeCombination one = productTypeRepository.getProductTypeCombinationById(1);
		Assert.assertNotNull("There is a ProductTypeCombination", one);
		Assert.assertEquals("The ProductTypeCombination matches", "Whole Blood", one.getCombinationName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	@Transactional
	public void testGetProductTypeCombinationByIdUnknown() throws Exception {
		productTypeRepository.getProductTypeCombinationById(123);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testDeactivateProductTypeCombination() throws Exception {
		productTypeRepository.deactivateProductTypeCombination(1);
		ProductTypeCombination one = productTypeRepository.getProductTypeCombinationById(1); // returns deleted entities
		Assert.assertNotNull("There is a ProductTypeCombination", one);
		Assert.assertTrue("The ProductTypeCombination is deleted", one.getIsDeleted());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testActivateProductTypeCombinationUnknown() throws Exception {
		productTypeRepository.activateProductTypeCombination(11);
		ProductTypeCombination one = productTypeRepository.getProductTypeCombinationById(11);
		Assert.assertNotNull("There is a ProductTypeCombination", one);
		Assert.assertFalse("The ProductTypeCombination is not deleted", one.getIsDeleted());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testUpdateComponentTypeCombination() throws Exception {
		ProductTypeCombination one = productTypeRepository.getProductTypeCombinationById(1);
		one.setCombinationName("Testing");
		productTypeRepository.updateComponentTypeCombination(one);
		ProductTypeCombination savedOne = productTypeRepository.getProductTypeCombinationById(1);
		Assert.assertEquals("ProductTypeCombination was saved successfully", "Testing", savedOne.getCombinationName());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testSaveComponentTypeCombination() throws Exception {
		ProductTypeCombination one = new ProductTypeCombination();
		one.setCombinationName("Testing");
		one.setIsDeleted(false);
		List<ProductType> productTypes = new ArrayList<ProductType>();
		productTypes.add(productTypeRepository.getProductTypeById(1));
		one.setProductTypes(productTypes);
		productTypeRepository.saveComponentTypeCombination(one);
		List<ProductTypeCombination> all = productTypeRepository.getAllProductTypeCombinations();
		boolean found = false;
		for (ProductTypeCombination ptc : all) {
			if (ptc.getCombinationName().equals("Testing")) {
				Assert.assertNotNull("ProductTypes were stored correctly", ptc.getProductTypes());
				Assert.assertEquals("ProductTypes were stored correctly", 1, ptc.getProductTypes().size());
				Assert.assertEquals("ProductTypes were stored correctly", "Whole Blood Single Pack - CPDA", ptc
				        .getProductTypes().get(0).getProductTypeName());
				found = true;
				break;
			}
		}
		Assert.assertTrue("ProductTypeCombination was saved successfully", found);
	}
}
