package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.counselling.PostDonationCounselling;

public class PostDonationCounsellingPersister extends AbstractEntityPersister<PostDonationCounselling> {

  @Override
  public PostDonationCounselling deepPersist(PostDonationCounselling postDonationCounselling, EntityManager entityManager) {

    if (postDonationCounselling.getDonation() != null) {
      aDonationPersister().deepPersist(postDonationCounselling.getDonation(), entityManager);
    }

    return persist(postDonationCounselling, entityManager);
  }

}
