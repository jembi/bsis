package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationControllerServiceTests extends UnitTestSuite {
  
  private static final UUID DONATION_ID = UUID.randomUUID();

  @Mock
  private DonationFactory donationFactory;
  @Mock
  private DonationRepository donationRepository;

  @InjectMocks
  private DonationControllerService donationControllerService;

  @Test
  public void testGetTestBatchStatusWithNullDonationBatch_shouldReturnNull() {
    // Set up fixture
    Donation donation = aDonation().withId(DONATION_ID).withDonationBatch(null).build();
    
    // Set up expectations
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // Exercise SUT
    TestBatchStatus returnedStatus = donationControllerService.getTestBatchStatusForDonation(DONATION_ID);
    
    // Verify
    assertThat(returnedStatus, is(nullValue()));
  }
  
  @Test
  public void testGetTestBatchStatusWithNullTestBatch_shouldReturnNull() {
    // Set up fixture
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationBatch(aDonationBatch().build())
        .withTestBatch(null)
        .build();
    
    // Set up expectations
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // Exercise SUT
    TestBatchStatus returnedStatus = donationControllerService.getTestBatchStatusForDonation(DONATION_ID);
    
    // Verify
    assertThat(returnedStatus, is(nullValue()));
  }
  
  @Test
  public void testGetTestBatchStatusWithTestBatch_shouldReturnTestBatchStatus() {
    // Set up fixture
    TestBatchStatus testBatchStatus = TestBatchStatus.RELEASED;
    Donation donation = aDonation()
        .withId(DONATION_ID)
        .withDonationBatch(aDonationBatch().build())
        .withTestBatch(aTestBatch().withStatus(testBatchStatus).build())
        .build();
    
    // Set up expectations
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // Exercise SUT
    TestBatchStatus returnedStatus = donationControllerService.getTestBatchStatusForDonation(DONATION_ID);
    
    // Verify
    assertThat(returnedStatus, is(testBatchStatus));
  }

  @Test
  public void testFindByDIN_shouldDelegateToRepositoryAndFactory() {
    String din = "din";

    Donation donation = aDonation().build();
    DonationViewModel expected = aDonationViewModel().build();

    when(donationRepository.findDonationByDonationIdentificationNumber(din)).thenReturn(donation);
    when(donationFactory.createDonationViewModel(donation)).thenReturn(expected);

    DonationViewModel actual = donationControllerService.findByDIN(din);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testFindByVenueAndPackTypeInRange_shouldDelegateToRepositoryAndFactory() {
    UUID venueId = UUID.randomUUID();
    UUID packTypeId = UUID.randomUUID();
    Date startDate = Date.from(Instant.now().minus(1, ChronoUnit.DAYS));
    Date endDate = Date.from(Instant.now());

    Donation donationOne = aDonation().build();
    Donation donationTwo = aDonation().build();
    DonationViewModel donationViewModelOne = aDonationViewModel().build();
    DonationViewModel donationViewModelTwo = aDonationViewModel().build();

    when(donationRepository.findByVenueAndPackTypeInRange(venueId, packTypeId, startDate, endDate))
        .thenReturn(Arrays.asList(donationOne, donationTwo));
    when(donationFactory.createDonationViewModel(donationOne)).thenReturn(donationViewModelOne);
    when(donationFactory.createDonationViewModel(donationTwo)).thenReturn(donationViewModelTwo);

    Collection<DonationViewModel> actual = donationControllerService
        .findByVenueAndPackTypeInRange(venueId, packTypeId, startDate, endDate);

    assertThat(actual, hasItems(donationViewModelOne, donationViewModelTwo));
  }

}
