package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
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
    File file = new File("src/test/resources/dataset/ComponentStatusChangeReasonRepositoryDataset.xml");
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
  public void testFindFirstComponentStatusChangeReasonForCategory() {
    UUID statusChangeReasonId = UUID.fromString("b827d865-abfe-41ad-98e4-60514148e215");
    ComponentStatusChangeReason returnedReason = componentStatusChangeReasonRepository.findFirstComponentStatusChangeReasonForCategory(
        ComponentStatusChangeReasonCategory.ISSUED);
    Assert.assertNotNull("A reason is returned", returnedReason);
    Assert.assertEquals("The correct reason is returned", statusChangeReasonId, returnedReason.getId());
  }
}
