package helpers.persisters;

import model.counselling.PostDonationCounselling;

import javax.persistence.EntityManager;

import static helpers.persisters.EntityPersisterFactory.aDonationPersister;

public class PostDonationCounsellingPersister extends AbstractEntityPersister<PostDonationCounselling> {

  @Override
  public PostDonationCounselling deepPersist(PostDonationCounselling postDonationCounselling, EntityManager entityManager) {

    if (postDonationCounselling.getDonation() != null) {
      aDonationPersister().deepPersist(postDonationCounselling.getDonation(), entityManager);
    }

    return persist(postDonationCounselling, entityManager);
  }

}
