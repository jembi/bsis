package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aDonationPersister;

import javax.persistence.EntityManager;

import model.counselling.PostDonationCounselling;

public class PostDonationCounsellingPersister extends AbstractEntityPersister<PostDonationCounselling> {

  @Override
  public PostDonationCounselling deepPersist(PostDonationCounselling postDonationCounselling, EntityManager entityManager) {

    if (postDonationCounselling.getDonation() != null) {
      aDonationPersister().deepPersist(postDonationCounselling.getDonation(), entityManager);
    }

    return persist(postDonationCounselling, entityManager);
  }

}
