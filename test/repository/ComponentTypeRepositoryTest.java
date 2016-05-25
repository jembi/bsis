package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;
import model.componenttype.ComponentTypeTimeUnits;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the ComponentTypeRepository
 */
public class ComponentTypeRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  ComponentTypeRepository componentTypeRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/ComponentTypeRepositoryDataset.xml");
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
    boolean valid = componentTypeRepository.verifyComponentTypeExists(1L);
    Assert.assertTrue("Is a valid ComponentType", valid);
  }

  @Test
  public void testIsComponentTypeValidFalse() throws Exception {
    boolean valid = componentTypeRepository.verifyComponentTypeExists(56L);
    Assert.assertFalse("Is not a valid ComponentType", valid);
  }

  @Test
  public void testIsComponentTypeValidDeleted() throws Exception {
    boolean valid = componentTypeRepository.verifyComponentTypeExists(17L);
    Assert.assertFalse("Is not a valid ComponentType", valid);
  }

  @Test
  public void testGetComponentTypeById() throws Exception {
    ComponentType one = componentTypeRepository.getComponentTypeById(1l);
    Assert.assertNotNull("There is a ComponentType", one);
    Assert.assertEquals("ComponentType matches", "Whole Blood Single Pack - CPDA", one.getComponentTypeName());
  }

  @Test
  @Transactional
  public void testGetComponentTypeByIdUnknown() throws Exception {
    ComponentType one = componentTypeRepository.getComponentTypeById(123l);
    Assert.assertNull("There is no ComponentType", one);
  }

  @Test
  @Ignore("Bug in HSQL:  org.hibernate.QueryException: could not resolve property: componentType of: model.componenttype.ComponentType [SELECT p FROM model.componenttype.ComponentType p where p.componentType = :componentTypeName]")
  public void testGetComponentTypeByName() throws Exception {
    ComponentType one = componentTypeRepository.getComponentTypeByName("Whole Blood Single Pack - CPDA");
    Assert.assertNotNull("There is a ComponentType", one);
    Assert.assertEquals("ComponentType matches", new Long(1), one.getId());
  }

  @Test
  public void testDeactivateComponentType() throws Exception {
    componentTypeRepository.deactivateComponentType(1l);
    ComponentType componentType = componentTypeRepository.getComponentTypeById(1l); // includes deleted
    Assert.assertNotNull("ComponentType is found", componentType);
    Assert.assertTrue("ComponentType is deleted", componentType.getIsDeleted());
  }

  @Test
  public void testActivateComponentType() throws Exception {
    componentTypeRepository.activateComponentType(17l);
    ComponentType componentType = componentTypeRepository.getComponentTypeById(17l);
    Assert.assertNotNull("ComponentType is found", componentType);
    Assert.assertFalse("ComponentType is not deleted", componentType.getIsDeleted());
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
    ComponentType existingComponentType = componentTypeRepository.getComponentTypeById(1l);
    existingComponentType.setDescription("Junit");
    componentTypeRepository.updateComponentType(existingComponentType);
    ComponentType updatedComponentType = componentTypeRepository.getComponentTypeById(1l);
    Assert.assertEquals("Description has been updated", "Junit", updatedComponentType.getDescription());
  }

  @Test
  // FIXME: I am not so sure this method is working as expected as it returns the pediComponentType not the parent and
  // it will fail with a NPE if the ComponentType with id=1 does not have a pediComponentType defined.
  public void testGetAllParentComponentTypes() throws Exception {
    List<ComponentType> all = componentTypeRepository.getAllParentComponentTypes();
    Assert.assertNotNull("There are parent ComponentTypes", all);
    Assert.assertEquals("There is 1 ComponentTypes", 1, all.size());
  }

  @Test
  //FIXME: it's not very clear what this method does
  public void testGetComponentTypeByIdList() throws Exception {
    List<ComponentType> all = componentTypeRepository.getComponentTypeByIdList(1l);
    Assert.assertNotNull("There are ComponentTypes", all);
    Assert.assertEquals("There is 1 Pedi ComponentTypes", 1, all.size());
  }

  @Test
  public void testGetAllComponentTypeCombinations() throws Exception {
    List<ComponentTypeCombination> all = componentTypeRepository.getAllComponentTypeCombinations();
    Assert.assertNotNull("There are ComponentTypeCombination", all);
    Assert.assertEquals("There are 10 ComponentTypeCombination", 10, all.size());
  }

  @Test
  public void testGetAllComponentTypeCombinationsIncludeDeleted() throws Exception {
    List<ComponentTypeCombination> all = componentTypeRepository.getAllComponentTypeCombinationsIncludeDeleted();
    Assert.assertNotNull("There are ComponentTypeCombination", all);
    Assert.assertEquals("There are 11 ComponentTypeCombination incl. deleted", 11, all.size());
  }

  @Test
  public void testGetComponentTypeCombinationById() throws Exception {
    ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(1l);
    Assert.assertNotNull("There is a ComponentTypeCombination", one);
    Assert.assertEquals("The ComponentTypeCombination matches", "Whole Blood", one.getCombinationName());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testGetComponentTypeCombinationByIdUnknown() throws Exception {
    componentTypeRepository.getComponentTypeCombinationById(123l);
  }

  @Test
  public void testDeactivateComponentTypeCombination() throws Exception {
    componentTypeRepository.deactivateComponentTypeCombination(1l);
    ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(1l); // returns deleted entities
    Assert.assertNotNull("There is a ComponentTypeCombination", one);
    Assert.assertTrue("The ComponentTypeCombination is deleted", one.getIsDeleted());
  }

  @Test
  public void testActivateComponentTypeCombinationUnknown() throws Exception {
    componentTypeRepository.activateComponentTypeCombination(11l);
    ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(11l);
    Assert.assertNotNull("There is a ComponentTypeCombination", one);
    Assert.assertFalse("The ComponentTypeCombination is not deleted", one.getIsDeleted());
  }

  @Test
  public void testUpdateComponentTypeCombination() throws Exception {
    ComponentTypeCombination one = componentTypeRepository.getComponentTypeCombinationById(1l);
    one.setCombinationName("Testing");
    componentTypeRepository.updateComponentTypeCombination(one);
    ComponentTypeCombination savedOne = componentTypeRepository.getComponentTypeCombinationById(1l);
    Assert.assertEquals("ComponentTypeCombination was saved successfully", "Testing", savedOne.getCombinationName());
  }

  @Test
  public void testSaveComponentTypeCombination() throws Exception {
    ComponentTypeCombination one = new ComponentTypeCombination();
    one.setCombinationName("Testing");
    one.setIsDeleted(false);
    List<ComponentType> componentTypes = new ArrayList<ComponentType>();
    componentTypes.add(componentTypeRepository.getComponentTypeById(1l));
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
