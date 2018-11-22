package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.TestBatchDonationRangeBackingForm;
import org.jembi.bsis.backingform.TestBatchDonationsBackingForm;
import org.jembi.bsis.factory.TestBatchFactory;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.DonationAdditionResult;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.jembi.bsis.service.TestBatchCRUDService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.PackTypeBuilder.aPackType;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchDonationRangeBackingFormBuilder.aTestBatchDonationRangeBackingForm;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.OPEN;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestBatchControllerServiceTests extends UnitTestSuite {

  @Mock
  private TestBatchFactory testBatchFactory;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private TestBatchCRUDService testBatchCRUDService;
  @Mock
  private TestBatchRepository testBatchRepository;

  @InjectMocks
  private TestBatchControllerService controllerService;

  @Test
  public void testAddDonationsToTestBatchWithTestSampleProducingPackType_shouldSetTestBatch() {
    UUID testBatchId = UUID.randomUUID();
    String fromDIN = "from";
    String toDIN = "to";
    TestBatchDonationRangeBackingForm backingForm = aTestBatchDonationRangeBackingForm().withTestBatchId(testBatchId)
        .withFromDIN(fromDIN).withToDIN(toDIN).build();

    PackType packtype = aPackType()
        .withTestSampleProduced(true)
        .build();
    DonationBatch donationBatchClosed = aDonationBatch().thatIsClosed().build();
    Donation donationOne = aDonation().withId(UUID.randomUUID()).withDonationBatch(donationBatchClosed).thatIsNotDeleted().withPackType(packtype).build();
    Donation donationTwo = aDonation().withId(UUID.randomUUID()).withDonationBatch(donationBatchClosed).thatIsNotDeleted().withPackType(packtype).build();
    List<Donation> donations = Arrays.asList(donationOne, donationTwo);

    TestBatch testBatch = aTestBatch().withId(UUID.randomUUID()).withStatus(OPEN).withDonations(new HashSet<>()).build();

    DonationAdditionResult result = DonationAdditionResult.builder().testBatch(testBatch).build();

    TestBatchFullViewModel expected = TestBatchFullViewModel.builderFull().build();

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);
    when(donationRepository.findDonationsBetweenTwoDins(fromDIN, toDIN))
        .thenReturn(donations);
    when(testBatchCRUDService.addDonationsToTestBatch(testBatch, donations)).thenReturn(result);
    when(testBatchFactory.createTestBatchFullViewModel(testBatch, Collections.emptySet(), Collections.emptySet(), Collections.emptySet()))
        .thenReturn(expected);

    TestBatchFullViewModel actual = controllerService.addDonationsToTestBatch(backingForm);

    assertThat(actual, is(expected));
  }

  @Test
  public void testRemoveDonationsFromBatch_shouldDelegateToDonationCRUDService() {
    UUID testBatchId = UUID.randomUUID();
    UUID donationOneId = UUID.randomUUID();
    UUID donationTwoId = UUID.randomUUID();
    TestBatchDonationsBackingForm backingForm = TestBatchDonationsBackingForm.builder().testBatchId(testBatchId)
        .donationIds(Arrays.asList(donationOneId, donationTwoId)).build();

    TestBatch testBatch = aTestBatch().withId(testBatchId).withStatus(OPEN).withDonations(new HashSet<>()).build();
    Donation donationOne = aDonation().withId(donationOneId).build();
    Donation donationTwo = aDonation().withId(donationTwoId).build();
    testBatch.addDonation(donationOne);
    testBatch.addDonation(donationTwo);
    List<Donation> donations = Arrays.asList(donationOne, donationTwo);

    TestBatchFullViewModel expected = TestBatchFullViewModel.builderFull().build();

    when(testBatchRepository.findTestBatchById(testBatchId)).thenReturn(testBatch);
    when(donationRepository.findDonationById(donationOneId)).thenReturn(donationOne);
    when(donationRepository.findDonationById(donationTwoId)).thenReturn(donationTwo);
    when(testBatchCRUDService.removeDonationsFromTestBatch(donations, testBatch)).thenReturn(testBatch);
    when(testBatchFactory.createTestBatchFullViewModel(testBatch)).thenReturn(expected);

    TestBatchFullViewModel actual = controllerService.removeDonationsFromBatch(backingForm);

    verify(testBatchCRUDService).removeDonationsFromTestBatch(donations, testBatch);
    assertThat(actual, is(equalTo(expected)));
  }
}
