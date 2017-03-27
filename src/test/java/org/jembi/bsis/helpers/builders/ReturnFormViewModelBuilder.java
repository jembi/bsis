package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.ReturnFormViewModel;

public class ReturnFormViewModelBuilder extends AbstractBuilder<ReturnFormViewModel> {

  private UUID id;
  private Date returnDate;
  private LocationFullViewModel returnedFrom;
  private LocationFullViewModel returnedTo;
  private ReturnStatus status = ReturnStatus.CREATED;

  public ReturnFormViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnDate(Date returnDate) {
    this.returnDate = returnDate;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnedFrom(LocationFullViewModel returnedFrom) {
    this.returnedFrom = returnedFrom;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnedTo(LocationFullViewModel returnedTo) {
    this.returnedTo = returnedTo;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnStatus(ReturnStatus status) {
    this.status = status;
    return this;
  }

  public ReturnFormViewModel build() {
    ReturnFormViewModel viewModel = new ReturnFormViewModel();
    viewModel.setId(id);
    viewModel.setReturnedFrom(returnedFrom);
    viewModel.setReturnedTo(returnedTo);
    viewModel.setReturnDate(returnDate);
    viewModel.setStatus(status);
    return viewModel;
  }

  public static ReturnFormViewModelBuilder aReturnFormViewModel() {
    return new ReturnFormViewModelBuilder();
  }

}