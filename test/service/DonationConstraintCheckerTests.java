package service;

import static helpers.builders.DonationBuilder.aDonation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.BloodTestResultRepository;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;

@RunWith(MockitoJUnitRunner.class)
public class DonationConstraintCheckerTests {
    
    private static final long IRRELEVANT_DONATION_ID = 17;

    @InjectMocks
    private DonationConstraintChecker donationConstraintChecker;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private BloodTestResultRepository bloodTestResultRepository;
    @Mock
    private ComponentRepository componentRepository;
    
    @Test
    public void testCanDeleteDonationWithDonationWithNotes_shouldReturnFalse() {
        Donation donationWithNotes = aDonation().withNotes("irrelevant.notes").build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
        
        boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonationWithDonationWithBloodTestResults_shouldReturnFalse() {
        Donation donationWithTestResults = aDonation().build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithTestResults);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);
        
        boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonationWithDonationWithChangedComponents_shouldReturnFalse() {
        Donation donationWithChangedComponents = aDonation().build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithChangedComponents);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);
        
        boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonationWithDonationWithNoConstraints_shouldReturnTrue() {
        Donation donationWithNotes = aDonation().withNotes("").build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        
        boolean canDelete = donationConstraintChecker.canDeleteDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(true));
    }
    
    @Test
    public void testCanUpdateDonationFieldsWithDonationWithBloodTestResults_shouldReturnFalse() {
        Donation donationWithNotes = aDonation().build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);
        
        boolean canDelete = donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanUpdateDonationFieldsWithDonationWithChangedComponents_shouldReturnFalse() {
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);
        
        boolean canDelete = donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanUpdateDonationWithFieldsDonationWithNoConstraints_shouldReturnTrue() {
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        
        boolean canDelete = donationConstraintChecker.canUpdateDonationFields(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(true));
    }
    
    @Test
    public void testDonationHasDiscrepanciesWithNoDiscrepancies_shouldReturnFalse() {
        Donation donation = aDonation()
                .withTTIStatus(TTIStatus.TTI_SAFE)
                .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
                .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
                .build();
        
        boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testDonationHasDiscrepanciesWithNotDoneTTIStatus_shouldReturnTrue() {
        Donation donation = aDonation()
                .withTTIStatus(TTIStatus.NOT_DONE)
                .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
                .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
                .build();
        
        boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testDonationHasDiscrepanciesWithAmbiguousBloodTypingMatchStatus_shouldReturnTrue() {
        Donation donation = aDonation()
                .withTTIStatus(TTIStatus.TTI_SAFE)
                .withBloodTypingMatchStatus(BloodTypingMatchStatus.AMBIGUOUS)
                .withBloodTyingStatus(BloodTypingStatus.COMPLETE)
                .build();
        
        boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testDonationHasDiscrepanciesWithPendingTestsBloodTypingStatus_shouldReturnTrue() {
        Donation donation = aDonation()
                .withTTIStatus(TTIStatus.TTI_SAFE)
                .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
                .withBloodTyingStatus(BloodTypingStatus.PENDING_TESTS)
                .build();
        
        boolean result = donationConstraintChecker.donationHasDiscrepancies(donation);
        
        assertThat(result, is(true));
    }

}
