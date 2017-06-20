package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentViewModel extends BaseViewModel<UUID> {

  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private Date expiresOn;
  private String donationIdentificationNumber;
  private String donationFlagCharacters;
  private ComponentStatus status;
  private int daysToExpire;
  private String componentCode;
  private LocationViewModel location;

  public LocationViewModel getLocation () {
    return location;
  }

  public void setLocation (LocationViewModel location) {
    this.location = location;
  }

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
  }
  
  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getDonationFlagCharacters() {
    return donationFlagCharacters;
  }

  public void setDonationFlagCharacters(String donationFlagCharacters) {
    this.donationFlagCharacters = donationFlagCharacters;
  }

  public ComponentStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentStatus status) {
    this.status = status;
  }
  public int getDaysToExpire() {
    return daysToExpire;
  }

  public void setDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

}
