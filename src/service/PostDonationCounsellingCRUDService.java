package service;

import controller.UtilController;
import model.counselling.CounsellingStatus;
import model.counselling.PostDonationCounselling;
import model.donation.Donation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.PostDonationCounsellingRepository;

import java.util.Date;

@Service
@Transactional

public class PostDonationCounsellingCRUDService {

  private static final Logger LOGGER = Logger.getLogger(PostDonationCounsellingCRUDService.class);

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  @Autowired
  private UtilController utilController;

  @Autowired
  private DateGeneratorService dateGeneratorService;

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
    postDonationCounselling.setCreatedBy(utilController.getCurrentUser());
    postDonationCounselling.setCreatedDate(dateGeneratorService.generateDate());
    postDonationCounselling.setLastUpdated(dateGeneratorService.generateDate());
    postDonationCounselling.setLastUpdatedBy(utilController.getCurrentUser());
    postDonationCounsellingRepository.save(postDonationCounselling);
    return postDonationCounselling;
  }

  public PostDonationCounselling updatePostDonationCounselling(long id, CounsellingStatus counsellingStatus,
                                                               Date counsellingDate, String notes) {

    PostDonationCounselling postDonationCounselling = postDonationCounsellingRepository.findById(id);

    if (postDonationCounselling == null) {
      throw new IllegalArgumentException("Post donation counselling not found for id: " + id);
    }

    postDonationCounselling.setFlaggedForCounselling(Boolean.FALSE);
    postDonationCounselling.setCounsellingStatus(counsellingStatus);
    postDonationCounselling.setCounsellingDate(counsellingDate);
    postDonationCounselling.getDonation().setNotes(notes);
    postDonationCounselling.setLastUpdated(dateGeneratorService.generateDate());
    postDonationCounselling.setLastUpdatedBy(utilController.getCurrentUser());
    postDonationCounselling.setIsDeleted(Boolean.FALSE);
    return postDonationCounsellingRepository.update(postDonationCounselling);
  }

  public PostDonationCounselling flagForCounselling(long id) {
    PostDonationCounselling postDonationCounselling = postDonationCounsellingRepository.findById(id);
    postDonationCounselling.setFlaggedForCounselling(Boolean.TRUE);
    postDonationCounselling.setCounsellingDate(null);
    postDonationCounselling.setCounsellingStatus(null);
    postDonationCounselling.getDonation().setNotes(null);
    postDonationCounselling.setIsDeleted(Boolean.FALSE);
    postDonationCounselling.setLastUpdated(dateGeneratorService.generateDate());
    postDonationCounselling.setLastUpdatedBy(utilController.getCurrentUser());
    return postDonationCounsellingRepository.update(postDonationCounselling);
  }
}
