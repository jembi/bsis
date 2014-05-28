package backingform;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import javax.validation.Valid;
import model.address.Address;
import model.address.AddressType;
import model.address.Contact;
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

    private String dateOfFirstDonation;

    private Address address;

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

    public String getBirthDate() {
        if (birthDate != null) {
            return birthDate;
        }
        if (donor == null) {
            return "";
        }
        return CustomDateFormatter.getDateString(donor.getBirthDate());
    }

    public void setBirthDate() {

        if (year.isEmpty() || month.isEmpty() || dayOfMonth.isEmpty()) {
            donor.setBirthDate(null);
            return;
        }
        birthDate = dayOfMonth + "/" + month + "/" + year;
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

    public void setBirthDateEstimated(Boolean birthDateEstimated) {
        donor.setBirthDateEstimated(birthDateEstimated);
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
    
    public void setLastUpdated(Date lastUpDated){
        donor.setLastUpdated(lastUpDated);
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

    public String getNationalID() {
        return donor.getNationalID();
    }

    public void setNationalID(String nationalID) {
        donor.setNationalID(nationalID);
    }

    public void setIdType(IdType idType) {
        donor.setIdType(idType);
    }

    public void setIdNumber(String idNumber) {
        donor.setIdNumber(idNumber);
    }

    public String getIdNumber() {
        return donor.getIdNumber();
    }

    public String getDonorPanel() {
        Location donorPanel = donor.getDonorPanel();
        if (donorPanel == null || donorPanel.getId() == null) {
            return null;
        }
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
        } else {
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
        } else {
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
        if (idType == null || idType.getId() == null) {
            return null;
        }
        return idType.getId().toString();
    }

    public void setPreferredLanguage(String language) {

        if (StringUtils.isBlank(language)) {
            donor.setPreferredLanguage(null);
        } else {
            PreferredLanguage preferredLanguage = new PreferredLanguage();
            try {
                preferredLanguage.setId(Long.parseLong(language));
                donor.setPreferredLanguage(preferredLanguage);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                donor.setPreferredLanguage(null);
            }
        }
    }

    public String getPreferredLanguage() {
        PreferredLanguage preferredLanguage = donor.getPreferredLanguage();
        if (preferredLanguage == null || preferredLanguage.getId() == null) {
            return null;
        }
        return preferredLanguage.getId().toString();
    }

    public String getDateOfFirstDonation() {
        if (dateOfFirstDonation != null) {
            return dateOfFirstDonation;
        }
        if (dateOfFirstDonation == null) {
            return "";
        }
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

    /**
     * Home Address getter & Setters
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

    public void setHomeAddressLine1(String homeAddressLine1) {
        address.setHomeAddressline1(homeAddressLine1);
    }

    public void setHomeAddressLine2(String homeAddressLine1) {
        address.setHomeAddressline2(homeAddressLine1);
    }

    public void setHomeAddressCity(String homeAddressCity) {
        donor.getAddress().setHomeAddressCity(homeAddressCity);
    }
    
    public void setHomeAddressDistrict(String homeAddressDistrict){
        address.setHomeAddressDistrict(homeAddressDistrict);
    }

    public void setHomeAddressCountry(String homeAddressCountry) {
        donor.getAddress().setHomeAddressCity(homeAddressCountry);
    }

    public void setHomeAddressState(String homeAddressState) {
        donor.getAddress().setHomeAddressState(homeAddressState);
    }

    public void setHomeAddressProvince(String homeAddressProvince) {
        donor.getAddress().setHomeAddressProvince(homeAddressProvince);
    }

    public void setHomeAddressZipcode(String zipcode) {
        donor.getAddress().setHomeAddressZipcode(zipcode);
    }

    /**
     * Work Address Getters & Setters
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

    public void setWorkAddressLine1(String workAddressLine1) {
        address.setWorkAddressline1(workAddressLine1);
    }

    public void setWorkAddressLine2(String workAddressLine2) {
        address.setWorkAddressline2(workAddressLine2);
    }

    public void setWorkAddressCity(String workAddressCity) {
        address.setHomeAddressCity(workAddressCity);
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
        return donor.getAddress().getPostalAddressline1();
    }

    public String getPostalAddressLine2() {
        return donor.getAddress().getPostalAddressline2();
    }

    public String getPostalAddressCity() {
        return donor.getAddress().getPostalAddressCity();
    }

    public void setPostalAddressCity(String postalAddressCity) {
        address.setPostalAddressCity(postalAddressCity);
    }

    public String getPostalAddressProvince() {
        return donor.getAddress().getPostalAddressProvince();
    }

    public void setPostalAddressState(String postalAddressState) {
        donor.getAddress().setPostalAddressState(postalAddressState);
    }

    public String getPostalAddressState() {
        return donor.getAddress().getPostalAddressState();
    }

    public String getPostalAddressDistrict() {
        return donor.getAddress().getPostalAddressDistrict();
    }

    public void setWorkAddressline1(String workAddressline1) {
        address.setWorkAddressline1(workAddressline1);
    }

    public void setWorkAddressline2(String workAddressline2) {
        address.setWorkAddressline2(workAddressline2);
    }

    public void setPostalAddressProvince(String postalAdressProvince) {
        address.setPostalAddressProvince(postalAdressProvince);
    }

    public void setPostalAddressDistrict(String postalAddressDistrict) {
        address.setPostalAddressDistrict(postalAddressDistrict);
    }

    public String getPostalAddressCountry() {
        return donor.getAddress().getPostalAddressCountry();
    }

    public void setPostalAddressCountry(String postalAddressCountry) {
        address.setPostalAddressCountry(postalAddressCountry);
    }

    public String getPostalAddressZipcode() {
        return donor.getAddress().getPostalAddressZipcode();
    }

    public void setPostalAddressZipcode(String postalAddressZipcode) {
        address.setPostalAddressZipcode(postalAddressZipcode);
    }

    /**
     * Contact Getters & Setters
     */
    public ContactMethodType getContactType() {
        return donor.getContact().getContactMethodType();
    }

    public void setContactType(String contactTypeId) {
        if (StringUtils.isBlank(contactTypeId)) {
             contact.setContactMethodType(null);
        } else {
            ContactMethodType contactType = new ContactMethodType();
            contactType.setId(Integer.parseInt(contactTypeId));
            contact.setContactMethodType(contactType);
        }
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

    /**
     * Address Type Getters & Setters
     */
    public AddressType getAddressType() {
        return donor.getAddress().getAddressType();
    }

    public void setAddressType(String addressTypeID) {
        if (StringUtils.isBlank(addressTypeID)) {
            setAddressType(null);
        } else {
            AddressType addressType = new AddressType();
            addressType.setId(Long.parseLong(addressTypeID));
            address.setAddressType(addressType);
        }

    }

}
