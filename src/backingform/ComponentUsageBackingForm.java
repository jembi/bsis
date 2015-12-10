package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.component.Component;
import model.modificationtracker.RowModificationTracker;
import model.usage.ComponentUsage;
import model.user.User;
import utils.CustomDateFormatter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;

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

  public void setLastUpdated(Date lastUpdated) {
    usage.setLastUpdated(lastUpdated);
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return usage.getCreatedDate();
  }

  public void setCreatedDate(Date createdDate) {
    usage.setCreatedDate(createdDate);
  }

  @JsonIgnore
  public User getCreatedBy() {
    return usage.getCreatedBy();
  }

  public void setCreatedBy(User createdBy) {
    usage.setCreatedBy(createdBy);
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return usage.getLastUpdatedBy();
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    usage.setLastUpdatedBy(lastUpdatedBy);
  }

  public Long getId() {
    return usage.getId();
  }

  public void setId(Long id) {
    usage.setId(id);
  }

  public String getHospital() {
    return usage.getHospital();
  }

  public void setHospital(String hospital) {
    usage.setHospital(hospital);
  }

  public String getPatientName() {
    return usage.getPatientName();
  }

  public void setPatientName(String patientName) {
    usage.setPatientName(patientName);
  }

  public String getWard() {
    return usage.getWard();
  }

  public void setWard(String ward) {
    usage.setWard(ward);
  }

  public String getUseIndication() {
    return usage.getUseIndication();
  }

  public void setUseIndication(String useIndication) {
    usage.setUseIndication(useIndication);
  }

  public String getUsageDate() {
    if (usageDate != null)
      return usageDate;
    if (usage == null)
      return "";
    return CustomDateFormatter.getDateTimeString(usage.getUsageDate());
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

  public String getNotes() {
    return usage.getNotes();
  }

  public void setNotes(String notes) {
    usage.setNotes(notes);
  }

  @JsonIgnore
  public Component getComponent() {
    return usage.getComponent();
  }

  public void setComponent(Component component) {
    usage.setComponent(component);
  }

  @JsonIgnore
  public RowModificationTracker getModificationTracker() {
    return usage.getModificationTracker();
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    usage.setModificationTracker(modificationTracker);
  }

  public Boolean getIsDeleted() {
    return usage.getIsDeleted();
  }

  public void setIsDeleted(Boolean isDeleted) {
    usage.setIsDeleted(isDeleted);
  }

  public int hashCode() {
    return usage.hashCode();
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