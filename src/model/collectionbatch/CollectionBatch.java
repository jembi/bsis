package model.collectionbatch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class CollectionBatch {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Column(length=30)
  private String batchNumber;

  @Column(length=30)
  private String collectionNumberBegin;

  private String collectionNumberEnd;
  
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

  public String getCollectionNumberBegin() {
    return collectionNumberBegin;
  }

  public void setCollectionNumberBegin(String collectionNumberBegin) {
    this.collectionNumberBegin = collectionNumberBegin;
  }

  public String getCollectionNumberEnd() {
    return collectionNumberEnd;
  }

  public void setCollectionNumberEnd(String collectionNumberEnd) {
    this.collectionNumberEnd = collectionNumberEnd;
  }
}
