package org.jembi.bsis.controllerservice;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.OrderFormFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.OrderFormRepository;
import org.jembi.bsis.service.OrderFormCRUDService;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderFormControllerService {
  
  @Autowired
  private OrderFormFactory orderFormFactory;
  
  @Autowired
  private OrderFormRepository orderFormRepository;
  
  @Autowired
  private OrderFormCRUDService orderFormCRUDService;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Autowired
  private LocationFactory locationFactory;
  
  @Autowired
  private ComponentTypeFactory componentTypeFactory;
  
  public List<OrderFormViewModel> findOrderForms(Date orderDateFrom, Date orderDateTo, UUID dispatchedFromId,
      UUID dispatchedToId, OrderType type, OrderStatus status) {
    List<OrderForm> orderForms = orderFormRepository.findOrderForms(orderDateFrom, orderDateTo, dispatchedFromId, 
        dispatchedToId, type, status);
    return orderFormFactory.createViewModels(orderForms);
  }
  
  public OrderFormFullViewModel findOrderForm(UUID id) {
    OrderForm orderForm = orderFormRepository.findById(id);
    return orderFormFactory.createFullViewModel(orderForm);
  }

  public OrderFormFullViewModel createOrderForm(OrderFormBackingForm backingForm) {
    OrderForm entity = orderFormFactory.createEntity(backingForm);
    entity.setStatus(OrderStatus.CREATED);
    orderFormRepository.save(entity);
    return orderFormFactory.createFullViewModel(entity);
  }
  
  public OrderFormFullViewModel updateOrderForm(OrderFormBackingForm backingForm) {
    OrderForm orderForm = orderFormFactory.createEntity(backingForm);
    OrderForm updatedOrderForm = orderFormCRUDService.updateOrderForm(orderForm);
    return orderFormFactory.createFullViewModel(updatedOrderForm);
  }
  
  public List<LocationFullViewModel> getUsageSites() {
    List<Location> usageSites = locationRepository.getUsageSites();
    return locationFactory.createFullViewModels(usageSites);
  }
  
  public List<LocationFullViewModel> getDistributionSites() {
    List<Location> distributionSites = locationRepository.getDistributionSites();
    return locationFactory.createFullViewModels(distributionSites);
  }
  
  public List<ComponentTypeViewModel> getAllComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypesThatCanBeIssued());
  }

  public void deleteOrderForm(UUID id) {
    orderFormCRUDService.deleteOrderForm(id);
  }
}
