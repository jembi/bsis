package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.util.Gender;

public class PatientBuilder extends AbstractEntityBuilder<Patient> {
  private UUID id;
  private String name1;
  private String name2;
  private Date dateOfBirth;
  private Gender gender;
  private String patientNumber;
  private String hospitalBloodBankNumber;
  private String hospitalWardNumber;
  private String bloodAbo;
  private String bloodRh;

  public PatientBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public PatientBuilder withName1(String name1) {
    this.name1 = name1;
    return this;
  }

  public PatientBuilder withName2(String name2) {
    this.name2 = name2;
    return this;
  }

  public PatientBuilder withDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public PatientBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }

  public PatientBuilder withPatientNumber(String patientNumber) {
    this.patientNumber = patientNumber;
    return this;
  }

  public PatientBuilder withHospitalBloodBankNumber(String hospitalBloodBankNumber) {
    this.hospitalBloodBankNumber = hospitalBloodBankNumber;
    return this;
  }

  public PatientBuilder withHospitalWardNumber(String hospitalWardNumber) {
    this.hospitalWardNumber = hospitalWardNumber;
    return this;
  }

  public PatientBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public PatientBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  @Override
  public Patient build() {
    Patient patient = new Patient();
    patient.setId(id);
    patient.setName1(name1);
    patient.setName2(name2);
    patient.setDateOfBirth(dateOfBirth);
    patient.setGender(gender);
    patient.setPatientNumber(patientNumber);
    patient.setHospitalBloodBankNumber(hospitalBloodBankNumber);
    patient.setHospitalWardNumber(hospitalWardNumber);
    patient.setBloodAbo(bloodAbo);
    patient.setBloodRh(bloodRh);
    return patient;
  }

  public static PatientBuilder aPatient() {
    return new PatientBuilder();
  }

}
