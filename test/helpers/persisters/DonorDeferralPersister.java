package helpers.persisters;

import model.donordeferral.DonorDeferral;

import javax.persistence.EntityManager;

import static helpers.persisters.EntityPersisterFactory.aDeferralReasonPersister;
import static helpers.persisters.EntityPersisterFactory.aDonorPersister;

public class DonorDeferralPersister extends AbstractEntityPersister<DonorDeferral> {

  @Override
  public DonorDeferral deepPersist(DonorDeferral donorDeferral, EntityManager entityManager) {
    aDonorPersister().deepPersist(donorDeferral.getDeferredDonor(), entityManager);
    if (donorDeferral.getDeferralReason() != null) {
      aDeferralReasonPersister().deepPersist(donorDeferral.getDeferralReason(), entityManager);
    }
    return persist(donorDeferral, entityManager);
  }

}
