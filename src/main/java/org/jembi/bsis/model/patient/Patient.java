package org.jembi.bsis.model.patient;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseUUIDEntity;
import org.jembi.bsis.model.util.Gender;

@Entity
@Audited
public class Patient extends BaseUUIDEntity {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Column(length = 20)
  private String name1;

  @NotBlank
  @Column(length = 20)
  private String name2;

  private Date dateOfBirth;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Column(length = 20)
  private String patientNumber;

  @Column(length = 20)
  private String hospitalBloodBankNumber;

  @Column(length = 20)
  private String hospitalWardNumber;

  @Column(length = 2)
  private String bloodAbo;

  @Column(length = 1)
  private String bloodRh;

  public String getName1() {
    return name1;
  }

  public void setName1(String name1) {
    this.name1 = name1;
  }

  public String getName2() {
    return name2;
  }

  public void setName2(String name2) {
    this.name2 = name2;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getPatientNumber() {
    return patientNumber;
  }

  public void setPatientNumber(String patientNumber) {
    this.patientNumber = patientNumber;
  }

  public String getHospitalBloodBankNumber() {
    return hospitalBloodBankNumber;
  }

  public void setHospitalBloodBankNumber(String hospitalBloodBankNumber) {
    this.hospitalBloodBankNumber = hospitalBloodBankNumber;
  }

  public String getHospitalWardNumber() {
    return hospitalWardNumber;
  }

  public void setHospitalWardNumber(String hospitalWardNumber) {
    this.hospitalWardNumber = hospitalWardNumber;
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

}
