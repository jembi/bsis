/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import model.donationbatch.DonationBatch;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author srikanth
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
public class DonationBatchRepositoryTest {
   
    @Autowired
    private DonationBatchRepository batchRepository;
    
    @Autowired
    private DataSource dataSource;
    static IDatabaseConnection connection;
    
    @Before
    public void init() {
        try {
            if (connection == null) {
                getConnection();
            }
            // Insert Data into database using DonationBatchRepositoryTest.xml
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
        File file = new File("test/dataset/DonationBatchDataset.xml");
        return new FlatXmlDataSet(file);
    }

    @Test
    /**
     * should return list of donation batches with in the specified date range 
     * 
     */
    public void findDonationBatches_ShouldReturnNonEmptyList_WhenSearchByDateRange(){
        Date startDate = new Date("07/01/2014");
        Date endDate = new Date("07/03/2014");
        List<Long> centerIds = new ArrayList<Long>();
        List<Long> siteIds = new ArrayList<Long>();
        
        centerIds.add(1l);
        siteIds.add(1l);

        List<DonationBatch> donationBatches = batchRepository.findDonationBatches
                   ("", centerIds, siteIds, startDate, endDate);
        assertTrue("Should return non empty donor list", !donationBatches.isEmpty());
        for(DonationBatch donationBatch : donationBatches){
            assertTrue("should return donation batches with in date range", donationBatch.getCreatedDate().after(startDate) && 
                    donationBatch.getCreatedDate().before(endDate));
        }
        
    }
    
}
    

