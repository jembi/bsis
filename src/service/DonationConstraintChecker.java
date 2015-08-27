package service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class DonationConstraintChecker {
    
    public boolean canDeletedDonation(long donationId) {
        // TODO
        return false;
    }

}
