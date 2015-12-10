package factory;

import model.adverseevent.AdverseEvent;
import model.donation.Donation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import service.DonationConstraintChecker;
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

  @InjectMocks
  private DonationViewModelFactory donationViewModelFactory;
  @Mock
  private DonationConstraintChecker donationConstraintChecker;
  @Mock
  private AdverseEventViewModelFactory adverseEventViewModelFactory;

  @Test
  public void testCreateDonationViewModelWithPermissions_shouldReturnViewModelWithCorrectDonationAndPermissions() {

    boolean irrelevantCanDeletePermission = true;
    boolean irrelevantCanUpdatePermission = true;
    Long irrelevantAdverseEventId = 11L;
    AdverseEvent adverseEvent = anAdverseEvent().withId(irrelevantAdverseEventId).build();
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID)
            .withAdverseEvent(adverseEvent)
            .build();

    AdverseEventViewModel adverseEventViewModel = anAdverseEventViewModel().withId(irrelevantAdverseEventId).build();

    DonationViewModel expectedDonationViewModel = aDonationViewModel()
            .withDonation(donation)
            .withPermission("canDelete", irrelevantCanDeletePermission)
            .withPermission("canUpdateDonationFields", irrelevantCanUpdatePermission)
            .withAdverseEvent(adverseEventViewModel)
            .build();

    when(donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanDeletePermission);
    when(donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID)).thenReturn(irrelevantCanUpdatePermission);
    when(adverseEventViewModelFactory.createAdverseEventViewModel(adverseEvent)).thenReturn(adverseEventViewModel);

    DonationViewModel returnedDonationViewModel = donationViewModelFactory.createDonationViewModelWithPermissions(
            donation);

    assertThat(returnedDonationViewModel, hasSameStateAsDonationViewModel(expectedDonationViewModel));
  }

}
