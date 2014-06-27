package backingform;

import java.util.List;

import model.collectionbatch.CollectionBatch;

public class FindCollectionBatchBackingForm {

  private CollectionBatch collectionBatch;
  private String din;
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

    public String getDin() {
        return din;
    }

    public void setDin(String din) {
        this.din = din;
    }
  
  
}
