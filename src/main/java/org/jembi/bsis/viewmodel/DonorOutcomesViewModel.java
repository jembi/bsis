package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.List;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DonorOutcomesViewModel {
  
  private String donorNumber;
  private String lastName;
  private String firstName;
  private Gender gender;
  private Date birthDate;
  private Date donationDate;
  private String donationIdentificationNumber;
  private String bloodAbo;
  private String bloodRh;
  private List<BloodTestResultFullViewModel> bloodTestResults;
  
  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDate() {
    return donationDate;
  }

  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
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

  public List<BloodTestResultFullViewModel> getBloodTestResults() {
    return bloodTestResults;
  }

  public void setBloodTestResults(List<BloodTestResultFullViewModel> bloodTestResults) {
    this.bloodTestResults = bloodTestResults;
  }

}
