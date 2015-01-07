package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import model.user.User;
import model.collectionbatch.CollectionBatch;
import model.location.Location;
import model.collectedsample.CollectedSample;
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
  
  public Integer getId(){
      return collectionBatch.getId();
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
  
  public Boolean getIsClosed() {
    return collectionBatch.getIsClosed();
  }

  public void setIsClosed(Boolean isClosed) {
    collectionBatch.setIsClosed(isClosed);
  }
  
  public void setDonorPanel(Long donorPanelId){
    Location donorPanel = new Location();
    donorPanel.setId(donorPanelId);
    collectionBatch.setDonorPanel(donorPanel);
  }

  @JsonIgnore
  public Date getLastUpdated() {
    return collectionBatch.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return collectionBatch.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return collectionBatch.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return collectionBatch.getLastUpdatedBy();
  }
  
  @JsonIgnore
  public List<CollectedSample> getCollectionsInBatch() {
    return collectionBatch.getCollectionsInBatch();
  }
  
  @JsonIgnore
  public Integer getNumCollections() {
    return collectionBatch.getCollectionsInBatch().size();
  }

}
