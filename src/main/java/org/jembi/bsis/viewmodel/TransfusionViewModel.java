package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransfusionViewModel extends BaseViewModel<UUID> {

  private String donationIdentificationNumber;

  private String componentCode;

  private String componentType;

  private LocationViewModel receivedFrom;

  private TransfusionOutcome transfusionOutcome;

  private Date dateTransfused;

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public String getComponentType() {
    return componentType;
  }

  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  public LocationViewModel getReceivedFrom() {
    return receivedFrom;
  }

  public void setReceivedFrom(LocationViewModel receivedFrom) {
    this.receivedFrom = receivedFrom;
  }

  public TransfusionOutcome getTransfusionOutcome() {
    return transfusionOutcome;
  }

  public void setTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDateTransfused() {
    return dateTransfused;
  }

  public void setDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
  }
}
