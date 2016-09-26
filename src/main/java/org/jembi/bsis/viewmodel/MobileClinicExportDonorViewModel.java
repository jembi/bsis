package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.location.Location;

public class MobileClinicExportDonorViewModel {

  private Long id;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private String gender;
  private String bloodType;
  private DonorStatus donorStatus;
  private String birthDate;
  private LocationFullViewModel venue;
  private Boolean isDeleted;
  private Boolean eligibility;
  
  public MobileClinicExportDonorViewModel() {
    super();
  }

  public MobileClinicExportDonorViewModel(Long id, String donorNumber, String firstName, String lastName, String gender,
      String bloodType, DonorStatus donorStatus, String birthDate, LocationFullViewModel venue, Boolean isDeleted,
      Boolean eligibility) {
    super();
    this.id = id;
    this.donorNumber = donorNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.bloodType = bloodType;
    this.donorStatus = donorStatus;
    this.birthDate = birthDate;
    this.venue = venue;
    this.isDeleted = isDeleted;
    this.eligibility = eligibility;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
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
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getBloodType() {
    return bloodType;
  }

  public void setBloodType(String bloodType) {
    this.bloodType = bloodType;
  }

  public DonorStatus getDonorStatus() {
    return donorStatus;
  }

  public void setDonorStatus(DonorStatus donorStatus) {
    this.donorStatus = donorStatus;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
  }

  public LocationFullViewModel getVenue() {
    return venue;
  }

  public void setVenue(LocationFullViewModel venue) {
    this.venue = venue;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Boolean getEligibility() {
    return eligibility;
  }

  public void setEligibility(Boolean eligibility) {
    this.eligibility = eligibility;
  }
}