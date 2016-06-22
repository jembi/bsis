package org.jembi.bsis.dto;

import java.util.Date;

import org.jembi.bsis.model.util.Gender;

public class DuplicateDonorDTO {

  private String groupKey;

  private String firstName;

  private String lastName;

  private Gender gender;

  private Date birthDate;

  private long count;

  public DuplicateDonorDTO() {
    super();
  }

  public DuplicateDonorDTO(String groupKey, String firstName, String lastName, Date birthDate, Gender gender,
      long count) {
    super();
    this.groupKey = groupKey;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthDate = birthDate;
    this.gender = gender;
    this.count = count;
  }

  public String getGroupKey() {
    return groupKey;
  }

  public void setGroupKey(String groupKey) {
    this.groupKey = groupKey;
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
