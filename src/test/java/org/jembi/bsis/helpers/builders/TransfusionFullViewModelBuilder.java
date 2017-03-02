package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;
import org.jembi.bsis.viewmodel.TransfusionFullViewModel;

import java.util.Date;

public class TransfusionFullViewModelBuilder extends AbstractBuilder<TransfusionFullViewModel> {

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
  
  public TransfusionFullViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TransfusionFullViewModelBuilder withPatient(PatientViewModel patient) {
    this.patient = patient;
    return this;
  }

  public TransfusionFullViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public TransfusionFullViewModelBuilder withComponent(ComponentViewModel component) {
    this.component = component;
    return this;
  }
  
  public TransfusionFullViewModelBuilder withUsageSite(LocationViewModel usageSite) {
    this.usageSite = usageSite;
    return this;
  }
  
  public TransfusionFullViewModelBuilder withTransfusionReactionType(TransfusionReactionTypeViewModel transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
    return this;
  }

  public TransfusionFullViewModelBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public TransfusionFullViewModelBuilder thatIsNotDeleted() {
    isDeleted = false;
    return this;
  }

  public TransfusionFullViewModelBuilder withTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
    return this;
  }

  public TransfusionFullViewModelBuilder withDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
    return this;
  }

  public TransfusionFullViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @Override
  public TransfusionFullViewModel build() {
    TransfusionFullViewModel transfusionViewModel = new TransfusionFullViewModel();
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

  public static TransfusionFullViewModelBuilder aTransfusionFullViewModel() {
    return new TransfusionFullViewModelBuilder();
  }
  
}