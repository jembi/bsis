package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;

import java.util.Date;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.PostDonationCounsellingPersister;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.user.User;

public class PostDonationCounsellingBuilder extends AbstractEntityBuilder<PostDonationCounselling> {

  private Long id;
  private Donation donation = aDonation().build();
  private boolean flaggedForCounselling;
  private CounsellingStatus counsellingStatus;
  private boolean isDeleted;
  private Date counsellingDate;
  private Date createdDate;
  private User createdBy;
  private Date lastUpdated;
  private User lastUpdatedBy;


  public PostDonationCounsellingBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PostDonationCounsellingBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }

  public PostDonationCounsellingBuilder thatIsFlaggedForCounselling() {
    flaggedForCounselling = Boolean.TRUE;
    return this;
  }

  public PostDonationCounsellingBuilder thatIsNotFlaggedForCounselling() {
    flaggedForCounselling = Boolean.FALSE;
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

  public PostDonationCounsellingBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public PostDonationCounsellingBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public PostDonationCounsellingBuilder withLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public PostDonationCounsellingBuilder withLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
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
    postDonationCounselling.setCreatedDate(createdDate);
    postDonationCounselling.setCreatedBy(createdBy);
    postDonationCounselling.setLastUpdated(lastUpdated);
    postDonationCounselling.setLastUpdatedBy(lastUpdatedBy);
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
