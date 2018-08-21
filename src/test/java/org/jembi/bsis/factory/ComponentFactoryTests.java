package org.jembi.bsis.factory;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.component.ComponentStatusChangeRepository;
import org.jembi.bsis.service.ComponentConstraintChecker;
import org.jembi.bsis.service.ComponentStatusCalculator;
import org.jembi.bsis.util.RandomTestDate;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentFullViewModelBuilder.aComponentFullViewModel;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeFullViewModelBuilder.aComponentTypeFullViewModel;
import static org.jembi.bsis.helpers.builders.ComponentTypeViewModelBuilder.aComponentTypeViewModel;
import static org.jembi.bsis.helpers.builders.ComponentViewModelBuilder.aComponentViewModel;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationViewModelBuilder.aLocationViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentFullViewModelMatcher.hasSameStateAsComponentFullViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentManagementViewModelMatcher.hasSameStateAsComponentManagementViewModel;
import static org.jembi.bsis.helpers.matchers.ComponentViewModelMatcher.hasSameStateAsComponentViewModel;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ComponentFactoryTests {

  @Mock private LocationFactory locationFactory;
  @Mock private PackTypeFactory packTypeFactory;
  @Mock private ComponentRepository componentRepository;
  @Mock private ComponentTypeFactory componentTypeFactory;
  @Mock private ComponentStatusCalculator componentStatusCalculator;
  @Mock private ComponentConstraintChecker componentConstraintChecker;
  @Mock private ComponentStatusChangeRepository statusChangeRepository;

  @InjectMocks private ComponentFactory componentFactory;

  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();

  @Test
  public void createComponentFullViewModel_oneComponent() throws Exception {
    // set up data
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").build();
    UUID locationId = UUID.randomUUID();
    Location location = aLocation().withId(locationId).build();

    Component parentComponent = aComponent().withId(COMPONENT_ID_2).build();
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType()
        .withId(componentTypeId)
        .withComponentTypeName("name")
        .withComponentTypeCode("0000")
        .build();
    
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
          .withId(componentType.getId())
          .withComponentTypeName(componentType.getComponentTypeName())
          .withComponentTypeCode(componentType.getComponentTypeCode())
          .build();
    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();
    
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withComponentType(componentType)
        .withLocation(location)
        .withDonation(donation)
        .withParentComponent(parentComponent)
        .withExpiresOn(new RandomTestDate())
        .build();
    
    ComponentFullViewModel expectedViewModel = aComponentFullViewModel()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withComponentType(componentTypeViewModel)
        .withLocation(locationViewModel)
        .withBloodAbo(donation.getBloodAbo())
        .withBloodRh(donation.getBloodRh())
        .withCreatedOn(component.getCreatedOn())
        .withExpiresOn(component.getExpiresOn())
        .withDaysToExpire(3)
        .thatIsNotInitialComponent()
        .build();

    // setup mocks
    when(locationFactory.createViewModel(location)).thenReturn(locationViewModel);
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(componentTypeViewModel);
    when(componentStatusCalculator.getDaysToExpire(component)).thenReturn(expectedViewModel.getDaysToExpire());

    // run test
    ComponentFullViewModel convertedViewModel = componentFactory.createComponentFullViewModel(component);
    
    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentFullViewModel(expectedViewModel));
  }
  
  @Test
  public void createComponentFullViewModels_componentList() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").build();
    components.add(aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withExpiresOn(new RandomTestDate())
        .build());
    components.add(aComponent()
        .withId(COMPONENT_ID_2)
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withExpiresOn(new RandomTestDate())
        .build());
    
    // run test
    List<ComponentFullViewModel> viewModels = componentFactory.createComponentFullViewModels(components);
    
    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertEquals("Correct number of view models created", 2, viewModels.size());
  }
  
  @Test
  public void createComponentFullViewModels_nullCollection() throws Exception {
    // set up data

    // run test
    List<ComponentFullViewModel> viewModels = componentFactory.createComponentFullViewModels(null);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }

  @Test
  public void createComponentManagementViewModels_returnsCollection() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").build();
    Component componentOne = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withExpiresOn(new RandomTestDate())
        .build();
    components.add(componentOne);
    Component componentTwo = aComponent()
        .withId(COMPONENT_ID_2)
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withExpiresOn(new RandomTestDate())
        .build();
    components.add(componentTwo);
    donation.addComponent(components.get(0));
    donation.addComponent(components.get(1));

    when(statusChangeRepository.findLatestDiscardReasonForComponent(any(Component.class))).thenReturn(Optional.empty());

    // run test
    List<ComponentManagementViewModel> viewModels = componentFactory.createManagementViewModels(components);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertEquals("Correct number of view models created", 2, viewModels.size());
  }

  @Test
  public void createComponentManagementViewModelsWithNull_returnsNullCollection() throws Exception {
    // set up data

    // run test
    List<ComponentManagementViewModel> viewModels = componentFactory.createManagementViewModels(null);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertTrue("No view models", viewModels.isEmpty());
  }

  @Test
  public void createManagementViewModel_oneComponent() throws Exception {
    // set up data
    Date createdOn = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(createdOn);
    cal.add(Calendar.DAY_OF_YEAR, 1);
    Date processedOn = cal.getTime();
    cal.add(Calendar.DAY_OF_YEAR, -2);
    Date expiresOn = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Component initialComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withCreatedOn(createdOn)
        .build();
    Donation donation = aDonation()
        .withDonationIdentificationNumber("1234567")
        .withBleedStartTime(sdf.parse("2016-01-01 13:00"))
        .withBleedEndTime(sdf.parse("2016-01-01 13:17"))
        .withComponent(initialComponent)
        .build();
    ComponentType componentType = aComponentType().build();
    ComponentStatusChangeReason reason = aComponentStatusChangeReason().build();
    ComponentStatusChange statusChange = aComponentStatusChange().withStatusChangeReason(reason).build();
    Component component = aComponent()
        .withId(COMPONENT_ID_2)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentCode("0011")
        .withComponentType(componentType)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiresOn)
        .withWeight(222)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .withComponentStatusChange(statusChange)
        .build();

    UUID componentTypeId2 = UUID.randomUUID();
    ComponentTypeFullViewModel componentTypeFullViewModel = aComponentTypeFullViewModel()
        .withId(componentTypeId2)
        .build();
    
    Map<String, Boolean> permissionMap = new HashMap<>();
    permissionMap.put("canDiscard", true);
    permissionMap.put("canProcess", true);
    permissionMap.put("canPreProcess", true);
    permissionMap.put("canUnprocess", true);
    permissionMap.put("canUndiscard", true);
    permissionMap.put("canRecordChildComponentWeight", true);
    
    ComponentManagementViewModel expectedViewModel = ComponentManagementViewModel.builder()
        .id(COMPONENT_ID_2)
        .status(ComponentStatus.AVAILABLE)
        .componentCode("0011")
        .componentType(componentTypeFullViewModel)
        .createdOn(processedOn)
        .expiresOn(expiresOn)
        .weight(222)
        .permissions(permissionMap)
        .daysToExpire(0)
        .batched(false)
        .inventoryStatus(InventoryStatus.IN_STOCK)
        .bleedStartTime(donation.getBleedStartTime())
        .bleedEndTime(donation.getBleedEndTime())
        .donationDateTime(createdOn)
        .parentComponentId(initialComponent.getId())
        .discardReason(reason.getStatusChangeReason())
        .discardReasonComment(statusChange.getStatusChangeReasonText())
        .build();

    // setup mocks
    when(componentTypeFactory.createFullViewModel(componentType)).thenReturn(componentTypeFullViewModel);
    when(statusChangeRepository.findLatestDiscardReasonForComponent(component)).thenReturn(Optional.of(statusChange));
    when(componentConstraintChecker.canDiscard(component)).thenReturn(true);
    when(componentConstraintChecker.canProcess(component)).thenReturn(true);
    when(componentConstraintChecker.canPreProcess(component)).thenReturn(true);
    when(componentConstraintChecker.canUnprocess(component)).thenReturn(true);
    when(componentConstraintChecker.canUndiscard(component)).thenReturn(true);
    when(componentConstraintChecker.canRecordChildComponentWeight(component)).thenReturn(true);

    // run test
    ComponentManagementViewModel convertedViewModel = componentFactory.createManagementViewModel(component);

    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Created correctly", convertedViewModel, hasSameStateAsComponentManagementViewModel(expectedViewModel));
  }

  @Test
  public void createManagementViewModelForInitialComponent_viewModelWithNullParentComponentIdReturned() throws Exception {
    // set up data
    UUID componentTypeId = UUID.randomUUID();
    ComponentType componentType = aComponentType()
        .withId(componentTypeId)
        .build();
    Donation donation = aDonation().build();
    Date expiresOn = new DateTime().minusDays(2).toDate();
    Component initialComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withComponentType(componentType)
        .withDonation(donation)
        .withExpiresOn(expiresOn)
        .build();
    donation.addComponent(initialComponent);

    ComponentTypeFullViewModel componentTypeFullViewModel = aComponentTypeFullViewModel()
        .withId(componentTypeId)
        .build();

    Map<String, Boolean> permissionMap = new HashMap<>();
    permissionMap.put("canDiscard", true);
    permissionMap.put("canProcess", true);
    permissionMap.put("canPreProcess", true);
    permissionMap.put("canUnprocess", true);
    permissionMap.put("canUndiscard", true);
    permissionMap.put("canRecordChildComponentWeight", true);

    ComponentManagementViewModel expectedViewModel = ComponentManagementViewModel.builder()
        .id(COMPONENT_ID_1)
        .status(ComponentStatus.QUARANTINED)
        .componentCode(null)
        .componentType(componentTypeFullViewModel)
        .createdOn(null)
        .expiresOn(initialComponent.getExpiresOn())
        .weight(null)
        .permissions(permissionMap)
        .daysToExpire(0)
        .batched(false)
        .inventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .bleedStartTime(null)
        .bleedEndTime(null)
        .donationDateTime(initialComponent.getCreatedOn())
        .parentComponentId(null)
        .build();

    // setup mocks
    when(componentTypeFactory.createFullViewModel(componentType)).thenReturn(componentTypeFullViewModel);
    when(statusChangeRepository.findLatestDiscardReasonForComponent(initialComponent)).thenReturn(Optional.empty());
    when(componentConstraintChecker.canDiscard(initialComponent)).thenReturn(true);
    when(componentConstraintChecker.canProcess(initialComponent)).thenReturn(true);
    when(componentConstraintChecker.canPreProcess(initialComponent)).thenReturn(true);
    when(componentConstraintChecker.canUnprocess(initialComponent)).thenReturn(true);
    when(componentConstraintChecker.canUndiscard(initialComponent)).thenReturn(true);
    when(componentConstraintChecker.canRecordChildComponentWeight(initialComponent)).thenReturn(true);

    // run test
    ComponentManagementViewModel convertedViewModel = componentFactory.createManagementViewModel(initialComponent);

    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Created correctly", convertedViewModel, hasSameStateAsComponentManagementViewModel(expectedViewModel));
  }

  @Test
  public void createComponentViewModel_oneComponent() throws Exception {
    // set up data
    Date createdOn = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(createdOn);
    cal.add(Calendar.DAY_OF_YEAR, -1);
    Date expiresOn = cal.getTime();

    Donation donation = aDonation()
        .withDonationIdentificationNumber("1234567")
        .withFlagCharacters("09")
        .build();
    ComponentType componentType = aComponentType().build();
    UUID locationId = UUID.randomUUID();
    Location location = aLocation().withId(locationId).build();
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(componentType)
        .withComponentCode("componentCode")
        .withCreatedOn(createdOn)
        .withExpiresOn(expiresOn)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    UUID componentTypeId = UUID.randomUUID();
    ComponentTypeViewModel componentTypeViewModel = aComponentTypeViewModel()
        .withId(componentTypeId)
        .build();

    LocationViewModel locationViewModel = aLocationViewModel().withId(locationId).build();
    
    ComponentViewModel expectedViewModel = aComponentViewModel().withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(componentTypeViewModel)
        .withComponentCode("componentCode")
        .withCreatedOn(createdOn)
        .withExpiresOn(expiresOn)
        .withDonationIdentificationNumber("1234567")
        .withDonationFlagCharacters("09")
        .withDaysToExpire(0)
        .withLocation(locationViewModel)
        .build();

    // setup mocks
    when(componentTypeFactory.createViewModel(componentType)).thenReturn(componentTypeViewModel);
    when(locationFactory.createViewModel(location)).thenReturn(locationViewModel);

    // run test
    ComponentViewModel convertedViewModel = componentFactory.createComponentViewModel(component);

    // do asserts
    Assert.assertNotNull("View model created", convertedViewModel);
    assertThat("Correct view model", convertedViewModel, hasSameStateAsComponentViewModel(expectedViewModel));
  }

  @Test
  public void createComponentViewModels_componentList() throws Exception {
    // set up data
    ArrayList<Component> components = new ArrayList<>();
    components.add(aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withExpiresOn(new RandomTestDate())
        .build());
    components.add(aComponent()
        .withId(COMPONENT_ID_2)
        .withStatus(ComponentStatus.DISCARDED)
        .withExpiresOn(new RandomTestDate())
        .build());

    // run test
    List<ComponentViewModel> viewModels = componentFactory.createComponentViewModels(components);

    // do asserts
    Assert.assertNotNull("View models created", viewModels);
    Assert.assertEquals("Correct number of view models created", 2, viewModels.size());
  }
  
  @Test
  public void createComponentFullViewModelWithNullParentComponent_shouldSetIntialComponent() {
    // set up data
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withParentComponent(null)
        .withExpiresOn(new RandomTestDate())
        .build();
    
    ComponentFullViewModel expectedViewModel = aComponentFullViewModel()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .thatIsInitialComponent()
        .withExpiresOn(component.getExpiresOn())
        .build();

    // run test
    ComponentFullViewModel returnedViewModel = componentFactory.createComponentFullViewModel(component);
  
    assertThat(returnedViewModel, is(hasSameStateAsComponentFullViewModel(expectedViewModel)));
  }
}
