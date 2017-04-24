package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentManagementViewModelBuilder.aComponentManagementViewModel;
import static org.jembi.bsis.helpers.builders.ComponentPreProcessingBackingFormBuilder.aComponentBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBackingFormBuilder.aComponentTypeCombinationBackingForm;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.ComponentViewModelBuilder.aComponentViewModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;
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
  
  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();

  @Test
  public void testFindComponentById_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    Component component = ComponentBuilder.aComponent().withId(COMPONENT_ID_1).build();
    ComponentFullViewModel componentFullViewModel = ComponentFullViewModelBuilder.aComponentFullViewModel().build();
    
    // setup mocks
    Mockito.when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    Mockito.when(componentFactory.createComponentFullViewModel(component)).thenReturn(componentFullViewModel);
    
    // SUT
    componentControllerService.findComponentById(COMPONENT_ID_1);
    
    // verify
    Mockito.verify(componentRepository).findComponentById(COMPONENT_ID_1);
    Mockito.verify(componentFactory).createComponentFullViewModel(component);
  }
  
  @Test
  public void testFindComponentByCodeAndDIN_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    String componentCode = "1234-01";
    String donationIdentificationNumber = "1234567";
    Component component = ComponentBuilder.aComponent().withId(COMPONENT_ID_1).build();
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
        ComponentBuilder.aComponent().withId(COMPONENT_ID_1).build(),
        ComponentBuilder.aComponent().withId(COMPONENT_ID_2).build()
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
        aComponent().withId(COMPONENT_ID_1).build(),
        aComponent().withId(COMPONENT_ID_2).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        aComponentViewModel().withId(COMPONENT_ID_1).build(), 
        aComponentViewModel().withId(COMPONENT_ID_2).build()
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
        aComponent().withId(COMPONENT_ID_1).withStatus(status).build(),
        aComponent().withId(COMPONENT_ID_2).withStatus(status).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        aComponentViewModel().withId(COMPONENT_ID_1).withStatus(status).build(),
        aComponentViewModel().withId(COMPONENT_ID_2).withStatus(status).build()
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
    List<UUID> componentTypeIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    ComponentStatus status = ComponentStatus.AVAILABLE;
    Date dateFrom = new Date();
    Date dateTo = new Date();
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(COMPONENT_ID_1).build(),
        ComponentBuilder.aComponent().withId(COMPONENT_ID_2).build()
    );
    List<ComponentViewModel> componentViewModels = Arrays.asList(
        aComponentViewModel().build(),
        aComponentViewModel().build()
    );
    
    // setup mocks
    Mockito.when(componentRepository.findAnyComponent(componentTypeIds, status, dateFrom, dateTo, null)).thenReturn(components);
    Mockito.when(componentFactory.createComponentViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.findAnyComponent(componentTypeIds, status, dateFrom, dateTo, null);
    
    // verify
    Mockito.verify(componentRepository).findAnyComponent(componentTypeIds, status, dateFrom,
        dateTo, null);
    Mockito.verify(componentFactory).createComponentViewModels(components);
  }
  
  @Test
  public void testProcessComponent_shouldCallServiceRepositoryAndFactory() throws Exception {
    // setup data
    String donationIdentificationNumber = "1234567";
    Date processedOn = new Date();
    List<Component> components = Arrays.asList(
        ComponentBuilder.aComponent().withId(COMPONENT_ID_1)
          .withDonation(DonationBuilder.aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
          .build(),
        ComponentBuilder.aComponent().withId(COMPONENT_ID_2).build()
    );
    List<ComponentManagementViewModel> componentViewModels = Arrays.asList(
        ComponentManagementViewModelBuilder.aComponentManagementViewModel().build(),
        ComponentManagementViewModelBuilder.aComponentManagementViewModel().build()
    );
    RecordComponentBackingForm form = new RecordComponentBackingForm();
    form.setParentComponentId(COMPONENT_ID_1);
    form.setComponentTypeCombination(aComponentTypeCombinationBackingForm().withId(UUID.randomUUID()).build());
    form.setProcessedOn(processedOn);
    
    // setup mocks   
    when(componentCRUDService.processComponent(form.getParentComponentId(), form.getComponentTypeCombination().getId(),
        processedOn)).thenReturn(components.get(0));
    when(componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber)).thenReturn(components);
    when(componentFactory.createManagementViewModels(components)).thenReturn(componentViewModels);
    
    // SUT
    componentControllerService.processComponent(form);
    
    // verify
    verify(componentCRUDService).processComponent(form.getParentComponentId(),
        form.getComponentTypeCombination().getId(), processedOn);
    verify(componentRepository).findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    verify(componentFactory).createManagementViewModels(components);
  }
  
  @Test
  public void testGetComponentTypes_shouldCallRepositoryAndFactory() throws Exception {
    // setup data
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeId2 = UUID.randomUUID();
    List<ComponentType> componentTypes = Arrays.asList(
        ComponentTypeBuilder.aComponentType().withId(componentTypeId1).build(),
        ComponentTypeBuilder.aComponentType().withId(componentTypeId2).build()
    );
    List<ComponentTypeViewModel> componentTypeViewModels = Arrays.asList(
        aComponentTypeViewModel().withId(componentTypeId1).build(),
        aComponentTypeViewModel().withId(componentTypeId2).build()
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
    UUID componentId = UUID.randomUUID();
    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Integer componentWeight = Integer.valueOf(123);
    ComponentPreProcessingBackingForm backingForm = aComponentBackingForm()
        .withId(componentId)
        .withWeight(componentWeight)
        .withBleedStartTime(dateFormat.parse("2016-12-14 08:10"))
        .withBleedEndTime(dateFormat.parse("2016-12-14 08:20"))
        .build();
    Component component = aComponent().withId(componentId).withWeight(componentWeight).build();
    
    // setup mocks
    Mockito.when(componentCRUDService.preProcessComponent(componentId, componentWeight, backingForm.getBleedStartTime(), backingForm.getBleedEndTime())).thenReturn(component);
    Mockito.when(componentFactory.createManagementViewModel(component))
    .thenReturn(ComponentManagementViewModelBuilder.aComponentManagementViewModel().build());
    
    // SUT
    componentControllerService.preProcessComponent(backingForm);
    
    // verify
    Mockito.verify(componentCRUDService).preProcessComponent(componentId, componentWeight, backingForm.getBleedStartTime(), backingForm.getBleedEndTime());
    Mockito.verify(componentFactory).createManagementViewModel(component);
  }
  
  @Test
  public void testUndiscardComponents_shouldUndiscardAndReturnComponents() {
    // Set up fixture
    UUID firstComponentId = UUID.randomUUID();
    UUID secondComponentId = UUID.randomUUID();
    
    Component firstComponent = aComponent().withId(firstComponentId).build();
    Component secondComponent = aComponent().withId(secondComponentId).build();
    
    ComponentManagementViewModel firstComponentFullViewModel = aComponentManagementViewModel().withId(firstComponentId).build();
    ComponentManagementViewModel secondComponentFullViewModel = aComponentManagementViewModel().withId(secondComponentId).build();

    List<UUID> componentIds = Arrays.asList(firstComponentId, secondComponentId);
    
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
