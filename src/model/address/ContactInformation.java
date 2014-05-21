package model.address;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class ContactInformation {

  @Length(max=100)
  @Column(length=200)
  private String homeAddress;
  
    @Length(max=100)
  @Column(length=200)
  private String postalAddress;
    
  @Length(max=100)
  @Column(length=200)
  private String workAddress;

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
  private String mobileNumber;
  
  @Length(max=20)
  @Column(length=20)
  private String homeNumber;
  
  @Length(max=20)
  @Column(length=20)
  private String workNumber;

  @Length(max=20)
  @Column(length=50)
  private String emailId;

  @ManyToOne
  private ContactMethodType preferredContactMethod;

  public void copy(ContactInformation contactInformation) {
    this.homeAddress = contactInformation.homeAddress;
    this.postalAddress = contactInformation.postalAddress;
    this.workAddress = contactInformation.workAddress;
    this.city = contactInformation.city;
    this.province = contactInformation.province;
    this.district = contactInformation.district;
    this.state = contactInformation.state;
    this.country = contactInformation.country;
    this.zipcode = contactInformation.zipcode;
    this.mobileNumber = contactInformation.mobileNumber;
    this.homeNumber = contactInformation.homeNumber;
    this.workNumber = contactInformation.workNumber;
    this.emailId = contactInformation.emailId;


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

  public ContactMethodType getPreferredContactMethod() {
    return preferredContactMethod;
  }

  public void setPreferredContactMethod(ContactMethodType preferredContactMethod) {
    this.preferredContactMethod = preferredContactMethod;
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

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
  
    
  
}