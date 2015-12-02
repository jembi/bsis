package service;

import java.util.Date;
import java.util.Objects;
import javax.persistence.NoResultException;
import model.adverseevent.AdverseEvent;
import model.adverseevent.AdverseEventType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.donor.Donor;
import model.packtype.PackType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import backingform.AdverseEventBackingForm;
import backingform.DonationBackingForm;
import repository.DonationBatchRepository;
import repository.DonationRepository;
import repository.DonorRepository;
import repository.PackTypeRepository;

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
    @Autowired
    private DonationBatchRepository donationBatchRepository;
    @Autowired
    private ComponentCRUDService componentCRUDService;
    @Autowired
    private PackTypeRepository packTypeRepository;
    @Autowired
    private DonorConstraintChecker donorConstraintChecker;
    @Autowired
    private DonorService donorService;
    
    public void deleteDonation(long donationId) throws IllegalStateException, NoResultException {
        
        if (!donationConstraintChecker.canDeleteDonation(donationId)) {
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
        
        donorService.setDonorDueToDonate(donor);
    }
    
    public Donation createDonation(DonationBackingForm donationBackingForm) {

        Donation donation = donationBackingForm.getDonation();
        PackType packType = packTypeRepository.getPackTypeById(donation.getPackType().getId());

        boolean discardComponents = false;

        if (packType.getCountAsDonation() &&
                !donorConstraintChecker.isDonorEligibleToDonate(donationBackingForm.getDonor().getId())) {
        
            DonationBatch donationBatch = donationBatchRepository.findDonationBatchByBatchNumber(
                    donationBackingForm.getDonationBatchNumber());

            if (!donationBatch.isBackEntry()) { 
                throw new IllegalArgumentException("Do not bleed donor");
            }

            // The donation batch is being back entered so allow the donation to be created but discard the components
            discardComponents = true;
            donation.setIneligibleDonor(true);
        }
        
        updateAdverseEventForDonation(donation, donationBackingForm.getAdverseEvent());
        donationRepository.addDonation(donation);
        
        if (discardComponents) {
            componentCRUDService.markComponentsBelongingToDonationAsUnsafe(donation);
        }
        
        return donation;
    }
    
    public Donation updateDonation(long donationId, DonationBackingForm donationBackingForm) {
        Donation donation = donationRepository.findDonationById(donationId);
        
        // Check if pack type or bleed times have been updated
        boolean packTypeUpdated = !Objects.equals(donation.getPackType(), donationBackingForm.getPackType());
        boolean donationFieldsUpdated = packTypeUpdated ||
                !Objects.equals(donation.getBleedStartTime(), donationBackingForm.getBleedStartTime()) ||
                !Objects.equals(donation.getBleedEndTime(), donationBackingForm.getBleedEndTime());
        
        if (donationFieldsUpdated && !donationConstraintChecker.canUpdateDonationFields(donationId)) {
            throw new IllegalArgumentException("Cannot update donation fields");
        }

        PackType packType = packTypeRepository.getPackTypeById(donationBackingForm.getPackType().getId());

        if (packType.getCountAsDonation() && !donorConstraintChecker.isDonorEligibleToDonate(donation.getDonor().getId())) {

            DonationBatch donationBatch = donationBatchRepository.findDonationBatchByBatchNumber(
                    donationBackingForm.getDonationBatchNumber());

            if (!donationBatch.isBackEntry()) {
                throw new IllegalArgumentException("Cannot set pack type that produces components");
            }
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
        
        updateAdverseEventForDonation(donation, donationBackingForm.getAdverseEvent());
        
        donation = donationRepository.updateDonation(donation);
        
        if (packTypeUpdated) {
          donorService.setDonorDueToDonate(donation.getDonor());
        }
        
        return donation;
    }
    
    private void updateAdverseEventForDonation(Donation donation, AdverseEventBackingForm adverseEventBackingForm) {
        if (adverseEventBackingForm == null || adverseEventBackingForm.getType() == null) {
            // Delete the adverse event
            donation.setAdverseEvent(null);
            return;
        }

        // Get the existing adverse event or create a new one
        AdverseEvent adverseEvent = donation.getAdverseEvent();
        if (adverseEvent == null) {
            adverseEvent = new AdverseEvent();
        }
        
        // Create an adverse event type with the correct id
        AdverseEventType adverseEventType = new AdverseEventType();
        adverseEventType.setId(adverseEventBackingForm.getType().getId());
        
        // Update the fields
        adverseEvent.setType(adverseEventType);
        adverseEvent.setComment(adverseEventBackingForm.getComment());

        donation.setAdverseEvent(adverseEvent);
    }

}
