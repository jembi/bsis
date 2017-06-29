package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.patient.Patient;


public class PatientMatcher extends AbstractTypeSafeMatcher<Patient> {

  public PatientMatcher(Patient expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, Patient patient) {
    description.appendText("A Patient with the following state:")
        .appendText("\nId: ").appendValue(patient.getId())
        .appendText("\nName one: ").appendValue(patient.getName1())
        .appendText("\nName two: ").appendValue(patient.getName2())
        .appendText("\nGender: ").appendValue(patient.getGender())
        .appendText("\nPatient number: ").appendValue(patient.getPatientNumber())
        .appendText("\nDate of birth: ").appendValue(patient.getDateOfBirth())
        .appendText("\nHospital blood bank number: ").appendValue(patient.getHospitalBloodBankNumber())
        .appendText("\n:Hospital ward number: ").appendValue(patient.getHospitalWardNumber())
        .appendText("\nBloodAbo: ").appendValue(patient.getBloodAbo())
        .appendText("\nBloodRh: ").appendValue(patient.getBloodRh());
  }

  @Override
  protected boolean matchesSafely(Patient actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName1(), expected.getName1())
        && Objects.equals(actual.getName2(), expected.getName2())
        && Objects.equals(actual.getGender(), expected.getGender())
        && Objects.equals(actual.getPatientNumber(), expected.getPatientNumber())
        && Objects.equals(actual.getDateOfBirth(), expected.getDateOfBirth())
        && Objects.equals(actual.getHospitalBloodBankNumber(), expected.getHospitalBloodBankNumber())
        && Objects.equals(actual.getBloodAbo(), expected.getBloodAbo())
        && Objects.equals(actual.getBloodRh(), expected.getBloodRh())
        && Objects.equals(actual.getHospitalWardNumber(), expected.getHospitalWardNumber());
  }
  
  public static PatientMatcher hasSameStateAsPatient(Patient expected) {
    return new PatientMatcher(expected);
  }
}