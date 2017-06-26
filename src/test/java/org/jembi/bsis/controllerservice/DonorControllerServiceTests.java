package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationFullViewModelBuilder.aDonationFullViewModel;
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
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonorControllerServiceTests extends UnitTestSuite {
  
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
    UUID donationId1 = UUID.randomUUID();
    UUID donationId2 = UUID.randomUUID();

    List<Donation> donations = Arrays.asList(
        aDonation().withId(donationId1).build(),
        aDonation().withId(donationId2).build()
    );
    Donor donor = aDonor().withId(userId).withDonations(donations).build();
    
    // Expectations
    List<DonationFullViewModel> expectedFullViewModels = Arrays.asList(
        aDonationFullViewModel().withId(donationId1).build(),
        aDonationFullViewModel().withId(donationId2).build()
    );
    
    when(donorRepository.findDonorById(donorId)).thenReturn(donor);
    when(donationFactory.createDonationFullViewModelsWithPermissions(donations)).thenReturn(expectedFullViewModels);
    
    // Test
    List<DonationFullViewModel> returnedFullViewModels = donorControllerService.findDonationsForDonor(donorId);
    
    // Verifications
    assertThat(returnedFullViewModels, is(expectedFullViewModels));
  }
}
