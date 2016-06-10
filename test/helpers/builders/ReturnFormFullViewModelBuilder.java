package helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.returnform.ReturnStatus;
import viewmodel.ComponentViewModel;
import viewmodel.LocationViewModel;
import viewmodel.ReturnFormFullViewModel;

public class ReturnFormFullViewModelBuilder extends AbstractBuilder<ReturnFormFullViewModel> {

  private Long id;
  private Date returnDate;
  private LocationViewModel returnedFrom;
  private LocationViewModel returnedTo;
  private ReturnStatus status = ReturnStatus.CREATED;
  private List<ComponentViewModel> components = new ArrayList<>();

  public ReturnFormFullViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ReturnFormFullViewModelBuilder withReturnDate(Date returnDate) {
    this.returnDate = returnDate;
    return this;
  }

  public ReturnFormFullViewModelBuilder withReturnedFrom(LocationViewModel returnedFrom) {
    this.returnedFrom = returnedFrom;
    return this;
  }

  public ReturnFormFullViewModelBuilder withReturnedTo(LocationViewModel returnedTo) {
    this.returnedTo = returnedTo;
    return this;
  }

  public ReturnFormFullViewModelBuilder withReturnStatus(ReturnStatus status) {
    this.status = status;
    return this;
  }

  public ReturnFormFullViewModelBuilder withComponent(ComponentViewModel component) {
    components.add(component);
    return this;
  }

  public ReturnFormFullViewModel build() {
    ReturnFormFullViewModel viewModel = new ReturnFormFullViewModel();
    viewModel.setId(id);
    viewModel.setReturnedFrom(returnedFrom);
    viewModel.setReturnedTo(returnedTo);
    viewModel.setReturnDate(returnDate);
    viewModel.setStatus(status);
    viewModel.setComponents(components);
    return viewModel;
  }

  public static ReturnFormFullViewModelBuilder aReturnFormFullViewModel() {
    return new ReturnFormFullViewModelBuilder();
  }

}
