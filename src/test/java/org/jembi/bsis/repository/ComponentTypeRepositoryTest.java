package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the ComponentTypeRepository
 */
public class ComponentTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  ComponentTypeRepository componentTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/ComponentTypeRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<ComponentType> all = componentTypeRepository.getAllComponentTypes();
    Assert.assertNotNull("There are ComponentTypes", all);
    Assert.assertEquals("There are 16 ComponentTypes", 16, all.size());
  }

  @Test
  public void testGetAllComponentTypesIncludeDeleted() throws Exception {
    List<ComponentType> all = componentTypeRepository.getAllComponentTypesIncludeDeleted();
    Assert.assertNotNull("There are ComponentTypes", all);
    Assert.assertEquals("There are 17 ComponentTypes (including deleted)", 17, all.size());
  }

  @Test
  public void testIsComponentTypeValidTrue() throws Exception {
    boolean valid = componentTypeRepository.verifyComponentTypeExists(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    Assert.assertTrue("Is a valid ComponentType", valid);
  }

  @Test
  public void testIsComponentTypeValidFalse() throws Exception {
    boolean valid = componentTypeRepository.verifyComponentTypeExists(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5856"));
    Assert.assertFalse("Is not a valid ComponentType", valid);
  }

  @Test
  public void testIsComponentTypeValidDeleted() throws Exception {
    boolean valid = componentTypeRepository.verifyComponentTypeExists(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5817"));
    Assert.assertFalse("Is not a valid ComponentType", valid);
  }

  @Test
  public void testGetComponentTypeById() throws Exception {
    ComponentType one = componentTypeRepository.getComponentTypeById(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    Assert.assertNotNull("There is a ComponentType", one);
    Assert.assertEquals("ComponentType matches", "Whole Blood Single Pack - CPDA", one.getComponentTypeName());
  }

  @Test
  @Transactional
  public void testGetComponentTypeByIdUnknown() throws Exception {
    ComponentType one = componentTypeRepository.getComponentTypeById(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5123"));
    Assert.assertNull("There is no ComponentType", one);
  }

  @Test
  public void testSaveComponentType() throws Exception {
    ComponentType componentType = new ComponentType();
    componentType.setComponentTypeName("Junit");
    componentType.setComponentTypeCode("j");
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
  public void testUpdateComponentType() throws Exception {
    ComponentType existingComponentType = componentTypeRepository.getComponentTypeById(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    existingComponentType.setDescription("Junit");
    componentTypeRepository.updateComponentType(existingComponentType);
    ComponentType updatedComponentType = componentTypeRepository.getComponentTypeById(UUID.fromString("99a61311-1234-4321-b3ea-39e11c2c5801"));
    Assert.assertEquals("Description has been updated", "Junit", updatedComponentType.getDescription());
  }
}
