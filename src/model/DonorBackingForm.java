package model;

import java.util.Date;
import java.util.List;

/** Advanced Search Form for finding donors
 * @author iamrohitbanga
 */
public class DonorBackingForm {

	private String donorNumber;
	private String firstName;
	private String lastName;
  private String gender;
  private Date birthDate;
  private Integer age;
  private String address;
  private String comments;


	private List<String> bloodTypes;

	public List<String> getBloodTypes() {
		return bloodTypes;
	}

	public String getDonorNumber() {
		return (this.donorNumber == null ? "" : this.donorNumber);
	}

	public String getFirstName() {
		return (this.firstName == null ? "" : this.firstName);
	}

	public String getLastName() {
		return (this.lastName == null ? "" : this.lastName) ;
	}

	public void setBloodTypes(List<String> bloodTypes) {
		this.bloodTypes = bloodTypes;
	}

	public void setDonorNumber(String donorNumber) {
		this.donorNumber = donorNumber;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
