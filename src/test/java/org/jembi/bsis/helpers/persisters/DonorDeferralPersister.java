package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDeferralReasonPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonorPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.donordeferral.DonorDeferral;

public class DonorDeferralPersister extends AbstractEntityPersister<DonorDeferral> {

  @Override
  public DonorDeferral deepPersist(DonorDeferral donorDeferral, EntityManager entityManager) {
    if (donorDeferral.getDeferredDonor() != null) {
      aDonorPersister().deepPersist(donorDeferral.getDeferredDonor(), entityManager);
    }
    if (donorDeferral.getDeferralReason() != null) {
      aDeferralReasonPersister().deepPersist(donorDeferral.getDeferralReason(), entityManager);
    }
    if (donorDeferral.getVenue() != null) {
      aLocationPersister().deepPersist(donorDeferral.getVenue(), entityManager);
    }
    return persist(donorDeferral, entityManager);
  }

}
