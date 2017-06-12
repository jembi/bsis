package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;

public class ComponentViewModelBuilder extends AbstractBuilder<ComponentViewModel> {

  private UUID id;
  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private Date expiresOn;
  private String donationIdentificationNumber;
  private String donationFlagCharacters;
  private ComponentStatus status;
  private int daysToExpire;
  private String componentCode;
  private LocationViewModel location;

  public ComponentViewModelBuilder withId(UUID id) {
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

  public ComponentViewModelBuilder withDonationFlagCharacters(String donationFlagCharacters) {
    this.donationFlagCharacters = donationFlagCharacters;
    return this;
  }

  public ComponentViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public ComponentViewModelBuilder withExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }
  
  public ComponentViewModelBuilder withDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
    return this;
  }

  public ComponentViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public ComponentViewModelBuilder withLocation(LocationViewModel location){
    this.location = location;
    return this;
  }

  @Override
  public ComponentViewModel build() {
    ComponentViewModel viewModel = new ComponentViewModel();
    viewModel.setId(id);
    viewModel.setStatus(status);
    viewModel.setComponentType(componentType);
    viewModel.setCreatedOn(createdOn);
    viewModel.setExpiresOn(expiresOn);
    viewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    viewModel.setDonationFlagCharacters(donationFlagCharacters);
    viewModel.setComponentCode(componentCode);
    viewModel.setDaysToExpire(daysToExpire);
    viewModel.setLocation(location);
    return viewModel;
  }
  
  public static ComponentViewModelBuilder aComponentViewModel() {
    return new ComponentViewModelBuilder();
  }

}
