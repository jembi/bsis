package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aReferralSite;
import static org.jembi.bsis.helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static org.jembi.bsis.helpers.matchers.PostDonationCounsellingMatcher.hasSameStateAsPostDonationCounselling;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class PostDonationCounsellingCRUDServiceTests extends UnitTestSuite {
  
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");

  @InjectMocks
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private DateGeneratorService dateGeneratorService;

  @Test
  public void testCreatePostDonationCounselling_shouldPersistAndReturnAFlaggedPostDonationCounsellingForDonation() {
    Donation donation = aDonation().withId(DONATION_ID).build();

    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
        .withId(null)
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(donation)
        .withCounsellingStatus(null)
        .withCounsellingDate(null)
        .build();

    when(postDonationCounsellingRepository.findPostDonationCounsellingForDonation(donation)).thenReturn(null);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
        .createPostDonationCounsellingForDonation(donation);

    verify(postDonationCounsellingRepository).save(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));
    assertThat(returnedPostDonationCounselling, hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling));
  }

  @Test
  public void testCreatePostDonationCounsellingWithExistingCounselling_shouldReturnExistingCounselling() {
    Donation donation = aDonation().withId(DONATION_ID).build();

    PostDonationCounselling existingPostDonationCounselling = aPostDonationCounselling()
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withDonation(donation)
        .build();

    when(postDonationCounsellingRepository.findPostDonationCounsellingForDonation(donation))
        .thenReturn(existingPostDonationCounselling);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
        .createPostDonationCounsellingForDonation(donation);

    verify(postDonationCounsellingRepository, never()).save(argThat(hasSameStateAsPostDonationCounselling(existingPostDonationCounselling)));
    assertThat(returnedPostDonationCounselling, hasSameStateAsPostDonationCounselling(existingPostDonationCounselling));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUpdatePostDonationCounsellingWithNoExistingPostDonationCounselling_shouldThrow() {
    UUID postDonationCounsellingId = UUID.randomUUID();
    PostDonationCounselling postDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .build();

    when(postDonationCounsellingRepository.findById(postDonationCounsellingId)).thenReturn(null);

    postDonationCounsellingCRUDService.updatePostDonationCounselling(postDonationCounselling);
  }

  @Test
  public void testUpdatePostDonationCounselling_shouldUpdateAndReturnPostDonationCounselling() {

    UUID postDonationCounsellingId = UUID.randomUUID();
    CounsellingStatus counsellingStatus = CounsellingStatus.RECEIVED_COUNSELLING;
    Date existingCounsellingDate = new DateTime().minusDays(1).toDate();
    Date counsellingDate = new Date();
    String notes = "some notes";
    
    Location referralSite = aReferralSite()
        .withName("ReferrealSite")
        .build();
    
    PostDonationCounselling updatedPostDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .withCounsellingDate(counsellingDate)
        .withCounsellingStatus(counsellingStatus)
        .thatIsNotFlaggedForCounselling()
        .withDonation(null)
        .withNotes(notes)
        .thatIsReferred()
        .withReferralSite(referralSite)
        .build();

    PostDonationCounselling existingPostDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .thatIsFlaggedForCounselling()
        .thatIsNotDeleted()
        .withCounsellingDate(existingCounsellingDate)
        .withCounsellingStatus(null)
        .thatIsNotReferred()
        .withDonation(aDonation()
            .withId(DONATION_ID)
            .build())
        .build();

    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
        .withId(postDonationCounsellingId)
        .thatIsNotFlaggedForCounselling()
        .thatIsNotDeleted()
        .withCounsellingStatus(counsellingStatus)
        .withCounsellingDate(counsellingDate)
        .thatIsReferred()
        .withReferralSite(referralSite)
        .withDonation(aDonation()
            .withId(DONATION_ID)
            .build())
        .withNotes(notes)
        .build();

    when(postDonationCounsellingRepository.findById(postDonationCounsellingId))
        .thenReturn(existingPostDonationCounselling);
    when(postDonationCounsellingRepository.update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling))))
        .thenReturn(expectedPostDonationCounselling);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
        .updatePostDonationCounselling(updatedPostDonationCounselling);

    verify(postDonationCounsellingRepository).findById(postDonationCounsellingId);
    verify(postDonationCounsellingRepository).update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));
    assertThat(returnedPostDonationCounselling, is(expectedPostDonationCounselling));

  }

}
