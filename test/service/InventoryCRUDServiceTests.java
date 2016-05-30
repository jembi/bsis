package service;

import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    inventoryCRUDService.findComponentsInStock(IRRELEVANT_DONATION_DIN, IRRELEVANT_COMPONENT_CODE, 1L, 1L, new Date(), "A+");
    verify(inventoryRepository).findComponentByCodeAndDINInStock(IRRELEVANT_COMPONENT_CODE, IRRELEVANT_DONATION_DIN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithCodeAndNoDINParams_throws() {
    inventoryCRUDService.findComponentsInStock(null, IRRELEVANT_COMPONENT_CODE, 1L, 1L, new Date(), "A+");
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithDINAndNoCodeParams_throws() {
    inventoryCRUDService.findComponentsInStock(IRRELEVANT_DONATION_DIN, null, 1L, 1L, new Date(), "A+");
  }

  @Test
  public void findComponentsInStockWithNoCodeAndNoDINParams_callsRightMethod() {
    Date dueToExpireBy = new Date();
    inventoryCRUDService.findComponentsInStock(null, null, 1L, 1L, dueToExpireBy, "A+");
    verify(inventoryRepository).findComponentsInStock(1L, 1l, dueToExpireBy, "A", "+");
  }

  @Test(expected = IllegalArgumentException.class)
  public void findComponentsInStockWithNoCodeAndNoDINParamsAndWrongBloodGroup_throws() {
    Date dueToExpireBy = new Date();
    inventoryCRUDService.findComponentsInStock(null, null, 1L, 1L, dueToExpireBy, "WrongBloodGroup");
  }

}
