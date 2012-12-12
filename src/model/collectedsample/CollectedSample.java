package model.collectedsample;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.bloodbagtype.BloodBagType;
import model.bloodbagtype.BloodBagTypeExists;
import model.donor.Donor;
import model.donor.DonorExists;
import model.donortype.DonorType;
import model.donortype.DonorTypeExists;
import model.location.Location;
import model.location.LocationExists;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.testresults.TestResult;
import model.user.User;

import org.hibernate.annotations.Index;

@Entity
public class CollectedSample implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
  private Long id;

  @Column(length=30)
  @Index(name="collectedSample_collectionNumber_index")
  private String collectionNumber;

  @DonorExists
  @ManyToOne(fetch=FetchType.LAZY)
  private Donor donor;

  @OneToMany(mappedBy="collectedSample")
  private List<TestResult> testResults;

  @LocationExists
  @ManyToOne(fetch=FetchType.LAZY)
  private Location collectionCenter;

  @LocationExists
  @ManyToOne(fetch=FetchType.LAZY)
  private Location collectionSite;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="collectedSample_collectedOn_index")
  private Date collectedOn;

  @DonorTypeExists
  @ManyToOne(fetch=FetchType.LAZY)
  private DonorType donorType;

  @BloodBagTypeExists
  @ManyToOne
  private BloodBagType bloodBagType;
  
  @Column(length=50)
  private String sampleNumber;

  @Column(length=50)
  private String shippingNumber;

  @OneToMany(mappedBy="collectedSample")
  private List<Product> products;

  @Valid
  private RowModificationTracker modificationTracker;
  
  @Lob
  private String notes;

  private Boolean isDeleted;

  public CollectedSample() {
    modificationTracker = new RowModificationTracker();
  }

  public Long getId() {
    return id;
  }

  public String getCollectionNumber() {
    return collectionNumber;
  }

  public Donor getDonor() {
    return donor;
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public Location getCollectionCenter() {
    return collectionCenter;
  }

  public Location getCollectionSite() {
    return collectionSite;
  }

  public Date getCollectedOn() {
    return collectedOn;
  }

  public DonorType getDonorType() {
    return donorType;
  }

  public BloodBagType getBloodBagType() {
    return bloodBagType;
  }

  public String getSampleNumber() {
    return sampleNumber;
  }

  public String getShippingNumber() {
    return shippingNumber;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public void setDonor(Donor donor) {
    this.donor = donor;
  }

  public void setTestResults(List<TestResult> testResults) {
    this.testResults = testResults;
  }

  public void setCollectionCenter(Location collectionCenter) {
    this.collectionCenter = collectionCenter;
  }

  public void setCollectionSite(Location collectionSite) {
    this.collectionSite = collectionSite;
  }

  public void setCollectedOn(Date collectedOn) {
    this.collectedOn = collectedOn;
  }

  public void setDonorType(DonorType donorType) {
    this.donorType = donorType;
  }

  public void setBloodBagType(BloodBagType bloodBagType) {
    this.bloodBagType = bloodBagType;
  }

  public void setSampleNumber(String sampleNumber) {
    this.sampleNumber = sampleNumber;
  }

  public void setShippingNumber(String shippingNumber) {
    this.shippingNumber = shippingNumber;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(CollectedSample collectedSample) {
    assert (this.getId().equals(collectedSample.getId()));
    this.collectionNumber = collectedSample.collectionNumber;
    this.donor = collectedSample.donor;
    this.donorType = collectedSample.donorType;
    this.collectedOn = collectedSample.collectedOn;
    this.collectionCenter = collectedSample.collectionCenter;
    this.collectionSite = collectedSample.collectionSite;
    this.notes = collectedSample.notes;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }

  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }

}