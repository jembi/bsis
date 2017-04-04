package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.controllerservice.DonorControllerService;
import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonorControllerServiceTests extends UnitTestSuite {
  
 // private static final long DONOR_ID = 17L;

  @InjectMocks
  private DonorControllerService donorControllerService;
  @Mock
  private DonorRepository donorRepository;
  @Mock
  private DonationFactory donationFactory;
  
  @Test
  public void testFindDonationsForDonor_shouldReturnViewModel() {
    // Setup
    UUID userId = UUID.randomUUID();
    UUID donorId = UUID.randomUUID();
    List<Donation> donations = Arrays.asList(
        aDonation().withId(1L).build(),
        aDonation().withId(8L).build()
    );
    Donor donor = aDonor().withId(userId).withDonations(donations).build();
    
    // Expectations
    List<DonationViewModel> expectedViewModels = Arrays.asList(
        aDonationViewModel().withId(1L).build(),
        aDonationViewModel().withId(8L).build()
    );
    
    when(donorRepository.findDonorById(donorId)).thenReturn(donor);
    when(donationFactory.createDonationViewModelsWithPermissions(donations)).thenReturn(expectedViewModels);
    
    // Test
    List<DonationViewModel> returnedViewModels = donorControllerService.findDonationsForDonor(donorId);
    
    // Verifications
    assertThat(returnedViewModels, is(expectedViewModels));
  }
}
