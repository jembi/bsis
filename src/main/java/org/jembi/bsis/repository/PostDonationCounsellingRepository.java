package org.jembi.bsis.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.jembi.bsis.dto.PostDonationCounsellingExportDTO;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.springframework.stereotype.Repository;

@Repository
public class PostDonationCounsellingRepository extends AbstractRepository<PostDonationCounselling> {

  public PostDonationCounselling findById(long id) {
    return entityManager.find(PostDonationCounselling.class, id);
  }

  public List<PostDonationCounselling> findPostDonationCounselling(Date startDate, Date endDate, Set<Long> venueIds, 
      CounsellingStatus counsellingStatus, Boolean referred, boolean flaggedForCounselling) {
    
    String counsellingStatusName = counsellingStatus != null ? counsellingStatus.name() : null;
    
    return entityManager.createNamedQuery(
        PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING,
        PostDonationCounselling.class)
        .setParameter("isDeleted", false)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("venueIds", venueIds)
        .setParameter("counsellingStatus", counsellingStatusName)
        .setParameter("referred", referred)
        .setParameter("flaggedForCounselling", flaggedForCounselling)
        .getResultList();
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

  public List<PostDonationCounsellingExportDTO> findPostDonationCounsellingsForExport() {
    return entityManager.createNamedQuery(
        PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLINGS_FOR_EXPORT,
        PostDonationCounsellingExportDTO.class)
        .setParameter("deleted", false)
        .getResultList();
  }

}
