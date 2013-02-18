package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.donor.Donor;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

import org.springframework.stereotype.Component;

@Component
public class DonorViewModel {
  private Donor donor;

  public DonorViewModel() {
  }

  public DonorViewModel(Donor donor) {
    this.donor = donor;
  }

  public String getId() {
    return donor.getId().toString();
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
    return donor.getGender().name();
  }

  public BloodAbo getBloodAbo() {
    return donor.getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return donor.getBloodRhd();
  }

  public String getBloodGroup() {
    return new BloodGroup(donor.getBloodAbo(), donor.getBloodRhd()).toString();
  }

  public String getBirthDate() {
    Date birthDate = donor.getBirthDate();
    if (birthDate != null) {
      DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
      return formatter.format(birthDate);
    }
    return "";
  }

  public String getAddress() {
    return donor.getAddress();
  }

  public String getCity() {
    return donor.getCity();
  }

  public String getProvince() {
    return donor.getProvince();
  }

  public String getDistrict() {
    return donor.getDistrict();
  }

  public String getState() {
    return donor.getState();
  }

  public String getCountry() {
    return donor.getCountry();
  }

  public String getZipcode() {
    return donor.getZipcode();
  }

  public String getNotes() {
    Object comments = donor.getNotes();
    return comments == null ? "" : comments.toString();
  }
}