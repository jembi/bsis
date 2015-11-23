package helpers.builders;

import static helpers.builders.DonationBuilder.aDonation;

import java.util.Date;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.PostDonationCounsellingPersister;
import model.counselling.CounsellingStatus;
import model.counselling.PostDonationCounselling;
import model.donation.Donation;

public class PostDonationCounsellingBuilder extends AbstractEntityBuilder<PostDonationCounselling> {
    
    private Long id;
    private Donation donation = aDonation().build();
    private boolean flaggedForCounselling;
    private CounsellingStatus counsellingStatus;
    private boolean isDeleted;
    private Date counsellingDate;
    
    public PostDonationCounsellingBuilder withId(Long id) {
        this.id = id;
        return this;
    }
    
    public PostDonationCounsellingBuilder withDonation(Donation donation) {
        this.donation = donation;
        return this;
    }

    public PostDonationCounsellingBuilder thatIsFlaggedForCounselling() {
        flaggedForCounselling = true;
        return this;
    }

    public PostDonationCounsellingBuilder thatIsNotFlaggedForCounselling() {
        flaggedForCounselling = false;
        return this;
    }

    public PostDonationCounsellingBuilder thatIsDeleted() {
        isDeleted = true;
        return this;
    }

    public PostDonationCounsellingBuilder thatIsNotDeleted() {
        isDeleted = false;
        return this;
    }


    
    public PostDonationCounsellingBuilder withCounsellingStatus(CounsellingStatus counsellingStatus) {
        this.counsellingStatus = counsellingStatus;
        return this;
    }
    
    public PostDonationCounsellingBuilder withCounsellingDate(Date counsellingDate) {
        this.counsellingDate = counsellingDate;
        return this;
    }

    @Override
    public PostDonationCounselling build() {
        PostDonationCounselling postDonationCounselling = new PostDonationCounselling();
        postDonationCounselling.setId(id);
        postDonationCounselling.setDonation(donation);
        postDonationCounselling.setFlaggedForCounselling(flaggedForCounselling);
        postDonationCounselling.setIsDeleted(isDeleted);
        postDonationCounselling.setCounsellingStatus(counsellingStatus);
        postDonationCounselling.setCounsellingDate(counsellingDate);
        return postDonationCounselling;
    }
    
    @Override
    public AbstractEntityPersister<PostDonationCounselling> getPersister() {
        return new PostDonationCounsellingPersister();
    }

    public static PostDonationCounsellingBuilder aPostDonationCounselling() {
        return new PostDonationCounsellingBuilder();
    }

}
