package model.donationbatch;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import constraintvalidator.LocationExists;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import model.donation.Donation;
import model.location.Location;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.testbatch.TestBatch;
import model.user.User;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import repository.DonationBatchQueryConstants;

@NamedQueries({
    @NamedQuery(name = DonationBatchQueryConstants.NAME_COUNT_DONATION_BATCHES,
            query = DonationBatchQueryConstants.QUERY_COUNT_DONATION_BATCHES)
})
@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class DonationBatch implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=20, unique=true)
  private String batchNumber;

  @SuppressWarnings("unchecked")
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="donationBatch", fetch = FetchType.EAGER)
  @Where(clause = "isDeleted = 0")
  private List<Donation> donations = Collections.EMPTY_LIST;
  
  @OneToOne
  @LocationExists
  @NotNull
  private Location venue;
  
  
  @ManyToOne
  private TestBatch testBatch;

  private boolean isDeleted;
  private boolean isClosed;

  @Lob
  private String notes;
  @Embedded
  private RowModificationTracker modificationTracker;
  
    @Column(nullable = false)
    private boolean backEntry;

  public DonationBatch() {
    modificationTracker = new RowModificationTracker();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBatchNumber() {
    return batchNumber;
  }

  public void setBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public List<Donation> getDonations() {
    return donations;
  }

  public void setDonation(List<Donation> donations) {
    this.donations = donations;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
  
  public boolean getIsClosed() {
    return isClosed;
  }

  public void setIsClosed(boolean isClosed) {
    this.isClosed = isClosed;
  }

  
  public TestBatch getTestBatch() {
     return testBatch;
   }

  public void setTestBatch(TestBatch testBatch) {
     this.testBatch = testBatch;
   }

    public Location getVenue() {
        return venue;
    }

    public void setVenue(Location venue) {
        this.venue = venue;
    }
  
  @Override
  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  @Override
  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  @Override
  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  @Override
  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  @Override
  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  @Override
  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  @Override
  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }
  
  public void copy(DonationBatch donationBatch){
      this.setNotes(donationBatch.getNotes());
      this.venue = donationBatch.getVenue();
  }

    public boolean isBackEntry() {
        return backEntry;
    }

    public void setBackEntry(boolean backEntry) {
        this.backEntry = backEntry;
    }

}
