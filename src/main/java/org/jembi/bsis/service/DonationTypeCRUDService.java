package org.jembi.bsis.service;

import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DonationTypeCRUDService {
  
  @Autowired
  DonationTypeRepository donationTypeRepository;

  public DonationType updateDonationType(DonationType donationType) {
    DonationType existingDonationType = donationTypeRepository.getDonationTypeById(donationType.getId());
    existingDonationType.setDonationType(donationType.getDonationType());
    existingDonationType.setIsDeleted(donationType.getIsDeleted());
    return donationTypeRepository.update(existingDonationType);
  }
}
