package viewmodel;

import java.util.Arrays;
import java.util.Date;

import model.component.Component;
import model.component.ComponentStatus;
import model.donation.Donation;
import model.user.User;

import org.joda.time.DateTime;
import org.joda.time.Days;

import utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentViewModel {

  @JsonIgnore
  private Component component;

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

  @JsonIgnore
  public Donation getDonation() {
    return component.getDonation();
  }

  public ComponentTypeViewModel getComponentType() {
    return new ComponentTypeViewModel(component.getComponentType());
  }

  public String getNotes() {
    return component.getNotes();
  }

  public Boolean getIsDeleted() {
    return component.getIsDeleted();
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

  public String getDonationID() {
    if (getComponent() == null || getComponent().getDonation() == null ||
        getComponent().getDonation().getId() == null
        )
      return "";
    return getComponent().getDonation().getId().toString();
  }
  
  public PackTypeBasicViewModel getPackType() {
    if (component.getDonation() == null || component.getDonation().getPackType() == null) {
      return null;
    }
    return new PackTypeBasicViewModel(component.getDonation().getPackType());
  }

  public String getAge() {
    DateTime today = new DateTime();
    DateTime createdOn = new DateTime(component.getCreatedOn().getTime());
    Long age = (long) Days.daysBetween(createdOn, today).getDays();
    return age + " days old";
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

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(component.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(component.getCreatedDate());
  }

  @JsonIgnore
  public String getCreatedBy() {
    User user = component.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  @JsonIgnore
  public String getLastUpdatedBy() {
    User user = component.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getIssuedOn() {
    return CustomDateFormatter.getDateTimeString(component.getIssuedOn());
  }

  @JsonIgnore
  public RequestViewModel getIssuedTo() {
    ComponentStatus status = component.getStatus();
    if (status == null)
      return null;
    else if (!status.equals(ComponentStatus.ISSUED))
      return null;
    else
      return new RequestViewModel(component.getIssuedTo());
  }

  public String getDiscardedOn() {
    return CustomDateFormatter.getDateTimeString(component.getDiscardedOn());
  }

  @JsonIgnore
  public String getBloodGroup() {
    if (component == null || component.getDonation() == null ||
        component.getDonation().getDonationIdentificationNumber() == null
        )
      return "";
    DonationViewModel donationViewModel = new DonationViewModel(component.getDonation());
    return donationViewModel.getBloodGroup();
  }

  public String getSubdivisionCode() {
    return component.getSubdivisionCode();
  }

  public Boolean getStatusAllowsSplitting() {
    return Arrays.asList(ComponentStatus.AVAILABLE, ComponentStatus.QUARANTINED)
        .contains(component.getStatus());
  }

  public String getComponentIdentificationNumber() {
    return component.getComponentIdentificationNumber();
  }

}
