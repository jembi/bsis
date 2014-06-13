/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package repository;

import java.io.File;
import javax.sql.DataSource;
import model.location.Location;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static repository.DonorRepositoryTest.connection;

/**
*
* @author srikanth
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
public class LocationRepositoryTest {
    
    @Autowired
    private LocationRepository locationRepository;
    
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
        File file = new File("test/dataset/LocationRepositoryDataset.xml");
        return new FlatXmlDataSet(file);
    }
    
    /**
     * should Persist in database on save
     * saveLocation(Location)
     */
    @Test
    public void saveLocation_ShouldPersist(){
        Location location = new Location();
        location.setName("rwanda");
        location.setIsCollectionCenter(true);
        location.setIsCollectionSite(false);
        location.setIsUsageSite(false);
        location.setIsCurrentLocation(true);
        location.setIsDeleted(false);
        locationRepository.saveLocation(location);
        assertNotNull("location should exist in databse on save", location.getId());
    }
    
     /**
     * should return  all the location objects
     * getAllLocations()
     */
    @Test
    public void findAllLocations_ShouldReturnNonEmptyList(){
        assertFalse("Should return  all the locations",
                      locationRepository.getAllLocations().isEmpty());
    }
    
    /**
     * should return  all the donor panels
     * getAllDonorPanels()
     */
    @Test
    public void findAllDonorPanels_ShouldReturnNonEmptyList(){
        assertTrue("Should return  all the  donor panels",
                      !locationRepository.getAllDonorPanels().isEmpty());
    }
    
    /**
     * should return the current location of donor panels
     * getCurrentDonorPanel()
     */
    @Test
    public void getCurrentLocation_ShouldReturnNonEmptyList(){
        assertFalse("Should return  the current location of donor panels",
                      locationRepository.getCurrentDonorPanel().isEmpty());
    }

   
 
    
}
