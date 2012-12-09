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

	public String getCenter() {
		return location.getIsCenter().toString();
	}

	public String getCollectionSite() {
		return location.getIsCollectionSite().toString();
	}

	public String getUsageSite() {
		return location.getIsUsageSite().toString();
	}

	public String getMobileSite() {
		return location.getIsMobileSite().toString();
	}
}
