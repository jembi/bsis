package service;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorDeferralBuilder.aDonorDeferral;
import static helpers.matchers.DonorDeferralMatcher.hasSameStateAsDonorDeferral;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DonorDeferral;
import model.donordeferral.DurationType;

import org.joda.time.DateTime;
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
    @Mock
    private DateGeneratorService dateGeneratorService;
    
    @Test
    public void testCreateDeferralForDonorWithDeferralReasonTypeWithPermanentDeferralReason_shouldCreateAndPersistDonorDeferral() {
        
        DeferralReasonType irrelevantDeferralReasonType = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
        Donor donor =  aDonor().build();
        DeferralReason deferralReason = aDeferralReason()
                .withType(irrelevantDeferralReasonType)
                .withDurationType(DurationType.PERMANENT)
                .build();
        
        DonorDeferral expectedDonorDeferral = aDonorDeferral()
                .withDeferredDonor(donor)
                .withDeferralReason(deferralReason)
                .withDeferredUntil(DonorDeferralCRUDService.PERMANENT_DEFERRAL_DATE)
                .build();
        
        when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
                .thenReturn(deferralReason);
        
        DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(
                donor, irrelevantDeferralReasonType);
        
        verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
        assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
    }
    
    @Test
    public void testCreateDeferralForDonorWithDeferralReasonTypeWithTemporaryDeferralReason_shouldCreateAndPersistDonorDeferral() {
        
        DeferralReasonType irrelevantDeferralReasonType = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
        int irrelevantDuration = 7;
        Date now = new Date();
        Donor donor =  aDonor().build();
        DeferralReason deferralReason = aDeferralReason()
                .withType(irrelevantDeferralReasonType)
                .withDurationType(DurationType.TEMPORARY)
                .withDefaultDuration(irrelevantDuration)
                .build();
        
        DonorDeferral expectedDonorDeferral = aDonorDeferral()
                .withDeferredDonor(donor)
                .withDeferralReason(deferralReason)
                .withDeferredUntil(new DateTime(now).plusDays(irrelevantDuration).toDate())
                .build();
        
        when(deferralReasonRepository.findDeferralReasonByType(irrelevantDeferralReasonType))
                .thenReturn(deferralReason);
        when(dateGeneratorService.generateDate()).thenReturn(now);
        
        DonorDeferral returnedDonorDeferral = donorDeferralCRUDService.createDeferralForDonorWithDeferralReasonType(
                donor, irrelevantDeferralReasonType);
        
        verify(donorDeferralRepository).save(argThat(hasSameStateAsDonorDeferral(expectedDonorDeferral)));
        assertThat(returnedDonorDeferral, hasSameStateAsDonorDeferral(expectedDonorDeferral));
    }

}
