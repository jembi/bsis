package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the DeferralReason Repository
 */
public class DeferralReasonRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  DeferralReasonRepository deferralReasonRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/DeferralReasonRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<DeferralReason> all = deferralReasonRepository.getAllDeferralReasonsIncludDeleted();
    Assert.assertNotNull("There are deferral reasons defined", all);
    Assert.assertEquals("There are 6 deferral reasons defined", 6, all.size());
  }

  @Test
  public void testGetDeferralReasonById() throws Exception {
    DeferralReason deferralReason = deferralReasonRepository.getDeferralReasonById(UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870681"));
    Assert.assertNotNull("DeferralReason with id 1 exists", deferralReason);
  }

  @Test
  public void testFindDeferralReason() throws Exception {
    DeferralReason deferralReason = deferralReasonRepository.findDeferralReason("High risk behaviour");
    Assert.assertNotNull("DeferralReason exists", deferralReason);
    Assert.assertEquals("Deferral reason matches", "High risk behaviour", deferralReason.getReason());
  }

  @Test
  public void testFindDeferralReasonUnknown() throws Exception {
    DeferralReason deferralReason = deferralReasonRepository.findDeferralReason("Junit");
    Assert.assertNull("DeferralReason does not exist", deferralReason);
  }

  @Test
  public void testUpdateDeferralReason() throws Exception {
    UUID deferralReasonId = UUID.fromString("11e71397-acc9-b7da-8cc5-34e6d7870681");
    DeferralReason deferralReason = deferralReasonRepository.getDeferralReasonById(deferralReasonId);
    Assert.assertNotNull("DeferralReason exists", deferralReason);

    deferralReason.setReason("Junit");
    deferralReasonRepository.updateDeferralReason(deferralReason);

    DeferralReason savedDeferralReason = deferralReasonRepository.getDeferralReasonById(deferralReasonId);
    Assert.assertNotNull("DeferralReason still exists", savedDeferralReason);
    Assert.assertEquals("Reason has been updated", "Junit", savedDeferralReason.getReason());
  }

  @Test
  public void testSaveDeferralReason() throws Exception {
    DeferralReason deferralReason = new DeferralReason();
    deferralReason.setReason("New reason");
    deferralReasonRepository.saveDeferralReason(deferralReason);

    List<DeferralReason> all = deferralReasonRepository.getAllDeferralReasonsIncludDeleted();
    Assert.assertNotNull("There are deferral reasons defined", all);
    Assert.assertEquals("There are 7 deferral reasons defined", 7, all.size());
  }
}
