package org.jembi.bsis.backingform.validator;

import static org.jembi.bsis.helpers.builders.GeneralConfigBackingFormBuilder.aGeneralConfigBackingFormBuilder;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.UUID;

import org.jembi.bsis.backingform.GeneralConfigBackingForm;
import org.jembi.bsis.helpers.builders.DataTypeBuilder;
import org.jembi.bsis.helpers.builders.GeneralConfigBuilder;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.repository.DataTypeRepository;
import org.jembi.bsis.repository.FormFieldRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

@RunWith(MockitoJUnitRunner.class)
public class GeneralConfigBackingFormValidatorTest {

  @InjectMocks
  GeneralConfigBackingFormValidator generalConfigBackingFormValidator;
  @Mock
  private GeneralConfigRepository generalConfigRepository;
  @Mock
  private DataTypeRepository dataTypeRepository;
  @Mock
  FormFieldRepository formFieldRepository;

  @Test
  public void testValid() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("TEXT").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("value");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testUpdate() throws Exception {
    // set up data
    UUID generalConfigId = UUID.randomUUID();
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("TEXT").build();
    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    
    form.setId(generalConfigId);
    form.setName("configname");
    form.setValue("value");
    form.setDescription("description");
    form.setDataType(dataType);

    GeneralConfig duplicate = GeneralConfigBuilder.aGeneralConfig().withId(generalConfigId).build();

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(duplicate);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testDuplicateConfigName() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("TEXT").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    UUID generalConfigId1 = UUID.randomUUID();
    UUID generalConfigId2 = UUID.randomUUID();
    
    form.setId(generalConfigId1);
    form.setName("configname");
    form.setValue("value");
    form.setDescription("description");
    form.setDataType(dataType);

    GeneralConfig duplicate = GeneralConfigBuilder.aGeneralConfig().withId(generalConfigId2).build();

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(duplicate);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: duplicate config name", errors.getFieldError("name"));
  }

  @Test
  public void testEmptyConfigName() throws Exception {
    // set up data
    UUID generalConfigId = UUID.randomUUID();
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("TEXT").build();
    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
  
    form.setId(generalConfigId);
    form.setName("");
    form.setValue("value");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
    // FIXME: I'd consider this an error
  }

  @Test
  public void testValidInteger() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("INTEGER").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("1234");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidInteger() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("INTEGER").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("not a number");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: invalid integer value", errors.getFieldError("value"));
  }

  @Test
  public void testValidDecimal() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("DECIMAL").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("12.34");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidDecimal() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("DECIMAL").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("123,123");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: invalid decimal value", errors.getFieldError("value"));
  }

  @Test
  public void testValidBooleanTrue() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("BOOLEAN").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("TRUE");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testInvalidBoolean() throws Exception {
    // set up data
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("BOOLEAN").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("0");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("Errors exist", 1, errors.getErrorCount());
    Assert.assertNotNull("Error: invalid booleanvalue", errors.getFieldError("value"));
  }

  @Test
  public void testValidBooleanFalse() throws Exception {
    // set up data""
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("BOOLEAN").build();

    GeneralConfigBackingForm form = new GeneralConfigBackingForm();
    form.setName("configname");
    form.setValue("false");
    form.setDescription("description");
    form.setDataType(dataType);

    // set up mocks
    when(dataTypeRepository.getDataTypeByid(1l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configname")).thenReturn(null);

    // run test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalconfig");
    generalConfigBackingFormValidator.validate(form, errors);

    // check asserts
    Assert.assertEquals("No errors exist", 0, errors.getErrorCount());
  }

  @Test
  public void testValidPassword_shouldSaveValue() {
    // Data setUp
    DataType dataType = DataTypeBuilder.aDataType().withId(5l).withDataType("password").build();
    
    GeneralConfigBackingForm form = aGeneralConfigBackingFormBuilder()
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("123")
        .build();
    
    //Mocks
    when(dataTypeRepository.getDataTypeByid(5l)).thenReturn(dataType);
    when(generalConfigRepository.getGeneralConfigByName("configName")).thenReturn(null);
    
    //Test
    Errors errors = new MapBindingResult(new HashMap<String, String>(), "generalConfig");
    generalConfigBackingFormValidator.validate(form, errors);
    
    //Asserts
    Assert.assertEquals("No errors exists ",0, errors.getErrorCount());
  }
}
