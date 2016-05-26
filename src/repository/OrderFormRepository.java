package repository;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import model.order.OrderForm;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(Long id) {
    String queryString = "SELECT d FROM OrderForm d WHERE d.id = :id";
    TypedQuery<OrderForm> query = entityManager.createQuery(queryString, OrderForm.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

}
