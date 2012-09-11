package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import viewmodel.DonorViewModel;

public class DonorBackingForm {
  private Donor donor;
  private List<String> bloodTypes;

  public DonorBackingForm() {
    donor = new Donor();
  }

  public DonorBackingForm(Donor donor) {
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

  public void setComments(String comments) {
    donor.setComments(comments);
  }

  public void setDonorId(String donorId) {
    donor.setDonorId(Long.parseLong(donorId));
  }

  public void setDonorNumber(String donorNumber) {
    donor.setDonorNumber(donorNumber);
  }

  public void setFirstName(String firstName) {
    donor.setFirstName(firstName);
  }

  public void setLastName(String lastName) {
    donor.setLastName(lastName);
  }

  public void setGender(String gender) {
    donor.setGender(gender);
  }

  public void setBloodType(String bloodType) {
    donor.setBloodType(bloodType);
  }

  public void setBirthDate(String birthdDate) {
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    try {
      this.setBirthDate(formatter.parse(birthdDate));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public void setBirthDate(Date birthDate) {
    donor.setBirthDate(birthDate);
  }

  public void setAddress(String address) {
    donor.setAddress(address);
  }

  public List<String> getBloodTypes() {
    return (bloodTypes == null) ? Arrays.asList(new String[0]) : bloodTypes;
  }

  public void setBloodTypes(List<String> bloodTypes) {
    this.bloodTypes = bloodTypes;
  }

  public DonorViewModel getDonorViewModel() {
    return new DonorViewModel(donor);
  }

  public Donor getDonor() {
    return donor;
  }
}
