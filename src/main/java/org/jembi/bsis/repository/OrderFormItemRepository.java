package org.jembi.bsis.repository;

import java.util.UUID;

import org.jembi.bsis.model.order.OrderFormItem;
import org.springframework.stereotype.Repository;

@Repository
public class OrderFormItemRepository extends AbstractRepository<OrderFormItem> {
  
  public OrderFormItem findById(UUID id) {
    return entityManager.find(OrderFormItem.class, id);
  }
}
