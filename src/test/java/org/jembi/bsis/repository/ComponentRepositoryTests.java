package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentProductionDTOBuilder.aComponentProductionDTO;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aDiscardReason;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DiscardedComponentDTOBuilder.aDiscardedComponentDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.ComponentMatcher.hasSameStateAsComponent;
import static org.jembi.bsis.helpers.matchers.ComponentProductionDTOMatcher.hasSameStateAsComponentProductionDTO;
import static org.jembi.bsis.helpers.matchers.DiscardedComponentDTOMatcher.hasSameStateAsDiscardedComponentDTO;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.ComponentExportDTO;
import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.dto.DiscardedComponentDTO;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentmovement.ComponentStatusChange;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private ComponentRepository componentRepository;
  
  @Test
  public void testFindComponentByCodeAndDIN_shouldReturnMatchingComponent() {
    
    String componentCode = "0011-01";
    String donationIdentificationNumber = "0000002";
    
    Donation donationWithExpectedDonationIdentificationNumber = aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Excluded by component code
    aComponent()
        .withComponentCode("0011-02")
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Excluded by donation identification number
    aComponent()
        .withComponentCode(componentCode)
        .withDonation(aDonation().withDonationIdentificationNumber("1000007").build())
        .buildAndPersist(entityManager);
    
    // Expected
    Component expectedComponent = aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .buildAndPersist(entityManager);
    
    // Test
    Component returnedComponent = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);;
    
    // Verify
    assertThat(returnedComponent, is(expectedComponent));
  }
  
  @Test(expected = NoResultException.class)
  public void testFindNonExistentComponentByCodeAndDIN_shoulThrow() {
    // Test
    componentRepository.findComponentByCodeAndDIN("", "");
  }


  @Test
  public void testComponentExists() throws Exception {
    Component component = ComponentBuilder.aComponent().buildAndPersist(entityManager);
    Assert.assertTrue("Component exists", componentRepository.verifyComponentExists(component.getId()));
  }

  @Test
  public void testEntityDoesNotExist() throws Exception {
    Assert.assertFalse("Component does not exist", componentRepository.verifyComponentExists(UUID.randomUUID()));
  }

  @Test
  public void testfindChildComponents() throws Exception {
    Component parentComponent = aComponent().buildAndPersist(entityManager);
    Component child1 = aComponent().withParentComponent(parentComponent).buildAndPersist(entityManager);
    Component child2 = aComponent().withParentComponent(parentComponent).buildAndPersist(entityManager);

    List<Component> children = componentRepository.findChildComponents(parentComponent);

    Assert.assertTrue("contains child1", children.contains(child1));
    Assert.assertTrue("contains child2", children.contains(child2));

  }

  @Test
  public void testFindAnyComponent_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date donationDateFrom = new DateTime().minusDays(7).toDate();
    Date donationDateTo = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().withComponentTypeCode("test2").buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(secondComponentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by status
    aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by donationDate
    aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(aDonation().withDonationDate(new DateTime().minusDays(20).toDate()).build())
        .withLocation(location)
        .withComponentType(componentType)
        .buildAndPersist(entityManager);

    // Excluded by Location
    aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(aLocation().build())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by ComponentType
    aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withComponentType(aComponentType().build())
        .withLocation(location)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    List<UUID> componentTypes = Arrays.asList(componentType.getId(), secondComponentType.getId());

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findAnyComponent(componentTypes, ComponentStatus.DISCARDED, donationDateFrom, donationDateTo, location.getId());

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindAnyComponentWithNullLocation_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date donationDateFrom = new DateTime().minusDays(7).toDate();
    Date donationDateTo = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().withComponentTypeCode("test2").buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(aLocation().build())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(secondComponentType)
            .withLocation(aLocation().build())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );
    List<UUID> componentTypes = Arrays.asList(componentType.getId(), secondComponentType.getId());

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findAnyComponent(componentTypes, ComponentStatus.DISCARDED, donationDateFrom, donationDateTo, null);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindAnyComponentWithNullStatus_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date donationDateFrom = new DateTime().minusDays(7).toDate();
    Date donationDateTo = new DateTime().plusDays(2).toDate();
    Location location = aLocation().build();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().withComponentTypeCode("test2").buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.EXPIRED)
            .withDonation(donation)
            .withComponentType(secondComponentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );
    List<UUID> componentTypes = Arrays.asList(componentType.getId(), secondComponentType.getId());

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findAnyComponent(componentTypes, null, donationDateFrom, donationDateTo, location.getId());

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindAnyComponentWithNullDonationDateFrom_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date donationDateTo = new DateTime().plusDays(2).toDate();
    Location location = aLocation().build();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().withComponentTypeCode("test2").buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(aDonation().withDonationDate(new Date()).build())
            .withComponentType(secondComponentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );
    List<UUID> componentTypes = Arrays.asList(componentType.getId(), secondComponentType.getId());

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findAnyComponent(componentTypes, ComponentStatus.DISCARDED, null, donationDateTo, null);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindAnyComponentWithNullDonationDateTo_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date donationDateFrom = new DateTime().minusDays(7).toDate();
    Location location = aLocation().build();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().withComponentTypeCode("test2").buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(secondComponentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );
    List<UUID> componentTypes = Arrays.asList(componentType.getId(), secondComponentType.getId());

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findAnyComponent(componentTypes, ComponentStatus.DISCARDED, donationDateFrom, null, location.getId());

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindAnyComponentWithNullComponentTypeIds_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date donationDateFrom = new DateTime().minusDays(7).toDate();
    Date donationDateTo = new DateTime().plusDays(2).toDate();
    Location location = aLocation().build();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    ComponentType secondComponentType = aComponentType().withComponentTypeCode("test2").buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withComponentType(secondComponentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findAnyComponent(null, ComponentStatus.DISCARDED, donationDateFrom, donationDateTo, location.getId());

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumberAndStatus_shouldReturnCorrectComponents() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Donation donation = aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );
    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by status
    aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by donation
    aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(aDonation().build())
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findComponentsByDonationIdentificationNumberAndStatus(
        donationIdentificationNumber, ComponentStatus.DISCARDED);
    
    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindComponentsForExport_shouldReturnComponentExportDTOsWithTheCorrectState() {
    // Set up fixture
    String donationIdentificationNumber = "9955741";
    String componentCode = "2011-01";
    String createdByUsername = "created.by";
    Date createdDate = new DateTime().minusDays(29).toDate();
    String parentComponentCode = "0011";
    Date createdOn = new DateTime().minusDays(7).toDate();
    ComponentStatus status = ComponentStatus.DISCARDED;
    String locationName = "Somewhere";
    Date issuedOn = new DateTime().minusDays(6).toDate();
    InventoryStatus inventoryStatus = InventoryStatus.REMOVED;
    Date discardedOn = new DateTime().minusDays(5).toDate();
    Date expiresOn = new DateTime().plusDays(30).toDate();
    String notes = "It's green!";
    String discardReason = "Bad blood";
    Component parentComponent = aComponent()
        .withCreatedDate(new DateTime(createdDate).minusDays(1).toDate()) // Create parent before child
        .withComponentCode(parentComponentCode)
        .build();
    
    // Expected
    Component component = aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .withComponentCode(componentCode)
        .withCreatedBy(aUser().withUsername(createdByUsername).build())
        .withCreatedDate(createdDate)
        .withParentComponent(parentComponent)
        .withCreatedOn(createdOn)
        .withStatus(status)
        .withLocation(aVenue().withName(locationName).build())
        .withIssuedOn(issuedOn)
        .withInventoryStatus(inventoryStatus)
        .withDiscardedOn(discardedOn)
        .withExpiresOn(expiresOn)
        .withNotes(notes)
        .buildAndPersist(entityManager);
    System.out.println(parentComponent.getId());
    System.out.println(component.getId());
    // Excluded issued status change reason
    aComponentStatusChange()
        .withComponent(component)
        .withStatusChangeReason(aComponentStatusChangeReason()
                .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.ISSUED)
                .withStatusChangeReason("Issued component")
                .build())
        .buildAndPersist(entityManager);

    // Excluded returned status change reason
    aComponentStatusChange()
        .withComponent(component)
        .withStatusChangeReason(aComponentStatusChangeReason()
                .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.RETURNED)
                .withStatusChangeReason("Returned component")
                .build())
        .buildAndPersist(entityManager);
    
    // Expected discard status change reason
    aComponentStatusChange()
        .withComponent(component)
        .withStatusChangeReason(aComponentStatusChangeReason()
                .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED)
                .withStatusChangeReason(discardReason)
                .build())
        .buildAndPersist(entityManager);
    
    // Deleted discard status change reason
    aComponentStatusChange()
        .thatIsDeleted()
        .withComponent(component)
        .withStatusChangeReason(aComponentStatusChangeReason()
                .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED)
                .withStatusChangeReason("Deleted undiscard")
                .build())
        .buildAndPersist(entityManager);
  
    // Excluded by deleted
    Component c = aComponent().thatIsDeleted().buildAndPersist(entityManager);
    System.out.println(c.getId());
    // Exercise SUT
    Set<ComponentExportDTO> returnedDTOs = componentRepository.findComponentsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(2));
       
    // Verify DTO parent state
    Iterator<ComponentExportDTO> iterator = returnedDTOs.iterator();
    assertThat(iterator.next().getComponentCode(), is(parentComponentCode));
    
    // Verify DTO state
    ComponentExportDTO returnedDTO = iterator.next();
    assertThat(returnedDTO.getDonationIdentificationNumber(), is(donationIdentificationNumber));
    assertThat(returnedDTO.getComponentCode(), is(componentCode));
    assertThat(returnedDTO.getCreatedBy(), is(createdByUsername));
    assertThat(returnedDTO.getCreatedDate(), isSameDayAs(createdDate));
    assertThat(returnedDTO.getLastUpdatedBy(), is(USERNAME));
    assertThat(returnedDTO.getLastUpdated(), isSameDayAs(new Date()));
    assertThat(returnedDTO.getParentComponentCode(), is(parentComponentCode));
    assertThat(returnedDTO.getCreatedOn(), isSameDayAs(createdOn));
    assertThat(returnedDTO.getStatus(), is(status));
    assertThat(returnedDTO.getLocation(), is(locationName));
    assertThat(returnedDTO.getIssuedOn(), isSameDayAs(issuedOn));
    assertThat(returnedDTO.getInventoryStatus(), is(inventoryStatus));
    assertThat(returnedDTO.getDiscardedOn(), isSameDayAs(discardedOn));
    assertThat(returnedDTO.getExpiresOn(), isSameDayAs(expiresOn));
    assertThat(returnedDTO.getNotes(), is(notes));
    assertThat(returnedDTO.getDiscardReason(), is(discardReason));
  }
  
  @Test
  public void testFindComponentsForExport_shouldOrderResultsByCreatedDate() {
    // Set up fixture
    String firstDonationIdentificationNumber = "1233322";
    String secondDonationIdentificationNumber = "6666666";
    String thirdDonationIdentificationNumber = "333333";
    
    Component componentWithStatusChanges = aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber(secondDonationIdentificationNumber).build())
        .withCreatedDate(new DateTime().minusDays(5).toDate())
        .buildAndPersist(entityManager);
    aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber(firstDonationIdentificationNumber).build())
        .withCreatedDate(new DateTime().minusDays(10).toDate())
        .buildAndPersist(entityManager);
    aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber(thirdDonationIdentificationNumber).build())
        .withCreatedDate(new DateTime().minusDays(1).toDate())
        .buildAndPersist(entityManager);

    // Add a status change to make sure that it does not affect sort order
    aComponentStatusChange()
        .withComponent(componentWithStatusChanges)
        .withStatusChangeReason(aComponentStatusChangeReason()
                .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED)
                .build())
        .buildAndPersist(entityManager);
    
    // Exercise SUT
    Set<ComponentExportDTO> returnedDTOs = componentRepository.findComponentsForExport();
    
    // Verify
    assertThat(returnedDTOs.size(), is(3));
    Iterator<ComponentExportDTO> iterator = returnedDTOs.iterator();
    assertThat(iterator.next().getDonationIdentificationNumber(), is(firstDonationIdentificationNumber));
    assertThat(iterator.next().getDonationIdentificationNumber(), is(secondDonationIdentificationNumber));
    assertThat(iterator.next().getDonationIdentificationNumber(), is(thirdDonationIdentificationNumber));
  }
  
  @Test
  public void testFindProducedComponentsByProcessingSite_shouldReturnCorrectResult() {
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    Date outOfRangeDate = new DateTime().plus(2).toDate();

    Location processingSite1 = aProcessingSite().withName("processingSite1").buildAndPersist(entityManager);
    Location processingSite2 = aProcessingSite().withName("processingSite2").buildAndPersist(entityManager);
    String expectedBloodAbo = "A";
    String expectedBloodRh = "+";
   
    ComponentType componentType = aComponentType()
        .withComponentTypeName("type1")
        .thatCanBeIssued()
        .buildAndPersist(entityManager);

    ComponentBatch componentBatch = aComponentBatch()
        .withLocation(processingSite1)
        .buildAndPersist(entityManager);
    
    Donation donation = aDonation()
        .withDonationDate(startDate)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .buildAndPersist(entityManager);

    // Expected components
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatch)
        .withDonation(donation)
        .withCreatedOn(startDate)
        .withLocation(processingSite1)
        .buildAndPersist(entityManager);
    
    // Excluded component by deletion
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatch)
        .withDonation(donation)
        .withLocation(processingSite1)
        .thatIsDeleted()
        .buildAndPersist(entityManager);
  
    // Excluded components by date range
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatch)
        .withDonation(donation)
        .withCreatedOn(outOfRangeDate)
        .buildAndPersist(entityManager);
    
    // Excluded component by processing Site
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatch)
        .withCreatedOn(endDate)
        .withDonation(donation)
        .withLocation(processingSite2)
        .buildAndPersist(entityManager);
    
    // Excluded component by component Type that cannot be issued. 
    ComponentType componentTypeThatCannotBeIssued = aComponentType()
        .withComponentTypeName("typeThatCannotBeIssued")
        .thatCanNotBeIssued()
        .buildAndPersist(entityManager);
    aComponent()
        .withComponentType(componentTypeThatCannotBeIssued)
        .withComponentBatch(componentBatch)
        .withDonation(donation)
        .withCreatedOn(startDate)
        .buildAndPersist(entityManager);

    // Excluded component by status PROCESSED
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatch)
        .withDonation(donation)
        .withCreatedOn(startDate)
        .withLocation(processingSite1)
        .withStatus(ComponentStatus.PROCESSED)
        .buildAndPersist(entityManager);
    
    ComponentProductionDTO expectedDTO =  aComponentProductionDTO() 
        .withComponentTypeName("type1")
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .withProcessingSite(processingSite1)
        .withCount(2L)
        .build();

    List<ComponentProductionDTO> returnedDtos = componentRepository
        .findProducedComponentsByProcessingSite(
            processingSite1.getId(), startDate, endDate);
     
    assertThat(returnedDtos.size(), is(1));
    assertThat(returnedDtos.get(0), hasSameStateAsComponentProductionDTO(expectedDTO));
  }

  @Test
  public void testFindSummaryOfDiscardedComponentsByProcessingSite_shouldIncludeCorrectData() {
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(8).toDate();

    Location expectedProcessingSite = aProcessingSite()
        .buildAndPersist(entityManager);
    ComponentStatus expectedComponentStatus = ComponentStatus.DISCARDED;
    ComponentType componentType1 = aComponentType()
        .thatContainsPlasma()
        .withComponentTypeName("Whole Blood Quad Pack - CPDA")
        .withComponentTypeCode("100111")
        .thatCanNotBeIssued()
        .buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType()
        .thatCanBeIssued()
        .withComponentTypeCode("100011")
        .withComponentTypeName("Packed Red Cells - CPDA")
        .buildAndPersist(entityManager);
    ComponentBatch componentBatch = aComponentBatch()
        .withLocation(expectedProcessingSite)
        .buildAndPersist(entityManager);
    ComponentStatusChangeReason discardReason1 = aDiscardReason()
        .withStatusChangeReason("Passed Expiry Dates")
        .buildAndPersist(entityManager);
    ComponentStatusChangeReason discardReason2 = aDiscardReason()
        .withStatusChangeReason("Processing Problems")
        .buildAndPersist(entityManager);
    Component component1 = aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatch)
        .withStatus(expectedComponentStatus)
        .buildAndPersist(entityManager);
    Component componentWithStatusAvailable = aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatch)
        .withStatus(ComponentStatus.AVAILABLE)
        .buildAndPersist(entityManager);
    Component componentWithNoComponentBatch = aComponent()
        .withComponentType(componentType2)
        .withStatus(expectedComponentStatus)
        .buildAndPersist(entityManager);
    Component component2 = aComponent()
        .withComponentType(componentType2)
        .withComponentBatch(componentBatch)
        .withStatus(expectedComponentStatus)
        .buildAndPersist(entityManager);
    ComponentStatusChange componentStatusChange1 = aComponentStatusChange()
        .withStatusChangeReason(discardReason1)
        .withNewStatus(expectedComponentStatus)
        .withStatusChangedOn(new Date())
        .withComponent(component1)
        .buildAndPersist(entityManager);
    ComponentStatusChange componentStatusChange2 = aComponentStatusChange()
        .withStatusChangeReason(discardReason2)
        .withNewStatus(expectedComponentStatus)
        .withComponent(component2)
        .withStatusChangedOn(new Date())
        .buildAndPersist(entityManager);

    // Excluded by date out of range
    aComponentStatusChange()
        .withComponent(component1)
        .withStatusChangeReason(discardReason1)
        .withStatusChangedOn(new DateTime().minusDays(9).toDate())
        .buildAndPersist(entityManager);

    // Excluded by ComponentStatus AVAILABLE
    aComponentStatusChange()
        .withComponent(componentWithStatusAvailable)
        .withStatusChangeReason(discardReason2)
        .buildAndPersist(entityManager);

    // Excluded for it is deleted
    aComponentStatusChange()
        .withStatusChangeReason(discardReason1)
        .withNewStatus(expectedComponentStatus)
        .withStatusChangedOn(new Date())
        .withComponent(component1)
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    // Excluded by newStatus AVAILABLE
    aComponentStatusChange()
        .withComponent(component1)
        .withStatusChangeReason(discardReason1)
        .withNewStatus(ComponentStatus.AVAILABLE)
        .withStatusChangedOn(new Date())
        .buildAndPersist(entityManager);

      // Excluded by NULL component batch
    aComponentStatusChange()
        .withComponent(componentWithNoComponentBatch)
        .withStatusChangeReason(discardReason1)
        .withStatusChangedOn(new Date())
        .buildAndPersist(entityManager);

    List<DiscardedComponentDTO> expectedDtos = Arrays.asList(
    aDiscardedComponentDTO()
        .withComponentType(componentType1.getComponentTypeName())
        .withVenue(expectedProcessingSite)
        .withComponentStatusChangeReason(componentStatusChange1.getStatusChangeReason().getStatusChangeReason())
        .withCount(1L)
        .build(),
    aDiscardedComponentDTO()
        .withComponentType(componentType2.getComponentTypeName())
        .withComponentStatusChangeReason(componentStatusChange2.getStatusChangeReason().getStatusChangeReason())
        .withVenue(expectedProcessingSite)
        .withCount(1L)
        .build()
    ); 

    List<DiscardedComponentDTO> returnedDtos = componentRepository.findSummaryOfDiscardedComponentsByProcessingSite(
            expectedProcessingSite.getId(), startDate, endDate);

    assertThat(returnedDtos, is(expectedDtos));
    assertThat(returnedDtos.get(0), hasSameStateAsDiscardedComponentDTO(expectedDtos.get(0)));
    assertThat(returnedDtos.get(1), hasSameStateAsDiscardedComponentDTO(expectedDtos.get(1)));
  }

  @Test
  public void testFindfindSummaryOfDiscardedComponentsByProcessingSite_shouldReturnCorrectCounts() {
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(8).toDate();

    Location expectedProcessingSite = aProcessingSite()
        .buildAndPersist(entityManager);
    ComponentStatus expectedComponentStatus = ComponentStatus.DISCARDED;
    ComponentType componentType1 = aComponentType()
        .thatContainsPlasma()
        .withComponentTypeName("Whole Blood Quad Pack - CPDA")
        .withComponentTypeCode("100111")
        .thatCanNotBeIssued()
        .buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType()
        .thatCanBeIssued()
        .withComponentTypeCode("100011")
        .withComponentTypeName("Packed Red Cells - CPDA")
        .buildAndPersist(entityManager);
    ComponentBatch componentBatch = aComponentBatch()
        .withLocation(expectedProcessingSite)
        .buildAndPersist(entityManager);
    ComponentStatusChangeReason discardReason1 = aDiscardReason()
        .withStatusChangeReason("Passed Expiry Dates")
        .buildAndPersist(entityManager);
    ComponentStatusChangeReason discardReason2 = aDiscardReason()
        .withStatusChangeReason("Processing Problems")
        .buildAndPersist(entityManager);
    Component component1 = aComponent()
        .withComponentType(componentType1)
        .withStatus(expectedComponentStatus)
        .withComponentBatch(componentBatch)
        .buildAndPersist(entityManager);
    Component component2 = aComponent()
        .withComponentType(componentType2)
        .withComponentBatch(componentBatch)
        .withStatus(expectedComponentStatus)
        .buildAndPersist(entityManager);
    Component component3 = aComponent()
        .withComponentType(componentType2)
        .withComponentBatch(componentBatch)
        .withStatus(expectedComponentStatus)
        .buildAndPersist(entityManager);

    // Expected
    aComponentStatusChange()
        .withStatusChangeReason(discardReason1)
        .withNewStatus(expectedComponentStatus)
        .withStatusChangedOn(new Date())
        .withComponent(component1)
        .buildAndPersist(entityManager);
    ComponentStatusChange componentStatusChange2 = aComponentStatusChange()
        .withStatusChangeReason(discardReason2)
        .withNewStatus(expectedComponentStatus)
        .withComponent(component2)
        .withStatusChangedOn(new Date())
        .buildAndPersist(entityManager);
    aComponentStatusChange()
        .withStatusChangeReason(discardReason2)
        .withNewStatus(expectedComponentStatus)
        .withStatusChangedOn(new Date())
        .withComponent(component3)
        .buildAndPersist(entityManager);

    List<DiscardedComponentDTO> expectedDtos = Arrays.asList(
    aDiscardedComponentDTO()
        .withComponentType(componentType1.getComponentTypeName())
        .withVenue(expectedProcessingSite)
        .withComponentStatusChangeReason(discardReason1.getStatusChangeReason())
        .withCount(1L)
        .build(),
    aDiscardedComponentDTO()
        .withComponentType(componentType2.getComponentTypeName())
        .withComponentStatusChangeReason(componentStatusChange2.getStatusChangeReason().getStatusChangeReason())
        .withVenue(expectedProcessingSite)
        .withCount(2L).build()
    );

    List<DiscardedComponentDTO> returnedDtos = componentRepository.findSummaryOfDiscardedComponentsByProcessingSite(
        expectedProcessingSite.getId(), startDate, endDate);

    assertThat(returnedDtos, is(expectedDtos));
    assertThat(returnedDtos.get(0), hasSameStateAsDiscardedComponentDTO(expectedDtos.get(0)));
    assertThat(returnedDtos.get(1), hasSameStateAsDiscardedComponentDTO(expectedDtos.get(1)));
  }

  @Test
  public void testFindfindSummaryOfDiscardedComponentsByProcessingSite_shouldReturnCorrectCountsWhenNoProcessingSiteIsProvided() {
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(8).toDate();

    Location expectedVenue1 = aProcessingSite()
        .buildAndPersist(entityManager);
    Location expectedVenue2 = aProcessingSite()
        .buildAndPersist(entityManager);
    ComponentStatus expectedComponentStatus = ComponentStatus.DISCARDED;
    ComponentType componentType1 = aComponentType()
        .thatContainsPlasma()
        .withComponentTypeName("Whole Blood Quad Pack - CPDA")
        .withComponentTypeCode("100111")
        .thatCanNotBeIssued()
        .buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType()
        .thatCanBeIssued()
        .withComponentTypeCode("100011")
        .withComponentTypeName("Packed Red Cells - CPDA")
        .buildAndPersist(entityManager);
    ComponentBatch componentBatch = aComponentBatch()
        .withLocation(expectedVenue1)
        .buildAndPersist(entityManager);
    ComponentBatch componentBatch2 = aComponentBatch()
        .withLocation(expectedVenue2)
        .buildAndPersist(entityManager);
    ComponentStatusChangeReason discardReason1 = aDiscardReason()
        .withStatusChangeReason("Passed Expiry Dates")
        .buildAndPersist(entityManager);
    ComponentStatusChangeReason discardReason2 = aDiscardReason()
        .withStatusChangeReason("Processing Problems")
        .buildAndPersist(entityManager);
    Component component1 = aComponent()
        .withComponentType(componentType1)
        .withStatus(expectedComponentStatus)
        .withComponentBatch(componentBatch)
        .buildAndPersist(entityManager);
    Component component2 = aComponent()
        .withComponentType(componentType2)
        .withComponentBatch(componentBatch2)
        .withStatus(expectedComponentStatus)
        .buildAndPersist(entityManager);

    // Expected
    aComponentStatusChange()
        .withStatusChangeReason(discardReason1)
        .withNewStatus(expectedComponentStatus)
        .withStatusChangedOn(new Date())
        .withComponent(component1)
        .buildAndPersist(entityManager);
    ComponentStatusChange componentStatusChange2 = aComponentStatusChange()
        .withStatusChangeReason(discardReason2)
        .withNewStatus(expectedComponentStatus)
        .withComponent(component2)
        .withStatusChangedOn(new Date())
        .buildAndPersist(entityManager);

    List<DiscardedComponentDTO> expectedDtos = Arrays.asList(
    aDiscardedComponentDTO()
        .withComponentType(componentType1.getComponentTypeName())
        .withVenue(expectedVenue1)
        .withComponentStatusChangeReason(discardReason1.getStatusChangeReason())
        .withCount(1L)
        .build(),
    aDiscardedComponentDTO()
        .withComponentType(componentType2.getComponentTypeName())
        .withComponentStatusChangeReason(componentStatusChange2.getStatusChangeReason().getStatusChangeReason())
        .withVenue(expectedVenue2)
        .withCount(1L).build()
    );

    List<DiscardedComponentDTO> returnedDtos = componentRepository.findSummaryOfDiscardedComponentsByProcessingSite(
        null, startDate, endDate);

    assertThat(returnedDtos, is(expectedDtos));
    assertThat(returnedDtos.get(0), hasSameStateAsDiscardedComponentDTO(returnedDtos.get(0)));
    assertThat(returnedDtos.get(1), hasSameStateAsDiscardedComponentDTO(expectedDtos.get(1)));
  }
 
  @Test
  public void testFindProducedComponentsByProcessingSite_shouldReturnDataForAllSites() {
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().minusDays(2).toDate();
    
    Location pretoriaProcessingSite = aProcessingSite().withName("Pretoria").buildAndPersist(entityManager);
    Location capeTownProcessingSite = aProcessingSite().withName("Cape Town").buildAndPersist(entityManager);
    Location zuluLandProcessingSite = aProcessingSite().withName("Zulu land").buildAndPersist(entityManager);
    
    String bloodAboA = "A";
    String bloodRhPos = "+";
    String bloodAboB = "B";
    String bloodRhNeg = "-";
        
    ComponentType componentType1 = aComponentType()
        .withComponentTypeName("componentType1")
        .thatCanBeIssued()
        .buildAndPersist(entityManager);
    
    ComponentType componentType2 = aComponentType()
        .withComponentTypeName("componentType2")
        .thatCanBeIssued()
        .buildAndPersist(entityManager);
   
    // Expected component batches
    ComponentBatch componentBatchOneWithLocationPretoria = aComponentBatch()
        .withLocation(pretoriaProcessingSite)
        .buildAndPersist(entityManager);
    
    ComponentBatch componentBatchTwoWithLocationCapeTown = aComponentBatch()
         .withLocation(capeTownProcessingSite)
         .buildAndPersist(entityManager);
    
    ComponentBatch componentBatchThreewithLocationZululand = aComponentBatch()
        .withLocation(zuluLandProcessingSite)
        .buildAndPersist(entityManager);
   
    Donation donationAPos = aDonation()
        .withDonationDate(startDate)
        .withBloodAbo(bloodAboA)
        .withBloodRh(bloodRhPos)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    
    Donation donationBNeg = aDonation()
        .withDonationDate(startDate)
        .withBloodAbo(bloodAboB)
        .withBloodRh(bloodRhNeg)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
  
    //group 1 -> Pretoria, componentType1, A+, (count 2)
    aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatchOneWithLocationPretoria)
        .withDonation(donationAPos)
        .withCreatedOn(startDate)
        .buildAndPersist(entityManager);
    aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatchOneWithLocationPretoria)
        .withDonation(donationAPos)
        .withCreatedOn(startDate)
        .buildAndPersist(entityManager);
    
    //group 2 -> Pretoria, componentType1, B- (count 1)
    aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatchOneWithLocationPretoria)
        .withDonation(donationBNeg)
        .withCreatedOn(startDate)
        .buildAndPersist(entityManager);

    //group 3 -> Pretoria, componentType2, B-  (count 1)
    aComponent()
        .withComponentType(componentType2)
        .withComponentBatch(componentBatchOneWithLocationPretoria)
        .withDonation(donationBNeg)
        .withCreatedOn(startDate)
        .buildAndPersist(entityManager);
    
    //group 4 -> CapeTown, componentType1, B- (count 2)
    aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatchTwoWithLocationCapeTown)
        .withDonation(donationBNeg)
        .withCreatedOn(endDate)
        .buildAndPersist(entityManager);
    aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatchTwoWithLocationCapeTown)
        .withDonation(donationBNeg)
        .withCreatedOn(endDate)
        .buildAndPersist(entityManager);
    
    //group 5 -> Zululand, componentType1, A+ (count 1)
    aComponent()
        .withComponentType(componentType1)
        .withComponentBatch(componentBatchThreewithLocationZululand)
        .withDonation(donationAPos)
        .withCreatedOn(endDate)
        .buildAndPersist(entityManager);
    
    List<ComponentProductionDTO> expectedComponentProductionDTO = Arrays.asList(
        aComponentProductionDTO() 
              .withComponentTypeName(componentType1.getComponentTypeName())
              .withBloodAbo(bloodAboA)
              .withBloodRh(bloodRhPos)
              .withProcessingSite(pretoriaProcessingSite)
              .withCount(2L)
            .build(), 
       aComponentProductionDTO() 
              .withComponentTypeName(componentType1.getComponentTypeName())
              .withBloodAbo(bloodAboB)
              .withBloodRh(bloodRhNeg)
              .withProcessingSite(pretoriaProcessingSite)
              .withCount(1L)
              .build(),
        aComponentProductionDTO() 
              .withComponentTypeName(componentType2.getComponentTypeName())
              .withBloodAbo(bloodAboB)
              .withBloodRh(bloodRhNeg)
              .withProcessingSite(pretoriaProcessingSite)
              .withCount(1L)
              .build(),
         aComponentProductionDTO() 
              .withComponentTypeName(componentType1.getComponentTypeName())
              .withBloodAbo(bloodAboB)
              .withBloodRh(bloodRhNeg)
              .withProcessingSite(capeTownProcessingSite)
              .withCount(2L)
              .build(),
         aComponentProductionDTO() 
              .withComponentTypeName(componentType1.getComponentTypeName())
              .withBloodAbo(bloodAboA)
              .withBloodRh(bloodRhPos)
              .withProcessingSite(zuluLandProcessingSite)
              .withCount(1L)
              .build()
      );
    
    List<ComponentProductionDTO> returnedDtos = componentRepository
        .findProducedComponentsByProcessingSite(
            null,startDate, endDate);
        
    assertThat(returnedDtos.size(), is(5));
    assertThat(returnedDtos.get(0), hasSameStateAsComponentProductionDTO(expectedComponentProductionDTO.get(0)));
    assertThat(returnedDtos.get(1), hasSameStateAsComponentProductionDTO(expectedComponentProductionDTO.get(1)));
    assertThat(returnedDtos.get(2), hasSameStateAsComponentProductionDTO(expectedComponentProductionDTO.get(2)));
    assertThat(returnedDtos.get(3), hasSameStateAsComponentProductionDTO(expectedComponentProductionDTO.get(3)));
    assertThat(returnedDtos.get(4), hasSameStateAsComponentProductionDTO(expectedComponentProductionDTO.get(4)));
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumberWithoutFlagCharaters_shouldReturnMatchingComponent() {
    
    String donationIdentificationNumber = "0000001";
    
    // Expected
     List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(aDonation()
                .withDonationIdentificationNumber(donationIdentificationNumber).build())
            .buildAndPersist(entityManager)
    );
     
    // Excluded by donation identification number
    aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber("0000002").build())
        .buildAndPersist(entityManager);
    
    // Test
    List<Component> returnedComponent = componentRepository.findComponentsByDonationIdentificationNumber(donationIdentificationNumber);
    
    // Verify
    assertThat(returnedComponent.size(), is(expectedComponents.size()));
    assertThat(returnedComponent.get(0), hasSameStateAsComponent(expectedComponents.get(0)));
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumberWihFlagCharaters_shouldReturnMatchingComponent() {
    
    String dinWithflagCharacters = "000000129";
    
    // Expected
     List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(aDonation()
                .withDonationIdentificationNumber("0000001").withFlagCharacters("29").build())        
            .buildAndPersist(entityManager)
    );
     
    // Excluded by donation identification number
    aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber("0000002").withFlagCharacters("36").build())
        .buildAndPersist(entityManager);
    
    // Test
    List<Component> returnedComponents = componentRepository.findComponentsByDonationIdentificationNumber(dinWithflagCharacters);
    
    // Verify
    assertThat(returnedComponents.size(), is(expectedComponents.size()));
    assertThat(returnedComponents.get(0), hasSameStateAsComponent(expectedComponents.get(0)));
  }
  
  @Test
  public void testFindComponentByCodeAndDINWithoutFlagCharaters_shouldReturnMatchingComponent() {
    
    String componentCode = "0011-01";
    String donationIdentificationNumber = "0000001";
   
    // Expected
    Component expectedComponent = aComponent()
        .withComponentCode(componentCode)
        .withDonation(aDonation()
            .withDonationIdentificationNumber(donationIdentificationNumber)
            .build())
        .buildAndPersist(entityManager);
    
    // Test
    Component returnedComponent = componentRepository.findComponentByCodeAndDIN(componentCode, donationIdentificationNumber);;
    
    // Verify
    assertThat(returnedComponent, hasSameStateAsComponent(expectedComponent));
  }
  
  @Test
  public void testFindComponentsByDonationIdentificationNumberAndStatusWithFlagCharaters_shouldReturnCorrectComponents() {
    // Set up fixture
    String dinWithFlagCharacters = "12345671B";
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.DISCARDED)
            .withDonation(aDonation()
                .withDonationIdentificationNumber("1234567")
                .withFlagCharacters("1B")
                .build())
            .buildAndPersist(entityManager)
    );
    
    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findComponentsByDonationIdentificationNumberAndStatus(
        dinWithFlagCharacters, ComponentStatus.DISCARDED);
    
    // Verify
    assertThat(returnedComponents.size(), is(expectedComponents.size()));
    assertThat(returnedComponents.get(0), hasSameStateAsComponent(expectedComponents.get(0)));
  }
    
  @Test
  public void testFindComponentsByDINAndTypeWithoutFlagCharacters_shouldReturnMatchingComponent() {
    
    String donationIdentificationNumber = "0000001";
       
    ComponentType componentType = aComponentType().withComponentTypeName("Name1").buildAndPersist(entityManager);

    // Expected
    List<Component> expectedComponent = Arrays.asList(
        aComponent()
            .withComponentType(componentType)
            .withDonation(aDonation()
                .withDonationIdentificationNumber(donationIdentificationNumber).build())
            .buildAndPersist(entityManager)
      );
    
    // Test
    List<Component> returnedComponents = componentRepository.findComponentsByDINAndType(donationIdentificationNumber, 
                                                                                            componentType.getId());
    // Verify
    assertThat(returnedComponents.size(), is(expectedComponent.size()));
    assertThat(returnedComponents.get(0), hasSameStateAsComponent(expectedComponent.get(0)));
  }
  
  @Test
  public void testFindComponentsByDINAndTypeWithFlagCharacters_shouldReturnMatchingComponent() {
   
    String dinWithFlagCharacters = "00000022B";
      
    ComponentType componentType = aComponentType().withComponentTypeName("Name1").buildAndPersist(entityManager);
    
    // Expected
    List<Component> expectedComponent = Arrays.asList(
        aComponent()
            .withComponentType(componentType)
            .withDonation(aDonation()
                .withDonationIdentificationNumber("0000002").withFlagCharacters("2B").build())
            .buildAndPersist(entityManager)
      );

    // Test
    List<Component> returnedComponents = componentRepository.findComponentsByDINAndType(dinWithFlagCharacters, 
                                                                                            componentType.getId());
    // Verify
    assertThat(returnedComponents.size(), is(expectedComponent.size()));
    assertThat(returnedComponents.get(0), hasSameStateAsComponent(expectedComponent.get(0)));
  }

  @Test
  public void testFindSafeComponentsWithPositiveAndNegativeRhLookUp_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("+").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(availableStatus)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withParentComponent(initialComponent)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .buildAndPersist(entityManager);
    
    // Excluded by being an initialComponent
    aComponent()
        .withParentComponent(null)
        .withStatus(availableStatus)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)   
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .buildAndPersist(entityManager);

    // Excluded by status
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(ComponentStatus.EXPIRED)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by createdOn date
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new DateTime().minusDays(30).toDate())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by Location
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(aLocation().build())
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by ComponentType
    aComponent()
        .withDonation(donation)
        .withComponentType(aComponentType().build())
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by InventotyStatus
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded for blood group
    aComponent()
        .withDonation(aDonation().withBloodAbo("B").withBloodRh("-").build())
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    List<BloodGroup> bloodGroups = Arrays.asList(new BloodGroup("O", "+"), new BloodGroup("AB", "-"));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(componentType.getId(), location.getId(),
        bloodGroups, startDate, endDate, Arrays.asList(inventoryStatus), false);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindSafeComponentsWithPositiveRhLookUp_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("+").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(availableStatus)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withParentComponent(initialComponent)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .buildAndPersist(entityManager);
    
    // Excluded by being an initialComponent
    aComponent()
        .withParentComponent(null)
        .withStatus(availableStatus)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)   
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .buildAndPersist(entityManager);

    // Excluded by status
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(ComponentStatus.EXPIRED)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by createdOn date
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new DateTime().minusDays(30).toDate())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by Location
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(aLocation().build())
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by ComponentType
    aComponent()
        .withDonation(donation)
        .withComponentType(aComponentType().build())
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by InventotyStatus
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded for blood group
    aComponent()
        .withDonation(aDonation().withBloodAbo("B").withBloodRh("-").build())
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    List<BloodGroup> bloodGroups = Arrays.asList(new BloodGroup("O", "+"), new BloodGroup("A", "+"));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(componentType.getId(), location.getId(),
        bloodGroups, startDate, endDate, Arrays.asList(inventoryStatus), false);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }
  
  @Test
  public void testFindSafeComponentsWithNegativeRhLookUp_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("-").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(availableStatus)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)
        .withParentComponent(initialComponent)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .buildAndPersist(entityManager);
    
    // Excluded by being an initialComponent
    aComponent()
        .withParentComponent(null)
        .withStatus(availableStatus)
        .withDonation(donation)
        .withLocation(location)
        .withComponentType(componentType)   
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .buildAndPersist(entityManager);

    // Excluded by status
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(ComponentStatus.EXPIRED)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by createdOn date
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new DateTime().minusDays(30).toDate())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by Location
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(aLocation().build())
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by ComponentType
    aComponent()
        .withDonation(donation)
        .withComponentType(aComponentType().build())
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded by InventotyStatus
    aComponent()
        .withDonation(donation)
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    // Excluded for blood group
    aComponent()
        .withDonation(aDonation().withBloodAbo("B").withBloodRh("-").build())
        .withComponentType(componentType)
        .withLocation(location)
        .withInventoryStatus(inventoryStatus)
        .withStatus(availableStatus)
        .withCreatedOn(new Date())
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);

    List<BloodGroup> bloodGroups = Arrays.asList(new BloodGroup("AB", "-"),new BloodGroup("O", "-"));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(componentType.getId(), location.getId(),
        bloodGroups, startDate, endDate, Arrays.asList(inventoryStatus), false);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }  

  @Test
  public void testFindSafeComponentsWithNullProcessingSite_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date())
        .withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("+").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType).withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus).withStatus(availableStatus).withCreatedOn(new Date())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType).withLocation(aLocation().build()).withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus).withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    List<BloodGroup> bloodGroups = Arrays.asList(new BloodGroup("O", "+"), new BloodGroup("AB", "-"));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(componentType.getId(), null,
        bloodGroups, startDate, endDate, Arrays.asList(inventoryStatus), true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindSafeComponentsWithNullComponentType_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("+").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(donation)
            .withComponentType(aComponentType().build())
            .withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    List<BloodGroup> bloodGroups = Arrays.asList(new BloodGroup("O", "+"), new BloodGroup("AB", "-"));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(null, location.getId(), bloodGroups,
        startDate, endDate, Arrays.asList(inventoryStatus), true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }


  @Test
  public void testFindSafeComponentsWithNullBloodGroops_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Date startDate = new DateTime().minusDays(7).toDate();
    Date endDate = new DateTime().plusDays(2).toDate();
    Donation donation = aDonation().withDonationDate(new Date())
        .withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("+").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(aDonation().withBloodAbo("B").withBloodRh("-").build()).withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus).withStatus(availableStatus).withCreatedOn(new Date())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType).withLocation(location).withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus).withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(componentType.getId(), location.getId(),
        null, startDate, endDate, Arrays.asList(inventoryStatus), true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindSafeComponentsWithNoDateRange_shouldReturnCorrectRecords() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    Donation donation = aDonation().withDonationDate(new Date()).withDonationIdentificationNumber(donationIdentificationNumber).withBloodAbo("O").withBloodRh("+").build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    Location location = aLocation().build();
    ComponentType componentType = aComponentType().withComponentTypeCode("test").buildAndPersist(entityManager);
    InventoryStatus inventoryStatus = InventoryStatus.IN_STOCK;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withParentComponent(initialComponent)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new DateTime().minusDays(50).toDate())
            .buildAndPersist(entityManager),
        aComponent()
            .withDonation(donation)
            .withComponentType(componentType)
            .withLocation(location)
            .withInventoryStatus(inventoryStatus)
            .withStatus(availableStatus)
            .withCreatedOn(new Date())
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager)
    );

    List<BloodGroup> bloodGroups = Arrays.asList(new BloodGroup("O", "+"), new BloodGroup("AB", "-"));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(componentType.getId(), location.getId(),
        bloodGroups, null, null, Arrays.asList(inventoryStatus), true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindSafeComponentsWithTwo_shouldReturnCorrectRecords() {
    // Set up fixture
    InventoryStatus inventoryStatus1 = InventoryStatus.NOT_IN_STOCK;
    InventoryStatus inventoryStatus2 = InventoryStatus.REMOVED;
    ComponentStatus availableStatus = ComponentStatus.AVAILABLE;
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withInventoryStatus(inventoryStatus1)
            .withStatus(availableStatus)
            .buildAndPersist(entityManager),
        aComponent()
            .withInventoryStatus(inventoryStatus2)
            .withStatus(availableStatus)
            .buildAndPersist(entityManager)
    );

    // Excluded by InventoryStatus
    aComponent()
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(availableStatus)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findSafeComponents(null, null, null, null, null, 
        Arrays.asList(inventoryStatus1, inventoryStatus2), true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  } 

  @Test
  public void testFindComponentsByDINAndComponentCodeAndStatus_shouldReturnCorrectComponents() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    String componentCode = "1234";
    Donation donation = aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withComponentCode(componentCode)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withComponentCode(componentCode)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager));

    // Excluded by being an initial component
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withParentComponent(null)
        .withComponentCode(componentCode)
        .buildAndPersist(entityManager);
    // Excluded by componentCode
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .withComponentCode("2222")
        .buildAndPersist(entityManager);
    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withComponentCode(componentCode)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by status
    aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(donation)
        .withComponentCode(componentCode)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by donation
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(aDonation().build())
        .withComponentCode(componentCode)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findComponentsByDINAndComponentCodeAndStatus(
        donationIdentificationNumber, componentCode, ComponentStatus.AVAILABLE, false);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindComponentsByDINWithFlagCharatersAndComponentCodeAndStatus_shouldReturnCorrectComponents() {
    // Set up fixture
    String dinWithFlagCharacters = "12345671B";
    String componentCode = "1234";
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
        .withStatus(ComponentStatus.DISCARDED)
        .withDonation(aDonation()
            .withDonationIdentificationNumber("1234567")
            .withFlagCharacters("1B")
            .build())
        .withComponentCode(componentCode).buildAndPersist(entityManager));

    // Exercise SUT
    List<Component> returnedComponents = componentRepository
        .findComponentsByDINAndComponentCodeAndStatus(dinWithFlagCharacters, componentCode, ComponentStatus.DISCARDED, true);

    // Verify
    assertThat(returnedComponents.size(), is(expectedComponents.size()));
    assertThat(returnedComponents.get(0), hasSameStateAsComponent(expectedComponents.get(0)));
  }

  @Test
  public void testFindComponentsByDINAndComponentCodeAndStatusWithComponentCodeNull_shouldReturnCorrectComponents() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    String componentCode = "1234";
    Donation donation = aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withComponentCode(componentCode)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withComponentCode(componentCode)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .withComponentCode("2222")
            .buildAndPersist(entityManager)
    );
    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withComponentCode(componentCode)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by status
    aComponent()
        .withStatus(ComponentStatus.EXPIRED)
        .withDonation(donation)
        .withComponentCode(componentCode)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by donation
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(aDonation().build())
        .withComponentCode(componentCode)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Component> returnedComponents = componentRepository.findComponentsByDINAndComponentCodeAndStatus(
        donationIdentificationNumber, null, ComponentStatus.AVAILABLE, true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindComponentsByDINAndComponentCodeAndStatusWithStatusNull_shouldReturnCorrectComponents() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    String componentCode = "1234";
    Donation donation = aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        aComponent()
            .withStatus(ComponentStatus.PROCESSED)
            .withDonation(donation)
            .withComponentCode(componentCode)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withComponentCode(componentCode)
            .withParentComponent(initialComponent)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.EXPIRED)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .withComponentCode(componentCode)
            .buildAndPersist(entityManager)
    );
    // Excluded by componentCode
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withParentComponent(initialComponent)
        .withComponentCode("2222")
        .buildAndPersist(entityManager);
    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withComponentCode(componentCode)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by donation
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(aDonation().build())
        .withComponentCode(componentCode)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Component> returnedComponents = componentRepository
        .findComponentsByDINAndComponentCodeAndStatus(donationIdentificationNumber, componentCode, null, true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

  @Test
  public void testFindComponentsByDINAndComponentCodeAndStatusWithCodeAndStatusNull_shouldReturnCorrectComponents() {
    // Set up fixture
    String donationIdentificationNumber = "2255448";
    String componentCode = "1234";
    Donation donation = aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build();
    Component initialComponent = aComponent().withDonation(donation).buildAndPersist(entityManager);
    List<Component> expectedComponents = Arrays.asList(
        initialComponent, // the initial component matches the query as well because both the status
                          // and component code is null
        aComponent().withStatus(ComponentStatus.PROCESSED).withDonation(donation).withComponentCode(componentCode)
            .withParentComponent(initialComponent).buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withComponentCode(componentCode).withParentComponent(initialComponent).buildAndPersist(entityManager),
        aComponent().withStatus(ComponentStatus.EXPIRED).withDonation(donation)
            .withParentComponent(initialComponent)
            .withComponentCode(componentCode)
            .buildAndPersist(entityManager),
        aComponent()
            .withStatus(ComponentStatus.AVAILABLE)
            .withDonation(donation)
            .withParentComponent(initialComponent)
            .withComponentCode("2222")
            .buildAndPersist(entityManager)
    );
    // Excluded by isDeleted
    aComponent()
        .withIsDeleted(true)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation)
        .withComponentCode(componentCode)
        .withParentComponent(initialComponent)
        .buildAndPersist(entityManager);
    // Excluded by donation
    aComponent()
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(aDonation().build())
        .withComponentCode(componentCode)
        .buildAndPersist(entityManager);

    // Exercise SUT
    List<Component> returnedComponents = componentRepository
        .findComponentsByDINAndComponentCodeAndStatus(donationIdentificationNumber, null, null, true);

    // Verify
    assertThat(returnedComponents, is(expectedComponents));
  }

}
