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

	public String getIsCenter() {
		return location.getIsCenter().toString();
	}

	public String getIsCollectionSite() {
		return location.getIsCollectionSite().toString();
	}

	public String getIsUsageSite() {
		return location.getIsUsageSite().toString();
	}

	public String getIsMobileSite() {
		return location.getIsMobileSite().toString();
	}
}
