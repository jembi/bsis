package org.jembi.bsis.model.address;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.Email;
import org.jembi.bsis.model.BaseUUIDEntity;

/**
 * Entity that contains the Donor's mobile, home and work telephone number and email address.
 */
@Entity
public class Contact extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  private String mobileNumber;

  private String homeNumber;

  private String workNumber;

  @Email
  private String email;


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
}
