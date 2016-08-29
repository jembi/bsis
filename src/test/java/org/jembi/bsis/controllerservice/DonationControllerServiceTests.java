package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class DonationControllerServiceTests extends UnitTestSuite {
  
  private static final long DONATION_ID = 90L;
  
  @InjectMocks
  private DonationControllerService donationControllerService;
  @Mock
  private DonationRepository donationRepository;
  
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
        .withDonationBatch(aDonationBatch().withTestBatch(null).build())
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
        .withDonationBatch(aDonationBatch().withTestBatch(aTestBatch().withStatus(testBatchStatus).build()).build())
        .build();
    
    // Set up expectations
    when(donationRepository.findDonationById(DONATION_ID)).thenReturn(donation);
    
    // Exercise SUT
    TestBatchStatus returnedStatus = donationControllerService.getTestBatchStatusForDonation(DONATION_ID);
    
    // Verify
    assertThat(returnedStatus, is(testBatchStatus));
  }

}
