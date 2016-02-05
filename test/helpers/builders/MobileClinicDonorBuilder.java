package helpers.builders;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.MobileClinicDonorPersister;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.donor.DonorStatus;
import model.donor.MobileClinicDonor;
import model.location.Location;
import model.util.Gender;

public class MobileClinicDonorBuilder extends AbstractEntityBuilder<MobileClinicDonor> {

  private Long id;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private Boolean deleted;
  private DonorStatus donorStatus;
  private Location venue;

  public MobileClinicDonorBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public MobileClinicDonorBuilder withDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
    return this;
  }

  public MobileClinicDonorBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public MobileClinicDonorBuilder thatIsNotDeleted() {
    deleted = false;
    return this;
  }

  public MobileClinicDonorBuilder withDonorStatus(DonorStatus donorStatus) {
    this.donorStatus = donorStatus;
    return this;
  }

  public MobileClinicDonorBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  public MobileClinicDonorBuilder withFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public MobileClinicDonorBuilder withLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public MobileClinicDonorBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public MobileClinicDonorBuilder withBirthDate(Date birthDate) {
    this.birthDate = birthDate;
    return this;
  }

  public MobileClinicDonorBuilder withBirthDate(String dateOfBirth) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    this.birthDate = sdf.parse(dateOfBirth);
    return this;
  }


  @Override
  public MobileClinicDonor build() {
    MobileClinicDonor donor = new MobileClinicDonor();
    donor.setId(id);
    donor.setDonorNumber(donorNumber);
    donor.setFirstName(firstName);
    donor.setLastName(lastName);
    donor.setGender(gender);
    donor.setBirthDate(birthDate);
    donor.setIsDeleted(deleted);
    donor.setDonorStatus(donorStatus);
    donor.setVenue(venue);
    return donor;
  }

  @Override
  public AbstractEntityPersister<MobileClinicDonor> getPersister() {
    return new MobileClinicDonorPersister();
  }

  public static MobileClinicDonorBuilder aMobileClinicDonor() {
    return new MobileClinicDonorBuilder();
  }

}
