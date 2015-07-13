package viewmodel;

import java.util.Date;

import utils.CustomDateFormatter;
import model.util.Gender;

/**
 * View model representing a summarised view of a donor.
 */
public class DonorSummaryViewModel {
    
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date birthDate;
    
    public DonorSummaryViewModel() {
        // no-args constructor
    }

    public DonorSummaryViewModel(String firstName, String lastName, Gender gender, Date birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
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

}
