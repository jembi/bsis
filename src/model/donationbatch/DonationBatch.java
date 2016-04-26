package model.donationbatch;

import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import constraintvalidator.LocationExists;
import model.BaseModificationTrackerEntity;
import model.componentbatch.ComponentBatch;
import model.donation.Donation;
import model.location.Location;
import model.testbatch.TestBatch;
import repository.DonationBatchQueryConstants;

@NamedQueries({
    @NamedQuery(name = DonationBatchQueryConstants.NAME_COUNT_DONATION_BATCHES,
        query = DonationBatchQueryConstants.QUERY_COUNT_DONATION_BATCHES),
    @NamedQuery(name = DonationBatchQueryConstants.NAME_VERIFY_DONATION_BATCH_WITH_ID_EXISTS,
        query = DonationBatchQueryConstants.QUERY_VERIFY_DONATION_BATCH_WITH_ID_EXISTS)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DonationBatch extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @Column(length = 20, unique = true)
  private String batchNumber;

  @SuppressWarnings("unchecked")
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "donationBatch", fetch = FetchType.EAGER)
  @Where(clause = "isDeleted = 0")
  private List<Donation> donations = Collections.EMPTY_LIST;

  @OneToOne
  @LocationExists
  @NotNull
  private Location venue;


  @ManyToOne
  private TestBatch testBatch;

  @OneToOne(fetch = FetchType.LAZY)
  private ComponentBatch componentBatch;

  private boolean isDeleted;
  private boolean isClosed;

  @Lob
  private String notes;

  @Column(nullable = false)
  private boolean backEntry;

  public DonationBatch() {
    super();
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

  public void copy(DonationBatch donationBatch) {
    this.setNotes(donationBatch.getNotes());
    this.venue = donationBatch.getVenue();
  }

  public boolean isBackEntry() {
    return backEntry;
  }

  public void setBackEntry(boolean backEntry) {
    this.backEntry = backEntry;
  }

  public ComponentBatch getComponentBatch() {
    return componentBatch;
  }

  public void setComponentBatch(ComponentBatch componentBatch) {
    this.componentBatch = componentBatch;
  }



}
