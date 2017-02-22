package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;
import org.jembi.bsis.viewmodel.TransfusionViewModel;

import java.util.Date;

public class TransfusionViewModelBuilder extends AbstractBuilder<TransfusionViewModel> {

  private Long id;
  private String donationIdentificationNumber;
  private PatientViewModel patient;
  private String notes;
  private ComponentViewModel component;
  private LocationViewModel usageSite;
  private TransfusionReactionTypeViewModel transfusionReactionType;
  private TransfusionOutcome transfusionOutcome;
  private Date dateTransfused;  
  private boolean isDeleted;
  
  public TransfusionViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TransfusionViewModelBuilder withPatient(PatientViewModel patient) {
    this.patient = patient;
    return this;
  }

  public TransfusionViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public TransfusionViewModelBuilder withComponent(ComponentViewModel component) {
    this.component = component;
    return this;
  }
  
  public TransfusionViewModelBuilder withUsageSite(LocationViewModel usageSite) {
    this.usageSite = usageSite;
    return this;
  }
  
  public TransfusionViewModelBuilder withTransfusionReactionType(TransfusionReactionTypeViewModel transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
    return this;
  }

  public TransfusionViewModelBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public TransfusionViewModelBuilder thatIsNotDeleted() {
    isDeleted = false;
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

  public TransfusionViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @Override
  public TransfusionViewModel build() {
    TransfusionViewModel transfusionViewModel = new TransfusionViewModel();
    transfusionViewModel.setId(id);
    transfusionViewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    transfusionViewModel.setPatient(patient);
    transfusionViewModel.setComponent(component);
    transfusionViewModel.setReceivedFrom(usageSite);
    transfusionViewModel.setTransfusionReactionType(transfusionReactionType);
    transfusionViewModel.setTransfusionOutcome(transfusionOutcome);
    transfusionViewModel.setDateTransfused(dateTransfused);
    transfusionViewModel.setIsDeleted(isDeleted);
    transfusionViewModel.setNotes(notes);
    return transfusionViewModel;
  }

  public static TransfusionViewModelBuilder aTransfusionViewModel() {
    return new TransfusionViewModelBuilder();
  }
  
}