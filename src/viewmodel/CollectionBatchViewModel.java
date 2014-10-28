package viewmodel;

import java.util.List;

import utils.CustomDateFormatter;

import model.collectedsample.CollectedSample;
import model.collectionbatch.CollectionBatch;
import model.location.Location;
import model.user.User;

public class CollectionBatchViewModel {

  private CollectionBatch collectionBatch;

  public CollectionBatchViewModel() {
  }
  
  public CollectionBatchViewModel(CollectionBatch collectionBatch) {
    this.collectionBatch = collectionBatch;
  }

  public Integer getId() {
    return collectionBatch.getId();
  }

  public String getBatchNumber() {
    return collectionBatch.getBatchNumber();
  }

  public String getNotes() {
    return collectionBatch.getNotes();
  }

  public List<CollectedSample> getCollectionsInBatch() {
    return collectionBatch.getCollectionsInBatch();
  }

  public Location getCollectionCenter() {
    return collectionBatch.getCollectionCenter();
  }

  public Location getCollectionSite() {
    return collectionBatch.getCollectionSite();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(collectionBatch.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(collectionBatch.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = collectionBatch.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = collectionBatch.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }
  
  public Boolean getIsClosed() {
    return collectionBatch.getIsClosed();
  }
}
