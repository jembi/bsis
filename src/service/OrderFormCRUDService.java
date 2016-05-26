package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.OrderFormBackingForm;
import factory.LocationViewModelFactory;
import factory.OrderFormFactory;
import model.order.OrderForm;
import model.order.OrderFormItem;
import model.order.OrderStatus;
import repository.OrderFormRepository;
import viewmodel.OrderFormFullViewModel;

@Service
@Transactional
public class OrderFormCRUDService {

  @Autowired
  private OrderFormFactory orderFormFactory;

  @Autowired
  private OrderFormRepository orderFormRepository;
  
  @Autowired
  private OrderFormItemCRUDService orderFormItemCRUDService;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

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
    existingOrderForm.setOrderDate(updatedOrderForm.getOrderDate());
    existingOrderForm.setStatus(updatedOrderForm.getStatus());
    existingOrderForm.setType(updatedOrderForm.getType());
    existingOrderForm.setDispatchedFrom(updatedOrderForm.getDispatchedFrom());
    existingOrderForm.setDispatchedTo(updatedOrderForm.getDispatchedTo());
    List<OrderFormItem> items = new ArrayList<>();
    for (OrderFormItem item : updatedOrderForm.getItems()) {
      items.add(orderFormItemCRUDService.createOrUpdateOrderFormItem(item));
    }
    existingOrderForm.setItems(items);
    existingOrderForm.setComponents(updatedOrderForm.getComponents());
    return orderFormRepository.update(existingOrderForm);
  }

  public List<OrderForm> findOrderForms(Date orderDateFrom, Date orderDateTo, Long dispatchedFromId,
      Long dispatchedToId, OrderStatus status) {
    return orderFormRepository.findOrderForms(orderDateFrom, orderDateTo, dispatchedFromId, dispatchedToId, status);
  }
}
