package viewmodel;

import model.location.Location;

public class LocationViewModel {

  private Location location;

  public LocationViewModel(Location location) {
    this.location = location;
  }

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
    return location.isProcessingSite();
  }
  
  public boolean getIsDistributionSite() {
    return location.isDistributionSite();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LocationViewModel other = (LocationViewModel) obj;
    if (location == null) {
      if (other.location != null)
        return false;
    } else if (!location.equals(other.location))
      return false;
    return true;
  }

}
