package org.jembi.bsis.service;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonationBatchConstraintChecker {

  @Autowired
  private DonationBatchRepository donationBatchRepository;


  public boolean canDeleteDonationBatch(Long donationBatchId) throws NoResultException {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (donationBatch.getDonations() == null || donationBatch.getDonations().size() == 0) {
      // can delete a donation batch if there are no donations in it
      return true;
    }

    return false;
  }

  public boolean canEditDonationBatch(Long donationBatchId) throws NoResultException {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (donationBatch.getIsClosed() || donationBatch.getIsDeleted()) {
      // can't edit a donation batch if it is closed
      return false;
    }

    return true;
  }

  public boolean canEditDonationBatchDate(Long donationBatchId) throws NoResultException {

    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (donationBatch.getDonations() != null && !donationBatch.getDonations().isEmpty()) {
      // can't edit a donation batch's date if there are donations in it
      return false;
    }

    return true;
  }

  public boolean canCloseDonationBatch(Long donationBatchId) {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (donationBatch.getIsClosed()) {
      // can't close if it's already closed
      return false;
    }

    if (donationBatch.getDonations() == null || donationBatch.getDonations().size() == 0) {
      // if there are no donations then it shouldn't be closed it should be deleted
      return false;
    }

    return true;
  }

  public boolean canReopenDonationBatch(Long donationBatchId) {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (!donationBatch.getIsClosed()) {
      // can't reopen if it's not closed already
      return false;
    }

    TestBatch testBatch = donationBatch.getTestBatch();
    if (testBatch != null) {
      // can't re-open a donation batch if it has a test batch
      return false;
    }

    return true;
  }
}
