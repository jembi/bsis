package service;

import javax.persistence.NoResultException;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import repository.BloodTestResultRepository;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonationConstraintChecker {
    
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private BloodTestResultRepository bloodTestResultRepository;
    @Autowired
    private ComponentRepository componentRepository;
    
    public boolean canDeleteDonation(long donationId) throws NoResultException {

        Donation donation = donationRepository.findDonationById(donationId);
        
        // Check for comments
        if (donation.getNotes() != null && !donation.getNotes().isEmpty()) {
            return false;
        }
        
        // Check for recorded test results
        if (bloodTestResultRepository.countBloodTestResultsForDonation(donationId) > 0) {
            return false;
        }
        
        // Check for processed components
        if (componentRepository.countChangedComponentsForDonation(donationId) > 0) {
            return false;
        }
        
        return true;
    }
    
    public boolean canUpdateDonationFields(long donationId) {

        // Check for recorded test results
        if (bloodTestResultRepository.countBloodTestResultsForDonation(donationId) > 0) {
            return false;
        }
        
        // Check for processed components
        if (componentRepository.countChangedComponentsForDonation(donationId) > 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Test outcome discrepancies: tti tests that require confirmatory testing, blood group serology test outcomes that
     * are ambiguous, or require a confirmatory outcome.
     */
    public boolean donationHasDiscrepancies(Donation donation) {
        
        return donation.getTTIStatus() == TTIStatus.NOT_DONE
                || donation.getBloodTypingMatchStatus() == BloodTypingMatchStatus.AMBIGUOUS
                || donation.getBloodTypingStatus() ==  BloodTypingStatus.PENDING_TESTS;
    }

}
