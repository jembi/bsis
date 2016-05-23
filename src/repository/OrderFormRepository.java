package repository;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import model.order.OrderForm;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(Long id) {
    OrderForm orderForm = entityManager.find(OrderForm.class, id);
    if (orderForm == null) {
      throw new NoResultException("OrderForm not found for id " + id);
    }
    return orderForm;
  }

}
