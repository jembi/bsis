package helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.order.OrderStatus;
import model.order.OrderType;
import viewmodel.ComponentViewModel;
import viewmodel.LocationViewModel;
import viewmodel.OrderFormItemViewModel;
import viewmodel.OrderFormFullViewModel;

public class OrderFormFullViewModelBuilder extends AbstractBuilder<OrderFormFullViewModel> {

  private Long id;
  private Date orderDate;
  private LocationViewModel dispatchedFrom;
  private LocationViewModel dispatchedTo;
  private OrderStatus status = OrderStatus.CREATED;
  private OrderType type = OrderType.ISSUE;
  private List<OrderFormItemViewModel> items = new ArrayList<>();
  private List<ComponentViewModel> components = new ArrayList<>();

  public OrderFormFullViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormFullViewModelBuilder withOrderDate(Date orderDate) {
    this.orderDate = orderDate;
    return this;
  }

  public OrderFormFullViewModelBuilder withDispatchedFrom(LocationViewModel dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
    return this;
  }

  public OrderFormFullViewModelBuilder withDispatchedTo(LocationViewModel dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
    return this;
  }

  public OrderFormFullViewModelBuilder withOrderStatus(OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderFormFullViewModelBuilder withOrderType(OrderType type) {
    this.type = type;
    return this;
  }
  
  public OrderFormFullViewModelBuilder withItem(OrderFormItemViewModel item) {
    this.items.add(item);
    return this;
  }

  public OrderFormFullViewModelBuilder withComponent(ComponentViewModel component) {
    this.components.add(component);
    return this;
  }

  public OrderFormFullViewModel build() {
    OrderFormFullViewModel viewModel = new OrderFormFullViewModel();
    viewModel.setId(id);
    viewModel.setDispatchedFrom(dispatchedFrom);
    viewModel.setDispatchedTo(dispatchedTo);
    viewModel.setOrderDate(orderDate);
    viewModel.setStatus(status);
    viewModel.setType(type);
    viewModel.setItems(items);
    viewModel.setComponents(components);
    return viewModel;
  }

  public static OrderFormFullViewModelBuilder anOrderFormFullViewModel() {
    return new OrderFormFullViewModelBuilder();
  }

}
