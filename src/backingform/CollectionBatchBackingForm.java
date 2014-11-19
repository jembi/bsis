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
  
  public Integer getId(){
      return collectionBatch.getId();
  }

  public String getBatchNumber() {
    return collectionBatch.getBatchNumber();
  }

  public void setBatchNumber(String batchNumber) {
    collectionBatch.setBatchNumber(batchNumber);
  }
  
   public void setDonorPanel(Location donorPanel){
       if(donorPanel == null || donorPanel.getId() == null){
           collectionBatch.setDonorPanel(null);
       }else{
           collectionBatch.setDonorPanel(donorPanel);
        }
            
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
