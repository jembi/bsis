package model.address;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class ContactInformation {

  @Length(max=100)
  @Column(length=100)
  private String address;

  @Length(max=25)
  @Column(length=25)
  private String city;

  @Length(max=25)
  @Column(length=25)
  private String province;

  @Length(max=25)
  @Column(length=25)
  private String district;

  @Length(max=25)
  @Column(length=25)
  private String state;

  @Length(max=25)
  @Column(length=25)
  private String country;

  @Length(max=10)
  @Column(length=10)
  private String zipcode;

  @Length(max=20)
  @Column(length=20)
  private String phoneNumber;

  @Length(max=20)
  @Column(length=20)
  private String otherPhoneNumber;

  public void copy(ContactInformation contactInformation) {
    this.address = contactInformation.address;
    this.city = contactInformation.city;
    this.province = contactInformation.province;
    this.district = contactInformation.district;
    this.state = contactInformation.state;
    this.country = contactInformation.country;
    this.zipcode = contactInformation.zipcode;
    this.phoneNumber = contactInformation.phoneNumber;
    this.otherPhoneNumber = contactInformation.otherPhoneNumber;
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getProvince() {
    return province;
  }

  public String getDistrict() {
    return district;
  }

  public String getState() {
    return state;
  }

  public String getCountry() {
    return country;
  }

  public String getZipcode() {
    return zipcode;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public String getOtherPhoneNumber() {
    return otherPhoneNumber;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void setOtherPhoneNumber(String otherPhoneNumber) {
    this.otherPhoneNumber = otherPhoneNumber;
  }
}