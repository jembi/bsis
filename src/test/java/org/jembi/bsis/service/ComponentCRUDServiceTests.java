package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aDiscardReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aReturnReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.anIssuedReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.anUnsafeReason;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.jembi.bsis.helpers.matchers.ComponentStatusChangeMatcher.hasSameStateAsComponentStatusChange;
import static org.jembi.bsis.helpers.matchers.DonationMatcher.hasSameStateAsDonation;
import static org.jembi.bsis.helpers.matchers.LocationMatcher.hasSameStateAsLocation;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.factory.ComponentFactory;
import org.jembi.bsis.helpers.builders.ComponentBatchBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.matchers.ComponentMatcher;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonType;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentStatusChangeReasonRepository;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

public class ComponentCRUDServiceTests extends UnitTestSuite {
  private static final UUID DONATION_ID_1 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");
  private static final UUID DONATION_ID_2 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a2");
  private static final UUID DONATION_ID_3 = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a3");
  private static final UUID COMPONENT_ID_1 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_2 = UUID.randomUUID();
  private static final UUID COMPONENT_ID_3 = UUID.randomUUID();

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
  @Mock
  private DateGeneratorService dateGeneratorService;
  @Mock
  private ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Mock
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private BleedTimeService bleedTimeService;
  
  @Test
  public void testCreateInitialComponentWithFalseConfig_shouldReturnNull() {
    // Set up data
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();
    Donation donation = aDonation().withPackType(packTypeThatCountsAsDonation).build();

    // Set up mocks
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)).thenReturn(false);

    // Run test
    Component component = componentCRUDService.createInitialComponent(donation);
    
    // Verify
    assertThat("Component is null", component == null);
  }
  
  @Test
  public void testCreateInitialComponent_shouldCreateCorrectComponent() {
    // Set up data
    Integer expiresAfterDays = 12;
    ComponentType componentType = ComponentTypeBuilder.aComponentType().withExpiresAfter(expiresAfterDays).build();
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).withComponentType(componentType).build();
    Location donationBatchVenue = aVenue().build();
    DonationBatch donationBatchWithComponentBatch = aDonationBatch()
        .withVenue(donationBatchVenue)
        .withComponentBatch(null)
        .build();
    Donor existingDonor = aDonor().build();
    Location donationVenue = aVenue().build();
    Donation donation = aDonation()
        .withPackType(packTypeThatCountsAsDonation)
        .withDonationBatch(donationBatchWithComponentBatch)
        .withDonor(existingDonor)
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withVenue(donationVenue)
        .build();

    Date createdOn = new Date();
    Date expiresOn = new DateTime(createdOn).plusDays(expiresAfterDays).toDate();
    
    // Set up mocks
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)).thenReturn(true);
    when(dateGeneratorService.generateDateTime(donation.getDonationDate(), donation.getBleedEndTime())).thenReturn(createdOn);

    // Run test
    Component component = componentCRUDService.createInitialComponent(donation);
    
    // Verify
    verify(componentRepository).save(component);
    assertThat(component.getComponentType(), is(componentType));
    assertThat(component.getStatus(), is(ComponentStatus.QUARANTINED));
    assertThat(component.getInventoryStatus(), is(InventoryStatus.NOT_IN_STOCK));
    assertThat(component.getCreatedOn(), is(createdOn));
    assertThat(component.getExpiresOn(), is(expiresOn));
  }

  @Test
  public void testCreateInitialComponentForDonationBatchWithComponentBatch_shouldSetComponentLocationToProcessingSite() {
    // Set up data
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();
    Location componentBatchProcessingSite = aProcessingSite().build();
    ComponentBatch componentBatch = aComponentBatch()
        .withLocation(componentBatchProcessingSite)
        .build();
    
    DonationBatch donationBatchWithComponentBatch = aDonationBatch()
        .withComponentBatch(componentBatch)
        .build();
    
    Donor existingDonor = aDonor().build();
    Location venue = aVenue().build();
    Donation donation = aDonation()
        .withPackType(packTypeThatCountsAsDonation)
        .withDonationBatch(donationBatchWithComponentBatch)
        .withDonor(existingDonor)
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withVenue(venue)
        .build();
    
    // Set up mocks
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)).thenReturn(true);
    when(donationBatchRepository.findComponentBatchByDonationbatchId(donationBatchWithComponentBatch.getId()))
        .thenReturn(componentBatch);
    when(dateGeneratorService.generateDateTime(donation.getDonationDate(), donation.getBleedEndTime())).thenReturn(new Date());

    // Run test
    Component component = componentCRUDService.createInitialComponent(donation);
    
    // Verify
    assertThat(component.getLocation(), hasSameStateAsLocation(componentBatchProcessingSite));
  }
  
  @Test
  public void testCreateInitialComponentForDonationBatchWithoutComponentBatch_shouldSetComponentLocationToDonationBatchVenue() {
    // Set up data
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();
    Location donationBatchVenue = aVenue().build();
    DonationBatch donationBatchWithComponentBatch = aDonationBatch()
        .withVenue(donationBatchVenue)
        .withComponentBatch(null)
        .build();
    Donor existingDonor = aDonor().build();
    Location donationVenue = aVenue().build();
    Donation donation = aDonation()
        .withPackType(packTypeThatCountsAsDonation)
        .withDonationBatch(donationBatchWithComponentBatch)
        .withDonor(existingDonor)
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withVenue(donationVenue)
        .build();
    
    // Set up mocks
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)).thenReturn(true);
    when(donationBatchRepository.findComponentBatchByDonationbatchId(donationBatchWithComponentBatch.getId())).thenReturn(null);
    when(dateGeneratorService.generateDateTime(donation.getDonationDate(), donation.getBleedEndTime())).thenReturn(new Date());

    // Run test
    Component component = componentCRUDService.createInitialComponent(donation);
    
    // Verify
    assertThat(component.getLocation(), hasSameStateAsLocation(donationBatchVenue));
  }

  @Test
  public void testRemoveComponentFromStock_shouldReturnComponentREMOVED() {
    UUID locationId = UUID.randomUUID();
    Location location = aLocation().withId(locationId).build();
    Donation donation = aDonation().withId(DONATION_ID_1).build();

    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withLocation(location)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .build();

    Component expectedRemovedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withLocation(location)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .build();

    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentRepository.update(argThat(
        hasSameStateAsComponent(expectedRemovedComponent)))).thenReturn(expectedRemovedComponent);

    // Exercise SUT
    Component removedComponent = componentCRUDService.removeComponentFromStock(component);

    // Verify
    assertThat(removedComponent, hasSameStateAsComponent(expectedRemovedComponent));
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedRemovedComponent)));
  }

  @Test
  public void testMarkComponentsBelongingToDonorAsUnsafe_shouldMarkComponentsAsUnsafe() {
    // Set up fixture
    Donation firstDonation = aDonation().withId(DONATION_ID_1).build();
    Donation secondDonation = aDonation().withId(DONATION_ID_2).build();
    Donation deletedDonation = aDonation().thatIsDeleted().withId(DONATION_ID_3).build();
    Donor donor = aDonor()
        .withDonation(firstDonation)
        .withDonation(secondDonation)
        .withDonation(deletedDonation)
        .build();
    
    // Set up expectations
    doNothing().when(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(any(Donation.class));

    // Exercise SUT
    componentCRUDService.markComponentsBelongingToDonorAsUnsafe(donor);
    
    // Verify
    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(argThat(hasSameStateAsDonation(firstDonation)));
    verify(componentCRUDService).markComponentsBelongingToDonationAsUnsafe(argThat(hasSameStateAsDonation(secondDonation)));
    verify(componentCRUDService, never()).markComponentsBelongingToDonationAsUnsafe(argThat(hasSameStateAsDonation(deletedDonation)));
  }

  @Test
  public void testDeleteComponent_shouldDeleteAComponent() {
    // Set up fixture
    UUID locationId = UUID.randomUUID();
    Location location = aLocation().withId(locationId).build();
    Donation donation = aDonation().withId(DONATION_ID_1).build();
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    Component expectedDeletedComponent = aComponent()
        .withLocation(location)
        .withDonation(donation)
        .withId(COMPONENT_ID_1)
        .thatIsDeleted()
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentRepository
        .update(argThat(hasSameStateAsComponent(expectedDeletedComponent))))
    .thenReturn(expectedDeletedComponent);
    
    // Exercise SUT
    Component deletedComponent = componentCRUDService.deleteComponent(COMPONENT_ID_1);
    
    // Verify
    assertThat(deletedComponent, hasSameStateAsComponent(expectedDeletedComponent));
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedDeletedComponent)));
  }

  @Test
  public void testMarkComponentsBelongingToDonationAsUnsafe_shouldMarkComponentsAsUnsafe() {
    // Set up fixture
    Component firstComponent = aComponent().withId(COMPONENT_ID_1).build();
    Component secondComponent = aComponent().withId(COMPONENT_ID_2).build();
    Component deletedComponent = aComponent().withId(COMPONENT_ID_3).withIsDeleted(true).build();

    Donation donation = aDonation()
        .withComponents(Arrays.asList(firstComponent, secondComponent, deletedComponent))
        .build();
    
    // Set up expectations
    doAnswer(returnsFirstArg()).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(firstComponent)), eq(ComponentStatusChangeReasonType.TEST_RESULTS));

    // Exercise SUT
    componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);
    
    // Verify
    verify(componentCRUDService).markComponentAsUnsafe(argThat(hasSameStateAsComponent(firstComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS));
    verify(componentCRUDService).markComponentAsUnsafe(argThat(hasSameStateAsComponent(secondComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS));
    verify(componentCRUDService, never()).markComponentAsUnsafe(argThat(hasSameStateAsComponent(deletedComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS));
  }

  @Test
  public void testDiscardComponent() throws Exception {
    // set up data
    Component component = aComponent().withId(COMPONENT_ID_1).build();
    UUID discardReasonId = UUID.randomUUID();
    String reasonText = "junit";
    
    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentRepository.update(component)).thenReturn(component);
    
    // run test
    componentCRUDService.discardComponent(COMPONENT_ID_1, discardReasonId, reasonText);
    
    // check asserts
    Assert.assertEquals("Component status is discarded", ComponentStatus.DISCARDED, component.getStatus());
    Assert.assertNotNull("Status change has been set", component.getStatusChanges());
    Assert.assertEquals("Status change has been set", 1, component.getStatusChanges().size());
    ComponentStatusChange statusChange = component.getStatusChanges().iterator().next();
    Assert.assertEquals("Status change is correct", ComponentStatus.DISCARDED, statusChange.getNewStatus());
    Assert.assertEquals("Status change is correct", discardReasonId, statusChange.getStatusChangeReason().getId());
    Assert.assertEquals("Status change is correct", reasonText, statusChange.getStatusChangeReasonText());
    Assert.assertEquals("Status change is correct", loggedInUser, statusChange.getChangedBy());
  }
  
  @Test
  public void testDiscardComponents() throws Exception {
    // set up data
    Component component1 = aComponent().withStatus(ComponentStatus.DISCARDED).withId(COMPONENT_ID_1).build();
    Component component2 = aComponent().withStatus(ComponentStatus.DISCARDED).withId(COMPONENT_ID_2).build();
    Component component3 = aComponent().withStatus(ComponentStatus.DISCARDED).withId(COMPONENT_ID_3).build();
    List<UUID> componentIds = Arrays.asList(COMPONENT_ID_1,COMPONENT_ID_2,COMPONENT_ID_3);
    UUID discardReasonId = UUID.randomUUID();
    String reasonText = "junit";

    // set up mocks (because we already tested discardComponent we use a "spy" mockup of
    // discardComponent to test discardComponents)
    doReturn(component1).when(componentCRUDService).discardComponent(COMPONENT_ID_1, discardReasonId, reasonText);
    doReturn(component2).when(componentCRUDService).discardComponent(COMPONENT_ID_2, discardReasonId, reasonText);
    doReturn(component3).when(componentCRUDService).discardComponent(COMPONENT_ID_3, discardReasonId, reasonText);

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
    UUID discardReasonId = UUID.randomUUID();
    Component component = aComponent().withId(COMPONENT_ID_1).withInventoryStatus(InventoryStatus.IN_STOCK).build();
    
    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentRepository.update(component)).thenReturn(component);
    
    // run test
    componentCRUDService.discardComponent(COMPONENT_ID_1, discardReasonId, "junit");
    
    // check asserts
    Assert.assertEquals("Component is now removed from stock", InventoryStatus.REMOVED, component.getInventoryStatus());
  }
  
  @Test
  public void testProcessComponent_shouldCreateChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType componentType2 = aComponentType().withId(componentTypeId2)
        .withComponentTypeCode("0002")
        .withComponentTypeName("#2")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.HOURS)
        .build();
    UUID componentTypeId3 = UUID.randomUUID();
    ComponentType componentType3 = aComponentType().withId(componentTypeId3)
        .withComponentTypeCode("0003")
        .withComponentTypeName("#3")
        .withExpiresAfter(1)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.YEARS)
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    ComponentBatch componentBatch = ComponentBatchBuilder.aComponentBatch().withLocation(location).build();
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(location)
        .withComponentBatch(componentBatch)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1, componentType1, componentType2, componentType3))
        .build();
    Date processedOn = new Date();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentBatch(componentBatch)
        .withProcessedOn(processedOn)
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
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentBatch(componentBatch)
        .build();
    Component expectedComponent2 = aComponent()
        .withComponentType(componentType1)
        .withComponentCode("0001-02")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentBatch(componentBatch)
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
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal2.getTime())
        .withLocation(location)
        .withComponentBatch(componentBatch)
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
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal3.getTime())
        .withLocation(location)
        .withComponentBatch(componentBatch)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(componentType2);
    when(componentTypeRepository.getComponentTypeById(componentTypeId3)).thenReturn(componentType3);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), processedOn);
    
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
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    Date processedOn = new Date();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withProcessedOn(processedOn)
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
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), processedOn);
    
    // verify results
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedParentComponent)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent1)));
  }
  
  @Test
  public void testProcessUnsafeComponent_shouldCreateUnsafeChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentStatusChangeId = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .withMaxBleedTime(16)
        .withMaxTimeSinceDonation(24)
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .build();
    ComponentStatusChangeReason statusChangeReason = anUnsafeReason()
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS).build();
    ComponentStatusChange statusChange = aComponentStatusChange()
        .withId(componentStatusChangeId)
        .withStatusChangedOn(new Date())
        .withStatusChangeReason(statusChangeReason).build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.UNSAFE)
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    Date processedOn = new Date();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .withProcessedOn(processedOn)
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
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .build();
    Component unsafeComponent = aComponent()
        .withComponentType(componentType1)
        .withComponentCode("0001")
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.UNSAFE)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentStatusChange(aComponentStatusChange()
            .withId(componentStatusChangeId)
            .withStatusChangeReason(aComponentStatusChangeReason()
                .withId(UUID.randomUUID()).build())
            .build())
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    doReturn(unsafeComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedComponent1)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), processedOn);
    
    // verify results
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedParentComponent)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponent1)));
    verify(componentCRUDService).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedComponent1)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));
  }

  /**
   * Test process unsafe component with unsafe status change TRCP should create unsafe child
   * components where applicable, which means unsafe only if they contain plasma.
   * 
   * TRCP: TEST_RESULTS_CONTAINS_PLASMA.
   */
  @Test
  public void testProcessUnsafeComponentWithUnsafeStatusChangeTRCP_shouldCreateUnsafeChildComponentsWhereApplicable() {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Date donationDate = new Date(); 
    UUID componentTypeCombinationId = UUID.randomUUID();
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .build();
    ComponentStatusChangeReason statusChangeReason = anUnsafeReason()
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA).build();
    ComponentStatusChange statusChange = aComponentStatusChange()
        .withId(UUID.randomUUID())
        .withStatusChangedOn(new Date())
        .withStatusChangeReason(statusChangeReason)
        .build();

    UUID componentTypeId1 = UUID.randomUUID();
    ComponentType componentTypeThatContainsPlasma = aComponentType()
        .withId(componentTypeId1)
        .thatContainsPlasma()
        .withExpiresAfter(90)
        .withComponentTypeCode("0001")
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .withMaxBleedTime(20)
        .withMaxTimeSinceDonation(25)
        .build();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType componentTypeThatDoesntContainsPlasma = aComponentType().withId(componentTypeId2)
        .withExpiresAfter(90)
        .withComponentTypeCode("0002")
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .withMaxBleedTime(20)
        .withMaxTimeSinceDonation(25)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentTypeThatContainsPlasma, componentTypeThatDoesntContainsPlasma))
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Date processedOn = new Date();
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.UNSAFE)
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .withComponentType(componentTypeThatContainsPlasma)
        .withComponentCode("0001")
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .withComponentType(componentTypeThatContainsPlasma)
        .withComponentCode("0001")
        .withProcessedOn(processedOn)
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedComponentThatDoesntContainsPlasma = aComponent()
        .withComponentType(componentTypeThatDoesntContainsPlasma)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("0002")
        .build();
    Component expectedComponentThatContainsPlasma = aComponent()
        .withComponentType(componentTypeThatContainsPlasma)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("0001")
        .build();
    Component mockedComponent = aComponent().build();

    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentTypeThatContainsPlasma);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2))
        .thenReturn(componentTypeThatDoesntContainsPlasma);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    doReturn(mockedComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedComponentThatContainsPlasma)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));
    
    // SUT
    parentComponent = componentCRUDService.processComponent(COMPONENT_ID_1, componentTypeCombination.getId(), new Date());

    // verify that both components are created
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponentThatContainsPlasma)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponentThatDoesntContainsPlasma)));
    // verify that only the component that contains plasma is marked as unsafe
    verify(componentCRUDService, times(1)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedComponentThatContainsPlasma)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));
    verify(componentCRUDService, times(0)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedComponentThatDoesntContainsPlasma)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));

  }

  /**
   * Test process unsafe component with unsafe status change LW should create unsafe child
   * components where applicable, which means unsafe only if they contain plasma.
   *
   * LW: LOW_WEIGHT.
   */
  @Test
  public void testProcessUnsafeComponentWithUnsafeStatusChangeLW_shouldCreateUnsafeChildComponentsWhereApplicable() {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Date donationDate = new Date();
    Date processedOn = new Date();
    UUID componentTypeCombinationId = UUID.randomUUID();
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    ComponentStatusChangeReason statusChangeReason = anUnsafeReason()
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.LOW_WEIGHT).build();
    ComponentStatusChange statusChange = aComponentStatusChange()
        .withId(UUID.randomUUID())
        .withStatusChangedOn(new Date())
        .withStatusChangeReason(statusChangeReason)
        .build();
    UUID componentTypeId1 = UUID.randomUUID();
    ComponentType componentTypeThatContainsPlasma = aComponentType()
        .withId(componentTypeId1)
        .thatContainsPlasma()
        .withExpiresAfter(90)
        .withComponentTypeCode("0001")
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType componentTypeThatDoesntContainsPlasma = aComponentType().withId(componentTypeId2)
        .withExpiresAfter(90)
        .withComponentTypeCode("0002")
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentTypeThatContainsPlasma, componentTypeThatDoesntContainsPlasma))
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.UNSAFE)
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .withComponentType(componentTypeThatContainsPlasma)
        .withComponentCode("0001")
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .withComponentType(componentTypeThatContainsPlasma)
        .withComponentCode("0001")
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedComponentThatDoesntContainsPlasma = aComponent()
        .withComponentType(componentTypeThatDoesntContainsPlasma)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("0002")
        .build();
    Component expectedComponentThatContainsPlasma = aComponent()
        .withComponentType(componentTypeThatContainsPlasma)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("0001")
        .build();
    Component mockedComponent = aComponent().build();

    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentTypeThatContainsPlasma);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(componentTypeThatDoesntContainsPlasma);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    doReturn(mockedComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedComponentThatContainsPlasma)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));

    // SUT
    parentComponent = componentCRUDService.processComponent(COMPONENT_ID_1, componentTypeCombination.getId(), processedOn);

    // verify that both components are created
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponentThatContainsPlasma)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedComponentThatDoesntContainsPlasma)));
    // verify that only the component that contains plasma is marked as unsafe
    verify(componentCRUDService, times(1)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedComponentThatContainsPlasma)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));
    verify(componentCRUDService, times(0)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedComponentThatDoesntContainsPlasma)), eq(ComponentStatusChangeReasonType.UNSAFE_PARENT));

  }
  
  /**
   * Test chain process unsafe component into component that doesn't contain plasma, should not mark
   * component as unsafe.
   * 
   * The initial component is the one with the status change with type TEST_RESULTS_CONTAINS_PLASMA,
   * and this will be checked instead of the parent, so the component produced shouldn't be marked
   * as UNSAFE.
   */
  @Test
  public void testChainProcessUnsafeComponentIntoComponentThatDoesntContainPlasma_shouldNotMarkComponentAsUnsafe() {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Date donationDate = new Date(); 
    UUID componentStatusChangeId1 = UUID.randomUUID();
    UUID componentStatusChangeId2 = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    ComponentStatusChangeReason statusChangeReasonUP = anUnsafeReason()
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.UNSAFE_PARENT).build();
    ComponentStatusChangeReason statusChangeReasonTRCP = anUnsafeReason()
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA).build();
    ComponentStatusChange statusChangeUP = aComponentStatusChange()
        .withId(componentStatusChangeId1)
        .withStatusChangedOn(new Date())
        .withStatusChangeReason(statusChangeReasonUP)
        .build();
    ComponentStatusChange statusChangeTRCP = aComponentStatusChange()
        .withId(componentStatusChangeId2)
        .withStatusChangedOn(new Date())
        .withStatusChangeReason(statusChangeReasonTRCP)
        .build();
    UUID componentTypeId1 = UUID.randomUUID();
    ComponentType componentType = aComponentType()
        .withId(componentTypeId1)
        .withExpiresAfter(90)
        .withComponentTypeCode("0001")
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType componentTypeThatDoesntContainsPlasma = aComponentType()
        .withId(componentTypeId2)
        .withExpiresAfter(90)
        .withComponentTypeCode("0002")
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentTypeThatDoesntContainsPlasma))
        .build();
    Component initialComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.UNSAFE)
        .withLocation(location)
        .withComponentStatusChange(statusChangeTRCP)
        .withComponentType(componentType)
        .withComponentCode("0001")
        .build();
    Component parentComponent = aComponent()
        .withId(COMPONENT_ID_2)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.UNSAFE)
        .withLocation(location)
        .withComponentStatusChange(statusChangeUP)
        .withComponentType(componentType)
        .withComponentCode("0001")
        .withParentComponent(initialComponent)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_2)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(componentTypeThatDoesntContainsPlasma);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    
    // SUT
    parentComponent = componentCRUDService.processComponent(parentComponent.getId(),
        componentTypeCombination.getId(), new Date());

    // verify that the component is not marked as unsafe, as the initial component change status is checked and is TRCP
    verify(componentCRUDService, times(0)).markComponentAsUnsafe(any(Component.class), any(ComponentStatusChangeReasonType.class));
  }
  
  @Test
  public void testProcessProcessedComponent_shouldNotCreateChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.PROCESSED)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), new Date());
    
    // verify results
    verify(componentRepository, times(0)).save(Mockito.any(Component.class));
  }
  
  @Test
  public void testProcessDiscardedComponent_shouldNotCreateChildComponents() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withStatus(ComponentStatus.DISCARDED)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), new Date());
    
    // verify results
    verify(componentRepository, times(0)).save(Mockito.any(Component.class));
  }
  
  @Test
  public void testProcessComponent_shouldSetProcessedOnOnParent() {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    UUID componentTypeId1 = UUID.randomUUID();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentType componentType1 = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .build();
    Date donationDate = new Date(); 
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(location)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType1))
        .build();
    Date processedOn = new Date();
    Component expectedParentComponent = aComponent().withId(parentComponentId).withDonation(donation)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.PROCESSED)
        .withLocation(location)
        .withProcessedOn(processedOn)
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType1);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), processedOn);
    
    // verify results
    verify(componentRepository).update(argThat(ComponentMatcher.hasSameStateAsComponent(expectedParentComponent)));
  }

  @Test(expected = IllegalStateException.class)
  public void testProcessComponentThatCannotBeProcessed_shouldThrow() {
    // set up data
    UUID parentComponentId = UUID.randomUUID();
    Component parentComponent = aComponent().withId(parentComponentId).build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(UUID.randomUUID())
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(aComponentType().build()))
        .build();
    
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(false);
    
    // SUT
    componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), new Date());
  }
  
  @Test
  public void testFindComponentById_shouldCallRepository() throws Exception {
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(aComponent().build());
    
    // SUT
    componentCRUDService.findComponentById(COMPONENT_ID_1);
    
    // check
    verify(componentRepository).findComponentById(COMPONENT_ID_1);
  }
  
  @Test
  public void testFindComponentsByDINAndType_shouldCallRepository() throws Exception {
    // set up data
    String din = String.valueOf("1234567");
    UUID componentTypeId = UUID.randomUUID();
    
    // mocks
    when(componentRepository.findComponentsByDINAndType(din, componentTypeId)).thenReturn(Arrays.asList(aComponent().build()));
    
    // SUT
    componentCRUDService.findComponentsByDINAndType(din, componentTypeId);
    
    // check
    verify(componentRepository).findComponentsByDINAndType(din, componentTypeId);
  }
  
  @Test
  public void testPreProcessComponent_shouldReturnExistingComponent() throws ParseException {
    // set up data
    Integer componentWeight = Integer.valueOf(420);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date donationDate = sdf.parse("2016-01-01 09:00");
    Date bleedStartTime = sdf.parse("2016-01-01 13:00");
    Date bleedEndTime = sdf.parse("2016-01-01 13:16");
    Donation donation = aDonation().build();
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withStatus(ComponentStatus.PROCESSED)
        .withWeight(componentWeight)
        .withCreatedOn(donationDate)
        .build();

    Date expectedCreatedOn = sdf.parse("2016-01-01 13:00");
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(donationRepository.update(donation)).thenReturn(donation);
    when(dateGeneratorService.generateDateTime(donationDate, bleedStartTime)).thenReturn(expectedCreatedOn);
    // SUT
    Component returnedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, componentWeight, bleedStartTime, bleedEndTime);
    // verify
    verify(donationRepository, times(1)).update(any(Donation.class));
    verify(dateGeneratorService).generateDateTime(donationDate, bleedStartTime);
    assertThat(donation.getBleedStartTime(), is(bleedStartTime));
    assertThat(donation.getBleedEndTime(), is(bleedEndTime));
    assertThat(returnedComponent, hasSameStateAsComponent(oldComponent));
  }
  
  @Test(expected = java.lang.IllegalStateException.class)
  public void testPreProcessComponentWithIncorrectWeight_shouldThrowException() throws Exception {
    // set up data
    Donation donation = aDonation().build();
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.PROCESSED)
        .withDonation(donation)
        .build();    
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(donationRepository.update(donation)).thenReturn(donation);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(false);
    
    // SUT
    componentCRUDService.preProcessComponent(COMPONENT_ID_1, 320, null, null);
  }

  @Test
  public void testPreProcessComponentWithWeightBetweenLowVolumeAndMinWeight_shouldDiscardComponent() throws Exception {
    // set up data
    Donation donation = aDonation().withPackType(aPackType().withLowVolumeWeight(316).withMinWeight(400).withMaxWeight(500).build()).build();
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withDonation(donation)
        .build();
    Component unsafeComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentStatusChange(aComponentStatusChange()
            .withId(UUID.randomUUID())
            .withStatusChangeReason(aComponentStatusChangeReason()
                .withId(UUID.randomUUID()).build())
            .build())
        .withDonation(donation)
        .build();

    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(oldComponent)).thenReturn(true);
    doReturn(unsafeComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(oldComponent)), eq(ComponentStatusChangeReasonType.LOW_WEIGHT));
    when(componentStatusCalculator.updateComponentStatus(unsafeComponent)).thenReturn(false);
    when(componentRepository.update(unsafeComponent)).thenReturn(unsafeComponent);

    // SUT
    Component updatedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 320, null, null);

    // check
    verify(componentCRUDService).markComponentAsUnsafe(oldComponent, ComponentStatusChangeReasonType.LOW_WEIGHT);
    assertThat("Component was flagged for discard", updatedComponent.getStatus(), is(ComponentStatus.UNSAFE));
  }

  @Test
  public void testPreProcessComponentWithInvalidLowWeight_shouldDiscardComponent() throws Exception {
    // set up data
    Donation donation = aDonation().withPackType(aPackType().withLowVolumeWeight(350).withMinWeight(400).withMaxWeight(500).build()).build();
    UUID componentStatusChangeId = UUID.randomUUID();
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withDonation(donation)
        .build();
    Component unsafeComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentStatusChange(aComponentStatusChange()
            .withId(componentStatusChangeId)
            .withStatusChangeReason(aComponentStatusChangeReason()
                .withId(UUID.randomUUID()).build())
            .build())
        .withDonation(donation)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(oldComponent)).thenReturn(true);
    doReturn(unsafeComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(oldComponent)), eq(ComponentStatusChangeReasonType.INVALID_WEIGHT));
    when(componentStatusCalculator.updateComponentStatus(unsafeComponent)).thenReturn(false);
    when(componentRepository.update(unsafeComponent)).thenReturn(unsafeComponent);
    
    // SUT
    Component updatedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 320, null, null);
    
    // check
    verify(componentCRUDService).markComponentAsUnsafe(oldComponent, ComponentStatusChangeReasonType.INVALID_WEIGHT);
    assertThat("Component was flagged for discard", updatedComponent.getStatus(), is(ComponentStatus.UNSAFE));
  }

  @Test
  public void testPreProcessComponentWithInvalidHighWeight_shouldDiscardComponent() throws Exception {
    // set up data
    Donation donation = aDonation().withPackType(aPackType().withLowVolumeWeight(350).withMinWeight(400).withMaxWeight(500).build()).build();
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withDonation(donation)
        .build();
    Component unsafeComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentStatusChange(aComponentStatusChange()
            .withId(UUID.randomUUID())
            .withStatusChangeReason(aComponentStatusChangeReason()
                .withId(UUID.randomUUID()).build())
            .build())
        .withDonation(donation)
        .build();

    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(oldComponent)).thenReturn(true);
    doReturn(unsafeComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(oldComponent)), eq(ComponentStatusChangeReasonType.INVALID_WEIGHT));
    when(componentStatusCalculator.updateComponentStatus(unsafeComponent)).thenReturn(false);
    when(componentRepository.update(unsafeComponent)).thenReturn(unsafeComponent);

    // SUT
    Component updatedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 700, null, null);

    // check
    verify(componentCRUDService).markComponentAsUnsafe(oldComponent, ComponentStatusChangeReasonType.INVALID_WEIGHT);
    assertThat("Component was flagged for discard", updatedComponent.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  @Test
  public void testPreProcessComponentWithValidWeight_shouldNotDiscardComponent() throws Exception {
    // set up data
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.updateComponentStatus(oldComponent)).thenReturn(false);
    when(componentRepository.update(oldComponent)).thenReturn(oldComponent);
    
    // SUT
    Component updatedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 420, null, null);
    
    // check
    assertThat("Component was not flagged for discard", updatedComponent.getStatus(), is(ComponentStatus.QUARANTINED));
  }

  @Test
  public void testPreProcessUnsafeComponentWithValidWeight_shouldNotDiscardComponentShouldRollBack() throws Exception {
    // set up data
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.UNSAFE)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(aComponentStatusChangeReason()
                .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.UNSAFE)
                .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT)
                .build())
            .build())
        .build();

    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.updateComponentStatus(oldComponent)).thenReturn(false);
    when(componentRepository.update(oldComponent)).thenReturn(oldComponent);

    // SUT
    Component updatedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 420, null, null);

    // check
    assertThat("Component is not unsafe", updatedComponent.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUpdateComponentWeight_shouldReEvaluateUnsafeStatus() throws Exception {
    // set up data
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date bleedStartTime = sdf.parse("2016-01-01 13:00");
    Date bleedEndTime = sdf.parse("2016-01-01 13:16");
    Component oldComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.UNSAFE)
        .withWeight(111)
        .build();
    Component reEvaluatedcomponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withWeight(420)
        .withDonation(aDonation().withPackType(aPackType().withMinWeight(400).withMaxWeight(500).build()).build())
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(oldComponent);
    when(componentConstraintChecker.canPreProcess(oldComponent)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(oldComponent)).thenReturn(false);
    when(componentStatusCalculator.updateComponentStatus(oldComponent)).thenReturn(true);
    when(componentRepository.update(reEvaluatedcomponent)).thenReturn(reEvaluatedcomponent);
    
    // SUT
    Component updatedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 420, bleedStartTime, bleedEndTime);
    
    // check
    assertThat("Component status was re-evaluated", updatedComponent.getStatus(), is(ComponentStatus.QUARANTINED));
  }
  
  @Test
  public void testUnprocessComponent_shouldDeleteAllChildrenAndSetStatusToAvailable() throws Exception {
    // set up data
    Donation donation = aDonation().build();
    Location location = aLocation().build();
    Component parentComponent = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.PROCESSED).withDonation(donation).withLocation(location).build();
    Component child1 = aComponent().withId(COMPONENT_ID_2).withDonation(donation).withLocation(location).build();
    Component child2 = aComponent().withId(COMPONENT_ID_3).withDonation(donation).withLocation(location).build();

    Component componentToUpdate = aComponent().withId(COMPONENT_ID_1).withDonation(donation).withLocation(location).withStatus(ComponentStatus.QUARANTINED).build();

    Component updatedComponent = aComponent().withId(COMPONENT_ID_1).withDonation(donation).withLocation(location).withStatus(ComponentStatus.AVAILABLE).build();
    Component child1Deleted = aComponent().withId(COMPONENT_ID_2).withDonation(donation).withLocation(location).withIsDeleted(true).build();
    Component child2Deleted = aComponent().withId(COMPONENT_ID_3).withDonation(donation).withLocation(location).withIsDeleted(true).build();

    // mocks
    when(componentConstraintChecker.canUnprocess(parentComponent)).thenReturn(true);
    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child1, child2));
    when(componentRepository.update(argThat(ComponentMatcher.hasSameStateAsComponent(componentToUpdate)))).thenReturn(updatedComponent);
    
    // SUT
    Component unprocessedComponent = componentCRUDService.unprocessComponent(parentComponent);

    // check
    verify(componentRepository, times(1)).update(argThat(ComponentMatcher.hasSameStateAsComponent(componentToUpdate)));
    verify(componentRepository, times(1)).update(argThat(ComponentMatcher.hasSameStateAsComponent(child1Deleted)));
    verify(componentRepository, times(1)).update(argThat(ComponentMatcher.hasSameStateAsComponent(child2Deleted)));

    assertThat("Parent component status is AVAILABLE", unprocessedComponent.getStatus(), is(ComponentStatus.AVAILABLE));
    assertThat("Parent inventory status is NOT_IN_STOCK", unprocessedComponent.getInventoryStatus(), is(InventoryStatus.NOT_IN_STOCK));
  }
  
  @Test
  public void testUnprocessComponentThatWasRemovedFromStock_shouldPutBackInStock() throws Exception {
    // set up data
    Donation donation = aDonation().build();
    Location location = aLocation().build();
    Component parentComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(donation)
        .withLocation(location)
        .build();
    Component child1 = aComponent().withId(COMPONENT_ID_2).withDonation(donation).withLocation(location).build();
    Component child2 = aComponent().withId(COMPONENT_ID_3).withDonation(donation).withLocation(location).build();

    Component componentToUpdate = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withLocation(location)
        .build();

    Component updatedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(donation)
        .withLocation(location)
        .build();

    Component child1Deleted = aComponent().withId(COMPONENT_ID_2).withDonation(donation).withLocation(location).withIsDeleted(true).build();
    Component child2Deleted = aComponent().withId(COMPONENT_ID_3).withDonation(donation).withLocation(location).withIsDeleted(true).build();

    // mocks
    when(componentConstraintChecker.canUnprocess(parentComponent)).thenReturn(true);
    when(componentRepository.findChildComponents(parentComponent)).thenReturn(Arrays.asList(child1, child2));
    when(componentRepository.update(argThat(ComponentMatcher.hasSameStateAsComponent(componentToUpdate)))).thenReturn(updatedComponent);
    
    // SUT
    Component unprocessedComponent = componentCRUDService.unprocessComponent(parentComponent);

    // check
    verify(componentRepository, times(1)).update(argThat(ComponentMatcher.hasSameStateAsComponent(componentToUpdate)));
    verify(componentRepository, times(1)).update(argThat(ComponentMatcher.hasSameStateAsComponent(child1Deleted)));
    verify(componentRepository, times(1)).update(argThat(ComponentMatcher.hasSameStateAsComponent(child2Deleted)));

    assertThat("Parent component status is AVAILABLE", unprocessedComponent.getStatus(), is(ComponentStatus.AVAILABLE));
    assertThat("Parent inventory status is IN_STOCK", unprocessedComponent.getInventoryStatus(), is(InventoryStatus.IN_STOCK));
  }
  
  @Test
  public void testUnprocessComponent_shouldResetProcessedOnDate() throws Exception {
    // set up data
    Donation donation = aDonation().build();
    Location location = aLocation().build();
    Component parentComponent = aComponent().withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.PROCESSED)
        .withDonation(donation)
        .withLocation(location)
        .withProcessedOn(new Date())
        .build();
    Component componentToUpdate = aComponent().withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withLocation(location)
        .withStatus(ComponentStatus.QUARANTINED)
        .withProcessedOn(null)
        .build();

    Component updatedComponent = aComponent().withId(COMPONENT_ID_1)
        .withDonation(donation)
        .withLocation(location)
        .withStatus(ComponentStatus.QUARANTINED)
        .build();

    // mocks
    when(componentConstraintChecker.canUnprocess(parentComponent)).thenReturn(true);
    when(componentRepository.findChildComponents(parentComponent)).thenReturn(new ArrayList<Component>());
    when(componentRepository.update(argThat(ComponentMatcher.hasSameStateAsComponent(componentToUpdate)))).thenReturn(updatedComponent);
    
    // SUT
    Component unprocessedComponent = componentCRUDService.unprocessComponent(parentComponent);

    // Check
    assertThat("Parent component processedOn is null", unprocessedComponent.getProcessedOn(), is(nullValue()));
  }
  
  @Test
  public void testUndiscardComponentThatWasInStock_shouldUndiscardAndReturnComponent() {
    // Set up fixture
    Component existingComponentThatWasInStock = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .build();
    
    // Set up expectations
    Component updatedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .build();
    
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(existingComponentThatWasInStock);
    when(componentConstraintChecker.canUndiscard(existingComponentThatWasInStock)).thenReturn(true);
    when(componentRepository.update(updatedComponent)).thenReturn(updatedComponent);
    
    // Exercise SUT
    Component returnedComponent = componentCRUDService.undiscardComponent(COMPONENT_ID_1);
    
    // Verify
    
    // Ensure that the status was recalculate
    verify(componentStatusCalculator).updateComponentStatus(updatedComponent);
    
    assertThat(returnedComponent, is(updatedComponent));
  }
  
  @Test
  public void testUndiscardComponentThatWasInNotStock_shouldUndiscardAndReturnComponent() {
    // Set up fixture
    Component existingComponentThatWasNotInStock = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    
    // Set up expectations
    Component updatedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .build();
    
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(existingComponentThatWasNotInStock);
    when(componentConstraintChecker.canUndiscard(existingComponentThatWasNotInStock)).thenReturn(true);
    when(componentRepository.update(updatedComponent)).thenReturn(updatedComponent);
    
    // Exercise SUT
    Component returnedComponent = componentCRUDService.undiscardComponent(COMPONENT_ID_1);
    
    // Verify
    
    // Ensure that the status was recalculate
    verify(componentStatusCalculator).updateComponentStatus(updatedComponent);
    
    assertThat(returnedComponent, is(updatedComponent));
  }
  
  @Test
  public void testUndiscardComponentWithStatusChange_shouldVoidComponentStatusChange() {
    // Set up fixture
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangedOn(new Date())
            .withStatusChangeReason(aDiscardReason().build())
            .build())
        .build();
    
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentConstraintChecker.canUndiscard(component)).thenReturn(true);
    when(componentRepository.update(component)).thenReturn(component);
    
    // Exercise SUT
    Component returnedComponent = componentCRUDService.undiscardComponent(COMPONENT_ID_1);
    
    // Verify
    
    assertThat(returnedComponent.getStatusChanges().iterator().next().getIsDeleted(), is(true));
  }
  
  @Test
  public void testUndiscardComponentWithManyStatusChanges_shouldVoidDiscardedComponentStatusChange() {
    // Set up fixture
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.DISCARDED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(aDiscardReason().build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(aReturnReason().build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason().build())
            .build())
        .build();
    
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentConstraintChecker.canUndiscard(component)).thenReturn(true);
    when(componentRepository.update(component)).thenReturn(component);
    
    // Exercise SUT
    Component returnedComponent = componentCRUDService.undiscardComponent(COMPONENT_ID_1);
    
    // Verify
    Iterator<ComponentStatusChange> it = returnedComponent.getStatusChanges().iterator();
    assertThat(it.next().getIsDeleted(), is(false));
    assertThat(it.next().getIsDeleted(), is(false));
    ComponentStatusChange discarded = it.next();
    assertThat(discarded.getIsDeleted(), is(true));
    assertThat(discarded.getStatusChangeReason().getCategory(), is(ComponentStatusChangeReasonCategory.DISCARDED));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUndiscardComponentWithComponentThatCannotBeUndiscarded_shouldThrow() {
    // Set up fixture
    Component existingComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.AVAILABLE)
        .build();
    
    // Set up expectations
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(existingComponent);
    when(componentConstraintChecker.canUndiscard(existingComponent)).thenReturn(false);
    
    // Exercise SUT
    componentCRUDService.undiscardComponent(COMPONENT_ID_1);
  }

  @Test
  public void testMarkComponentsAsUnsafe_shouldCreateStatusChange() {
    Date date = new Date();
    Donation donation = aDonation().build();
    Location location = aLocation().build();
    Component component = aComponent().withDonation(donation).withLocation(location).build();
    ComponentStatusChangeReason statusChangeReason = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.UNSAFE)
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.UNSAFE_PARENT)
        .build();
    ComponentStatusChange statusChange =
        aComponentStatusChange().withStatusChangeReason(statusChangeReason).withStatusChangedOn(date).build();
    Component updatedComponent = aComponent().withDonation(donation).withLocation(location)
        .withComponentStatusChange(statusChange)
        .withStatus(ComponentStatus.UNSAFE).build();
    
    // Set up expectations
    when(dateGeneratorService.generateDate()).thenReturn(date);

    // Run test
    componentCRUDService.markComponentAsUnsafe(component, ComponentStatusChangeReasonType.UNSAFE_PARENT);
    
    // Verify
    verify(componentRepository).update(argThat(hasSameStateAsComponent(updatedComponent)));
  }
  
  @Test
  public void testMarkDiscardedComponentAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus() {
    testMarkComponentsAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus(ComponentStatus.DISCARDED);
  }
  
  @Test
  public void testMarkIssuedComponentAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus() {
    testMarkComponentsAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus(ComponentStatus.ISSUED);
  }
  
  @Test
  public void testMarkUsedComponentAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus() {
    testMarkComponentsAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus(ComponentStatus.TRANSFUSED);
  }
  
  @Test
  public void testMarkProcessedComponentAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus() {
    testMarkComponentsAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus(ComponentStatus.PROCESSED);
  }
  
  @Test
  public void testMarkUnsafeComponentAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus() {
    testMarkComponentsAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus(ComponentStatus.UNSAFE);
  }

  private void testMarkComponentsAsUnsafe_shouldCreateStatusChangeAndNotUpdateStatus(ComponentStatus status) {
    Date date = new Date();
    Donation donation = aDonation().build();
    Location location = aLocation().build();
    Component component = aComponent()
        .withStatus(status)
        .withDonation(donation)
        .withLocation(location)
        .build();
    ComponentStatusChangeReason statusChangeReason = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.UNSAFE)
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.UNSAFE_PARENT)
        .build();
    ComponentStatusChange statusChange = aComponentStatusChange()
        .withStatusChangeReason(statusChangeReason)
        .withStatusChangedOn(date)
        .build();
    Component updatedComponent = aComponent()
        .withStatus(status)
        .withDonation(donation)
        .withLocation(location)
        .withComponentStatusChange(statusChange)
        .build();
    
    // Set up expectations
    when(dateGeneratorService.generateDate()).thenReturn(date);

    // Run test
    componentCRUDService.markComponentAsUnsafe(component, ComponentStatusChangeReasonType.UNSAFE_PARENT);
    
    // Verify
    verify(componentRepository).update(argThat(hasSameStateAsComponent(updatedComponent)));
  }
  
  @SuppressWarnings("unchecked")
  @Test(expected = IllegalArgumentException.class)
  public void testMarkComponentsAsUnsafeWithNonExistingReason_shouldThrow() {
    Date date = new Date();
    Donation donation = aDonation().build();
    Location location = aLocation().build();
    ComponentStatusChangeReason statusChangeReason = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.UNSAFE)
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.UNSAFE_PARENT)
        .build();
    ComponentStatusChange statusChange =
        aComponentStatusChange().withStatusChangeReason(statusChangeReason).withStatusChangedOn(date).build();
    Component component = aComponent().withDonation(donation).withLocation(location)
        .withComponentStatusChange(statusChange)
        .withStatus(ComponentStatus.UNSAFE).build();       
    
    // Set up expectations
    when(dateGeneratorService.generateDate()).thenReturn(date);
    when(componentStatusChangeReasonRepository.findComponentStatusChangeReasonByCategoryAndType(
        ComponentStatusChangeReasonCategory.UNSAFE, ComponentStatusChangeReasonType.UNSAFE_PARENT))
            .thenThrow(NoResultException.class);

    // Run test
    componentCRUDService.markComponentAsUnsafe(component, ComponentStatusChangeReasonType.UNSAFE_PARENT);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testTransferComponentWithNonDistributionSite_shouldThrow() {
    // Set up fixture
    Component componentToTransfer = aComponent().withLocation(aDistributionSite().build()).build();
    Location transferTo = aUsageSite().build();

    // Call test method
    componentCRUDService.transferComponent(componentToTransfer, transferTo);
  }
  
  @Test
  public void testTransferComponent_shouldUpdateComponentLocation() {
    // Set up fixture
    Component componentToTransfer = aComponent().withLocation(aDistributionSite().build()).build();
    Location transferTo = aDistributionSite().build();
    
    // Set up expectations
    when(componentRepository.update(componentToTransfer)).thenReturn(componentToTransfer);
    
    // Call test method
    Component returnedComponent = componentCRUDService.transferComponent(componentToTransfer, transferTo);
    
    // Verify
    assertThat(returnedComponent, is(componentToTransfer));
    assertThat(returnedComponent.getLocation(), is(transferTo));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIssueComponentWithNonUsageSite_shouldThrow() {
    // Set up fixture
    Component componentToIssue = aComponent().withLocation(aDistributionSite().build()).build();
    Location issueTo = aDistributionSite().build();
    
    // Call test method
    componentCRUDService.issueComponent(componentToIssue, issueTo);
  }
  
  @Test
  public void testIssueComponent_shouldUpdateComponentFields() {
    // Set up fixture
    Component componentToIssue = aComponent().withId(COMPONENT_ID_1).withLocation(aDistributionSite().build()).build();
    Location issueTo = aUsageSite().build();
    Date issuedDate = new Date();
    ComponentStatusChangeReason statusChangeReason = anIssuedReason().build();
    ComponentStatusChange expectedStatusChange = aComponentStatusChange()
        .withStatusChangedOn(new Date()).withNewStatus(ComponentStatus.ISSUED)
        .withStatusChangeReason(statusChangeReason).build();
    expectedStatusChange.setComponent(componentToIssue);
    
    // Set up expectations
    when(dateGeneratorService.generateDate()).thenReturn(issuedDate);
    when(componentStatusChangeReasonRepository.findFirstComponentStatusChangeReasonForCategory(
        ComponentStatusChangeReasonCategory.ISSUED)).thenReturn(statusChangeReason);
    when(componentRepository.update(componentToIssue)).thenAnswer(returnsFirstArg());
    
    // Call test method
    Component returnedComponent = componentCRUDService.issueComponent(componentToIssue, issueTo);
    
    // Verify
    assertThat(returnedComponent, is(componentToIssue));
    assertThat(returnedComponent.getLocation(), is(issueTo));
    assertThat(returnedComponent.getStatusChanges().iterator().next(), hasSameStateAsComponentStatusChange(expectedStatusChange));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReturnComponentToNonDistributionSite_shouldThrow() {
    // Set up data
    Component componentToReturn = aComponent().withLocation(aUsageSite().build()).build();
    Location returnedTo = aUsageSite().build();

    // Run test
    componentCRUDService.returnComponent(componentToReturn, returnedTo);
  }
  
  @Test
  public void testReturnComponent_shouldUpdateComponentCorrectly() {
    // Set up data
    Component componentToReturn = aComponent()
        .withId(COMPONENT_ID_1)
        .withLocation(aUsageSite().build())
        .withStatus(ComponentStatus.ISSUED)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withDonation(null).build();
    Location returnedTo = aDistributionSite().build();
    ComponentStatusChangeReason statusChangeReason = aReturnReason().build();
    
    Date date = new Date();
    ComponentStatusChange expectedStatusChange = aComponentStatusChange()
        .withId(UUID.randomUUID())
        .withStatusChangedOn(date)
        .withStatusChangeReason(statusChangeReason)
        .build();
    Component expectedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withLocation(returnedTo)
        .withStatus(ComponentStatus.AVAILABLE)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withDonation(null)
        .withComponentStatusChange(expectedStatusChange)
        .build();
    expectedStatusChange.setComponent(expectedComponent);
    
    // Set up mocks
    when(dateGeneratorService.generateDate()).thenReturn(date);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedComponent)))).thenAnswer(returnsFirstArg());
    when(componentStatusChangeReasonRepository.findFirstComponentStatusChangeReasonForCategory(ComponentStatusChangeReasonCategory.RETURNED)).thenReturn(statusChangeReason);
    
    // Run test
    Component returnedComponent = componentCRUDService.returnComponent(componentToReturn, returnedTo);
    
    // Verify
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
    assertThat(returnedComponent.getStatusChanges().iterator().next(), hasSameStateAsComponentStatusChange(expectedStatusChange));
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedComponent)));
  }
  
  @Test
  public void testPutComponentInStock_shouldHaveCorrectInventoryStatus() {
    // Set up data
    Location location = aLocation().build();
    Donation donation = aDonation().build();
    Component componentToPutInStock = aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withDonation(donation).withLocation(location).build();
    Component expectedComponent = aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withDonation(donation).withLocation(location).build();
    
    // Set up mocks
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedComponent)))).thenReturn(expectedComponent);
    
    // Run test
    Component returnedComponent = componentCRUDService.putComponentInStock(componentToPutInStock);
    
    // Verify
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedComponent)));
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
  }

  @Test
  public void testUndiscardComponent_shouldCallRollBackStatusChangesCorrectly() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1).build();
    
    // Set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentConstraintChecker.canUndiscard(component)).thenReturn(true);
    
    // Exercise SUT
    componentCRUDService.undiscardComponent(COMPONENT_ID_1);
    
    // Verify
    verify(componentCRUDService).rollBackComponentStatusChanges(component,
        ComponentStatusChangeReasonCategory.DISCARDED);
  }
  
  @Test
  public void testUnprocessComponent_shouldCallRollBackStatusChangesCorrectly() {
    // Set up fixture
    Component component = aComponent().build();

    // Set up mocks
    when(componentConstraintChecker.canUnprocess(component)).thenReturn(true);
    
    // Exercise SUT
    componentCRUDService.unprocessComponent(component);

    // Verify
    verify(componentCRUDService).rollBackComponentStatusChanges(component, null);
  }
  
  @Test
  public void testPreProcessUnsafeComponent_shouldCallRollBackStatusChangesCorrectly() throws ParseException {
    // Set up fixture
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date bleedStartTime = sdf.parse("2016-01-01 13:00");
    Date bleedEndTime = sdf.parse("2016-01-01 13:16");
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.UNSAFE).build();
    
    // Set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentConstraintChecker.canPreProcess(component)).thenReturn(true);
    
    // Exercise SUT
    componentCRUDService.preProcessComponent(COMPONENT_ID_1, 320, bleedStartTime, bleedEndTime);

    // Verify
    verify(componentCRUDService).rollBackComponentStatusChanges(component, ComponentStatusChangeReasonCategory.UNSAFE,
        ComponentStatusChangeReasonType.INVALID_WEIGHT, ComponentStatusChangeReasonType.LOW_WEIGHT);
  }
  
  @Test
  public void testPreProcessSafeComponent_shouldntCallRollBackStatusChanges() throws ParseException {
    // Set up fixture
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date bleedStartTime = sdf.parse("2016-01-01 13:00");
    Date bleedEndTime = sdf.parse("2016-01-01 13:16");
    Component component = aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.AVAILABLE).build();

    // Set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentConstraintChecker.canPreProcess(component)).thenReturn(true);

    // Exercise SUT
    componentCRUDService.preProcessComponent(COMPONENT_ID_1, 320, bleedStartTime, bleedEndTime);

    // Verify
    verify(componentCRUDService, never()).rollBackComponentStatusChanges(component,
        ComponentStatusChangeReasonCategory.UNSAFE, ComponentStatusChangeReasonType.INVALID_WEIGHT,
        ComponentStatusChangeReasonType.LOW_WEIGHT);
  }

  @Test
  public void testEditWeightToValidRangeForComponentThatCanRollBack_componentStatusQuarantinedAndCorrectStatusChangeDeletes() throws ParseException {
    // Set up fixture
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date bleedStartTime = sdf.parse("2016-01-01 13:00");
    Date bleedEndTime = sdf.parse("2016-01-01 13:16");
    Component component =
        aComponent().withId(COMPONENT_ID_1).withStatus(ComponentStatus.UNSAFE)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type INVALID_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build())
            .build())
        .build();
    
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(component);
    when(componentConstraintChecker.canPreProcess(component)).thenReturn(true);
    when(componentStatusCalculator.shouldComponentBeDiscardedForInvalidWeight(component)).thenReturn(false);
    when(componentStatusCalculator.shouldComponentBeDiscardedForLowWeight(component)).thenReturn(false);
    when(componentRepository.update(component)).thenReturn(component);
    
    // Exercise SUT
    Component returnedComponent = componentCRUDService.preProcessComponent(COMPONENT_ID_1, 320, bleedStartTime, bleedEndTime);

    // Verify
    assertThat(returnedComponent.getStatus(), is(ComponentStatus.QUARANTINED));
    Iterator<ComponentStatusChange> it = returnedComponent.getStatusChanges().iterator();
    ComponentStatusChange unsafe = it.next();
    assertThat(unsafe.getIsDeleted(), is(true));
    assertThat(unsafe.getStatusChangeReason().getCategory(), is(ComponentStatusChangeReasonCategory.UNSAFE));
  }

  @Test
  public void testMarkComponentsBelongingToDonationAsUnsafeIfContainsPlasma_shouldMarkComponentsAsUnsafe() {
    // Set up fixture
    Component firstComponent = aComponent().withId(COMPONENT_ID_1)
        .withComponentType(aComponentType()
            .thatContainsPlasma()
            .build())
        .build();
    Component secondComponent = aComponent().withId(COMPONENT_ID_2)
        .withComponentType(aComponentType()
            .build())
        .build();
    Component thirdComponent = aComponent().withId(COMPONENT_ID_2)
        .withComponentType(aComponentType()
            .thatContainsPlasma()
            .build())
        .build();
    Component deletedComponent = aComponent().withId(COMPONENT_ID_3)
        .withComponentType(aComponentType()
            .thatContainsPlasma()
            .build())
        .withIsDeleted(true)
        .build();

    Donation donation = aDonation()
        .withId(DONATION_ID_1)
        .withComponents(Arrays.asList(firstComponent, secondComponent, thirdComponent, deletedComponent))
        .build();

    // Set up expectations
    doAnswer(returnsFirstArg()).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(firstComponent)), eq(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA));

    // Exercise SUT
    componentCRUDService.markComponentsBelongingToDonationAsUnsafeIfContainsPlasma(donation);

    // Verify
    verify(componentCRUDService).markComponentAsUnsafe(argThat(hasSameStateAsComponent(firstComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA));
    verify(componentCRUDService, never()).markComponentAsUnsafe(argThat(hasSameStateAsComponent(secondComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA));
    verify(componentCRUDService).markComponentAsUnsafe(argThat(hasSameStateAsComponent(thirdComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA));
    verify(componentCRUDService, never()).markComponentAsUnsafe(argThat(hasSameStateAsComponent(deletedComponent)),
        eq(ComponentStatusChangeReasonType.TEST_RESULTS_CONTAINS_PLASMA));
  }
  
  @Test
  public void testCreateInitialComponentWithComponentBatch_shouldReturnComponentBatch() {
    PackType packTypeThatCountsAsDonation = aPackType().withCountAsDonation(true).build();
    Location componentBatchProcessingSite = aProcessingSite().build();
    ComponentBatch componentBatch = aComponentBatch().withLocation(componentBatchProcessingSite).build();
    DonationBatch donationBatchWithComponentBatch = aDonationBatch()
        .withComponentBatch(componentBatch)
        .build();
    Donor existingDonor = aDonor().build();
    Location venue = aVenue().build();
    Donation donation = aDonation()
        .withPackType(packTypeThatCountsAsDonation)
        .withDonationBatch(donationBatchWithComponentBatch)
        .withDonor(existingDonor)
        .withDonationDate(new Date())
        .withBleedStartTime(new Date())
        .withBleedEndTime(new Date())
        .withVenue(venue)
        .build();
    
    // Set up mocks
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.CREATE_INITIAL_COMPONENTS)).thenReturn(true);
    when(donationBatchRepository.findComponentBatchByDonationbatchId(donationBatchWithComponentBatch.getId()))
        .thenReturn(componentBatch);
    when(dateGeneratorService.generateDateTime(donation.getDonationDate(), donation.getBleedEndTime())).thenReturn(new Date());
    
    // Run test
    Component component = componentCRUDService.createInitialComponent(donation);
    
    // Verify
    assertThat(component.getComponentBatch(), is(componentBatch));
    
  }
  
  @Test
  public void testUpdateComponentWithNewPackType_shouldUpdateComponentTypeCodeAndExpiresOn() {
    // Set up data
    Integer expiresAfterDays = 12;
    String componentTypeCode = "new123";
    Date createdOn = new Date();
    Date expiresOn = new Date();

    UUID componentTypeId3 = UUID.randomUUID();
    ComponentType newComponentType = aComponentType()
        .withId(componentTypeId3)
        .withComponentTypeCode(componentTypeCode)
        .withExpiresAfter(expiresAfterDays)
        .build();
    
    PackType newPackType = aPackType().withComponentType(newComponentType).build();

    UUID componentTypeId2 = UUID.randomUUID();
    Component component = aComponent()
        .withComponentType(aComponentType().withId(componentTypeId2).build())
        .withComponentCode("123")
        .withCreatedOn(createdOn)
        .withExpiresOn(expiresOn)
        .build();

    Date expectedExpiresOn = new DateTime(createdOn).plusDays(expiresAfterDays).toDate();
    Component expectedComponent = aComponent()
        .withComponentType(newComponentType)
        .withComponentCode(componentTypeCode)
        .withCreatedOn(createdOn)
        .withExpiresOn(expectedExpiresOn)
        .withStatus(component.getStatus())
        .withInventoryStatus(component.getInventoryStatus())
        .withLocation(component.getLocation())
        .withDonation(component.getDonation())
        .build();
    
    // Run test
    Component updatedComponent = componentCRUDService.updateComponentWithNewPackType(component, newPackType);
    
    // Verify
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedComponent)));
    assertThat(updatedComponent, hasSameStateAsComponent(expectedComponent));
  }

  /**
   * Test process unsafe component with unsafe status change EMBT should create unsafe child
   * components where applicable, which means unsafe if bleed time exceeds maximum bleed time.
   *
   * EMBT: EXCEEDS_MAX_BLEED_TIME.
   */
  @Test
  public void testProcessUnsafeComponentWithUnsafeStatusChangeEMBT_shouldCreateUnsafeChildComponentsWhereApplicable() {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Date donationDate = new Date();
    UUID componentTypeCombinationId = UUID.randomUUID();
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .withBleedStartTime(new DateTime().toDate())
        .withBleedEndTime(new DateTime().plusMinutes(50).toDate())
        .build();
    UUID componentTypeId1 = UUID.randomUUID();
    ComponentType parentComponentType = aComponentType()
        .withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .build();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType componentTypeWithMaxBleedTimeEqualToDonationBleedTime = aComponentType()
        .withId(componentTypeId2)
        .withExpiresAfter(90)
        .withComponentTypeCode("1000")
        .withMaxBleedTime(50)
        .withMaxTimeSinceDonation(20)
        .build();
    UUID componentTypeId3 = UUID.randomUUID();
    ComponentType componentTypeWithMaxBleedTimeGreaterThanDonationBleedTime = aComponentType()
        .withId(componentTypeId3)
        .withExpiresAfter(90)
        .withComponentTypeCode("1001")
        .withMaxBleedTime(51)
        .withMaxTimeSinceDonation(20)
        .build();
    UUID componentTypeId4 = UUID.randomUUID();
    ComponentType componentTypeWithMaxBleedTimeLessThanDonationBleedTime = aComponentType()
        .withId(componentTypeId4)
        .withExpiresAfter(90)
        .withComponentTypeCode("1002")
        .withMaxBleedTime(20)
        .withMaxTimeSinceDonation(25)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentTypeWithMaxBleedTimeEqualToDonationBleedTime, 
            componentTypeWithMaxBleedTimeGreaterThanDonationBleedTime, componentTypeWithMaxBleedTimeLessThanDonationBleedTime))
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.QUARANTINED)
        .withLocation(location)
        .withComponentType(parentComponentType)
        .withComponentCode("0001")
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentType(parentComponentType)
        .withComponentCode("0001")
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedSafeChildComponent = aComponent()
        .withComponentType(componentTypeWithMaxBleedTimeGreaterThanDonationBleedTime)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("1001")
        .build();
    Component expectedUnsafeChildComponent1 = aComponent()
        .withComponentType(componentTypeWithMaxBleedTimeEqualToDonationBleedTime)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("1000")
        .build();
    Component expectedUnsafeChildComponent2 = aComponent()
        .withComponentType(componentTypeWithMaxBleedTimeLessThanDonationBleedTime)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("1002")
        .build();
    Component mockedComponent = aComponent().build();

    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(componentTypeWithMaxBleedTimeEqualToDonationBleedTime);
    when(componentTypeRepository.getComponentTypeById(componentTypeId3)).thenReturn(componentTypeWithMaxBleedTimeGreaterThanDonationBleedTime);
    when(componentTypeRepository.getComponentTypeById(componentTypeId4)).thenReturn(componentTypeWithMaxBleedTimeLessThanDonationBleedTime);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    when(bleedTimeService.getBleedTime(donation.getBleedStartTime(), donation.getBleedEndTime())).thenReturn(50L);
    doReturn(mockedComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedUnsafeChildComponent1)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAX_BLEED_TIME));
    doReturn(mockedComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedUnsafeChildComponent2)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAX_BLEED_TIME));

    // SUT
    parentComponent = componentCRUDService.processComponent(COMPONENT_ID_1, componentTypeCombination.getId(), new Date());

    // verify that the components are created
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedSafeChildComponent)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedUnsafeChildComponent2)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedUnsafeChildComponent1)));
    // verify that only the component that exceeds max bleed time is marked as unsafe
    verify(componentCRUDService, times(1)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedUnsafeChildComponent1)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAX_BLEED_TIME));
    verify(componentCRUDService, times(1)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedUnsafeChildComponent2)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAX_BLEED_TIME));
    verify(componentCRUDService, times(0)).markComponentAsUnsafe(argThat(hasSameStateAsComponent(expectedSafeChildComponent)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAX_BLEED_TIME));
  }

  /**
   * Test process unsafe component with unsafe status change EMTSD should create unsafe child
   * components where applicable, which means unsafe if exceeds max time since donation.
   *
   * EMTSD: EXCEEDS_MAXTIME_SINCE_DONATION.
   */
  @Test
  public void testProcessUnsafeComponentWithUnsafeStatusChangeEMTSD_shouldCreateUnsafeChildComponentsWhereApplicable() {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    Date donationDate = new Date();
    Date processedOn = new DateTime().plusHours(20).toDate();
    UUID componentTypeCombinationId = UUID.randomUUID();
    Donation donation = aDonation().withId(DONATION_ID_1)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID componentTypeId1 = UUID.randomUUID();
    ComponentType parentComponentType = aComponentType()
        .withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .build();
    UUID componentTypeId2 = UUID.randomUUID();
    ComponentType componentTypeMaxTimeSinceDonationLessThanTimeSinceDonation = aComponentType()
        .withId(componentTypeId2)
        .withExpiresAfter(90)
        .withComponentTypeCode("1000")
        .withMaxBleedTime(20)
        .withMaxTimeSinceDonation(5)
        .build();
    UUID componentTypeId3 = UUID.randomUUID();
    ComponentType componentTypeMaxTimeSinceDonationGreaterThanTimeSinceDonation = aComponentType()
        .withId(componentTypeId3)
        .withExpiresAfter(90)
        .withComponentTypeCode("1001")
        .withMaxBleedTime(20)
        .withMaxTimeSinceDonation(21)
        .build();
    UUID componentTypeId4 = UUID.randomUUID();
    ComponentType componentTypeMaxTimeSinceDonationEqualToTimeSinceDonation = aComponentType()
        .withId(componentTypeId4)
        .withExpiresAfter(90)
        .withComponentTypeCode("1002")
        .withMaxBleedTime(20)
        .withMaxTimeSinceDonation(20)
        .build();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(
            componentTypeMaxTimeSinceDonationLessThanTimeSinceDonation, 
            componentTypeMaxTimeSinceDonationGreaterThanTimeSinceDonation,
            componentTypeMaxTimeSinceDonationEqualToTimeSinceDonation))
        .build();
    UUID parentComponentId = COMPONENT_ID_1;
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.QUARANTINED)
        .withLocation(location)
        .withComponentType(parentComponentType)
        .withComponentCode("0001")
        .build();
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentType(parentComponentType)
        .withProcessedOn(new DateTime().plusHours(20).toDate())
        .withComponentCode("0001")
        .build();
    Calendar expiryCal1 = Calendar.getInstance();
    expiryCal1.setTime(donationDate);
    expiryCal1.add(Calendar.DAY_OF_YEAR, 90);
    Component expectedSafeChildComponent = aComponent()
        .withComponentType(componentTypeMaxTimeSinceDonationGreaterThanTimeSinceDonation)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("1001")
        .build();
    Component expectedUnsafeChildComponent1 = aComponent()
        .withComponentType(componentTypeMaxTimeSinceDonationLessThanTimeSinceDonation)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("1000")
        .build();
    Component expectedUnsafeChildComponent2 = aComponent()
        .withComponentType(componentTypeMaxTimeSinceDonationEqualToTimeSinceDonation)
        .withDonation(donation)
        .withParentComponent(expectedParentComponent)
        .withStatus(ComponentStatus.QUARANTINED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(processedOn)
        .withExpiresOn(expiryCal1.getTime())
        .withLocation(location)
        .withComponentCode("1002")
        .build();
    Component mockedComponent = aComponent().build();

    // set up mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId2)).thenReturn(componentTypeMaxTimeSinceDonationLessThanTimeSinceDonation);
    when(componentTypeRepository.getComponentTypeById(componentTypeId3)).thenReturn(componentTypeMaxTimeSinceDonationGreaterThanTimeSinceDonation);
    when(componentTypeRepository.getComponentTypeById(componentTypeId4)).thenReturn(componentTypeMaxTimeSinceDonationEqualToTimeSinceDonation);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId)).thenReturn(componentTypeCombination);
    when(bleedTimeService.getTimeSinceDonation(donation.getDonationDate(), processedOn)).thenReturn(20L);
    doReturn(mockedComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedUnsafeChildComponent1)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAXTIME_SINCE_DONATION));
    doReturn(mockedComponent).when(componentCRUDService).markComponentAsUnsafe(
        argThat(hasSameStateAsComponent(expectedUnsafeChildComponent2)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAXTIME_SINCE_DONATION));

    // SUT
    parentComponent = componentCRUDService.processComponent(COMPONENT_ID_1, componentTypeCombination.getId(), processedOn);

    // verify that the components are created
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedUnsafeChildComponent1)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedUnsafeChildComponent2)));
    verify(componentRepository).save(argThat(hasSameStateAsComponent(expectedSafeChildComponent)));
    // verify that only the component that exceeds max time since donation is marked as unsafe
    verify(componentCRUDService, times(1)).markComponentAsUnsafe(argThat(
        hasSameStateAsComponent(expectedUnsafeChildComponent1)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAXTIME_SINCE_DONATION));
    verify(componentCRUDService, times(1)).markComponentAsUnsafe(argThat(
        hasSameStateAsComponent(expectedUnsafeChildComponent2)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAXTIME_SINCE_DONATION));
    verify(componentCRUDService, times(0)).markComponentAsUnsafe(argThat(
        hasSameStateAsComponent(expectedSafeChildComponent)), eq(ComponentStatusChangeReasonType.EXCEEDS_MAXTIME_SINCE_DONATION));
  }

  @Test
  public void testRecordChildComponentWeightFirstTime_shouldSetComponentWeight() throws Exception {
    // set up data
    Component parentComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withWeight(250)
        .build();
    Component existingComponent = aComponent()
        .withId(COMPONENT_ID_2)
        .withParentComponent(parentComponent)
        .withWeight(null)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_2)).thenReturn(existingComponent);
    when(componentConstraintChecker.canRecordChildComponentWeight(existingComponent)).thenReturn(true);
    when(componentRepository.update(existingComponent)).thenAnswer(returnsFirstArg());
    
    // SUT
    Integer newWeight = 100;
    Component updatedComponent = componentCRUDService.recordChildComponentWeight(COMPONENT_ID_2, newWeight);
    
    // check
    assertThat("Component weight was updated", updatedComponent.getWeight(), is(newWeight));
  }

  @Test
  public void testRecordChildComponentWeight_shouldSetComponentWeight() throws Exception {
    // set up data
    Component parentComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withWeight(250)
        .build();
   
    Component existingComponent = aComponent()
        .withId(COMPONENT_ID_2)
        .withParentComponent(parentComponent)
        .withWeight(100)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_1)).thenReturn(existingComponent);
    when(componentConstraintChecker.canRecordChildComponentWeight(existingComponent)).thenReturn(true);
    when(componentRepository.update(existingComponent)).thenAnswer(returnsFirstArg());
    
    // SUT
    Integer newWeight = 200;
    Component updatedComponent = componentCRUDService.recordChildComponentWeight(COMPONENT_ID_1, newWeight);
    
    // check
    assertThat("Component weight was updated", updatedComponent.getWeight(), is(newWeight));
  }

  @Test
  public void testRecordChildComponentWeightWithoutChange_shouldDoNothing() throws Exception {
    // set up data
    Component parentComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withWeight(250)
        .build();
  
    Component existingComponent = aComponent()
        .withId(COMPONENT_ID_2)
        .withParentComponent(parentComponent)
        .withWeight(100)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_2)).thenReturn(existingComponent);
    when(componentConstraintChecker.canRecordChildComponentWeight(existingComponent)).thenReturn(true);
    
    // SUT
    Integer newWeight = 100;
    componentCRUDService.recordChildComponentWeight(COMPONENT_ID_2, newWeight);
    
    // check
    verify(componentRepository, never()).update(existingComponent);
  }

  @Test(expected = java.lang.IllegalStateException.class)
  public void testRecordChildComponentWeightWithConstraint_shouldThrow() throws Exception {
    // set up data
    Component parentComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withWeight(250)
        .build();

    Component existingComponent = aComponent()
        .withId(COMPONENT_ID_2)
        .withParentComponent(parentComponent)
        .withWeight(100)
        .build();
    
    // mocks
    when(componentRepository.findComponentById(COMPONENT_ID_2)).thenReturn(existingComponent);
    when(componentConstraintChecker.canRecordChildComponentWeight(existingComponent)).thenReturn(false);
    
    // SUT
    Integer newWeight = 200;
    componentCRUDService.recordChildComponentWeight(COMPONENT_ID_2, newWeight);
  }
  
  @Test
  public void testRollBackStatusChangesWithNoCategorySpecified_shouldntDeleteAnyChanges() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type INVALID_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type TEST_RESULTS can't be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS).build())
            .build())
        .build();

    // mocks
    when(componentRepository.update(component)).thenReturn(component);

    // Exercise SUT
    Component returnedComponent = componentCRUDService.rollBackComponentStatusChanges(component, null,
        ComponentStatusChangeReasonType.INVALID_WEIGHT);

    // Verify
    Iterator<ComponentStatusChange> it = returnedComponent.getStatusChanges().iterator();
    ComponentStatusChange changeWithTypeTestResults = it.next();
    assertThat(changeWithTypeTestResults.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.TEST_RESULTS));
    assertThat(changeWithTypeTestResults.getIsDeleted(), is(false));

    ComponentStatusChange changeWithTypeInvalidWeight = it.next();
    assertThat(changeWithTypeInvalidWeight.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.INVALID_WEIGHT));
    assertThat(changeWithTypeInvalidWeight.getIsDeleted(), is(false));

  }

  @Test
  public void testRollBackStatusChangesWithNoTypesSpecified_shouldDeleteRollBackableChangesOnly() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type INVALID_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type TEST_RESULTS can't be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS).build())
            .build())
        .build();
    
    // mocks
    when(componentRepository.update(component)).thenReturn(component);

    // Exercise SUT
    Component returnedComponent =
        componentCRUDService.rollBackComponentStatusChanges(component, ComponentStatusChangeReasonCategory.UNSAFE);

    // Verify
    assertThat(returnedComponent.getStatusChanges().size(), is(2));
    assertThat(returnedComponent.getStatusChanges().first().getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.TEST_RESULTS));
    assertThat(returnedComponent.getStatusChanges().first().getIsDeleted(), is(false));

    assertThat(returnedComponent.getStatusChanges().last().getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.INVALID_WEIGHT));
    assertThat(returnedComponent.getStatusChanges().last().getIsDeleted(), is(true));
  }
  
  @Test
  public void testRollBackStatusChangesWithTypesSpecified_shouldDeleteRollBackableChangesWithSpecifiedTypesOnly() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type INVALID_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type LOW_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.LOW_WEIGHT).build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type TEST_RESULTS can't be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS).build())
            .build())
        .build();
    
    // mocks
    when(componentRepository.update(component)).thenReturn(component);

    // Exercise SUT
    Component returnedComponent =
        componentCRUDService.rollBackComponentStatusChanges(component, ComponentStatusChangeReasonCategory.UNSAFE,
            ComponentStatusChangeReasonType.TEST_RESULTS, ComponentStatusChangeReasonType.INVALID_WEIGHT);

    // Verify
    Iterator<ComponentStatusChange> it = returnedComponent.getStatusChanges().iterator();
    ComponentStatusChange changeWithTypeTestResults = it.next();
    assertThat(changeWithTypeTestResults.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.TEST_RESULTS));
    assertThat(changeWithTypeTestResults.getIsDeleted(), is(false));

    ComponentStatusChange changeWithTypeInvalidWeight = it.next();
    assertThat(changeWithTypeInvalidWeight.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.LOW_WEIGHT));
    assertThat(changeWithTypeInvalidWeight.getIsDeleted(), is(false));

    ComponentStatusChange changeWithTypeLowWeight = it.next();
    assertThat(changeWithTypeLowWeight.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.INVALID_WEIGHT));
    assertThat(changeWithTypeLowWeight.getIsDeleted(), is(true));
  }
  
  @Test
  public void testRollBackStatusChangesWithCategorySpecified_shouldDeleteRollBackableChangesWithSpecifiedCategoryOnly() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type INVALID_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.INVALID_WEIGHT).build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type LOW_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.LOW_WEIGHT).build())
            .build())
        .build();
    
    // mocks
    when(componentRepository.update(component)).thenReturn(component);

    // Exercise SUT
    Component returnedComponent =
        componentCRUDService.rollBackComponentStatusChanges(component, ComponentStatusChangeReasonCategory.DISCARDED);

    // Verify
    Iterator<ComponentStatusChange> it = returnedComponent.getStatusChanges().iterator();

    ComponentStatusChange changeWithTypeInvalidWeight = it.next();
    assertThat(changeWithTypeInvalidWeight.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.LOW_WEIGHT));
    assertThat(changeWithTypeInvalidWeight.getIsDeleted(), is(false));

    ComponentStatusChange changeWithTypeLowWeight = it.next();
    assertThat(changeWithTypeLowWeight.getStatusChangeReason().getType(),
        is(ComponentStatusChangeReasonType.INVALID_WEIGHT));
    assertThat(changeWithTypeLowWeight.getIsDeleted(), is(false));
  }
  
  @Test
  public void testRollBackStatusChangesForComponentWithANonRollBackableUnsafeStatusChangeType_shouldSetStatusToUnsafe() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type TEST_RESULTS can't be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.TEST_RESULTS).build())
            .build())
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type LOW_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.LOW_WEIGHT).build())
            .build())
        .build();

    // mocks
    when(componentRepository.update(component)).thenReturn(component);

    // Exercise SUT
    Component returnedComponent = componentCRUDService.rollBackComponentStatusChanges(component, null);

    // Verify
    assertThat(returnedComponent.getStatus(), is(ComponentStatus.UNSAFE));
  }
  
  /**
   * Test roll back status changes for component with only a roll backable unsafe status change
   * type_should set status to quarantined.
   * 
   * Note that the status will be updated by the componentStatusCalculator, when updating the
   * component, but the initial status before that, will be quarantined.
   */
  @Test
  public void testRollBackStatusChangesForComponentWithNoNonRollBackableUnsafeStatusChangeType_shouldSetStatusToQuarantined() {
    // Set up fixture
    Component component = aComponent().withId(COMPONENT_ID_1)
        .withComponentStatusChange(aComponentStatusChange()
            .withStatusChangeReason(anUnsafeReason()
            // type LOW_WEIGHT can be rolled back
            .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.LOW_WEIGHT).build())
            .build())
        .build();

    // mocks
    when(componentRepository.update(component)).thenReturn(component);

    // Exercise SUT
    Component returnedComponent = componentCRUDService.rollBackComponentStatusChanges(component, null);

    // Verify
    assertThat(returnedComponent.getStatus(), is(ComponentStatus.QUARANTINED));
  }

  @Test
  public void testTransfuseComponent_shouldHaveCorrectStatus() {
    // Set up data
    Location location = aLocation().build();
    Donation donation = aDonation().build();
    Component transfusedComponent = aComponent()
        .withStatus(ComponentStatus.ISSUED)
        .withDonation(donation)
        .withLocation(location)
        .build();
    Component expectedComponent = aComponent()
        .withStatus(ComponentStatus.TRANSFUSED)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    // Set up mocks
    when(componentConstraintChecker.canTransfuse(argThat(hasSameStateAsComponent(transfusedComponent)))).thenReturn(true);
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedComponent)))).thenReturn(expectedComponent);
    
    // Run test
    Component returnedComponent = componentCRUDService.transfuseComponent(transfusedComponent);
    
    // Verify
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedComponent)));
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
  }

  @Test(expected = IllegalStateException.class)
  public void testTransfuseComponentThatsAvailable_shouldThrow() {
    // Set up data
    Location location = aLocation().build();
    Donation donation = aDonation().build();
    Component transfusedComponent = aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    // Set up mocks
    when(componentConstraintChecker.canTransfuse(argThat(hasSameStateAsComponent(transfusedComponent)))).thenReturn(false);
    
    // Run test
    componentCRUDService.transfuseComponent(transfusedComponent);
  }
  
  @Test
  public void testUnTransfuseComponentThatsTransfused_shouldReturnComponentInCorrectState() {
    // Set up data
    Location location = aLocation().thatIsUsageSite().build();
    Donation donation = aDonation().build();
    
    Component transfusedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.TRANSFUSED)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    Component expectedComponent = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.ISSUED)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    // set up mocks
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedComponent)))).thenReturn(expectedComponent);
    
    // Run test
    Component returnedComponent = componentCRUDService.untransfuseComponent(transfusedComponent);
    
    // Verify
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedComponent)));
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testUnTransfuseComponentThatsNotTransfused_shouldThrow() {
    // Set up data
    Location location = aLocation().thatIsUsageSite().build();
    Donation donation = aDonation().build();
    Component component = aComponent()
        .withId(COMPONENT_ID_1)
        .withStatus(ComponentStatus.ISSUED)
        .withDonation(donation)
        .withLocation(location)
        .build();
    
    // Run test
    componentCRUDService.untransfuseComponent(component);
  }

  public void testProcessComponentWithNullProcessedOnDate_shouldIgnoreTimeSinceDonationCheck() throws Exception {
    // set up data
    Location location = LocationBuilder.aLocation().build();
    UUID componentTypeId1 = UUID.randomUUID();
    ComponentType componentType = aComponentType().withId(componentTypeId1)
        .withComponentTypeCode("0001")
        .withComponentTypeName("#1")
        .withExpiresAfter(90)
        .withExpiresAfterUnits(ComponentTypeTimeUnits.DAYS)
        .build();
    Date donationDate = new Date(); 
    UUID donationId = UUID.randomUUID();
    Donation donation = aDonation().withId(donationId)
        .withDonationIdentificationNumber("1234567")
        .withDonationDate(donationDate)
        .build();
    UUID parentComponentId = UUID.randomUUID();
    ComponentBatch componentBatch = ComponentBatchBuilder.aComponentBatch().withLocation(location).build();
    Component parentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(location)
        .withComponentBatch(componentBatch)
        .build();
    UUID componentTypeCombinationId = UUID.randomUUID();
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination().withId(componentTypeCombinationId)
        .withCombinationName("Combination")
        .withComponentTypes(Arrays.asList(componentType))
        .build();
    Date processedOn = null;
    Component expectedParentComponent = aComponent().withId(parentComponentId)
        .withDonation(donation)
        .withCreatedOn(donationDate)
        .withStatus(ComponentStatus.PROCESSED)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withCreatedOn(donation.getDonationDate())
        .withLocation(location)
        .withComponentBatch(componentBatch)
        .withProcessedOn(processedOn)
        .build();
    // set up mocks
    when(componentRepository.findComponentById(parentComponentId)).thenReturn(parentComponent);
    when(componentConstraintChecker.canProcess(parentComponent)).thenReturn(true);
    when(componentTypeRepository.getComponentTypeById(componentTypeId1)).thenReturn(componentType);  
    when(componentRepository.update(argThat(hasSameStateAsComponent(expectedParentComponent)))).thenReturn(expectedParentComponent);
    when(componentTypeCombinationRepository.findComponentTypeCombinationById(componentTypeCombinationId))
        .thenReturn(componentTypeCombination);
    
    // SUT
    Component processedComponent = componentCRUDService.processComponent(parentComponentId, componentTypeCombination.getId(), processedOn);
    
    // verify results
    verify(componentRepository).update(argThat(hasSameStateAsComponent(expectedParentComponent)));
    // check that processedOn date is null
    assertThat(processedComponent.getProcessedOn(), is(expectedParentComponent.getProcessedOn()));
  }
}
