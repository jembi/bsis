package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.bloodbagtype.BloodBagType;
import model.collectedsample.CollectedSample;
import model.donor.Donor;
import model.donortype.DonorType;
import model.location.Location;
import model.product.Product;
import model.testresults.TestResult;
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
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(collectedSample.getCollectedOn());
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

  public List<TestResult> getTestResults() {
    return collectedSample.getTestResults();
  }

  public Location getCollectionCenter() {
    return collectedSample.getCollectionCenter();
  }

  public Location getCollectionSite() {
    return collectedSample.getCollectionSite();
  }

  public DonorType getDonorType() {
    return collectedSample.getDonorType();
  }

  public BloodBagType getBloodBagType() {
    return collectedSample.getBloodBagType();
  }

  public String getSampleNumber() {
    return collectedSample.getSampleNumber();
  }

  public String getShippingNumber() {
    return collectedSample.getShippingNumber();
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

  public Date getLastUpdated() {
    return collectedSample.getLastUpdated();
  }

  public Date getCreatedDate() {
    return collectedSample.getCreatedDate();
  }

  public User getCreatedBy() {
    return collectedSample.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return collectedSample.getLastUpdatedBy();
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
}