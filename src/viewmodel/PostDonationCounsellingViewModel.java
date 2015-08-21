package viewmodel;

import java.util.Date;

import model.counselling.PostDonationCounselling;
import utils.JsonDateSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PostDonationCounsellingViewModel {
    
    private PostDonationCounselling postDonationCounselling;
    
    public PostDonationCounsellingViewModel(PostDonationCounselling postDonationCounselling) {
        this.postDonationCounselling = postDonationCounselling;
    }
    
    public long getId() {
        return postDonationCounselling.getId();
    }
    
    public DonationViewModel getDonation() {
        if (postDonationCounselling.getDonation() == null) {
            return null;
        }
        return new DonationViewModel(postDonationCounselling.getDonation());
    }
    
    public boolean isFlaggedForCounselling() {
        return postDonationCounselling.isFlaggedForCounselling();
    }
    
    @JsonSerialize(using = JsonDateSerialiser.class)
    public Date getCounsellingDate() {
        return postDonationCounselling.getCounsellingDate();
    }
    
    public CounsellingStatusViewModel getCounsellingStatus() {
        if (postDonationCounselling.getCounsellingStatus() == null) {
            return null;
        }
        return new CounsellingStatusViewModel(postDonationCounselling.getCounsellingStatus());
    }

}
