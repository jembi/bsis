package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DeferralReasonBuilder.aDeferralReason;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DeferralReasonType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
  
  @Test
  public void testGetAllDeferralReasons_shouldAllNotDeletedEntities() {
    // Excluded by deleted flag
    aDeferralReason()
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    DeferralReason notDeletedDeferralReason = aDeferralReason()
        .thatIsNotDeleted()
        .buildAndPersist(entityManager);

    DeferralReason expectedDeferralReason = aDeferralReason()
        .withId(notDeletedDeferralReason.getId())
        .build();

    List<DeferralReason> returnedDeferralReasons = deferralReasonRepository.getAllDeferralReasons();

    assertThat(returnedDeferralReasons.size(), is(1));
    assertThat(returnedDeferralReasons.get(0), is(expectedDeferralReason));
  }
  
  @Test
  public void testVerifyDeferralReasonExistsWithNoDeferralReason_shouldReturnFalse() {
    boolean result = deferralReasonRepository.verifyDeferralReasonExists(UUID.randomUUID());
    
    assertThat(result, is(false));
  }
  
  @Test
  public void testVerifyDeferralReasonExistsWithExistingDeferralReason_shouldReturnTrue() {
    DeferralReason existingDeferralReason = aDeferralReason().thatIsNotDeleted().buildAndPersist(entityManager);
    
    boolean result = deferralReasonRepository.verifyDeferralReasonExists(existingDeferralReason.getId());
    
    assertThat(result, is(true));
  }
  
  @Test
  public void testVerifyDeferralReasonExistsWithDeletedDeferralReason_shouldReturnFalse() {
    DeferralReason deletedDeferralReason = aDeferralReason().thatIsDeleted().buildAndPersist(entityManager);
    
    boolean result = deferralReasonRepository.verifyDeferralReasonExists(deletedDeferralReason.getId());
    
    assertThat(result, is(false));
  }

}
