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

  public String getIsCollectionCenter() {
    return location.getIsCollectionCenter()!=null? location.getIsCollectionCenter().toString(): "";
  }

  public String getIsCollectionSite() {
    return location.getIsCollectionSite()!=null?location.getIsCollectionSite().toString():"";
  }

  public String getIsUsageSite() {
    return location.getIsUsageSite()? location.getIsUsageSite().toString(): "";
  }

  public String getIsMobileSite() {
    return location.getIsMobileSite()!=null? location.getIsMobileSite().toString(): "";
  }

  public String getIsDonorPanel() {
    return location.getIsDonorPanel()!=null?location.getIsDonorPanel().toString():"";
  }
  
  public String getIsCurrentLocation() {
    return location.getIsCurrentLocation()!=null?location.getIsCurrentLocation().toString():"";
  }
  
}
