package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeBuilder.aComponentStatusChange;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;
import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aDiscardReason;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.DiscardedComponentDTOBuilder.aDiscardedComponentDTO;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.UserBuilder.aUser;
import static org.jembi.bsis.helpers.matchers.DiscardedComponentDTOMatcher.hasSameStateAsDiscardedComponentDTO;
import static org.jembi.bsis.helpers.matchers.SameDayMatcher.isSameDayAs;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.ComponentExportDTO;
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
        .withCanBeIssued(Boolean.FALSE)
        .buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType()
        .withCanBeIssued(Boolean.TRUE)
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
        .withCanBeIssued(Boolean.FALSE)
        .buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType()
        .withCanBeIssued(Boolean.TRUE)
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
        .withCanBeIssued(Boolean.FALSE)
        .buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType()
        .withCanBeIssued(Boolean.TRUE)
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
}
