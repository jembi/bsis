package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.LocationBuilder.aDistributionSite;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dto.StockLevelDTO;
import helpers.builders.ComponentTypeBuilder;
import helpers.builders.LocationBuilder;
import model.component.Component;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.inventory.InventoryStatus;
import model.location.Location;
import suites.ContextDependentTestSuite;

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

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withComponentType(type1)
        .withDonation(donation).withLocation(location1).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withComponentType(type2)
        .withDonation(donation).withLocation(location2).buildAndPersist(entityManager);
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location1.getId(), InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 1, levels.size());

    // Verify right component was returned
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());

  }

  @Test
  public void testFindStockLevels_verifyInStockResults() {
    
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    ComponentType type3 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type3").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withComponentType(type1)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withComponentType(type2)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withComponentType(type2)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.NOT_LABELLED).withComponentType(type3)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.IN_STOCK);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());

    // Verify count
    Assert.assertEquals("Verify count", 1, levels.get(0).getCount());
    Assert.assertEquals("Verify count", 2, levels.get(1).getCount());

    // Verify componentType
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());

    // Verify blood group
    Assert.assertEquals("Verify blood group", "A+", levels.get(0).getBloodAbo() + levels.get(0).getBloodRh());
    Assert.assertEquals("Verify blood group", "A+", levels.get(1).getBloodAbo() + levels.get(0).getBloodRh());

  }
  
  @Test
  public void testFindStockLevels_verifyNotLabelledResults() {
    
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("+").buildAndPersist(entityManager);
    ComponentType type1 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type1").buildAndPersist(entityManager);
    ComponentType type2 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type2").buildAndPersist(entityManager);
    ComponentType type3 = ComponentTypeBuilder.aComponentType().withComponentTypeName("type3").buildAndPersist(entityManager);
    Location location = LocationBuilder.aProcessingSite().withName("PSite").buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.NOT_LABELLED).withComponentType(type1)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.NOT_LABELLED).withComponentType(type2)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    aComponent().withInventoryStatus(InventoryStatus.NOT_LABELLED).withComponentType(type2)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);

    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withComponentType(type3)
        .withDonation(donation).withLocation(location).buildAndPersist(entityManager);
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevels(InventoryStatus.NOT_LABELLED);
    
    // Verify levels returned
    Assert.assertEquals("Verify levels returned", 2, levels.size());

    // Verify count
    Assert.assertEquals("Verify count", 1, levels.get(0).getCount());
    Assert.assertEquals("Verify count", 2, levels.get(1).getCount());

    // Verify componentType
    Assert.assertEquals("Verify componentType", type1, levels.get(0).getComponentType());
    Assert.assertEquals("Verify componentType", type2, levels.get(1).getComponentType());

    // Verify blood group
    Assert.assertEquals("Verify blood group", "A+", levels.get(0).getBloodAbo() + levels.get(0).getBloodRh());
    Assert.assertEquals("Verify blood group", "A+", levels.get(1).getBloodAbo() + levels.get(0).getBloodRh());

  }
  
  @Test
  public void testFindComponentByCodeDINAndInventoryStatus_shouldReturnMatchingComponent() {
    
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
    
    // Excluded by inventoryStatus = NOT_LABELLED
    aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .withInventoryStatus(InventoryStatus.NOT_LABELLED)
        .buildAndPersist(entityManager);
    
    // Excluded by inventoryStatus = REMOVED
    aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .withInventoryStatus(InventoryStatus.REMOVED)
        .buildAndPersist(entityManager);
    
    // Expected
    Component expectedComponent = aComponent()
        .withComponentCode(componentCode)
        .withDonation(donationWithExpectedDonationIdentificationNumber)
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .buildAndPersist(entityManager);
    
    // Test
    Component returnedComponent = inventoryRepository.findComponentByCodeDINAndInventoryStatus(componentCode,
        donationIdentificationNumber, InventoryStatus.IN_STOCK);
    
    // Verify
    assertThat(returnedComponent, is(expectedComponent));
  }

  @Test(expected = NoResultException.class)
  public void testFindNonExistentComponentByCodeDINAndInventoryStatus_shoulThrow() {
    // Test
    inventoryRepository.findComponentByCodeDINAndInventoryStatus("0011-01", "0000002", InventoryStatus.IN_STOCK);
  }

  @Test
  public void testFindComponentsInStockNoParams_shouldReturnAllComponentsInStock() {
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).buildAndPersist(entityManager);
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).buildAndPersist(entityManager);
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).buildAndPersist(entityManager);

    // Test
    List<Component> components = inventoryRepository.findComponentsInStock(null, null, null, null, null);

    // Verify
    Assert.assertEquals("Found 3 component", 3, components.size());
  }

  @Test
  public void testFindComponentsInStockWithParams_shouldReturnMatchingComponent() {
    // Set up
    Location loc = aDistributionSite().buildAndPersist(entityManager);
    ComponentType componentType = ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager);
    Donation donation = aDonation().withBloodAbo("A").withBloodRh("-").buildAndPersist(entityManager);
    Date expiresOn = new Date();
    Component expected = aComponent()
        .withInventoryStatus(InventoryStatus.IN_STOCK)
        .withLocation(loc)
        .withComponentType(componentType)
        .withDonation(donation)
        .withExpiresOn(expiresOn)
        .buildAndPersist(entityManager);
    
    // Excluded: not in stock
    aComponent()
    .withLocation(loc)  
    .withComponentType(componentType)
    .withDonation(donation)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different location
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withComponentType(componentType)
    .withDonation(donation)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different componentType
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withLocation(loc)
    .withDonation(donation)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different bloodGroup
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withLocation(loc)
    .withComponentType(componentType)
    .withExpiresOn(expiresOn)
    .buildAndPersist(entityManager);
    
    // Excluded: different expiresOn
    aComponent()
    .withInventoryStatus(InventoryStatus.IN_STOCK)
    .withLocation(loc)
    .withComponentType(componentType)
    .withDonation(donation)
    .buildAndPersist(entityManager);

    // Test
    List<Component> components = inventoryRepository.findComponentsInStock(loc.getId(), componentType.getId(), expiresOn, donation.getBloodAbo(), donation.getBloodRh());

    // Verify
    Assert.assertEquals("Found 1 component", 1, components.size());
    Assert.assertEquals(expected, components.get(0));
  }

}
