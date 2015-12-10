package model.counselling;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import constraintvalidator.DonationExists;
import model.donation.Donation;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;
import org.hibernate.envers.Audited;
import repository.PostDonationCounsellingNamedQueryConstants;

import javax.persistence.*;
import java.util.Date;

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
public class PostDonationCounselling implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

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

  @Embedded
  private RowModificationTracker modificationTracker;

  public PostDonationCounselling() {
    modificationTracker = new RowModificationTracker();
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @Override
  public boolean equals(Object other) {
    return other == this || other instanceof PostDonationCounselling && ((PostDonationCounselling) other).id == id;
  }

  @Override
  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  @Override
  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  @Override
  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  @Override
  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  @Override
  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  @Override
  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }


}
