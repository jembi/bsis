package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the ComponentStatusChangeReasonRepository
 */
public class ComponentStatusChangeReasonRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/ComponentStatusChangeReasonRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository.getAllComponentStatusChangeReasons();
    Assert.assertNotNull("There are ComponentStatusChangeReason", all);
    Assert.assertEquals("There are 11 ComponentStatusChangeReason", 12, all.size());
  }

  @Test
  public void testGetComponentStatusChangeReasons() throws Exception {
    List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository
        .getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
    Assert.assertNotNull("There are DISCARDED ComponentStatusChangeReason", all);
    Assert.assertEquals("There are 6 DISCARDED ComponentStatusChangeReason", 6, all.size());
  }

  @Test
  public void testGetComponentStatusChangeReasonsNone() throws Exception {
    List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository
        .getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.ISSUED);
    Assert.assertNotNull("Doesn't return a null list", all);
    Assert.assertEquals("There is 1 ISSUED ComponentStatusChangeReason", 1, all.size());
  }

  @Test
  public void testGetComponentStatusChangeReasonById() throws Exception {
    ComponentStatusChangeReason one = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(1l);
    Assert.assertNotNull("There is a ComponentStatusChangeReason", one);
    Assert.assertEquals("ComponentStatusChangeReason matches", ComponentStatusChangeReasonCategory.DISCARDED,
        one.getCategory());
  }

  @Test
  public void testGetComponentStatusChangeReasonByIdUnknown() throws Exception {
    ComponentStatusChangeReason one = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(123l);
    Assert.assertNull("There is no ComponentStatusChangeReason", one);
  }

  @Test
  @Ignore("Bug - getAllComponentStatusChangeReasonsAsMap only returns one ComponentStatusChangeReason per ComponentStatusChangeReasonCategory")
  public void testGetAllComponentStatusChangeReasonsAsMap() throws Exception {
    Map<ComponentStatusChangeReasonCategory, ComponentStatusChangeReason> result = componentStatusChangeReasonRepository
        .getAllComponentStatusChangeReasonsAsMap();
    Assert.assertNotNull("Does not return a null map", result);
    Assert.assertEquals("There are 3 ComponentStatusChangeReasonCategory with ComponentStatusChangeReason", 3, result.size());
  }
  
  @Test
  public void testFindFirstComponentStatusChangeReasonForCategory() {
    ComponentStatusChangeReason returnedReason = componentStatusChangeReasonRepository.findFirstComponentStatusChangeReasonForCategory(
        ComponentStatusChangeReasonCategory.ISSUED);
    Assert.assertNotNull("A reason is returned", returnedReason);
    Assert.assertEquals("The correct reason is returned", (Long) 15L, returnedReason.getId());
  }
}
