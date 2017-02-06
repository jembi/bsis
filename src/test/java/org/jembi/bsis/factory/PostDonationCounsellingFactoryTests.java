package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.DonorViewModelBuilder.aDonorViewModel;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBackingFormBuilder.aPostDonationCounsellingBackingForm;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingViewModelBuilder.aPostDonationCounsellingViewModel;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingMatcher.hasSameStateAsPostDonationCounselling;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingViewModelMatcher.hasSameStateAsPostDonationCounsellingViewModel;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PostDonationCounsellingFactoryTests extends UnitTestSuite {

  @InjectMocks
  private PostDonationCounsellingFactory postDonationCounsellingFactory;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private DonationFactory donationFactory;
  @Mock
  private DonorViewModelFactory donorFactory;

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsTrue() {

    boolean canRemoveStatus = true;
    long donorId = 21L;
    long donationId = 87L;
    long postDonationCounsellingId = 32L;
    
    Donor donor = aDonor().withId(donorId).build();
    Donation donation = aDonation().withId(donationId).withDonor(donor).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsFlaggedForCounselling()
        .withNotes("notes")
        .build();
    
    DonorViewModel expectedDonorViewModel = aDonorViewModel().withDonor(donor).build();
    DonationViewModel expectedDonationViewModel = aDonationViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withId(postDonationCounsellingId)
        .withDonation(expectedDonationViewModel)
        .withDonor(expectedDonorViewModel)
        .withPermission("canRemoveStatus", canRemoveStatus)
        .thatIsFlaggedForCounselling()
        .withNotes("notes")
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(1);
    when(donationFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);
    when(donorFactory.createDonorViewModel(donor)).thenReturn(expectedDonorViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

  @Test
  public void testCreateViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsFalse() {

    boolean canRemoveStatus = false;
    long donorId = 21L;
    long donationId = 87L;
    long postDonationCounsellingId = 32L;
    
    Donor donor = aDonor().withId(donorId).build();
    Donation donation = aDonation().withId(donationId).withDonor(donor).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withDonation(donation)
        .thatIsFlaggedForCounselling()
        .build();
    
    DonorViewModel expectedDonorViewModel = aDonorViewModel().withDonor(donor).build();
    DonationViewModel expectedDonationViewModel = aDonationViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withId(postDonationCounsellingId)
        .withDonation(expectedDonationViewModel)
        .withDonor(expectedDonorViewModel)
        .withPermission("canRemoveStatus", canRemoveStatus)
        .thatIsFlaggedForCounselling()
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(0);
    when(donationFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);
    when(donorFactory.createDonorViewModel(donor)).thenReturn(expectedDonorViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingFactory
        .createViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

  @Test
  public void testCreateEntity_shouldReturnEntityInCorrectState() {
    Date counsellingDate = new Date();

    PostDonationCounsellingBackingForm form = aPostDonationCounsellingBackingForm()
        .withId(1L)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .withNotes("notes")
        .build();

    PostDonationCounselling expectedEntity = aPostDonationCounselling()
        .withId(1L)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(CounsellingStatus.RECEIVED_COUNSELLING)
        .thatIsNotFlaggedForCounselling()
        .withDonation(null) // donation is not mapped in the form, so must be null
        .withNotes("notes")
        .build();

    PostDonationCounselling returnedEntity = postDonationCounsellingFactory.createEntity(form);

    assertThat(returnedEntity, hasSameStateAsPostDonationCounselling(expectedEntity));
  }
}
