package org.jembi.bsis.viewmodel;

import java.util.UUID;

import org.jembi.bsis.model.location.Location;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocationFullViewModel extends BaseViewModel<UUID> {

  @JsonIgnore
  private Location location;

  private DivisionViewModel divisionLevel1;
  private DivisionViewModel divisionLevel2;
  private DivisionViewModel divisionLevel3;

  public LocationFullViewModel(Location location) {
    this.location = location;
  }

  @Override
  public UUID getId() {
    return location.getId();
  }

  public String getName() {
    return location.getName();
  }

  public boolean getIsDeleted() {
    return location.getIsDeleted();
  }

  public boolean getIsUsageSite() {
    return location.getIsUsageSite();
  }

  public boolean getIsMobileSite() {
    return location.getIsMobileSite();
  }

  public boolean getIsVenue() {
    return location.getIsVenue();
  }
  
  public boolean getIsProcessingSite() {
    return location.getIsProcessingSite();
  }
  
  public boolean getIsDistributionSite() {
    return location.getIsDistributionSite();
  }

  public boolean getIsTestingSite() {
    return location.getIsTestingSite();
  }
  
  public boolean getIsReferralSite() {
    return location.getIsReferralSite();
  }

  public void setDivisionLevel1(DivisionViewModel divisionLevel1) {
    this.divisionLevel1 = divisionLevel1;
  }

  public void setDivisionLevel2(DivisionViewModel divisionLevel2) {
    this.divisionLevel2 = divisionLevel2;
  }

  public void setDivisionLevel3(DivisionViewModel divisionLevel3) {
    this.divisionLevel3 = divisionLevel3;
  }

  public DivisionViewModel getDivisionLevel1() {
    return divisionLevel1;
  }

  public DivisionViewModel getDivisionLevel2() {
    return divisionLevel2;
  }

  public DivisionViewModel getDivisionLevel3() {
    return divisionLevel3;
  }

}
