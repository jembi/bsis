package factory;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static helpers.builders.PostDonationCounsellingViewModelBuilder.aPostDonationCounsellingViewModel;
import static helpers.matchers.PostDonationCounsellingViewModelMatcher.hasSameStateAsPostDonationCounsellingViewModel;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import model.counselling.PostDonationCounselling;
import org.junit.Test;

import org.mockito.InjectMocks;

import org.mockito.Mock;
import repository.PostDonationCounsellingRepository;
import suites.UnitTestSuite;
import viewmodel.PostDonationCounsellingViewModel;


public class PostDonationCounsellingViewModelFactoryTests extends UnitTestSuite {

  @InjectMocks
  private PostDonationCounsellingViewModelFactory postDonationCounsellingViewModelFactory;

  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  @Test
  public void testCreatePostDonationCounsellingViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsTrue() {

    boolean canResetStatus = true;
    long donorId = 1L;

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
            .withId(donorId)
            .withDonation(aDonation().withDonor(aDonor()
                    .withId(donorId).build())
                    .build()
            )
            .thatIsFlaggedForCounselling()
            .build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
            .withPermission("canResetStatus", canResetStatus)
            .withPostDonationCounselling(postDonationCounselling)
            .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(1);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingViewModelFactory
            .createPostDonationCounsellingViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

  @Test
  public void testCreatePostDonationCounsellingViewModel_shouldReturnViewModelWithCorrectDonorAndPermissionsFalse() {

    boolean canResetStatus = false;
    long donorId = 1L;

    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
            .withId(donorId)
            .withDonation(aDonation().withDonor(aDonor()
                    .withId(donorId).build())
                    .build()
            )
            .thatIsFlaggedForCounselling()
            .build();

    PostDonationCounsellingViewModel expectedPostDonationCounsellingViewModel = aPostDonationCounsellingViewModel()
            .withPermission("canResetStatus", canResetStatus)
            .withPostDonationCounselling(postDonationCounselling)
            .build();

    when(postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(donorId)).thenReturn(0);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingViewModelFactory
            .createPostDonationCounsellingViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

}
