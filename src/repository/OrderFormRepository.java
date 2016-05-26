package repository;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import model.location.Location;
import model.order.OrderForm;
import model.order.OrderStatus;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(Long id) {
    TypedQuery<OrderForm> query = entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_ID, OrderForm.class);
    query.setParameter("id", id).setParameter("isDeleted", false);

    return query.getSingleResult();
  }

  public List<OrderForm> findByStatus(OrderStatus status) {
    TypedQuery<OrderForm> query = entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_STATUS, OrderForm.class);
    query.setParameter("status", status).setParameter("isDeleted", false);
    return query.getResultList();
  }

  public List<OrderForm> findOrderForms(Date orderDateFrom, Date orderDateTo, Location dispatchedFrom,
      Location dispatchedTo) {
    String queryString = "SELECT o FROM OrderForm o WHERE o.isDeleted=:isDeleted ";
    if (orderDateFrom != null) {
      queryString = queryString + "AND o.orderDate >= :orderDateFrom ";
    }
    if (orderDateTo != null) {
      queryString = queryString + "AND o.orderDate <= :orderDateTo ";
    }
    if (dispatchedFrom != null) {
      queryString = queryString + "AND o.dispatchedFrom = :dispatchedFrom ";
    }
    if (dispatchedTo != null) {
      queryString = queryString + "AND o.dispatchedTo = :dispatchedTo ";
    }

    queryString = queryString + "ORDER BY o.orderDate DESC";

    TypedQuery<OrderForm> query = entityManager.createQuery(queryString, OrderForm.class);
    query.setParameter("isDeleted", false);

    if (orderDateFrom != null) {
      query.setParameter("orderDateFrom", orderDateFrom);
    }
    if (orderDateTo != null) {
      query.setParameter("orderDateTo", orderDateTo);
    }
    if (dispatchedFrom != null) {
      query.setParameter("dispatchedFrom", dispatchedFrom);
    }
    if (dispatchedTo != null) {
      query.setParameter("dispatchedTo", dispatchedTo);
    }

    return query.getResultList();
  }

}
