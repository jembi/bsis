package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.util.Gender;

public class DuplicateDonorViewModel {

  private String groupKey;

  private String firstName;

  private String lastName;

  private Gender gender;

  private Date birthDate;

  private long count;

  public DuplicateDonorViewModel() {
    super();
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

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return "DuplicateDonorViewModel [groupKey=" + groupKey + ", firstName=" + firstName + ", lastName=" + lastName
        + ", gender=" + gender + ", birthDate=" + birthDate + ", count=" + count + "]";
  }


}
