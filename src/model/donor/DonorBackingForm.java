package model.donor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.CustomDateFormatter;
import model.address.ContactInformation;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;
import model.util.Gender;
import viewmodel.donor.DonorViewModel;

public class DonorBackingForm {

  @NotNull
  @Valid
  private Donor donor;
  private List<String> bloodTypes;

  private String birthDate;

  public DonorBackingForm() {
    donor = new Donor();
  }

  public DonorBackingForm(Donor donor) {
    this.donor = donor;
  }

  public String getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
    donor.setBirthDate(CustomDateFormatter.getDateFromString(birthDate));
  }

  public List<String> getBloodTypes() {
    return (bloodTypes == null) ? Arrays.asList(new String[0]) : bloodTypes;
  }

  public void setBloodTypes(List<String> bloodTypes) {
    this.bloodTypes = bloodTypes;
  }

  public DonorViewModel getDonorViewModel() {
    return new DonorViewModel(donor);
  }

  public Donor getDonor() {
    return donor;
  }

  public boolean equals(Object obj) {
    return donor.equals(obj);
  }

  public Long getId() {
    return donor.getId();
  }

  public String getDonorNumber() {
    return donor.getDonorNumber();
  }

  public String getFirstName() {
    return donor.getFirstName();
  }

  public String getMiddleName() {
    return donor.getMiddleName();
  }

  public String getLastName() {
    return donor.getLastName();
  }

  public Gender getGender() {
    return donor.getGender();
  }

  public BloodRhd getBloodAbo() {
    return donor.getBloodRhd();
  }

  public BloodRhd getBloodRhd() {
    return donor.getBloodRhd();
  }

  public String getAddress() {
    return donor.getAddress();
  }

  public String getCity() {
    return donor.getCity();
  }

  public String getState() {
    return donor.getState();
  }

  public String getCountry() {
    return donor.getCountry();
  }

  public String getPhoneNumber() {
    return donor.getPhoneNumber();
  }

  public Date getLastUpdated() {
    return donor.getLastUpdated();
  }

  public Date getCreatedDate() {
    return donor.getCreatedDate();
  }

  public String getNotes() {
    return donor.getNotes();
  }

  public Boolean getIsDeleted() {
    return donor.getIsDeleted();
  }

  public User getCreatedBy() {
    return donor.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return donor.getLastUpdatedBy();
  }

  public int hashCode() {
    return donor.hashCode();
  }

  public void setId(Long id) {
    donor.setId(id);
  }

  public void setDonorNumber(String donorNumber) {
    donor.setDonorNumber(donorNumber);
  }

  public void setFirstName(String firstName) {
    donor.setFirstName(firstName);
  }

  public void setMiddleName(String middleName) {
    donor.setMiddleName(middleName);
  }

  public void setLastName(String lastName) {
    donor.setLastName(lastName);
  }

  public void setGender(String gender) {
    donor.setGender(Gender.valueOf(gender));
  }

  public void setGender(Gender gender) {
    donor.setGender(gender);
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    donor.setBloodAbo(bloodAbo);
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    donor.setBloodRhd(bloodRhd);
  }

  public void setAddress(String address) {
    donor.setAddress(address);
  }

  public void setCity(String city) {
    donor.setCity(city);
  }

  public void setState(String state) {
    donor.setState(state);
  }

  public void setCountry(String country) {
    donor.setCountry(country);
  }

  public void setPhoneNumber(String contactNumber) {
    donor.setPhoneNumber(contactNumber);
  }

  public void setLastUpdated(Date lastUpdated) {
    donor.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    donor.setCreatedDate(createdDate);
  }

  public void setNotes(String notes) {
    donor.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    donor.setIsDeleted(isDeleted);
  }

  public void setCreatedBy(User createdBy) {
    donor.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    donor.setLastUpdatedBy(lastUpdatedBy);
  }

  public String toString() {
    return donor.toString();
  }

  public ContactInformation getContactInformation() {
    return donor.getContactInformation();
  }

  public void setContactInformation(ContactInformation contactInformation) {
    donor.setContactInformation(contactInformation);
  }

  public String getZipcode() {
    return donor.getZipcode();
  }

  public void setZipcode(String zipcode) {
    donor.setZipcode(zipcode);
  }

  public BloodGroup getBloodGroup() {
    return new BloodGroup(donor.getBloodAbo(), donor.getBloodRhd());
  }

  public void setBloodGroup(String bloodGroupStr) {
    BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
    donor.setBloodAbo(bloodGroup.getBloodAbo());
    donor.setBloodRhd(bloodGroup.getBloodRhd());
  }
}