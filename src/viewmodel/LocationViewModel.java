package viewmodel;

import java.util.List;

import model.Location;
import model.LocationType;

public class LocationViewModel {
    private Location location;
    private List<LocationType> allLocationTypes;

    public LocationViewModel(Location location, List<LocationType> allLocationTypes) {
        this.location = location;
        this.allLocationTypes = allLocationTypes;
    }

    public Long getLocationId() {
        return location.getLocationId();
    }

    public String getName() {
        return location.getName();
    }

    public Long getType() {
        return location.getType();
    }

    public String getTypeName() {
        return location.getType() == null ? "" : getLocationTypeName(location.getType(), allLocationTypes);
    }

    public String getCenter() {
        return location.getCenter().toString();
    }

    public String getCollectionSite() {
        return location.getCollectionSite().toString();
    }

    public String getUsageSite() {
        return location.getUsageSite().toString();
    }

    public String getMobileSite() {
        return location.getMobileSite().toString();
    }

    private String getLocationTypeName(Long typeId, List<LocationType> allLocationTypes) {
        for (LocationType type : allLocationTypes) {
            if(type.getLocationTypeId().equals(typeId)){
                return type.getName();
            }
        }
        return "";
    }
}
