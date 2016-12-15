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

  public Date getProcessedOn() {
    return processedOn;
  }

  public void setProcessedOn(Date processedOn) {
    this.processedOn = processedOn;
  }

  @JsonIgnore
  public void setLastUpdated(Date lastUpdated) {
    // Ignore
  }

  @JsonIgnore
  public void setCreatedDate(Date createdDate) {
    // Ignore
  }

  @JsonIgnore
  public void setCreatedBy(User createdBy) {
    // Ignore
  }

  @JsonIgnore
  public void setLastUpdatedBy(User lastUpdatedBy) {
    // Ignore
  }
}
