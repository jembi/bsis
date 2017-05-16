package org.jembi.bsis.model.address;

import javax.persistence.Entity;

import org.jembi.bsis.model.BaseEntity;
import org.jembi.bsis.model.BaseUUIDEntity;

/**
 * Entity containing the home, work and postal address of a Donor.
 */
@Entity
public class Address extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

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

  public String getHomeAddressCountry() {
    return homeAddressCountry;
  }

  public void setHomeAddressCountry(String homeAddressCountry) {
    this.homeAddressCountry = homeAddressCountry;
  }

  public String getHomeAddressState() {
    return homeAddressState;
  }

  public void setHomeAddressState(String homeAddressState) {
    this.homeAddressState = homeAddressState;
  }

  public String getHomeAddressZipcode() {
    return homeAddressZipcode;
  }

  public void setHomeAddressZipcode(String homeAddressZipcode) {
    this.homeAddressZipcode = homeAddressZipcode;
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
