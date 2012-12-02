package viewmodel;

import java.util.List;

import model.location.Location;
import model.location.LocationType;

public class LocationViewModel {
	private Location location;
	private List<LocationType> allLocationTypes;

	public LocationViewModel(Location location,
			List<LocationType> allLocationTypes) {
		this.location = location;
		this.allLocationTypes = allLocationTypes;
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

	private String getLocationTypeName(Long typeId,
			List<LocationType> allLocationTypes) {
		for (LocationType type : allLocationTypes) {
			if (type.getId().equals(typeId)) {
				return type.getName();
			}
		}
		return "";
	}
}
