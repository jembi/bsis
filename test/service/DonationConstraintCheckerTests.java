package service;

import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.PackTypeBuilder.aPackType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
import repository.BloodTestResultRepository;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.DonorRepository;

@RunWith(MockitoJUnitRunner.class)
public class DonationConstraintCheckerTests {
    
    private static final long IRRELEVANT_DONATION_ID = 17;
    private static final long IRRELEVANT_DONOR_ID = 96;

    @InjectMocks
    private DonationConstraintChecker donationConstraintChecker;
    @Mock
    private DonationRepository donationRepository;
    @Mock
    private BloodTestResultRepository bloodTestResultRepository;
    @Mock
    private ComponentRepository componentRepository;
    @Mock
    private DonorRepository donorRepository;
    @Mock
    private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
    
    @Test
    public void testCanDeleteDonationWithDonationWithNotes_shouldReturnFalse() {
        Donation donationWithNotes = aDonation().withNotes("irrelevant.notes").build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
        
        boolean canDelete = donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonationWithDonationWithBloodTestResults_shouldReturnFalse() {
        Donation donationWithTestResults = aDonation().build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithTestResults);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);
        
        boolean canDelete = donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonationWithDonationWithChangedComponents_shouldReturnFalse() {
        Donation donationWithChangedComponents = aDonation().build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithChangedComponents);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(1);
        
        boolean canDelete = donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID);
        
        assertThat(canDelete, is(false));
    }
    
    @Test
    public void testCanDeleteDonationWithDonationWithNoConstraints_shouldReturnTrue() {
        Donation donationWithNotes = aDonation().withNotes("").build();
        
        when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donationWithNotes);
        when(bloodTestResultRepository.countBloodTestResultsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        when(componentRepository.countChangedComponentsForDonation(IRRELEVANT_DONATION_ID)).thenReturn(0);
        
        boolean canDelete = donationConstraintChecker.canDeletedDonation(IRRELEVANT_DONATION_ID);
        
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
    public void testIsDonorEligibleToDonateWithNonDeferredDonorWithoutDonations_shouldReturnTrue() {
        
        Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donor);
        when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
        
        boolean isEligible = donationConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID);
        
        assertThat(isEligible, is(true));
    }
    
    @Test
    public void testIsDonorEligibleToDonateWithDeferredDonorWithoutDonations_shouldReturnFalse() {
        
        Donor donor = aDonor().withId(IRRELEVANT_DONOR_ID).build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donor);
        when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(true);
        
        boolean isEligible = donationConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID);
        
        assertThat(isEligible, is(false));
    }
    
    @Test
    public void testIsDonorEligibleToDonateWithNonDeferredDonorWithDonationNotCountedAsDonation_shouldReturnTrue() {
        
        Donor donor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDonation(aDonation()
                        .withPackType(aPackType().withCountAsDonation(false).build())
                        .build())
                .build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donor);
        when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
        
        boolean isEligible = donationConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID);
        
        assertThat(isEligible, is(true));
    }
    
    @Test
    public void testIsDonorEligibleToDonateWithNonDeferredDonorWithDonationNotOverlappingPeriod_shouldReturnTrue() {
        
        Date previousDonationDate = new DateTime().minusDays(3).toDate();
        
        Donor donor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDonation(aDonation()
                        .withDonationDate(previousDonationDate)
                        .withPackType(aPackType()
                                .withCountAsDonation(true)
                                .withPeriodBetweenDonations(3)
                                .build())
                        .build())
                .build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donor);
        when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
        
        boolean isEligible = donationConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID);
        
        assertThat(isEligible, is(true));
    }
    
    @Test
    public void testIsDonorEligibleToDonateWithNonDeferredDonorWithDonationOverlappingPeriod_shouldReturnFalse() {
        
        Date previousDonationDate = new DateTime().minusDays(3).toDate();
        
        Donor donor = aDonor()
                .withId(IRRELEVANT_DONOR_ID)
                .withDonation(aDonation()
                        .withDonationDate(previousDonationDate)
                        .withPackType(aPackType()
                                .withCountAsDonation(true)
                                .withPeriodBetweenDonations(5)
                                .build())
                        .build())
                .build();
        
        when(donorRepository.findDonorById(IRRELEVANT_DONOR_ID)).thenReturn(donor);
        when(donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)).thenReturn(false);
        
        boolean isEligible = donationConstraintChecker.isDonorEligibleToDonate(IRRELEVANT_DONOR_ID);
        
        assertThat(isEligible, is(false));
    }

}
