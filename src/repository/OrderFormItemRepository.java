package repository;

import org.springframework.stereotype.Repository;

import model.order.OrderFormItem;

@Repository
public class OrderFormItemRepository extends AbstractRepository<OrderFormItem> {
  
  public OrderFormItem findById(long id) {
    return entityManager.find(OrderFormItem.class, id);
  }
}
