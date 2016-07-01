package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentManagementViewModelBuilder.aComponentManagementViewModel;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.factory.ComponentStatusChangeReasonFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.ComponentManagementViewModelBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.ComponentViewModelBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ComponentControllerServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private ComponentControllerService componentControllerService;
  
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private ComponentCRUDService componentCRUDService;
  @Mock
  private ComponentFactory componentFactory;
  @Mock
  private ComponentTypeRepository componentTypeRepository;
  @Mock
  private ComponentTypeFactory componentTypeFactory;
  @Mock
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
  @Mock
  private ComponentStatusChangeReasonFactory componentStatusChangeReasonFactory;

  @Test
  public void testFindComponentById_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    Long componentId = 1L;
    Component component = ComponentBuilder.aComponent().withId(componentId).build();
    ComponentViewModel componentViewModel = ComponentViewModelBuilder.aComponentViewModel().build();
    
    // setup mocks
    Mockito.when(componentRepository.findComponentById(componentId)).thenReturn(component);
    Mockito.when(componentFactory.createComponentViewModel(component)).thenReturn(componentViewModel);
    
    // SUT
    componentControllerService.findComponentById(1L);
    
    // verify
    Mockito.verify(componentRepository).findComponentById(componentId);
    Mockito.verify(componentFactory).createComponentViewModel(component);
  }
  
  @Test
  public void testFindComponentByCodeAndDIN_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    Long componentId = 1L;
    String componentCode = "1234-01";
    String donationIdentificationNumber = "1234567";
    Component component = ComponentBuilder.aComponent().withId(componentId).build();
    ComponentViewModel componentViewModel = ComponentViewModelBuilder.aComponentViewModel().build();
    
    // setup mocks
    Mockito.when(componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber)).thenReturn(component);
    Mockito.when(componentFactory.createComponentViewModel(component)).thenReturn(componentViewModel);
    
    // SUT
    componentControllerService.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    
    // verify
    Mockito.verify(componentRepository).findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    Mockito.verify(componentFactory).createComponentViewModel(component);
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumber_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    String donationIdentificationNumber = "1234567";
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(1L).build(),
        ComponentBuilder.aComponent().withId(2L).build()
    );
    List<ComponentManagementViewModel> componentViewModels = Arrays.asList(
        ComponentManagementViewModelBuilder.aComponentManagementViewModel().build(),
        ComponentManagementViewModelBuilder.aComponentManagementViewModel().build()
    );
    
    // setup mocks
    Mockito.when(componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber)).thenReturn(components);
    Mockito.when(componentFactory.createManagementViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    
    // verify
    Mockito.verify(componentRepository).findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    Mockito.verify(componentFactory).createManagementViewModels(components);
  }
  
  @Test
  public void testFindAnyComponent_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    String donationIdentificationNumber = "1234567";
    List<Long> componentTypeIds = Arrays.asList(1L, 2L);
    List<ComponentStatus> statusStringToComponentStatus = Arrays.asList(ComponentStatus.AVAILABLE);
    Date dateFrom = new Date();
    Date dateTo = new Date();
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(1L).build(),
        ComponentBuilder.aComponent().withId(2L).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        ComponentViewModelBuilder.aComponentViewModel().build(),
        ComponentViewModelBuilder.aComponentViewModel().build()
    );
    
    // setup mocks
    Mockito.when(componentRepository.findAnyComponent(donationIdentificationNumber, componentTypeIds, 
        statusStringToComponentStatus, dateFrom, dateTo)).thenReturn(components);
    Mockito.when(componentFactory.createComponentViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.findAnyComponent(donationIdentificationNumber, componentTypeIds, 
        statusStringToComponentStatus, dateFrom, dateTo);
    
    // verify
    Mockito.verify(componentRepository).findAnyComponent(donationIdentificationNumber, componentTypeIds, 
        statusStringToComponentStatus, dateFrom, dateTo);
    Mockito.verify(componentFactory).createComponentViewModels(components);
  }
  
  @Test
  public void testDiscardComponent_shouldCallServiceRepositoryAndFactory() throws Exception {
    // setup data
    Long id = 1L;
    Long discardReasonId = 1L;
    String discardReasonText = "Other reasons";
    String donationIdentificationNumber = "1234567";
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(1L)
          .withDonation(DonationBuilder.aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
          .build(),
        ComponentBuilder.aComponent().withId(2L).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        ComponentViewModelBuilder.aComponentViewModel().build(),
        ComponentViewModelBuilder.aComponentViewModel().build()
    );
    
    // setup mocks       
    Mockito.when(componentCRUDService.discardComponent(id, discardReasonId, discardReasonText)).thenReturn(components.get(0));
    Mockito.when(componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber)).thenReturn(components);
    Mockito.when(componentFactory.createComponentViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.discardComponent(id, discardReasonId, discardReasonText);
    
    // verify
    Mockito.verify(componentCRUDService).discardComponent(id, discardReasonId, discardReasonText);
    Mockito.verify(componentRepository).findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    Mockito.verify(componentFactory).createComponentViewModels(components);
  }
  
  @Test
  public void testProcessComponent_shouldCallServiceRepositoryAndFactory() throws Exception {
    // setup data
    String donationIdentificationNumber = "1234567";
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(1L)
          .withDonation(DonationBuilder.aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
          .build(),
        ComponentBuilder.aComponent().withId(2L).build()
    );
    List<ComponentManagementViewModel> componentViewModels = Arrays.asList(
        ComponentManagementViewModelBuilder.aComponentManagementViewModel().build(),
        ComponentManagementViewModelBuilder.aComponentManagementViewModel().build()
    );
    RecordComponentBackingForm form = new RecordComponentBackingForm();
    form.setParentComponentId("1");
    form.setComponentTypeCombination(new ComponentTypeCombination());
    
    // setup mocks   
    Mockito.when(componentCRUDService.processComponent(form.getParentComponentId(), form.getComponentTypeCombination())).thenReturn(components.get(0));
    Mockito.when(componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber)).thenReturn(components);
    Mockito.when(componentFactory.createManagementViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.processComponent(form);
    
    // verify
    Mockito.verify(componentCRUDService).processComponent(form.getParentComponentId(), form.getComponentTypeCombination());
    Mockito.verify(componentRepository).findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    Mockito.verify(componentFactory).createManagementViewModels(components);
  }
  
  @Test
  public void testGetComponentTypes_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    List<ComponentType> componentTypes = Arrays.asList(
        ComponentTypeBuilder.aComponentType().withId(1L).build(),
        ComponentTypeBuilder.aComponentType().withId(2L).build()
    );
    List<ComponentTypeViewModel> componentTypeViewModels = Arrays.asList(
        new ComponentTypeViewModel(componentTypes.get(0)),
        new ComponentTypeViewModel(componentTypes.get(1))
    );
    
    // setup mocks
    Mockito.when(componentTypeRepository.getAllComponentTypes()).thenReturn(componentTypes);
    Mockito.when(componentTypeFactory.createViewModels(componentTypes)).thenReturn(componentTypeViewModels);
    
    // SUT
    componentControllerService.getComponentTypes();
    
    // verify
    Mockito.verify(componentTypeRepository).getAllComponentTypes();
    Mockito.verify(componentTypeFactory).createViewModels(componentTypes);
  }
  
  @Test
  public void testGetDiscardReasons_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    ComponentStatusChangeReason reason = new ComponentStatusChangeReason();
    List<ComponentStatusChangeReason> reasons = Arrays.asList(reason, reason);
    
    // setup mocks
    when(componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED)).thenReturn(reasons);
    
    // SUT
    componentControllerService.getDiscardReasons();
    
    // verify
    Mockito.verify(componentStatusChangeReasonRepository).getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
    Mockito.verify(componentStatusChangeReasonFactory).createDiscardReasonViewModels(reasons);
  }
  
  @Test
  public void testGetReturnReasons_shouldCallRepository() throws Exception {
    // setup data
    List<ComponentStatusChangeReason> reasons = Arrays.asList(
        new ComponentStatusChangeReason(),
        new ComponentStatusChangeReason()
    );
    
    // setup mocks
    Mockito.when(componentStatusChangeReasonRepository.getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.RETURNED)).thenReturn(reasons);
    
    // SUT
    componentControllerService.getReturnReasons();
    
    // verify
    Mockito.verify(componentStatusChangeReasonRepository).getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.RETURNED);
  }
  
  @Test
  public void testUpdateComponent_shouldCallServiceRepositoryAndFactory() throws Exception {
    // setup data
    Long componentId = Long.valueOf(1);
    ComponentBackingForm backingForm = aComponentBackingForm().withId(componentId).withWeight(123).build();
    Component component = aComponent().withId(componentId).withWeight(123).build();
    
    // setup mocks
    Mockito.when(componentFactory.createEntity(backingForm)).thenReturn(component);
    Mockito.when(componentCRUDService.updateComponent(component)).thenReturn(component);
    Mockito.when(componentFactory.createManagementViewModel(component))
    .thenReturn(ComponentManagementViewModelBuilder.aComponentManagementViewModel().build());
    
    // SUT
    componentControllerService.updateComponent(backingForm);
    
    // verify
    Mockito.verify(componentFactory).createEntity(backingForm);
    Mockito.verify(componentCRUDService).updateComponent(component);
    Mockito.verify(componentFactory).createManagementViewModel(component);
  }
  
  @Test
  public void testUndiscardComponents_shouldUndiscardAndReturnComponents() {
    // Set up fixture
    long firstComponentId = 1L;
    long secondComponentId = 3L;
    
    Component firstComponent = aComponent().withId(firstComponentId).build();
    Component secondComponent = aComponent().withId(secondComponentId).build();
    
    ComponentManagementViewModel firstComponentViewModel = aComponentManagementViewModel().withId(firstComponentId).build();
    ComponentManagementViewModel secondComponentViewModel = aComponentManagementViewModel().withId(secondComponentId).build();

    List<Long> componentIds = Arrays.asList(firstComponentId, secondComponentId);
    
    // Set up expectations
    List<ComponentManagementViewModel> expectedViewModels = Arrays.asList(firstComponentViewModel, secondComponentViewModel);
    
    when(componentCRUDService.undiscardComponent(firstComponentId)).thenReturn(firstComponent);
    when(componentCRUDService.undiscardComponent(secondComponentId)).thenReturn(secondComponent);
    when(componentFactory.createManagementViewModel(firstComponent)).thenReturn(firstComponentViewModel);
    when(componentFactory.createManagementViewModel(secondComponent)).thenReturn(secondComponentViewModel);
    
    // Exercise SUT
    List<ComponentManagementViewModel> returnedViewModels = componentControllerService.undiscardComponents(componentIds);
    
    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }
}
