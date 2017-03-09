package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.springframework.stereotype.Repository;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(long id) throws NoResultException {
    TypedQuery<OrderForm> query = entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_ID, OrderForm.class);
    query.setParameter("id", id);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public OrderForm findByComponent(long componentId) {
    List<OrderForm> orderFormList =
        entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_COMPONENT, OrderForm.class)
        .setParameter("componentId", componentId)
        .setParameter("isDeleted", false)
        .getResultList();

    if (orderFormList.size() == 0) {
      return null;
    } else if (orderFormList.size() == 1) {
      return orderFormList.get(0);
    }

    throw new IllegalStateException(
        "The component with ID: " + componentId + " is present in more than one OrderForm. This is not allowed. ");
  }

  public List<OrderForm> findOrderForms(Date orderDateFrom, Date orderDateTo, Long dispatchedFromId,
      Long dispatchedToId, OrderType type, OrderStatus status) {
    boolean includeStatus = true, incudeType = true;
    if (status == null) {
      includeStatus = false;
    }
    if (type == null) {
      incudeType = false;
    }
    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_ORDER_FORMS,
        OrderForm.class)
        .setParameter("isDeleted", false)
        .setParameter("orderDateFrom", orderDateFrom)
        .setParameter("orderDateTo", orderDateTo)
        .setParameter("dispatchedFromId", dispatchedFromId)
        .setParameter("dispatchedToId", dispatchedToId)
        .setParameter("type", type)
        .setParameter("status", status)
        .setParameter("includeStatus", includeStatus)
        .setParameter("incudeType", incudeType)
        .getResultList();
  }

  public List<BloodUnitsOrderDTO> findBloodUnitsOrdered(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BLOOD_UNITS_ORDERED, 
        BloodUnitsOrderDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("orderStatus", OrderStatus.DISPATCHED)
        .setParameter("orderTypes", OrderType.getIssueOrderTypes())
        .setParameter("orderDeleted", false)
        .getResultList();
  }

  public List<BloodUnitsOrderDTO> findBloodUnitsIssued(Date startDate, Date endDate) {
    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BLOOD_UNITS_ISSUED, 
        BloodUnitsOrderDTO.class)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("orderStatus", OrderStatus.DISPATCHED)
        .setParameter("orderTypes", OrderType.getIssueOrderTypes())
        .setParameter("orderDeleted", false)
        .getResultList();
  }

  public boolean verifyComponentNotInAnotherOrderForm(Long id, Long componentId) {
    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_VERIFY_COMPONENT_NOT_IN_ANOTHER_ORDER_FORM, Boolean.class)
        .setParameter("id", id)
        .setParameter("componentId", componentId)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }
}
