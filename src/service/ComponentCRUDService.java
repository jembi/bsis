package service;

import model.component.ComponentStatus;
import model.donation.Donation;
import model.donor.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.ComponentRepository;

@Transactional
@Service
public class ComponentCRUDService {
    
    @Autowired
    private ComponentRepository componentRepository;
    
    /**
     * Change the status of components belonging to the donor from AVAILABLE to UNSAFE.
     */
    public void markComponentsBelongingToDonorAsUnsafe(Donor donor) {
        componentRepository.updateComponentStatusForDonor(ComponentStatus.AVAILABLE, ComponentStatus.UNSAFE, donor);
    }

    /**
     * Change the status of components linked to the donation from AVAILABLE to UNSAFE.
     */
    public void markComponentsBelongingToDonationAsUnsafe(Donation donation) {
        componentRepository.updateComponentStatusForDonation(ComponentStatus.AVAILABLE, ComponentStatus.UNSAFE, donation);
    }

}
