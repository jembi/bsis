package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.component.Component;
import model.component.ProductStatus;
import model.componenttype.ComponentTypeCombination;
import model.donation.Donation;
import model.user.User;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.CustomDateFormatter;

public class ComponentCombinationBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  @JsonIgnore
  private Component component;

  private String createdOn;

  private String expiresOn;

  private ComponentTypeCombination componentTypeCombination;

  Map<String, String> expiresOnByComponentTypeId;
  
  public ComponentCombinationBackingForm() {
    expiresOnByComponentTypeId = new HashMap<String, String>();
    setComponent(new Component());
  }

  public ComponentCombinationBackingForm(boolean autoGenerate) {
    expiresOnByComponentTypeId = new HashMap<String, String>();
    setComponent(new Component());
  }

  public ComponentCombinationBackingForm(Component component) {
    this.setComponent(component);
  }

  public Long getId() {
    return component.getId();
  }

  @JsonIgnore
  public Donation getDonation() {
    return component.getDonation();
  }
  
  @JsonIgnore
  public Date getLastUpdated() {
    return component.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return component.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return component.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return component.getLastUpdatedBy();
  }

  public String getNotes() {
    return component.getNotes();
  }

  public Boolean getIsDeleted() {
    return component.getIsDeleted();
  }

  public int hashCode() {
    return component.hashCode();
  }

  public void setId(Long id) {
    component.setId(id);
  }

  public void setDonation(Donation donation) {
    component.setDonation(donation);
  }

  public String getCreatedOn() {
    if (createdOn != null)
      return createdOn;
    if (getComponent() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(component.getCreatedOn());
  }

  public String getExpiresOn() {
    return expiresOn;
  }

  public void setLastUpdated(Date lastUpdated) {
    component.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    component.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    component.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    component.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    component.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    component.setIsDeleted(isDeleted);
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
    ProductStatus status = component.getStatus();
    if (status == null)
      return "";
    else
      return component.getStatus().toString();
  }

  public void setStatus(String status) {
    component.setStatus(ProductStatus.valueOf(status));
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
    }
    else {
      componentTypeCombination = new ComponentTypeCombination();
      try {
        componentTypeCombination.setId(Integer.parseInt(componentTypeCombinationId));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}