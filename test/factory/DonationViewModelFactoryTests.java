package factory;

import static helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import model.adverseevent.AdverseEvent;
import model.donation.Donation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.DonationConstraintChecker;
import service.DonorConstraintChecker;
import viewmodel.AdverseEventViewModel;
import viewmodel.DonationViewModel;

import static helpers.builders.AdverseEventBuilder.anAdverseEvent;
import static helpers.builders.AdverseEventViewModelBuilder.anAdverseEventViewModel;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationViewModelFactoryTests {

    private static final long IRRELEVANT_DONATION_ID = 89;
    private static final long ANOTHER_IRRELEVANT_DONATION_ID = 90;
    private static final long IRRELEVANT_DONOR_ID = 89;
    private static final long ANOTHER_IRRELEVANT_DONOR_ID = 90;

    @InjectMocks
    private DonationViewModelFactory donationViewModelFactory;
    @Mock
    private DonationConstraintChecker donationConstraintChecker;
    @Mock
    private AdverseEventViewModelFactory adverseEventViewModelFactory;
    @Mock
    private DonorConstraintChecker donorConstraintChecker;

    @Test
    public void testCreateDonationViewModelWithPermissions_shouldReturnViewModelWithCorrectDonationAndPermissions() {

        boolean irrelevantCanDeletePermission = true;
        boolean irrelevantCanUpdatePermission = true;
        boolean irrelevantCanDonatePermission = true;
        boolean irrelevantIsBackEntryPermission = true;

        Long irrelevantAdverseEventId = 11L;
        AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();
        Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID)
                .withDonor(aDonor().withId(IRRELEVANT_DONOR_ID).build())
                .withDonationBatch(aDonationBatch().thatIsBackEntry().build())
                .withAdverseEvent(adverseEvent)
                .build();

        AdverseEventViewModel adverseEventViewModel = anAdverseEventViewModel().withId(irrelevantAdverseEventId).build();

        DonationViewModel expectedDonationViewModel = aDonationViewModel()
                .withDonation(donation)
                .withPermission("canDelete", irrelevantCanDeletePermission)
                .withPermission("canUpdateDonationFields", irrelevantCanUpdatePermission)
                .withPermission("canDonate", irrelevantCanDonatePermission)
                .withPermission("isBackEntry", irrelevantIsBackEntryPermission)
                .withAdverseEvent(adverseEventViewModel)
                .build();

        when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanDeletePermission);
        when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanUpdatePermission);
        when(donorConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID)).thenReturn(irrelevantCanDonatePermission);
        when(adverseEventViewModelFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);


        DonationViewModel returnedDonationViewModel = donationViewModelFactory.createDonationViewModelWithPermissions(
                donation);

        assertThat(returnedDonationViewModel, hasSameStateAsDonationViewModel(expectedDonationViewModel));
    }

    @Test
    public void testCreateDonationViewModelsWithPermissions_shouldReturnViewModelsWithCorrectDonationAndPermissions() {

        Long irrelevantAdverseEventId = 11L;
        AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();
        Donation donation1 = aDonation().withId(IRRELEVANT_DONATION_ID)
                .withDonor(aDonor().withId(IRRELEVANT_DONOR_ID).build())
                .withDonationBatch(aDonationBatch().thatIsBackEntry().build())
                .withAdverseEvent(adverseEvent)
                .build();
        Donation donation2 = aDonation().withId(ANOTHER_IRRELEVANT_DONATION_ID)
            .withDonor(aDonor().withId(ANOTHER_IRRELEVANT_DONOR_ID).build())
            .withDonationBatch(aDonationBatch().build())
            .build();
        List<Donation> donations = Arrays.asList(new Donation[] { donation1, donation2 } );

        AdverseEventViewModel adverseEventViewModel = anAdverseEventViewModel().withId(irrelevantAdverseEventId).build();

        DonationViewModel expectedDonation1ViewModel = aDonationViewModel()
                .withDonation(donation1)
                .withPermission("canDelete", true)
                .withPermission("canUpdateDonationFields", true)
                .withPermission("canDonate", true)
                .withPermission("isBackEntry", true)
                .withAdverseEvent(adverseEventViewModel)
                .build();
        DonationViewModel expectedDonation2ViewModel = aDonationViewModel()
            .withDonation(donation2)
            .withPermission("canDelete", true)
            .withPermission("canUpdateDonationFields", true)
            .withPermission("canDonate", false)
            .withPermission("isBackEntry", false)
            .build();

        when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donorConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID)).thenReturn(true);
        when(donorConstraintChecker.isDonorDeferred(IRRELEVANT_DONATION_ID)).thenReturn(false);
        when(adverseEventViewModelFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);
        when(donationConstraintChecker.canDeleteDonation(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donationConstraintChecker.canUpdateDonationFields(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donorConstraintChecker.isDonorEligibleToDonate(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donorConstraintChecker.isDonorDeferred(ANOTHER_IRRELEVANT_DONATION_ID)).thenReturn(true);

        List<DonationViewModel> returnedDonationViewModels = donationViewModelFactory.createDonationViewModelsWithPermissions(donations);

        assertThat(returnedDonationViewModels.get(0), hasSameStateAsDonationViewModel(expectedDonation1ViewModel));
        assertThat(returnedDonationViewModels.get(1), hasSameStateAsDonationViewModel(expectedDonation2ViewModel));
    }
}
