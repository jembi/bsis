package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aBloodTestPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aUserPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;

public class BloodTestResultPersister extends AbstractEntityPersister<BloodTestResult> {

  @Override
  public BloodTestResult deepPersist(BloodTestResult bloodTestResult, EntityManager entityManager) {
    
    if (bloodTestResult.getDonation() != null) {
      aDonationPersister().deepPersist(bloodTestResult.getDonation(), entityManager);
    }

    if (bloodTestResult.getBloodTest() != null) {
      aBloodTestPersister().deepPersist(bloodTestResult.getBloodTest(), entityManager);
    }

    if (bloodTestResult.getCreatedBy() != null) {
      aUserPersister().deepPersist(bloodTestResult.getCreatedBy(), entityManager);
    }
    
    return persist(bloodTestResult, entityManager);
  }
}
