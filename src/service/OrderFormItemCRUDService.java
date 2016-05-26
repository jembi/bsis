package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.order.OrderFormItem;
import repository.OrderFormItemRepository;

@Service
@Transactional
public class OrderFormItemCRUDService {
  
  @Autowired
  private OrderFormItemRepository orderFormItemRepository;
  
  public OrderFormItem createOrUpdateOrderFormItem(OrderFormItem orderFormItem) {
    if (orderFormItem.getId() == null) {
      return createOrderFormItem(orderFormItem);
    } else {
      return updateOrderFormItem(orderFormItem);
    }
  }
  
  public OrderFormItem createOrderFormItem(OrderFormItem orderFormItem) {
    orderFormItemRepository.save(orderFormItem);
    return orderFormItem;
  }
  
  public OrderFormItem updateOrderFormItem(OrderFormItem updatedOrderFormItem) {
    OrderFormItem existingOrderFormItem = orderFormItemRepository.findById(updatedOrderFormItem.getId());
    existingOrderFormItem.setComponentType(updatedOrderFormItem.getComponentType());
    existingOrderFormItem.setBloodAbo(updatedOrderFormItem.getBloodAbo());
    existingOrderFormItem.setBloodRh(updatedOrderFormItem.getBloodRh());
    existingOrderFormItem.setNumberOfUnits(updatedOrderFormItem.getNumberOfUnits());
    return orderFormItemRepository.update(existingOrderFormItem);
  }

}
