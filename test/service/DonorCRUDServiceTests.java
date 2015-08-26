package service;

import static helpers.builders.DonorBuilder.aDonor;
import static helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import model.donor.Donor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DonationRepository;
import repository.DonorDeferralRepository;
import repository.DonorRepository;

@RunWith(MockitoJUnitRunner.class)
public class DonorCRUDServiceTests {
    
    private static final Long IRRELEVANT_DONOR_ID = 99L;
    
    @InjectMocks
    private DonorCRUDService donorCRUDService;
    @Mock
    private DonorRepository donorRepository;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private DonorDeferralRepository donorReferralRepository;
    
    @Test(expected = IllegalStateException.class)
    public void testDeleteDonorWithDonorWithNotes_shouldThrow() {
        
        Donor donorWithNotes = aDonor().withNotes("irrelevant.notes").build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donorWithNotes);
        
        donorCRUDService.deleteDonor(IRRELEVANT_DONOR_ID);
        
        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verifyNoMoreInteractions(donorRepository, donationRepository);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeleteDonorWithDonorWithDonations_shouldThrow() {
        
        Donor donorWithDonations = aDonor().build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donorWithDonations);
        when(donationRepository.countDonationsForDonor(donorWithDonations)).thenReturn(1);
        
        donorCRUDService.deleteDonor(IRRELEVANT_DONOR_ID);

        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verify(donationRepository).countDonationsForDonor(donorWithDonations);
        verifyNoMoreInteractions(donorRepository, donationRepository);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeleteDonorWithDonorWithDeferrals_shouldThrow() {
        
        Donor donorWithDeferrals = aDonor().build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donorWithDeferrals);
        when(donationRepository.countDonationsForDonor(donorWithDeferrals)).thenReturn(0);
        when(donorReferralRepository.countDonorDeferralsForDonor(donorWithDeferrals)).thenReturn(1);
        
        donorCRUDService.deleteDonor(IRRELEVANT_DONOR_ID);

        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verify(donationRepository).countDonationsForDonor(donorWithDeferrals);
        verify(donorReferralRepository).countDonorDeferralsForDonor(donorWithDeferrals);
        verifyNoMoreInteractions(donorRepository, donationRepository);
    }
    
    @Test
    public void testDeleteDonor_shouldSoftDeleteDonor() {
        
        Donor existingDonor = aDonor().withNotes("").build();
        Donor expectedDonor = aDonor().thatIsDeleted().withNotes("").build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(existingDonor);
        when(donationRepository.countDonationsForDonor(existingDonor)).thenReturn(0);
        when(donorReferralRepository.countDonorDeferralsForDonor(existingDonor)).thenReturn(0);
        
        donorCRUDService.deleteDonor(IRRELEVANT_DONOR_ID);

        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verify(donationRepository).countDonationsForDonor(existingDonor);
        verify(donorReferralRepository).countDonorDeferralsForDonor(existingDonor);
        verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
        verifyNoMoreInteractions(donorRepository, donationRepository);
    }

}
