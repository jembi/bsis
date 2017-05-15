package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.GeneralConfigBuilder.aGeneralConfig;
import static org.jembi.bsis.helpers.builders.GeneralConfigViewModelBuilder.aGeneralConfigViewModelBuilder;
import static org.jembi.bsis.helpers.matchers.GeneralConfigViewModelMatcher.hasSameStateAsGeneralConfigViewModel;

import java.util.List;
import java.util.UUID;
import java.util.Arrays;

import org.jembi.bsis.helpers.builders.DataTypeBuilder;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.GeneralConfigViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;

public class GeneralConfigFactoryTests extends UnitTestSuite {
  
  @InjectMocks
  private GeneralConfigFactory generalConfigFactory;
  
  @Test
  public void testConvertGenConfigEntityToViewModelWithTextDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(1l).withDataType("text").build();
    UUID generalConfigId = UUID.randomUUID();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withValue("text Values")
        .withDataType(dataType)
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(generalConfigId)
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
    UUID generalConfigId = UUID.randomUUID();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("1234")
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("1234")
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));  
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelWithDecimalDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(3l).withDataType("Decimal").build();
    UUID generalConfigId = UUID.randomUUID();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("12.25")
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("12.25")
        .build();
    
    //Test
    GeneralConfigViewModel viewModel = generalConfigFactory.createViewModel(generalConfig);
    
    //Assertions
    assertThat(viewModel, hasSameStateAsGeneralConfigViewModel(expectedViewModel));  
  }
  
  @Test
  public void testConvertGenConfigEntityToViewModelBooleanDataType_shouldReturnExpectedViewModel() {
    //Data SetUp
    DataType dataType = DataTypeBuilder.aDataType().withId(4l).withDataType("Boolean").build();
    UUID generalConfigId = UUID.randomUUID();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("true")
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("true")
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
    UUID generalConfigId = UUID.randomUUID();
    
    GeneralConfig generalConfig = aGeneralConfig()
        .withId(generalConfigId)
        .withName("name")
        .withDescription("description")
        .withDataType(dataType)
        .withValue("*****")
        .build();
    
    GeneralConfigViewModel expectedViewModel = aGeneralConfigViewModelBuilder()
        .withId(generalConfigId)
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
  
  @Test
  public void testConvertGenConfigViewModel_shouldReturnExpectedViewModels () {
    // Data setUp
    DataType password = DataTypeBuilder.aDataType().withId(5l).withDataType("Password").build();
    DataType booolean = DataTypeBuilder.aDataType().withId(4l).withDataType("Boolean").build();
    DataType integer = DataTypeBuilder.aDataType().withId(3l).withDataType("Integer").build();
    DataType decimal = DataTypeBuilder.aDataType().withId(2l).withDataType("Decimal").build();
    DataType text = DataTypeBuilder.aDataType().withId(1l).withDataType("Text").build();
    UUID generalConfiIdId1 = UUID.randomUUID();
    UUID generalConfiIdId2 = UUID.randomUUID();
    UUID generalConfiIdId3 = UUID.randomUUID();
    UUID generalConfiIdId4 = UUID.randomUUID();
    UUID generalConfiIdId5 = UUID.randomUUID();
    
    List<GeneralConfig> configs = Arrays.asList(
        aGeneralConfig().withId(generalConfiIdId5).withName("name").withDescription("description").withDataType(password).withValue("").build(),
        aGeneralConfig().withId(generalConfiIdId4).withName("name").withDescription("description").withDataType(booolean).withValue("true").build(),
        aGeneralConfig().withId(generalConfiIdId3).withName("name").withDescription("description").withDataType(integer).withValue("1234").build(),
        aGeneralConfig().withId(generalConfiIdId2).withName("name").withDescription("description").withDataType(decimal).withValue("12.34").build(),
        aGeneralConfig().withId(generalConfiIdId1).withName("name").withDescription("description").withDataType(text).withValue("text").build());
    
    List<GeneralConfigViewModel> expectedConfigs = Arrays.asList(
        aGeneralConfigViewModelBuilder().withId(generalConfiIdId5).withName("name").withDescription("description").withDataType(password).withValue("").build(),
        aGeneralConfigViewModelBuilder().withId(generalConfiIdId4).withName("name").withDescription("description").withDataType(booolean).withValue("true").build(),
        aGeneralConfigViewModelBuilder().withId(generalConfiIdId3).withName("name").withDescription("description").withDataType(integer).withValue("1234").build(),
        aGeneralConfigViewModelBuilder().withId(generalConfiIdId2).withName("name").withDescription("description").withDataType(decimal).withValue("12.34").build(),
        aGeneralConfigViewModelBuilder().withId(generalConfiIdId1).withName("name").withDescription("description").withDataType(text).withValue("text").build());       
    
    // Test
    List<GeneralConfigViewModel> viewModels = generalConfigFactory.createViewModels(configs);
   
    //Assertions
    assertThat("View models are created", viewModels.size() == 5);
    assertThat(viewModels.get(0), hasSameStateAsGeneralConfigViewModel(expectedConfigs.get(0)));
    assertThat(viewModels.get(1), hasSameStateAsGeneralConfigViewModel(expectedConfigs.get(1)));
    assertThat(viewModels.get(2), hasSameStateAsGeneralConfigViewModel(expectedConfigs.get(2)));
    assertThat(viewModels.get(3), hasSameStateAsGeneralConfigViewModel(expectedConfigs.get(3)));
    assertThat(viewModels.get(4), hasSameStateAsGeneralConfigViewModel(expectedConfigs.get(4)));
  }
}
