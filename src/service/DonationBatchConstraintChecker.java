package service;

import javax.persistence.NoResultException;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import repository.DonationBatchRepository;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class DonationBatchConstraintChecker {
    
    @Autowired
    private DonationBatchRepository donationBatchRepository;
 
    
    public boolean canDeleteDonationBatch(int donationBatchId) throws NoResultException {

        DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
        
        // check if any donations have been added
        if (donationBatch.getDonations() == null || donationBatch.getDonations().size() == 0) {
        	return true;
        }
        
        return false;
    }
    
    public boolean canCloseDonationBatch(int donationBatchId) {
        DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
        
        if (donationBatch.getIsClosed()) {
        	// can't close if it's already closed
        	return false;
        }
        
        // check if any donations have been added
        if (donationBatch.getDonations() == null || donationBatch.getDonations().size() == 0) {
        	return false;
        }
        
        return true;
    }
    
    public boolean canReopenDonationBatch(int donationBatchId) {
        DonationBatch donationBatch = donationBatchRepository.findDonationBatchById(donationBatchId);
        
        if (!donationBatch.getIsClosed()) {
        	// can't reopen if it's not closed already
        	return false;
        }

        // check if the test batch has been assigned
        TestBatch testBatch = donationBatch.getTestBatch();
        if (testBatch == null) {
        	return false;
        }
        
        return true;
    }
}
