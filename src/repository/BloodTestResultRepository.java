package repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional(readOnly = true)
@Repository
public class BloodTestResultRepository {

  @PersistenceContext
  private EntityManager entityManager;

  // TODO: Test
  public int countBloodTestResultsForDonation(long donationId) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION,
        Number.class)
        .setParameter("donationId", donationId)
        .getSingleResult()
        .intValue();
  }

}
