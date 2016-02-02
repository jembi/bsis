package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.component.Component;
import model.component.ComponentStatus;
import model.componenttype.ComponentType;
import model.donation.Donation;
import model.user.User;
import org.apache.commons.lang3.StringUtils;
import utils.CustomDateFormatter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ComponentBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private Component component;

  private String createdOn;

  private String expiresOn;

  private List<String> componentTypes;

  public ComponentBackingForm() {
    setComponent(new Component());
  }

  public ComponentBackingForm(boolean autoGenerate) {
    setComponent(new Component());
  }

  public ComponentBackingForm(Component component) {
    this.setComponent(component);
  }

  public Long getId() {
    return component.getId();
  }

  public void setId(Long id) {
    component.setId(id);
  }

  public Donation getDonation() {
    return component.getDonation();
  }

  public void setDonation(Donation donation) {
    component.setDonation(donation);
  }

  public String getComponentType() {
    ComponentType componentType = component.getComponentType();
    if (componentType == null)
      return "";
    else
      return componentType.getId().toString();
  }

  public void setComponentType(String componentTypeId) {
    if (StringUtils.isBlank(componentTypeId)) {
      component.setComponentType(null);
    } else {
      ComponentType pt = new ComponentType();
      try {
        pt.setId(Long.parseLong(componentTypeId));
        component.setComponentType(pt);
      } catch (Exception ex) {
        ex.printStackTrace();
        component.setComponentType(null);
      }
    }
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return component.getLastUpdated();
  }

  public void setLastUpdated(Date lastUpdated) {
    component.setLastUpdated(lastUpdated);
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return component.getCreatedDate();
  }

  public void setCreatedDate(Date createdDate) {
    component.setCreatedDate(createdDate);
  }

  @JsonIgnore
  public User getCreatedBy() {
    return component.getCreatedBy();
  }

  public void setCreatedBy(User createdBy) {
    component.setCreatedBy(createdBy);
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return component.getLastUpdatedBy();
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    component.setLastUpdatedBy(lastUpdatedBy);
  }

  public String getNotes() {
    return component.getNotes();
  }

  public void setNotes(String notes) {
    component.setNotes(notes);
  }

  public Boolean getIsDeleted() {
    return component.getIsDeleted();
  }

  public void setIsDeleted(Boolean isDeleted) {
    component.setIsDeleted(isDeleted);
  }

  public int hashCode() {
    return component.hashCode();
  }

  public String getCreatedOn() {
    if (createdOn != null)
      return createdOn;
    if (getComponent() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(component.getCreatedOn());
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
    try {
      component.setCreatedOn(CustomDateFormatter.getDateTimeFromString(createdOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      component.setCreatedOn(null);
    }
  }

  public String getExpiresOn() {
    if (expiresOn != null)
      return expiresOn;
    if (getComponent() == null)
      return "";
    return CustomDateFormatter.getDateString(component.getExpiresOn());
  }

  public void setExpiresOn(String expiresOn) {
    this.expiresOn = expiresOn;
    try {
      component.setExpiresOn(CustomDateFormatter.getDateTimeFromString(expiresOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      component.setExpiresOn(null);
    }
  }

  public String toString() {
    return component.toString();
  }

  public List<String> getComponentTypes() {
    return componentTypes;
  }

  public void setComponentTypes(List<String> componentTypes) {
    this.componentTypes = componentTypes;
  }

  public String getDonationIdentificationNumber() {
    if (component == null || component.getDonation() == null ||
        component.getDonation().getDonationIdentificationNumber() == null
        )
      return "";
    return component.getDonation().getDonationIdentificationNumber();
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    Donation donation = new Donation();
    donation.setDonationIdentificationNumber(donationIdentificationNumber);
    component.setDonation(donation);
  }

  @JsonIgnore
  public Component getComponent() {
    return component;
  }

  private void setComponent(Component component) {
    this.component = component;
  }

  @JsonIgnore
  public String getStatus() {
    ComponentStatus status = component.getStatus();
    if (status == null)
      return "";
    else
      return component.getStatus().toString();
  }

  public void setStatus(String status) {
    component.setStatus(ComponentStatus.valueOf(status));
  }
}