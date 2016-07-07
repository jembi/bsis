package org.jembi.bsis.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.factory.OrderFormFactory;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderFormCRUDService {

  @Autowired
  private OrderFormFactory orderFormFactory;

  @Autowired
  private OrderFormRepository orderFormRepository;
  
  @Autowired
  private ComponentDispatchService componentDispatchService;
  
  @Autowired
  private OrderFormConstraintChecker orderFormConstraintChecker;

  public OrderForm createOrderForm(OrderFormBackingForm backingForm) {
    OrderForm entity = orderFormFactory.createEntity(backingForm);
    entity.setStatus(OrderStatus.CREATED);
    orderFormRepository.save(entity);
    return entity;
  }
  
  public OrderFormFullViewModel updateOrderForm(OrderFormBackingForm backingForm) {
    OrderForm orderForm = orderFormFactory.createEntity(backingForm);
    OrderForm updatedOrderForm = updateOrderForm(orderForm);
    return orderFormFactory.createFullViewModel(updatedOrderForm);
  }
  
  public OrderForm updateOrderForm(OrderForm updatedOrderForm) {
    OrderForm existingOrderForm = orderFormRepository.findById(updatedOrderForm.getId());
    
    // Check that OrderForm can be edited
    if (!orderFormConstraintChecker.canEdit(existingOrderForm)) {
      throw new IllegalStateException("Cannot edit OrderForm.");
    }

    // If the order is being dispatched then transfer or issue each component
    if (updatedOrderForm.getStatus() == OrderStatus.DISPATCHED) {

      // Check that OrderForm can be dispatched
      if (!orderFormConstraintChecker.canDispatch(existingOrderForm)) {
        throw new IllegalStateException("Cannot dispatch OrderForm");
      }

      for (Component component : updatedOrderForm.getComponents()) {
        if (updatedOrderForm.getType() == OrderType.ISSUE) {
          componentDispatchService.issueComponent(component, updatedOrderForm.getDispatchedTo());
        } else if (updatedOrderForm.getType() == OrderType.TRANSFER) {
          componentDispatchService.transferComponent(component, updatedOrderForm.getDispatchedTo());
        }
      }
    }

    // Reset orderFormItems so that hibernate can execute the orphan removal
    existingOrderForm.getItems().clear();
    existingOrderForm.getItems().addAll(updatedOrderForm.getItems());

    existingOrderForm.setOrderDate(updatedOrderForm.getOrderDate());
    existingOrderForm.setStatus(updatedOrderForm.getStatus());
    existingOrderForm.setType(updatedOrderForm.getType());
    existingOrderForm.setDispatchedFrom(updatedOrderForm.getDispatchedFrom());
    existingOrderForm.setDispatchedTo(updatedOrderForm.getDispatchedTo());
    existingOrderForm.setComponents(updatedOrderForm.getComponents());
    return orderFormRepository.update(existingOrderForm);
  }

  public List<OrderForm> findOrderForms(Date orderDateFrom, Date orderDateTo, Long dispatchedFromId,
      Long dispatchedToId, OrderType type, OrderStatus status) {
    return orderFormRepository.findOrderForms(orderDateFrom, orderDateTo, dispatchedFromId, dispatchedToId, type, status);
  }
}
