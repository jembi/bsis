package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.DonationCRUDService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.UUID;

import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestBatchControllerServiceTests extends UnitTestSuite {

  @Mock private DonationRepository donationRepository;
  @Mock private DonationCRUDService donationCRUDService;
  @Mock private TestBatchRepository testBatchRepository;

  @InjectMocks private TestBatchControllerService controllerService;

  @Test
  public void testRemoveDonationsFromBatch_shouldDelegateToDonationCRUDService() {
    UUID testBatchId = UUID.randomUUID();
    UUID donationOneId = UUID.randomUUID();
    UUID donationTwoId = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = TestBatchDonationsBackingForm.builder().testBatchId(testBatchId)
        .donationIds(Arrays.asList(donationOneId, donationTwoId)).build();

    TestBatch testBatch = aTestBatch().withId(testBatchId).build();
    Donation donationOne = aDonation().withId(donationOneId).build();
    Donation donationTwo = aDonation().withId(donationTwoId).build();

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);
    when(donationRepository.findDonationById(donationOneId)).thenReturn(donationOne);
    when(donationRepository.findDonationById(donationTwoId)).thenReturn(donationTwo);

    controllerService.removeDonationsFromBatch(backingForm);

    verify(donationCRUDService).removeDonationsFromTestBatch(Arrays.asList(donationOne, donationTwo), testBatch);
  }
}
