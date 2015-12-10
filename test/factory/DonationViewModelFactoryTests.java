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
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DonationViewModelFactoryTests {

    private static final long IRRELEVANT_DONATION_ID = 89;
    private static final long IRRELEVANT_DONOR_ID = 89;

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

}
