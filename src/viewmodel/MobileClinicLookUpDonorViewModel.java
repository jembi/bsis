package viewmodel;

import java.util.Date;

import utils.CustomDateFormatter;
import model.util.Gender;

public class MobileClinicLookUpDonorViewModel {

    private long id;
    private String donorNumber;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    private String bloodAbo;
    private String bloodRh;
    private Boolean eligibility;
    
    public MobileClinicLookUpDonorViewModel() {
        // no-args constructor
    }

    /**
     * Do not remove or change the signature of this method.
     * 
     * @see {@link DonorRepository#findDonorSummaryByDonorNumber(String)}
     */
    public MobileClinicLookUpDonorViewModel(long id, String donorNumber, String firstName, String lastName, Gender gender, Date birthDate, String bloodAbo, String bloodRh) {
        this.id = id;
        this.donorNumber = donorNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.bloodAbo = bloodAbo;
        this.bloodRh = bloodRh;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getDonorNumber() {
        return donorNumber;
    }
    
    public void setDonorNumber(String donorNumber) {
        this.donorNumber = donorNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender == null ? "" : gender.name();
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return CustomDateFormatter.getDateString(birthDate);
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    
    public String getBloodAbo() {
        return bloodAbo;
    }
    
    public void setBloodAbo(String bloodAbo) {
        this.bloodAbo = bloodAbo;
    }
    
    public String getBloodRh() {
        return bloodRh;
    }
    
    public void setBloodRh(String bloodRh) {
        this.bloodRh = bloodRh;
    }
    
    public Boolean getEligibility() {
        return eligibility;
    }
    
    public void setEligibility(Boolean eligibility) {
        this.eligibility = eligibility;
    }
    

}
