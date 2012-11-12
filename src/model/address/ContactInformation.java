package model.address;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class ContactInformation {

  @Length(max=255)
  @Column(length=255)
  private String address;

  @Length(max=50)
  @Column(length=50)
  private String city;

  @Length(max=50)
  @Column(length=50)
  private String state;

  @Length(max=50)
  @Column(length=50)
  private String country;

  @Length(max=10)
  @Column(length=10)
  private String zipcode;

  @Length(max=30)
  @Column(length=50)
  private String phoneNumber;

  public String getAddress() {
    return address;
  }
  public String getCity() {
    return city;
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
  public void setAddress(String address) {
    this.address = address;
  }
  public void setCity(String city) {
    this.city = city;
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
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public void copy(ContactInformation contactInformation) {
    address = contactInformation.address;
    city = contactInformation.city;
    state = contactInformation.state;
    country = contactInformation.country;
    zipcode = contactInformation.zipcode;
    phoneNumber = contactInformation.phoneNumber;
  }
}