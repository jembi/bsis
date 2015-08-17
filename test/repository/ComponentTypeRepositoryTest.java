package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;

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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the ComponentTypeRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class ComponentTypeRepositoryTest {
	
	@Autowired
	ComponentTypeRepository componentTypeRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ComponentTypeRepositoryDataset.xml");
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
	@Transactional
	public void testGetAll() throws Exception {
		List<ComponentType> all = componentTypeRepository.getAllComponentTypes();
		Assert.assertNotNull("There are ComponentTypes", all);
		Assert.assertEquals("There are 16 ComponentTypes", 16, all.size());
	}
	
	@Test
	@Transactional
	public void testGetAllComponentTypesIncludeDeleted() throws Exception {
		List<ComponentType> all = componentTypeRepository.getAllComponentTypesIncludeDeleted();
		Assert.assertNotNull("There are ComponentTypes", all);
		Assert.assertEquals("There are 17 ComponentTypes (including deleted)", 17, all.size());
	}
	
	@Test
	@Transactional
	public void testIsComponentTypeValidTrue() throws Exception {
		boolean valid = componentTypeRepository.isComponentTypeValid("Whole Blood Single Pack - CPDA");
		Assert.assertTrue("Is a valid ComponentType", valid);
	}
	
	@Test
	@Transactional
	public void testIsComponentTypeValidFalse() throws Exception {
		boolean valid = componentTypeRepository.isComponentTypeValid("Test");
		Assert.assertFalse("Is not a valid ComponentType", valid);
	}
	
	@Test
	@Transactional
	public void testIsComponentTypeValidDeleted() throws Exception {
		boolean valid = componentTypeRepository.isComponentTypeValid("Ignore me");
		Assert.assertFalse("Is not a valid ComponentType", valid);
	}
	
	@Test
	@Transactional
	public void testGetComponentTypeById() throws Exception {
		ComponentType one = componentTypeRepository.getComponentTypeById(1);
		Assert.assertNotNull("There is a ComponentType", one);
		Assert.assertEquals("ComponentType matches", "Whole Blood Single Pack - CPDA", one.getComponentTypeName());
	}
	
	@Test
	@Transactional
	public void testGetComponentTypeByIdUnknown() throws Exception {
		ComponentType one = componentTypeRepository.getComponentTypeById(123);
		Assert.assertNull("There is no ComponentType", one);
	}
	
	@Test
	@Transactional
	@Ignore("Bug in HSQL:  org.hibernate.QueryException: could not resolve property: componentType of: model.componenttype.ComponentType [SELECT p FROM model.componenttype.ComponentType p where p.componentType = :componentTypeName]")
	public void testGetComponentTypeByName() throws Exception {
		ComponentType one = componentTypeRepository.getComponentTypeByName("Whole Blood Single Pack - CPDA");
		Assert.assertNotNull("There is a ComponentType", one);
		Assert.assertEquals("ComponentType matches", new Integer(1), one.getId());
	}
	
	@Test
	@Transactional
	public void testDeactivateComponentType() throws Exception {
		componentTypeRepository.deactivateComponentType(1);
		ComponentType componentType = componentTypeRepository.getComponentTypeById(1); // includes deleted
		Assert.assertNotNull("ComponentType is found", componentType);
		Assert.assertTrue("ComponentType is deleted", componentType.getIsDeleted());
	}
	
	@Test
	@Transactional
	public void testActivateComponentType() throws Exception {
		componentTypeRepository.activateComponentType(17);
		ComponentType componentType = componentTypeRepository.getComponentTypeById(17);
		Assert.assertNotNull("ComponentType is found", componentType);
		Assert.assertFalse("ComponentType is not deleted", componentType.getIsDeleted());
	}
	
	@Test
	@Transactional
	public void testSaveComponentType() throws Exception {
		ComponentType componentType = new ComponentType();
		componentType.setComponentTypeName("Junit");
		componentType.setComponentTypeNameShort("j");
		componentType.setExpiresAfter(1);
		componentType.setExpiresAfterUnits(ComponentTypeTimeUnits.DAYS);
		componentType.setHasBloodGroup(true);
		componentType.setIsDeleted(false);
		ComponentType savedComponentType = componentTypeRepository.saveComponentType(componentType);
		Assert.assertNotNull("ComponentType id has been set", savedComponentType.getId());
		ComponentType retrievedComponentType = componentTypeRepository.getComponentTypeById(savedComponentType.getId());
		Assert.assertNotNull("ComponentType has been saved", retrievedComponentType);
		Assert.assertEquals("ComponentType has been saved", "Junit", retrievedComponentType.getComponentTypeName());
	}
	
	@Test
	@Transactional
	public void testUpdateComponentType() throws Exception {
		ComponentType existingComponentType = componentTypeRepository.getComponentTypeById(1);
		existingComponentType.setDescription("Junit");
		componentTypeRepository.updateComponentType(existingComponentType);
		ComponentType updatedComponentType = componentTypeRepository.getComponentTypeById(1);
		Assert.assertEquals("Description has been updated", "Junit", updatedComponentType.getDescription());
	}
	
	@Test
	@Transactional
	// FIXME: I am not so sure this method is working as expected as it returns the pediComponentType not the parent and
	// it will fail with a NPE if the ComponentType with id=1 does not have a pediComponentType defined.
	public void testGetAllParentComponentTypes() throws Exception {
		List<ComponentType> all = componentTypeRepository.getAllParentComponentTypes();
		Assert.assertNotNull("There are parent ComponentTypes", all);
		Assert.assertEquals("There is 1 ComponentTypes", 1, all.size());
	}
	
	@Test
	@Transactional
	//FIXME: it's not very clear what this method does
	public void testGetComponentTypeByIdList() throws Exception {
		List<ComponentType> all = componentTypeRepository.getComponentTypeByIdList(1);
		Assert.assertNotNull("There are ComponentTypes", all);
		Assert.assertEquals("There is 1 Pedi ComponentTypes", 1, all.size());
	}
	
	@Test
	@Transactional
	public void testGetAllComponentTypeCombinations() throws Exception {
		List<ComponentTypeCombination> all = componentTypeRepository.getAllComponentTypeCombinations();
		Assert.assertNotNull("There are ComponentTypeCombination", all);
		Assert.assertEquals("There are 10 ComponentTypeCombination", 10, all.size());
	}
	
	@Test
	@Transactional
	public void testGetAllComponentTypeCombinationsIncludeDeleted() throws Exception {
		List<ComponentTypeCombination> all = componentTypeRepository.getAllComponentTypeCombinationsIncludeDeleted();
		Assert.assertNotNull("There are ComponentTypeCombination", all);
		Assert.assertEquals("There are 11 ComponentTypeCombination incl. deleted", 11, all.size());
	}
	
	@Test
	@Transactional
	public void testGetComponentTypeCombinationById() throws Exception {
		ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(1);
		Assert.assertNotNull("There is a ComponentTypeCombination", one);
		Assert.assertEquals("The ComponentTypeCombination matches", "Whole Blood", one.getCombinationName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	@Transactional
	public void testGetComponentTypeCombinationByIdUnknown() throws Exception {
		componentTypeRepository.getComponentTypeCombinationById(123);
	}
	
	@Test
	@Transactional
	public void testDeactivateComponentTypeCombination() throws Exception {
		componentTypeRepository.deactivateComponentTypeCombination(1);
		ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(1); // returns deleted entities
		Assert.assertNotNull("There is a ComponentTypeCombination", one);
		Assert.assertTrue("The ComponentTypeCombination is deleted", one.getIsDeleted());
	}
	
	@Test
	@Transactional
	public void testActivateComponentTypeCombinationUnknown() throws Exception {
		componentTypeRepository.activateComponentTypeCombination(11);
		ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(11);
		Assert.assertNotNull("There is a ComponentTypeCombination", one);
		Assert.assertFalse("The ComponentTypeCombination is not deleted", one.getIsDeleted());
	}
	
	@Test
	@Transactional
	public void testUpdateComponentTypeCombination() throws Exception {
		ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(1);
		one.setCombinationName("Testing");
		componentTypeRepository.updateComponentTypeCombination(one);
		ComponentTypeCombination savedOne = componentTypeRepository.getComponentTypeCombinationById(1);
		Assert.assertEquals("ComponentTypeCombination was saved successfully", "Testing", savedOne.getCombinationName());
	}
	
	@Test
	@Transactional
	public void testSaveComponentTypeCombination() throws Exception {
		ComponentTypeCombination one = new ComponentTypeCombination();
		one.setCombinationName("Testing");
		one.setIsDeleted(false);
		List<ComponentType> componentTypes = new ArrayList<ComponentType>();
		componentTypes.add(componentTypeRepository.getComponentTypeById(1));
		one.setComponentTypes(componentTypes);
		componentTypeRepository.saveComponentTypeCombination(one);
		List<ComponentTypeCombination> all = componentTypeRepository.getAllComponentTypeCombinations();
		boolean found = false;
		for (ComponentTypeCombination ptc : all) {
			if (ptc.getCombinationName().equals("Testing")) {
				Assert.assertNotNull("ComponentTypes were stored correctly", ptc.getComponentTypes());
				Assert.assertEquals("ComponentTypes were stored correctly", 1, ptc.getComponentTypes().size());
				Assert.assertEquals("ComponentTypes were stored correctly", "Whole Blood Single Pack - CPDA", ptc
				        .getComponentTypes().get(0).getComponentTypeName());
				found = true;
				break;
			}
		}
		Assert.assertTrue("ComponentTypeCombination was saved successfully", found);
	}
}
