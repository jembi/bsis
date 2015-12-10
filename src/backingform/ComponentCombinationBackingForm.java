package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.component.Component;
import model.component.ComponentStatus;
import model.componenttype.ComponentTypeCombination;
import model.donation.Donation;
import model.user.User;
import org.apache.commons.lang3.StringUtils;
import utils.CustomDateFormatter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ComponentCombinationBackingForm {

  public static final int ID_LENGTH = 12;
  Map<String, String> expiresOnByComponentTypeId;
  @NotNull
  @Valid
  @JsonIgnore
  private Component component;
  private String createdOn;
  private String expiresOn;
  private ComponentTypeCombination componentTypeCombination;

  public ComponentCombinationBackingForm() {
    expiresOnByComponentTypeId = new HashMap<>();
    setComponent(new Component());
  }

  public ComponentCombinationBackingForm(boolean autoGenerate) {
    expiresOnByComponentTypeId = new HashMap<>();
    setComponent(new Component());
  }

  public ComponentCombinationBackingForm(Component component) {
    this.setComponent(component);
  }

  public Long getId() {
    return component.getId();
  }

  public void setId(Long id) {
    component.setId(id);
  }

  @JsonIgnore
  public Donation getDonation() {
    return component.getDonation();
  }

  public void setDonation(Donation donation) {
    component.setDonation(donation);
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
    return expiresOn;
  }

  @SuppressWarnings("unchecked")
  public void setExpiresOn(String expiresOn) {
    this.expiresOn = expiresOn;
    ObjectMapper mapper = new ObjectMapper();
    try {
      expiresOnByComponentTypeId = mapper.readValue(expiresOn, HashMap.class);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String toString() {
    return component.toString();
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

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

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

  public String getComponentTypeCombination() {
    if (componentTypeCombination == null || componentTypeCombination.getId() == null)
      return "";
    else
      return componentTypeCombination.getId().toString();
  }

  public void setComponentTypeCombination(String componentTypeCombinationId) {
    if (StringUtils.isBlank(componentTypeCombinationId)) {
      componentTypeCombination = null;
    } else {
      componentTypeCombination = new ComponentTypeCombination();
      try {
        componentTypeCombination.setId(Integer.parseInt(componentTypeCombinationId));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}