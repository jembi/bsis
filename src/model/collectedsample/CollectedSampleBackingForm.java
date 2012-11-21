package model.collectedsample;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.TestResult;
import model.donor.Donor;
import model.user.User;
import model.util.Location;

import org.springframework.beans.factory.annotation.Autowired;

import repository.BloodBagTypeRepository;
import repository.DonorRepository;
import repository.LocationRepository;

public class CollectedSampleBackingForm {

  @NotNull
  @Valid
  private CollectedSample collectedSample;
  private List<String> centers;
  private List<String> sites;
  private String dateCollectedFrom;
  private String dateCollectedTo;

  @Autowired
  private DonorRepository donorRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private BloodBagTypeRepository bloodBagTypeRepository;
  
  public CollectedSampleBackingForm() {
    collectedSample = new CollectedSample();
  }

  public CollectedSampleBackingForm(CollectedSample collection) {
    this.collectedSample = collection;
  }

  public void copy(CollectedSample collection) {
    collection.copy(collection);
  }

  public CollectedSample getCollectedSample() {
    return this.collectedSample;
  }

  public List<String> getCenters() {
    return centers;
  }

  public List<String> getSites() {
    return sites;
  }

  public String getCollectedOn() {
    Date dateCollected = collectedSample.getCollectedOn();
    if (dateCollected == null)
      return null;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    return dateFormat.format(dateCollected);
  }

  public String getCollectionNumber() {
    return collectedSample.getCollectionNumber();
  }

  public void setCollectedOn(String dateCollected) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      collectedSample.setCollectedOn(dateFormat.parse(dateCollected));
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
    this.collectedSample = collection;
  }

  public void setCenters(List<String> centers) {
    this.centers = centers;
  }

  public void setSites(List<String> sites) {
    this.sites = sites;
  }

  public boolean equals(Object obj) {
    return collectedSample.equals(obj);
  }

  public Long getId() {
    return collectedSample.getId();
  }

  public Donor getDonor() {
    return collectedSample.getDonor();
  }

  public List<TestResult> getTestResults() {
    return collectedSample.getTestResults();
  }

  public String getCenter() {
    return collectedSample.getCenter().getName();
  }

  public Location getSite() {
    return collectedSample.getSite();
  }

  public String getDonorType() {
    return collectedSample.getDonorType();
  }

  public String getBloodBagType() {
    BloodBagType bloodBagType = collectedSample.getBloodBagType();
    if (bloodBagType == null)
      return "";
    else
      return bloodBagType.toString();
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

  public int hashCode() {
    return collectedSample.hashCode();
  }

  public void setId(Long id) {
    collectedSample.setId(id);
  }

  public void setCollectionNumber(String collectionNumber) {
    collectedSample.setCollectionNumber(collectionNumber);
  }

  public void setDonor(String donorId) {
    collectedSample.setDonor(donorRepository.findDonorById(donorId));
  }

  public void setDonor(Donor donor) {
    collectedSample.setDonor(donor);
  }

  public void setTestResults(List<TestResult> testResults) {
    collectedSample.setTestResults(testResults);
  }

  public void setCenter(String center) {
    collectedSample.setCenter(locationRepository.getCenterByName(center));
  }

  public void setSite(String site) {
    collectedSample.setSite(locationRepository.getCenterByName(site));
  }

  public void setCollectedOn(Date collectedOn) {
    collectedSample.setCollectedOn(collectedOn);
  }

  public void setDonorType(String donorType) {
    collectedSample.setDonorType(donorType);
  }

  public void setBloodBagType(String bloodBagType) {
    collectedSample.setBloodBagType(bloodBagTypeRepository.fromString(bloodBagType));
  }

  public void setSampleNumber(String sampleNumber) {
    collectedSample.setSampleNumber(sampleNumber);
  }

  public void setShippingNumber(String shippingNumber) {
    collectedSample.setShippingNumber(shippingNumber);
  }

  public void setLastUpdated(Date lastUpdated) {
    collectedSample.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    collectedSample.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    collectedSample.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    collectedSample.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    collectedSample.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    collectedSample.setIsDeleted(isDeleted);
  }
}