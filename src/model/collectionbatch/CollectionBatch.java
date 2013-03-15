package model.collectionbatch;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import model.collectedsample.CollectedSample;
import model.location.Location;
import model.location.LocationExists;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

@Entity
@Audited
public class CollectionBatch implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, columnDefinition="TINYINT(4)")
  private Integer id;

  @Column(length=30)
  private String batchNumber;

  @LocationExists
  @ManyToOne
  private Location collectionCenter;

  @LocationExists
  @ManyToOne
  private Location collectionSite;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="collectionBatch")
  private List<CollectedSample> collectionsInBatch;

  private boolean isDeleted;

  @Lob
  private String notes;

  private RowModificationTracker modificationTracker;

  public CollectionBatch() {
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

  public List<CollectedSample> getCollectionsInBatch() {
    return collectionsInBatch;
  }

  public void setCollectionsInBatch(List<CollectedSample> collectionsInBatch) {
    this.collectionsInBatch = collectionsInBatch;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Location getCollectionCenter() {
    return collectionCenter;
  }

  public void setCollectionCenter(Location collectionCenter) {
    this.collectionCenter = collectionCenter;
  }

  public Location getCollectionSite() {
    return collectionSite;
  }

  public void setCollectionSite(Location collectionSite) {
    this.collectionSite = collectionSite;
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
