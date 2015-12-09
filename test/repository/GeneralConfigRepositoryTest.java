package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.admin.GeneralConfig;

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

/**
 * Test using DBUnit to test the GeneralConfigRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class GeneralConfigRepositoryTest {
	
	@Autowired
	GeneralConfigRepository generalConfigRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/GeneralConfigRepositoryDataset.xml");
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
	public void testGetAll() throws Exception {
		List<GeneralConfig> all = generalConfigRepository.getAll();
		Assert.assertNotNull("There are GeneralConfigs", all);
		Assert.assertEquals("There are 33 GeneralConfig", 33, all.size());
	}
	
	@Test
	public void testFindGeneralConfigById() throws Exception {
		GeneralConfig one = generalConfigRepository.getGeneralConfigById(1);
		Assert.assertNotNull("There is a GeneralConfig with id 1", one);
		Assert.assertEquals("The GeneralConfig matches what was expected", "donation.bpSystolicMax", one.getName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void testFindGeneralConfigByIdUnknown() throws Exception {
		generalConfigRepository.getGeneralConfigById(1111);
	}
	
	@Test
	public void testGetGeneralConfigByName() throws Exception {
		GeneralConfig bpSystolicMax = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
		Assert.assertNotNull("There is a GeneralConfig by that name", bpSystolicMax);
		Assert.assertEquals("The GeneralConfig matches what was expected", "250", bpSystolicMax.getValue());
	}
	
	@Test
	public void testGetGeneralConfigByNameUnknown() throws Exception {
		GeneralConfig junit = generalConfigRepository.getGeneralConfigByName("junit");
		Assert.assertNull("There is no junit GeneralConfig", junit);
	}
	
	@Test
	public void testUpdate() throws Exception {
		GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
		generalConfig.setValue("255");
		generalConfigRepository.update(generalConfig);
		GeneralConfig savedGeneralConfig = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
		Assert.assertEquals("The GeneralConfig has been updated", "255", savedGeneralConfig.getValue());
	}
	
	@Test
	public void testUpdateAll() throws Exception {
		GeneralConfig generalConfig1 = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
		generalConfig1.setValue("255");
		GeneralConfig generalConfig2 = generalConfigRepository.getGeneralConfigByName("donation.bpDiastolicMax");
		generalConfig2.setValue("155");
		List<GeneralConfig> generalConfigs = new ArrayList<>();
		generalConfigs.add(generalConfig1);
		generalConfigs.add(generalConfig2);
		generalConfigRepository.updateAll(generalConfigs);
		GeneralConfig savedGeneralConfig1 = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
		Assert.assertEquals("The GeneralConfig has been updated", "255", savedGeneralConfig1.getValue());
		GeneralConfig savedGeneralConfig2 = generalConfigRepository.getGeneralConfigByName("donation.bpDiastolicMax");
		Assert.assertEquals("The GeneralConfig has been updated", "155", savedGeneralConfig2.getValue());
	}
	
	@Test
	public void testSave() throws Exception {
		GeneralConfig bpSystolicMax = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
		GeneralConfig newGeneralConfig = new GeneralConfig();
		newGeneralConfig.copy(bpSystolicMax);
		newGeneralConfig.setName("junit.save");
		generalConfigRepository.save(newGeneralConfig);
		GeneralConfig savedGeneralConfig = generalConfigRepository.getGeneralConfigByName("junit.save");
		Assert.assertNotNull("There is a new GeneralConfig", savedGeneralConfig);
		Assert.assertEquals("The new GeneralConfig is correct", "250", savedGeneralConfig.getValue());
	}
}
