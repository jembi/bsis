package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.PatientViewModel;

public class PatientViewModelMatcher extends AbstractTypeSafeMatcher<PatientViewModel> {

  public PatientViewModelMatcher(PatientViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, PatientViewModel patientViewModel) {
    description.appendText("A PatientViewModel with the following state:")
        .appendText("\nId: ").appendValue(patientViewModel.getId())
        .appendText("\nName one: ").appendValue(patientViewModel.getName1())
        .appendText("\nName two: ").appendValue(patientViewModel.getName2())
        .appendText("\nGender: ").appendValue(patientViewModel.getGender())
        .appendText("\nPatient number: ").appendValue(patientViewModel.getPatientNumber())
        .appendText("\nDate of birth: ").appendValue(patientViewModel.getDateOfBirth())
        .appendText("\nHospital blood bank number: ").appendValue(patientViewModel.getHospitalBloodBankNumber())
        .appendText("\nHospital ward number: ").appendValue(patientViewModel.getHospitalWardNumber())
        .appendText("\nBloodGroup: ").appendValue(patientViewModel.getBloodGroup());
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
