package service;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import repository.DonationBatchRepository;

import javax.persistence.NoResultException;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonationBatchConstraintChecker {

  @Autowired
  private DonationBatchRepository donationBatchRepository;


  public boolean canDeleteDonationBatch(int donationBatchId) throws NoResultException {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    return donationBatch.getDonations() == null || donationBatch.getDonations().size() == 0;

  }

  public boolean canEditDonationBatch(int donationBatchId) throws NoResultException {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    return !(donationBatch.getIsClosed() || donationBatch.getIsDeleted());

  }

  public boolean canEditDonationBatchDate(int donationBatchId) throws NoResultException {

    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    return !(donationBatch.getDonations() != null && !donationBatch.getDonations().isEmpty());

  }

  public boolean canCloseDonationBatch(int donationBatchId) {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (donationBatch.getIsClosed()) {
      // can't close if it's already closed
      return false;
    }

    return !(donationBatch.getDonations() == null || donationBatch.getDonations().size() == 0);

  }

  public boolean canReopenDonationBatch(int donationBatchId) {
    DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);

    if (!donationBatch.getIsClosed()) {
      // can't reopen if it's not closed already
      return false;
    }

    TestBatch testBatch = donationBatch.getTestBatch();
    return testBatch == null;

  }
}
