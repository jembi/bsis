package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.DonorOutcomesViewModelBuilder.aDonorOutcomesViewModel;
import static org.jembi.bsis.helpers.matchers.DonorOutcomesViewModelMatcher.withSameStateAsDonorOutcomesViewModel;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class DonorOutcomesViewModelFactoryTests extends UnitTestSuite {

  @Spy
  @InjectMocks
  private DonorOutcomesViewModelFactory donorOutcomesViewModelFactory;
  @Mock
  private BloodTestResultFactory bloodTestResultFactory;
  
  @Test
  public void testCreateDonorOutcomesViewModel_shouldReturnViewModelWithTheCorrectState() {
    
    // Set up fixture
    String donationIdentificationNumber = "0099997";
    Date donationDate = new Date();
    String donorNumber = "011199";
    String firstName = "Test";
    String lastName = "Donor";
    Gender gender = Gender.female;
    Date birthDate = new DateTime().minusYears(40).toDate();
    String bloodAbo = "A";
    String bloodRh = "+";
    
    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult().withId(UUID.randomUUID()).build(),
        aBloodTestResult().withId(UUID.randomUUID()).build()
    );
    
    Donation donation = aDonation()
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withDonationDate(donationDate)
        .withDonor(aDonor()
            .withDonorNumber(donorNumber)
            .withFirstName(firstName)
            .withGender(gender)
            .withLastName(lastName)
            .withBirthDate(birthDate)
            .withBloodAbo(bloodAbo)
            .withBloodRh(bloodRh)
            .build())
        .withBloodTestResults(bloodTestResults)
        .build();
    
    // Set up expectations
    List<BloodTestResultFullViewModel> bloodTestResultFullViewModels = Arrays.asList(
        BloodTestResultFullViewModel.builder().id(UUID.randomUUID()).build(),
        BloodTestResultFullViewModel.builder().id(UUID.randomUUID()).build()
    );

    when(bloodTestResultFactory.createFullViewModels(bloodTestResults))
        .thenReturn(bloodTestResultFullViewModels);
    
    DonorOutcomesViewModel expectedViewModel = aDonorOutcomesViewModel()
        .withDonorNumber(donorNumber)
        .withFirstName(firstName)
        .withLastName(lastName)
        .withGender(gender)
        .withBirthDate(birthDate)
        .withDonationDate(donationDate)
        .withDonationIdentificationNumber(donationIdentificationNumber)
        .withBloodAbo(bloodAbo)
        .withBloodRh(bloodRh)
        .withBloodTestResults(bloodTestResultFullViewModels)
        .build();
    
    // Exercise SUT
    DonorOutcomesViewModel returnedViewModel = donorOutcomesViewModelFactory.createDonorOutcomesViewModel(donation);
    
    // Verify
    assertThat(returnedViewModel, withSameStateAsDonorOutcomesViewModel(expectedViewModel));
  }
  
  @Test
  public void testCreateDonorOutcomeViewModels_shouldDelegateToCreateDonorOutcomesViewModel() {
    // Set up fixture
    Donation firstDonation = aDonation().withId(UUID.randomUUID()).build();
    Donation secondDonation = aDonation().withId(UUID.randomUUID()).build();
    
    // Set up expectations
    DonorOutcomesViewModel firstViewModel = aDonorOutcomesViewModel().build();
    DonorOutcomesViewModel secondViewModel = aDonorOutcomesViewModel().build();
    
    doReturn(firstViewModel).when(donorOutcomesViewModelFactory).createDonorOutcomesViewModel(firstDonation);
    doReturn(secondViewModel).when(donorOutcomesViewModelFactory).createDonorOutcomesViewModel(secondDonation);
    
    List<DonorOutcomesViewModel> expectedViewModels = Arrays.asList(firstViewModel, secondViewModel);
    
    // Exercise SUT
    List<DonorOutcomesViewModel> returnedViewModels = donorOutcomesViewModelFactory.createDonorOutcomesViewModels(
        Arrays.asList(firstDonation, secondDonation));
    
    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }
}
