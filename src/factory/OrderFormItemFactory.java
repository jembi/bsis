package factory;

import model.order.OrderForm;
import model.order.OrderFormItem;
import model.util.BloodGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viewmodel.OrderFormItemViewModel;
import backingform.OrderFormItemBackingForm;

/**
 * A factory for creating OrderFormItem objects.
 */
@Service
public class OrderFormItemFactory {
  
  @Autowired
  ComponentTypeFactory componentTypeFactory;

  public OrderFormItem createEntity(OrderForm orderForm, OrderFormItemBackingForm backingForm) {
    OrderFormItem entity = new OrderFormItem();
    entity.setId(backingForm.getId());
    entity.setComponentType(backingForm.getComponentType().getComponentType());
    BloodGroup bloodGroup = new BloodGroup(backingForm.getBloodGroup());
    entity.setBloodAbo(bloodGroup.getBloodAbo());
    entity.setBloodRh(bloodGroup.getBloodRh());
    entity.setNumberOfUnits(backingForm.getNumberOfUnits());
    entity.setOrderForm(orderForm);
    return entity;
  }

  public OrderFormItemViewModel createViewModel(OrderFormItem entity) {
    OrderFormItemViewModel viewModel = new OrderFormItemViewModel();
    viewModel.setId(entity.getId());
    viewModel.setBloodGroup(entity.getBloodAbo()+entity.getBloodRh());
    viewModel.setComponentType(componentTypeFactory.createViewModel(entity.getComponentType()));
    viewModel.setNumberOfUnits(entity.getNumberOfUnits());
    return viewModel;
  }

}