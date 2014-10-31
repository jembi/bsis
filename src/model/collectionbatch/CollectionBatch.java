package model.collectionbatch;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import constraintvalidator.LocationExists;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import model.collectedsample.CollectedSample;
import model.location.Location;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.testbatch.TestBatch;
import model.user.User;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;


@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class CollectionBatch implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=20, unique=true)
  private String batchNumber;

  @LocationExists
  @ManyToOne
  private Location collectionCenter;

  @LocationExists
  @ManyToOne
  private Location collectionSite;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="collectionBatch", fetch = FetchType.EAGER)
  private List<CollectedSample> collectionsInBatch;
  
  
  @ManyToOne
  @JoinColumn(updatable = false)
  private TestBatch testBatch;

  private boolean isDeleted;
  private boolean isClosed;

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
  
  public boolean getIsClosed() {
    return isClosed;
  }

  public void setIsClosed(boolean isClosed) {
    this.isClosed = isClosed;
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
  
  public TestBatch getTestBatch() {
     return testBatch;
   }

  public void setTestBatch(TestBatch testBatch) {
     this.testBatch = testBatch;
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
