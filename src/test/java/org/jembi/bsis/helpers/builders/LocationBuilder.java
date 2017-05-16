package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.model.location.Location;

public class LocationBuilder extends AbstractEntityBuilder<Location> {

  private UUID id;
  private boolean venue;
  private String name = "location";
  private boolean usageSite;
  private boolean processingSite;
  private boolean mobileSite;
  private boolean distributionSite;
  private boolean testingSite;
  private boolean referralSite;
  private boolean deleted;
  private String notes;
  private Division divisionLevel1;
  private Division divisionLevel2;
  private Division divisionLevel3;

  public LocationBuilder withId(UUID id) {
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
  
  public LocationBuilder thatIsReferralSite() {
    referralSite = true;
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

  public LocationBuilder withDivisionLevel1(Division divisionLevel1) {
    this.divisionLevel1 = divisionLevel1;
    return this;
  }

  public LocationBuilder withDivisionLevel2(Division divisionLevel2) {
    this.divisionLevel2 = divisionLevel2;
    return this;
  }

  public LocationBuilder withDivisionLevel3(Division divisionLevel3) {
    this.divisionLevel3 = divisionLevel3;
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
    location.setIsReferralSite(referralSite);
    location.setDivisionLevel1(divisionLevel1);
    location.setDivisionLevel2(divisionLevel2);
    location.setDivisionLevel3(divisionLevel3);
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
  
  public static LocationBuilder aReferralSite() {
    return new LocationBuilder().thatIsReferralSite();
  }
}
