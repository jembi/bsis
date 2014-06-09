/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package repository;

import java.io.File;
import javax.sql.DataSource;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static repository.DonorRepositoryTest.connection;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author srikanth
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
public class GeneralConfigurationRepositoryTest {
    
    @Autowired
    private GeneralConfigRepository configRepository;
    
     @Autowired
    private DataSource dataSource;
    
        @Before
    public void init() {
        try {
            if (connection == null) {
                getConnection();
            }
            // Insert Data into database using DonorRepositoryDataset.xml
            IDataSet dataSet = getDataSet();
          
            DatabaseOperation.INSERT.execute(connection, dataSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() throws Exception {
        // Remove data from database
        DatabaseOperation.DELETE_ALL.execute(connection, getDataSet());
    }

    
        /**
     * This method is executed once before test case execution start and
     * acquires datasource from spring context and create new dbunit
     * IDatabaseConnection. This method is also useful to set HSQLDB
     * datatypefactory.
     */
    private void getConnection() {
        try {
            connection = new DatabaseDataSourceConnection(dataSource);
            DatabaseConfig config = connection.getConfig();
            // Set HSQLDB datatypefactory
            config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                    new HsqldbDataTypeFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private IDataSet getDataSet() throws Exception {
        File file = new File("test/dataset/generalConfigurationDataset.xml");
        return new FlatXmlDataSet(file);
    }

    
    @Test
    /**
     * Should Return the list of GeneralConfigs 
     * getAll()
     */
    public void findAllConfigs_ShouldNotReturnEmptyList(){
        assertTrue("Should return the list of configs",configRepository.getAll().size()>1);
       
    }
    
}
