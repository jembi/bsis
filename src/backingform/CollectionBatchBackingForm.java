package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.Valid;

import model.collectionbatch.CollectionBatch;
import model.location.Location;

import org.apache.commons.lang3.StringUtils;

public class CollectionBatchBackingForm {

  @Valid
  @JsonIgnore
  private CollectionBatch collectionBatch;

  public CollectionBatchBackingForm() {
    collectionBatch = new CollectionBatch();
  }

  public CollectionBatch getCollectionBatch() {
    return collectionBatch;
  }

  public void setCollectionBatch(CollectionBatch collectionBatch) {
    this.collectionBatch = collectionBatch;
  }

  public void setId(Integer id) {
    collectionBatch.setId(id);
  }

  public String getBatchNumber() {
    return collectionBatch.getBatchNumber();
  }

  public void setBatchNumber(String batchNumber) {
    collectionBatch.setBatchNumber(batchNumber);
  }

  public void setCollectionCenter(Long center) {
      Location l = new Location();
      l.setId(center);
      collectionBatch.setCollectionCenter(l);
  }

  public void setCollectionSite(Long collectionSite) {
      Location l = new Location();
      l.setId(collectionSite);
      collectionBatch.setCollectionSite(l);
  }

  public String getNotes() {
    return collectionBatch.getNotes();
  }

  public void setNotes(String notes) {
    collectionBatch.setNotes(notes);
  }
  
  public Boolean getIsClosed() {
    return collectionBatch.getIsClosed();
  }

  public void setIsClosed(Boolean isClosed) {
	collectionBatch.setIsClosed(isClosed);
  }
}
