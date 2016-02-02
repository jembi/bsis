package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.componenttype.ComponentTypeCombination;
import model.user.User;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

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

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  @JsonIgnore
  public User getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

}
