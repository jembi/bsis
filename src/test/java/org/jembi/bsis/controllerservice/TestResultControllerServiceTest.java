package org.jembi.bsis.controllerservice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBasicBloodTypingBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestFullViewModelBuilder.aBasicTTIBloodTestFullViewModel;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aReleasedTestBatch;
import static org.jembi.bsis.helpers.matchers.BloodTestingRuleResultMatcher.hasSameStateAsBloodTestingRuleResult;
import static org.jembi.bsis.helpers.matchers.DonationMatcher.hasSameStateAsDonation;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jembi.bsis.factory.TestSampleFactory;
import org.jembi.bsis.helpers.builders.BloodTestResultBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.service.BloodTestsService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.jembi.bsis.viewmodel.TestSampleViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TestResultControllerServiceTest extends UnitTestSuite {
  
  @InjectMocks
  private TestResultControllerService testResultControllerService;

  @Mock
  private BloodTestsService bloodTestsService;

  @Mock
  private TestSampleFactory testSampleFactory;

  @Mock
  private DonationRepository donationRepository;

  @Mock
  private BloodTestResultRepository bloodTestResultRepository;

  @Test
  public void testGetTestSample_shouldCallServices() throws Exception {
    Donation donation = aDonation().build();
    List<BloodTestResult> testOutcomes =
        Arrays.asList(BloodTestResultBuilder.aBloodTestResult().build());
    when(donationRepository.findDonationByDonationIdentificationNumber("din")).thenReturn(donation);
    when(bloodTestResultRepository.getTestOutcomes(donation))
        .thenReturn(testOutcomes);
    when(testSampleFactory.createViewModel(donation, testOutcomes))
        .thenReturn(TestSampleViewModel.builder().build());
    testResultControllerService.getTestSample("din");
    verify(donationRepository).findDonationByDonationIdentificationNumber("din");
    verify(bloodTestResultRepository).getTestOutcomes(donation);
    verify(testSampleFactory).createViewModel(donation, testOutcomes);
  }

  @Test
  public void testGetBloodTestingRuleResult_shouldCallService() throws Exception {
    Donation donation = aDonation().build();
    testResultControllerService.getBloodTestingRuleResult(donation);
    verify(bloodTestsService).executeTests(argThat(hasSameStateAsDonation(donation)));
  }

  @Test
  public void testGetBloodTestingRuleResults_shouldCallService() throws Exception {
    TestBatch testBatch = aReleasedTestBatch()
      .withDonation(aDonation().build())
      .withDonation(aDonation().build())
      .build();
    testResultControllerService.getBloodTestingRuleResults(testBatch);
    verify(bloodTestsService).executeTests(testBatch.getDonations());
  }

  @Test
  public void testGetBloodTestingRuleResultsWithBloodTestType_shouldFilterResults() throws Exception {
    Donation donation1 = aDonation().build();
    Donation donation2 = aDonation().build();
    TestBatch testBatch = aReleasedTestBatch()
      .withDonation(donation1)
      .withDonation(donation2)
      .build();
    BloodTestFullViewModel aboBloodTest = aBasicBloodTypingBloodTestFullViewModel().build();
    BloodTestFullViewModel ttiBloodTest = aBasicTTIBloodTestFullViewModel().build();
    UUID results1TTI = UUID.randomUUID();
    BloodTestResultFullViewModel results1ViewModel =
        BloodTestResultFullViewModel.builder().bloodTest(ttiBloodTest).result("POS").build();
    Map<UUID, BloodTestResultFullViewModel> results1 = new HashMap<>();
    results1.put(UUID.randomUUID(), BloodTestResultFullViewModel.builder().bloodTest(aboBloodTest).result("A").build());
    results1.put(results1TTI, results1ViewModel);
    UUID results2TTI = UUID.randomUUID();
    BloodTestResultFullViewModel results2ViewModel = BloodTestResultFullViewModel.builder().bloodTest(ttiBloodTest).result("NEG").build();
    Map<UUID, BloodTestResultFullViewModel> results2 = new HashMap<>();
    results2.put(UUID.randomUUID(), BloodTestResultFullViewModel.builder().bloodTest(aboBloodTest).result("O").build());
    results2.put(results2TTI, results2ViewModel);
    List<BloodTestingRuleResult> allResults = Arrays.asList(
        aBloodTestingRuleResult().withRecentResults(results1).build(),
        aBloodTestingRuleResult().withRecentResults(results2).build()
     );

    Map<UUID,BloodTestResultFullViewModel> expectedResults1 = new HashMap<>();
    expectedResults1.put(results1TTI, results1ViewModel);
    BloodTestingRuleResult expectedRuleResults1 = aBloodTestingRuleResult().withRecentResults(expectedResults1).build();
    Map<UUID,BloodTestResultFullViewModel> expectedResults2 = new HashMap<>();
    expectedResults2.put(results2TTI, results2ViewModel);
    BloodTestingRuleResult expectedRuleResults2 = aBloodTestingRuleResult().withRecentResults(expectedResults2).build();

    when(bloodTestsService.executeTests(testBatch.getDonations())).thenReturn(allResults);
    
    List<BloodTestingRuleResult> results = testResultControllerService.getBloodTestingRuleResults(BloodTestType.BASIC_TTI, testBatch);

    assertThat(results.size(), is(2));
    assertThat(results, hasItem(hasSameStateAsBloodTestingRuleResult(expectedRuleResults1)));
    assertThat(results, hasItem(hasSameStateAsBloodTestingRuleResult(expectedRuleResults2)));
    verify(bloodTestsService).executeTests(testBatch.getDonations());
  }

  @Test
  public void testGetBloodTestingRuleResultsWithNullBloodTestType_shouldReturnAllResults() throws Exception {
    Donation donation1 = aDonation().build();
    Donation donation2 = aDonation().build();
    TestBatch testBatch = aReleasedTestBatch()
      .withDonation(donation1)
      .withDonation(donation2)
      .build();
    BloodTestFullViewModel aboBloodTest = aBasicBloodTypingBloodTestFullViewModel().build();
    BloodTestFullViewModel ttiBloodTest = aBasicTTIBloodTestFullViewModel().build();
    UUID results1TTI = UUID.randomUUID();
    BloodTestResultFullViewModel results1ViewModel =
        BloodTestResultFullViewModel.builder().bloodTest(ttiBloodTest).result("POS").build();
    Map<UUID, BloodTestResultFullViewModel> results1 = new HashMap<>();
    results1.put(UUID.randomUUID(), BloodTestResultFullViewModel.builder().bloodTest(aboBloodTest).result("A").build());
    results1.put(results1TTI, results1ViewModel);
    UUID results2TTI = UUID.randomUUID();
    BloodTestResultFullViewModel results2ViewModel =
        BloodTestResultFullViewModel.builder().bloodTest(ttiBloodTest).result("NEG").build();
    Map<UUID, BloodTestResultFullViewModel> results2 = new HashMap<>();
    results2.put(UUID.randomUUID(), BloodTestResultFullViewModel.builder().bloodTest(aboBloodTest).result("O").build());
    results2.put(results2TTI, results2ViewModel);
    List<BloodTestingRuleResult> allResults = Arrays.asList(
        aBloodTestingRuleResult().withRecentResults(results1).build(),
        aBloodTestingRuleResult().withRecentResults(results2).build()
     );

    when(bloodTestsService.executeTests(testBatch.getDonations())).thenReturn(allResults);
    
    List<BloodTestingRuleResult> results = testResultControllerService.getBloodTestingRuleResults(null, testBatch);

    assertThat(results.size(), is(2));
    assertThat(results, hasItem(hasSameStateAsBloodTestingRuleResult(allResults.get(0))));
    assertThat(results, hasItem(hasSameStateAsBloodTestingRuleResult(allResults.get(1))));
    verify(bloodTestsService).executeTests(testBatch.getDonations());
  }
}
