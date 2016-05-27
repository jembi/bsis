package viewmodel;

import java.util.Date;

import model.component.Component;
import model.inventory.InventoryStatus;

import org.joda.time.DateTime;
import org.joda.time.Days;

import utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentViewModel {

  @JsonIgnore
  private Component component;
  
  private LocationViewModel location;

  public ComponentViewModel() {
  }

  public ComponentViewModel(Component component) {
    this.component = component;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public Long getId() {
    return component.getId();
  }

  public ComponentTypeViewModel getComponentType() {
    // FIXME: use factory
    return new ComponentTypeFullViewModel(component.getComponentType());
  }

  public String getNotes() {
    return component.getNotes();
  }

  public String getCreatedOn() {
    if (component.getCreatedOn() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(component.getCreatedOn());
  }

  public String getExpiresOn() {
    if (component.getExpiresOn() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(component.getExpiresOn());
  }

  public String getDonationIdentificationNumber() {
    if (getComponent() == null || getComponent().getDonation() == null ||
        getComponent().getDonation().getDonationIdentificationNumber() == null
        )
      return "";
    return getComponent().getDonation().getDonationIdentificationNumber();
  }
  
  public PackTypeViewModel getPackType() {
    if (component.getDonation() == null || component.getDonation().getPackType() == null) {
      return null;
    }
    return new PackTypeViewModel(component.getDonation().getPackType());
  }

  public String getStatus() {
    return component.getStatus().toString();
  }

  public String getExpiryStatus() {
    Date today = new Date();
    if (today.equals(component.getExpiresOn()) || today.before(component.getExpiresOn())) {
      DateTime expiresOn = new DateTime(component.getExpiresOn().getTime());
      Long age = (long) Days.daysBetween(expiresOn, new DateTime()).getDays();
      return Math.abs(age) + " days to expire";
    } else {
      return "Already expired";
    }
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(component.getCreatedDate());
  }

  public String getIssuedOn() {
    return CustomDateFormatter.getDateTimeString(component.getIssuedOn());
  }

  public String getDiscardedOn() {
    return CustomDateFormatter.getDateTimeString(component.getDiscardedOn());
  }
  
  public String getBloodAbo() {
    return component.getDonation().getBloodAbo();
  }
  
  public String getBloodRh() {
    return component.getDonation().getBloodRh();
  }

  public String getComponentCode() {
    return component.getComponentCode();
  }
  
  public InventoryStatus getInventoryStatus() {
    return component.getInventoryStatus();
  }
  
  public void setLocation(LocationViewModel location) {
    this.location = location;
  }

  public LocationViewModel getLocation() {
    return location;
  }
}
