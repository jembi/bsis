package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.CollectedDonationDTOBuilder.aCollectedDonationDTO;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationTypeBuilder.aDonationType;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;
import static org.jembi.bsis.helpers.builders.ComponentProductionDTOBuilder.aComponentProductionDTO;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.matchers.ComponentProductionDTOMatcher.hasSameStateAsComponentProductionDTO;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.CollectedDonationDTO;
import org.jembi.bsis.dto.ComponentExportDTO;
import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.helpers.builders.ComponentBatchBuilder;
import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;
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
    Assert.assertFalse("Component does not exist", componentRepository.verifyComponentExists(1L));
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
    
    // Expected
    Component component = aComponent()
        .withDonation(aDonation().withDonationIdentificationNumber(donationIdentificationNumber).build())
        .withComponentCode(componentCode)
        .withCreatedBy(aUser().withUsername(createdByUsername).build())
        .withCreatedDate(createdDate)
        .withParentComponent(aComponent()
            .withCreatedDate(new DateTime(createdDate).minusDays(1).toDate()) // Create parent before child
            .withComponentCode(parentComponentCode)
            .build())
        .withCreatedOn(createdOn)
        .withStatus(status)
        .withLocation(aVenue().withName(locationName).build())
        .withIssuedOn(issuedOn)
        .withInventoryStatus(inventoryStatus)
        .withDiscardedOn(discardedOn)
        .withExpiresOn(expiresOn)
        .withNotes(notes)
        .buildAndPersist(entityManager);

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
    aComponent().thatIsDeleted().buildAndPersist(entityManager);
    
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
  public void testFindProducedComponentsByProcessingSite_shouldReturnCorrectField() {
    Date donationStartDate = new DateTime().minusDays(7).toDate();
    Date donationEndDate = new DateTime().minusDays(2).toDate();
    
    Location expectedVenue = aProcessingSite().withName("venue").buildAndPersist(entityManager);
    String expectedBloodAbo = "A";
    String expectedBloodRh = "+";
   
    ComponentType componentType = aComponentType()
        .withComponentTypeName("type1")
        .withCanBeIssued(true)
        .buildAndPersist(entityManager);
    
    ComponentBatch componentBatch = aComponentBatch()
        .withLocation(expectedVenue)
        .buildAndPersist(entityManager);
     
    Donation donation = aDonation()
        .withDonationDate(donationStartDate)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
    
    
    aDonation()
      .withDonationDate(new Date())
      .withBloodAbo(expectedBloodAbo)
      .withBloodRh(expectedBloodRh)
      .thatIsDeleted()
      .buildAndPersist(entityManager);
    
    //Excluded donation with out of range date
   Donation outOfDateRangeDonation = aDonation()
    .withDonationDate(new Date())
    .withBloodAbo(expectedBloodAbo)
    .withBloodRh(expectedBloodRh)
    .buildAndPersist(entityManager);
    
// Excluded by being deleted
   Donation deletedDonation = aDonation()
       .withDonationDate(new Date())
       .withBloodAbo(expectedBloodAbo)
       .withBloodRh(expectedBloodRh)
       .thatIsDeleted()
       .buildAndPersist(entityManager);
   
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatch)
        .withDonation(donation)
        .buildAndPersist(entityManager);
    
   // Excluded component by deleted donation
   aComponent()
       .withComponentType(componentType)
       .withComponentBatch(componentBatch)
       .withDonation(deletedDonation)
       .buildAndPersist(entityManager);
  
   // Excluded component by out of range date 
   aComponent()
       .withComponentType(componentType)
       .withComponentBatch(componentBatch)
       .withDonation(outOfDateRangeDonation)
       .buildAndPersist(entityManager);
    
    ComponentProductionDTO expectedDTO =
        aComponentProductionDTO()
          .withComponentTypeName(componentType.getComponentTypeName())
          .withBloodAbo(expectedBloodAbo)
          .withBloodRh(expectedBloodRh)
          .withVenue(expectedVenue)
          .withCount(1)
          .build(); 

    List<ComponentProductionDTO> returnedDtos = componentRepository
        .findProducedComponentsByProcessingSite(
            expectedVenue.getId(), donationStartDate, donationEndDate);
     
    assertThat(returnedDtos.size(), is(1));
    assertThat(returnedDtos.get(0),hasSameStateAsComponentProductionDTO(expectedDTO));
  }
 
  @Test
  public void testFindProducedComponentsByProcessingSite_shouldReturnAllSites() {
    Date donationStartDate = new DateTime().minusDays(7).toDate();
    Date donationEndDate = new DateTime().minusDays(2).toDate();
    
    Location firstProcessingSite = aProcessingSite().withName("Pretoria").buildAndPersist(entityManager);
    Location secondProcessingSite = aProcessingSite().withName("Cape Town").buildAndPersist(entityManager);
    Location thirdProcessingSite = aProcessingSite().withName("Zulu land").buildAndPersist(entityManager);
    
    String expectedBloodAbo = "A";
    String expectedBloodRh = "+";
   
    ComponentType componentType = aComponentType()
        .withComponentTypeName("type1")
        .withCanBeIssued(true)
        .buildAndPersist(entityManager);
    
    ComponentBatch componentBatchOne = aComponentBatch()
        .withLocation(secondProcessingSite)
        .buildAndPersist(entityManager);
    
    ComponentBatch componentBatchTwo = aComponentBatch()
       .withLocation(firstProcessingSite)
       .buildAndPersist(entityManager);
    
    ComponentBatch componentBatchThree = aComponentBatch()
        .withLocation(thirdProcessingSite)
        .buildAndPersist(entityManager);
   
    Donation donation = aDonation()
        .withDonationDate(donationStartDate)
        .withBloodAbo(expectedBloodAbo)
        .withBloodRh(expectedBloodRh)
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);
  
    aComponent()
        .withComponentType(componentType)
        .withComponentBatch(componentBatchOne)
        .withDonation(donation)
        .buildAndPersist(entityManager);
    
    aComponent()
      .withComponentType(componentType)
      .withComponentBatch(componentBatchTwo)
      .withDonation(donation)
      .buildAndPersist(entityManager);
    
    aComponent()
      .withComponentType(componentType)
      .withComponentBatch(componentBatchThree)
      .withDonation(donation)
      .buildAndPersist(entityManager);

    List<ComponentProductionDTO> returnedDtos = componentRepository
        .findProducedComponentsByProcessingSite(
            null,donationStartDate, donationEndDate);
        
    assertThat(returnedDtos.size(), is(3));
    Iterator<ComponentProductionDTO> iterator = returnedDtos.iterator();
    assertThat(iterator.next().getVenue(), is(firstProcessingSite));
    assertThat(iterator.next().getVenue(), is(secondProcessingSite));
    assertThat(iterator.next().getVenue(), is(thirdProcessingSite));
    
  }
}
