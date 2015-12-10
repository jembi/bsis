package service;

import model.admin.DataType;
import model.admin.EnumDataType;
import model.admin.GeneralConfig;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import repository.GeneralConfigRepository;
import suites.UnitTestSuite;

import static helpers.builders.DataTypeBuilder.aDataType;
import static helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
