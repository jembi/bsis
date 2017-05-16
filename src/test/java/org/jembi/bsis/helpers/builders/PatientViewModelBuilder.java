package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.viewmodel.PatientViewModel;

public class PatientViewModelBuilder extends AbstractBuilder<PatientViewModel> {
 
  private UUID id;
  private String name1;
  private String name2;
  private Date dateOfBirth;
  private Gender gender; 
  private String patientNumber;
  private String hospitalBloodBankNumber;
  private String hospitalWardNumber;
  private String bloodGroup;
  
  public PatientViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  } 
  
  public PatientViewModelBuilder withName1(String name1) {
    this.name1 = name1;
    return this;
  }
  
  public PatientViewModelBuilder withName2(String name2) {
    this.name2 = name2;
    return this;
  }
  
  public PatientViewModelBuilder withDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }
  
  public PatientViewModelBuilder withGender(Gender gender) {
    this.gender = gender;
    return this;
  }
  
  public PatientViewModelBuilder withPatientNumber(String patientNumber) {
    this.patientNumber = patientNumber;
    return this;
  }
  
  public PatientViewModelBuilder withHospitalBloodBankNumber(String hospitalBloodBankNumber) {
    this.hospitalBloodBankNumber = hospitalBloodBankNumber;
    return this;
  }
  
  public PatientViewModelBuilder withHospitalWardNumber(String hospitalWardNumber) {
    this.hospitalWardNumber = hospitalWardNumber;
    return this;
  }

  public PatientViewModelBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
    return this;
  }

  @Override
  public PatientViewModel build() {
    PatientViewModel patientViewModel = new PatientViewModel();
    patientViewModel.setId(id);
    patientViewModel.setName1(name1);
    patientViewModel.setName2(name2);
    patientViewModel.setDateOfBirth(dateOfBirth);
    patientViewModel.setGender(gender);
    patientViewModel.setPatientNumber(patientNumber);
    patientViewModel.setHospitalBloodBankNumber(hospitalBloodBankNumber);
    patientViewModel.setHospitalWardNumber(hospitalWardNumber);
    patientViewModel.setBloodGroup(bloodGroup);
    
    return patientViewModel;
  }
  
  public static PatientViewModelBuilder aPatientViewModel() {
    return new PatientViewModelBuilder();
  }

}
