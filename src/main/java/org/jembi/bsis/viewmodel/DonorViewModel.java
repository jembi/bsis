package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.address.Address;
import org.jembi.bsis.model.address.AddressType;
import org.jembi.bsis.model.address.Contact;
import org.jembi.bsis.model.address.ContactMethodType;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.donor.DonorStatus;
import org.jembi.bsis.model.idtype.IdType;
import org.jembi.bsis.model.preferredlanguage.PreferredLanguage;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Component
public class DonorViewModel {
  private Donor donor;
  private Map<String, Boolean> permissions;

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

  public String getTitle() {
    return donor.getTitle();
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
    return CustomDateFormatter.getDateString(donor.getBirthDate());
  }

  public String getDateOfFirstDonation() {
    Date dateOfFirstDonation = donor.getDateOfFirstDonation();
    if (dateOfFirstDonation != null) {
      return CustomDateFormatter.getDateTimeString(dateOfFirstDonation);
    } else {
      return "";
    }
  }

  public Boolean getBirthDateEstimated() {
    return donor.getBirthDateEstimated();
  }


  public String getNotes() {
    Object comments = donor.getNotes();
    return comments == null ? "" : comments.toString();
  }

  public String getCallingName() {
    return donor.getCallingName();
  }

  public PreferredLanguage getPreferredLanguage() {
    /*if (donor.getPreferredLanguage() == null ||
          donor.getPreferredLanguage().getPreferredLanguage() == null)
	      return "";
	  else
	  		return donor.getPreferredLanguage().getPreferredLanguage();
	  */
    return donor.getPreferredLanguage();
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

  public LocationFullViewModel getVenue() {
    return new LocationFullViewModel(donor.getVenue());
  }

  public String getDateOfLastDonation() {
    Date dateOfLastDonation = donor.getDateOfLastDonation();
    return CustomDateFormatter.getDateTimeString(dateOfLastDonation);
  }

  public DonorStatus getDonorStatus() {
    return donor.getDonorStatus();
  }


  public String getAge() {
    if (donor.getBirthDateInferred() != null) {
      DateTime dt1 = new DateTime(donor.getBirthDateInferred());
      DateTime dt2 = new DateTime(new Date());
      int year1 = dt1.year().get();
      int year2 = dt2.year().get();
      return new Integer(year2 - year1).toString();
    } else {
      return "";
    }
  }

  public Address getAddress() {
    return donor.getAddress();
  }

  public Contact getContact() {
    return donor.getContact();
  }

  public String getIdNumber() {
    return donor.getIdNumber();
  }

  public IdType getIdType() {
    //return donor.getIdType()!=null?donor.getIdType().getIdType():"";
    return donor.getIdType();
  }

  public ContactMethodType getContactMethodType() {
    //return donor.getContactMethodType()!=null?donor.getContactMethodType().getContactMethodType():"";
    return donor.getContactMethodType();
  }

  public AddressType getPreferredAddressType() {
    //return donor.getAddressType()!=null?donor.getAddressType().getPreferredAddressType():"";
    return donor.getAddressType();
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  @JsonIgnore
  public Donor getDonor() {
    return donor;
  }
}
