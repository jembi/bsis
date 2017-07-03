package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationFullViewModelBuilder.aDonationFullViewModel;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.LocationBackingFormBuilder.aTestingSiteBackingForm;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.jembi.bsis.helpers.builders.TestBatchFullViewModelBuilder.aTestBatchFullViewModel;
import static org.jembi.bsis.helpers.builders.TestBatchViewModelBuilder.aTestBatchViewModel;
import static org.jembi.bsis.helpers.matchers.DonationTestOutcomesReportViewModelMatcher.hasSameStateAsDonationTestOutcomesReportViewModel;
import static org.jembi.bsis.helpers.matchers.TestBatchFullViewModelMatcher.hasSameStateAsTestBatchFullViewModel;
import static org.jembi.bsis.helpers.matchers.TestBatchMatcher.hasSameStateAsTestBatch;
import static org.jembi.bsis.helpers.matchers.TestBatchViewModelMatcher.hasSameStateAsTestBatchViewModel;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.DonationTestOutcomesReportViewModelBuilder;
import org.jembi.bsis.helpers.builders.DonorBuilder;
import org.jembi.bsis.helpers.builders.PackTypeBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.TestBatchConstraintChecker;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchViewModel;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("unchecked")
public class TestBatchFactoryTests extends UnitTestSuite {

  private static final UUID IRRELEVANT_TEST_BATCH_ID = UUID.randomUUID();
  private static final UUID ANOTHER_IRRELEVANT_TEST_BATCH_ID = UUID.randomUUID();
  private static final TestBatchStatus IRRELEVANT_STATUS = TestBatchStatus.OPEN;
  private static final String IRRELEVANT_BATCH_NUMBER = "1234";
  private static final Date IRRELEVANT_TEST_BATCH_DATE = new Date();
  private static final Date IRRELEVANT_LAST_UPDATED_DATE = new Date();
  private static final String IRRELEVANT_NOTES = "some test batch notes";
  private static final CanReleaseResult CANT_RELEASE = new TestBatchConstraintChecker.CanReleaseResult(false);

  @InjectMocks
  private TestBatchFactory testBatchFactory;
  @Mock
  private TestBatchConstraintChecker testBatchConstraintChecker;
  @Mock
  private LocationFactory locationFactory;
  @Mock
  private DonationBatchRepository donationBatchRepository;
  @Mock
  private LocationRepository locationRepository;
  @Mock
  private DonationFactory donationFactory;
  
  private Set<Donation> createDonations() {
    PackType packType = PackTypeBuilder.aPackType().withTestSampleProduced(true).build();
    Set<Donation> donations = new HashSet<>(Arrays.asList(
        aDonation().withPackType(packType).build()
    ));
    
    return donations;
  }

  @Test
  public void testCreateTestBatchBasicViewModel_shouldReturnTestBatchViewModelsWithTheCorrectState() {

    Set<Donation> donations = createDonations();

    TestBatch testBatch1 =
      aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonations(donations)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    TestBatch testBatch2 =
      aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonations(donations)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    TestBatchViewModel expectedViewModel = aTestBatchViewModel()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .build();
    
    List<TestBatch> testBatches = Arrays.asList(new TestBatch[]{testBatch1, testBatch2});

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch1)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch2)).thenReturn(CANT_RELEASE);
    when(donationFactory.createDonationViewModel(any(Donation.class))).thenReturn(aDonationViewModel().build());

    List<TestBatchViewModel> returnedViewModels = testBatchFactory.createTestBatchBasicViewModels(testBatches);

    assertThat(returnedViewModels.get(0), hasSameStateAsTestBatchViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchFullViewModel_shouldReturnTestBatchFullViewModelWithTheCorrectState() {

    DonationViewModel donationViewModel = aDonationViewModel().build();
    Set<Donation> donations = createDonations();

    TestBatch testBatch1 = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonations(donations)
        .withNotes(IRRELEVANT_NOTES)
        .build();
    
    TestBatch testBatch2 = aTestBatch()
        .withId(ANOTHER_IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonations(donations)
        .withNotes(IRRELEVANT_NOTES)
        .build();
    List<TestBatch> testBatches = Arrays.asList(new TestBatch[]{testBatch1, testBatch2});

    TestBatchFullViewModel expectedViewModel1 = aTestBatchFullViewModel()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(Arrays.<DonationViewModel>asList(donationViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonations", false)
        .build();
    TestBatchFullViewModel expectedViewModel2 = aTestBatchFullViewModel()
        .withId(ANOTHER_IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(Arrays.<DonationViewModel>asList(donationViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonations", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch1)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch2)).thenReturn(CANT_RELEASE);
    when(donationFactory.createDonationViewModels(any(Collection.class))).thenReturn(Arrays.asList(donationViewModel));

    List<TestBatchFullViewModel> returnedViewModels =
        testBatchFactory.createTestBatchFullViewModels(testBatches, false);

    assertThat(returnedViewModels.get(0), hasSameStateAsTestBatchFullViewModel(expectedViewModel1));
    assertThat(returnedViewModels.get(1), hasSameStateAsTestBatchFullViewModel(expectedViewModel2));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanReleaseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    int expectedReadyCount = 2;
    Set<Donation> donations = createDonations();
 
    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonations(donations)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    CanReleaseResult canReleaseResult = new CanReleaseResult(true, expectedReadyCount);

    DonationViewModel donationViewModel = aDonationViewModel().build();
    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(Arrays.asList(donationViewModel))
        .withPermission("canRelease", true)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonations", false)
        .withReadyForReleaseCount(expectedReadyCount)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(canReleaseResult);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(false);
    when(donationFactory.createDonationViewModels(any(Collection.class))).thenReturn(Arrays.asList(donationViewModel));

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanCloseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {
    
    Set<Donation> donations = createDonations();

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(donations)
        .build();

    DonationViewModel donationViewModel = aDonationViewModel().build();
    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(Arrays.asList(donationViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", true)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonations", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(true);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(false);
    when(donationFactory.createDonationViewModels(any(Collection.class))).thenReturn(Arrays.asList(donationViewModel));

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorAndConstraints_shouldReturnTestBatchViewModelWithTheCorrectState() {

    Set<Donation> donations = createDonations();
    
    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(donations)
        .build();

    DonationViewModel donationViewModel = aDonationViewModel().build();
    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(Arrays.asList(donationViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonations", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(false);
    when(donationFactory.createDonationViewModels(any(Collection.class))).thenReturn(Arrays.asList(donationViewModel));

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanDeleteTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    Set<Donation> donations = createDonations();
    
    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(donations)
        .build();

    DonationViewModel donationViewModel = aDonationViewModel().build();
    TestBatchFullViewModel expectedViewModel = aTestBatchFullViewModel()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonations(Arrays.asList(donationViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", true)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonations", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(true);
    when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
    when(testBatchConstraintChecker.canAddOrRemoveDonation(testBatch)).thenReturn(false);
    when(donationFactory.createDonationViewModels(any(Collection.class))).thenReturn(Arrays.asList(donationViewModel));

    TestBatchFullViewModel returnedViewModel = testBatchFactory.createTestBatchFullViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchFullViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchOutcomesReport_shouldReturnCorrectPreviousDonationAboRh() throws Exception {
      
    Donor donor = DonorBuilder.aDonor().build();
    
    Donation donation1 = aDonation()
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01"))
        .withDonor(donor)
        .withBloodAbo("A1")
        .withBloodRh("B1")
        .withDonationIdentificationNumber("A1")
        .build();
    Donation donation2 = aDonation()
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-03"))
        .withDonor(donor)
        .withBloodAbo("A2")
        .withBloodRh("B2")
        .withDonationIdentificationNumber("A2")
        .build();
    Donation donation3 = aDonation()
        .withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-04"))
        .withDonor(donor)
        .withBloodAbo("A3")
        .withBloodRh("B3")
        .withDonationIdentificationNumber("A3")
        .build();
    Donation donation4 = aDonation().
        withDonationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-05"))
        .withDonor(donor)
        .withBloodAbo("A4")
        .withBloodRh("B4")
        .withDonationIdentificationNumber("A4")
        .build();
    
    donor.setDonations(Arrays.asList(donation1, donation2, donation3, donation4));
    
    TestBatch testBatch = aTestBatch()
        .withDonation(donation1)
        .withDonation(donation2)
        .withDonation(donation3)
        .withDonation(donation4)
        .build();

    DonationTestOutcomesReportViewModel expectedTestOutcomeViewModel1 = DonationTestOutcomesReportViewModelBuilder.aDonationTestOutcomesReportViewModel()
        .withDonationIdentificationNumber("A1")
        .withPreviousDonationAboRhOutcome("")
        .withBloodTestOutcomes(new HashMap<String, String>())
        .build();

    DonationTestOutcomesReportViewModel expectedTestOutcomeViewModel2 = DonationTestOutcomesReportViewModelBuilder.aDonationTestOutcomesReportViewModel()
        .withDonationIdentificationNumber("A2")
        .withPreviousDonationAboRhOutcome("A1B1")
        .withBloodTestOutcomes(new HashMap<String, String>())
        .build();

    DonationTestOutcomesReportViewModel expectedTestOutcomeViewModel3 = DonationTestOutcomesReportViewModelBuilder.aDonationTestOutcomesReportViewModel()
        .withDonationIdentificationNumber("A3")
        .withPreviousDonationAboRhOutcome("A2B2")
        .withBloodTestOutcomes(new HashMap<String, String>())
        .build();

    DonationTestOutcomesReportViewModel expectedTestOutcomeViewModel4 = DonationTestOutcomesReportViewModelBuilder.aDonationTestOutcomesReportViewModel()
        .withDonationIdentificationNumber("A4")
        .withPreviousDonationAboRhOutcome("A3B3")
        .withBloodTestOutcomes(new HashMap<String, String>())
        .build();
    
    List<DonationTestOutcomesReportViewModel> report = testBatchFactory.createDonationTestOutcomesReportViewModels(testBatch);

    assertThat(report, hasItem(hasSameStateAsDonationTestOutcomesReportViewModel(expectedTestOutcomeViewModel1)));
    assertThat(report, hasItem(hasSameStateAsDonationTestOutcomesReportViewModel(expectedTestOutcomeViewModel2)));
    assertThat(report, hasItem(hasSameStateAsDonationTestOutcomesReportViewModel(expectedTestOutcomeViewModel3)));
    assertThat(report, hasItem(hasSameStateAsDonationTestOutcomesReportViewModel(expectedTestOutcomeViewModel4)));
  }
  
  @Test
  public void testCreateTestBatchOutcomesReport_shouldReturnExpectedObject() {
      
    Donor donor = DonorBuilder.aDonor().build();
    
    Donation donation1 = aDonation()
        .withDonationDate(IRRELEVANT_TEST_BATCH_DATE)
        .withDonor(donor)
        .withBloodAbo("A1")
        .withBloodRh("B1")
        .withDonationIdentificationNumber("1234567")
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .build();
    
    donor.setDonations(Arrays.asList(donation1));
    
    TestBatch testBatch = aTestBatch()
        .withDonation(donation1)
        .build();
    
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
    UUID locationId = UUID.randomUUID();
    TestBatchBackingForm backingForm = new TestBatchBackingForm();
    backingForm.setId(IRRELEVANT_TEST_BATCH_ID);
    backingForm.setStatus(TestBatchStatus.OPEN);
    backingForm.setTestBatchDate(IRRELEVANT_TEST_BATCH_DATE);
    backingForm.setLocation(aTestingSiteBackingForm().withId(locationId).build());

    Location location = aTestingSite().withId(locationId).build();
    
    TestBatch expectedTestBatch = aTestBatch()
        .withId(IRRELEVANT_TEST_BATCH_ID)
        .withStatus(TestBatchStatus.OPEN)
        .withTestBatchDate(IRRELEVANT_TEST_BATCH_DATE)
        .withLocation(location)
        .withDonations(new HashSet<Donation>())
        .build();
    
    when(locationRepository.getLocation(locationId)).thenReturn(location);
    
    TestBatch returnedTestBatch = testBatchFactory.createEntity(backingForm);
    
    assertThat(returnedTestBatch, hasSameStateAsTestBatch(expectedTestBatch));
  }
  
  @Test
  public void testCreateDonationFullViewModels_shouldReturnOnlyAmbiguous() {

    Donation d1 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    Donation d2 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED).build();
    Donation d3 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    Donation d4 = DonationBuilder.aDonation().withBloodTypingMatchStatus(BloodTypingMatchStatus.RESOLVED).build();

    DonationFullViewModel d1FullViewModel =
        aDonationFullViewModel().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();
    DonationFullViewModel d3FullViewModel =
        aDonationFullViewModel().withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS).build();

    TestBatch testBatch = TestBatchBuilder.aTestBatch()
        .withDonation(d1)
        .withDonation(d2)
        .withDonation(d3)
        .withDonation(d4)
        .build();
    
    when(donationFactory.createDonationFullViewModelWithoutPermissions(d1)).thenReturn(d1FullViewModel);
    when(donationFactory.createDonationFullViewModelWithoutPermissions(d3)).thenReturn(d3FullViewModel);
    
    List<DonationFullViewModel> donationFullViewModels =
        testBatchFactory.createDonationFullViewModels(testBatch, BloodTypingMatchStatus.AMBIGUOUS);

    Assert.assertTrue("2 ambiguous donations on the test batch", donationFullViewModels.size() == 2);

  }

}
