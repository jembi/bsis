package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.jembi.bsis.dto.BloodUnitsOrderDTO;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.springframework.stereotype.Repository;

@Repository
public class OrderFormRepository extends AbstractRepository<OrderForm> {

  public OrderForm findById(UUID id) throws NoResultException {
    TypedQuery<OrderForm> query = entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_ID, OrderForm.class);
    query.setParameter("id", id);
    query.setParameter("isDeleted", false);
    return query.getSingleResult();
  }

  public List<OrderForm> findByComponent(UUID componentId) {
    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_BY_COMPONENT, OrderForm.class)
        .setParameter("componentId", componentId)
        .setParameter("isDeleted", false)
        .getResultList();
  }

  public List<OrderForm> findOrderForms(Date orderDateFrom, Date orderDateTo, UUID dispatchedFromId,
      UUID dispatchedToId, OrderType type, OrderStatus status) {
    boolean includeAllStatuses = false, incudeAllTypes = false;
    if (status == null) {
      includeAllStatuses = true;
    }
    if (type == null) {
      incudeAllTypes = true;
    }

    boolean includeAllDispatchFromLocations = false;
    if (dispatchedFromId == null) {
      includeAllDispatchFromLocations = true;
    }
    boolean includeAllDispatchToLocations = false;
    if (dispatchedToId == null) {
      includeAllDispatchToLocations = true;
    }

    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_FIND_ORDER_FORMS,
        OrderForm.class)
        .setParameter("isDeleted", false)
        .setParameter("orderDateFrom", orderDateFrom)
        .setParameter("orderDateTo", orderDateTo)
        .setParameter("dispatchedFromId", dispatchedFromId)
        .setParameter("includeAllDispatchFromLocations", includeAllDispatchFromLocations)
        .setParameter("dispatchedToId", dispatchedToId)
        .setParameter("includeAllDispatchToLocations", includeAllDispatchToLocations)
        .setParameter("type", type)
        .setParameter("status", status)
        .setParameter("includeAllStatuses", includeAllStatuses)
        .setParameter("incudeAllTypes", incudeAllTypes)
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

  public boolean isComponentInAnotherOrderForm(UUID id, UUID componentId) {
    return entityManager.createNamedQuery(OrderFormNamedQueryConstants.NAME_IS_COMPONENT_IN_ANOTHER_ORDER_FORM, Boolean.class)
        .setParameter("id", id)
        .setParameter("includeId", id != null)
        .setParameter("componentId", componentId)
        .setParameter("orderStatus", OrderStatus.CREATED)
        .setParameter("isDeleted", false)
        .getSingleResult();
  }
}
