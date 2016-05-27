package factory;

import java.util.ArrayList;
import java.util.List;

import model.component.Component;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderFormItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.ComponentRepository;
import repository.LocationRepository;
import viewmodel.OrderFormFullViewModel;
import viewmodel.OrderFormItemViewModel;
import viewmodel.OrderFormViewModel;
import backingform.ComponentBackingForm;
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

  @Autowired
  private ComponentRepository componentRepository;

  @Autowired
  private ComponentViewModelFactory componentViewModelFactory;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

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
    List<Component> components = new ArrayList<>();
    if (backingForm.getComponents() != null) {
      for (ComponentBackingForm component : backingForm.getComponents()) {
        components.add(componentRepository.findComponent(component.getId()));
      }
    }
    entity.setItems(items);
    entity.setComponents(components);
    return entity;
  }

  public OrderFormFullViewModel createFullViewModel(OrderForm entity) {
    OrderFormFullViewModel viewModel = new OrderFormFullViewModel();
    populateBasicViewModel(entity, viewModel);
    List<OrderFormItemViewModel> items = new ArrayList<>();
    for (OrderFormItem item : entity.getItems()) {
      items.add(orderFormItemFactory.createViewModel(item));
    }
    viewModel.setItems(items);
    viewModel.setComponents(componentViewModelFactory.createComponentViewModels(entity.getComponents()));
    return viewModel;
  }

  public OrderFormViewModel createViewModel(OrderForm entity) {
    OrderFormViewModel viewModel = new OrderFormViewModel();
    populateBasicViewModel(entity, viewModel);
    return viewModel;
  }
  
  public List<OrderFormViewModel> createViewModels(List<OrderForm> entities) {
    List<OrderFormViewModel> viewModels = new ArrayList<>();
    if (entities != null) {
      for (OrderForm entity : entities) {
        viewModels.add(createViewModel(entity));
      }
    }
    return viewModels;
  }
  
  private void populateBasicViewModel(OrderForm entity, OrderFormViewModel viewModel) {
    viewModel.setId(entity.getId());
    viewModel.setDispatchedFrom(locationViewModelFactory.createLocationViewModel(entity.getDispatchedFrom()));
    viewModel.setDispatchedTo(locationViewModelFactory.createLocationViewModel(entity.getDispatchedTo()));
    viewModel.setOrderDate(entity.getOrderDate());
    viewModel.setStatus(entity.getStatus());
    viewModel.setType(entity.getType());
  }
}