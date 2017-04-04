package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.utils.CustomDateFormatter;

/**
 * View model representing a summarised view of a donor.
 */
public class DonorSummaryViewModel {

  private UUID id;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private String donorNumber;
  private String venueName;

  public DonorSummaryViewModel() {
    // no-args constructor
  }

  /**
   * Do not remove or change the signature of this method.
   *
   * @see {@link DonorRepository#findDonorSummaryByDonorNumber(String)}
   */
  public DonorSummaryViewModel(UUID id, String firstName, String lastName, Gender gender, Date birthDate) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.birthDate = birthDate;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getGender() {
    return gender == null ? "" : gender.name();
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getBirthDate() {
    return CustomDateFormatter.getDateString(birthDate);
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getVenueName() {
    return venueName;
  }

  public void setVenueName(String venueName) {
    this.venueName = venueName;
  }

}
