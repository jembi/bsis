package org.jembi.bsis.dto;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class DeferredDonorsDTO {
  
  private DeferralReason deferralReason;
  private Gender gender;
  private Location venue;
  private long count;

  public DeferredDonorsDTO() {
    // default constructor
  }
  
  public DeferredDonorsDTO(DeferralReason deferralReason, Gender gender, Location venue, long count) {
    super();
    this.deferralReason = deferralReason;
    this.gender = gender;
    this.venue = venue;
    this.count = count;
  }

  public DeferralReason getDeferralReason() {
    return deferralReason;
  }

  public void setDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }
}
