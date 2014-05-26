package backingform;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.validation.Valid;
import model.address.ContactInformation;
import model.address.ContactMethodType;
import model.donor.Donor;
import model.idtype.IdType;
import model.location.Location;
import model.preferredlanguage.PreferredLanguage;
import model.user.User;
import model.util.Gender;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import utils.CustomDateFormatter;
import viewmodel.DonorViewModel;
public class DonorBackingForm {

  @Valid
  private Donor donor;

  // store a local copy of birthdate string as validation may have failed
  private String birthDate;
  
  //to capture date of birth parameters--#11
  String year;
  String month;
  String dayOfMonth;

  private Boolean ageFormatCorrect;

  private String ageSpecified;
  

  public DonorBackingForm() {
    donor = new Donor();
    ageFormatCorrect = null;
  }

  public DonorBackingForm(Donor donor) {
    this.donor = donor;
  }

  public String getBirthDate() {
    if (birthDate != null)
      return birthDate;
    if (donor == null)
      return "";
    return CustomDateFormatter.getDateString(donor.getBirthDate());
  }

  public void setBirthDate() {
	
    if(year.isEmpty() || month.isEmpty() || dayOfMonth.isEmpty())
	  {  
	donor.setBirthDate(null);
    return;
	  }
	  birthDate = dayOfMonth+"/"+month+"/"+year;
    try {
      donor.setBirthDate(CustomDateFormatter.getDateFromString(birthDate));
    } catch (ParseException ex) {
      donor.setBirthDate(null);
    }
  }
  
  public void setBirthDate(String birthDate) {
    this.birthDate = birthDate;
    try {
      donor.setBirthDate(CustomDateFormatter.getDateFromString(birthDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      donor.setBirthDate(null);
    }
  }

  public DonorViewModel getDonorViewModel() {
    return new DonorViewModel(donor);
  }

  public Donor getDonor() {
    return donor;
  }

  public boolean equals(Object obj) {
    return donor.equals(obj);
  }

  public Long getId() {
    return donor.getId();
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

  public String getCallingName() {
    return donor.getCallingName();
  }

  public Boolean getBirthDateEstimated(){
	return donor.getBirthDateEstimated();
  }
  
  public String getGender() {
    if (donor == null || donor.getGender() == null)
      return null;
    return donor.getGender().toString();
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

  public String getMobileNumber() {
    return donor.getMobileNumber();
  }
  
   public String getHomeNumber() {
    return donor.getHomeNumber();
  }

    public String getWorkNumber() {
    return donor.getWorkNumber();
  }


  public Date getLastUpdated() {
    return donor.getLastUpdated();
  }

  public Date getCreatedDate() {
    return donor.getCreatedDate();
  }

  public String getNotes() {
    return donor.getNotes();
  }

  public Boolean getIsDeleted() {
    return donor.getIsDeleted();
  }

  public User getCreatedBy() {
    return donor.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return donor.getLastUpdatedBy();
  }

  public int hashCode() {
    return donor.hashCode();
  }

  public void setId(Long id) {
    donor.setId(id);
  }

  public void setDonorNumber(String donorNumber) {
    donor.setDonorNumber(donorNumber);
  }

  public void setFirstName(String firstName) {
    donor.setFirstName(firstName);
  }

  public void setMiddleName(String middleName) {
    donor.setMiddleName(middleName);
  }

  public void setLastName(String lastName) {
    donor.setLastName(lastName);
  }

  public void setCallingName(String callingName) {
    donor.setCallingName(callingName);
  }

  public void setGender(String gender) {
    donor.setGender(Gender.valueOf(gender));
  }
  
  public void setBirthDateEstimated(Boolean birthDateEstimated){
	  donor.setBirthDateEstimated(birthDateEstimated);
  }

  public void setHomeAddress(String homeAddress) {
    donor.setHomeAddress(homeAddress);
  }
  
  public void setPostalAddress(String postalAddress) {
    donor.setPostalAddress(postalAddress);
  }

    
  public void setWorkAddress(String workAddress) {
    donor.setWorkAddress(workAddress);
  }
  
public String getHomeAddress() {
    return donor.getHomeAddress();
}

public String getPostalAddress() {
    return donor.getPostalAddress();
}
public String getWorkAddress() {
    return donor.getWorkAddress();
}
    
  
  public void setCity(String city) {
    donor.setCity(city);
  }

  public void setProvince(String province) {
    donor.setProvince(province);
  }

  public void setDistrict(String district) {
    donor.setDistrict(district);
  }

  public void setState(String state) {
    donor.setState(state);
  }

  public void setCountry(String country) {
    donor.setCountry(country);
  }

  public void setMobileNumber(String mobileNumber) {
    donor.setMobileNumber(mobileNumber);
  }
  
   public void setHomeNumber(String homeNumber) {
    donor.setHomeNumber(homeNumber);
  }
   
    public void setWorkNumber(String workNumber) {
    donor.setWorkNumber(workNumber);
  }

  public void setLastUpdated(Date lastUpdated) {
    donor.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    donor.setCreatedDate(createdDate);
  }

  public void setNotes(String notes) {
    donor.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    donor.setIsDeleted(isDeleted);
  }

  public void setCreatedBy(User createdBy) {
    donor.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    donor.setLastUpdatedBy(lastUpdatedBy);
  }

  public String toString() {
    return donor.toString();
  }

  public ContactInformation getContactInformation() {
    return donor.getContactInformation();
  }

  public void setContactInformation(ContactInformation contactInformation) {
    donor.setContactInformation(contactInformation);
  }

  public String getZipcode() {
    return donor.getZipcode();
  }

  public void setZipcode(String zipcode) {
    donor.setZipcode(zipcode);
  }
 
      public String getWorkAddressCity() {
        return donor.getWorkAddressCity();
    }

    public void setWorkAddressCity(String workAddressCity) {
        donor.setWorkAddressCity(workAddressCity);
    }

    public String getWorkAddressProvince() {
        return donor.getWorkAddressProvince();
    }

    public void setWorkAddressProvince(String workAddressProvince) {
        donor.setWorkAddressProvince(workAddressProvince);
    }

    public String getWorkAddressDistrict() {
        return donor.getWorkAddressDistrict();    }

    public void setWorkAddressDistrict(String workAddressDistrict) {
        donor.setWorkAddressDistrict(workAddressDistrict);
    }

    public String getWorkAddressCountry() {
        return donor.getWorkAddressCountry();
    }

    public void setWorkAddressCountry(String workAddressCountry) {
         donor.setWorkAddressCountry(workAddressCountry);
    }

    public String getWorkAddressZipcode() {
        return donor.getWorkAddressZipcode();
    }

    public void setWorkAddressZipcode(String workAddressZipcode) {
        donor.setWorkAddressZipcode(workAddressZipcode);
    }
 public String getWorkAddressState() {
        return donor.getWorkAddressState();
    }

    public void setWorkAddressState(String workAddressState) {
        donor.setWorkAddressState(workAddressState);
    }
    
    public String getPostalAddressCity() {
        return donor.getPostalAddressCity();
    }

    public void setPostalAddressCity(String postalAddressCity) {
        donor.setPostalAddressCity(postalAddressCity);
    }

    public String getPostalAddressProvince() {
        return donor.getPostalAddressProvince();
    }
    
    public void setPostalAddressState(String postalAddressState) {
        donor.setPostalAddressState(postalAddressState);
    }
    
    public String getPostalAddressState(){
       return donor.getPostalAddressState();
    }

  
    public void setPostalAddressProvince(String postalAdressProvince) {
        donor.setPostalAddressProvince(postalAdressProvince);
    }

    public String getPostalAddressDistrict() {
        return donor.getPostalAddressDistrict();
    }

    public void setPostalAddressDistrict(String postalAddressDistrict) {
       donor.setPostalAddressDistrict(postalAddressDistrict);
    }

    public String getPostalAddressCountry() {
        return donor.getPostalAddressCountry();
    }

    public void setPostalAddressCountry(String postalAddressCountry) {
        donor.setPostalAddressCountry(postalAddressCountry);
    }

    public String getPostalAddressZipcode() {
        return donor.getPostalAddressZipcode();
    }

    public void setPostalAddressZipcode(String postalAddressZipcode) {
        donor.setPostalAddressZipcode(postalAddressZipcode);
    }
  
  public String getAge() {
    if (donor.getBirthDateInferred() != null) {
      DateTime dt1 = new DateTime(donor.getBirthDateInferred());
      DateTime dt2 = new DateTime(new Date());
      int year1 = dt1.year().get();
      int year2 = dt2.year().get();
      return new Integer(year2-year1).toString();
    } else {
      return ageSpecified;
    }
  }

  public void setAge(String ageStr) {
    ageSpecified = ageStr;
    if (ageStr == null || StringUtils.isBlank(ageStr)) {
      donor.setBirthDateInferred(null);
      return;
    }
    try {
      int age = Integer.parseInt(ageStr);
      DateTime dt = new DateTime(new Date());
      Calendar c = Calendar.getInstance();
      c.setTime(dt.toDateMidnight().toDate());
      c.set(Calendar.MONTH, Calendar.JANUARY);
      c.set(Calendar.DATE, 1);
      c.add(Calendar.YEAR, -age);
      donor.setBirthDateInferred(c.getTime());
      ageFormatCorrect = true;
    } catch (NumberFormatException ex) {
      ageFormatCorrect = false;
      donor.setBirthDate(null);
    }
  }

  public void setEmail(String email){
      donor.setEmail(email);
  }
  
  public String getEmail(){
      return donor.getEmail();
  }
  public Boolean isAgeFormatCorrect() {
    return ageFormatCorrect;
  }

  public String getNationalID() {
    return donor.getNationalID();
  }

  public void setNationalID(String nationalID) {
    donor.setNationalID(nationalID);
  }

  public void setIdType(IdType idType){
      donor.setIdType(idType);
  }
  

   public void setIdNumber(String idNumber){
      donor.setIdNumber(idNumber);
  }
  
  public String getIdNumber(){
      return donor.getIdNumber();
  }
  

  
 
  
  
  public String getDonorPanel() {
    Location donorPanel = donor.getDonorPanel();
    if (donorPanel == null || donorPanel.getId() == null)
      return null;
    return donorPanel.getId().toString();
  }

  	public String getYear() {
  		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}
	
	public String getMonth() {
		return month;
	}
	
	public void setMonth(String month) {
		this.month = month;
	}
	
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	

    
	public void setDonorPanel(String donorPanel) {
	    if (StringUtils.isBlank(donorPanel)) {
	      donor.setDonorPanel(null);
	    }
	    else {
	      Location l = new Location();
	      try {
	        l.setId(Long.parseLong(donorPanel));
	        donor.setDonorPanel(l);
	      } catch (NumberFormatException ex) {
	        ex.printStackTrace();
	        donor.setDonorPanel(null);
	      }
	    }
	}
       
        
        public void setIdType(String idType) {
	    if (StringUtils.isBlank(idType)) {
	      donor.setDonorPanel(null);
	    }
	    else {
	      IdType type = new IdType();
	      try {
	        type.setId(Long.parseLong(idType));
	        donor.setIdType(type);
	      } catch (NumberFormatException ex) {
	        ex.printStackTrace();
	        donor.setDonorPanel(null);
	      }
	    }
	}
        
          
  public String getIdType() {
      IdType idType = donor.getIdType();
        if (idType == null || idType.getId() == null)
             return null;
        return idType.getId().toString();
	}

  public void setPreferredLanguage(String language){
     
       if (StringUtils.isBlank(language)) {
	      donor.setPreferredContactMethod(null);
	    }
	    else {
	      PreferredLanguage preferredLanguage = new PreferredLanguage();
	      try {
	        preferredLanguage.setId(Long.parseLong(language));
	        donor.setPreferredLanguage(preferredLanguage);
	      } catch (NumberFormatException ex) {
	        ex.printStackTrace();
	        donor.setPreferredContactMethod(null);
	      }
	    }
  }
  
     public String getPreferredLanguage() {
      PreferredLanguage preferredLanguage = donor.getPreferredLanguage();
        if (preferredLanguage == null || preferredLanguage.getId() == null)
             return null;
        return preferredLanguage.getId().toString();
	}

        
  public String getPreferredContactMethod() {
    ContactMethodType contactMethodType = donor.getPreferredContactMethod();
    if (contactMethodType == null || contactMethodType.getId() == null)
      return null;
    else
      return contactMethodType.getId().toString();
  }

  public void setPreferredContactMethod(String contactMethodTypeId) {
    if (StringUtils.isBlank(contactMethodTypeId)) {
      donor.setPreferredContactMethod(null);
    }
    else {
      ContactMethodType ct = new ContactMethodType();
      try {
        ct.setId(Integer.parseInt(contactMethodTypeId));
        donor.setPreferredContactMethod(ct);
      } catch (Exception ex) {
        ex.printStackTrace();
        donor.setPreferredContactMethod(null);
      }
    }
  }
  
  public String getBloodAbo() {
    if (StringUtils.isBlank(donor.getBloodAbo()) || donor.getBloodAbo() == null){
      return "";
    }
    else{
      return donor.getBloodAbo();
    }
  }
  
  public void setBloodAbo(String bloodAbo) {
	  if (StringUtils.isBlank(bloodAbo)){
		  donor.setBloodAbo(null);
	  }
	  else{
		  donor.setBloodAbo(bloodAbo);
	  }    
  }
  
  public String getBloodRh() {
    if (StringUtils.isBlank(donor.getBloodRh()) || donor.getBloodRh() == null){
      return "";
    }
    else{
      return donor.getBloodRh();
    }
  }
  
  public void setBloodRh(String bloodRh) {
	  if (StringUtils.isBlank(bloodRh)){
		  donor.setBloodRh(null);
	  }
	  else{
		  donor.setBloodRh(bloodRh);
	  }    
  }

}