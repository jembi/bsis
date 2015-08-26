package service;

import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationRepository;
import repository.DonorDeferralRepository;
import repository.DonorRepository;

@Transactional
@Service
public class DonorCRUDService {
    
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonorDeferralRepository donorDeferralRepository;
    
    public void deleteDonor(long donorId) {
        Donor donor = donorRepository.findDonorById(donorId);
        
        if (donor.getNotes() != null && !donor.getNotes().isEmpty()) {
            throw new IllegalStateException("Cannot delete donor with notes");
        }
        
        if (donationRepository.countDonationsForDonor(donor) > 0) {
            throw new IllegalStateException("Cannot delete donor with donations");
        }
        
        if (donorDeferralRepository.countDonorDeferralsForDonor(donor) > 0) {
            throw new IllegalStateException("Cannot delete donor with deferrals");
        }
        
        // Soft delete the donor
        donor.setIsDeleted(true);
        donorRepository.updateDonor(donor);
    }

}
