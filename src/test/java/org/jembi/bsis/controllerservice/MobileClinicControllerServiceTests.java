package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonorOutcomesViewModelBuilder.aDonorOutcomesViewModel;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.factory.DonorOutcomesViewModelFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class MobileClinicControllerServiceTests extends UnitTestSuite {
  
  @InjectMocks
  private MobileClinicControllerService mobileClinicControllerService;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private DonorOutcomesViewModelFactory donorOutcomesViewModelFactory;
  
  @Test
  public void testGetDonorOutcomes_shouldReturnCorrectViewModels() {
    // Set up fixture
    long venueId = 1;
    Date startDate = new DateTime().minusDays(30).toDate();
    Date endDate = new DateTime().minusDays(7).toDate();
    
    Location donorVenue = aVenue().withId(1L).build();
    List<Donation> donations = Arrays.asList(aDonation().withId(1L).build(), aDonation().withId(2L).build());
    
    // Set up expectations
    List<DonorOutcomesViewModel> expectedViewModels = Arrays.asList(
        aDonorOutcomesViewModel().build(),
        aDonorOutcomesViewModel().build()
    );

    when(locationRepository.getLocation(venueId)).thenReturn(donorVenue);
    when(donationRepository.findLastDonationsByDonorVenueAndDonationDate(donorVenue, startDate, endDate)).thenReturn(donations);
    when(donorOutcomesViewModelFactory.createDonorOutcomesViewModels(donations)).thenReturn(expectedViewModels);
    
    // Exercise SUT
    List<DonorOutcomesViewModel> returnedViewModels = mobileClinicControllerService.getDonorOutcomes(venueId,
        startDate, endDate);
    
    // Verify
    assertThat(returnedViewModels, is(expectedViewModels));
  }

}
