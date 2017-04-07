package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.address.Address;

import java.util.UUID;

public class AddressBuilder extends AbstractEntityBuilder<Address> {

  private UUID id;

  private String homeAddressLine1;
  private String homeAddressLine2;
  private String homeAddressCity;
  private String homeAddressProvince;
  private String homeAddressDistrict;
  private String homeAddressCountry;
  private String homeAddressState;
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

  public AddressBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public AddressBuilder withHomeAddress(String line1, String line2, String city, 
      String province, String district, String country, String state, String zipcode) {
    this.homeAddressLine1 = line1;
    this.homeAddressLine2 = line2;
    this.homeAddressCity = city;
    this.homeAddressProvince = province;
    this.homeAddressDistrict = district;
    this.homeAddressCountry = country;
    this.homeAddressState = state;
    this.homeAddressZipcode = zipcode;
    return this;
  }
  
  public AddressBuilder withWorkAddress(String line1, String line2, String city, 
      String province, String district, String country, String state, String zipcode) {
    this.workAddressLine1 = line1;
    this.workAddressLine2 = line2;
    this.workAddressCity = city;
    this.workAddressProvince = province;
    this.workAddressDistrict = district;
    this.workAddressCountry = country;
    this.workAddressState = state;
    this.workAddressZipcode = zipcode;
    return this;
  }
  
  public AddressBuilder withPostalAddress(String line1, String line2, String city, 
      String province, String district, String country, String state, String zipcode) {
    this.postalAddressLine1 = line1;
    this.postalAddressLine2 = line2;
    this.postalAddressCity = city;
    this.postalAddressProvince = province;
    this.postalAddressDistrict = district;
    this.postalAddressCountry = country;
    this.postalAddressState = state;
    this.postalAddressZipcode = zipcode;
    return this;
  }

  @Override
  public Address build() {
    Address address = new Address();
    address.setId(id);

    address.setHomeAddressLine1(homeAddressLine1);
    address.setHomeAddressLine2(homeAddressLine2);
    address.setHomeAddressCity(homeAddressCity);
    address.setHomeAddressCountry(homeAddressCountry);
    address.setHomeAddressDistrict(homeAddressDistrict);
    address.setHomeAddressProvince(homeAddressProvince);
    address.setHomeAddressState(homeAddressState);
    address.setHomeAddressZipcode(homeAddressZipcode);
    
    address.setWorkAddressLine1(workAddressLine1);
    address.setWorkAddressLine2(workAddressLine2);
    address.setWorkAddressCity(workAddressCity);
    address.setWorkAddressCountry(workAddressCountry);
    address.setWorkAddressDistrict(workAddressDistrict);
    address.setWorkAddressProvince(workAddressProvince);
    address.setWorkAddressState(workAddressState);
    address.setWorkAddressZipcode(workAddressZipcode);
    
    address.setPostalAddressLine1(postalAddressLine1);
    address.setPostalAddressLine2(postalAddressLine2);
    address.setPostalAddressCity(postalAddressCity);
    address.setPostalAddressCountry(postalAddressCountry);
    address.setPostalAddressDistrict(postalAddressDistrict);
    address.setPostalAddressProvince(postalAddressProvince);
    address.setPostalAddressState(postalAddressState);
    address.setPostalAddressZipcode(postalAddressZipcode);
    
    return address;
  }
  
  public static AddressBuilder anAddress() {
    return new AddressBuilder();
  }

}
