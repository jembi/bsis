package helpers.builders;

import model.location.Location;

public class LocationBuilder extends AbstractEntityBuilder<Location> {
    
    private Long id;
    private boolean venue;
    private String name;
    
    public LocationBuilder withId(long id) {
        this.id = id;
        return this;
    }
    
    public LocationBuilder thatIsVenue() {
        venue = true;
        return this;
    }
    
    public LocationBuilder withName(String name) {
    	this.name = name;
    	return this;
    }

    @Override
    public Location build() {
        Location location = new Location();
        location.setId(id);
        location.setName(name);
        location.setIsVenue(venue);
        return location;
    }
    
    public static LocationBuilder aLocation() {
        return new LocationBuilder();
    }
    
    public static LocationBuilder aVenue() {
        return new LocationBuilder().thatIsVenue();
    }

}
