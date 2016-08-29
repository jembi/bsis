package org.jembi.bsis.factory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.DonationViewModelBuilder.aDonationViewModel;
import static org.jembi.bsis.helpers.builders.DonorBuilder.aDonor;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingViewModelBuilder.aPostDonationCounsellingViewModel;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingViewModelMatcher.hasSameStateAsPostDonationCounsellingViewModel;
import static org.mockito.Mockito.when;

import org.jembi.bsis.factory.DonationFactory;
import org.jembi.bsis.factory.PostDonationCounsellingViewModelFactory;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.junit.Test;

import org.mockito.InjectMocks;

import org.mockito.Mock;


public class PostDonationCounsellingViewModelFactoryTests extends UnitTestSuite {

  @InjectMocks
  private PostDonationCounsellingViewModelFactory postDonationCounsellingViewModelFactory;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private DonationFactory donationFactory;

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
    when(donationFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);

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
    when(donationFactory.createDonationViewModelWithoutPermissions(donation)).thenReturn(expectedDonationViewModel);

    PostDonationCounsellingViewModel returnedPostDonationCounsellingViewModel = postDonationCounsellingViewModelFactory
        .createPostDonationCounsellingViewModel(postDonationCounselling);

    assertThat(returnedPostDonationCounsellingViewModel, hasSameStateAsPostDonationCounsellingViewModel(expectedPostDonationCounsellingViewModel));

  }

}
