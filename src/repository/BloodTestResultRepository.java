package repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public class BloodTestResultRepository {
    
    public int countBloodTestResultsForDonation(long donationId) {
        // TODO
        return 0;
    }

}
