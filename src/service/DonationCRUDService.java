package service;

import java.util.Date;

import javax.persistence.NoResultException;

import model.donation.Donation;
import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationRepository;
import repository.DonorRepository;

@Transactional
@Service
public class DonationCRUDService {
    
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private DonationConstraintChecker donationConstraintChecker;
    @Autowired
    private DonorCRUDService donorCRUDService;
    @Autowired
    private DonorRepository donorRepository;
    
    public void deleteDonation(long donationId) throws IllegalStateException, NoResultException {
        
        if (!donationConstraintChecker.canDeletedDonation(donationId)) {
            throw new IllegalStateException("Cannot delete donation with constraints");
        }
        
        // Soft delete donation
        Donation donation = donationRepository.findDonationById(donationId);
        donation.setIsDeleted(true);
        donationRepository.updateDonation(donation);
        
        Date donationDate = donation.getDonationDate();
        Donor donor = donation.getDonor();
        
        // If this was the donor's first donation
        if (donationDate.equals(donor.getDateOfFirstDonation())) {
            Date dateOfFirstDonation = donationRepository.findDateOfFirstDonationForDonor(donor.getId());
            donor.setDateOfFirstDonation(dateOfFirstDonation);
            donorRepository.updateDonor(donor);
        }
        
        // If this was the donor's last donation
        if (donationDate.equals(donor.getDateOfLastDonation())) {
            Date dateOfLastDonation = donationRepository.findDateOfLastDonationForDonor(donor.getId());
            donor.setDateOfLastDonation(dateOfLastDonation);
            donorRepository.updateDonor(donor);
        }
    }

}
