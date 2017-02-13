package org.jembi.bsis.service;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostDonationCounsellingCRUDService {

  private static final Logger LOGGER = Logger.getLogger(PostDonationCounsellingCRUDService.class);

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  public void setPostDonationCounsellingRepository(PostDonationCounsellingRepository postDonationCounsellingRepository) {
    this.postDonationCounsellingRepository = postDonationCounsellingRepository;
  }

  public PostDonationCounselling createPostDonationCounsellingForDonation(Donation donation) {
    LOGGER.info("Creating post donation counselling for donation: " + donation);

    PostDonationCounselling existingCounselling = postDonationCounsellingRepository.findPostDonationCounsellingForDonation(
        donation);
    if (existingCounselling != null) {
      // Return existing counselling instead of creating a new PostDonationCounselling
      return existingCounselling;
    }

    PostDonationCounselling postDonationCounselling = new PostDonationCounselling();
    postDonationCounselling.setDonation(donation);
    postDonationCounselling.setFlaggedForCounselling(Boolean.TRUE);
    postDonationCounselling.setIsDeleted(Boolean.FALSE);
    postDonationCounsellingRepository.save(postDonationCounselling);
    return postDonationCounselling;
  }

  public PostDonationCounselling updatePostDonationCounselling(PostDonationCounselling postDonationCounselling) {

    PostDonationCounselling existingPostDonationCounselling = postDonationCounsellingRepository
        .findById(postDonationCounselling.getId());

    if (existingPostDonationCounselling == null) {
      throw new IllegalArgumentException("Post donation counselling not found for id: " + postDonationCounselling.getId());
    }

    existingPostDonationCounselling.setFlaggedForCounselling(postDonationCounselling.isFlaggedForCounselling());
    existingPostDonationCounselling.setCounsellingStatus(postDonationCounselling.getCounsellingStatus());
    existingPostDonationCounselling.setCounsellingDate(postDonationCounselling.getCounsellingDate());
    existingPostDonationCounselling.setNotes(postDonationCounselling.getNotes());
    existingPostDonationCounselling.setReferred(postDonationCounselling.getReferred());
    existingPostDonationCounselling.setReferralSite(postDonationCounselling.getReferralSite());

    return postDonationCounsellingRepository.update(existingPostDonationCounselling);
  }

}
