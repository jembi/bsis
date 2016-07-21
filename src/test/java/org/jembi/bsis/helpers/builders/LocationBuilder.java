package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.location.Location;

public class LocationBuilder extends AbstractEntityBuilder<Location> {

  private Long id;
  private boolean venue;
  private String name = "location";
  private boolean usageSite;
  private boolean processingSite;
  private boolean mobileSite;
  private boolean distributionSite;
  private boolean testingSite;
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

  public LocationBuilder thatIsProcessingSite() {
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

  public LocationBuilder thatIsDistributionSite() {
    distributionSite = true;
    return this;
  }
  
  public LocationBuilder thatIsTestingSite() {
    testingSite = true;
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
    location.setIsProcessingSite(processingSite);
    location.setIsMobileSite(mobileSite);
    location.setIsDistributionSite(distributionSite);
    location.setIsDeleted(deleted);
    location.setIsTestingSite(testingSite);
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

  public static LocationBuilder aDistributionSite() {
    return new LocationBuilder().thatIsDistributionSite();
  }

  public static LocationBuilder aUsageSite() {
    return new LocationBuilder().thatIsUsageSite();
  }

  public static LocationBuilder aTestingSite() {
    return new LocationBuilder().thatIsTestingSite();
  }
}
