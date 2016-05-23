package repository;

import static helpers.builders.OrderFormItemBuilder.anOrderItemForm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import model.order.OrderFormItem;
import suites.ContextDependentTestSuite;

public class OrderFormItemRepositoryTests extends ContextDependentTestSuite {
 
  @Autowired
  private OrderFormItemRepository orderFormItemRepository;
  
  @Test
  public void testFindById_shouldReturnOrderFormItem() {
    OrderFormItem orderFormItem = anOrderItemForm().buildAndPersist(entityManager);
    
    OrderFormItem returnedOrderFormItem = orderFormItemRepository.findById(orderFormItem.getId());
    
    assertThat(returnedOrderFormItem, is(orderFormItem));
  }

}
