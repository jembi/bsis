package org.jembi.bsis.backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.user.User;

public class RecordComponentBackingForm {

  @NotBlank
  private String parentComponentId;

	/*
  @NotBlank
    private String childComponentTypeId;
    */

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

  public String getParentComponentId() {
    return parentComponentId;
  }

  public void setParentComponentId(String parentComponentId) {
    this.parentComponentId = parentComponentId;
  }

	/*
  public String getChildComponentTypeId() {
        return childComponentTypeId;
    }
	
	public void setChildComponentTypeId(String childComponentTypeId){
		this.childComponentTypeId = childComponentTypeId;
	}
	*/

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

}
