package viewmodel;

import java.util.List;

import model.CustomDateFormatter;
import model.bloodbagtype.BloodBagType;
import model.collectedsample.CollectedSample;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.product.Product;
import model.user.User;

public class CollectedSampleViewModel {

  private CollectedSample collectedSample;

  public CollectedSampleViewModel() {
  }

	public CollectedSampleViewModel(CollectedSample collection) {
		this.collectedSample = collection;
	}

  public void copy(CollectedSample collection) {
    collection.copy(collection);
  }

  public String getCollectedOn() {
    if (collectedSample.getCollectedOn() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(collectedSample.getCollectedOn());
  }

  public boolean equals(Object obj) {
    return collectedSample.equals(obj);
  }

  public Long getId() {
    return collectedSample.getId();
  }

  public String getCollectionNumber() {
    return collectedSample.getCollectionNumber();
  }

  public Donor getDonor() {
    return collectedSample.getDonor();
  }

  public Location getCollectionCenter() {
    return collectedSample.getCollectionCenter();
  }

  public Location getCollectionSite() {
    return collectedSample.getCollectionSite();
  }

  public DonationType getDonationType() {
    return collectedSample.getDonationType();
  }

  public BloodBagType getBloodBagType() {
    return collectedSample.getBloodBagType();
  }

  public String getNotes() {
    return collectedSample.getNotes();
  }

  public Boolean getIsDeleted() {
    return collectedSample.getIsDeleted();
  }

  public List<Product> getProducts() {
    return collectedSample.getProducts();
  }

  public int hashCode() {
    return collectedSample.hashCode();
  }

  public String toString() {
    return collectedSample.toString();
  }

  public String getDonorNumber() {
   if (collectedSample.getDonor() == null)
     return "";
   return collectedSample.getDonor().getDonorNumber();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(collectedSample.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(collectedSample.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = collectedSample.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = collectedSample.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getTTIStatus() {
    if (collectedSample.getTTIStatus() == null)
      return "";
    return collectedSample.getTTIStatus().toString();
  }

  public String getCollectionBatchNumber() {
    if (collectedSample.getCollectionBatch() == null)
      return "";
    return collectedSample.getCollectionBatch().getBatchNumber();
  }

  public String getBloodTypingStatus() {
    if (collectedSample.getBloodTypingStatus() == null)
      return "";
    return collectedSample.getBloodTypingStatus().toString();
  }

  public String getBloodAbo() {
    if (collectedSample.getBloodAbo() == null)
      return "";
    return collectedSample.getBloodAbo().toString();
  }

  public String getBloodRh() {
    if (collectedSample.getBloodRh() == null)
      return "";
    return collectedSample.getBloodRh().toString();
  }

  public String getExtraBloodTypeInformation() {
    if (collectedSample.getExtraBloodTypeInformation() == null)
      return "";
    return collectedSample.getExtraBloodTypeInformation();
  }

  public String getBloodGroup() {
    return getBloodAbo() + getBloodRh();
  }
}
