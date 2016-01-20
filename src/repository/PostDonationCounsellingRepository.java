package repository;

import model.counselling.PostDonationCounselling;
import model.donation.Donation;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.Map.Entry;

@Repository
public class PostDonationCounsellingRepository extends AbstractRepository<PostDonationCounselling> {

  public PostDonationCounselling findById(long id) {
    return entityManager.find(PostDonationCounselling.class, id);
  }

  public List<Donation> findDonationsFlaggedForCounselling(Date startDate, Date endDate, Set<Long> venueIds) {

    StringBuilder queryBuilder = new StringBuilder()
            .append("SELECT DISTINCT(pdc.donation) ")
            .append("FROM PostDonationCounselling pdc ")
            .append("WHERE pdc.flaggedForCounselling = :flaggedForCounselling ")
            .append("AND pdc.isDeleted = :isDeleted ");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("flaggedForCounselling", true);
    parameters.put("isDeleted", false);

    if (startDate != null) {
      queryBuilder.append("AND pdc.donation.donationDate >= :startDate ");
      parameters.put("startDate", startDate);
    }

    if (endDate != null) {
      queryBuilder.append("AND pdc.donation.donationDate <= :endDate ");
      parameters.put("endDate", endDate);
    }

    if (venueIds != null && !venueIds.isEmpty()) {
      queryBuilder.append("AND pdc.donation.venue.id IN :venueIds ");
      parameters.put("venueIds", venueIds);
    }

    TypedQuery<Donation> query = entityManager.createQuery(queryBuilder.toString(), Donation.class);
    for (Entry<String, Object> entry : parameters.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }
    return query.getResultList();
  }

  public PostDonationCounselling findPostDonationCounsellingForDonor(Long donorId) throws NoResultException {

    return entityManager.createNamedQuery(
            PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONOR,
            PostDonationCounselling.class)
            .setParameter("donorId", donorId)
            .setParameter("isDeleted", false)
            .setMaxResults(1)
            .getSingleResult();
  }

  public int countFlaggedPostDonationCounsellingsForDonor(Long donorId) {

    return entityManager.createNamedQuery(
            PostDonationCounsellingNamedQueryConstants.NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR,
            Number.class)
            .setParameter("donorId", donorId)
            .setParameter("flaggedForCounselling", true)
            .setParameter("isDeleted", false)
            .getSingleResult()
            .intValue();
  }

  public int countNotFlaggedPostDonationCounsellingsForDonor(Long donorId) {

    return entityManager.createNamedQuery(
            PostDonationCounsellingNamedQueryConstants.NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR,
            Number.class)
            .setParameter("donorId", donorId)
            .setParameter("flaggedForCounselling", false)
            .setParameter("isDeleted", false)
            .getSingleResult()
            .intValue();
  }

  public PostDonationCounselling findPostDonationCounsellingForDonation(Donation donation) {

    List<PostDonationCounselling> postDonationCounsellings = entityManager.createNamedQuery(
            PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONATION,
            PostDonationCounselling.class)
            .setParameter("donation", donation)
            .setParameter("isDeleted", false)
            .getResultList();

    return postDonationCounsellings.size() > 0 ? postDonationCounsellings.get(0) : null;
  }


}
