package service;

import model.component.ComponentStatus;
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
     * Change the status of components belonging to a donor from AVAILABLE to UNSAFE.
     */
    public void markComponentsBelongingToDonorAsUnsafe(Donor donor) {
        componentRepository.updateComponentStatusForDonor(ComponentStatus.AVAILABLE, ComponentStatus.UNSAFE, donor);
    }

}
