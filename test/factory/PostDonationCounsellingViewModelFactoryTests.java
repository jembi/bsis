package factory;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static helpers.builders.PostDonationCounsellingViewModelBuilder.aPostDonationCounsellingViewModel;
import static helpers.matchers.PostDonationCounsellingViewModelMatcher.hasSameStateAsPostDonationCounsellingViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import model.counselling.PostDonationCounselling;
import model.donation.Donation;

import org.junit.Test;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import repository.PostDonationCounsellingRepository;
import suites.UnitTestSuite;
import viewmodel.DonationViewModel;
import viewmodel.PostDonationCounsellingViewModel;


public class PostDonationCounsellingViewModelFactoryTests extends UnitTestSuite {

  @InjectMocks
  private PostDonationCounsellingViewModelFactory postDonationCounsellingViewModelFactory;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private DonationViewModelFactory donationViewModelFactory;

  @Test
  public void testCreatePostDonationCounsellingViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsTrue() {

    boolean canRemoveStatus = true;
    long donorId = 1L;
    long donationId = 87L;
    
    Donation donation = aDonation().withId(donationId).withDonor(aDonor().withId(donorId).build()).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(donorId)
        .withDonation(donation)
        .thatIsFlaggedForCounselling()
        .build();
    
    DonationViewModel expectedDonationViewModel = aDonationViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withDonation(expectedDonationViewModel)
        .withPermission("canRemoveStatus", canRemoveStatus)
        .withPostDonationCounselling(postDonationCounselling)
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(1);
    when(donationViewModelFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingViewModelFactory
        .createPostDonationCounsellingViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

  @Test
  public void testCreatePostDonationCounsellingViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsFalse() {

    boolean canRemoveStatus = false;
    long donorId = 1L;
    long donationId = 87L;
    
    Donation donation = aDonation().withId(donationId).withDonor(aDonor().withId(donorId).build()).build();

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(donorId)
        .withDonation(donation)
        .thatIsFlaggedForCounselling()
        .build();
    
    DonationViewModel expectedDonationViewModel = aDonationViewModel().withId(donationId).build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
        .withDonation(expectedDonationViewModel)
        .withPermission("canRemoveStatus", canRemoveStatus)
        .withPostDonationCounselling(postDonationCounselling)
        .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(0);
    when(donationViewModelFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingViewModelFactory
        .createPostDonationCounsellingViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

}
