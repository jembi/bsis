package org.jembi.bsis.factory;

import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.util.BloodGroup;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.springframework.stereotype.Service;

@Service
public class PatientFactory {
  
  public PatientViewModel createViewModel(Patient patient) {
    PatientViewModel patientViewModel = new PatientViewModel();
    String bloodGroup = "";
    if (patient.getBloodAbo() != null && patient.getBloodRh() != null) {
      bloodGroup = patient.getBloodAbo() + patient.getBloodRh();
    }
    patientViewModel.setId(patient.getId());
    patientViewModel.setName1(patient.getName1());
    patientViewModel.setName2(patient.getName2());
    patientViewModel.setDateOfBirth(patient.getDateOfBirth());
    patientViewModel.setPatientNumber(patient.getPatientNumber());
    patientViewModel.setHospitalBloodBankNumber(patient.getHospitalBloodBankNumber());
    patientViewModel.setHospitalWardNumber(patient.getHospitalWardNumber());
    patientViewModel.setGender(patient.getGender());
    patientViewModel.setBloodGroup(bloodGroup);
    
    return patientViewModel;
  
  }
  
  public Patient createEntity(PatientBackingForm patientBackingForm) {
    Patient patient = new Patient();
    patient.setId(patientBackingForm.getId());
    patient.setName1(patientBackingForm.getName1());
    patient.setName2(patientBackingForm.getName2());
    patient.setDateOfBirth(patientBackingForm.getDateOfBirth());
    patient.setGender(patientBackingForm.getGender());
    patient.setPatientNumber(patientBackingForm.getPatientNumber());
    patient.setHospitalBloodBankNumber(patientBackingForm.getHospitalBloodBankNumber());
    patient.setHospitalWardNumber(patientBackingForm.getHospitalWardNumber());
    BloodGroup bloodGroup = new BloodGroup(patientBackingForm.getBloodGroup());
    patient.setBloodAbo(bloodGroup.getBloodAbo());
    patient.setBloodRh(bloodGroup.getBloodRh());
    
    return patient;
  }

}
