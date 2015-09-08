package factory;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.matchers.DonationViewModelMatcher.hasSameStateAsDonationViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import model.donation.Donation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import service.DonationConstraintChecker;
import viewmodel.DonationViewModel;

@RunWith(MockitoJUnitRunner.class)
public class DonationViewModelFactoryTests {
    
    private static final long IRRELEVANT_DONATION_ID = 89;

    @InjectMocks
    private DonationViewModelFactory donationViewModelFactory;
    @Mock
    private DonationConstraintChecker donationConstraintChecker;
    
    @Test
    public void testCreateDonationViewModelWithPermissions_shouldReturnViewModelWithCorrectDonationAndPermissions() {
        
        boolean irrelevantCanDeletePermission = true;
        boolean irrelevantCanUpdatePermission = true;
        Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).build();
        
        DonationViewModel expectedDonationViewModel = aDonationViewModel()
                .withDonation(donation)
                .withPermission("canDelete", irrelevantCanDeletePermission)
                .withPermission("canUpdateDonationFields", irrelevantCanUpdatePermission)
                .build();
        
        when(donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanDeletePermission);
        when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanUpdatePermission);
        
        DonationViewModel returnedDonationViewModel = donationViewModelFactory.createDonationViewModelWithPermissions(
                donation);
        
        assertThat(returnedDonationViewModel, hasSameStateAsDonationViewModel(expectedDonationViewModel));
    }

}
