package viewmodel;

import model.util.Gender;
import utils.CustomDateFormatter;

import java.util.Date;

/**
 * View model representing a summarised view of a donor.
 */
public class DonorSummaryViewModel {

  private long id;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date birthDate;

  public DonorSummaryViewModel() {
    // no-args constructor
  }

  /**
   * Do not remove or change the signature of this method.
   *
   * @see {@link DonorRepository#findDonorSummaryByDonorNumber(String)}
   */
  public DonorSummaryViewModel(long id, String firstName, String lastName, Gender gender, Date birthDate) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.gender = gender;
    this.birthDate = birthDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public String getGender() {
    return gender == null ? "" : gender.name();
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getBirthDate() {
    return CustomDateFormatter.getDateString(birthDate);
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

}
