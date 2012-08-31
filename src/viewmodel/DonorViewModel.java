package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Donor;

import org.joda.time.DateTime;

public class DonorViewModel {
	private Donor donor;

	public DonorViewModel(Donor donor) {
		this.donor = donor;
	}

	public String getDonorId() {
		return donor.getDonorId().toString();
	}

	public String getDonorNumber() {
		return donor.getDonorNumber();
	}

	public String getFirstName() {
		return donor.getFirstName();
	}

	public String getLastName() {
		return donor.getLastName();
	}

	public String getGender() {
		return donor.getGender();
	}

	public String getBloodType() {
		return donor.getBloodType();
	}

	public String getBirthDate() {
		Date birthDate = donor.getBirthDate();
		if (birthDate != null) {
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			return formatter.format(birthDate);
		}
		return "";
	}

	public String getBirthDateMonth() {
		if (getBirthDate().length() > 0) {
			DateTime dob = new DateTime(donor.getBirthDate());
			return String.format("%02d", dob.monthOfYear().get());
		}
		return "";
	}

	public String getBirthDateDay() {
		if (getBirthDate().length() > 0) {
			DateTime dob = new DateTime(donor.getBirthDate());
			return String.format("%02d", dob.dayOfMonth().get());
		}
		return "";
	}

	public String getBirthDateYear() {
		if (getBirthDate().length() > 0) {
			DateTime dob = new DateTime(donor.getBirthDate());
			return String.format("%04d", dob.year().get());
		}
		return "";
	}

	public String getAddress() {
		return donor.getAddress();
	}

	public String getAge() {
		return donor.getAge() == null ? "" : donor.getAge().toString();
	}

	public String getComments() {
		Object comments = donor.getComments();
		return comments == null ? "" : comments.toString();
	}
}
