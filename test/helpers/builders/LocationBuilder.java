package helpers.builders;

import model.location.Location;

public class LocationBuilder extends AbstractEntityBuilder<Location> {

  private Long id;
  private boolean venue;
  private String name;
  private boolean usageSite;
  private boolean processingSite;
  private boolean mobileSite;
  private boolean deleted;
  private String notes;

  public LocationBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public LocationBuilder thatIsVenue() {
    venue = true;
    return this;
  }

  private LocationBuilder thatIsProcessingSite() {
    processingSite = true;
    return this;
  }

  public LocationBuilder thatIsUsageSite() {
    usageSite = true;
    return this;
  }

  public LocationBuilder thatIsMobileSite() {
    mobileSite = true;
    return this;
  }

  public LocationBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public LocationBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public LocationBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  @Override
  public Location build() {
    Location location = new Location();
    location.setId(id);
    location.setName(name);
    location.setNotes(notes);
    location.setIsVenue(venue);
    location.setIsUsageSite(usageSite);
    location.setProcessingSite(processingSite);
    location.setIsMobileSite(mobileSite);
    location.setIsDeleted(deleted);
    return location;
  }

  public static LocationBuilder aLocation() {
    return new LocationBuilder();
  }

  public static LocationBuilder aVenue() {
    return new LocationBuilder().thatIsVenue();
  }
  
  public static LocationBuilder aProcessingSite() {
    return new LocationBuilder().thatIsProcessingSite();
  }

}
