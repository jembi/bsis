package model.counselling;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import model.BaseModificationTrackerEntity;
import model.donation.Donation;

import org.hibernate.envers.Audited;

import repository.PostDonationCounsellingNamedQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import constraintvalidator.DonationExists;

@NamedQueries({
    @NamedQuery(name = PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONOR,
        query = PostDonationCounsellingNamedQueryConstants.QUERY_FIND_POST_DONATION_COUNSELLING_FOR_DONOR),
    @NamedQuery(name = PostDonationCounsellingNamedQueryConstants.NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR,
        query = PostDonationCounsellingNamedQueryConstants.QUERY_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR),
    @NamedQuery(name = PostDonationCounsellingNamedQueryConstants.NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONATION,
        query = PostDonationCounsellingNamedQueryConstants.QUERY_FIND_POST_DONATION_COUNSELLING_FOR_DONATION)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class PostDonationCounselling extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @DonationExists
  @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
  private Donation donation;

  @Column(nullable = false)
  private boolean flaggedForCounselling;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = true)
  private CounsellingStatus counsellingStatus;

  @Column(nullable = true)
  private Date counsellingDate;

  @Column(nullable = false)
  private boolean isDeleted;

  public PostDonationCounselling() {
    super();
  }

  public Donation getDonation() {
    return donation;
  }

  public void setDonation(Donation donation) {
    this.donation = donation;
  }

  public boolean isFlaggedForCounselling() {
    return flaggedForCounselling;
  }

  public void setFlaggedForCounselling(boolean flaggedForCounselling) {
    this.flaggedForCounselling = flaggedForCounselling;
  }

  public CounsellingStatus getCounsellingStatus() {
    return counsellingStatus;
  }

  public void setCounsellingStatus(CounsellingStatus counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
  }

  public Date getCounsellingDate() {
    return counsellingDate;
  }

  public void setCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
  }

  public boolean isIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
