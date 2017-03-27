package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;
import org.jembi.bsis.viewmodel.PatientViewModel;

public class OrderFormViewModelBuilder extends AbstractBuilder<OrderFormViewModel> {

  private UUID id;
  private Date orderDate;
  private LocationFullViewModel dispatchedFrom;
  private LocationFullViewModel dispatchedTo;
  private PatientViewModel patient;
  private OrderStatus status = OrderStatus.CREATED;
  private OrderType type = OrderType.ISSUE;

  public OrderFormViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public OrderFormViewModelBuilder withOrderDate(Date orderDate) {
    this.orderDate = orderDate;
    return this;
  }

  public OrderFormViewModelBuilder withDispatchedFrom(LocationFullViewModel dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
    return this;
  }

  public OrderFormViewModelBuilder withDispatchedTo(LocationFullViewModel dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
    return this;
  }

  public OrderFormViewModelBuilder withPatient(PatientViewModel patient) {
    this.patient = patient;
    return this;
  }

  public OrderFormViewModelBuilder withOrderStatus(OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderFormViewModelBuilder withOrderType(OrderType type) {
    this.type = type;
    return this;
  }

  public OrderFormViewModel build() {
    OrderFormViewModel viewModel = new OrderFormViewModel();
    viewModel.setId(id);
    viewModel.setDispatchedFrom(dispatchedFrom);
    viewModel.setDispatchedTo(dispatchedTo);
    viewModel.setOrderDate(orderDate);
    viewModel.setStatus(status);
    viewModel.setType(type);
    viewModel.setPatient(patient);
    return viewModel;
  }

  public static OrderFormViewModelBuilder anOrderFormViewModel() {
    return new OrderFormViewModelBuilder();
  }

}
