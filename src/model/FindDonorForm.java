package model;

import java.util.List;

public class FindDonorForm {

	private String donorNumber;
	private String firstName;
	private String lastName;

	private List<String> bloodGroups;

	public List<String> getBloodGroups() {
		return bloodGroups;
	}

	public String getDonorNumber() {
		return this.donorNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setBloodGroups(List<String> bloodGroups) {
		this.bloodGroups = bloodGroups;
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
