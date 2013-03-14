package model.donor;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.validation.Valid;

import model.CustomDateFormatter;
import model.address.ContactInformation;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;
import model.util.Gender;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import repository.DonorRepository;
import viewmodel.DonorViewModel;

public class DonorBackingForm {

  @Valid
  private Donor donor;

  // store a local copy of birthdate string as validation may have failed
  private String birthDate;

  private Boolean ageFormatCorrect;

  private String ageSpecified;

  public DonorBackingForm() {
    donor = new Donor();
    ageFormatCorrect = null;
  }

  public DonorBackingForm(Donor donor) {
    this.donor = donor;
  }

  public String getBirthDate() {
    if (birthDate != null)
      return birthDate;
    if (donor == null)
      return "";
    return CustomDateFormatter.getDateString(donor.getBirthDate());
  }

  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
    try {
      donor.setBirthDate(CustomDateFormatter.getDateFromString(birthDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      donor.setBirthDate(null);
    }
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

  public String getCallingName() {
    return donor.getCallingName();
  }

  public String getGender() {
    if (donor == null || donor.getGender() == null)
      return null;
    return donor.getGender().toString();
  }

  public BloodAbo getBloodAbo() {
    return donor.getBloodAbo();
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

  public String getProvince() {
    return donor.getProvince();
  }

  public String getDistrict() {
    return donor.getDistrict();
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

  public void setCallingName(String callingName) {
    donor.setCallingName(callingName);
  }

  public void setGender(String gender) {
    donor.setGender(Gender.valueOf(gender));
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

  public void setProvince(String province) {
    donor.setProvince(province);
  }

  public void setDistrict(String district) {
    donor.setDistrict(district);
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

  public String getBloodGroup() {
    return new BloodGroup(donor.getBloodAbo(), donor.getBloodRhd()).toString();
  }

  public void setBloodGroup(String bloodGroupStr) {
    BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
    donor.setBloodAbo(bloodGroup.getBloodAbo());
    donor.setBloodRhd(bloodGroup.getBloodRhd());
  }

  public void generateDonorNumber() {
    donor.setDonorNumber(DonorRepository.generateUniqueDonorNumber());
  }

  public String getAge() {
    return ageSpecified;
  }

  public void setAge(String ageStr) {
    ageSpecified = ageStr;
    if (ageStr == null || StringUtils.isBlank(ageStr)) {
      return;
    }
    try {
      int age = Integer.parseInt(ageStr);
      DateTime dt = new DateTime(new Date());
      Calendar c = Calendar.getInstance();
      c.setTime(dt.toDateMidnight().toDate());
      c.set(Calendar.MONTH, Calendar.JANUARY);
      c.set(Calendar.DATE, 1);
      c.add(Calendar.YEAR, -age);
      donor.setBirthDateInferred(c.getTime());
      ageFormatCorrect = true;
    } catch (NumberFormatException ex) {
      ageFormatCorrect = false;
      donor.setBirthDate(null);
    }
  }

  public Boolean isAgeFormatCorrect() {
    return ageFormatCorrect;
  }

  public String getNationalID() {
    return donor.getNationalID();
  }

  public void setNationalID(String nationalID) {
    donor.setNationalID(nationalID);
  }

  public String getOtherPhoneNumber() {
    return donor.getOtherPhoneNumber();
  }

  public void setOtherPhoneNumber(String otherPhoneNumber) {
    donor.setOtherPhoneNumber(otherPhoneNumber);
  }
}