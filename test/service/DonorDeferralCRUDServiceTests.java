package service;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorDeferralBuilder.aDonorDeferral;
import static helpers.matchers.DonorDeferralMatcher.hasSameStateAsDonorDeferral;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DeferralType;
import model.donordeferral.DonorDeferral;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DeferralReasonRepository;
import repository.DonorDeferralRepository;

@RunWith(MockitoJUnitRunner.class)
public class DonorDeferralCRUDServiceTests {
    
    @InjectMocks
    private DonorDeferralCRUDService donorDeferralCRUDService;
    @Mock
    private DonorDeferralRepository donorDeferralRepository;
    @Mock
    private DeferralReasonRepository deferralReasonRepository;
    
    @Test
    public void testCreateAutomatedUnsafeDeferralForDonor_shouldCreateAndPersistDonorDeferral() {
        
        Donor donor =  aDonor().build();
        DeferralReason deferralReason = aDeferralReason().build();
        
        DonorDeferral expectedDonorDeferral = aDonorDeferral()
                .thatIsNotVoided()
                .withDeferredDonor(donor)
                .withDeferralReason(deferralReason)
                .withDeferralType(DeferralType.PERMANENT)
                .withDeferredUntil(DonorDeferralCRUDService.PERMANENT_DEFERRAL_DATE)
                .build();
        
        when(deferralReasonRepository.findDeferralReasonByType(DeferralReasonType.AUTOMATED_TTI_UNSAFE))
                .thenReturn(deferralReason);
        
        DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createAutomatedUnsafeDeferralForDonor(donor);
        
        verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
        assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
    }

}
