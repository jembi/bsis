package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.TransfusionViewModel;

public class TransfusionViewModelBuilder extends AbstractBuilder<TransfusionViewModel> {

  private UUID id;
  private String donationIdentificationNumber;
  private String componentCode;
  private String componentType;
  private LocationViewModel usageSite;
  private TransfusionOutcome transfusionOutcome;
  private Date dateTransfused;  
  
  public TransfusionViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public TransfusionViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public TransfusionViewModelBuilder withComponentType(String componentType) {
    this.componentType = componentType;
    return this;
  }

  public TransfusionViewModelBuilder withUsageSite(LocationViewModel usageSite) {
    this.usageSite = usageSite;
    return this;
  }

  public TransfusionViewModelBuilder withTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
    return this;
  }

  public TransfusionViewModelBuilder withDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
    return this;
  }

  @Override
  public TransfusionViewModel build() {
    TransfusionViewModel transfusionViewModel = new TransfusionViewModel();
    transfusionViewModel.setId(id);
    transfusionViewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    transfusionViewModel.setComponentCode(componentCode);
    transfusionViewModel.setComponentType(componentType);
    transfusionViewModel.setReceivedFrom(usageSite);
    transfusionViewModel.setTransfusionOutcome(transfusionOutcome);
    transfusionViewModel.setDateTransfused(dateTransfused);
    return transfusionViewModel;
  }

  public static TransfusionViewModelBuilder aTransfusionViewModel() {
    return new TransfusionViewModelBuilder();
  }
  
}