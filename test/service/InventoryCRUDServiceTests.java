package service;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import model.util.BloodGroup;
import repository.InventoryRepository;
import suites.UnitTestSuite;

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
    inventoryCRUDService.findComponentsInStock(IRRELEVANT_DONATION_DIN, IRRELEVANT_COMPONENT_CODE, 1L, 1L, new Date(),
        bloodGroups);
    verify(inventoryRepository).findComponentByCodeAndDINInStock(IRRELEVANT_COMPONENT_CODE, IRRELEVANT_DONATION_DIN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithCodeAndNoDINParams_throws() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    inventoryCRUDService.findComponentsInStock(null, IRRELEVANT_COMPONENT_CODE, 1L, 1L, new Date(), bloodGroups);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithDINAndNoCodeParams_throws() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    inventoryCRUDService.findComponentsInStock(IRRELEVANT_DONATION_DIN, null, 1L, 1L, new Date(), bloodGroups);
  }

  @Test
  public void findComponentsInStockWithNoCodeAndNoDINParams_callsRightMethod() {
    Date dueToExpireBy = new Date();
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("A+");
    List<BloodGroup> bloodGroupObjs = new ArrayList<>();
    bloodGroupObjs.add(new BloodGroup("A+"));
    inventoryCRUDService.findComponentsInStock(null, null, 1L, 1L, dueToExpireBy, bloodGroups);
    verify(inventoryRepository).findComponentsInStock(1L, 1L, dueToExpireBy, bloodGroupObjs);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithNoCodeAndNoDINParamsAndWrongBloodGroup_throws() {
    List<String> bloodGroups = new ArrayList<>();
    bloodGroups.add("WrongBloodGroup");
    inventoryCRUDService.findComponentsInStock(null, null, 1L, 1L, null, bloodGroups);
  }

  @Test
  public void findComponentsInStockWithNoParams_callsRightMethod() {
    inventoryCRUDService.findComponentsInStock(null, null, null, null, null, null);
    verify(inventoryRepository).findComponentsInStock(null, null, null, null);
  }

}
