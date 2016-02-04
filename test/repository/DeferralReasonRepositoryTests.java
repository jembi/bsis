package repository;

import static helpers.builders.DeferralReasonBuilder.aDeferralReason;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import model.donordeferral.DeferralReason;
import model.donordeferral.DeferralReasonType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.ContextDependentTestSuite;

public class DeferralReasonRepositoryTests extends ContextDependentTestSuite {

    @Autowired
    private DeferralReasonRepository deferralReasonRepository;
    
    @Test(expected = NoResultException.class)
    public void testFindDeferralReasonByTypeWithNoMatchingDeferralReasons_shouldThrow() {
        deferralReasonRepository.findDeferralReasonByType(DeferralReasonType.AUTOMATED_TTI_UNSAFE);
    }
    
    @Test(expected = NonUniqueResultException.class)
    public void testFindDeferralReasonByTypeWithMultipleMatchingDeferralReasons_shouldThrow() {
        DeferralReasonType type = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
        
        aDeferralReason()
                .thatIsNotDeleted()
                .withType(type)
                .buildAndPersist(entityManager);
        aDeferralReason()
                .thatIsNotDeleted()
                .withType(type)
                .buildAndPersist(entityManager);
        
        deferralReasonRepository.findDeferralReasonByType(type);
    }
    
    @Test
    public void testFindDeferralReasonByType_shouldReturnNonDeletedDeferralReasonsMatchingType() {
        DeferralReasonType type = DeferralReasonType.AUTOMATED_TTI_UNSAFE;
        
        // Excluded by deleted flag
        aDeferralReason()
                .thatIsDeleted()
                .withType(type)
                .buildAndPersist(entityManager);

        // Excluded by type
        aDeferralReason()
                .thatIsNotDeleted()
                .withType(DeferralReasonType.NORMAL)
                .buildAndPersist(entityManager);

        DeferralReason expectedDeferralReason = aDeferralReason()
                .thatIsNotDeleted()
                .withType(type)
                .buildAndPersist(entityManager);

        DeferralReason returnedDeferralReason = deferralReasonRepository.findDeferralReasonByType(type);
        
        assertThat(returnedDeferralReason, is(expectedDeferralReason));
    }

}
