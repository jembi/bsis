package model.donor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.BaseModificationTrackerEntity;
import model.location.Location;
import model.util.Gender;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import constraintvalidator.LocationExists;

@Entity
@Audited
@Table(name = "Donor",
    indexes = {
        @Index(name = "donor_donorNumber_index", columnList = "id", unique = true),
        @Index(name = "donor_firstName_index", columnList = "firstName", unique = false),
        @Index(name = "donor_lastName_index", columnList = "lastName", unique = false)
    })
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class MobileClinicDonor extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 20, unique = true)
  @Length(max = 20)
  private String donorNumber;

  @Column(length = 20)
  @Length(max = 20)
  private String firstName;

  @Length(max = 20)
  @Column(length = 20)
  private String lastName;

  @Enumerated(EnumType.STRING)
  @Column(length = 15)
  private Gender gender;

  @Column(length = 10)
  private String bloodAbo;

  @Column(length = 10)
  private String bloodRh;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private DonorStatus donorStatus;

  @Temporal(TemporalType.DATE)
  private Date birthDate;

  @LocationExists
  @ManyToOne
  private Location venue;

  private Boolean isDeleted;

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

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
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

  public DonorStatus getDonorStatus() {
    return donorStatus;
  }

  public void setDonorStatus(DonorStatus donorStatus) {
    this.donorStatus = donorStatus;
  }

  public Date getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(Date birthDate) {
    this.birthDate = birthDate;
  }

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
