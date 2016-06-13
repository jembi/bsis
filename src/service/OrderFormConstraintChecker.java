package service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.order.OrderForm;
import model.order.OrderStatus;

@Transactional
@Service
public class OrderFormConstraintChecker {

  public boolean canDispatch(OrderForm orderForm) {
    return 
        orderForm.getStatus().equals(OrderStatus.CREATED) && 
        orderForm.getComponents() != null && 
        orderForm.getComponents().size() > 0;
  }
  
  public boolean canEdit(OrderForm orderForm) {
    return orderForm.getStatus().equals(OrderStatus.CREATED);
  }
}
