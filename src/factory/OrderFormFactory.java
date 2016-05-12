package factory;

import org.springframework.stereotype.Service;

import backingform.OrderFormBackingForm;
import model.order.OrderForm;
import viewmodel.OrderFormViewModel;

/**
 * A factory for creating OrderForm related objects.
 */
@Service
public class OrderFormFactory {

  public OrderForm toEntity(OrderFormBackingForm backingForm) {
    OrderForm entity = new OrderForm();
    entity.setDispatchedFrom(backingForm.getDispatchedFrom());
    entity.setDispatchedTo(backingForm.getDispatchedTo());
    entity.setOrderDate(backingForm.getOrderDate());
    return entity;
  }

  public OrderFormViewModel toViewModel(OrderForm entity) {
    OrderFormViewModel viewModel = new OrderFormViewModel();
    viewModel.setId(entity.getId());
    viewModel.setDispatchedFrom(entity.getDispatchedFrom());
    viewModel.setDispatchedTo(entity.getDispatchedTo());
    viewModel.setOrderDate(entity.getOrderDate());
    return viewModel;
  }

}
