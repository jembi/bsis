package helpers.builders;

import model.location.Location;

public class LocationBuilder extends AbstractEntityBuilder<Location> {
    
    private Long id;
    private boolean donorPanel;
    
    public LocationBuilder withId(long id) {
        this.id = id;
        return this;
    }
    
    public LocationBuilder thatIsDonorPanel() {
        donorPanel = true;
        return this;
    }

    @Override
    public Location build() {
        Location location = new Location();
        location.setId(id);
        location.setIsDonorPanel(donorPanel);
        return location;
    }
    
    public static LocationBuilder aLocation() {
        return new LocationBuilder();
    }
    
    public static LocationBuilder aDonorPanel() {
        return new LocationBuilder().thatIsDonorPanel();
    }

}
