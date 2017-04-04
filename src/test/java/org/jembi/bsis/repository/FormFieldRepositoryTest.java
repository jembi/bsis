package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the FormFieldRepository
 */
public class FormFieldRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  FormFieldRepository formFieldRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/FormFieldRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testFindFormFieldById() throws Exception {
    FormField one = formFieldRepository.findFormFieldById(1l);
    Assert.assertNotNull("There is a FormField with id 1", one);
    Assert.assertEquals("The Form field matches what was expected", "Donor", one.getForm());
  }

  @Test
  public void testFindFormFieldByIdUnknown() throws Exception {
    FormField one = formFieldRepository.findFormFieldById(1111l);
    Assert.assertNull("There is no FormField with id 1111", one);
  }

  @Test
  public void testGetFormField() throws Exception {
    FormField donorField = formFieldRepository.getFormField("Donor", "donorNumber");
    Assert.assertNotNull("There is a donorNumber field for the Donor form", donorField);
    Assert.assertEquals("The Form field matches what was expected", "Donor", donorField.getForm());
  }

  @Test
  public void testGetFormFieldUnknown() throws Exception {
    FormField donorField = formFieldRepository.getFormField("Donor", "junit");
    Assert.assertNull("There is no junit field for the Donor form", donorField);
  }

  @Test
  public void testGetAllDonorForm() throws Exception {
    List<FormField> all = formFieldRepository.getFormFields("Donor");
    Assert.assertNotNull("There are FormFields for the Donor Form", all);
    Assert.assertEquals("There are 46 FormFields", 46, all.size());
  }

  @Test
  public void testGetRequiredFormFieldsDonorForm() throws Exception {
    List<String> all = formFieldRepository.getRequiredFormFields("Donor");
    Assert.assertNotNull("There are FormFields for the Donor Form", all);
    Assert.assertEquals("There are 5 required FormFields", 5, all.size());
    Assert.assertTrue("donorNumber is a required field for the Donor form", all.contains("donorNumber"));
  }

  @Test
  public void testGetRequiredFormFieldsUnknownForm() throws Exception {
    List<String> all = formFieldRepository.getRequiredFormFields("Junit");
    Assert.assertNotNull("There are no FormFields for the unknown Form", all);
    Assert.assertEquals("There are no FormFields for the unknown Form", 0, all.size());
  }

  @Test
  public void testGetFieldMaxLengths() throws Exception {
    Map<String, Integer> maxLengths = formFieldRepository.getFieldMaxLengths("Donor");
    Assert.assertNotNull("There is a non-null result", maxLengths);
    Assert.assertEquals("There are 45 FormFields for Donor Form", 45, maxLengths.size());
    // FIXME: there is a bug here - callingName field is repeated for Donor Form
    Integer length = maxLengths.get("donorNumber");
    Assert.assertEquals("donorNumber has a maxLength of 15", new Integer(15), length);
  }

  @Test
  public void testUpdateFormField() throws Exception {
    FormField donorField = formFieldRepository.getFormField("Donor", "donorNumber");
    Assert.assertNotNull("There is a donorNumber field for the Donor form", donorField);
    donorField.setMaxLength(123);
    formFieldRepository.updateFormField(donorField);
    FormField savedDonorField = formFieldRepository.getFormField("Donor", "donorNumber");
    Assert.assertEquals("The Form field matches what was expected", new Integer(123), savedDonorField.getMaxLength());
  }
}
