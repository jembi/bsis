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
	if (location.getName() == null)
	   return "";
    return location.getName();
  }
  
  public String getIsDeleted() {
	if (location.getIsDeleted() == null)
		return "";
    return location.getIsDeleted().toString();
  }

  public String getIsUsageSite() {
	if (location.getIsUsageSite() == null)
		return "";
    return location.getIsUsageSite().toString();
  }

  public String getIsMobileSite() {
	if (location.getIsMobileSite() == null)
		return ""; 
    return location.getIsMobileSite().toString();
  }

  public String getIsDonorPanel() {
	if (location.getIsDonorPanel() == null)
		return ""; 
    return location.getIsDonorPanel().toString();
  }
}
