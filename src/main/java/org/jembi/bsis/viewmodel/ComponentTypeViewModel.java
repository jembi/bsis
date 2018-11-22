package org.jembi.bsis.viewmodel;

import java.util.UUID;

public class ComponentTypeViewModel extends BaseViewModel<UUID> {

  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  private Integer maxBleedTime;
  private Integer maxTimeSinceDonation;
  
  public String getComponentTypeName() {
    return componentTypeName;
  }

  public String getComponentTypeCode() {
    return componentTypeCode;
  }

  public String getDescription() {
    return description;
  }

  public void setComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
  }

  public void setComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getMaxBleedTime() {
    return maxBleedTime;
  }

  public void setMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
  }

  public Integer getMaxTimeSinceDonation() {
    return maxTimeSinceDonation;
  }

  public void setMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
  }

}
