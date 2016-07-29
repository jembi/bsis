package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentManagementViewModelBuilder.aComponentManagementViewModel;
import static org.jembi.bsis.helpers.builders.ComponentViewModelBuilder.aComponentViewModel;
import static org.mockito.Mockito.verify;
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
import org.jembi.bsis.helpers.builders.ComponentFullViewModelBuilder;
import org.jembi.bsis.helpers.builders.ComponentManagementViewModelBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
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
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
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
    ComponentFullViewModel componentFullViewModel = ComponentFullViewModelBuilder.aComponentFullViewModel().build();
    
    // setup mocks
    Mockito.when(componentRepository.findComponentById(componentId)).thenReturn(component);
    Mockito.when(componentFactory.createComponentFullViewModel(component)).thenReturn(componentFullViewModel);
    
    // SUT
    componentControllerService.findComponentById(1L);
    
    // verify
    Mockito.verify(componentRepository).findComponentById(componentId);
    Mockito.verify(componentFactory).createComponentFullViewModel(component);
  }
  
  @Test
  public void testFindComponentByCodeAndDIN_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    Long componentId = 1L;
    String componentCode = "1234-01";
    String donationIdentificationNumber = "1234567";
    Component component = ComponentBuilder.aComponent().withId(componentId).build();
    ComponentFullViewModel componentFullViewModel = ComponentFullViewModelBuilder.aComponentFullViewModel().build();
    
    // setup mocks
    Mockito.when(componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber)).thenReturn(component);
    Mockito.when(componentFactory.createComponentFullViewModel(component)).thenReturn(componentFullViewModel);
    
    // SUT
    componentControllerService.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    
    // verify
    Mockito.verify(componentRepository).findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);
    Mockito.verify(componentFactory).createComponentFullViewModel(component);
  }
  
  @Test
  public void testFindManagementComponentsByDonationIdentificationNumber_shouldCallRepositoryAndFactory() throws Exception {
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
    componentControllerService.findManagementComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    
    // verify
    Mockito.verify(componentRepository).findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    Mockito.verify(componentFactory).createManagementViewModels(components);
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumber_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    String donationIdentificationNumber = "1234567";
    List<Component> components = Arrays.asList(
        aComponent().withId(1L).build(),
        aComponent().withId(2L).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        aComponentViewModel().withId(1L).build(), 
        aComponentViewModel().withId(2L).build()
    );
    
    // setup mocks
    when(componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber)).thenReturn(components);
    when(componentFactory.createComponentViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    List<ComponentViewModel> returnedViewModels = componentControllerService.findComponentsByDonationIdentificationNumber(
        donationIdentificationNumber);
    
    // verify
    verify(componentRepository).findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    verify(componentFactory).createComponentViewModels(components);
    assertThat(returnedViewModels, is(componentViewModels));
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumberAndStatus_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    String donationIdentificationNumber = "1234567";
    ComponentStatus status = ComponentStatus.DISCARDED;
    List<Component> components = Arrays.asList(
        aComponent().withId(1L).withStatus(status).build(),
        aComponent().withId(2L).withStatus(status).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        aComponentViewModel().withId(1L).withStatus(status).build(),
        aComponentViewModel().withId(2L).withStatus(status).build()
    );
    
    // setup mocks
    when(componentRepository.findComponentsByDonationIdentificationNumberAndStatus(donationIdentificationNumber, status))
        .thenReturn(components);
    when(componentFactory.createComponentViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    List<ComponentViewModel> returnedViewModels = componentControllerService.findComponentsByDonationIdentificationNumberAndStatus(
        donationIdentificationNumber, status);
    
    // verify
    assertThat(returnedViewModels, is(componentViewModels));
  }
  
  @Test
  public void testFindAnyComponent_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    List<Long> componentTypeIds = Arrays.asList(1L, 2L);
    ComponentStatus status = ComponentStatus.AVAILABLE;
    Date dateFrom = new Date();
    Date dateTo = new Date();
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(1L).build(),
        ComponentBuilder.aComponent().withId(2L).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        aComponentViewModel().build(),
        aComponentViewModel().build()
    );
    
    // setup mocks
    Mockito.when(componentRepository.findAnyComponent(componentTypeIds, status, dateFrom, dateTo)).thenReturn(components);
    Mockito.when(componentFactory.createComponentViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.findAnyComponent(componentTypeIds, status, dateFrom, dateTo);
    
    // verify
    Mockito.verify(componentRepository).findAnyComponent(componentTypeIds, status, dateFrom,
        dateTo);
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
  public void testRecordComponentWeight_shouldCallServiceRepositoryAndFactory() throws Exception {
    // setup data
    Long componentId = Long.valueOf(1);
    int componentWeight = 123;
    ComponentBackingForm backingForm = aComponentBackingForm().withId(componentId).withWeight(componentWeight).build();
    Component component = aComponent().withId(componentId).withWeight(componentWeight).build();
    
    // setup mocks
    Mockito.when(componentFactory.createEntity(backingForm)).thenReturn(component);
    Mockito.when(componentCRUDService.recordComponentWeight(componentId, componentWeight)).thenReturn(component);
    Mockito.when(componentFactory.createManagementViewModel(component))
    .thenReturn(ComponentManagementViewModelBuilder.aComponentManagementViewModel().build());
    
    // SUT
    componentControllerService.recordComponentWeight(backingForm);
    
    // verify
    Mockito.verify(componentFactory).createEntity(backingForm);
    Mockito.verify(componentCRUDService).recordComponentWeight(componentId, componentWeight);
    Mockito.verify(componentFactory).createManagementViewModel(component);
  }
  
  @Test
  public void testUndiscardComponents_shouldUndiscardAndReturnComponents() {
    // Set up fixture
    long firstComponentId = 1L;
    long secondComponentId = 3L;
    
    Component firstComponent = aComponent().withId(firstComponentId).build();
    Component secondComponent = aComponent().withId(secondComponentId).build();
    
    ComponentManagementViewModel firstComponentFullViewModel = aComponentManagementViewModel().withId(firstComponentId).build();
    ComponentManagementViewModel secondComponentFullViewModel = aComponentManagementViewModel().withId(secondComponentId).build();

    List<Long> componentIds = Arrays.asList(firstComponentId, secondComponentId);
    
    // Set up expectations
    List<ComponentManagementViewModel> expectedViewModels = Arrays.asList(firstComponentFullViewModel, secondComponentFullViewModel);
    
    when(componentCRUDService.undiscardComponent(firstComponentId)).thenReturn(firstComponent);
    when(componentCRUDService.undiscardComponent(secondComponentId)).thenReturn(secondComponent);
    when(componentFactory.createManagementViewModel(firstComponent)).thenReturn(firstComponentFullViewModel);
    when(componentFactory.createManagementViewModel(secondComponent)).thenReturn(secondComponentFullViewModel);
    
    // Exercise SUT
    List<ComponentManagementViewModel> returnedViewModels = componentControllerService.undiscardComponents(componentIds);
    
    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }
}
