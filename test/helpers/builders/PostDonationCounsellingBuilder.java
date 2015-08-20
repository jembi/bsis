package helpers.builders;

import static helpers.builders.DonationBuilder.aDonation;
import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.PostDonationCounsellingPersister;
import model.counselling.PostDonationCounselling;
import model.donation.Donation;

public class PostDonationCounsellingBuilder extends AbstractEntityBuilder<PostDonationCounselling> {
    
    private Donation donation = aDonation().build();
    private boolean flaggedForCounselling;
    
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

    @Override
    public PostDonationCounselling build() {
        PostDonationCounselling postDonationCounselling = new PostDonationCounselling();
        postDonationCounselling.setDonation(donation);
        postDonationCounselling.setFlaggedForCounselling(flaggedForCounselling);
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
