package helpers.builders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.donor.DonorStatus;
import model.location.Location;
import model.util.Gender;
import dto.MobileClinicDonorDTO;

public class MobileClinicDonorBuilder extends AbstractBuilder<MobileClinicDonorDTO> {

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
    return donorDTO;
  }

  public static MobileClinicDonorBuilder aMobileClinicDonor() {
    return new MobileClinicDonorBuilder();
  }

}
