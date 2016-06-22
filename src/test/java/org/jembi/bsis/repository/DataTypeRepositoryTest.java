package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.DataTypeRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DataType Repository
 */
public class DataTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DataTypeRepository dataTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DataTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<DataType> all = dataTypeRepository.getAll();
    Assert.assertNotNull("There are data types defined", all);
    Assert.assertEquals("There are 4 data types defined", 4, all.size());
  }

  @Test
  public void testGetDataTypeByid() throws Exception {
    DataType dataType = dataTypeRepository.getDataTypeByid(1L);
    Assert.assertNotNull("DataType with id 1 exists", dataType);
  }
}
