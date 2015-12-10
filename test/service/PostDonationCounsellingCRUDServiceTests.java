package service;

import controller.UtilController;
import helpers.builders.UserBuilder;
import model.counselling.CounsellingStatus;
import model.counselling.PostDonationCounselling;
import model.donation.Donation;
import model.user.User;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.PostDonationCounsellingRepository;

import java.util.Date;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static helpers.matchers.PostDonationCounsellingMatcher.hasSameStateAsPostDonationCounselling;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostDonationCounsellingCRUDServiceTests {

  @InjectMocks
  private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
  @Mock
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Mock
  private UtilController utilController;
  @Mock
  private DateGeneratorService dateGeneratorService;

  @Test
  public void testCreatePostDonationCounselling_shouldPersistAndReturnAFlaggedPostDonationCounsellingForDonation() {
    Donation donation = aDonation().withId(23L).build();
    Date counsellingDate = new Date();


    User admin = UserBuilder.aUser()
            .withUsername("admin")
            .withId(1)
            .build();

    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
            .withId(null)
            .thatIsFlaggedForCounselling()
            .thatIsNotDeleted()
            .withDonation(donation)
            .withCreatedBy(admin)
            .withCreatedDate(counsellingDate)
            .withLastUpdated(counsellingDate)
            .withLastUpdatedBy(admin)
            .withCounsellingStatus(null)
            .withCounsellingDate(null)
            .build();

    when(utilController.getCurrentUser()).thenReturn(admin);
    when(dateGeneratorService.generateDate()).thenReturn(counsellingDate);

    when(postDonationCounsellingRepository.findPostDonationCounsellingForDonation(donation)).thenReturn(null);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
            .createPostDonationCounsellingForDonation(donation);

    verify(postDonationCounsellingRepository).save(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));
    assertThat(returnedPostDonationCounselling, hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling));
  }

  @Test
  public void testCreatePostDonationCounsellingWithExistingCounselling_shouldReturnExistingCounselling() {
    Donation donation = aDonation().withId(23L).build();

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
    long postDonationCounsellingId = 75;

    when(postDonationCounsellingRepository.findById(postDonationCounsellingId))
            .thenReturn(null);

    postDonationCounsellingCRUDService.updatePostDonationCounselling(postDonationCounsellingId,
            CounsellingStatus.RECEIVED_COUNSELLING, new Date(), "");
  }

  @Test
  public void testUpdatePostDonationCounselling_shouldUpdateAndReturnPostDonationCounselling() {

    long postDonationCounsellingId = 75;
    long donationId = 55;
    CounsellingStatus counsellingStatus = CounsellingStatus.RECEIVED_COUNSELLING;
    Date existingCounsellingDate = new DateTime().minusDays(1).toDate();
    Date counsellingDate = new Date();
    String notes = "some notes";

    User admin = UserBuilder.aUser()
            .withUsername("admin")
            .withId(1)
            .build();

    User ordinary = UserBuilder.aUser()
            .withUsername("ordinary")
            .withId(2)
            .build();


    PostDonationCounselling existingPostDonationCounselling = aPostDonationCounselling()
            .withId(postDonationCounsellingId)
            .thatIsFlaggedForCounselling()
            .thatIsNotDeleted()
            .withCreatedBy(ordinary)
            .withLastUpdated(existingCounsellingDate)
            .withLastUpdatedBy(ordinary)
            .withCreatedDate(existingCounsellingDate)
            .withDonation(aDonation()
                    .withId(donationId)
                    .build())
            .build();

    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
            .withId(postDonationCounsellingId)
            .thatIsNotFlaggedForCounselling()
            .thatIsNotDeleted()
            .withCreatedBy(ordinary)
            .withLastUpdated(counsellingDate)
            .withLastUpdatedBy(admin)
            .withCreatedDate(existingCounsellingDate)
            .withCounsellingStatus(counsellingStatus)
            .withCounsellingDate(counsellingDate)
            .withDonation(aDonation()
                    .withId(donationId)
                    .withNotes(notes)
                    .build())
            .build();

    when(utilController.getCurrentUser()).thenReturn(admin);
    when(dateGeneratorService.generateDate()).thenReturn(counsellingDate);

    when(postDonationCounsellingRepository.findById(postDonationCounsellingId))
            .thenReturn(existingPostDonationCounselling);
    when(postDonationCounsellingRepository.update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling))))
            .thenReturn(expectedPostDonationCounselling);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
            .updatePostDonationCounselling(postDonationCounsellingId, counsellingStatus, counsellingDate, notes);

    verify(postDonationCounsellingRepository).findById(postDonationCounsellingId);
    verify(postDonationCounsellingRepository).update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));
    assertThat(returnedPostDonationCounselling, is(expectedPostDonationCounselling));

  }

  @Test
  public void testFlagForCounselling_shouldFlagForCounsellingAndReturnPostDonationCounselling() {
    long postDonationCounsellingId = 75;
    long donationId = 55;
    CounsellingStatus counsellingStatus = CounsellingStatus.RECEIVED_COUNSELLING;
    Date existingCounsellingDate = new DateTime().minusDays(1).toDate();
    Date counsellingDate = new Date();
    String notes = "some notes";

    User admin = UserBuilder.aUser()
            .withUsername("admin")
            .withId(1)
            .build();

    User ordinary = UserBuilder.aUser()
            .withUsername("ordinary")
            .withId(2)
            .build();

    PostDonationCounselling existingPostDonationCounselling = aPostDonationCounselling()
            .withId(postDonationCounsellingId)
            .thatIsNotFlaggedForCounselling()
            .thatIsNotDeleted()
            .withCounsellingStatus(counsellingStatus)
            .withCounsellingDate(existingCounsellingDate)
            .withCreatedBy(ordinary)
            .withLastUpdated(existingCounsellingDate)
            .withLastUpdatedBy(ordinary)
            .withCreatedDate(existingCounsellingDate)
            .withDonation(aDonation()
                    .withId(donationId)
                    .withNotes(notes)
                    .build())
            .build();

    PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
            .withId(postDonationCounsellingId)
            .thatIsFlaggedForCounselling()
            .thatIsNotDeleted()
            .withCreatedDate(existingCounsellingDate)
            .withCreatedBy(ordinary)
            .withLastUpdated(counsellingDate)
            .withLastUpdatedBy(admin)
            .withCounsellingStatus(null)
            .withCounsellingDate(null)
            .withDonation(aDonation()
                    .withId(donationId)
                    .withNotes(null)
                    .build())
            .build();

    when(utilController.getCurrentUser()).thenReturn(admin);

    when(dateGeneratorService.generateDate()).thenReturn(counsellingDate);

    when(postDonationCounsellingRepository.findById(postDonationCounsellingId))
            .thenReturn(existingPostDonationCounselling);
    when(postDonationCounsellingRepository.update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling))))
            .thenReturn(expectedPostDonationCounselling);

    PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
            .flagForCounselling(postDonationCounsellingId);

    verify(postDonationCounsellingRepository).findById(postDonationCounsellingId);
    verify(postDonationCounsellingRepository).update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));

    assertThat(returnedPostDonationCounselling, is(expectedPostDonationCounselling));
  }

}
