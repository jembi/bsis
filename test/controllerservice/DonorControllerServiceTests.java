package controllerservice;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import factory.DonationViewModelFactory;
import model.donation.Donation;
import model.donor.Donor;
import repository.DonorRepository;
import suites.UnitTestSuite;
import viewmodel.DonationViewModel;

public class DonorControllerServiceTests extends UnitTestSuite {
  
  private static final long DONOR_ID = 17L;

  @InjectMocks
  private DonorControllerService donorControllerService;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationViewModelFactory donationViewModelFactory;
  
  @Test
  public void testFindDonationsForDonor_shouldReturnViewModel() {
    // Setup
    List<Donation> donations = Arrays.asList(
        aDonation().withId(1L).build(),
        aDonation().withId(8L).build()
    );
    Donor donor = aDonor().withId(USER_ID).withDonations(donations).build();
    
    // Expectations
    List<DonationViewModel> expectedViewModels = Arrays.asList(
        aDonationViewModel().withId(1L).build(),
        aDonationViewModel().withId(8L).build()
    );
    
    when(donorRepository.findDonorById(DONOR_ID)).thenReturn(donor);
    when(donationViewModelFactory.createDonationViewModelsWithPermissions(donations)).thenReturn(expectedViewModels);
    
    // Test
    List<DonationViewModel> returnedViewModels = donorControllerService.findDonationsForDonor(DONOR_ID);
    
    // Verifications
    assertThat(returnedViewModels, is(expectedViewModels));
  }
}
