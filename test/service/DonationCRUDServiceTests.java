package service;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.matchers.DonationMatcher.hasSameStateAsDonation;
import static helpers.matchers.DonorMatcher.hasSameStateAsDonor;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import model.donation.Donation;
import model.donor.Donor;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DonationRepository;
import repository.DonorRepository;

@RunWith(MockitoJUnitRunner.class)
public class DonationCRUDServiceTests {
    
    private static final long IRRELEVANT_DONATION_ID = 2;
    private static final long IRRELEVANT_DONOR_ID = 7;
    private static final Date IRRELEVANT_DATE_OF_FIRST_DONATION = new DateTime().minusDays(7).toDate();
    private static final Date IRRELEVANT_DATE_OF_LAST_DONATION = new DateTime().minusDays(2).toDate();

    @InjectMocks
    private DonationCRUDService donationCRUDService;
    @Mock
    private DonationConstraintChecker donationConstraintChecker;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private DonorRepository donorRepository;
    
    @Test(expected = IllegalStateException.class)
    public void testDeleteDonationWithConstraints_shouldThrow() {
        
        when(donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID)).thenReturn(false);

        donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);
        
        verify(donationConstraintChecker).canDeletedDonation(IRRELEVANT_DONATION_ID);
        verifyNoMoreInteractions(donationConstraintChecker, donationRepository, donorRepository);
    }

    @Test
    public void testDeleteDonationWithFirstDonation_shouldSoftDeleteDonationAndUpdateDonorFirstDonationDate() {

        // Set up fixture
        Donor existingDonor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
                .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
                .build();
        Donation existingDonation = aDonation()
                .withId(IRRELEVANT_DONATION_ID)
                .withDonor(existingDonor)
                .withDonationDate(IRRELEVANT_DATE_OF_FIRST_DONATION)
                .build();

        // Set up expectations
        Donation expectedDonation = aDonation()
                .thatIsDeleted()
                .withId(IRRELEVANT_DONATION_ID)
                .withDonor(existingDonor)
                .withDonationDate(IRRELEVANT_DATE_OF_FIRST_DONATION)
                .build();
        Date expectedDateOfFirstDonation = new Date();
        Donor expectedDonor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDateOfFirstDonation(expectedDateOfFirstDonation)
                .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
                .build();
        
        when(donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
        when(donationRepository.findDateOfFirstDonationForDonor(IRRELEVANT_DONOR_ID)).thenReturn(expectedDateOfFirstDonation);

        // Exercise SUT
        donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);
        
        // Verify
        verify(donationConstraintChecker).canDeletedDonation(IRRELEVANT_DONATION_ID);
        verify(donationRepository).findDonationById(IRRELEVANT_DONATION_ID);
        verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
        verify(donationRepository).findDateOfFirstDonationForDonor(IRRELEVANT_DONOR_ID);
        verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
        verifyNoMoreInteractions(donationConstraintChecker, donationRepository, donorRepository);
    }

    @Test
    public void testDeleteDonationWithLastDonation_shouldSoftDeleteDonationAndUpdateDonorLastDonationDate() {

        // Set up fixture
        Donor existingDonor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
                .withDateOfLastDonation(IRRELEVANT_DATE_OF_LAST_DONATION)
                .build();
        Donation existingDonation = aDonation()
                .withId(IRRELEVANT_DONATION_ID)
                .withDonor(existingDonor)
                .withDonationDate(IRRELEVANT_DATE_OF_LAST_DONATION)
                .build();

        // Set up expectations
        Donation expectedDonation = aDonation()
                .thatIsDeleted()
                .withId(IRRELEVANT_DONATION_ID)
                .withDonor(existingDonor)
                .withDonationDate(IRRELEVANT_DATE_OF_LAST_DONATION)
                .build();
        Date expectedDateOfLastDonation = new Date();
        Donor expectedDonor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDateOfFirstDonation(IRRELEVANT_DATE_OF_FIRST_DONATION)
                .withDateOfLastDonation(expectedDateOfLastDonation)
                .build();
        
        when(donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID)).thenReturn(true);
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(existingDonation);
        when(donationRepository.findDateOfLastDonationForDonor(IRRELEVANT_DONOR_ID)).thenReturn(expectedDateOfLastDonation);

        // Exercise SUT
        donationCRUDService.deleteDonation(IRRELEVANT_DONATION_ID);
        
        // Verify
        verify(donationConstraintChecker).canDeletedDonation(IRRELEVANT_DONATION_ID);
        verify(donationRepository).findDonationById(IRRELEVANT_DONATION_ID);
        verify(donationRepository).updateDonation(argThat(hasSameStateAsDonation(expectedDonation)));
        verify(donationRepository).findDateOfLastDonationForDonor(IRRELEVANT_DONOR_ID);
        verify(donorRepository).updateDonor(argThat(hasSameStateAsDonor(expectedDonor)));
        verifyNoMoreInteractions(donationConstraintChecker, donationRepository, donorRepository);
    }

}
