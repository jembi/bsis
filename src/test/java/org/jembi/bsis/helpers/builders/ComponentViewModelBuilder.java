package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;

public class ComponentViewModelBuilder extends AbstractBuilder<ComponentViewModel> {

  private Long id;
  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private String donationIdentificationNumber;
  private ComponentStatus status;
  private String expiryStatus;
  private String componentCode;

  public ComponentViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentViewModelBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  public ComponentViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public ComponentViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public ComponentViewModelBuilder withExpiryStatus(String expiryStatus) {
    this.expiryStatus = expiryStatus;
    return this;
  }

  public ComponentViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  @Override
  public ComponentViewModel build() {
    ComponentViewModel viewModel = new ComponentViewModel();
    viewModel.setId(id);
    viewModel.setStatus(status);
    viewModel.setComponentType(componentType);
    viewModel.setCreatedOn(createdOn);
    viewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    viewModel.setComponentCode(componentCode);
    viewModel.setExpiryStatus(expiryStatus);
    return viewModel;
  }
  
  public static ComponentViewModelBuilder aComponentViewModel() {
    return new ComponentViewModelBuilder();
  }

}
