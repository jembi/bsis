package org.jembi.bsis.model.donationbatch;

import java.util.Collections;
import java.util.Date;
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
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.DonationBatchQueryConstants;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@NamedQueries({
    @NamedQuery(name = DonationBatchQueryConstants.NAME_COUNT_DONATION_BATCHES,
        query = DonationBatchQueryConstants.QUERY_COUNT_DONATION_BATCHES),
    @NamedQuery(name = DonationBatchQueryConstants.NAME_VERIFY_DONATION_BATCH_WITH_ID_EXISTS,
        query = DonationBatchQueryConstants.QUERY_VERIFY_DONATION_BATCH_WITH_ID_EXISTS),
    @NamedQuery(name = DonationBatchQueryConstants.NAME_FIND_UNASSIGNED_DONATION_BATCHES_WITH_COMPONENTS,
        query = DonationBatchQueryConstants.QUERY_FIND_UNASSIGNED_DONATION_BATCHES_WITH_COMPONENTS),
    @NamedQuery(name = DonationBatchQueryConstants.NAME_FIND_COMPONENTBATCH_BY_DONATIONBATCH_ID,
        query = DonationBatchQueryConstants.QUERY_FIND_COMPONENTBATCH_BY_DONATIONBATCH_ID)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class DonationBatch extends BaseModificationTrackerUUIDEntity {

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
  @NotNull
  private Location venue;

  @OneToOne(fetch = FetchType.LAZY)
  private ComponentBatch componentBatch;

  private boolean isDeleted;
  private boolean isClosed;

  @Lob
  private String notes;

  @Column(nullable = false)
  private boolean backEntry;

  @NotNull
  @Column(nullable = false)
  private Date donationBatchDate;

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

  public Location getVenue() {
    return venue;
  }

  public void setVenue(Location venue) {
    this.venue = venue;
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

  public Date getDonationBatchDate() {
    return donationBatchDate;
  }

  public void setDonationBatchDate(Date donationBatchDate) {
    this.donationBatchDate = donationBatchDate;
  }
}
