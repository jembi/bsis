package service;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static helpers.matchers.PostDonationCounsellingMatcher.hasSameStateAsPostDonationCounselling;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Date;
import model.counselling.CounsellingStatus;
import model.counselling.PostDonationCounselling;
import model.donation.Donation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.PostDonationCounsellingRepository;

@RunWith(MockitoJUnitRunner.class)
public class PostDonationCounsellingCRUDServiceTests {
    
    @InjectMocks
    private PostDonationCounsellingCRUDService postDonationCounsellingCRUDService;
    @Mock
    private PostDonationCounsellingRepository postDonationCounsellingRepository;
    
    @Test
    public void testCreatePostDonationCounselling_shouldPersistAndReturnAFlaggedPostDonationCounsellingForDonation() {
        Donation donation = aDonation().withId(23L).build();
        
        PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
                .thatIsFlaggedForCounselling()
                .thatIsNotDeleted()
                .withDonation(donation)
                .build();
        
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
    
//    @Test
//    public void testUpdatePostDonationCounselling_shouldUpdateAndReturnPostDonationCounselling() {
//
//        long postDonationCounsellingId = 75;
//        long donationId = 55;
//        CounsellingStatus counsellingStatus = CounsellingStatus.RECEIVED_COUNSELLING;
//        Date counsellingDate = new Date();
//        String notes = "some notes";
//
//        PostDonationCounselling existingPostDonationCounselling = aPostDonationCounselling()
//                .withId(postDonationCounsellingId)
//                .thatIsFlaggedForCounselling()
//                .withDonation(aDonation()
//                        .withId(donationId)
//                        .build())
//                .build();
//
//        PostDonationCounselling expectedPostDonationCounselling = aPostDonationCounselling()
//                .withId(postDonationCounsellingId)
//                .thatIsNotFlaggedForCounselling()
//                .withCounsellingStatus(counsellingStatus)
//                .withCounsellingDate(counsellingDate)
//                .withDonation(aDonation()
//                        .withId(donationId)
//                        .withNotes(notes)
//                        .build())
//                .build();
//
//        when(postDonationCounsellingRepository.findById(postDonationCounsellingId))
//                .thenReturn(existingPostDonationCounselling);
//        when(postDonationCounsellingRepository.update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling))))
//                .thenReturn(expectedPostDonationCounselling);
//
//        PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
//                .updatePostDonationCounselling(postDonationCounsellingId, counsellingStatus, counsellingDate, notes);
//
//        verify(postDonationCounsellingRepository).findById(postDonationCounsellingId);
//        verify(postDonationCounsellingRepository).update(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));
//        assertThat(returnedPostDonationCounselling, is(expectedPostDonationCounselling));
//    }

}
