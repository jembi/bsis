package org.jembi.bsis.service;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DonationBatchCRUDService {

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private DonationBatchConstraintChecker donationBatchConstraintChecker;

  @Autowired
  private DateGeneratorService dateService;

  public void deleteDonationBatch(Long donationBatchId) throws IllegalStateException, NoResultException {

    if (!donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId)) {
      throw new IllegalStateException("Cannot delete donation batch with constraints");
    }

    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
    donationBatch.setIsDeleted(true);

    donationBatchRepository.updateDonationBatch(donationBatch);
  }

  public DonationBatch updateDonationBatch(DonationBatch donationBatch) {
    // get the existing donation batch
    DonationBatch existingDonationBatch = donationBatchRepository.findDonationBatchById(donationBatch.getId());

    // if the created date or venue has changed, update the donations
    for (Donation donation : existingDonationBatch.getDonations()) {
      if (donation.getDonationDate().getTime() != donationBatch.getCreatedDate().getTime()) {
        donation.setDonationDate(dateService.generateDateTime(donationBatch.getCreatedDate(), donation.getDonationDate()));
      }
      if (donation.getVenue().getId() != donationBatch.getVenue().getId()) {
        donation.setVenue(donationBatch.getVenue());
      }
      if (donation.getComponents() != null) {
        for (Component component : donation.getComponents()) {
          component.setCreatedOn(dateService.generateDateTime(donationBatch.getCreatedDate(),
              component.getCreatedOn()));
        }
      }
      donationRepository.updateDonation(donation);
    }

    // update the updateable fields
    existingDonationBatch.setIsClosed(donationBatch.getIsClosed());
    existingDonationBatch.setVenue(donationBatch.getVenue());
    existingDonationBatch.setCreatedDate(donationBatch.getCreatedDate());

    // persist the changes
    DonationBatch updatedDonationBatch = donationBatchRepository.updateDonationBatch(existingDonationBatch);

    return updatedDonationBatch;
  }
}
