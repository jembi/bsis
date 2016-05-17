package factory;

import model.order.OrderForm;
import model.order.OrderFormItem;

import org.springframework.stereotype.Service;

import viewmodel.ComponentTypeViewModel;
import viewmodel.OrderFormItemViewModel;
import backingform.OrderFormItemBackingForm;

/**
 * A factory for creating OrderFormItem objects.
 */
@Service
public class OrderFormItemFactory {

  public OrderFormItem createEntity(OrderForm orderForm, OrderFormItemBackingForm backingForm) {
    OrderFormItem entity = new OrderFormItem();
    entity.setId(backingForm.getId());
    entity.setComponentType(backingForm.getComponentType().getComponentType());
    entity.setBloodAbo(backingForm.getBloodAbo());
    entity.setBloodRh(backingForm.getBloodRh());
    entity.setNumberOfUnits(backingForm.getNumberOfUnits());
    entity.setOrderForm(orderForm);
    entity.setDeleted(backingForm.getIsDeleted());
    return entity;
  }

  public OrderFormItemViewModel createViewModel(OrderFormItem entity) {
    OrderFormItemViewModel viewModel = new OrderFormItemViewModel();
    viewModel.setId(entity.getId());
    viewModel.setBloodAbo(entity.getBloodAbo());
    viewModel.setBloodRh(entity.getBloodRh());
    viewModel.setComponentType(new ComponentTypeViewModel(entity.getComponentType()));
    viewModel.setNumberOfUnits(entity.getNumberOfUnits());
    return viewModel;
  }

}