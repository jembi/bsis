package factory;



import java.util.ArrayList;
import java.util.List;

import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.LocationRepository;
import viewmodel.LocationViewModel;
import viewmodel.OrderFormItemViewModel;
import viewmodel.OrderFormViewModel;
import backingform.OrderFormBackingForm;
import backingform.OrderFormItemBackingForm;

/**
 * A factory for creating OrderForm related objects.
 */
@Service
public class OrderFormFactory {

  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private OrderFormItemFactory orderFormItemFactory;

  public OrderForm createEntity(OrderFormBackingForm backingForm) {
    OrderForm entity = new OrderForm();
    Location from = locationRepository.getLocation(backingForm.getDispatchedFrom().getId());
    Location to = locationRepository.getLocation(backingForm.getDispatchedTo().getId());
    entity.setId(backingForm.getId());
    entity.setDispatchedFrom(from);
    entity.setDispatchedTo(to);
    entity.setOrderDate(backingForm.getOrderDate());
    entity.setStatus(backingForm.getStatus());
    entity.setType(backingForm.getType());
    List<OrderFormItem> items = new ArrayList<>();
    if (backingForm.getItems() != null) {
      for (OrderFormItemBackingForm item : backingForm.getItems()) {
        items.add(orderFormItemFactory.createEntity(entity, item));
      }
    }
    entity.setItems(items);
    return entity;
  }

  public OrderFormViewModel createViewModel(OrderForm entity) {
    OrderFormViewModel viewModel = new OrderFormViewModel();
    viewModel.setId(entity.getId());
    viewModel.setDispatchedFrom(new LocationViewModel(entity.getDispatchedFrom()));
    viewModel.setDispatchedTo(new LocationViewModel(entity.getDispatchedTo()));
    viewModel.setOrderDate(entity.getOrderDate());
    viewModel.setStatus(entity.getStatus());
    viewModel.setType(entity.getType());
    List<OrderFormItemViewModel> items = new ArrayList<>();
    for (OrderFormItem item : entity.getItems()) {
      items.add(orderFormItemFactory.createViewModel(item));
    }
    viewModel.setItems(items);
    return viewModel;
  }

}
