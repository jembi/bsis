package service;

import javax.persistence.NoResultException;

import model.component.Component;
import model.donation.Donation;
import model.donationbatch.DonationBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationBatchRepository;
import repository.DonationRepository;

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

    return donationBatchRepository.updateDonationBatch(existingDonationBatch);
  }
}
