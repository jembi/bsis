package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.donor.Donor;
import model.user.User;
import model.util.Location;

import org.springframework.beans.factory.annotation.Autowired;

import repository.DonorRepository;

public class CollectedSampleBackingForm {

  private CollectedSample collection;
  private List<String> centers;
  private List<String> sites;
  private String dateCollectedFrom;
  private String dateCollectedTo;

  @Autowired
  private DonorRepository donorRepository;
  private Donor donor;
  
  public CollectedSampleBackingForm() {
    collection = new CollectedSample();
  }

  public CollectedSampleBackingForm(CollectedSample collection) {
    this.collection = collection;
  }

  public void copy(CollectedSample collection) {
    collection.copy(collection);
  }

  public CollectedSample getCollection() {
    return this.collection;
  }

  public List<String> getCenters() {
    return centers;
  }

  public List<String> getSites() {
    return sites;
  }

  public String getCollectedOn() {
    Date dateCollected = collection.getCollectedOn();
    if (dateCollected == null)
      return null;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    return dateFormat.format(dateCollected);
  }

  public String getCollectionNumber() {
    return collection.getCollectionNumber();
  }

  public void setCollectedOn(String dateCollected) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      collection.setCollectedOn(dateFormat.parse(dateCollected));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String getDateCollectedFrom() {
    return dateCollectedFrom;
  }

  public void setDateCollectedFrom(String dateCollectedFrom) {
    this.dateCollectedFrom = dateCollectedFrom;
  }

  public String getDateCollectedTo() {
    return dateCollectedTo;
  }

  public void setDateCollectedTo(String dateCollectedTo) {
    this.dateCollectedTo = dateCollectedTo;
  }

  public void setCollection(CollectedSample collection) {
    this.collection = collection;
  }

  public void setCenters(List<String> centers) {
    this.centers = centers;
  }

  public void setSites(List<String> sites) {
    this.sites = sites;
  }

  public boolean equals(Object obj) {
    return collection.equals(obj);
  }

  public Long getId() {
    return collection.getId();
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

  public int hashCode() {
    return collection.hashCode();
  }

  public void setId(Long id) {
    collection.setId(id);
  }

  public void setCollectionNumber(String collectionNumber) {
    collection.setCollectionNumber(collectionNumber);
  }

  public void setDonor(String donorNumber) {
    donor = donorRepository.findDonorById(donorNumber);
  }

  public void setDonor(Donor donor) {
    collection.setDonor(donor);
  }

  public void setTestResults(List<TestResult> testResults) {
    collection.setTestResults(testResults);
  }

  public void setCenter(Location center) {
    collection.setCenter(center);
  }

  public void setSite(Location site) {
    collection.setSite(site);
  }

  public void setCollectedOn(Date collectedOn) {
    collection.setCollectedOn(collectedOn);
  }

  public void setDonorType(DonorType donorType) {
    collection.setDonorType(donorType);
  }

  public void setSampleNumber(String sampleNumber) {
    collection.setSampleNumber(sampleNumber);
  }

  public void setShippingNumber(String shippingNumber) {
    collection.setShippingNumber(shippingNumber);
  }

  public void setLastUpdated(Date lastUpdated) {
    collection.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    collection.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    collection.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    collection.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    collection.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    collection.setIsDeleted(isDeleted);
  }

  public String toString() {
    return collection.toString();
  }
}