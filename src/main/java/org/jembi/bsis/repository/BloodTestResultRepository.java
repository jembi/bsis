package org.jembi.bsis.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.Donation;
import org.springframework.stereotype.Repository;

@Repository
public class BloodTestResultRepository extends AbstractRepository<BloodTestResult> {

  @PersistenceContext
  private EntityManager entityManager;

  // TODO: Test
  public int countBloodTestResultsForDonation(long donationId) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION,
        Number.class)
        .setParameter("donationId", donationId)
        .setParameter("testOutcomeDeleted", false)
        .getSingleResult()
        .intValue();
  }

  public List<BloodTestResult> getTestOutcomes(Donation donation) {
    return entityManager.createNamedQuery(
        BloodTestResultNamedQueryConstants.NAME_GET_TEST_OUTCOMES_FOR_DONATION, BloodTestResult.class)
        .setParameter("donation", donation)
        .setParameter("testOutcomeDeleted", false)
        .getResultList();
  }

}
