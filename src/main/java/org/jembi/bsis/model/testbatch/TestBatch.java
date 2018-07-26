package org.jembi.bsis.model.testbatch;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.constant.TestBatchNamedQueryConstants;
import org.jembi.bsis.service.TestBatchCRUDService;

import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import static org.jembi.bsis.model.testbatch.TestBatchStatus.CLOSED;
import static org.jembi.bsis.model.testbatch.TestBatchStatus.OPEN;

@NamedQueries({
    @NamedQuery(name = TestBatchNamedQueryConstants.NAME_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION,
        query = TestBatchNamedQueryConstants.QUERY_FIND_TEST_BATCHES_BY_STATUSES_PERIOD_AND_LOCATION)
})
@Entity
@Audited
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class TestBatch extends BaseModificationTrackerUUIDEntity {

  private static final long serialVersionUID = 1L;

  @Lob
  private String notes;

  private Boolean isDeleted = Boolean.FALSE;

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(length = 20, unique = true)
  @Size(min = 6, max = 6)
  private String batchNumber;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "DATETIME", nullable = false)
  private Date testBatchDate;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private TestBatchStatus status;

  @SuppressWarnings("unchecked")
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy = "testBatch", fetch = FetchType.LAZY)
  @Where(clause = "isDeleted = 0")
  private Set<Donation> donations = Collections.EMPTY_SET;

  @ManyToOne(optional = false)
  private Location location;

  private boolean backEntry;

  public TestBatch() {
    super();
  }

  public boolean isOpen() {
    return OPEN == status;
  }

  public boolean isClosed() {
    return CLOSED == status;
  }

  public void addDonation(Donation donation) {
    if (!this.isOpen()) {
      throw new IllegalStateException("Donations may only be added to open test batches");
    } else if (donation.getTestBatch() != null && !Objects.equals(donation.getTestBatch(), this)) {
      throw new IllegalArgumentException(String
          .format("Unable to add %s to %s. Donation already assigned to %s", donation, this, donation
              .getTestBatch()));
    }
    donation.setTestBatch(this);
    this.donations.add(donation);
  }

  public void removeDonation(Donation donation) {
    if (!this.isOpen()) {
      throw new IllegalStateException("Donations may only be removed from open test batches");
    } else if (donation.getTestBatch() != null && !Objects.equals(donation.getTestBatch(), this)) {
      throw new IllegalArgumentException(String.
          format("Unable to remove %s from %s. Donation already assigned to %s", donation, this, donation
              .getTestBatch()));
    }
    donation.setTestBatch(null);
    this.donations.remove(donation);
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getBatchNumber() {
    return batchNumber;
  }

  public void setBatchNumber(String batchNumber) {
    this.batchNumber = batchNumber;
  }

  public TestBatchStatus getStatus() {
    return status;
  }

  /**
   * N.B. Updating the status of a test batch should be done via the {@link TestBatchCRUDService}.
   *
   * @param status The new {@link TestBatchStatus}
   */
  public void setStatus(TestBatchStatus status) {
    this.status = status;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public Date getTestBatchDate() {
    return testBatchDate;
  }

  public void setTestBatchDate(Date testBatchDate) {
    this.testBatchDate = testBatchDate;
  }

  public Set<Donation> getDonations() {
    return donations;
  }

  public void setDonations(Set<Donation> donations) {
    this.donations = donations;
  }

  public boolean isBackEntry() {
    return backEntry;
  }

  public void setBackEntry(boolean backEntry) {
    this.backEntry = backEntry;
  }
}
