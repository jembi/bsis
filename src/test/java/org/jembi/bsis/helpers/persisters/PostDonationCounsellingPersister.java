package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aUserPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.counselling.PostDonationCounselling;

public class PostDonationCounsellingPersister extends AbstractEntityPersister<PostDonationCounselling> {

  @Override
  public PostDonationCounselling deepPersist(PostDonationCounselling postDonationCounselling, EntityManager entityManager) {

    if (postDonationCounselling.getDonation() != null) {
      aDonationPersister().deepPersist(postDonationCounselling.getDonation(), entityManager);
    }
    
    if (postDonationCounselling.getCreatedBy() != null) {
      aUserPersister().deepPersist(postDonationCounselling.getCreatedBy(), entityManager);
    }

    return persist(postDonationCounselling, entityManager);
  }

}
