package service;

import java.util.Date;
import java.util.Objects;

import javax.persistence.NoResultException;

import model.donation.Donation;
import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backingform.DonationBackingForm;
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
    
    public Donation updateDonation(long donationId, DonationBackingForm donationBackingForm) {
        Donation donation = donationRepository.findDonationById(donationId);
        
        // Check if pack type or bleed times have been updated
        boolean donationFieldsUpdated = !Objects.equals(donation.getPackType(), donationBackingForm.getPackType()) ||
                !Objects.equals(donation.getBleedStartTime(), donationBackingForm.getBleedStartTime()) ||
                !Objects.equals(donation.getBleedEndTime(), donationBackingForm.getBleedEndTime());
        
        if (donationFieldsUpdated && !donationConstraintChecker.canUpdateDonationFields(donationId)) {
            throw new IllegalArgumentException("Cannot update donation fields");
        }
        
        donation.setDonorPulse(donationBackingForm.getDonorPulse());
        donation.setHaemoglobinCount(donationBackingForm.getHaemoglobinCount());
        donation.setHaemoglobinLevel(donationBackingForm.getHaemoglobinLevel());
        donation.setBloodPressureSystolic(donationBackingForm.getBloodPressureSystolic());
        donation.setBloodPressureDiastolic(donationBackingForm.getBloodPressureDiastolic());
        donation.setDonorWeight(donationBackingForm.getDonorWeight());
        donation.setNotes(donationBackingForm.getNotes());
        donation.setPackType(donationBackingForm.getPackType());
        donation.setBleedStartTime(donationBackingForm.getBleedStartTime());
        donation.setBleedEndTime(donationBackingForm.getBleedEndTime());
        donation.setAdverseEvent(donationBackingForm.getAdverseEvent());
        return donationRepository.updateDonation(donation);
    }

}
