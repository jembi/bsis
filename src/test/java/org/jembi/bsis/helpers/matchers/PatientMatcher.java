package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.patient.Patient;


public class PatientMatcher extends TypeSafeMatcher<Patient> {

  private Patient expected;

  public PatientMatcher(Patient expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Patient with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName one: ").appendValue(expected.getName1())
        .appendText("\nName two: ").appendValue(expected.getName2())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nPatient number: ").appendValue(expected.getPatientNumber())
        .appendText("\nDate of birth: ").appendValue(expected.getDateOfBirth())
        .appendText("\nHospital blood bank number: ").appendValue(expected.getHospitalBloodBankNumber())
        .appendText("\n:Hospital ward number: ").appendValue(expected.getHospitalWardNumber())
        .appendText("\nBloodAbo: ").appendValue(expected.getBloodAbo())
        .appendText("\nBloodRh: ").appendValue(expected.getBloodRh());
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