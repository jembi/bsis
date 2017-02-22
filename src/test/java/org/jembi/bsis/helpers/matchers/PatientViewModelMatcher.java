package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.PatientViewModel;

public class PatientViewModelMatcher extends TypeSafeMatcher<PatientViewModel> {

  private PatientViewModel expected;

  public PatientViewModelMatcher(PatientViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A PatientViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName one: ").appendValue(expected.getName1())
        .appendText("\nName two: ").appendValue(expected.getName2())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nPatient number: ").appendValue(expected.getPatientNumber())
        .appendText("\nDate of birth: ").appendValue(expected.getDateOfBirth())
        .appendText("\nHospital blood bank number: ").appendValue(expected.getHospitalBloodBankNumber())
        .appendText("\nHospital ward number: ").appendValue(expected.getHospitalWardNumber())
        .appendText("\nBloodGroup: ").appendValue(expected.getBloodGroup());
  }

  @Override
  protected boolean matchesSafely(PatientViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName1(), expected.getName1())
        && Objects.equals(actual.getName2(), expected.getName2())
        && Objects.equals(actual.getGender(), expected.getGender())
        && Objects.equals(actual.getPatientNumber(), expected.getPatientNumber())
        && Objects.equals(actual.getDateOfBirth(), expected.getDateOfBirth())
        && Objects.equals(actual.getHospitalBloodBankNumber(), expected.getHospitalBloodBankNumber())
        && Objects.equals(actual.getBloodGroup(), expected.getBloodGroup())
        && Objects.equals(actual.getHospitalWardNumber(), expected.getHospitalWardNumber());
  }
  
  public static PatientViewModelMatcher hasSameStateAsPatientViewModel(PatientViewModel expected) {
    return new PatientViewModelMatcher(expected);
  }
}
