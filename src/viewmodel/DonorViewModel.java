package viewmodel;

import java.util.Date;
import model.address.Address;
import model.address.AddressType;
import model.address.Contact;
import model.address.ContactMethodType;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.idtype.IdType;
import model.location.Location;
import model.address.ContactMethodType;
import model.preferredlanguage.PreferredLanguage;
import model.user.User;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import utils.CustomDateFormatter;

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
    Date birthDate = donor.getBirthDate();
    if (birthDate != null) {
      //return CustomDateFormatter.getDateString(birthDate);
    	return birthDate.toString();
    } else {
      return "";
    }
  }
  
  public String getDateOfFirstDonation() {
    Date dateOfFirstDonation = donor.getDateOfFirstDonation();
    if (dateOfFirstDonation != null) {
      return CustomDateFormatter.getDateString(dateOfFirstDonation);
    } else {
      return "";
    }
  }
  
  public Boolean getBirthDateEstimated(){
	  return donor.getBirthDateEstimated();
  }

 
  public String getNotes() {
    Object comments = donor.getNotes();
    return comments == null ? "" : comments.toString();
  }
  
  public String getCallingName(){
      return donor.getCallingName();
  }
  
  public PreferredLanguage getPreferredLanguage(){
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
 

  public String getAge() {
    if (donor.getBirthDateInferred() != null) {
      DateTime dt1 = new DateTime(donor.getBirthDateInferred());
      DateTime dt2 = new DateTime(new Date());
      int year1 = dt1.year().get();
      int year2 = dt2.year().get();
      return new Integer(year2-year1).toString();
    } else {
      return "";
    }
  }
  
  public Address getAddress(){
      return donor.getAddress();
  }
  
  public Contact getContact(){
      return donor.getContact();
  }
  
  public  String getIdNumber(){
      return donor.getIdNumber();
  }
  public IdType getIdType(){
      //return donor.getIdType()!=null?donor.getIdType().getIdType():"";
      return donor.getIdType();
  }
  
  public ContactMethodType getContactMethodType(){
    //return donor.getContactMethodType()!=null?donor.getContactMethodType().getContactMethodType():"";
	  return donor.getContactMethodType();
  }
  
  public String getPreferredAddressType(){
     //return donor.getAddressType()!=null?donor.getAddressType().getPreferredAddressType():"";
	 return donor.getAddressType();
  }
  
  
}
