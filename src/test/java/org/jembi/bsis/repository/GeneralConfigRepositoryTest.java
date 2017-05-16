package org.jembi.bsis.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the GeneralConfigRepository
 */
public class GeneralConfigRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  GeneralConfigRepository generalConfigRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/GeneralConfigRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAll() throws Exception {
    List<GeneralConfig> all = generalConfigRepository.getAll();
    Assert.assertNotNull("There are GeneralConfigs", all);
    Assert.assertEquals("There are 33 GeneralConfig", 33, all.size());
  }

  @Test
  public void testFindGeneralConfigById() throws Exception {
    GeneralConfig one = generalConfigRepository.getGeneralConfigById(UUID.fromString("551baa94-6033-4ba3-8c1f-731012ba4fa1"));
    Assert.assertNotNull("There is a GeneralConfig with id 1", one);
    Assert.assertEquals("The GeneralConfig matches what was expected", "donation.bpSystolicMax", one.getName());
  }

  @Test(expected = javax.persistence.NoResultException.class)
  public void testFindGeneralConfigByIdUnknown() throws Exception {
    UUID unknownGeneralConfigId = UUID.randomUUID();
    generalConfigRepository.getGeneralConfigById(unknownGeneralConfigId);
  }

  @Test
  public void testGetGeneralConfigByName() throws Exception {
    GeneralConfig bpSystolicMax = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
    Assert.assertNotNull("There is a GeneralConfig by that name", bpSystolicMax);
    Assert.assertEquals("The GeneralConfig matches what was expected", "250", bpSystolicMax.getValue());
  }

  @Test
  public void testGetGeneralConfigByNameUnknown() throws Exception {
    GeneralConfig junit = generalConfigRepository.getGeneralConfigByName("junit");
    Assert.assertNull("There is no junit GeneralConfig", junit);
  }

  @Test
  public void testUpdate() throws Exception {
    GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
    generalConfig.setValue("255");
    generalConfigRepository.update(generalConfig);
    GeneralConfig savedGeneralConfig = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
    Assert.assertEquals("The GeneralConfig has been updated", "255", savedGeneralConfig.getValue());
  }

  @Test
  public void testUpdateAll() throws Exception {
    GeneralConfig generalConfig1 = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
    generalConfig1.setValue("255");
    GeneralConfig generalConfig2 = generalConfigRepository.getGeneralConfigByName("donation.bpDiastolicMax");
    generalConfig2.setValue("155");
    List<GeneralConfig> generalConfigs = new ArrayList<GeneralConfig>();
    generalConfigs.add(generalConfig1);
    generalConfigs.add(generalConfig2);
    generalConfigRepository.updateAll(generalConfigs);
    GeneralConfig savedGeneralConfig1 = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
    Assert.assertEquals("The GeneralConfig has been updated", "255", savedGeneralConfig1.getValue());
    GeneralConfig savedGeneralConfig2 = generalConfigRepository.getGeneralConfigByName("donation.bpDiastolicMax");
    Assert.assertEquals("The GeneralConfig has been updated", "155", savedGeneralConfig2.getValue());
  }

  @Test
  public void testSave() throws Exception {
    GeneralConfig bpSystolicMax = generalConfigRepository.getGeneralConfigByName("donation.bpSystolicMax");
    GeneralConfig newGeneralConfig = new GeneralConfig();
    newGeneralConfig.copy(bpSystolicMax);
    newGeneralConfig.setName("junit.save");
    generalConfigRepository.save(newGeneralConfig);
    GeneralConfig savedGeneralConfig = generalConfigRepository.getGeneralConfigByName("junit.save");
    Assert.assertNotNull("There is a new GeneralConfig", savedGeneralConfig);
    Assert.assertEquals("The new GeneralConfig is correct", "250", savedGeneralConfig.getValue());
  }
}
