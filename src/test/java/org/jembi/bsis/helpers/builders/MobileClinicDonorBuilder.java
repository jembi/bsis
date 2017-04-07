package org.jembi.bsis.helpers.builders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.util.Gender;

public class MobileClinicDonorBuilder extends AbstractBuilder<MobileClinicDonorDTO> {

  private UUID id;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private Boolean deleted;
  private DonorStatus donorStatus;
  private Location venue;
  private String bloodAbo;
  private String bloodRh;

  public MobileClinicDonorBuilder withId(UUID id) {
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

  public MobileClinicDonorBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public MobileClinicDonorBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  @Override
  public MobileClinicDonorDTO build() {
    MobileClinicDonorDTO donorDTO = new MobileClinicDonorDTO();
    donorDTO.setId(id);
    donorDTO.setDonorNumber(donorNumber);
    donorDTO.setFirstName(firstName);
    donorDTO.setLastName(lastName);
    donorDTO.setGender(gender);
    donorDTO.setBirthDate(birthDate);
    donorDTO.setIsDeleted(deleted);
    donorDTO.setDonorStatus(donorStatus);
    donorDTO.setVenue(venue);
    donorDTO.setBloodAbo(bloodAbo);
    donorDTO.setBloodRh(bloodRh);
    return donorDTO;
  }

  public static MobileClinicDonorBuilder aMobileClinicDonor() {
    return new MobileClinicDonorBuilder();
  }

}
