package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.OrderFormItemBuilder.anOrderItemForm;

import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.repository.OrderFormItemRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
