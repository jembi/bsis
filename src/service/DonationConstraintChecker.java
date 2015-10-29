package service;

import javax.persistence.NoResultException;
import model.donation.Donation;
import model.donor.Donor;
import model.packtype.PackType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import repository.BloodTestResultRepository;
import repository.ComponentRepository;
import repository.DonationRepository;
import repository.DonorRepository;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonationConstraintChecker {
    
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private BloodTestResultRepository bloodTestResultRepository;
    @Autowired
    private ComponentRepository componentRepository;
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private DonorDeferralStatusCalculator donorDeferralStatusCalculator;
    
    public boolean isDonorEligibleToDonate(long donorId) {
        
        Donor donor = donorRepository.findDonorById(donorId);
        
        if (donor.getDonations() != null) {

            for (Donation donation : donor.getDonations()) {
    
                PackType packType = donation.getPackType();
    
                if (!packType.getCountAsDonation()) {
                    // Don't check period between donations if it doesn't count as a donation
                    continue;
                }
    
                // Work out the next allowed donation date
                DateTime nextDonationDate = new DateTime(donation.getDonationDate())
                        .plusDays(packType.getPeriodBetweenDonations())
                        .withTimeAtStartOfDay();
                
                // Check if the next allowed donation date is after today
                if (nextDonationDate.isAfter(new DateTime().withTimeAtStartOfDay())) {
                    return false;
                }
            }
        }

        if (donorDeferralStatusCalculator.isDonorCurrentlyDeferred(donor)) {
            return false;
        }
        
        return true;
    }
    
    public boolean canDeletedDonation(long donationId) throws NoResultException {

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

}
