package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DataTypeBuilder.aDataType;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.EnumDataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class GeneralConfigAccessorServiceTests extends UnitTestSuite {

  private static final String IRRELEVANT_CONFIG_NAME = "irrelevant.configName";

  @InjectMocks
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Mock
  private GeneralConfigRepository generalConfigRepository;

  @Test(expected = IllegalArgumentException.class)
  public void testGetBooleanValueWithMissingGeneralConfig_shouldThrow() {
    when(generalConfigRepository.getGeneralConfigByName(IRRELEVANT_CONFIG_NAME)).thenReturn(null);

    generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetBooleanValueWithNonBooleanGeneralConfig_shouldThrow() {
    DataType nonBooleanDataType = aDataType().withDataType(EnumDataType.INTEGER.name()).build();
    GeneralConfig generalConfig = aGeneralConfig().withDataType(nonBooleanDataType).build();

    when(generalConfigRepository.getGeneralConfigByName(IRRELEVANT_CONFIG_NAME)).thenReturn(generalConfig);

    generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME);
  }

  @Test
  public void testGetBooleanValueWithFalseValue_shouldReturnFalse() {
    testGetBooleanValue_shouldReturnSameBooleanValue(false);
  }

  @Test
  public void testGetBooleanValueWithTrueValue_shouldReturnTrue() {
    testGetBooleanValue_shouldReturnSameBooleanValue(true);
  }

  private void testGetBooleanValue_shouldReturnSameBooleanValue(boolean booleanValue) {
    DataType booleanDataType = aDataType().withDataType(EnumDataType.BOOLEAN.name()).build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withDataType(booleanDataType)
        .withValue(Boolean.toString(booleanValue))
        .build();

    when(generalConfigRepository.getGeneralConfigByName(anyString())).thenReturn(generalConfig);

    boolean returnedBooleanValue = generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME);

    verify(generalConfigRepository).getGeneralConfigByName(IRRELEVANT_CONFIG_NAME);
    assertThat(returnedBooleanValue, is(booleanValue));
  }
  
  @Test
  public void testGetBooleanValueWithDefault_shouldReturnDefaultValue() {
    
    boolean defaultValue = true;

    boolean returnedBooleanValue = generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME, defaultValue);

    assertThat(returnedBooleanValue, is(defaultValue));
  }
  
  @Test
  public void testGetBooleanValueWithDefaultAndExistingGeneralConfig_shouldReturnGeneralConfigValue() {
    
    boolean booleanValue = false;
    DataType booleanDataType = aDataType().withDataType(EnumDataType.BOOLEAN.name()).build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withDataType(booleanDataType)
        .withValue(Boolean.toString(booleanValue))
        .build();

    when(generalConfigRepository.getGeneralConfigByName(IRRELEVANT_CONFIG_NAME)).thenReturn(generalConfig);
    
    boolean returnedBooleanValue = generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME, true);

    assertThat(returnedBooleanValue, is(booleanValue));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetIntValueWithMissingGeneralConfig_shouldThrow() {
    when(generalConfigRepository.getGeneralConfigByName(IRRELEVANT_CONFIG_NAME)).thenReturn(null);

    generalConfigAccessorService.getIntValue(IRRELEVANT_CONFIG_NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetIntValueWithNonIntGeneralConfig_shouldThrow() {
    DataType nonIntDataType = aDataType().withDataType(EnumDataType.BOOLEAN.name()).build();
    GeneralConfig generalConfig = aGeneralConfig().withDataType(nonIntDataType).build();

    when(generalConfigRepository.getGeneralConfigByName(IRRELEVANT_CONFIG_NAME)).thenReturn(generalConfig);

    generalConfigAccessorService.getIntValue(IRRELEVANT_CONFIG_NAME);
  }

  @Test
  public void testGetIntValue_shouldReturnCorrectIntValue() {
    int intValue = 123;
    DataType booleanDataType = aDataType().withDataType(EnumDataType.INTEGER.name()).build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withDataType(booleanDataType)
        .withValue(Integer.toString(intValue))
        .build();

    when(generalConfigRepository.getGeneralConfigByName(anyString())).thenReturn(generalConfig);

    int returnedIntValue = generalConfigAccessorService.getIntValue(IRRELEVANT_CONFIG_NAME);

    verify(generalConfigRepository).getGeneralConfigByName(IRRELEVANT_CONFIG_NAME);
    assertThat(returnedIntValue, is(intValue));
  }

  @Test
  public void testGetGeneralConfigValueByName_shouldReturnCorrectValueAsString() {
    // set up data
    int intValue = 123;
    DataType booleanDataType = aDataType().withDataType(EnumDataType.INTEGER.name()).build();
    GeneralConfig generalConfig = aGeneralConfig()
        .withDataType(booleanDataType)
        .withValue(Integer.toString(intValue))
        .build();

    // set up mocks
    when(generalConfigRepository.getGeneralConfigByName("my.int")).thenReturn(generalConfig);

    // run tests
    String value = generalConfigAccessorService.getGeneralConfigValueByName("my.int");

    // do asserts
    Assert.assertNotNull("value was returned", value);
    Assert.assertEquals("value is correct", "123", value);
  }

  @Test
  public void testGetGeneralConfigValueByName_shouldReturnEmptyString() {
    // set up data

    // set up mocks
    when(generalConfigRepository.getGeneralConfigByName("unknown")).thenReturn(null);

    // run tests
    String value = generalConfigAccessorService.getGeneralConfigValueByName("unknown");

    // do asserts
    Assert.assertNotNull("value was returned", value);
    Assert.assertEquals("value is correct", "", value);
  }
}
