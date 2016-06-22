package org.jembi.bsis.dto;

import java.util.Objects;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class BloodTestResultDTO {
  
  private BloodTest bloodTest;
  private Gender gender;
  private long count;
  private Location venue;
  private String result;

  public BloodTestResultDTO() {
    // Default constructor
  }

  public BloodTestResultDTO(BloodTest bloodTest, String result, Gender gender, Location venue, long count) {
    this.bloodTest = bloodTest;
    this.result = result;
    this.gender = gender;
    this.venue = venue;
    this.count = count;
  }

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
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
  
  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof BloodTestResultDTO)) {
      return false;
    }

    BloodTestResultDTO other = (BloodTestResultDTO) obj;

    return Objects.equals(getBloodTest(), other.getBloodTest()) &&
        Objects.equals(getGender(), other.getGender()) &&
        Objects.equals(getCount(), other.getCount()) &&
        Objects.equals(getVenue(), other.getVenue()) &&
        Objects.equals(getResult(), other.getResult());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getBloodTest(), getGender(), getCount(), getVenue(), getResult());
  }

}
