package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.PostDonationCounsellingExportDTO;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.springframework.stereotype.Repository;

@Repository
public class PostDonationCounsellingRepository extends AbstractRepository<PostDonationCounselling> {

  public PostDonationCounselling findById(UUID id) {
    return entityManager.find(PostDonationCounselling.class, id);
  }

  public List<PostDonationCounselling> findPostDonationCounselling(Date startDate, Date endDate, Set<UUID> venueIds,
      CounsellingStatus counsellingStatus, Boolean referred, Boolean notReferred, boolean flaggedForCounselling) {
    
    String counsellingStatusName = counsellingStatus != null ? counsellingStatus.name() : null;
    boolean venuesHasItems = (venueIds == null || venueIds.isEmpty()) ? false : true;

    return entityManager.createNamedQuery(
        PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING,
        PostDonationCounselling.class)
        .setParameter("isDeleted", false)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("venueIds", venueIds)
        .setParameter("venuesHasItems", venuesHasItems)
        .setParameter("counsellingStatus", counsellingStatusName)
        .setParameter("referred1", referred)
        .setParameter("referred2", (notReferred == null ? null : !notReferred))
        .setParameter("includeReferred", (referred != null && notReferred != null))
        .setParameter("flaggedForCounselling", flaggedForCounselling)
        .getResultList();
  }

  public PostDonationCounselling findPostDonationCounsellingForDonor(UUID donorId) throws NoResultException {

    return entityManager.createNamedQuery(
        PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONOR,
        PostDonationCounselling.class)
        .setParameter("donorId", donorId)
        .setParameter("isDeleted", false)
        .setMaxResults(1)
        .getSingleResult();
  }

  public int countFlaggedPostDonationCounsellingsForDonor(UUID donorId) {

    return entityManager.createNamedQuery(
        PostDonationCounsellingNamedQueryConstants.NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR,
        Number.class)
        .setParameter("donorId", donorId)
        .setParameter("flaggedForCounselling", true)
        .setParameter("isDeleted", false)
        .getSingleResult()
        .intValue();
  }

  public int countNotFlaggedPostDonationCounsellingsForDonor(UUID donorId) {

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

  public List<PostDonationCounsellingExportDTO> findPostDonationCounsellingsForExport() {
    return entityManager.createNamedQuery(
        PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLINGS_FOR_EXPORT,
        PostDonationCounsellingExportDTO.class)
        .setParameter("deleted", false)
        .getResultList();
  }

}
