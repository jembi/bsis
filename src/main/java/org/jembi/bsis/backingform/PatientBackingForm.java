package org.jembi.bsis.backingform;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PatientBackingForm {
  
  private Long id;
  
  @NotNull
  private String name1;
  
  @NotNull
  private String name2;
  
  private Date dateOfBirth;
  private Gender gender;
  private String patientNumber;
  private String hospitalBloodBankNumber;
  private String hospitalWardNumber;
  private String bloodAbo;
  private String bloodRh;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
  
  @JsonSerialize(using = DateTimeSerialiser.class)
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