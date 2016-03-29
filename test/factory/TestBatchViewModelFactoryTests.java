package factory;

import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static helpers.builders.TestBatchViewModelBuilder.aTestBatchViewModel;
import static helpers.matchers.TestBatchViewModelMatcher.hasSameStateAsTestBatchViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import service.TestBatchConstraintChecker;
import service.TestBatchConstraintChecker.CanReleaseResult;
import suites.UnitTestSuite;
import viewmodel.DonationBatchFullViewModel;
import viewmodel.TestBatchViewModel;

public class TestBatchViewModelFactoryTests extends UnitTestSuite {

  private static final Long IRRELEVANT_ID = 4L;
  private static final Long ANOTHER_IRRELEVANT_ID = 5L;
  private static final TestBatchStatus IRRELEVANT_STATUS = TestBatchStatus.OPEN;
  private static final String IRRELEVANT_BATCH_NUMBER = "1234";
  private static final Date IRRELEVANT_CREATED_DATE = new Date();
  private static final Date IRRELEVANT_LAST_UPDATED_DATE = new Date();
  private static final String IRRELEVANT_NOTES = "some test batch notes";
  private static final CanReleaseResult CANT_RELEASE = new TestBatchConstraintChecker.CanReleaseResult(false);

  @InjectMocks
  private TestBatchViewModelFactory testBatchViewModelFactory;
  @Mock
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  @Mock
  private TestBatchConstraintChecker testBatchConstraintChecker;

  @Test
  public void testCreateTestBatchViewModelsWithDonationBatches_shouldReturnTestBatchViewModelsWithTheCorrectState() {

    DonationBatch donationBatch = aDonationBatch().build();

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatches(Arrays.asList(donationBatch))
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
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .withPermission("canRelease", false)
        .withPermission("canClose", false)
        .withPermission("canDelete", false)
        .withPermission("canEdit", false)
        .withPermission("canReopen", false)
        .withPermission("canEditDonationBatches", false)
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(CANT_RELEASE);
    when(donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch))
        .thenReturn(donationBatchViewModel);

    TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, false);

    assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithDonationBatches_shouldReturnTestBatchViewModelWithTheCorrectState() {

    DonationBatch donationBatch = aDonationBatch().build();

    TestBatch testBatch1 = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatches(Arrays.asList(donationBatch))
        .withNotes(IRRELEVANT_NOTES)
        .build();
    TestBatch testBatch2 = aTestBatch()
        .withId(ANOTHER_IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withDonationBatches(Arrays.asList(donationBatch))
        .withNotes(IRRELEVANT_NOTES)
        .build();
    List<TestBatch> testBatches = Arrays.asList(new TestBatch[]{testBatch1, testBatch2});

    DonationBatchFullViewModel donationBatchViewModel = new DonationBatchFullViewModel();

    TestBatchViewModel expectedViewModel1 = aTestBatchViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .build();
    TestBatchViewModel expectedViewModel2 = aTestBatchViewModel()
        .withId(ANOTHER_IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Arrays.asList(donationBatchViewModel))
        .build();

    when(testBatchConstraintChecker.canReleaseTestBatch(testBatch1)).thenReturn(CANT_RELEASE);
    when(donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch))
        .thenReturn(donationBatchViewModel);

    List<TestBatchViewModel> returnedViewModels = testBatchViewModelFactory.createTestBatchViewModels(testBatches, false);

    assertThat(returnedViewModels.get(0), hasSameStateAsTestBatchViewModel(expectedViewModel1));
    assertThat(returnedViewModels.get(1), hasSameStateAsTestBatchViewModel(expectedViewModel2));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanReleaseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    int expectedReadyCount = 2;

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    CanReleaseResult canReleaseResult = new CanReleaseResult(true, expectedReadyCount);

    TestBatchViewModel expectedViewModel = aTestBatchViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Collections.<DonationBatchFullViewModel>emptyList())
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

    TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanCloseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    TestBatchViewModel expectedViewModel = aTestBatchViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Collections.<DonationBatchFullViewModel>emptyList())
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

    TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorAndConstraints_shouldReturnTestBatchViewModelWithTheCorrectState() {

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    TestBatchViewModel expectedViewModel = aTestBatchViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Collections.<DonationBatchFullViewModel>emptyList())
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

    TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
  }

  @Test
  public void testCreateTestBatchViewModelWithTestingSupervisorThatCanDeleteTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {

    TestBatch testBatch = aTestBatch()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .build();

    TestBatchViewModel expectedViewModel = aTestBatchViewModel()
        .withId(IRRELEVANT_ID)
        .withStatus(IRRELEVANT_STATUS)
        .withBatchNumber(IRRELEVANT_BATCH_NUMBER)
        .withCreatedDate(IRRELEVANT_CREATED_DATE)
        .withLastUpdatedDate(IRRELEVANT_LAST_UPDATED_DATE)
        .withNotes(IRRELEVANT_NOTES)
        .withDonationBatches(Collections.<DonationBatchFullViewModel>emptyList())
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

    TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, true);

    assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
  }
}
