package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.user.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RecordComponentBackingForm {

  private UUID parentComponentId;

  private ComponentTypeCombinationBackingForm componentTypeCombination;

  private Date processedOn;

  public UUID getParentComponentId() {
    return parentComponentId;
  }

  public void setParentComponentId(UUID parentComponentId) {
    this.parentComponentId = parentComponentId;
  }

  public ComponentTypeCombinationBackingForm getComponentTypeCombination() {
    return componentTypeCombination;
  }

  public void setComponentTypeCombination(ComponentTypeCombinationBackingForm componentTypeCombination) {
    this.componentTypeCombination = componentTypeCombination;
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
