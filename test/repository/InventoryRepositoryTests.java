package repository;

import static helpers.builders.ComponentBuilder.aComponent;
import static helpers.builders.DonationBuilder.aDonation;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dto.StockLevelDTO;
import helpers.builders.ComponentTypeBuilder;
import helpers.builders.LocationBuilder;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.inventory.InventoryStatus;
import model.location.Location;
import suites.ContextDependentTestSuite;

public class InventoryRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private InventoryRepository inventoryRepository;

  @Test
  public void testFindStockLevelsForLocation_verifyInStockResults() {
    
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
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location, InventoryStatus.IN_STOCK);
    
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
  public void testFindStockLevelsForLocation_verifyNotLabelledResults() {
    
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
    
    
    List<StockLevelDTO> levels = inventoryRepository.findStockLevelsForLocation(location, InventoryStatus.NOT_LABELLED);
    
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

}
