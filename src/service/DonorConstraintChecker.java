package service;

import javax.persistence.NoResultException;

import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationRepository;
import repository.DonorDeferralRepository;
import repository.DonorRepository;

@Transactional(readOnly = true)
@Service
public class DonorConstraintChecker {
    
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonorDeferralRepository donorDeferralRepository;
    
    public boolean canDeleteDonor(long donorId) throws NoResultException {
        Donor donor = donorRepository.findDonorById(donorId);
        
        if (donor.getNotes() != null && !donor.getNotes().isEmpty()) {
            return false;
        }
        
        if (donationRepository.countDonationsForDonor(donor) > 0) {
            return false;
        }
        
        if (donorDeferralRepository.countDonorDeferralsForDonor(donor) > 0) {
            return false;
        }

        return true;
    }

}
