package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.CollectedSample;
import model.DonorType;
import model.TestResult;
import model.donor.Donor;
import model.user.User;
import model.util.Location;

public class CollectionViewModel {
	private CollectedSample collection;
	private List<Location> allCollectionSites;
	private List<Location> allCenters;

	public CollectionViewModel(CollectedSample collection) {
		this.collection = collection;
	}

	public CollectionViewModel(CollectedSample collection,
			List<Location> allCollectionSites, List<Location> allCenters) {

		this.collection = collection;
		this.allCollectionSites = allCollectionSites;
		this.allCenters = allCenters;
	}

  public void copy(CollectedSample collection) {
    collection.copy(collection);
  }

  public boolean equals(Object obj) {
    return collection.equals(obj);
  }

  public Long getId() {
    return collection.getId();
  }

  public String getCollectionNumber() {
    return collection.getCollectionNumber();
  }

  public Donor getDonor() {
    return collection.getDonor();
  }

  public List<TestResult> getTestResults() {
    return collection.getTestResults();
  }

  public Location getCenter() {
    return collection.getCenter();
  }

  public Location getSite() {
    return collection.getSite();
  }

  public String getCollectedOn() {
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(collection.getCollectedOn());
  }

  public DonorType getDonorType() {
    return collection.getDonorType();
  }

  public String getSampleNumber() {
    return collection.getSampleNumber();
  }

  public String getShippingNumber() {
    return collection.getShippingNumber();
  }

  public Date getLastUpdated() {
    return collection.getLastUpdated();
  }

  public Date getCreatedDate() {
    return collection.getCreatedDate();
  }

  public User getCreatedBy() {
    return collection.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return collection.getLastUpdatedBy();
  }

  public String getNotes() {
    return collection.getNotes();
  }

  public Boolean getIsDeleted() {
    return collection.getIsDeleted();
  }

  public CollectedSample getCollection() {
    return collection;
  }

  public List<Location> getAllCollectionSites() {
    return allCollectionSites;
  }

  public List<Location> getAllCenters() {
    return allCenters;
  }

  public void setCollection(CollectedSample collection) {
    this.collection = collection;
  }

  public void setAllCollectionSites(List<Location> allCollectionSites) {
    this.allCollectionSites = allCollectionSites;
  }

  public void setAllCenters(List<Location> allCenters) {
    this.allCenters = allCenters;
  }

}