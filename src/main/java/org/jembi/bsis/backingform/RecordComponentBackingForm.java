package org.jembi.bsis.backingform;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.user.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RecordComponentBackingForm {

  @NotBlank
  private Long parentComponentId;

  @NotBlank
  private ComponentTypeCombination componentTypeCombination;

  private Integer numUnits;

  @JsonIgnore
  private Date createdDate;
  @JsonIgnore
  private Date lastUpdated;
  @JsonIgnore
  private User lastUpdatedBy;
  @JsonIgnore
  private User createdBy;
  private Date processedOn;

  public Long getParentComponentId() {
    return parentComponentId;
  }

  public void setParentComponentId(Long parentComponentId) {
    this.parentComponentId = parentComponentId;
  }

  public ComponentTypeCombination getComponentTypeCombination() {
    return componentTypeCombination;
  }

  public void setComponentTypeCombination(ComponentTypeCombination componentTypeCombination) {
    this.componentTypeCombination = componentTypeCombination;
  }

  public Integer getNumUnits() {
    return numUnits;
  }

  public void setNumUnits(Integer numUnits) {
    this.numUnits = numUnits;
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return lastUpdated;
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return createdDate;
  }

  @JsonIgnore
  public User getCreatedBy() {
    return createdBy;
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public Date getProcessedOn() {
    return processedOn;
  }

  public void setProcessedOn(Date processedOn) {
    this.processedOn = processedOn;
  }
}
