package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.jembi.bsis.helpers.builders.LocationBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.jembi.bsis.viewmodel.TestSampleViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TestSampleFactoryTests extends UnitTestSuite {

  @InjectMocks
  private TestSampleFactory testSampleFactory;

  @Mock
  private BloodTestResultFactory bloodTestResultFactory;

  @Test
  public void testCreateFullViewModel_shouldReturnViewModelWithTheCorrectState() {
    Date donationDate = new Date();
    Date testBatchDate = new Date();
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    
    TestBatch testBatch = aTestBatch()
        .withLocation(LocationBuilder.aTestingSite().withName("testingSite").build())
        .withTestBatchDate(testBatchDate)
        .build();

    Donation donation = aDonation()
        .withDonationIdentificationNumber("din")
        .withVenue(aVenue().withName("venue").build())
        .withDonationDate(donationDate).withBloodAbo("A").withBloodRh("+")
        .withPackType(PackTypeBuilder.aPackType().withPackType("packType").build())
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withTestBatch(testBatch)
        .build();
    
    List<BloodTestResult> testOutcomes = Arrays.asList(
        aBloodTestResult().withId(id1).build(), 
        aBloodTestResult().withId(id2).build());
    
    List<BloodTestResultViewModel> testOutcomeViewModels = Arrays.asList(
        BloodTestResultViewModel.builder().id(id1).build(),
        BloodTestResultViewModel.builder().id(id2).build());

    TestSampleViewModel expected = TestSampleViewModel.builder()
        .din("din")
        .venue("venue")
        .donationDate(donationDate).bloodGroup("A+").packType("packType").ttiStatus(TTIStatus.SAFE)
        .bloodTypingStatus(BloodTypingStatus.COMPLETE)
        .bloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .testingSite("testingSite").testingDate(testBatchDate)
        .testOutcomes(testOutcomeViewModels)
        .build();

    when(bloodTestResultFactory.createViewModels(testOutcomes)).thenReturn(testOutcomeViewModels);

    TestSampleViewModel actual =
        testSampleFactory.createViewModel(donation, testOutcomes);

    assertThat(actual, is(equalTo(expected)));
  }
}
