package org.jembi.bsis.service;

import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DonorCRUDService {

  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  public void deleteDonor(UUID donorId) throws IllegalStateException, NoResultException {

    if (!donorConstraintChecker.canDeleteDonor(donorId)) {
      throw new IllegalStateException("Cannot delete donor with constraints");
    }

    // Soft delete the donor
    Donor donor = donorRepository.findDonorById(donorId);
    donor.setIsDeleted(true);
    donorRepository.updateDonor(donor);
  }

}
