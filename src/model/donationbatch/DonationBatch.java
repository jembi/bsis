package model.donationbatch;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import model.collectedsample.CollectedSample;
import model.location.Location;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import constraintvalidator.LocationExists;


@Entity
@Audited
public class DonationBatch implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=20, unique=true)
  private String batchNumber;

  @LocationExists
  @ManyToOne
  private Location donationCenter;

  @LocationExists
  @ManyToOne
  private Location donationSite;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="donationBatch")
  private List<CollectedSample> collectionsInBatch;

  private boolean isDeleted;

  @Lob
  private String notes;

  private RowModificationTracker modificationTracker;

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

  public List<CollectedSample> getDonationsInBatch() {
    return collectionsInBatch;
  }

  public void setDonationsInBatch(List<CollectedSample> collectionsInBatch) {
    this.collectionsInBatch = collectionsInBatch;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Location getDonationCenter() {
    return donationCenter;
  }

  public void setDonationCenter(Location donationCenter) {
    this.donationCenter = donationCenter;
  }

  public Location getDonationSite() {
    return donationSite;
  }

  public void setDonationSite(Location donationSite) {
    this.donationSite = donationSite;
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
}
