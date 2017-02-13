
package org.jembi.bsis.backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocationBackingForm {

  private Long id;
  private String name;
  private boolean isVenue;
  private boolean isMobileSite;
  private boolean isProcessingSite;
  private boolean isDistributionSite;
  private boolean isTestingSite;
  private boolean isUsageSite;
  private boolean isReferralSite;
  private boolean isDeleted;
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

  public boolean getIsUsageSite() {
    return isUsageSite;
  }

  public void setIsUsageSite(boolean isUsageSite) {
    this.isUsageSite = isUsageSite;
  }

  public boolean getIsMobileSite() {
    return isMobileSite;
  }

  public void setIsMobileSite(boolean isMobileSite) {
    this.isMobileSite = isMobileSite;
  }

  public boolean getIsVenue() {
    return isVenue;
  }

  public void setIsVenue(boolean isVenue) {
    this.isVenue = isVenue;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
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

  public boolean getIsReferralSite() {
    return isReferralSite;
  }

  public void setIsReferralSite(boolean isReferralSite) {
    this.isReferralSite = isReferralSite;
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