package repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import suites.ContextDependentTestSuite;

public class DonationRepositoryTests extends ContextDependentTestSuite {
    
    @Autowired
    private DonationRepository donationRepository;
    
    @Test
    public void testFindCollectedDonationsReportIndicators() {
    }

}
