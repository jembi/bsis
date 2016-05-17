package repository;

import org.springframework.stereotype.Repository;

import model.order.OrderForm;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(Long id) {
    return entityManager.find(OrderForm.class, id);
  }

}
