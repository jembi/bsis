package org.jembi.bsis.controllerservice;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class LabellingControllerServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private LabellingControllerService labellingControllerService;
  
  @Mock
  ComponentTypeRepository componentTypeRepository;
  @Mock
  ComponentTypeFactory componentTypeFactory;

  @Test
  public void testFindReturnForms_shouldCallRepositoryAndFactory() throws Exception {
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    List<ComponentType> entities = Arrays.asList(
        ComponentTypeBuilder.aComponentType().withId(componentTypeId1).build(),
        ComponentTypeBuilder.aComponentType().withId(componentTypeId2).build()
    );
    List<ComponentTypeViewModel> viewModels = Arrays.asList(
        ComponentTypeViewModelBuilder.aComponentTypeViewModel().withId(componentTypeId1).build(),
        ComponentTypeViewModelBuilder.aComponentTypeViewModel().withId(componentTypeId2).build()
    );
    
    // set up mocks
    when(componentTypeRepository.getAllComponentTypes()).thenReturn(entities);
    when(componentTypeFactory.createViewModels(entities)).thenReturn(viewModels);
    
    // SUT
    labellingControllerService.getComponentTypes();
    
    // verify behaviour
    Mockito.verify(componentTypeRepository).getAllComponentTypes();
    Mockito.verify(componentTypeFactory).createViewModels(entities);
  }
}
