package viewmodel;

import java.util.Date;

import model.counselling.PostDonationCounselling;
import repository.PostDonationCounsellingRepository;
import utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PostDonationCounsellingViewModel {

    private PostDonationCounsellingRepository postDonationCounsellingRepository;
    
    private PostDonationCounselling postDonationCounselling;
    
    public PostDonationCounsellingViewModel(PostDonationCounselling postDonationCounselling, PostDonationCounsellingRepository postDonationCounsellingRepository) {
        this.postDonationCounselling = postDonationCounselling;
        this.postDonationCounsellingRepository = postDonationCounsellingRepository;
    }
    
    public long getId() {
        return postDonationCounselling.getId();
    }

    public boolean getHasCounselling() {
        return postDonationCounsellingRepository
                .countNotFlaggedPostDonationCounsellingsForDonor(
                        postDonationCounselling.getDonation().getDonor().getId()) > 0;
    }

    
    public boolean isFlaggedForCounselling() {
        return postDonationCounselling.isFlaggedForCounselling();
    }
    
    @JsonSerialize(using = DateTimeSerialiser.class)
    public Date getCounsellingDate() {
        return postDonationCounselling.getCounsellingDate();
    }
    
    public CounsellingStatusViewModel getCounsellingStatus() {
        if (postDonationCounselling.getCounsellingStatus() == null) {
            return null;
        }
        return new CounsellingStatusViewModel(postDonationCounselling.getCounsellingStatus());
    }
    
    public String getNotes() {
        return postDonationCounselling.getDonation().getNotes();
    }
    
    public DonationViewModel getDonation() {
        return new DonationViewModel(postDonationCounselling.getDonation());
    }
    
    public DonorViewModel getDonor() {
        return new DonorViewModel(postDonationCounselling.getDonation().getDonor());
    }

}
