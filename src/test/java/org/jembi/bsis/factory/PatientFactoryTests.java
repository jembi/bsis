package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.PatientBackingFormBuilder.aPatientBackingForm;
import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;
import static org.jembi.bsis.helpers.builders.PatientViewModelBuilder.aPatientViewModel;
import static org.jembi.bsis.helpers.matchers.PatientMatcher.hasSameStateAsPatient;
import static org.jembi.bsis.helpers.matchers.PatientViewModelMatcher.hasSameStateAsPatientViewModel;

import java.util.UUID;

import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.PatientViewModel;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.mockito.InjectMocks;

public class PatientFactoryTests extends UnitTestSuite {
  
  @InjectMocks
  private PatientFactory patientFactory;
  
  @Test
  public void testCreatePatientViewModel_shouldReturnViewModelWithCorrectState() {
    // Set up fixtures
    UUID uuid = UUID.randomUUID();
    LocalDate dateOfBirth = new LocalDate(1970, 05, 22);
    Patient patient = aPatient()
        .withId(uuid)
        .withName1("PatientName")
        .withName2("PatientSurname")
        .withGender(Gender.female)
        .withPatientNumber("79105235649")
        .withHospitalBloodBankNumber("hostpitalBloodBank")
        .withHospitalWardNumber("A200")
        .withDateOfBirth(dateOfBirth.toDate())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    
    PatientViewModel expectedViewModel = aPatientViewModel()
        .withId(uuid)
        .withName1("PatientName")
        .withName2("PatientSurname")
        .withGender(Gender.female)
        .withPatientNumber("79105235649")
        .withHospitalBloodBankNumber("hostpitalBloodBank")
        .withHospitalWardNumber("A200")
        .withDateOfBirth(dateOfBirth.toDate())
        .withBloodGroup("A+")
        .build();
    
    // Test
    PatientViewModel returnedPatientViewModel = patientFactory.createViewModel(patient);
    
    // Assertions
    assertThat(expectedViewModel, hasSameStateAsPatientViewModel(returnedPatientViewModel));
  }
  
  @Test
  public void testCreatePatientViewModelWithNoBloodGroup_shouldReturnEmptyStringForBloodGroup() {
    // Set up fixtures
    UUID uuid = UUID.randomUUID();
    LocalDate dateOfBirth = new LocalDate(1970, 05, 22);
    Patient patient = aPatient()
        .withId(uuid)
        .withName1("PatientName")
        .withName2("PatientSurname")
        .withGender(Gender.female)
        .withPatientNumber("79105235649")
        .withHospitalBloodBankNumber("hostpitalBloodBank")
        .withHospitalWardNumber("A200")
        .withDateOfBirth(dateOfBirth.toDate())
        .withBloodAbo(null)
        .withBloodRh(null)
        .build();
    
    // Test
    PatientViewModel returnedPatientViewModel = patientFactory.createViewModel(patient);
    
    // Assertions
    assertThat(returnedPatientViewModel.getBloodGroup(), is(""));
  }
  
  @Test
  public void testCreatePatientEntity_shouldReturnEntityWithCorrectState() {
    // Set up fixtures
    UUID uuid = UUID.randomUUID();
    LocalDate dateOfBirth = new LocalDate(1970, 05, 22);
    PatientBackingForm patientBackingForm = aPatientBackingForm()
        .withId(uuid)
        .withName1("PatientName")
        .withName2("PatientSurname")
        .withGender(Gender.female)
        .withPatientNumber("79105235649")
        .withHospitalBloodBankNumber("hostpitalBloodBank")
        .withHospitalWardNumber("A200")
        .withDateOfBirth(dateOfBirth.toDate())
        .withBloodGroup("A+")
        .build();
    
    Patient expectedPatient = aPatient()
        .withId(uuid)
        .withName1("PatientName")
        .withName2("PatientSurname")
        .withGender(Gender.female)
        .withPatientNumber("79105235649")
        .withHospitalBloodBankNumber("hostpitalBloodBank")
        .withHospitalWardNumber("A200")
        .withDateOfBirth(dateOfBirth.toDate())
        .withBloodAbo("A")
        .withBloodRh("+")
        .build();
    
    // Test
    Patient returnedPatient = patientFactory.createEntity(patientBackingForm);
    
    // Assertion
    assertThat(returnedPatient, hasSameStateAsPatient(expectedPatient));
  }
}
