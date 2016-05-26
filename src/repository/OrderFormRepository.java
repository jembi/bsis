package repository;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import model.order.OrderForm;
import model.order.OrderStatus;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(Long id) {
    TypedQuery<OrderForm> query = entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_ID, OrderForm.class);
    query.setParameter("id", id);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public List<OrderForm> findOrderForms(Date orderDateFrom, Date orderDateTo, Long dispatchedFromId,
      Long dispatchedToId, OrderStatus status) {
    String queryString = "SELECT o FROM OrderForm o WHERE o.isDeleted=:isDeleted ";
    if (orderDateFrom != null) {
      queryString = queryString + "AND o.orderDate >= :orderDateFrom ";
    }
    if (orderDateTo != null) {
      queryString = queryString + "AND o.orderDate <= :orderDateTo ";
    }
    if (dispatchedFromId != null) {
      queryString = queryString + "AND o.dispatchedFrom.id = :dispatchedFromId ";
    }
    if (dispatchedToId != null) {
      queryString = queryString + "AND o.dispatchedTo.id = :dispatchedToId ";
    }
    if (status != null) {
      queryString = queryString + "AND o.status = :status ";
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
    if (dispatchedFromId != null) {
      query.setParameter("dispatchedFromId", dispatchedFromId);
    }
    if (dispatchedToId != null) {
      query.setParameter("dispatchedToId", dispatchedToId);
    }
    if (status != null) {
      query.setParameter("status", status);
    }

    return query.getResultList();
  }

}
