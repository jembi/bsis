/**
 * issue - #209{Adapt Bsis To Expose rest Services}
 * Reason - Not required in later versions as get requests should not be binded to be objects
 *
package backingform;

import java.util.List;

import model.collectionbatch.CollectionBatch;

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
* */
