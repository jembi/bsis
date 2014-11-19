package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import model.address.Address;
import model.address.AddressType;
import model.address.Contact;
import model.address.ContactMethodType;
import model.donor.Donor;
import model.donor.DonorStatus;
import model.idtype.IdType;
import model.location.Location;
import model.preferredlanguage.PreferredLanguage;
import model.user.User;
import model.util.Gender;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.time.DateTime;
import utils.CustomDateFormatter;

public class DonorBackingForm {

    @Valid
    @JsonIgnore
    private Donor donor;

    // store a local copy of birthdate string as validation may have failed
    private String birthDate;

    //to capture date of birth parameters--#11
    
    @Pattern(regexp = "[0-9]+", message = "Given Input Must be a number")
    String year;
    
    @Pattern(regexp = "[0-9]+",  message = "Given Input Must be a number")
    String month;
    
    @Pattern(regexp = "[0-9]+",  message = "Given Input Must be a number")
    String dayOfMonth;

    private Boolean ageFormatCorrect;

    private String ageSpecified;

    private String dateOfFirstDonation;

    @Valid
    private Address address;

    @Valid
    private Contact contact;

    public DonorBackingForm() {
        donor = new Donor();
        ageFormatCorrect = null;
        address = new Address();
        contact = new Contact();
    }

    public DonorBackingForm(Donor donor) {
        this.donor = donor;
    }
    
    @JsonIgnore
    public DonorStatus getDonorStatus() {
        return donor.getDonorStatus();
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
    
    public String getBirthDate() {
        return CustomDateFormatter.getDateString(donor.getBirthDate());
    }
    
    public void setBirthDate(String birthDate) {
        try {
            donor.setBirthDate(CustomDateFormatter.getDateFromString(birthDate));
        } catch (ParseException ex) {
            ex.printStackTrace();
            donor.setBirthDate(null);
        }
    } 
    
//
//    public DonorViewModel getDonorViewModel() {
//        return new DonorViewModel(donor);
//    }

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

    public String getTitle() {
        return donor.getTitle();

    }

    public void setTitle(String title) {
        donor.setTitle(title);
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

    public Boolean getBirthDateEstimated() {
        return donor.getBirthDateEstimated();
    }

    public String getGender() {
        if (donor == null || donor.getGender() == null) {
            return null;
        }
        return donor.getGender().toString();
    }
    
    @JsonIgnore
    public Date getLastUpdated() {
        return donor.getLastUpdated();
    }

    @JsonIgnore
    public Date getCreatedDate() {
        return donor.getCreatedDate();
    }
    
    @JsonIgnore
    public User getCreatedBy() {
        return donor.getCreatedBy();
    }

    @JsonIgnore
    public User getLastUpdatedBy() {
        return donor.getLastUpdatedBy();
    }
    
    public void setCreatedDate(Date createdDate) {
        donor.setCreatedDate(createdDate);
    }
    
    public void setCreatedBy(User createdBy) {
        donor.setCreatedBy(createdBy);
    }

    public void setLastUpdatedBy(User lastUpdatedBy) {
        donor.setLastUpdatedBy(lastUpdatedBy);
    }
    
    public void setLastUpdated(Date lastUpDated){
        donor.setLastUpdated(lastUpDated);
    }
    
    @JsonIgnore
    public Date getDateOfLastDonation() {
	    return donor.getDateOfLastDonation();
	}
	
	public void setDateOfLastDonation(Date dateOfLastDonation) {
	    donor.setDateOfLastDonation(dateOfLastDonation);
	}


    public String getNotes() {
        return donor.getNotes();
    }

    public Boolean getIsDeleted() {
        return donor.getIsDeleted();
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

    public void setBirthDateEstimated(Boolean birthDateEstimated) {
        donor.setBirthDateEstimated(birthDateEstimated);
    }


    public void setNotes(String notes) {
        donor.setNotes(notes);
    }

    public void setIsDeleted(Boolean isDeleted) {
        donor.setIsDeleted(isDeleted);
    }

    


    public String toString() {
        return donor.toString();
    }

    public String getAge() {
        if (donor.getBirthDateInferred() != null) {
            DateTime dt1 = new DateTime(donor.getBirthDateInferred());
            DateTime dt2 = new DateTime(new Date());
            int year1 = dt1.year().get();
            int year2 = dt2.year().get();
            return new Integer(year2 - year1).toString();
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

    public Boolean isAgeFormatCorrect() {
        return ageFormatCorrect;
    }
    
    public String getDonorPanel() {
        Location donorPanel = donor.getDonorPanel();
        if (donorPanel == null || donorPanel.getId() == null) {
            return null;
        }
        
        return donorPanel.getId().toString();
    }
    
    public void setDonorPanel(Location donorPanel) {
        if (donorPanel == null || donorPanel.getId() == null) {
            donor.setDonorPanel(null);
        } else {
            Location l = new Location();
            try {
                l.setId(donorPanel.getId());
                donor.setDonorPanel(l);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                donor.setDonorPanel(null);
            } 
        }
    }
    
    public String getPreferredLanguage() {

        if (donor.getPreferredLanguage()==null || donor.getPreferredLanguage().getId() == null) {
        	return null;
        }
        return donor.getPreferredLanguage().getId().toString();
        
    }
    
    public void setPreferredLanguage(PreferredLanguage preferredLanguage){
    	if (preferredLanguage == null){
    		donor.setPreferredLanguage(null);
    	}
    	else if (preferredLanguage.getId() == null){
    		donor.setPreferredLanguage(null);
    	}
    	else{
    		PreferredLanguage pl  = new PreferredLanguage();
	        pl.setId(preferredLanguage.getId());
	        donor.setPreferredLanguage(pl);
    	}
    }

    public String getDateOfFirstDonation() {
//        if (dateOfFirstDonation != null) { // Issue in editing form because of this lines 
//            return dateOfFirstDonation;
//        }
//        if (dateOfFirstDonation == null) {
//            return "";
//        }
        return CustomDateFormatter.getDateString(donor.getDateOfFirstDonation());
    }

    public void setDateOfFirstDonation(String dateOfFirstDonation) {
        this.dateOfFirstDonation = dateOfFirstDonation;
        try {
            donor.setDateOfFirstDonation(CustomDateFormatter.getDateFromString(dateOfFirstDonation));
        } catch (ParseException ex) {
            ex.printStackTrace();
            donor.setDateOfFirstDonation(null);
        }
    }

    public String getBloodAbo() {
        if (StringUtils.isBlank(donor.getBloodAbo()) || donor.getBloodAbo() == null) {
            return "";
        } else {
            return donor.getBloodAbo();
        }
    }

    public void setBloodAbo(String bloodAbo) {
        if (StringUtils.isBlank(bloodAbo)) {
            donor.setBloodAbo(null);
        } else {
            donor.setBloodAbo(bloodAbo);
        }
    }

    public String getBloodRh() {
        if (StringUtils.isBlank(donor.getBloodRh()) || donor.getBloodRh() == null) {
            return "";
        } else {
            return donor.getBloodRh();
        }
    }

    public void setBloodRh(String bloodRh) {
        if (StringUtils.isBlank(bloodRh)) {
            donor.setBloodRh(null);
        } else {
            donor.setBloodRh(bloodRh);
        }
    }
    
    @JsonIgnore
    public String getBloodGroup() {
	    if (StringUtils.isBlank(donor.getBloodAbo()) || StringUtils.isBlank(donor.getBloodRh()))
	      return "";
	    else
	      return donor.getBloodAbo() + donor.getBloodRh();
	}

    /**
     * Home Address getter & Setters
     */
    public String getHomeAddressLine1() {
        return address.getHomeAddressLine1();
    }

    public String getHomeAddressLine2() {
        return address.getHomeAddressLine2();
    }

    public String getHomeAddressCity() {
        return address.getHomeAddressCity();
    }

    public String getHomeAddressProvince() {
        return address.getHomeAddressProvince();
    }

    public String getHomeAddressDistrict() {
        return address.getHomeAddressDistrict();
    }

    public String getHomeAddressState() {
        return address.getHomeAddressState();
    }

    public String getHomeAddressCountry() {
        return address.getHomeAddressCountry();
    }

    public String getHomeAddressZipcode() {
        return address.getHomeAddressZipcode();
    }

    public void setHomeAddressLine1(String homeAddressLine1) {
        address.setHomeAddressLine1(homeAddressLine1);
    }

    public void setHomeAddressLine2(String homeAddressLine1) {
        address.setHomeAddressLine2(homeAddressLine1);
    }

    public void setHomeAddressCity(String homeAddressCity) {
        address.setHomeAddressCity(homeAddressCity);
    }
    
    public void setHomeAddressDistrict(String homeAddressDistrict){
        address.setHomeAddressDistrict(homeAddressDistrict);
    }

    public void setHomeAddressCountry(String homeAddressCountry) {
        address.setHomeAddressCountry(homeAddressCountry);
    }

    public void setHomeAddressState(String homeAddressState) {
        address.setHomeAddressState(homeAddressState);
    }

    public void setHomeAddressProvince(String homeAddressProvince) {
        address.setHomeAddressProvince(homeAddressProvince);
    }

    public void setHomeAddressZipcode(String zipcode) {
        address.setHomeAddressZipcode(zipcode);
    }

    /**
     * Work Address Getters & Setters
     */
    public String getWorkAddressLine1() {
        return address.getWorkAddressLine1();
    }

    public String getWorkAddressLine2() {
        return address.getWorkAddressLine2();
    }

    public String getWorkAddressCity() {
        return address.getWorkAddressCity();
    }

    public String getWorkAddressProvince() {
        return address.getWorkAddressProvince();
    }

    public String getWorkAddressDistrict() {
        return address.getWorkAddressDistrict();
    }

    public String getWorkAddressState() {
        return address.getWorkAddressCountry();
    }

    public String getWorkAddressCountry() {
        return address.getWorkAddressCountry();
    }

    public String getWorkAddressZipcode() {
        return address.getWorkAddressZipcode();
    }

    public void setWorkAddressLine1(String workAddressLine1) {
        address.setWorkAddressLine1(workAddressLine1);
    }
    public void setWorkAddressDistrict(String workAddressDistrict) {
        address.setWorkAddressDistrict(workAddressDistrict);
    }
    public void setWorkAddressLine2(String workAddressLine2) {
        address.setWorkAddressLine2(workAddressLine2);
    }

    public void setWorkAddressCity(String workAddressCity) {
        address.setWorkAddressCity(workAddressCity);
    }

    public void setWorkAddressProvince(String workAddressProvince) {
        address.setWorkAddressProvince(workAddressProvince);
    }

    public void setWorkAddressState(String workAddressState) {
        address.setWorkAddressState(workAddressState);
    }

    public void setWorkAddressCountry(String workAddressCountry) {
        address.setWorkAddressCountry(workAddressCountry);
    }

    public void setworkAddressZipcode(String workAddressZipcode) {
        address.setWorkAddressZipcode(workAddressZipcode);
    }

    /**
     * Postal Address getters & Setters
     */
    public String getPostalAddressLine1() {
        return address.getPostalAddressLine1();
    }

    public String getPostalAddressLine2() {
        return address.getPostalAddressLine2();
    }
    
    public void setPostalAddressLine1(String postalAddressLine1){
        address.setPostalAddressLine1(postalAddressLine1);
    }
    
    public void setPostalAddressLine2(String postalAddressLine2){
        address.setPostalAddressLine2(postalAddressLine2);
    }

    public String getPostalAddressCity() {
        return address.getPostalAddressCity();
    }

    public void setPostalAddressCity(String postalAddressCity) {
        address.setPostalAddressCity(postalAddressCity);
    }

    public String getPostalAddressProvince() {
        return address.getPostalAddressProvince();
    }

    public void setPostalAddressState(String postalAddressState) {
        address.setPostalAddressState(postalAddressState);
    }

    public String getPostalAddressState() {
        return address.getPostalAddressState();
    }

    public String getPostalAddressDistrict() {
        return address.getPostalAddressDistrict();
    }

    public void setPostalAddressProvince(String postalAdressProvince) {
        address.setPostalAddressProvince(postalAdressProvince);
    }

    public void setPostalAddressDistrict(String postalAddressDistrict) {
        address.setPostalAddressDistrict(postalAddressDistrict);
    }

    public String getPostalAddressCountry() {
        return address.getPostalAddressCountry();
    }

    public void setPostalAddressCountry(String postalAddressCountry) {
        address.setPostalAddressCountry(postalAddressCountry);
    }

    public String getPostalAddressZipcode() {
        return address.getPostalAddressZipcode();
    }

    public void setPostalAddressZipcode(String postalAddressZipcode) {
        address.setPostalAddressZipcode(postalAddressZipcode);
    }

    

    public String getMobileNumber() {
        return contact.getMobileNumber();
    }

    public String getHomeNumber() {
        return contact.getHomeNumber();
    }

    public String getWorkNumber() {
        return contact.getWorkNumber();
    }

    public void setMobileNumber(String mobileNumber) {
        contact.setMobileNumber(mobileNumber);
    }

    public void setHomeNumber(String homeNumber) {
        contact.setHomeNumber(homeNumber);
    }

    public void setWorkNumber(String workNumber) {
        contact.setWorkNumber(workNumber);
    }

    public void setEmail(String email) {
        contact.setEmail(email);
    }
    
    public String getEmail(){
        return contact.getEmail();
        
    }

    public Address getAddress() {
        return address;    
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    
    public Contact getContact(){
        return contact;
    }

    public void setContact(Contact contact){
        this.contact = contact ;
    }
    
    public String getPreferredAddressType() {
        
        if (donor.getAddressType()==null  || donor.getAddressType().getId() == null) {
            return null;
        }
        return donor.getAddressType().getId().toString();
    }
    
    public void setPreferredAddressType(AddressType addressType){
        donor.setAddressType(addressType);
    }
    
    public String getIdType() {
        if (donor.getIdType()==null || donor.getIdType().getId() == null) {
            return null;
        }
        return donor.getIdType().getId().toString();
    }
    
    public void setIdType(IdType idType){
    	if(idType == null){
    		donor.setIdType(null);
    	}
    	else if(idType.getId() == null){
    		donor.setIdType(null);
    	}
    	else{
	    	IdType idt = new IdType();
	    	idt.setId(idType.getId());
	        donor.setIdType(idt);
    	}
    }
    
    public String getIdNumber(){
        return donor.getIdNumber();
    }
    public void setIdNumber(String idNumber){
        donor.setIdNumber(idNumber);
    }
    
    public String getContactMethodType(){
        
        if (donor.getContactMethodType()==null || donor.getContactMethodType().getId() == null) {
            return null;
        }
        return donor.getContactMethodType().getId().toString();
        
    }
    
    public void setContactMethodType(ContactMethodType contactMethodType){
    	if (contactMethodType == null){
    		donor.setContactMethodType(null);
    	}
    	else if (contactMethodType.getId() == null){
    		donor.setContactMethodType(null);
    	}
    	else{
	    	ContactMethodType cmt  = new ContactMethodType();
	        cmt.setId(contactMethodType.getId());
	        donor.setContactMethodType(cmt);
    	}
    }    
    
   @JsonIgnore
    public String getContactId(){
        if(contact.getId()!=null)     
        return contact.getId().toString();
        return "";
    }
    
    public void setContactId(String contactId){
        contact.setId(Long.parseLong(contactId));
    }
    
    @JsonIgnore
    public String getAddressId(){
        if(address.getId()!=null)
        return  address.getId().toString();
        return "";
    }
    
    public void setAddressId(String addressId){
        address.setId(Long.parseLong(addressId));
    }
}
