package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.location.Location;

public class LocationViewModel extends BaseViewModel {

  @JsonIgnore
  private Location location;

  public LocationViewModel(Location location) {
    this.location = location;
  }

  @Override
  public Long getId() {
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

}
