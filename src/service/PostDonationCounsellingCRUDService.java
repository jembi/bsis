package service;

import java.util.Date;

import model.counselling.CounsellingStatus;
import model.counselling.PostDonationCounselling;
import model.donation.Donation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.PostDonationCounsellingRepository;

@Service
public class PostDonationCounsellingCRUDService {
    
    private static final Logger LOGGER = Logger.getLogger(PostDonationCounsellingCRUDService.class);
    
    @Autowired
    private PostDonationCounsellingRepository postDonationCounsellingRepository;
    
    public void setPostDonationCounsellingRepository(PostDonationCounsellingRepository postDonationCounsellingRepository) {
        this.postDonationCounsellingRepository = postDonationCounsellingRepository;
    }

    public PostDonationCounselling createPostDonationCounsellingForDonation(Donation donation) {
        LOGGER.info("Creating post donation counselling for donation: " + donation);
        
        PostDonationCounselling postDonationCounselling = new PostDonationCounselling();
        postDonationCounselling.setDonation(donation);
        postDonationCounselling.setFlaggedForCounselling(true);
        postDonationCounsellingRepository.save(postDonationCounselling);
        return postDonationCounselling;
    }
    
    public PostDonationCounselling updatePostDonationCounselling(long id, CounsellingStatus counsellingStatus,
            Date counsellingDate, String notes) {
        
        PostDonationCounselling postDonationCounselling = postDonationCounsellingRepository.findById(id);
        
        if (postDonationCounselling == null) {
            throw new IllegalArgumentException("Post donation counselling not found for id: " + id);
        }
        
        postDonationCounselling.setFlaggedForCounselling(false);
        postDonationCounselling.setCounsellingStatus(counsellingStatus);
        postDonationCounselling.setCounsellingDate(counsellingDate);
        postDonationCounselling.getDonation().setNotes(notes);
        
        return postDonationCounsellingRepository.update(postDonationCounselling);
    }

}
