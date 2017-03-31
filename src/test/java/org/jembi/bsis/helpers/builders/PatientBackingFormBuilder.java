package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.model.util.Gender;

public class PatientBackingFormBuilder extends AbstractBuilder<PatientBackingForm> {
  
  private UUID id;
  private String name1;
  private String name2;
  private Date dateOfBirth;
  private Gender gender;
  private String patientNumber;
  private String hospitalBloodBankNumber;
  private String hospitalWard;
  private String bloodGroup;
  
  public PatientBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public PatientBackingFormBuilder withName1(String name1) {
    this.name1 = name1;
    return this;
  }
  
  public PatientBackingFormBuilder withName2(String name2) {
    this.name2 = name2;
    return this;
  }
  
  public PatientBackingFormBuilder withDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }
  
  public PatientBackingFormBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }
  
  public PatientBackingFormBuilder withPatientNumber(String patientNumber) {
    this.patientNumber = patientNumber;
    return this;
  }
  
  public PatientBackingFormBuilder withHospitalBloodBankNumber(String hospitalBloodBankNumber) {
    this.hospitalBloodBankNumber = hospitalBloodBankNumber;
    return this;
  }
  
  public PatientBackingFormBuilder withHospitalWardNumber(String hospitalWard) {
    this.hospitalWard = hospitalWard;
    return this;
  }
  
  public PatientBackingFormBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
    return this;
  }
  
  @Override
  public PatientBackingForm build() {
    PatientBackingForm patientBackingForm = new PatientBackingForm();
    patientBackingForm.setId(id);
    patientBackingForm.setName1(name1);
    patientBackingForm.setName2(name2);
    patientBackingForm.setDateOfBirth(dateOfBirth);
    patientBackingForm.setGender(gender);
    patientBackingForm.setPatientNumber(patientNumber);
    patientBackingForm.setHospitalBloodBankNumber(hospitalBloodBankNumber);
    patientBackingForm.setHospitalWardNumber(hospitalWard);
    patientBackingForm.setBloodGroup(bloodGroup);
    
    return patientBackingForm;
  }
  
  public static PatientBackingFormBuilder aPatientBackingForm() {
    return new PatientBackingFormBuilder();
  }
}
