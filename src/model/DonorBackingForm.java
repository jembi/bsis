package model;

import java.util.List;

/** Advanced Search Form for finding donors
 * @author iamrohitbanga
 */
public class DonorBackingForm {

	private String donorNumber;
	private String firstName;
	private String lastName;

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
}
