package controllerservice;

import static org.mockito.Mockito.when;
import helpers.builders.ComponentTypeBuilder;

import java.util.Arrays;
import java.util.List;

import model.componenttype.ComponentType;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import repository.ComponentTypeRepository;
import suites.UnitTestSuite;
import viewmodel.ComponentTypeViewModel;
import factory.ComponentTypeFactory;

public class LabellingControllerServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private LabellingControllerService labellingControllerService;
  
  @Mock
  ComponentTypeRepository componentTypeRepository;
  @Mock
  ComponentTypeFactory componentTypeFactory;

  @Test
  public void testFindReturnForms_shouldCallRepositoryAndFactory() throws Exception {
    List<ComponentType> entities = Arrays.asList(
        ComponentTypeBuilder.aComponentType().build(),
        ComponentTypeBuilder.aComponentType().build()
    );
    List<ComponentTypeViewModel> viewModels = Arrays.asList(
        new ComponentTypeViewModel(entities.get(0)),
        new ComponentTypeViewModel(entities.get(1))
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
