package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.TestResult;
import model.collectedsample.CollectedSample;
import model.donor.Donor;
import model.donortype.DonorType;
import model.user.User;
import model.util.Location;

public class CollectedSampleViewModel {
	private CollectedSample collectedSample;
	private List<Location> allCollectionSites;
	private List<Location> allCenters;

	public CollectedSampleViewModel(CollectedSample collection) {
		this.collectedSample = collection;
	}

	public CollectedSampleViewModel(CollectedSample collection,
			List<Location> allCollectionSites, List<Location> allCenters) {

		this.collectedSample = collection;
		this.allCollectionSites = allCollectionSites;
		this.allCenters = allCenters;
	}

  public void copy(CollectedSample collection) {
    collection.copy(collection);
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

  public Location getCenter() {
    return collectedSample.getCenter();
  }

  public Location getSite() {
    return collectedSample.getSite();
  }

  public String getCollectedOn() {
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(collectedSample.getCollectedOn());
  }

  public DonorType getDonorType() {
    return collectedSample.getDonorType();
  }

  public String getSampleNumber() {
    return collectedSample.getSampleNumber();
  }

  public String getShippingNumber() {
    return collectedSample.getShippingNumber();
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

  public String getNotes() {
    return collectedSample.getNotes();
  }

  public Boolean getIsDeleted() {
    return collectedSample.getIsDeleted();
  }

  public CollectedSample getCollection() {
    return collectedSample;
  }

  public List<Location> getAllCollectionSites() {
    return allCollectionSites;
  }

  public List<Location> getAllCenters() {
    return allCenters;
  }

  public void setCollection(CollectedSample collection) {
    this.collectedSample = collection;
  }

  public void setAllCollectionSites(List<Location> allCollectionSites) {
    this.allCollectionSites = allCollectionSites;
  }

  public void setAllCenters(List<Location> allCenters) {
    this.allCenters = allCenters;
  }

}