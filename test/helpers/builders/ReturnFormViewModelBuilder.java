package helpers.builders;

import java.util.Date;

import model.returnform.ReturnStatus;
import viewmodel.LocationViewModel;
import viewmodel.ReturnFormViewModel;

public class ReturnFormViewModelBuilder extends AbstractBuilder<ReturnFormViewModel> {

  private Long id;
  private Date returnDate;
  private LocationViewModel returnedFrom;
  private LocationViewModel returnedTo;
  private ReturnStatus status = ReturnStatus.CREATED;

  public ReturnFormViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnDate(Date returnDate) {
    this.returnDate = returnDate;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnedFrom(LocationViewModel returnedFrom) {
    this.returnedFrom = returnedFrom;
    return this;
  }

  public ReturnFormViewModelBuilder withReturnedTo(LocationViewModel returnedTo) {
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
