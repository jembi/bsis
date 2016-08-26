package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.jembi.bsis.helpers.builders.GeneralConfigViewModelBuilder.aGeneralConfigViewModelBuilder;
import static org.jembi.bsis.helpers.matchers.GeneralConfigViewModelMatcher.hasSameStateAsGeneralConfigViewModel;

import org.jembi.bsis.helpers.builders.DataTypeBuilder;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.GeneralConfigViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class GeneralConfigFactoryTests extends UnitTestSuite{
  
  @InjectMocks
  private GeneralConfigFactory generalConfigFactory;
  
  @Test
  public void testConvertGenConfigEntityToViewModelWithTextDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("text").build();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(1l)
        .withName("name")
        .withDescription("description")
        .withValue("text Values")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(1l)
        .withName("name")
        .withDescription("description")
        .withValue("text Values")
        .withDataType(dataType)
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));
    
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelWithIntegerDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(2l).withDataType("Integer").build();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(2l)
        .withName("name")
        .withDescription("description")
        .withValue("1234")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(2l)
        .withName("name")
        .withDescription("description")
        .withValue("1234")
        .withDataType(dataType)
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));  
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelWithDecimalDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(3l).withDataType("Integer").build();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(3l)
        .withName("name")
        .withDescription("description")
        .withValue("12.25")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(3l)
        .withName("name")
        .withDescription("description")
        .withValue("12.25")
        .withDataType(dataType)
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));  
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelBooleanDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(4l).withDataType("Integer").build();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(4l)
        .withName("name")
        .withDescription("description")
        .withValue("1234")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(4l)
        .withName("name")
        .withDescription("description")
        .withValue("1234")
        .withDataType(dataType)
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));  
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelPasswordDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(4l).withDataType("Integer").build();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(4l)
        .withName("name")
        .withDescription("description")
        .withValue("1234")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(4l)
        .withName("name")
        .withDescription("description")
        .withValue("1234")
        .withDataType(dataType)
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelWithPassword_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(5l).withDataType("password").build();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(5l)
        .withName("name")
        .withDescription("description")
        .withValue("")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(5l)
        .withName("name")
        .withDescription("description")
        .withValue("")
        .withDataType(dataType)
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel)); 
  }

}
