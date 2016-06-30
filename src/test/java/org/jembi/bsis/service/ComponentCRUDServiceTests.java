package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeType;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.service.ComponentCRUDService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentCRUDServiceTests extends UnitTestSuite {

  @Spy
  @InjectMocks
  private ComponentCRUDService componentCRUDService;
  @Mock
  private ComponentRepository componentRepository;
  @Mock
  private ComponentFactory componentFactory;
  @Mock
  private ComponentTypeRepository componentTypeRepository;
  @Mock
  private ComponentStatusCalculator componentStatusCalculator;
  @Mock
  private ComponentConstraintChecker componentConstraintChecker;

  @Test
  public void testMarkComponentsBelongingToDonorAsUnsafe_shouldDelegateToRepositoryWithCorrectParameters() {

    Donor donor = aDonor().build();

    componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donor);

    verify(componentRepository).updateComponentStatusesForDonor(
        Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED), ComponentStatus.UNSAFE, donor);
  }

  @Test
  public void testMarkComponentsBelongingToDonationAsUnsafe_shouldDelegateToRepositoryWithCorrectParameters() {

    Donation donation = aDonation().build();

    componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);

    verify(componentRepository).updateComponentStatusForDonation(
        Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED), ComponentStatus.UNSAFE,
        donation);
  }

  @Test
  public void testDiscardComponent() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).build();
    Long discardReasonId = 1L;
    String reasonText = "junit";
    
    // set up mocks
    when(componentRepository.findComponentById(1L)).thenReturn(component);
    when(componentRepository.update(component)).thenReturn(component);
    
    // run test
    componentCRUDService.discardComponent(1L, 1L, reasonText);
    
    // check asserts
    Assert.assertEquals("Component status is discarded", ComponentStatus.DISCARDED, component.getStatus());
    Assert.assertNotNull("Status change has been set", component.getStatusChanges());
    Assert.assertEquals("Status change has been set", 1, component.getStatusChanges().size());
    ComponentStatusChange statusChange = component.getStatusChanges().get(0);
    Assert.assertEquals("Status change is correct", ComponentStatusChangeType.DISCARDED, statusChange.getStatusChangeType());
    Assert.assertEquals("Status change is correct", ComponentStatus.DISCARDED, statusChange.getNewStatus());
    Assert.assertEquals("Status change is correct", discardReasonId, statusChange.getStatusChangeReason().getId());
    Assert.assertEquals("Status change is correct", reasonText, statusChange.getStatusChangeReasonText());
    Assert.assertEquals("Status change is correct", loggedInUser, statusChange.getChangedBy());
  }
  
  @Test
  public void testDiscardComponents() throws Exception {
    // set up data
    Component component1 = aComponent().withStatus(ComponentStatus.DISCARDED).withId(1L).build();
    Component component2 = aComponent().withStatus(ComponentStatus.DISCARDED).withId(2L).build();
    Component component3 = aComponent().withStatus(ComponentStatus.DISCARDED).withId(3L).build();
    List<Long> componentIds = Arrays.asList(1L,2L,3L);
    Long discardReasonId = 1L;
    String reasonText = "junit";

    // set up mocks (because we already tested discardComponent we use a "spy" mockup of
    // discardComponent to test discardComponents)
    doReturn(component1).when(componentCRUDService).discardComponent(1L, discardReasonId, reasonText);
    doReturn(component2).when(componentCRUDService).discardComponent(2L, discardReasonId, reasonText);
    doReturn(component3).when(componentCRUDService).discardComponent(3L, discardReasonId, reasonText);

    // run test
    componentCRUDService.discardComponents(componentIds, discardReasonId, reasonText);

    // Verify
    verify(componentCRUDService, times(1)).discardComponent(componentIds.get(0), discardReasonId, reasonText);
    verify(componentCRUDService, times(1)).discardComponent(componentIds.get(1), discardReasonId, reasonText);
    verify(componentCRUDService, times(1)).discardComponent(componentIds.get(2), discardReasonId, reasonText);
  }

  @Test
  public void testDiscardInStockComponent() throws Exception {
    // set up data
    Component component = aComponent().withId(1L).withInventoryStatus(InventoryStatus.IN_STOCK).build();
    
    // set up mocks
    when(componentRepository.findComponentById(1L)).thenReturn(component);
    when(componentRepository.update(component)).thenReturn(component);
    
    // run test
    componentCRUDService.discardComponent(1L, 1L, "junit");
    
    // check asserts
    Assert.assertEquals("Component is now removed from stock", InventoryStatus.REMOVED, component.getInventoryStatus());
  }
  
  @Test
  public void testProcessComponent_shouldCreateChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Long componentTypeId1 = Long.valueOf(1);
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    Long componentTypeId2 = Long.valueOf(2);
    ComponentType componentType2 = aComponentType().withId(componentTypeId2)
        .withComponentTypeCode("0002")
        .withComponentTypeName("#2")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.HOURS)
        .build();
    Long componentTypeId3 = Long.valueOf(3);
    ComponentType componentType3 = aComponentType().withId(componentTypeId3)
        .withComponentTypeCode("0003")
        .withComponentTypeName("#3")
        .withExpiresAfter(1)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.YEARS)
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(1L)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    Long parentComponentId = Long.valueOf(1);
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(1L)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1, componentType1, componentType2, componentType3))
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedComponent1 = aComponent()
        .withComponentType(componentType1)
        .withComponentCode("0001-01")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .build();
    Component expectedComponent2 = aComponent()
        .withComponentType(componentType1)
        .withComponentCode("0001-02")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .build();
    Calendar expiryCal2 = Calendar.getInstance();
    expiryCal2.setTime(donationDate);
    expiryCal2.add(Calendar.HOUR_OF_DAY, 90);
    Component expectedComponent3 = aComponent()
        .withComponentType(componentType2)
        .withComponentCode("0002")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal2.getTime())
        .withLocation(location)
        .build();
    Calendar expiryCal3 = Calendar.getInstance();
    expiryCal3.setTime(donationDate);
    expiryCal3.add(Calendar.YEAR, 1);
    Component expectedComponent4 = aComponent()
        .withComponentType(componentType3)
        .withComponentCode("0003")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal3.getTime())
        .withLocation(location)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(componentType2);
    when(componentTypeRepository.getComponentTypeById(componentTypeId3)).thenReturn(componentType3);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId.toString(), componentTypeCombination);
    
    // verify results
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedParentComponent)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent1)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent2)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent3)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent4)));
  }
  
  @Test
  public void testProcessInStockComponent_shouldUpdateParentComponentToRemoved() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Long componentTypeId1 = Long.valueOf(1);
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(1L)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    Long parentComponentId = Long.valueOf(1);
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(1L)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedComponent1 = aComponent()
        .withComponentType(componentType1)
        .withComponentCode("0001")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId.toString(), componentTypeCombination);
    
    // verify results
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedParentComponent)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent1)));
  }
  
  @Test
  public void testProcessUnsafeComponent_shouldCreateUnsafeChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Long componentTypeId1 = Long.valueOf(1);
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(1L)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    Long parentComponentId = Long.valueOf(1);
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.UNSAFE)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(1L)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedComponent1 = aComponent()
        .withComponentType(componentType1)
        .withComponentCode("0001")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.UNSAFE)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId.toString(), componentTypeCombination);
    
    // verify results
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedParentComponent)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent1)));
  }
  
  @Test
  public void testProcessProcessedComponent_shouldNotCreateChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Long componentTypeId1 = Long.valueOf(1);
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(1L)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    Long parentComponentId = Long.valueOf(1);
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.PROCESSED)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(1L)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId.toString(), componentTypeCombination);
    
    // verify results
    verify(componentRepository, times(0)).save(Mockito.any(Component.class));
  }
  
  @Test
  public void testProcessDiscardedComponent_shouldNotCreateChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Long componentTypeId1 = Long.valueOf(1);
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(1L)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    Long parentComponentId = Long.valueOf(1);
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withStatus(ComponentStatus.DISCARDED)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(1L)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId.toString(), componentTypeCombination);
    
    // verify results
    verify(componentRepository, times(0)).save(Mockito.any(Component.class));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testProcessComponentThatCannotBeProcessed_shouldThrow() {
    // set up data
    Long parentComponentId = 11L;
    Component parentComponent = aComponent().withId(parentComponentId).build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(1L)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(aComponentType().build()))
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(false);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId.toString(), componentTypeCombination);
  }
  
  @Test
  public void testFindComponentById_shouldCallRepository() throws Exception {
    // set up data
    Long id = Long.valueOf(1);
    
    // mocks
    when(componentRepository.findComponentById(id)).thenReturn(aComponent().build());
    
    // SUT
    componentCRUDService.findComponentById(id);
    
    // check
    verify(componentRepository).findComponentById(id);
  }
  
  @Test
  public void testFindComponentsByDINAndType_shouldCallRepository() throws Exception {
    // set up data
    String din = String.valueOf("1234567");
    Long id = Long.valueOf(1);
    
    // mocks
    when(componentRepository.findComponentsByDINAndType(din, id)).thenReturn(Arrays.asList(aComponent().build()));
    
    // SUT
    componentCRUDService.findComponentsByDINAndType(din, id);
    
    // check
    verify(componentRepository).findComponentsByDINAndType(din, id);
  }
  
  @Test(expected = java.lang.IllegalStateException.class)
  public void testUpdateComponentWeight_shouldThrowException() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    Component oldComponent = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.PROCESSED)
        .build();
    Component component = aComponent()
        .withId(componentId)
        .withWeight(320)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // mocks
    when(componentRepository.findComponentById(componentId)).thenReturn(oldComponent);
    when(componentConstraintChecker.canRecordWeight(component)).thenReturn(false);
    
    // SUT
    componentCRUDService.updateComponent(component);
  }

  @Test
  public void testUpdateComponentWeight_shouldDiscardComponent() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    Component oldComponent = aComponent()
        .withId(componentId)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    Component component = aComponent()
        .withId(componentId)
        .withWeight(320)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // mocks
    when(componentRepository.findComponentById(componentId)).thenReturn(oldComponent);
    when(componentConstraintChecker.canRecordWeight(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscarded(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.updateComponentStatus(oldComponent)).thenReturn(false);
    when(componentRepository.update(oldComponent)).thenReturn(oldComponent);
    
    // SUT
    Component updatedComponent = componentCRUDService.updateComponent(component);
    
    // check
    assertThat("Component was flagged for discard", updatedComponent.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testUpdateComponentWeight_shouldNotDiscardComponent() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    Component oldComponent = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.QUARANTINED)
        .build();
    Component component = aComponent()
        .withId(componentId)
        .withStatus(ComponentStatus.QUARANTINED)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // mocks
    when(componentRepository.findComponentById(componentId)).thenReturn(oldComponent);
    when(componentConstraintChecker.canRecordWeight(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscarded(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.updateComponentStatus(oldComponent)).thenReturn(false);
    when(componentRepository.update(oldComponent)).thenReturn(oldComponent);
    
    // SUT
    Component updatedComponent = componentCRUDService.updateComponent(component);
    
    // check
    assertThat("Component was not flagged for discard", updatedComponent.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponent_shouldJustSave() throws Exception {
    // set up data
    Long componentId = Long.valueOf(1);
    Component component = aComponent()
        .withId(componentId)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(componentId)).thenReturn(component);
    when(componentStatusCalculator.shouldComponentBeDiscarded(component)).thenReturn(false);
    when(componentStatusCalculator.updateComponentStatus(component)).thenReturn(false);
    when(componentRepository.update(component)).thenReturn(component);
    
    // SUT
    componentCRUDService.updateComponent(component);
    
    // check
    verify(componentRepository).update(component);
  }
}
