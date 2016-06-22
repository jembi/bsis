package org.jembi.bsis.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.jembi.bsis.helpers.builders.FormFieldBuilder;
import org.jembi.bsis.model.admin.FormField;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.service.FormFieldAccessorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import scala.actors.threadpool.Arrays;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class FormFieldAccessorServiceTest {

  @InjectMocks
  FormFieldAccessorService formFieldAccessorService;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testGetFormFieldsForForm() throws Exception {
    // set up data
    FormField formField1 = FormFieldBuilder.aFormField()
        .withForm("Donor")
        .withField("firstName")
        .withDefaultDisplayName("First Name")
        .build();
    FormField formField2 = FormFieldBuilder.aFormField()
        .withForm("Donor")
        .withField("lastName")
        .withDefaultDisplayName("Last Name")
        .build();
    List<FormField> formFields = Arrays.asList(new FormField[]{formField1, formField2});

    // set up mocks
    when(formFieldRepository.getFormFields("Donor")).thenReturn(formFields);

    // run test
    Map<String, Map<String, Object>> fields = formFieldAccessorService.getFormFieldsForForm("Donor");

    // asserts
    Assert.assertEquals("Two fields returned", 2, fields.size());
    Map<String, Object> firstNameFields = fields.get("firstName");
    Assert.assertNotNull("firstName fields exist", firstNameFields);
    Assert.assertEquals("fields are present", 8, firstNameFields.size());
    Assert.assertEquals("fields are present", "First Name", firstNameFields.get(FormField.DISPLAY_NAME));
    Map<String, Object> lastNameFields = fields.get("lastName");
    Assert.assertNotNull("lastName fields exist", lastNameFields);
    Assert.assertEquals("fields are present", 8, lastNameFields.size());
    Assert.assertEquals("fields are present", "Last Name", lastNameFields.get(FormField.DISPLAY_NAME));
  }
}
