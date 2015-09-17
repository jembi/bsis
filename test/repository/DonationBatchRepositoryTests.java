package repository;

import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
@Transactional
public class DonationBatchRepositoryTests {
    
    @Autowired
    private DonationBatchRepository donationBatchRepository;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void testCountOpenDonationBatchesWithNoDonationBatches_shouldReturnZero() {
        int returnedCount = donationBatchRepository.countOpenDonationBatches();
        assertThat(returnedCount, is(0));
    }

    @Test
    public void testCountOpenDonationBatches_shouldCountOpenAndNotDeletedBatches() {
        // Expected donation batches
        aDonationBatch().buildAndPersist(entityManager);
        aDonationBatch().buildAndPersist(entityManager);
        
        // Excluded by closed flag
        aDonationBatch().thatIsClosed().buildAndPersist(entityManager);
        
        // Excluded by deleted flag
        aDonationBatch().thatIsDeleted().buildAndPersist(entityManager);
        
        int returnedCount = donationBatchRepository.countOpenDonationBatches();

        assertThat(returnedCount, is(2));
    }

}
