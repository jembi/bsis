
package org.jembi.bsis.backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocationBackingForm {

  private Long id;
  private String name;
  private Boolean isVenue;
  private Boolean isMobileSite;
  private Boolean isProcessingSite;
  private Boolean isDistributionSite;
  private Boolean isTestingSite;
  private Boolean isUsageSite;
  private Boolean isDeleted;
  private String notes;
  private DivisionBackingForm divisionLevel3;

  public LocationBackingForm() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getIsUsageSite() {
    return isUsageSite;
  }

  public void setIsUsageSite(Boolean isUsageSite) {
    this.isUsageSite = isUsageSite;
  }

  public Boolean getIsMobileSite() {
    return isMobileSite;
  }

  public void setIsMobileSite(Boolean isMobileSite) {
    this.isMobileSite = isMobileSite;
  }

  public Boolean getIsVenue() {
    return isVenue;
  }

  public void setIsVenue(Boolean isVenue) {
    this.isVenue = isVenue;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
  
  public boolean getIsProcessingSite() {
    return isProcessingSite;
  }
  
  public void setIsProcessingSite(boolean isProcessingSite) {
    this.isProcessingSite = isProcessingSite;
  }
  
  public boolean getIsDistributionSite() {
    return isDistributionSite;
  }
  
  public void setIsDistributionSite(boolean isDistributionSite) {
    this.isDistributionSite = isDistributionSite;
  }

  public boolean getIsTestingSite() {
    return isTestingSite;
  }
  
  public void setIsTestingSite(boolean isTestingSite) {
    this.isTestingSite = isTestingSite;
  }

  public void setDivisionLevel3(DivisionBackingForm divisionLevel3) {
    this.divisionLevel3 = divisionLevel3;
  }
  
  public DivisionBackingForm getDivisionLevel3() {
    return divisionLevel3;
  }

  @JsonIgnore
  public void setDivisionLevel1(DivisionBackingForm divisionLevel1) {
    // ignore
  }

  @JsonIgnore
  public void setDivisionLevel2(DivisionBackingForm divisionLevel2) {
    // ignore
  }

}