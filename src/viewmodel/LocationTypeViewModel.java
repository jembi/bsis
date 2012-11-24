package viewmodel;

import model.location.LocationType;

public class LocationTypeViewModel {
	private LocationType locationType;

	public LocationTypeViewModel(LocationType locationType) {
		this.locationType = locationType;
	}

	public String getLocationTypeId() {
		return locationType.getId().toString();
	}

	public String getName() {
		return locationType.getName();
	}
}
