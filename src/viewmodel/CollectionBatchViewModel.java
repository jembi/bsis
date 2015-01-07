package viewmodel;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import utils.CustomDateFormatter;

import viewmodel.CollectedSampleViewModel;
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

  public List<CollectedSampleViewModel> getCollectionsInBatch() {
    //return collectionBatch.getCollectionsInBatch();
    if (collectionBatch.getCollectionsInBatch() == null)
      return Arrays.asList(new CollectedSampleViewModel[0]);
    List<CollectedSampleViewModel> collectionViewModels = new ArrayList<CollectedSampleViewModel>();
    for (CollectedSample collection : collectionBatch.getCollectionsInBatch()) {
      collectionViewModels.add(new CollectedSampleViewModel(collection));
    }
    return collectionViewModels;
  }
  
  public Integer getNumCollections() {
	 return collectionBatch.getCollectionsInBatch().size();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(collectionBatch.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateString(collectionBatch.getCreatedDate());
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
  
  public LocationViewModel getDonorPanel(){
      return  new LocationViewModel((collectionBatch.getDonorPanel()));
  }
}
