package model.collectionbatch;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;

import model.collectedsample.CollectedSample;

@Entity
@Audited
public class CollectionBatch {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Column(length=30)
  private String batchNumber;

  @OneToMany(mappedBy="batch")
  private List<CollectedSample> collectionsInBatch;
  
  @Lob
  private String notes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
}
