package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aTestingSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchFullViewModelBuilder.aTestBatchFullViewModel;
import static org.jembi.bsis.helpers.builders.TestBatchViewModelBuilder.aTestBatchViewModel;
import static org.jembi.bsis.helpers.matchers.DonationTestOutcomesReportViewModelMatcher.hasSameStateAsDonationTestOutcomesReportViewModel;
import static org.jembi.bsis.helpers.matchers.TestBatchFullViewModelMatcher.hasSameStateAsTestBatchFullViewModel;
import static org.jembi.bsis.helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.jembi.bsis.helpers.matchers.TestBatchViewModelMatcher.hasSameStateAsTestBatchViewModel;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.helpers.builders.DonationTestOutcomesReportViewModelBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTypingStatus;
import org.jembi.bsis.service.TestBatchConstraintChecker;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationBatchFullViewModel;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchViewModel;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TestBatchFactoryTests extends UnitTestSuite {

  private static final Long IRRELEVANT_ID = 4L;
  private static final Long ANOTHER_IRRELEVANT_ID = 5L;
  private static final TestBatchStatus IRRELEVANT_STATUS = TestBatchStatus.OPEN;
  private static final String IRRELEVANT_BATCH_NUMBER = "1234";
  private static final Date IRRELEVANT_CREATED_DATE = new Date();
  private static final Date IRRELEVANT_LAST_UPDATED_DATE = new Date();
  private static final String IRRELEVANT_NOTES = "some test batch notes";
  private static final CanReleaseResult CANT_RELEASE = new TestBatchConstraintChecker.CanReleaseResult(false);

  @InjectMocks
  private TestBatchFactory testBatchFactory;
  @Mock
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  @Mock
  private TestBatchConstraintChecker testBatchConstraintChecker;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  private LocationRepository locationRepository;

  private DonationBatch createDonationBatch() {
    PackType packType = PackTypeBuilder.aPackType().withTestSampleProduced(true).build();
    Donation donation = aDonation().withPackType(packType).build();
    DonationBatch donationBatch = aDonationBatch().withDonation(donation).build();
    return donationBatch;
  }

  @Test
  public void testCreateTestBatchBasicViewModel_shouldReturnTestBatchViewModelsWithTheCorrectState() {

    DonationBatch donationBatch = createDonationBatch();

    TestBatch testBatch1 =
        aTestBatch().withId(IRRELEVANT_ID).withStatus(IRRELEVANT_STATUS).withBatchNumber(IRRELEVANT_BATCH_NUMBER)
            .withCreatedDate(IRRELEVANT_CREATED_DATE).withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
            .withDonationBatches(new HashSet<>(Arrays.asList(donationBatch))).withNotes(IRRELEVANT_NOTES).build();

    TestBatch testBatch2 =
        aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatches(new HashSet<>(Arrays.asList(donationBatch)))
        .withNotes(IRRELEVANT_NOTES)
        .build();

    DonationBatchFullViewModel donationBatchViewModel = new DonationBatchFullViewModel();

    TestBatchViewModel expectedViewModel = aTestBatchViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .build();
    
    List<TestBatch> testBatches = Arrays.asList(new TestBatch[]{testBatch1, testBatch2});

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch1)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch2)).thenReturn(CANT_RELEASE);
    when(donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch))
        .thenReturn(donationBatchViewModel);

    List<TestBatchViewModel> returnedViewModels = testBatchFactory.createTestBatchBasicViewModels(testBatches);

    assertThat(returnedViewModels.get(0), hasSameStateAsTestBatchViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchFullViewModel_shouldReturnTestBatchFullViewModelWithTheCorrectState() {

    DonationBatch donationBatch = createDonationBatch();
    DonationBatchFullViewModel donationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);

    TestBatch testBatch1 = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatch(donationBatch)
        .withNotes(IRRELEVANT_NOTES)
        .build();
    
    TestBatch testBatch2 = aTestBatch()
        .withId(ANOTHER_IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatch(donationBatch)
        .withNotes(IRRELEVANT_NOTES)
        .build();
    List<TestBatch> testBatches = Arrays.asList(new TestBatch[]{testBatch1, testBatch2});

    TestBatchFullViewModel expectedViewModel1 = aTestBatchFullViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.<DonationBatchViewModel>asList(donationBatchViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .build();
    TestBatchFullViewModel expectedViewModel2 = aTestBatchFullViewModel()
        .withId(ANOTHER_IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.<DonationBatchViewModel>asList(donationBatchViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch1)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch2)).thenReturn(CANT_RELEASE);
    when(donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch))
        .thenReturn(donationBatchViewModel);

    List<TestBatchFullViewModel> returnedViewModels =
        testBatchFactory.createTestBatchFullViewModels(testBatches, false);

    assertThat(returnedViewModels.get(0), hasSameStateAsTestBatchFullViewModel(expectedViewModel1));
    assertThat(returnedViewModels.get(1), hasSameStateAsTestBatchFullViewModel(expectedViewModel2));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanReleaseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    int expectedReadyCount = 2;
    
    DonationBatch donationBatch = createDonationBatch();
    DonationBatchViewModel donationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);


    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatch(donationBatch)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    CanReleaseResult canReleaseResult = new CanReleaseResult(true, expectedReadyCount);

    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .withPermission("canRelease", true)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .withReadyForReleaseCount(expectedReadyCount)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(canReleaseResult);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch)).thenReturn(false);

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanCloseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    DonationBatch donationBatch = createDonationBatch();
    DonationBatchViewModel donationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatch(donationBatch)
        .build();

    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", true)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(true);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch)).thenReturn(false);

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorAndConstraints_shouldReturnTestBatchViewModelWithTheCorrectState() {

    DonationBatch donationBatch = createDonationBatch();
    DonationBatchViewModel donationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);
    
    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatch(donationBatch)
        .build();

    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch)).thenReturn(false);

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanDeleteTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    DonationBatch donationBatch = createDonationBatch();
    DonationBatchViewModel donationBatchViewModel =
        donationBatchViewModelFactory.createDonationBatchFullViewModel(donationBatch);
    
    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatch(donationBatch)
        .build();

    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", true)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(true);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch)).thenReturn(false);

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchOutcomesReport_shouldReturnCorrectPreviousDonationAboRh() {
      
    Donor donor = DonorBuilder.aDonor().build();
    
    DateTime date1 = new DateTime();
    DateTime date2 = date1.plusDays(2);
    DateTime date3 = date1.plusDays(3);
    DateTime date4 = date1.plusDays(4);
    
    Donation donation1 = aDonation().withDonationDate(date1.toDate()).withDonor(donor).withBloodAbo("A1")
        .withBloodRh("B1").withDonationIdentificationNumber("A1").build();
    Donation donation2 = aDonation().withDonationDate(date2.toDate()).withDonor(donor).withBloodAbo("A2")
        .withBloodRh("B2").withDonationIdentificationNumber("A2").build();
    Donation donation3 = aDonation().withDonationDate(date3.toDate()).withDonor(donor).withBloodAbo("A3")
        .withBloodRh("B3").withDonationIdentificationNumber("A3").build();
    Donation donation4 = aDonation().withDonationDate(date4.toDate()).withDonor(donor).withBloodAbo("A4")
        .withBloodRh("B4").withDonationIdentificationNumber("A4").build();
    List<Donation> donations = new ArrayList<>();
    donations.add(donation1);
    donations.add(donation2);
    donations.add(donation3);
    donations.add(donation4);
    
    donor.setDonations(donations);

    DonationBatch donationBatch = aDonationBatch().withDonations(donations).build();
    
    TestBatch testBatch = aTestBatch().withDonationBatch(donationBatch).build();
    
    List<DonationTestOutcomesReportViewModel> report =
        testBatchFactory.createDonationTestOutcomesReportViewModels(testBatch);

    assertThat("Donation 1 has no previous abo/rh", report.get(0).getPreviousDonationAboRhOutcome().isEmpty());
    assertThat("Donation 2 has correct previous abo/rh", report.get(1).getPreviousDonationAboRhOutcome().equals("A1B1"));
    assertThat("Donation 3 has correct previous abo/rh", report.get(2).getPreviousDonationAboRhOutcome().equals("A2B2"));
    assertThat("Donation 4 has correct previous abo/rh", report.get(3).getPreviousDonationAboRhOutcome().equals("A3B3"));
  }
  
  @Test
  public void testCreateTestBatchOutcomesReport_shouldReturnExpectedObject() {
      
    Donor donor = DonorBuilder.aDonor().build();
    
    Donation donation1 = aDonation()
        .withDonationDate(IRRELEVANT_CREATED_DATE)
        .withDonor(donor)
        .withBloodAbo("A1")
        .withBloodRh("B1")
        .withDonationIdentificationNumber("1234567")
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .build();
    List<Donation> donations = new ArrayList<>();
    donations.add(donation1);
    
    donor.setDonations(donations);

    DonationBatch donationBatch = aDonationBatch().withDonations(donations).build();
    
    TestBatch testBatch = aTestBatch().withDonationBatch(donationBatch).build();
    
    DonationTestOutcomesReportViewModel expectedViewModel =
        DonationTestOutcomesReportViewModelBuilder
        .aDonationTestOutcomesReportViewModel()
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withPreviousDonationAboRhOutcome("")
        .withDonationIdentificationNumber("1234567")
        .withBloodTestOutcomes(new HashMap<String, String>())
        .build();
    
    List<DonationTestOutcomesReportViewModel> returnedViewModels =
        testBatchFactory.createDonationTestOutcomesReportViewModels(testBatch);

    assertThat(returnedViewModels.get(0), hasSameStateAsDonationTestOutcomesReportViewModel(expectedViewModel));
  }

  @Test
  public void testCreateEntity_shouldSetCorrectFields() {
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(IRRELEVANT_ID);
    backingForm.setStatus(TestBatchStatus.OPEN.toString());
    backingForm.setCreatedDate(IRRELEVANT_CREATED_DATE);
    backingForm.setDonationBatchIds(Arrays.asList(1L, 2L));
    backingForm.setLocation(aTestingSiteBackingForm().withId(7L).build());
    
    DonationBatch firstDonationBatch = aDonationBatch().withId(1L).build();
    DonationBatch secondDonationBatch = aDonationBatch().withId(2L).build();
    Location location = aTestingSite().withId(7L).build();
    
    TestBatch expectedTestBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(TestBatchStatus.OPEN)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withDonationBatches(new HashSet<>(Arrays.asList(firstDonationBatch, secondDonationBatch)))
        .withLocation(location)
        .build();
    
    when(donationBatchRepository.findDonationBatchById(1L)).thenReturn(firstDonationBatch);
    when(donationBatchRepository.findDonationBatchById(2L)).thenReturn(secondDonationBatch);
    when(locationRepository.getLocation(7L)).thenReturn(location);
    
    TestBatch returnedTestBatch = testBatchFactory.createEntity(backingForm);
    
    assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
  }
  
}
