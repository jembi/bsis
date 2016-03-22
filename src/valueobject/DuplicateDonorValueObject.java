package valueobject;

import java.util.Date;

import model.util.Gender;

public class DuplicateDonorValueObject {

  private String donorNumber;

  private String firstName;

  private String lastName;

  private Gender gender;

  private Date birthDate;

  private long count;



  public DuplicateDonorValueObject() {
    super();
  }

  public DuplicateDonorValueObject(String donorNumber, String firstName, String lastName, Date birthDate, Gender gender,
      long count) {
    super();
    this.donorNumber = donorNumber;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.gender = gender;
    this.count = count;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }



}
