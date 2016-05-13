package factory;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backingform.OrderFormBackingForm;
import model.location.Location;
import model.order.OrderForm;
import repository.LocationRepository;
import viewmodel.LocationViewModel;
import viewmodel.OrderFormViewModel;

/**
 * A factory for creating OrderForm related objects.
 */
@Service
public class OrderFormFactory {

  @Autowired
  private LocationRepository locationRepository;

  public OrderForm createEntity(OrderFormBackingForm backingForm) {
    OrderForm entity = new OrderForm();
    Location from = locationRepository.getLocation(backingForm.getDispatchedFrom().getId());
    Location to = locationRepository.getLocation(backingForm.getDispatchedTo().getId());
    entity.setDispatchedFrom(from);
    entity.setDispatchedTo(to);
    entity.setOrderDate(backingForm.getOrderDate());
    return entity;
  }

  public OrderFormViewModel createViewModel(OrderForm entity) {
    OrderFormViewModel viewModel = new OrderFormViewModel();
    viewModel.setId(entity.getId());
    viewModel.setDispatchedFrom(new LocationViewModel(entity.getDispatchedFrom()));
    viewModel.setDispatchedTo(new LocationViewModel(entity.getDispatchedTo()));
    viewModel.setOrderDate(entity.getOrderDate());
    return viewModel;
  }

}
