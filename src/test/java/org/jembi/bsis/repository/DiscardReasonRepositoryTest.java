package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.DiscardReasonRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DiscardReasonRepository
 */
public class DiscardReasonRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DiscardReasonRepository discardReasonRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DiscardReasonRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAllDiscardsReasons() throws Exception {
    List<ComponentStatusChangeReason> all = discardReasonRepository.getAllDiscardReasons(true);
    Assert.assertNotNull("There are discard reasons defined", all);
    Assert.assertEquals("There are 8 discard reasons defined", 8, all.size());
  }
  
  @Test
  public void testGetAllDiscardReasonsNotDeleted() throws Exception {
    List<ComponentStatusChangeReason> allNotDeleted = discardReasonRepository.getAllDiscardReasons(false);
    Assert.assertNotNull("There are discard reasons defined", allNotDeleted);
    Assert.assertEquals("There are 6 discard reasons defined", 6, allNotDeleted.size());
  }

  @Test
  public void testGetDiscardReasonById() throws Exception {
    UUID discardReasonId = UUID.fromString("b827d865-abfe-41ad-98e4-60514148e231");
    ComponentStatusChangeReason discardReason = discardReasonRepository.getDiscardReasonById(discardReasonId);
    Assert.assertNotNull("Discard reason with id 1 exists", discardReason);
  }

  @Test
  public void testFindDeferralReason() throws Exception {
    ComponentStatusChangeReason reason = discardReasonRepository.findDiscardReason("Incomplete Donation");
    Assert.assertNotNull("Discard reason exists", reason);
    Assert.assertEquals("Discard reason matches", "Incomplete Donation", reason.getStatusChangeReason());
  }

  @Test
  public void testFindDeferralReasonUnknown() throws Exception {
    ComponentStatusChangeReason reason = discardReasonRepository.findDiscardReason("Junit");
    Assert.assertNull("Discard reason does not exist", reason);
  }

  @Test
  public void testUpdateDeferralReason() throws Exception {
    UUID discardReasonId = UUID.fromString("b827d865-abfe-41ad-98e4-60514148e231");
    ComponentStatusChangeReason reason = discardReasonRepository.getDiscardReasonById(discardReasonId);
    Assert.assertNotNull("Discard reason exists", reason);

    reason.setStatusChangeReason("Junit");
    discardReasonRepository.updateDiscardReason(reason);

    ComponentStatusChangeReason savedReason = discardReasonRepository.getDiscardReasonById(discardReasonId);
    Assert.assertNotNull("Discard reason still exists", savedReason);
    Assert.assertEquals("Reason has been updated", "Junit", savedReason.getStatusChangeReason());
  }

  @Test
  public void testSaveDeferralReason() throws Exception {
    ComponentStatusChangeReason reason = new ComponentStatusChangeReason();
    reason.setStatusChangeReason("Junit");
    reason.setCategory(ComponentStatusChangeReasonCategory.DISCARDED);
    discardReasonRepository.saveDiscardReason(reason);

    List<ComponentStatusChangeReason> all = discardReasonRepository.getAllDiscardReasons(true);
    Assert.assertNotNull("There are Discard reasons defined", all);
    Assert.assertEquals("There are 9 Discard reasons defined", 9, all.size());
  }

  @Test
  public void testDiscardReasonExists() throws Exception {
    UUID discardReasonId = UUID.fromString("b827d865-abfe-41ad-98e4-60514148e231");
    Assert.assertTrue("DiscardReason exists", discardReasonRepository.verifyDiscardReasonExists(discardReasonId));
  }

  @Test
  public void testEntityDoesNotExist() throws Exception {
    Assert.assertFalse("DiscardReason does not exist", discardReasonRepository.verifyDiscardReasonExists(UUID.randomUUID()));
  }

}
