package valueobject;

import model.donationtype.DonationType;
import model.location.Location;
import model.util.Gender;

import java.util.Objects;

public class CollectedDonationValueObject {

  private DonationType donationType;
  private Gender gender;
  private String bloodAbo;
  private String bloodRh;
  private long count;
  private Location venue;

  public CollectedDonationValueObject() {
    // Default constructor
  }

  public CollectedDonationValueObject(DonationType donationType, Gender gender, String bloodAbo, String bloodRh,
                                      Location venue, long count) {
    this.donationType = donationType;
    this.gender = gender;
    this.bloodAbo = bloodAbo;
    this.bloodRh = bloodRh;
    this.venue = venue;
    this.count = count;
  }

  public DonationType getDonationType() {
    return donationType;
  }

  public void setDonationType(DonationType donationType) {
    this.donationType = donationType;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof CollectedDonationValueObject)) {
      return false;
    }

    CollectedDonationValueObject other = (CollectedDonationValueObject) obj;

    return Objects.equals(getDonationType(), other.getDonationType()) &&
            Objects.equals(getGender(), other.getGender()) &&
            Objects.equals(getBloodAbo(), other.getBloodAbo()) &&
            Objects.equals(getBloodRh(), other.getBloodRh()) &&
            Objects.equals(getCount(), other.getCount()) &&
            Objects.equals(getVenue(), other.getVenue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDonationType(), getGender(), getBloodAbo(), getBloodRh(), getCount(), getVenue());
  }

}
