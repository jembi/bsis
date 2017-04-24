package org.jembi.bsis.service;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.repository.InventoryRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InventoryCRUDServiceTests extends UnitTestSuite {

  private static final String IRRELEVANT_DONATION_DIN = "1234567";
  private static final String IRRELEVANT_COMPONENT_CODE = "0011";

  @InjectMocks
  private InventoryCRUDService inventoryCRUDService;
  @Mock
  private InventoryRepository inventoryRepository;

  @Test
  public void findComponentsInStockWithCodeAndDINParams_callsRightMethod() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    inventoryCRUDService.findComponentsInStock(IRRELEVANT_DONATION_DIN, IRRELEVANT_COMPONENT_CODE, locationId, componentTypeId,
        new Date(),
        bloodGroups);
    verify(inventoryRepository).findComponentByCodeAndDINInStock(IRRELEVANT_COMPONENT_CODE, IRRELEVANT_DONATION_DIN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithCodeAndNoDINParams_throws() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    inventoryCRUDService.findComponentsInStock(null, IRRELEVANT_COMPONENT_CODE, locationId, componentTypeId, new Date(),
        bloodGroups);
  }


  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithDINAndNoCodeParams_throws() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    inventoryCRUDService.findComponentsInStock(IRRELEVANT_DONATION_DIN, null, locationId, componentTypeId, new Date(), bloodGroups);
  }

  @Test
  public void findComponentsInStockWithNoCodeAndNoDINParams_callsRightMethod() {
    Date dueToExpireBy = new Date();
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    List<BloodGroup> bloodGroupObjs = new ArrayList<>();
    bloodGroupObjs.add(new BloodGroup("A+"));
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    inventoryCRUDService.findComponentsInStock(null, null, locationId, componentTypeId, dueToExpireBy, bloodGroups);
    verify(inventoryRepository).findComponentsInStock(locationId, componentTypeId, dueToExpireBy, bloodGroupObjs);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithNoCodeAndNoDINParamsAndWrongBloodGroup_throws() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("WrongBloodGroup");
    UUID locationId = UUID.randomUUID();
    UUID componentTypeId = UUID.randomUUID();
    inventoryCRUDService.findComponentsInStock(null, null, locationId, componentTypeId, null, bloodGroups);
  }

  @Test
  public void findComponentsInStockWithNoParams_callsRightMethod() {
    inventoryCRUDService.findComponentsInStock(null, null, null, null, null, null);
    verify(inventoryRepository).findComponentsInStock(null, null, null, null);
  }

}
