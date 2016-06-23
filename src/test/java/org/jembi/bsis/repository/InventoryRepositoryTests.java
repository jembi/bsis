package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.StockLevelDTO;
import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.InventoryRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class InventoryRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private InventoryRepository inventoryRepository;
  
  @Test
  public void testFindStockForLocationLevels_verifyCorrectLocation() {
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location1 = LocationBuilder.aProcessingSite().withName("PSite1").buildAndPersist(entityManager);
    Location location2 = LocationBuilder.aProcessingSite().withName("PSite2").buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type1).withDonation(donation).withLocation(location1).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.EXPIRED)
        .withComponentType(type2).withDonation(donation).withLocation(location2).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location1.getId(), InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 1, levels.size());

    // Verify right component was returned
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
  }
  
  @Test
  public void testFindStockForLocationLevels_verifyNotDeletedResults() {
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite1").buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.EXPIRED)
        .withComponentType(type2).withDonation(donation).withLocation(location).withIsDeleted(true).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location.getId(), InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 1, levels.size());

    // Verify right component was returned
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
  }
  
  @Test
  public void testFindStockForLocationLevels_verifyCorrectBloodGroups() {
    Donation donation1 = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    Donation donation2 = aDonation().withBloodAbo("O").withBloodRh("-").buildAndPersist(entityManager);
    Donation donation3 = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite1").buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type1).withDonation(donation1).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type2).withDonation(donation2).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
    .withComponentType(type2).withDonation(donation3).withLocation(location).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location.getId(), InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 3, levels.size());

    // Verify right component was returned
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(2).getComponentType());
    
    // Verify right counts were returned
    Assert.assertEquals("Verify componentType count", 1, levels.get(0).getCount());
    Assert.assertEquals("Verify componentType count", 1, levels.get(1).getCount());
    Assert.assertEquals("Verify componentType count", 1, levels.get(2).getCount());
    
    // Verify blood group
    Assert.assertEquals("Verify blood group", "A+", levels.get(0).getBloodAbo() + levels.get(0).getBloodRh());
    Assert.assertEquals("Verify blood group", "A+", levels.get(1).getBloodAbo() + levels.get(1).getBloodRh());
    Assert.assertEquals("Verify blood group", "O-", levels.get(2).getBloodAbo() + levels.get(2).getBloodRh());
  }
  
  @Test
  public void testFindStockForLocationLevels_verifyCorrectInventoryStatus() {
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite1").buildAndPersist(entityManager);
    
    // components that match
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    // components that don't match due to Inventory Statuses
    aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location.getId(), InventoryStatus.NOT_IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 1, levels.size());

    // Verify right components were returned
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    
    // Verify right counts were returned
    Assert.assertEquals("Verify componentType count", 1, levels.get(0).getCount());
  }
  
  @Test
  public void testFindStockForLocationLevels_verifyCorrectComponentStatuses() {
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite1").buildAndPersist(entityManager);
    
    // components that match
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.QUARANTINED)
    .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    // components that don't match due to Component Status (note that USED and ISSUED belong to a Inventory Status.REMOVED)
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.PROCESSED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.SPLIT)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.UNSAFE)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.DISCARDED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location.getId(), InventoryStatus.NOT_IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());

    // Verify right components were returned
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());
    
    // Verify right count returned
    Assert.assertEquals("Verify componentType count", 2, levels.get(0).getCount());
    Assert.assertEquals("Verify componentType count", 1, levels.get(1).getCount());
  }

  @Test
  public void testFindStockLevels_verifyCorrectInventoryStatus() {
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    ComponentType type3 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type3").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    // Components that match
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    // Components that do not match due to Inventory Status
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type3).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type3).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());

    // Verify componentType
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());
    
    // Verify count
    Assert.assertEquals("Verify count", 1, levels.get(0).getCount());
    Assert.assertEquals("Verify count", 2, levels.get(1).getCount());
  }
  
  @Test
  public void testFindStockLevels_verifyCorrectBloodGroup() {
    Donation donation1 = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    Donation donation2 = aDonation().withBloodAbo("O").withBloodRh("-").buildAndPersist(entityManager);
    Donation donation3 = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type).withDonation(donation1).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type).withDonation(donation2).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type).withDonation(donation3).withLocation(location).buildAndPersist(entityManager);
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());

    // Verify componentType
    Assert.assertEquals("Verify componentType", type, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type, levels.get(1).getComponentType());
    
    // Verify count
    Assert.assertEquals("Verify count", 2, levels.get(0).getCount());
    Assert.assertEquals("Verify count", 1, levels.get(1).getCount());

    // Verify blood group
    Assert.assertEquals("Verify blood group", "A+", levels.get(0).getBloodAbo() + levels.get(0).getBloodRh());
    Assert.assertEquals("Verify blood group", "O-", levels.get(1).getBloodAbo() + levels.get(1).getBloodRh());
  }
  
  @Test
  public void testFindStockLevels_verifyNotDeletedResults() {
    
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    // Component that does match
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.EXPIRED)
        .withComponentType(type).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    // Component that does not match due to being deleted
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.EXPIRED).withIsDeleted(true)
        .withComponentType(type).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.NOT_IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 1, levels.size());

    // Verify count
    Assert.assertEquals("Verify count", 1, levels.get(0).getCount());
  }
  
  @Test
  public void testFindStockLevels_verifyNotLabelledCorrectComponentStatuses() {
    
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    // components that match
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.QUARANTINED)
        .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    // components that don't match due to Component Status (note that USED and ISSUED belong to a Inventory Status.REMOVED)
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.PROCESSED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.SPLIT)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.UNSAFE)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.DISCARDED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.NOT_IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());
    
    // Verify componentType
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());

    // Verify count
    Assert.assertEquals("Verify count", 1, levels.get(0).getCount());
    Assert.assertEquals("Verify count", 2, levels.get(1).getCount());
  }
  
  @Test
  public void testFindStockLevels_verifyInStockCorrectComponentStatuses() {
    
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    // components that match
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.QUARANTINED)
        .withComponentType(type1).withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE)
        .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.EXPIRED)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    // components that don't match - note IN_STOCK and UNSAFE can happen if a Donor self-diagnoses
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.UNSAFE)
    .withComponentType(type2).withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());
    
    // Verify componentType
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());

    // Verify count
    Assert.assertEquals("Verify count", 1, levels.get(0).getCount());
    Assert.assertEquals("Verify count", 2, levels.get(1).getCount());
  }
  
  @Test
  public void testFindComponentByCodeAndDINInStock_shouldReturnMatchingComponent() {
    
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
    
    // Excluded by inventoryStatus = NOT_IN_STOCK
    aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .withInventoryStatus(InventoryStatus.NOT_IN_STOCK)
        .withStatus(ComponentStatus.QUARANTINED)
        .buildAndPersist(entityManager);
    
    // Excluded by inventoryStatus = REMOVED
    aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .withStatus(ComponentStatus.ISSUED)
        .buildAndPersist(entityManager);
    
    // Expected
    Component expectedComponent = aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .buildAndPersist(entityManager);
    
    // Test
    Component returnedComponent = inventoryRepository.findComponentByCodeAndDINInStock(componentCode,
        donationIdentificationNumber);
    
    // Verify
    assertThat(returnedComponent, is(expectedComponent));
  }

  @Test(expected = NoResultException.class)
  public void testFindNonExistentComponentByCodeAndDINInStock_shoulThrow() {
    // Test
    inventoryRepository.findComponentByCodeAndDINInStock("0011-01", "0000002");
  }

  @Test
  public void testFindComponentsInStockNoParams_shouldReturnAllComponentsInStock() {
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE).buildAndPersist(entityManager);
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE).buildAndPersist(entityManager);
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE).buildAndPersist(entityManager);

    // Test
    List<Component> components = inventoryRepository.findComponentsInStock(null, null, null, null);

    // Verify
    Assert.assertEquals("Found 3 component", 3, components.size());
  }

  @Test
  public void testFindComponentsInStockWithParams_shouldReturnMatchingComponent() {
    // Set up
    Location loc = aDistributionSite().buildAndPersist(entityManager);
    ComponentType componentType = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    Date expiresOn = new Date();
    Component expected = aComponent()
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withLocation(loc)
        .withComponentType(componentType)
        .withDonation(donation)
        .withExpiresOn(expiresOn)
        .buildAndPersist(entityManager);
    
    // Excluded: not in stock
    aComponent()
    .withStatus(ComponentStatus.PROCESSED)
    .withLocation(loc)  
    .withComponentType(componentType)
    .withDonation(donation)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different location
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withStatus(ComponentStatus.AVAILABLE)
    .withComponentType(componentType)
    .withDonation(donation)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different componentType
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withStatus(ComponentStatus.AVAILABLE)
    .withLocation(loc)
    .withDonation(donation)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different bloodGroup
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withStatus(ComponentStatus.AVAILABLE)
    .withLocation(loc)
    .withComponentType(componentType)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different expiresOn
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withStatus(ComponentStatus.AVAILABLE)
    .withLocation(loc)
    .withComponentType(componentType)
    .withDonation(donation)
    .buildAndPersist(entityManager);

    // Test
    List<BloodGroup> bloodGroups = new ArrayList<>();
    bloodGroups.add(new BloodGroup("A+"));
    List<Component> components = inventoryRepository.findComponentsInStock(loc.getId(), componentType.getId(), expiresOn, bloodGroups);

    // Verify
    Assert.assertEquals("Found 1 component", 1, components.size());
    Assert.assertEquals(expected, components.get(0));
  }
  
  @Test
  public void testFindComponentsInStockWithManyBloodGroupsParam_shouldReturnMatchingComponents() {
    
    // Set up
    Donation donation1 = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    Donation donation2 = aDonation().withBloodAbo("A").withBloodRh("-").buildAndPersist(entityManager);
    Donation donation3 = aDonation().withBloodAbo("B").withBloodRh("+").buildAndPersist(entityManager);
    Component component1 = aComponent()
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation1)
        .buildAndPersist(entityManager);
    Component component2 = aComponent()
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation2)
        .buildAndPersist(entityManager);
    aComponent()
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withStatus(ComponentStatus.AVAILABLE)
        .withDonation(donation3)
        .buildAndPersist(entityManager);
    
    // Test
    List<BloodGroup> bloodGroups = new ArrayList<>();
    bloodGroups.add(new BloodGroup("A+"));
    bloodGroups.add(new BloodGroup("A-"));
    List<Component> components = inventoryRepository.findComponentsInStock(null, null, null, bloodGroups);

    // Verify
    Assert.assertEquals("Found 2 components", 2, components.size());
    Assert.assertEquals(component1, components.get(0));
    Assert.assertEquals(component2, components.get(1));
  }

}
