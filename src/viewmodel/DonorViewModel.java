package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.CustomDateFormatter;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.location.Location;
import model.user.User;

import org.apache.commons.lang3.StringUtils;
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

  public String getMiddleName() {
    return donor.getMiddleName();
  }

  public String getLastName() {
    return donor.getLastName();
  }

  public String getGender() {
    return donor.getGender().name();
  }

  public String getBloodAbo() {
    return donor.getBloodAbo();
  }

  public String getBloodRh() {
    return donor.getBloodRh();
  }

  public String getBloodGroup() {
    if (StringUtils.isBlank(donor.getBloodAbo()) || StringUtils.isBlank(donor.getBloodRh()))
      return "";
    else
      return donor.getBloodAbo() + donor.getBloodRh();
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

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(donor.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(donor.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = donor.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = donor.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public Location getDonorPanel() {
    return donor.getDonorPanel();
  }

  public String getDateOfLastDonation() {
    Date dateOfLastDonation = donor.getDateOfLastDonation();
    return CustomDateFormatter.getDateString(dateOfLastDonation);
  }

  public DonorStatus getDonorStatus() {
    return donor.getDonorStatus();
  }

  public String getNationalID() {
    return donor.getNationalID();
  }
}
