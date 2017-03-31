package org.jembi.bsis.service;

import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.repository.OrderFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderFormCRUDService {

  @Autowired
  private OrderFormRepository orderFormRepository;
  
  @Autowired
  private ComponentCRUDService componentCRUDService;
  
  @Autowired
  private OrderFormConstraintChecker orderFormConstraintChecker;
  
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
        if (OrderType.isIssue(updatedOrderForm.getType())) {
          componentCRUDService.issueComponent(component, updatedOrderForm.getDispatchedTo());
        } else if (OrderType.isTransfer(updatedOrderForm.getType())) {
          componentCRUDService.transferComponent(component, updatedOrderForm.getDispatchedTo());
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
    existingOrderForm.setPatient(updatedOrderForm.getPatient());
    return orderFormRepository.update(existingOrderForm);
  }

  public OrderForm deleteOrderForm(UUID id) {
    OrderForm existingOrderForm = orderFormRepository.findById(id);
    
    // Check that OrderForm can be edited
    if (!orderFormConstraintChecker.canDelete(existingOrderForm)) {
      throw new IllegalStateException("Cannot delete OrderForm.");
    }
    
    existingOrderForm.setIsDeleted(true);
    return orderFormRepository.update(existingOrderForm);
  }
}
