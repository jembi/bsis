package org.jembi.bsis.repository;

import org.jembi.bsis.model.order.OrderFormItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderFormItemRepository extends AbstractRepository<OrderFormItem> {
  
  public OrderFormItem findById(long id) {
    return entityManager.find(OrderFormItem.class, id);
  }
}
