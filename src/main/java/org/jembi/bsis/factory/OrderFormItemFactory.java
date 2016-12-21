package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.viewmodel.OrderFormItemViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    entity.setComponentType(componentTypeFactory.createEntity(backingForm.getComponentType()));
    BloodGroup bloodGroup = new BloodGroup(backingForm.getBloodGroup());
    entity.setBloodAbo(bloodGroup.getBloodAbo());
    entity.setBloodRh(bloodGroup.getBloodRh());
    entity.setNumberOfUnits(backingForm.getNumberOfUnits());
    entity.setOrderForm(orderForm);
    return entity;
  }

  public List<OrderFormItemViewModel> createViewModels(List<OrderFormItem> entities) {
    List<OrderFormItemViewModel> items = new ArrayList<>();
    for (OrderFormItem item : entities) {
      items.add(createViewModel(item));
    }
    return items;
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