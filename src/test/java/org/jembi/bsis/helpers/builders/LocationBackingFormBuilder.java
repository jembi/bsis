package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.DivisionBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;

public class LocationBackingFormBuilder extends AbstractBuilder<LocationBackingForm> {

  private UUID id;
  private boolean venue;
  private String name;
  private boolean usageSite;
  private boolean processingSite;
  private boolean mobileSite;
  private boolean distributionSite;
  private boolean referralSite;
  private boolean deleted;
  private String notes;
  private boolean testingSite;
  private DivisionBackingForm divisionLevel3;

  public LocationBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public LocationBackingFormBuilder thatIsVenue() {
    venue = true;
    return this;
  }

  public LocationBackingFormBuilder thatIsProcessingSite() {
    processingSite = true;
    return this;
  }

  public LocationBackingFormBuilder thatIsUsageSite() {
    usageSite = true;
    return this;
  }

  public LocationBackingFormBuilder thatIsMobileSite() {
    mobileSite = true;
    return this;
  }

  public LocationBackingFormBuilder thatIsDistributionSite() {
    distributionSite = true;
    return this;
  }

  public LocationBackingFormBuilder thatIsReferralSite() {
    referralSite = true;
    return this;
  }

  public LocationBackingFormBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public LocationBackingFormBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public LocationBackingFormBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public LocationBackingFormBuilder thatIsTestingSite() {
    testingSite = true;
    return this;
  }
  
  public LocationBackingFormBuilder withDivisionLevel3(DivisionBackingForm divisionLevel3) {
    this.divisionLevel3 = divisionLevel3;
    return this;
  }

  @Override
  public LocationBackingForm build() {
    LocationBackingForm location = new LocationBackingForm();
    location.setId(id);
    location.setName(name);
    location.setNotes(notes);
    location.setVenue(venue);
    location.setDeleted(deleted);
    location.setUsageSite(usageSite);
    location.setMobileSite(mobileSite);
    location.setTestingSite(testingSite);
    location.setReferralSite(referralSite);
    location.setProcessingSite(processingSite);
    location.setDistributionSite(distributionSite);
    location.setDivisionLevel3(divisionLevel3);
    return location;
  }

  public static LocationBackingFormBuilder aLocationBackingForm() {
    return new LocationBackingFormBuilder();
  }

  public static LocationBackingFormBuilder aVenueBackingForm() {
    return new LocationBackingFormBuilder().thatIsVenue();
  }
  
  public static LocationBackingFormBuilder aProcessingSiteBackingForm() {
    return new LocationBackingFormBuilder().thatIsProcessingSite();
  }

  public static LocationBackingFormBuilder aDistributionSiteBackingForm() {
    return new LocationBackingFormBuilder().thatIsDistributionSite();
  }

  public static LocationBackingFormBuilder aUsageSiteBackingForm() {
    return new LocationBackingFormBuilder().thatIsUsageSite();
  }

  public static LocationBackingFormBuilder aTestingSiteBackingForm() {
    return new LocationBackingFormBuilder().thatIsTestingSite();
  }

  public static LocationBackingFormBuilder aReferralSiteBackingForm() {
    return new LocationBackingFormBuilder().thatIsReferralSite();
  }
}
