package org.jembi.bsis.service;

import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderFormConstraintChecker {

  public boolean canDispatch(OrderForm orderForm) {
    return 
        orderForm.getStatus().equals(OrderStatus.CREATED) && 
        orderForm.getComponents() != null && 
        orderForm.getComponents().size() > 0 &&
        (!OrderType.isPatientRequest(orderForm.getType())
            || (OrderType.isPatientRequest(orderForm.getType()) && orderForm.getPatient() != null));
  }
  
  public boolean canEdit(OrderForm orderForm) {
    return orderForm.getStatus().equals(OrderStatus.CREATED);
  }
  
  public boolean canDelete(OrderForm orderForm) {
    return !orderForm.getStatus().equals(OrderStatus.DISPATCHED);
  }
}