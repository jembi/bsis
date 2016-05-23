package service;

import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static helpers.builders.UserBuilder.aUser;
import static helpers.matchers.OrderFormItemMatcher.hasSameStateAsOrderFormItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import model.order.OrderFormItem;
import repository.OrderFormItemRepository;
import suites.UnitTestSuite;

public class OrderFormItemCRUDServiceTests extends UnitTestSuite {
  
  private static final long ORDER_FORM_ITEM_ID = 363;
  
  @InjectMocks
  private OrderFormItemCRUDService orderFormItemCRUDService;
  
  @Mock
  private OrderFormItemRepository orderFormItemRepository;

  @Test
  public void testCreateOrUpdateOrderFormItemWithoutId_shouldCreateOrderFormItem() {
    // Set up fixture
    OrderFormItem orderFormItem = anOrderItemForm().build();
    
    // Test method
    OrderFormItem returnedOrderFormItem = orderFormItemCRUDService.createOrUpdateOrderFormItem(orderFormItem);
    
    // Verify
    verify(orderFormItemRepository).save(orderFormItem);
    assertThat(returnedOrderFormItem, hasSameStateAsOrderFormItem(orderFormItem));
  }

  @Test
  public void testCreateOrUpdateOrderFormItemWithId_shouldUpdateOrderFormItem() {
    // Set up fixture
    OrderFormItem orderFormItem = anOrderItemForm().withId(ORDER_FORM_ITEM_ID).build();
    OrderFormItem existingOrderFormItem = anOrderItemForm().withId(ORDER_FORM_ITEM_ID).build();
    OrderFormItem updatedOrderFormItem = anOrderItemForm()
        .withId(ORDER_FORM_ITEM_ID)
        .withLastUpdated(new Date())
        .withLastUpdatedBy(aUser().build())
        .build();
    
    // Set up expectations
    when(orderFormItemRepository.findById(ORDER_FORM_ITEM_ID)).thenReturn(existingOrderFormItem);
    when(orderFormItemRepository.update(existingOrderFormItem)).thenReturn(updatedOrderFormItem);
    
    // Test method
    OrderFormItem returnedOrderFormItem = orderFormItemCRUDService.createOrUpdateOrderFormItem(orderFormItem);
    
    // Verify
    assertThat(returnedOrderFormItem, hasSameStateAsOrderFormItem(updatedOrderFormItem));
  }
}
