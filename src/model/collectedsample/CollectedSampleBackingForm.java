package model.collectedsample;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.CustomDateFormatter;
import model.bloodbagtype.BloodBagType;
import model.donationtype.DonationType;
import model.donor.Donor;
import model.location.Location;
import model.testresults.TestResult;
import model.user.User;

import org.apache.commons.lang3.RandomStringUtils;

public class CollectedSampleBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private CollectedSample collectedSample;

  private List<String> centers;
  private List<String> sites;
  private String dateCollectedFrom;
  private String dateCollectedTo;

  private String collectedOn;

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
    if (collectedOn != null)
      return collectedOn;
    if (collectedSample == null)
      return "";
    return CustomDateFormatter.getDateTimeString(collectedSample.getCollectedOn());
  }

  public String getCollectionNumber() {
    return collectedSample.getCollectionNumber();
  }

  public void setCollectedOn(String collectedOn) {
    this.collectedOn = collectedOn;
    try {
      collectedSample.setCollectedOn(CustomDateFormatter.getDateTimeFromString(collectedOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      collectedSample.setCollectedOn(null);
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

  public String getCollectionCenter() {
    Location center = collectedSample.getCollectionCenter();
    if (center == null || center.getId() == null)
      return null;
    return center.getId().toString();
  }

  public String getCollectionSite() {
    Location site = collectedSample.getCollectionSite();
    if (site == null || site.getId() == null)
      return null;
    return site.getId().toString();
  }

  public String getDonorType() {
    DonationType donorType = collectedSample.getDonorType();
    if (donorType == null || donorType.getId() == null)
      return null;
    else
      return donorType.getId().toString();
  }

  public String getBloodBagType() {
    BloodBagType bloodBagType = collectedSample.getBloodBagType();
    if (bloodBagType == null || bloodBagType.getId() == null)
      return null;
    else
      return bloodBagType.getId().toString();
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

  public void setDonor(Donor donor) {
    collectedSample.setDonor(donor);
  }

  public void setTestResults(List<TestResult> testResults) {
    collectedSample.setTestResults(testResults);
  }

  public void setCollectionCenter(String center) {
    if (center == null) {
      collectedSample.setCollectionCenter(null);
    }
    else {
      Location l = new Location();
      l.setId(Long.parseLong(center));
      collectedSample.setCollectionCenter(l);
    }
  }

  public void setCollectionSite(String collectionSite) {
    if (collectionSite == null) {
      collectedSample.setCollectionSite(null);
    }
    else {
      Location l = new Location();
      l.setId(Long.parseLong(collectionSite));
      collectedSample.setCollectionSite(l);
    }
  }

  public void setDonorType(String donorTypeId) {
    if (donorTypeId == null || donorTypeId.isEmpty()) {
      collectedSample.setDonorType(null);
    }
    else {
      DonationType dt = new DonationType();
      dt.setId(Integer.parseInt(donorTypeId));
      collectedSample.setDonorType(dt);
    }
  }

  public void setBloodBagType(String bloodBagTypeId) {
    if (bloodBagTypeId == null || bloodBagTypeId.isEmpty()) {
      collectedSample.setBloodBagType(null);
    }
    else {
      BloodBagType bt = new BloodBagType();
      bt.setId(Integer.parseInt(bloodBagTypeId));
      collectedSample.setBloodBagType(bt);
    }
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

  public void generateCollectionNumber() {
    String uniqueCollectedSampleNumber;
    uniqueCollectedSampleNumber = "C-" +
                        RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    collectedSample.setCollectionNumber(uniqueCollectedSampleNumber);
  }

  public String getDonorNumber() {
    if (collectedSample == null || collectedSample.getDonor() == null ||
        collectedSample.getDonor().getDonorNumber() == null
       )
      return "";
    return collectedSample.getDonor().getDonorNumber();
  }

  public void setDonorNumber(String donorNumber) {
    Donor donor = new Donor();
    donor.setDonorNumber(donorNumber);
    collectedSample.setDonor(donor);
  }

  public String getDonorIdHidden() {
    if (collectedSample == null)
      return null;
    Donor donor = collectedSample.getDonor();
    if (donor == null || donor.getId() == null)
      return null;
    return donor.getId().toString();
  }

  public void setDonorIdHidden(String donorId) {
    if (donorId == null) {
      collectedSample.setDonor(null);
    }
    else {
      
      try {
        Donor d = new Donor();
        d.setId(Long.parseLong(donorId));
        collectedSample.setDonor(d);
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
        collectedSample.setDonor(null);
      }
    }
  }
}