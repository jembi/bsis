package org.jembi.bsis.dto;

import java.util.Objects;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class BloodTestTotalDTO {
  
  private Gender gender;
  private long total;
  private Location venue;

  public BloodTestTotalDTO() {
    // Default constructor
  }

  public BloodTestTotalDTO(Gender gender, Location venue, long total) {
    this.gender = gender;
    this.venue = venue;
    this.total = total;
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

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof BloodTestTotalDTO)) {
      return false;
    }

    BloodTestTotalDTO other = (BloodTestTotalDTO) obj;

    return Objects.equals(getGender(), other.getGender()) &&
        Objects.equals(getTotal(), other.getTotal()) &&
        Objects.equals(getVenue(), other.getVenue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getGender(), getTotal(), getVenue());
  }

}
