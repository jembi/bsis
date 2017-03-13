package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.OrderFormConstraintChecker;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  private InventoryFactory inventoryFactory;

  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private PatientFactory patientFactory;

  @Autowired
  private OrderFormConstraintChecker orderFormConstraintChecker;

  public OrderForm createEntity(OrderFormBackingForm backingForm) {
    OrderForm entity = new OrderForm();
    Location from = locationRepository.getLocation(backingForm.getDispatchedFrom().getId());
    Location to = locationRepository.getLocation(backingForm.getDispatchedTo().getId());
    entity.setId(backingForm.getId());
    entity.setDispatchedFrom(from);
    if (backingForm.getPatient() != null) {
      entity.setPatient(patientFactory.createEntity(backingForm.getPatient()));
    }
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
    viewModel.setItems(orderFormItemFactory.createViewModels(entity.getItems()));
    viewModel.setComponents(inventoryFactory.createViewModels(entity.getComponents()));

    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDispatch", orderFormConstraintChecker.canDispatch(entity));
    permissions.put("canEdit", orderFormConstraintChecker.canEdit(entity));
    permissions.put("canDelete", orderFormConstraintChecker.canDelete(entity));
    viewModel.setPermissions(permissions);
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
    viewModel.setDispatchedFrom(locationFactory.createFullViewModel(entity.getDispatchedFrom()));
    viewModel.setDispatchedTo(locationFactory.createFullViewModel(entity.getDispatchedTo()));
    if (entity.getPatient() != null) {
      viewModel.setPatient(patientFactory.createViewModel(entity.getPatient()));
    }
    viewModel.setOrderDate(entity.getOrderDate());
    viewModel.setStatus(entity.getStatus());
    viewModel.setType(entity.getType());
  }
}