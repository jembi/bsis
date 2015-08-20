package service;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.PostDonationCounsellingBuilder.aPostDonationCounselling;
import static helpers.matchers.PostDonationCounsellingMatcher.hasSameStateAsPostDonationCounselling;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
                .withDonation(donation)
                .build();
        
        PostDonationCounselling returnedPostDonationCounselling = postDonationCounsellingCRUDService
                .createPostDonationCounsellingForDonation(donation);
        
        verify(postDonationCounsellingRepository).save(argThat(hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling)));
        verifyNoMoreInteractions(postDonationCounsellingRepository);
        assertThat(returnedPostDonationCounselling, hasSameStateAsPostDonationCounselling(expectedPostDonationCounselling));
    }

}
