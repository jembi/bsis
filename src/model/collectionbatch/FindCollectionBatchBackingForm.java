package model.collectionbatch;

import java.util.List;

public class FindCollectionBatchBackingForm {

  private CollectionBatch collectionBatch;

  private List<String> collectionCenters;
  private List<String> collectionSites;

  public FindCollectionBatchBackingForm() {
    collectionBatch = new CollectionBatch();
  }
  
  public CollectionBatch getBatch() {
    return collectionBatch;
  }

  public void setBatch(CollectionBatch batch) {
    this.collectionBatch = batch;
  }

  public String getBatchNumber() {
    return collectionBatch.getBatchNumber();
  }

  public void setBatchNumber(String batchNumber) {
    collectionBatch.setBatchNumber(batchNumber);
  }

  public String getNotes() {
    return collectionBatch.getNotes();
  }

  public void setNotes(String notes) {
    collectionBatch.setNotes(notes);
  }

  public List<String> getCollectionCenters() {
    return collectionCenters;
  }

  public void setCollectionCenters(List<String> collectionCenters) {
    this.collectionCenters = collectionCenters;
  }

  public List<String> getCollectionSites() {
    return collectionSites;
  }

  public void setCollectionSites(List<String> collectionSites) {
    this.collectionSites = collectionSites;
  }
}
