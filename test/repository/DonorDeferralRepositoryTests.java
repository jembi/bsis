package repository;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static helpers.builders.DonorBuilder.aDonor;
import static helpers.builders.DonorDeferralBuilder.aDonorDeferral;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.donor.Donor;
import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;
import model.donordeferral.DurationType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import suites.ContextDependentTestSuite;

public class DonorDeferralRepositoryTests extends ContextDependentTestSuite {
    
    @Autowired
    private DonorDeferralRepository donorDeferralRepository;
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void testCountDonorDeferralsForDonor_shouldReturnCorrectCount() {
        
        Donor donor = aDonor().build();
        
        // Expected
        aDonorDeferral().withDeferredDonor(donor).buildAndPersist(entityManager);
        // Excluded because voided
        aDonorDeferral().thatIsVoided().withDeferredDonor(donor).buildAndPersist(entityManager);
        // Excluded by donor
        aDonorDeferral().withDeferredDonor(aDonor().build()).buildAndPersist(entityManager);
        
        int returnedCount = donorDeferralRepository.countDonorDeferralsForDonor(donor);
        
        assertThat(returnedCount, is(1));
    }
    
    @Test
    public void testCountCurrentDonorDeferralsForDonor_shouldReturnCorrectCount() {
        
        Date pastDate = new DateTime().minusDays(3).toDate();
        Date currentDate = new Date();
        Date futureDate = new DateTime().plusDays(3).toDate();
        
        Donor donor = aDonor().build();
        DeferralReason temporaryDeferralReason = aDeferralReason()
                .withType(DeferralReasonType.NORMAL)
                .withDurationType(DurationType.TEMPORARY)
                .build();
        DeferralReason permanentDeferralReason = aDeferralReason()
                .withType(DeferralReasonType.AUTOMATED_TTI_UNSAFE)
                .withDurationType(DurationType.PERMANENT)
                .build();
        
        // Expected
        aDonorDeferral()
                .withDeferralReason(temporaryDeferralReason)
                .withDeferredUntil(futureDate)
                .withDeferredDonor(donor)
                .buildAndPersist(entityManager);
        // Expected
        aDonorDeferral()
                .withDeferralReason(temporaryDeferralReason)
                .withDeferredUntil(currentDate) // edge case: deferral ending today
                .withDeferredDonor(donor)
                .buildAndPersist(entityManager);
        // Expected
        aDonorDeferral()
                .withDeferralReason(permanentDeferralReason)
                .withDeferredDonor(donor)
                .buildAndPersist(entityManager);
        // Excluded by past deferred until date
        aDonorDeferral()
                .withDeferralReason(temporaryDeferralReason)
                .withDeferredUntil(pastDate)
                .withDeferredDonor(donor)
                .buildAndPersist(entityManager);
        // Excluded because voided
        aDonorDeferral()
                .thatIsVoided()
                .withDeferralReason(temporaryDeferralReason)
                .withDeferredUntil(futureDate)
                .withDeferredDonor(donor)
                .buildAndPersist(entityManager);
        // Excluded because voided
        aDonorDeferral()
                .thatIsVoided()
                .withDeferralReason(permanentDeferralReason)
                .withDeferredDonor(donor)
                .buildAndPersist(entityManager);
        // Excluded by donor
        aDonorDeferral()
                .withDeferralReason(temporaryDeferralReason)
                .withDeferredUntil(futureDate)
                .withDeferredDonor(aDonor().build())
                .buildAndPersist(entityManager);
        
        int returnedCount = donorDeferralRepository.countCurrentDonorDeferralsForDonor(donor);
        
        assertThat(returnedCount, is(2));
    }

}
