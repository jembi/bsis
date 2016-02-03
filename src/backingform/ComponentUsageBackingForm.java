package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.component.Component;
import model.modificationtracker.RowModificationTracker;
import model.usage.ComponentUsage;
import model.user.User;
import utils.CustomDateFormatter;

public class ComponentUsageBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private ComponentUsage usage;

  private String usageDate;

  public ComponentUsageBackingForm() {
    usage = new ComponentUsage();
  }

  public ComponentUsageBackingForm(ComponentUsage usage) {
    this.usage = usage;
  }

  @JsonIgnore
  public ComponentUsage getUsage() {
    return usage;
  }

  public void setUsage(ComponentUsage usage) {
    this.usage = usage;
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return usage.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return usage.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return usage.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return usage.getLastUpdatedBy();
  }

  public Long getId() {
    return usage.getId();
  }

  public String getHospital() {
    return usage.getHospital();
  }

  public String getPatientName() {
    return usage.getPatientName();
  }

  public String getWard() {
    return usage.getWard();
  }

  public String getUseIndication() {
    return usage.getUseIndication();
  }

  public String getUsageDate() {
    if (usageDate != null)
      return usageDate;
    if (usage == null)
      return "";
    return CustomDateFormatter.getDateTimeString(usage.getUsageDate());
  }

  public String getNotes() {
    return usage.getNotes();
  }

  @JsonIgnore
  public Component getComponent() {
    return usage.getComponent();
  }

  @JsonIgnore
  public RowModificationTracker getModificationTracker() {
    return usage.getModificationTracker();
  }

  public Boolean getIsDeleted() {
    return usage.getIsDeleted();
  }

  public int hashCode() {
    return usage.hashCode();
  }

  public void setLastUpdated(Date lastUpdated) {
    usage.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    usage.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    usage.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    usage.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setId(Long id) {
    usage.setId(id);
  }

  public void setHospital(String hospital) {
    usage.setHospital(hospital);
  }

  public void setPatientName(String patientName) {
    usage.setPatientName(patientName);
  }

  public void setWard(String ward) {
    usage.setWard(ward);
  }

  public void setUseIndication(String useIndication) {
    usage.setUseIndication(useIndication);
  }

  public void setUsageDate(String usageDate) {
    this.usageDate = usageDate;
    try {
      usage.setUsageDate(CustomDateFormatter.getDateTimeFromString(usageDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      usage.setUsageDate(null);
    }
  }

  public void setNotes(String notes) {
    usage.setNotes(notes);
  }

  public void setComponent(Component component) {
    usage.setComponent(component);
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    usage.setModificationTracker(modificationTracker);
  }

  public void setIsDeleted(Boolean isDeleted) {
    usage.setIsDeleted(isDeleted);
  }

  public String getComponentId() {
    if (usage.getComponent() != null && usage.getComponent().getId() != null)
      return usage.getComponent().getId().toString();
    else
      return "-1";
  }

  public void setComponentId(String componentId) {
    Component component;
    try {
      component = new Component();
      component.setId(Long.parseLong(componentId));
      usage.setComponent(component);
    } catch (NumberFormatException ex) {
      usage.setComponent(null);
      ex.printStackTrace();
    }
  }
}