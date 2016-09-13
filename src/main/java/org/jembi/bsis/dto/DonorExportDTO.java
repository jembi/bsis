package org.jembi.bsis.dto;

import java.util.Date;

import org.jembi.bsis.model.util.Gender;

public class DonorExportDTO extends ModificationTrackerExportDTO {

  private String donorNumber;
  private String title;
  private String firstName;
  private String middleName;
  private String lastName;
  private String callingName;
  private Gender gender;
  private Date birthDate;
  private String preferredLanguage;
  private String venue;
  private String bloodABO;
  private String bloodRh;
  private String notes;
  private String idType;
  private String idNumber;
  private Date dateOfFirstDonation;
  private Date dateOfLastDonation;
  private Date dueToDonate;
  private String contactMethodType;
  private String mobileNumber;
  private String homeNumber;
  private String workNumber;
  private String email;
  private String preferredAddressType;
  private String homeAddressLine1;
  private String homeAddressLine2;
  private String homeAddressCity;
  private String homeAddressProvince;
  private String homeAddressDistrict;
  private String homeAddressState;
  private String homeAddressCountry;
  private String homeAddressZipcode;
  private String workAddressLine1;
  private String workAddressLine2;
  private String workAddressCity;
  private String workAddressProvince;
  private String workAddressDistrict;
  private String workAddressCountry;
  private String workAddressState;
  private String workAddressZipcode;
  private String postalAddressLine1;
  private String postalAddressLine2;
  private String postalAddressCity;
  private String postalAddressProvince;
  private String postalAddressDistrict;
  private String postalAddressCountry;
  private String postalAddressState;
  private String postalAddressZipcode;

  public DonorExportDTO() {
    // Default constructor
  }

  public DonorExportDTO(String donorNumber, Date createdDate, String createdBy, Date lastUpdated, String lastUpdatedBy,
      String title, String firstName, String middleName, String lastName, String callingName, Gender gender,
      Date birthDate, String preferredLanguage, String venue, String bloodABO, String bloodRh, String notes,
      String idType, String idNumber, Date dateOfFirstDonation, Date dateOfLastDonation, Date dueToDonate,
      String contactMethodType, String mobileNumber, String homeNumber, String workNumber, String email,
      String preferredAddressType, String homeAddressLine1, String homeAddressLine2, String homeAddressCity,
      String homeAddressProvince, String homeAddressDistrict, String homeAddressCountry, String homeAddressState,
      String homeAddressZipcode, String workAddressLine1, String workAddressLine2, String workAddressCity,
      String workAddressProvince, String workAddressDistrict, String workAddressCountry, String workAddressState,
      String workAddressZipcode, String postalAddressLine1, String postalAddressLine2, String postalAddressCity,
      String postalAddressProvince, String postalAddressDistrict, String postalAddressCountry,
      String postalAddressState, String postalAddressZipcode) {
    this.donorNumber = donorNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.title = title;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.callingName = callingName;
    this.gender = gender;
    this.birthDate = birthDate;
    this.preferredLanguage = preferredLanguage;
    this.venue = venue;
    this.bloodABO = bloodABO;
    this.bloodRh = bloodRh;
    this.notes = notes;
    this.idType = idType;
    this.idNumber = idNumber;
    this.dateOfFirstDonation = dateOfFirstDonation;
    this.dateOfLastDonation = dateOfLastDonation;
    this.dueToDonate = dueToDonate;
    this.contactMethodType = contactMethodType;
    this.mobileNumber = mobileNumber;
    this.homeNumber = homeNumber;
    this.workNumber = workNumber;
    this.email = email;
    this.preferredAddressType = preferredAddressType;
    this.homeAddressLine1 = homeAddressLine1;
    this.homeAddressLine2 = homeAddressLine2;
    this.homeAddressCity = homeAddressCity;
    this.homeAddressProvince = homeAddressProvince;
    this.homeAddressDistrict = homeAddressDistrict;
    this.homeAddressCountry = homeAddressCountry;
    this.homeAddressState = homeAddressState;
    this.homeAddressZipcode = homeAddressZipcode;
    this.workAddressLine1 = workAddressLine1;
    this.workAddressLine2 = workAddressLine2;
    this.workAddressCity = workAddressCity;
    this.workAddressProvince = workAddressProvince;
    this.workAddressDistrict = workAddressDistrict;
    this.workAddressCountry = workAddressCountry;
    this.workAddressState = workAddressState;
    this.workAddressZipcode = workAddressZipcode;
    this.postalAddressLine1 = postalAddressLine1;
    this.postalAddressLine2 = postalAddressLine2;
    this.postalAddressCity = postalAddressCity;
    this.postalAddressProvince = postalAddressProvince;
    this.postalAddressDistrict = postalAddressDistrict;
    this.postalAddressCountry = postalAddressCountry;
    this.postalAddressState = postalAddressState;
    this.postalAddressZipcode = postalAddressZipcode;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getCallingName() {
    return callingName;
  }

  public void setCallingName(String callingName) {
    this.callingName = callingName;
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

  public String getPreferredLanguage() {
    return preferredLanguage;
  }

  public void setPreferredLanguage(String preferredLanguage) {
    this.preferredLanguage = preferredLanguage;
  }

  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public String getBloodABO() {
    return bloodABO;
  }

  public void setBloodABO(String bloodABO) {
    this.bloodABO = bloodABO;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getIdType() {
    return idType;
  }

  public void setIdType(String idType) {
    this.idType = idType;
  }

  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public Date getDateOfFirstDonation() {
    return dateOfFirstDonation;
  }

  public void setDateOfFirstDonation(Date dateOfFirstDonation) {
    this.dateOfFirstDonation = dateOfFirstDonation;
  }

  public Date getDateOfLastDonation() {
    return dateOfLastDonation;
  }

  public void setDateOfLastDonation(Date dateOfLastDonation) {
    this.dateOfLastDonation = dateOfLastDonation;
  }

  public Date getDueToDonate() {
    return dueToDonate;
  }

  public void setDueToDonate(Date dueToDonate) {
    this.dueToDonate = dueToDonate;
  }

  public String getContactMethodType() {
    return contactMethodType;
  }

  public void setContactMethodType(String contactMethodType) {
    this.contactMethodType = contactMethodType;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getHomeNumber() {
    return homeNumber;
  }

  public void setHomeNumber(String homeNumber) {
    this.homeNumber = homeNumber;
  }

  public String getWorkNumber() {
    return workNumber;
  }

  public void setWorkNumber(String workNumber) {
    this.workNumber = workNumber;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPreferredAddressType() {
    return preferredAddressType;
  }

  public void setPreferredAddressType(String preferredAddressType) {
    this.preferredAddressType = preferredAddressType;
  }

  public String getHomeAddressLine1() {
    return homeAddressLine1;
  }

  public void setHomeAddressLine1(String homeAddressLine1) {
    this.homeAddressLine1 = homeAddressLine1;
  }

  public String getHomeAddressLine2() {
    return homeAddressLine2;
  }

  public void setHomeAddressLine2(String homeAddressLine2) {
    this.homeAddressLine2 = homeAddressLine2;
  }

  public String getHomeAddressCity() {
    return homeAddressCity;
  }

  public void setHomeAddressCity(String homeAddressCity) {
    this.homeAddressCity = homeAddressCity;
  }

  public String getHomeAddressProvince() {
    return homeAddressProvince;
  }

  public void setHomeAddressProvince(String homeAddressProvince) {
    this.homeAddressProvince = homeAddressProvince;
  }

  public String getHomeAddressDistrict() {
    return homeAddressDistrict;
  }

  public void setHomeAddressDistrict(String homeAddressDistrict) {
    this.homeAddressDistrict = homeAddressDistrict;
  }

  public String getHomeAddressState() {
    return homeAddressState;
  }

  public void setHomeAddressState(String homeAddressState) {
    this.homeAddressState = homeAddressState;
  }

  public String getHomeAddressCountry() {
    return homeAddressCountry;
  }

  public void setHomeAddressCountry(String homeAddressCountry) {
    this.homeAddressCountry = homeAddressCountry;
  }

  public String getHomeAddressZipcode() {
    return homeAddressZipcode;
  }

  public void setHomeAddressZipcode(String homeAddressZipcode) {
    this.homeAddressZipcode = homeAddressZipcode;
  }

  public String getWorkAddressLine1() {
    return workAddressLine1;
  }

  public void setWorkAddressLine1(String workAddressLine1) {
    this.workAddressLine1 = workAddressLine1;
  }

  public String getWorkAddressLine2() {
    return workAddressLine2;
  }

  public void setWorkAddressLine2(String workAddressLine2) {
    this.workAddressLine2 = workAddressLine2;
  }

  public String getWorkAddressCity() {
    return workAddressCity;
  }

  public void setWorkAddressCity(String workAddressCity) {
    this.workAddressCity = workAddressCity;
  }

  public String getWorkAddressProvince() {
    return workAddressProvince;
  }

  public void setWorkAddressProvince(String workAddressProvince) {
    this.workAddressProvince = workAddressProvince;
  }

  public String getWorkAddressDistrict() {
    return workAddressDistrict;
  }

  public void setWorkAddressDistrict(String workAddressDistrict) {
    this.workAddressDistrict = workAddressDistrict;
  }

  public String getWorkAddressCountry() {
    return workAddressCountry;
  }

  public void setWorkAddressCountry(String workAddressCountry) {
    this.workAddressCountry = workAddressCountry;
  }

  public String getWorkAddressState() {
    return workAddressState;
  }

  public void setWorkAddressState(String workAddressState) {
    this.workAddressState = workAddressState;
  }

  public String getWorkAddressZipcode() {
    return workAddressZipcode;
  }

  public void setWorkAddressZipcode(String workAddressZipcode) {
    this.workAddressZipcode = workAddressZipcode;
  }

  public String getPostalAddressLine1() {
    return postalAddressLine1;
  }

  public void setPostalAddressLine1(String postalAddressLine1) {
    this.postalAddressLine1 = postalAddressLine1;
  }

  public String getPostalAddressLine2() {
    return postalAddressLine2;
  }

  public void setPostalAddressLine2(String postalAddressLine2) {
    this.postalAddressLine2 = postalAddressLine2;
  }

  public String getPostalAddressCity() {
    return postalAddressCity;
  }

  public void setPostalAddressCity(String postalAddressCity) {
    this.postalAddressCity = postalAddressCity;
  }

  public String getPostalAddressProvince() {
    return postalAddressProvince;
  }

  public void setPostalAddressProvince(String postalAddressProvince) {
    this.postalAddressProvince = postalAddressProvince;
  }

  public String getPostalAddressDistrict() {
    return postalAddressDistrict;
  }

  public void setPostalAddressDistrict(String postalAddressDistrict) {
    this.postalAddressDistrict = postalAddressDistrict;
  }

  public String getPostalAddressCountry() {
    return postalAddressCountry;
  }

  public void setPostalAddressCountry(String postalAddressCountry) {
    this.postalAddressCountry = postalAddressCountry;
  }

  public String getPostalAddressState() {
    return postalAddressState;
  }

  public void setPostalAddressState(String postalAddressState) {
    this.postalAddressState = postalAddressState;
  }

  public String getPostalAddressZipcode() {
    return postalAddressZipcode;
  }

  public void setPostalAddressZipcode(String postalAddressZipcode) {
    this.postalAddressZipcode = postalAddressZipcode;
  }
}
