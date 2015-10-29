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
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import service.TestBatchConstraintChecker;
import suites.UnitTestSuite;
import viewmodel.DonationBatchViewModel;
import viewmodel.TestBatchViewModel;

public class TestBatchViewModelFactoryTests extends UnitTestSuite {

    private static final Long IRRELEVANT_ID = 4L;
    private static final TestBatchStatus IRRELEVANT_STATUS = TestBatchStatus.OPEN;
    private static final String IRRELEVANT_BATCH_NUMBER = "1234";
    private static final Date IRRELEVANT_CREATED_DATE = new Date();
    private static final Date IRRELEVANT_LAST_UPDATED_DATE = new Date();
    private static final String IRRELEVANT_NOTES = "some test batch notes";
    
    @InjectMocks
    private TestBatchViewModelFactory testBatchViewModelFactory;
    @Mock
    private DonationBatchViewModelFactory donationBatchViewModelFactory;
    @Mock
    private TestBatchConstraintChecker testBatchConstraintChecker;
    
    @Test
    public void testCreateTestBatchViewModelWithDonationBatches_shouldReturnTestBatchViewModelWithTheCorrectState() {
        
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
        
        DonationBatchViewModel donationBatchViewModel = new DonationBatchViewModel();
        
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
                .build();
        
        when(donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch, true))
                .thenReturn(donationBatchViewModel);
        
        TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, false);
        
        assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
    }
    
    @Test
    public void testCreateTestBatchViewModelWithTestingSupervisorThatCanReleaseTestBatch_shouldReturnTestBatchViewModelWithTheCorrectState() {
        
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
                .withDonationBatches(Collections.<DonationBatchViewModel>emptyList())
                .withPermission("canRelease", true)
                .withPermission("canClose", false)
                .withPermission("canDelete", false)
                .withPermission("canEdit", false)
                .withPermission("canReopen", false)
                .build();

        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(true);
        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
        
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
                .withDonationBatches(Collections.<DonationBatchViewModel>emptyList())
                .withPermission("canRelease", false)
                .withPermission("canClose", true)
                .withPermission("canDelete", false)
                .withPermission("canEdit", false)
                .withPermission("canReopen", false)
                .build();

        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(true);
        when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
        
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
                .withDonationBatches(Collections.<DonationBatchViewModel>emptyList())
                .withPermission("canRelease", false)
                .withPermission("canClose", false)
                .withPermission("canDelete", false)
                .withPermission("canEdit", false)
                .withPermission("canReopen", false)
                .build();

        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
        
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
                .withDonationBatches(Collections.<DonationBatchViewModel>emptyList())
                .withPermission("canRelease", false)
                .withPermission("canClose", false)
                .withPermission("canDelete", true)
                .withPermission("canEdit", false)
                .withPermission("canReopen", false)
                .build();

        when(testBatchConstraintChecker.canReleaseTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canCloseTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canDeleteTestBatch(testBatch)).thenReturn(true);
        when(testBatchConstraintChecker.canEditTestBatch(testBatch)).thenReturn(false);
        when(testBatchConstraintChecker.canReopenTestBatch(testBatch)).thenReturn(false);
        
        TestBatchViewModel returnedViewModel = testBatchViewModelFactory.createTestBatchViewModel(testBatch, true);
        
        assertThat(returnedViewModel, hasSameStateAsTestBatchViewModel(expectedViewModel));
    }
}
