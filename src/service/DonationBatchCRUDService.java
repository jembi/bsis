package service;

import javax.persistence.NoResultException;

import model.donationbatch.DonationBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationBatchRepository;

@Transactional
@Service
public class DonationBatchCRUDService {
	
	@Autowired
	private DonationBatchRepository donationBatchRepository;
	
	@Autowired
	private DonationBatchConstraintChecker donationBatchConstraintChecker;
	
	public void deleteDonationBatch(int donationBatchId) throws IllegalStateException, NoResultException {
		
		if (!donationBatchConstraintChecker.canDeleteDonationBatch(donationBatchId)) {
			throw new IllegalStateException("Cannot delete donation batch with constraints");
		}
		
		DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
		donationBatch.setIsDeleted(true);
		
		donationBatchRepository.updateDonationBatch(donationBatch);
	}
	
	public DonationBatch updateDonationBatch(DonationBatch donationBatch) {
		DonationBatch existingDonationBatch = donationBatchRepository.findDonationBatchById(donationBatch.getId());
		existingDonationBatch.setIsClosed(donationBatch.getIsClosed());
		existingDonationBatch.setVenue(donationBatch.getVenue());
		existingDonationBatch.setCreatedDate(donationBatch.getCreatedDate());
		DonationBatch updatedDonationBatch = donationBatchRepository.updateDonationBatch(existingDonationBatch);
		return updatedDonationBatch;
	}
}
