package service;

import static helpers.builders.DonorBuilder.aDonor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
public class DonorConstraintCheckerTests {
    
    private static final Long IRRELEVANT_DONOR_ID = 99L;
    
    @InjectMocks
    private DonorConstraintChecker donorConstraintChecker;
    @Mock
    private DonorRepository donorRepository;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private DonorDeferralRepository donorReferralRepository;
    
    @Test
    public void testCanDeleteDonorWithDonorWithNotes_shouldReturnFalse() {
        
        Donor donorWithNotes = aDonor().withNotes("irrelevant.notes").build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donorWithNotes);
        
        boolean canDelete = donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID);
        
        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verifyNoMoreInteractions(donorRepository, donationRepository);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonorWithDonorWithDonations_shouldReturnFalse() {
        
        Donor donorWithDonations = aDonor().build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donorWithDonations);
        when(donationRepository.countDonationsForDonor(donorWithDonations)).thenReturn(1);
        
        boolean canDelete = donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID);

        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verify(donationRepository).countDonationsForDonor(donorWithDonations);
        verifyNoMoreInteractions(donorRepository, donationRepository);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonorWithDonorWithDeferrals_shouldReturnFalse() {
        
        Donor donorWithDeferrals = aDonor().build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donorWithDeferrals);
        when(donationRepository.countDonationsForDonor(donorWithDeferrals)).thenReturn(0);
        when(donorReferralRepository.countDonorDeferralsForDonor(donorWithDeferrals)).thenReturn(1);
        
        boolean canDelete = donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID);

        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verify(donationRepository).countDonationsForDonor(donorWithDeferrals);
        verify(donorReferralRepository).countDonorDeferralsForDonor(donorWithDeferrals);
        verifyNoMoreInteractions(donorRepository, donationRepository);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonorWithNoConstraints_shouldReturnTrue() {
        
        Donor existingDonor = aDonor().withNotes("").build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(existingDonor);
        when(donationRepository.countDonationsForDonor(existingDonor)).thenReturn(0);
        when(donorReferralRepository.countDonorDeferralsForDonor(existingDonor)).thenReturn(0);
        
        boolean canDelete = donorConstraintChecker.canDeleteDonor(IRRELEVANT_DONOR_ID);

        verify(donorRepository).findDonorById(IRRELEVANT_DONOR_ID);
        verify(donationRepository).countDonationsForDonor(existingDonor);
        verify(donorReferralRepository).countDonorDeferralsForDonor(existingDonor);
        verifyNoMoreInteractions(donorRepository, donationRepository);
        
        assertThat(canDelete, is(true));
    }
}
