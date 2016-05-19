package service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.OrderFormBackingForm;
import factory.OrderFormFactory;
import model.order.OrderForm;
import model.order.OrderStatus;
import repository.OrderFormRepository;
import viewmodel.OrderFormViewModel;

@Service
@Transactional
public class OrderFormCRUDService {

  @Autowired
  private OrderFormFactory orderFormFactory;

  @Autowired
  private OrderFormRepository orderFormRepository;

  public OrderForm createOrderForm(OrderFormBackingForm backingForm) {
    OrderForm entity = orderFormFactory.createEntity(backingForm);
    entity.setStatus(OrderStatus.CREATED);
    orderFormRepository.save(entity);
    return entity;
  }
  
  public OrderFormViewModel updateOrderForm(OrderFormBackingForm backingForm) {
    OrderForm orderForm = orderFormFactory.createEntity(backingForm);
    OrderForm updatedOrderForm = updateOrderForm(orderForm);
    return orderFormFactory.createViewModel(updatedOrderForm);
  }
  
  public OrderForm updateOrderForm(OrderForm updatedOrderForm) {
    OrderForm existingOrderForm = orderFormRepository.findById(updatedOrderForm.getId());
    existingOrderForm.setOrderDate(updatedOrderForm.getOrderDate());
    existingOrderForm.setStatus(updatedOrderForm.getStatus());
    existingOrderForm.setType(updatedOrderForm.getType());
    existingOrderForm.setDispatchedFrom(updatedOrderForm.getDispatchedFrom());
    existingOrderForm.setDispatchedTo(updatedOrderForm.getDispatchedTo());
    existingOrderForm.setItems(updatedOrderForm.getItems());
    return orderFormRepository.update(existingOrderForm);
  }
}
