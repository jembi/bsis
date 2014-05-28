package viewmodel;

import java.util.Date;
import model.address.AddressType;
import model.address.ContactMethodType;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.location.Location;
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
      return CustomDateFormatter.getDateString(birthDate);
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
  
  public String getIdType(){
	  if (donor.getIdType() == null ||
	        donor.getIdType().getIdType() == null)
	      return "";
	  else
	      return donor.getIdType().getIdType();
  }
  
  public String getIdNumber(){
      return donor.getIdNumber();
  }
   
  public String getCallingName(){
      return donor.getCallingName();
  }
  public String getPreferredLanguage(){
	  if (donor.getPreferredLanguage() == null ||
	        donor.getPreferredLanguage().getPreferredLanguage() == null)
	      return "";
	  else
	      return donor.getPreferredLanguage().getPreferredLanguage();
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
  
  /**
     * @return 
     * Home Address Getter Methods
     */
    public String getHomeAddressLine1() {
        return donor.getAddress().getHomeAddressline1();
    }

    public String getHomeAddressLine2() {
        return donor.getAddress().getHomeAddressline2();
    }

    public String getHomeAddressCity() {
        return donor.getAddress().getHomeAddressCity();
    }

    public String getHomeAddressProvince() {
        return donor.getAddress().getHomeAddressProvince();
    }

    public String getHomeAddressDistrict() {
        return donor.getAddress().getHomeAddressDistrict();
    }

    public String getHomeAddressState() {
        return donor.getAddress().getHomeAddressState();
    }

    public String getHomeAddressCountry() {
        return donor.getAddress().getHomeAddressCountry();
    }

    public String getHomeAddressZipcode() {
        return donor.getAddress().getHomeAddressZipcode();
    }

    
    /**
     * @return 
     * Work Address Getters 
     */
    public String getWorkAddressline1() {
        return donor.getAddress().getWorkAddressline1();
    }

    public String getWorkAddressline2() {
        return donor.getAddress().getWorkAddressline2();
    }

    public String getWorkAddressCity() {
        return donor.getAddress().getWorkAddressCity();
    }

    public String getWorkAddressProvince() {
        return donor.getAddress().getWorkAddressProvince();
    }

    public String getWorkAddressDistrict() {
        return donor.getAddress().getWorkAddressDistrict();
    }

    public String getWorkAddressState() {
        return donor.getAddress().getWorkAddressCountry();
    }

    public String getWorkAddressCountry() {
        return donor.getAddress().getWorkAddressCountry();
    }

    public String getWorkAddressZipcode() {
        return donor.getAddress().getWorkAddressZipcode();
    }

    
    /**
     * Postal Address getters & Setters
     */
    public String getPostalAddressLine1() {
        return donor.getAddress().getPostalAddressline1();
    }

    public String getPostalAddressLine2() {
        return donor.getAddress().getPostalAddressline2();
    }

    public String getPostalAddressCity() {
        return donor.getAddress().getPostalAddressCity();
    }


    public String getPostalAddressProvince() {
        return donor.getAddress().getPostalAddressProvince();
    }

    
    public String getPostalAddressState() {
        return donor.getAddress().getPostalAddressState();
    }
    
    public String getPostalAddressCountry(){
        return donor.getAddress().getPostalAddressCountry();
    }

    public String getPostalAddressDistrict() {
        return donor.getAddress().getPostalAddressDistrict();
    }
 
    public String getPostalAddressZipcode() {
        return donor.getAddress().getPostalAddressZipcode();
    }

    
    /**
     * Contact Getters & Setters
     */
    public ContactMethodType getContactType() {
        return donor.getContact().getContactMethodType();
    }

    
    public String getMobileNumber() {
        return donor.getContact().getMobileNumber();
    }

    public String getHomeNumber() {
        return donor.getContact().getHomeNumber();
    }

    public String getWorkNumber() {
        return donor.getContact().getWorkNumber();
    }

    
    /**
     * Address Type Getters & Setters
     */
    public AddressType getAddressType() {
        return donor.getAddress().getAddressType();
    }

    

  
}
