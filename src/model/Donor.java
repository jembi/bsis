package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;

@Entity
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long donorId;
    private String donorNumber;
    private String firstName;
    private String lastName;
    private String gender;
    private String bloodType;
    private Date birthDate;
    private Integer age;
    private String address;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isDeleted;

    public Donor(String donorNumber, String firstName, String lastName, String gender, String bloodType, Date birthDate, Integer age, String address, Boolean isDeleted) {
        this.donorNumber = donorNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.bloodType = bloodType;
        this.birthDate = birthDate;
        this.age = age;
        this.address = address;
        this.isDeleted = isDeleted;
    }

    public Donor() {
    }

    public void copy(Donor otherDonor) {
        this.donorNumber = otherDonor.donorNumber;
        this.firstName = otherDonor.firstName;
        this.lastName = otherDonor.lastName;
        this.gender = otherDonor.gender;
        this.bloodType = otherDonor.bloodType;
        this.birthDate = otherDonor.birthDate;
        this.age = otherDonor.age;
        this.address = otherDonor.address;
        this.isDeleted = otherDonor.isDeleted;
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
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getDonorId() {
        return donorId;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getAge() {
        if (birthDate != null) {
            DateMidnight birthDate = new DateMidnight(getBirthDate());
            DateTime now = new DateTime();
            return Years.yearsBetween(birthDate, now).getYears();
        }
        return null;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
