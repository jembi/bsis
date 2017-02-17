package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.model.user.User;

public class TransfusionBuilder extends AbstractEntityBuilder<Transfusion> {

  private Long id;
  private Date createdDate;
  private User createdBy;
  private Date lastUpdated;
  private User lastUpdatedBy;
  private String donationIdentificationNumber;
  private Patient patient;
  private String componentCode;
  private ComponentType componentType;
  private Location usageSite;
  private TransfusionReactionType transfusionReactionType;
  private TransfusionOutcome transfusionOutcome;
  private Date dateTransfused;  
  private Boolean isDeleted;
  
  public TransfusionBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public TransfusionBuilder withPatient(Patient patient) {
    this.patient = patient;
    return this;
  }

  public TransfusionBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public TransfusionBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }
  
  public TransfusionBuilder withComponentType(ComponentType componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public TransfusionBuilder withUsageSite(Location usageSite) {
    this.usageSite = usageSite;
    return this;
  }
  
  public TransfusionBuilder withTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
    return this;
  }

  public TransfusionBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public TransfusionBuilder thatIsNotDeleted() {
    isDeleted = false;
    return this;
  }

  public TransfusionBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public TransfusionBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public TransfusionBuilder withLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public TransfusionBuilder withLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

  public TransfusionBuilder withTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
    return this;
  }

  public TransfusionBuilder withDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
    return this;
  }  

  @Override
  public Transfusion build() {
    Transfusion transfusion = new Transfusion();
    transfusion.setId(id);
    transfusion.setDonationIdentificationNumber(donationIdentificationNumber);
    transfusion.setPatient(patient);
    transfusion.setComponentCode(componentCode);
    transfusion.setComponentType(componentType);
    transfusion.setUsageSite(usageSite);
    transfusion.setTransfusionReactionType(transfusionReactionType);
    transfusion.setTransfusionOutcome(transfusionOutcome);
    transfusion.setDateTransfused(dateTransfused);
    transfusion.setIsDeleted(isDeleted);    
    transfusion.setCreatedDate(createdDate);
    transfusion.setCreatedBy(createdBy);
    transfusion.setLastUpdated(lastUpdated);
    transfusion.setLastUpdatedBy(lastUpdatedBy);
    return transfusion;
  }

  public static TransfusionBuilder aTransfusion() {
    return new TransfusionBuilder();
  }
  
}