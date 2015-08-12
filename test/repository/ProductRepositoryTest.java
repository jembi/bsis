package repository;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.donation.Donation;
import model.product.Product;
import model.product.ProductStatus;
import model.productmovement.ProductStatusChange;
import model.productmovement.ProductStatusChangeReason;
import model.productmovement.ProductStatusChangeType;
import model.producttype.ProductType;

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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import security.BsisUserDetails;

/**
 * Test using DBUnit to test the ProductRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class ProductRepositoryTest {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	DonationRepository donationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProductStatusChangeReasonRepository productStatusChangeReasonRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ProductRepositoryDataset.xml");
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
			initSpringSecurityUser(); // must be done after data has been inserted successfully
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
	
	public void initSpringSecurityUser() throws Exception {
		BsisUserDetails user = new BsisUserDetails(userRepository.findUserById(1));
		TestingAuthenticationToken auth = new TestingAuthenticationToken(user, "Credentials");
		SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	// TODO: test the following methods
	//findMatchingProductsForRequest
	//findNumberOfIssuedProducts
	//addProductCombination
	
	@Test
	@Transactional
	public void testGetAllProducts() throws Exception {
		List<Product> all = productRepository.getAllProducts();
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 7 Products", 7, all.size());
	}
	
	@Test
	@Transactional
	public void testGetProducts() throws Exception {
		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<Product> all = productRepository.getProducts(fromDate, toDate);
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 7 Products", 7, all.size());
	}
	
	@Test
	@Transactional
	public void testGetProductsFromProductIds() throws Exception {
		String[] productIds = new String[] { "1", "2", "4" };
		List<Product> all = productRepository.getProductsFromProductIds(productIds);
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 3 Products", 3, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - problem in HSQL: could not resolve property: donationIdentificationNumber of: model.product.Product [SELECT p FROM model.product.Product p WHERE p.donationIdentificationNumber = :donationIdentificationNumber and p.isDeleted = :isDeleted]")
	public void isProductCreatedFalse() throws Exception {
		boolean created = productRepository.isProductCreated("4444444");
		Assert.assertFalse("Product not created", created);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - problem in HSQL: could not resolve property: donationIdentificationNumber of: model.product.Product [SELECT p FROM model.product.Product p WHERE p.donationIdentificationNumber = :donationIdentificationNumber and p.isDeleted = :isDeleted]")
	public void isProductCreatedTrue() throws Exception {
		boolean created = productRepository.isProductCreated("3333333");
		Assert.assertTrue("Product created", created);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.product.Product [SELECT p FROM model.product.Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued]")
	public void testGetAllUnissuedProducts() throws Exception {
		List<Product> all = productRepository.getAllUnissuedProducts();
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 3 Products", 3, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: type of: model.product.Product [SELECT p FROM model.product.Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued]")
	public void testGetAllUnissuedProductsAboRhd() throws Exception {
		List<Product> all = productRepository.getAllUnissuedProducts("PROCESSED", "A", "+");
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 0 Products", 0, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: isIssued of: model.product.Product [SELECT p FROM model.product.Product p where p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate]")
	public void testGetAllUnissuedThirtyFiveDayProducts() throws Exception {
		List<Product> all = productRepository.getAllUnissuedThirtyFiveDayProducts();
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 0 Products", 0, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HQL: could not resolve property: type of: model.product.Product [SELECT p FROM model.product.Product p where p.type = :productType and p.abo= :abo and p.rhd= :rhd and p.isDeleted = :isDeleted and p.isIssued= :isIssued and p.createdOn > :minDate]")
	public void testGetAllUnissuedThirtyFiveDayProductsWithParameters() throws Exception {
		List<Product> all = productRepository.getAllUnissuedThirtyFiveDayProducts("PROCESSED", "A", "+");
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 0 Products", 0, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("There appears to be a bug in the HQL: could not resolve property: type of: model.product.Product")
	public void testGetAllProductsWithProductTypeQUARANTINED() throws Exception {
		List<Product> all = productRepository.getAllProducts("QUARANTINED");
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 3 Products", 3, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("There appears to be a bug in the HQL: could not resolve property: type of: model.product.Product")
	public void testGetAllProductsWithProductTypeEXPIRED() throws Exception {
		List<Product> all = productRepository.getAllProducts("EXPIRED");
		Assert.assertNotNull("There are Products", all);
		Assert.assertEquals("There are 0 Products", 0, all.size());
	}
	
	@Test
	@Transactional
	public void testFindProductWithId() throws Exception {
		Product one = productRepository.findProduct(1l);
		Assert.assertNotNull("There is a Product with id 1", one);
		Assert.assertEquals("Product is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testFindProductById() throws Exception {
		Product one = productRepository.findProductById(1l);
		Assert.assertNotNull("There is a Product with id 1", one);
		Assert.assertEquals("Product is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testFindProductByIdUnknown() throws Exception {
		Product one = productRepository.findProduct(1111l);
		Assert.assertNull("There is no Product with id 1111", one);
	}
	
	@Test
	@Transactional
	@Ignore("A bug - there is no attribute of Product called productNumber")
	public void testFindProductByProductNumber() throws Exception {
		productRepository.findProduct("123");
	}
	
	@Test
	@Transactional
	public void testFindProductsByDIN() throws Exception {
		List<Product> all = productRepository.findProductsByDonationIdentificationNumber("1111111");
		Assert.assertNotNull("There are Products with DIN 111111", all);
		Assert.assertFalse("There are Products with DIN 111111", all.isEmpty());
		Assert.assertEquals("There are 2 Products with DIN 1111111", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testFindProductsByDINUnknown() throws Exception {
		List<Product> all = productRepository.findProductsByDonationIdentificationNumber("1111112");
		Assert.assertNotNull("Does not return an null", all);
		Assert.assertTrue("There are 0 Products with DIN 111112", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindProductByDINAndProductTypeId() throws Exception {
		Product one = productRepository.findProduct("1111111", "1");
		Assert.assertNotNull("There is a Product with DIN 1111111", one);
		Assert.assertEquals("Product is linked to the correct Donation", "1111111", one.getDonationIdentificationNumber());
	}
	
	@Test
	@Transactional
	public void testFindProductByDINAndProductTypeIdUnknown() throws Exception {
		Product one = productRepository.findProduct("1111112", "1");
		Assert.assertNull("There is no a Product with DIN 1111112", one);
	}
	
	@Test
	@Transactional
	public void testFindProductByProductTypes() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.PROCESSED);
		List<Integer> productTypeIds = new ArrayList<Integer>();
		productTypeIds.add(1);
		List<Product> all = productRepository.findProductByProductTypes(productTypeIds, status, pagingParams);
		Assert.assertNotNull("Does not return an null", all);
		Assert.assertEquals("There are 3 'Whole Blood Single Pack - CPDA' Products", 3, all.size());
	}
	
	@Test
	@Transactional
	public void testFindProductByProductTypesNone() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		List<Integer> productTypeIds = new ArrayList<Integer>();
		productTypeIds.add(1);
		List<Product> all = productRepository.findProductByProductTypes(productTypeIds, status, pagingParams);
		Assert.assertNotNull("Does not return a null list", all);
		Assert.assertTrue("There are no Products", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindProductByDINAndStatus() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		status.add(ProductStatus.PROCESSED);
		List<Product> all = productRepository.findProductByDonationIdentificationNumber("1111111", status, pagingParams);
		Assert.assertNotNull("There is a Product with DIN 1111111", all);
		Assert.assertFalse("There is a Product with DIN 1111111", all.isEmpty());
		Assert.assertEquals("There should be two products", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testFindProductByDINAndStatusNone() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.EXPIRED);
		List<Product> all = productRepository.findProductByDonationIdentificationNumber("1111111", status, pagingParams);
		Assert.assertTrue("There should be 0 products", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindProductByDINAndStatusUnknown() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		status.add(ProductStatus.PROCESSED);
		List<Product> all = productRepository.findProductByDonationIdentificationNumber("1111112", status, pagingParams);
		Assert.assertTrue("There should be 0 products", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindAnyProductDIN() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<Product> all = productRepository.findAnyProduct("1111111", null, null, null, null, pagingParams);
		Assert.assertNotNull("There is a Product with DIN 1111111", all);
		Assert.assertFalse("There is a Product with DIN 1111111", all.isEmpty());
		Assert.assertEquals("There should be 2 products", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testFindAnyProductDINAndStatus() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		List<Product> all = productRepository.findAnyProduct("1111111", null, status, null, null, pagingParams);
		Assert.assertNotNull("There are matching products", all);
		Assert.assertEquals("There should be 1 products", 1, all.size());
	}
	
	@Test
	@Transactional
	public void testFindAnyProductQuarantinedType1() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		List<ProductStatus> status = new ArrayList<ProductStatus>();
		status.add(ProductStatus.QUARANTINED);
		List<Integer> productTypeIds = new ArrayList<Integer>();
		productTypeIds.add(1);
		List<Product> all = productRepository.findAnyProduct(null, productTypeIds, status, null, null, pagingParams);
		Assert.assertNotNull("There aren't matching products", all);
		Assert.assertTrue("There should be 0 products", all.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindAnyProductBetweenDates() throws Exception {
		Map<String, Object> pagingParams = new HashMap<String, Object>();
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<Product> all = productRepository.findAnyProduct(null, null, null, start, end, pagingParams);
		Assert.assertNotNull("There are matching products", all);
		Assert.assertEquals("There should be 7 products", 7, all.size());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in the HQL: could not resolve property: productType of: model.producttype.ProductType [SELECT p FROM model.producttype.ProductType p where p.productType = :productTypeName]")
	public void testFindProductTypeByProductTypeName() throws Exception {
		ProductType one = productRepository.findProductTypeByProductTypeName("Whole Blood Single Pack - CPDA");
		Assert.assertNotNull("ProductType match", one);
		Assert.assertEquals("ProductType is correct", "0011", one.getProductTypeNameShort());
	}
	
	@Test
	@Transactional
	public void testFindProductTypeByProductTypeId() throws Exception {
		ProductType one = productRepository.findProductTypeBySelectedProductType(1);
		Assert.assertNotNull("ProductType match", one);
		Assert.assertEquals("ProductType is correct", "Whole Blood Single Pack - CPDA", one.getProductTypeName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	@Transactional
	public void testFindProductTypeByProductTypeIdUnknown() throws Exception {
		productRepository.findProductTypeBySelectedProductType(123);
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChanges() throws Exception {
		Product discardedProduct = productRepository.findProduct(6l);
		List<ProductStatusChange> changes = productRepository.getProductStatusChanges(discardedProduct);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Product was DISCARDED", "DISCARDED", changes.get(0).getNewStatus().toString());
	}
	
	@Test
	@Transactional
	public void testGetProductStatusChangesNone() throws Exception {
		Product quarantinedProduct = productRepository.findProduct(4l);
		List<ProductStatusChange> changes = productRepository.getProductStatusChanges(quarantinedProduct);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertTrue("0 ProductStatusChange", changes.isEmpty());
	}
	
	@Test
	@Transactional
	public void testFindNumberOfDiscardedProductsDaily() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> panels = new ArrayList<String>();
		panels.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB+");
		bloodGroups.add("AB-");
		Map<String, Map<Long, Long>> results = productRepository.findNumberOfDiscardedProducts(donationDateFrom,
		    donationDateTo, "daily", panels, bloodGroups);
		Assert.assertEquals("2 blood types searched", 2, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("11/08/2015");
		Map<Long, Long> abPlusResults = results.get("AB+");
		Assert.assertEquals("3 days in the searched period", 3, abPlusResults.size());
		Long abPlus11thResults = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ discarded donations", new Long(1), abPlus11thResults);
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinus11thResults = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations", new Long(0), abMinus11thResults);
	}
	
	@Test
	@Transactional
	public void testFindNumberOfDiscardedProductsMonthly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> panels = new ArrayList<String>();
		panels.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB+");
		bloodGroups.add("AB-");
		bloodGroups.add("O-");
		bloodGroups.add("O+");
		Map<String, Map<Long, Long>> results = productRepository.findNumberOfDiscardedProducts(donationDateFrom,
		    donationDateTo, "monthly", panels, bloodGroups);
		Assert.assertEquals("4 blood types searched", 4, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/08/2015");
		Map<Long, Long> abPlusResults = results.get("AB+");
		Assert.assertEquals("1 month in the searched period", 1, abPlusResults.size());
		Long abPlusAugResults = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ discarded donations", new Long(1), abPlusAugResults);
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinusAugResults = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations", new Long(0), abMinusAugResults);
	}
	
	@Test
	@Transactional
	public void testFindNumberOfDiscardedProductsYearly() throws Exception {
		Date donationDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2014-08-10");
		Date donationDateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2015-08-12");
		List<String> panels = new ArrayList<String>();
		panels.add("1");
		List<String> bloodGroups = new ArrayList<String>();
		bloodGroups.add("AB-");
		bloodGroups.add("AB+");
		Map<String, Map<Long, Long>> results = productRepository.findNumberOfDiscardedProducts(donationDateFrom,
		    donationDateTo, "yearly", panels, bloodGroups);
		Assert.assertEquals("2 blood types searched", 2, results.size());
		Date formattedDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015");
		Map<Long, Long> abPlusResults = results.get("AB+");
		Assert.assertEquals("2 year in the searched period", 2, abPlusResults.size());
		Long abPlus2015Results = abPlusResults.get(formattedDate.getTime());
		Assert.assertEquals("1 AB+ discarded donations", new Long(1), abPlus2015Results);
		Map<Long, Long> abMinusResults = results.get("AB-");
		Long abMinus2015Results = abMinusResults.get(formattedDate.getTime());
		Assert.assertEquals("0 AB- donations", new Long(0), abMinus2015Results);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	// FIXME: issue with package dependencies here - ProductRepository uses UtilController to retrieve the logged in user.  
	public void testDiscardProduct() throws Exception {
		ProductStatusChangeReason discardReason = productStatusChangeReasonRepository.getProductStatusChangeReasonById(5);
		productRepository.discardProduct(1l, discardReason, "junit");
		Product product = productRepository.findProduct(1l);
		List<ProductStatusChange> changes = productRepository.getProductStatusChanges(product);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ProductStatusChangeType", ProductStatusChangeType.DISCARDED, changes.get(0).getStatusChangeType());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	// FIXME: issue with package dependencies here - ProductRepository uses UtilController to retrieve the logged in user.  
	public void testReturnProduct() throws Exception {
		ProductStatusChangeReason returnReason = productStatusChangeReasonRepository.getProductStatusChangeReasonById(7);
		productRepository.returnProduct(1l, returnReason, "junit");
		Product product = productRepository.findProduct(1l);
		List<ProductStatusChange> changes = productRepository.getProductStatusChanges(product);
		Assert.assertNotNull("Not empty list", changes);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ProductStatusChangeType", ProductStatusChangeType.RETURNED, changes.get(0).getStatusChangeType());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	// FIXME: issue with package dependencies here - ProductRepository uses UtilController to retrieve the logged in user.  
	public void testSplitProduct() throws Exception {
		boolean split = productRepository.splitProduct(2l, 2);
		Assert.assertTrue("Split was successful", split);
		Product product = productRepository.findProduct(2l);
		Assert.assertEquals("Status is changed to SPLIT", ProductStatus.SPLIT, product.getStatus());
		List<ProductStatusChange> changes = productRepository.getProductStatusChanges(product);
		Assert.assertEquals("1 ProductStatusChange", 1, changes.size());
		Assert.assertEquals("Correct ProductStatusChangeType", ProductStatusChangeType.SPLIT, changes.get(0).getStatusChangeType());
		List<Product> products = productRepository.findProductsByDonationIdentificationNumber("1111111");
		// FIXME: I'm not sure that numProductsAfterSplitting is correctly named - expected only 1 extra product to be created after the split
		Assert.assertEquals("4 products after split",  4, products.size());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testSplitProductTwice() throws Exception {
		productRepository.splitProduct(2l, 1);
		List<Product> products = productRepository.findProductsByDonationIdentificationNumber("1111111");
		for (Product p : products) {
			if (p.getStatus() == ProductStatus.SPLIT) {
				boolean split = productRepository.splitProduct(p.getId(), 1);
				Assert.assertFalse("Cannot split a product that has already been split", split);
			}
		}
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testSplitProductUnknown() throws Exception {
		boolean split = productRepository.splitProduct(123l, 2);
		Assert.assertFalse("Unknown product", split);
	}

	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testUpdateProduct() throws Exception {
		Product productToUpdate = productRepository.findProduct(2l);
		productToUpdate.setComponentIdentificationNumber("junit123");
		productRepository.updateProduct(productToUpdate);
		Product updatedProduct = productRepository.findProduct(2l);
		Assert.assertEquals("Product has been updated", "junit123", updatedProduct.getComponentIdentificationNumber());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testUpdateExpiryStatus() throws Exception {
		// create expirable product
		Product productToExpire = productRepository.findProduct(2l);
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DAY_OF_YEAR, -3);
		productToExpire.setExpiresOn(today.getTime());
		productToExpire.setStatus(ProductStatus.AVAILABLE);
		Product expiringProduct = productRepository.updateProduct(productToExpire);
		// run test
		productRepository.updateExpiryStatus();
		// check product has been expired
		Product expiredProduct = productRepository.findProduct(expiringProduct.getId());
		Assert.assertEquals("Product has been expired", ProductStatus.EXPIRED, expiredProduct.getStatus());
	}

	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testSetProductStatusToProcessed() throws Exception {
		productRepository.setProductStatusToProcessed(2l);
		Product processedProduct = productRepository.findProduct(2l);
		Assert.assertEquals("Product has been processed", ProductStatus.PROCESSED, processedProduct.getStatus());
	}
	
	@Test
	@Transactional
	@Ignore("It seems impossible to get products in a NULL status - by default it is set to QUARANTINED")
	public void testUpdateQuarantineStatus() throws Exception {
		// create product with null status
		Product newProduct = new Product();
		Donation newDonation = new Donation();
		Product existingProduct = productRepository.findProduct(1l);
		newProduct.copy(existingProduct);
		newDonation.copy(existingProduct.getDonation());
		newDonation.setDonationIdentificationNumber("7654321");
		Calendar today = Calendar.getInstance();
		newDonation.setCreatedDate(today.getTime());
		newDonation.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation);
		newProduct.setDonation(newDonation);
		newProduct.setStatus(null);
		Product savedProduct = productRepository.addProduct(newProduct);
		savedProduct = productRepository.findProduct(savedProduct.getId());
		Assert.assertNull("Cleared status", savedProduct.getStatus()); // check that status is null before test
		// run test
		productRepository.updateQuarantineStatus();
		// assert that the test was successful
		Product processedProduct = productRepository.findProduct(savedProduct.getId());
		Assert.assertEquals("Product has been QUARANTINED", ProductStatus.QUARANTINED, processedProduct.getStatus());
	}

	@Test
	@Transactional
	public void testUpdateProductInternalFieldsProcessed() throws Exception {
		Product product = productRepository.findProduct(1l);
		boolean updatedProduct = productRepository.updateProductInternalFields(product);
		Assert.assertFalse("PROCESSED product is not updated", updatedProduct);
	}
	
	@Test
	@Transactional
	public void testUpdateProductInternalFieldsDiscarded() throws Exception {
		Product product = productRepository.findProduct(6l);
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertFalse("DISCARDED product is not updated", updatedProductStatus);
	}

	@Test
	@Transactional
	public void testUpdateProductInternalFieldsIssued() throws Exception {
		// setup test
		Product product = productRepository.findProduct(1l);
		product.setStatus(ProductStatus.ISSUED);
		// run test
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertFalse("ISSUED product is not updated", updatedProductStatus);
	}
	
	@Test
	@Transactional
	public void testUpdateProductInternalFieldsUsed() throws Exception {
		// setup test
		Product product = productRepository.findProduct(1l);
		product.setStatus(ProductStatus.USED);
		// run test
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertFalse("USED product is not updated", updatedProductStatus);
	}

	@Test
	@Transactional
	public void testUpdateProductInternalFieldsSplit() throws Exception {
		// setup test
		Product product = productRepository.findProduct(1l);
		product.setStatus(ProductStatus.SPLIT);
		// run test
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertFalse("SPLIT product is not updated", updatedProductStatus);
	}

	@Test
	@Transactional
	public void testUpdateProductInternalFieldsQuarantined() throws Exception {
		// setup test
		Product product = productRepository.findProduct(4l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -10);
		product.setExpiresOn(cal.getTime());
		// run test
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertTrue("QUARANTINED product status is changed", updatedProductStatus);
		Assert.assertEquals("Product status is actually EXPIRED", ProductStatus.EXPIRED, product.getStatus());
	}
	
	@Test
	@Transactional
	public void testUpdateProductInternalFieldsSafe() throws Exception {
		// setup test
		Product product = productRepository.findProduct(4l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 10);
		product.setExpiresOn(cal.getTime());
		// run test
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertTrue("SAFE product status is changed", updatedProductStatus);
		Assert.assertEquals("Product status is actually AVAILABLE", ProductStatus.AVAILABLE, product.getStatus());
	}

	@Test
	@Transactional
	public void testUpdateProductInternalFieldsUnSafe() throws Exception {
		// setup test
		Product product = productRepository.findProduct(7l);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 10);
		product.setExpiresOn(cal.getTime());
		// run test
		boolean updatedProductStatus = productRepository.updateProductInternalFields(product);
		Assert.assertTrue("UNSAFE product status is changed", updatedProductStatus);
		Assert.assertEquals("Product status is actually UNSAFE", ProductStatus.UNSAFE, product.getStatus());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - this won't work if there are foreign key references such as ProductStatusChange entries...")
	public void testDeleteAllProducts() throws Exception {
		productRepository.deleteAllProducts();
		List<Product> all = productRepository.getAllProducts();
		Assert.assertNotNull("Doesn't return null object", all);
		Assert.assertTrue("No products left", all.isEmpty());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testDeleteProduct() throws Exception {
		productRepository.deleteProduct(1l);
		Product deletedProduct = productRepository.findProduct(1l);
		Assert.assertNotNull("Product isn't actually deleted", deletedProduct);
		Assert.assertTrue("Product is marked as isDeleted", deletedProduct.getIsDeleted());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testAddProduct() throws Exception {
		Product newProduct = new Product();
		Donation newDonation = new Donation();
		Product existingProduct = productRepository.findProduct(1l);
		newProduct.copy(existingProduct);
		newDonation.copy(existingProduct.getDonation());
		newDonation.setDonationIdentificationNumber("7654321");
		Calendar today = Calendar.getInstance();
		newDonation.setCreatedDate(today.getTime());
		newDonation.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation);
		newProduct.setDonation(newDonation);
		productRepository.addProduct(newProduct);
		List<Product> products = productRepository.findProductsByDonationIdentificationNumber("7654321");
		Assert.assertEquals("A new product was added", 1, products.size());
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testAddAllProducts() throws Exception {
		Product newProduct1 = new Product();
		Donation newDonation1 = new Donation();
		Product existingProduct1 = productRepository.findProduct(1l);
		newProduct1.copy(existingProduct1);
		newDonation1.copy(existingProduct1.getDonation());
		newDonation1.setDonationIdentificationNumber("7654321");
		Calendar today = Calendar.getInstance();
		newDonation1.setCreatedDate(today.getTime());
		newDonation1.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation1.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation1);
		newProduct1.setDonation(newDonation1);
		
		Product newProduct2 = new Product();
		Donation newDonation2 = new Donation();
		Product existingProduct2 = productRepository.findProduct(1l);
		newProduct2.copy(existingProduct2);
		newDonation2.copy(existingProduct2.getDonation());
		newDonation2.setDonationIdentificationNumber("7654320");
		newDonation2.setCreatedDate(today.getTime());
		newDonation2.setBleedEndTime(today.getTime());
		today.add(Calendar.MINUTE, -15);
		newDonation2.setBleedStartTime(today.getTime());
		donationRepository.addDonation(newDonation2);
		newProduct2.setDonation(newDonation2);
		
		List<Product> newProducts = new ArrayList<Product>();
		newProducts.add(newProduct1);
		newProducts.add(newProduct2);
		productRepository.addAllProducts(newProducts);
		
		List<Product> products1 = productRepository.findProductsByDonationIdentificationNumber("7654321");
		Assert.assertEquals("A new product was added", 1, products1.size());
		List<Product> products2 = productRepository.findProductsByDonationIdentificationNumber("7654320");
		Assert.assertEquals("A new product was added", 1, products2.size());
	}
}
