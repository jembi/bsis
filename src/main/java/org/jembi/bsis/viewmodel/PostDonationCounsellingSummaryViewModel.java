package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.util.Gender;

public class PostDonationCounsellingSummaryViewModel {

  private UUID id;
  private String counselled;
  private String referred;
  private Date counsellingDate;
  private String donorNumber;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;
  private String bloodGroup;
  private String donationIdentificationNumber;
  private Date donationDate;
  private LocationViewModel venue;
  private UUID donorId;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCounselled() {
    return counselled;
  }

  public void setCounselled(String counselled) {
    this.counselled = counselled;
  }

  public String getReferred() {
    return referred;
  }

  public void setReferred(String referred) {
    this.referred = referred;
  }

  public Date getCounsellingDate() {
    return counsellingDate;
  }

  public void setCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
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

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public String getBloodGroup() {
    return bloodGroup;
  }

  public void setBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public Date getDonationDate() {
    return donationDate;
  }

  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }

  public LocationViewModel getVenue() {
    return venue;
  }

  public void setVenue(LocationViewModel venue) {
    this.venue = venue;
  }

  public UUID getDonorId() {
    return donorId;
  }

  public void setDonorId(UUID donorId) {
    this.donorId = donorId;
  }

}
