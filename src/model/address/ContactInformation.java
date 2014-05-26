package model.address;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class ContactInformation {
  
 
  
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

  private String mobileNumber;
  private String homeNumber;
  private String workNumber;
  private String email;
  private String workAddress;
  private String postalAddress;
  private String homeAddress;
  
private String workAddressCity;
private String workAddressProvince;
private String workAddressDistrict;
private String workAddressCountry;
private String workAddressState;
private String workAddressZipcode;

private String postalAddressCity;
private String postalAddressProvince;
private String postalAddressDistrict;
private String postalAddressCountry;
private String postalAddressState;
private String postalAddressZipcode;


  @ManyToOne
  private ContactMethodType preferredContactMethod;

  public void copy(ContactInformation contactInformation) {
    
    this.city = contactInformation.city;
    this.province = contactInformation.province;
    this.district = contactInformation.district;
    this.state = contactInformation.state;
    this.country = contactInformation.country;
    this.zipcode = contactInformation.zipcode;

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

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

  public ContactMethodType getPreferredContactMethod() {
    return preferredContactMethod;
  }

  public void setPreferredContactMethod(ContactMethodType preferredContactMethod) {
    this.preferredContactMethod = preferredContactMethod;
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

    public String getWorkAddressZipcode() {
        return workAddressZipcode;
    }

    public void setWorkAddressZipcode(String workAddressZipcode) {
        this.workAddressZipcode = workAddressZipcode;
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

    public String getPostalAddressZipcode() {
        return postalAddressZipcode;
    }
    
   public void setPostalAddressZipcode(String postalAddressZipcode) {
        this.postalAddressZipcode = postalAddressZipcode;
    }
    
    public String getWorkAddressState() {
        return workAddressState;
    }

    public void setWorkAddressState(String workAddressState) {
        this.workAddressState = workAddressState;
    }

    public String getPostalAddressState() {
        return postalAddressState;
    }

    public void setPostalAddressState(String postalAddressState) {
        this.postalAddressState = postalAddressState;
    }

  
  
  
  
}