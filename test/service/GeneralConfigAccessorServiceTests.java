package service;

import static helpers.builders.DataTypeBuilder.aDataType;
import static helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import model.admin.DataType;
import model.admin.EnumDataType;
import model.admin.GeneralConfig;

import org.junit.Test;

import repository.GeneralConfigRepository;

public class GeneralConfigAccessorServiceTests {
    
    private static final String IRRELEVANT_CONFIG_NAME = "irrelevant.configName";
    
    private GeneralConfigAccessorService generalConfigAccessorService;
    private GeneralConfigRepository generalConfigRepository;
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanValueWithMissingGeneralConfig_shouldThrow() {
        setUpFixture();
        
        when(generalConfigRepository.getGeneralConfigByName(IRRELEVANT_CONFIG_NAME)).thenReturn(null);
        
        generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetBooleanValueWithNonBooleanGeneralConfig_shouldThrow() {
        setUpFixture();
        
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
        setUpFixture();
        
        DataType booleanDataType = aDataType().withDataType(EnumDataType.BOOLEAN.name()).build();
        GeneralConfig generalConfig = aGeneralConfig()
                .withDataType(booleanDataType)
                .withValue(Boolean.toString(booleanValue))
                .build();
        
        when(generalConfigRepository.getGeneralConfigByName(anyString())).thenReturn(generalConfig);
        
        boolean returnedBooleanValue = generalConfigAccessorService.getBooleanValue(IRRELEVANT_CONFIG_NAME);
        
        verify(generalConfigRepository).getGeneralConfigByName(IRRELEVANT_CONFIG_NAME);
        verifyNoMoreInteractions(generalConfigRepository);
        
        assertThat(returnedBooleanValue, is(booleanValue));
    }
    
    private void setUpFixture() {
        generalConfigRepository = mock(GeneralConfigRepository.class);
        
        generalConfigAccessorService = new GeneralConfigAccessorService();
        generalConfigAccessorService.setGeneralConfigRepository(generalConfigRepository);
    }

}
