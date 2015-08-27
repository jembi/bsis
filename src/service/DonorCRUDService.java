package service;

import javax.persistence.NoResultException;

import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonorRepository;

@Transactional
@Service
public class DonorCRUDService {
    
    @Autowired
    private DonorRepository donorRepository;
    @Autowired
    private DonorConstraintChecker donorConstraintChecker;
    
    public void deleteDonor(long donorId) throws IllegalStateException, NoResultException {

        if (!donorConstraintChecker.canDeleteDonor(donorId)) {
            throw new IllegalStateException("Cannot delete donor with constraints");
        }
        
        // Soft delete the donor
        Donor donor = donorRepository.findDonorById(donorId);
        donor.setIsDeleted(true);
        donorRepository.updateDonor(donor);
    }

}
