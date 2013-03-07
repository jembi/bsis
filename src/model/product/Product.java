package model.product;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleExists;
import model.compatibility.CompatibilityTest;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.producttype.ProductType;
import model.producttype.ProductTypeExists;
import model.request.Request;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Index;

@FilterDef(name="availableProductsNotExpiredFilter", defaultCondition="isDeleted = '0' && isAvailable = '1'")
@Entity
public class Product implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @Column(length=30)
  @Index(name="product_productNumber_index")
  private String productNumber;

  @CollectedSampleExists
  @ManyToOne(fetch=FetchType.LAZY)
  private CollectedSample collectedSample;

  @ProductTypeExists
  @ManyToOne
  private ProductType productType;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="product_expiresOn_index")
  private Date expiresOn;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="donor_bloodAbo_index")
  private BloodAbo bloodAbo;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="donor_bloodRhd_index")
  private BloodRhd bloodRhd;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date discardedOn;

  @Column(length=100)
  private String discardReason;

  @ManyToOne
  private Request issuedTo;

  @Temporal(TemporalType.TIMESTAMP)
  private Date issuedOn;

  @ManyToOne
  private User discardedBy;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private ProductStatus status;

  @OneToMany(mappedBy="testedProduct", fetch=FetchType.LAZY)
  private List<CompatibilityTest> compatibilityTests;

  @Lob
  private String notes;

  private Boolean isDeleted;

  @Valid
  private RowModificationTracker modificationTracker;

  public Product() {
    modificationTracker = new RowModificationTracker();
  }

  public void copy(Product product) {
    assert (this.getId().equals(product.getId()));
    this.productNumber = product.productNumber;
    this.collectedSample = product.collectedSample;
    this.productType = product.productType;
    this.createdOn = product.createdOn;
    this.expiresOn = product.expiresOn;
    this.notes = product.notes;
  }

  public Long getId() {
    return id;
  }

  public String getProductNumber() {
    return productNumber;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public ProductType getProductType() {
    return productType;
  }

  public Date getExpiresOn() {
    return expiresOn;
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

  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public void setExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public BloodAbo getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public BloodRhd getBloodRhd() {
    return bloodRhd;
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    this.bloodRhd = bloodRhd;
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

  public String getCollectionNumber() {
    if (collectedSample == null)
      return null;
    return collectedSample.getCollectionNumber();
  }

  public ProductStatus getStatus() {
    return status;
  }

  public void setStatus(ProductStatus status) {
    this.status = status;
  }

  public List<CompatibilityTest> getCompatibilityTests() {
    return compatibilityTests;
  }

  public void setCompatibilityTests(List<CompatibilityTest> compatibilityTests) {
    this.compatibilityTests = compatibilityTests;
  }

  public Date getDiscardedOn() {
    return discardedOn;
  }

  public void setDiscardedOn(Date discardedOn) {
    this.discardedOn = discardedOn;
  }

  public String getDiscardReason() {
    return discardReason;
  }

  public void setDiscardReason(String discardReason) {
    this.discardReason = discardReason;
  }

  public User getDiscardedBy() {
    return discardedBy;
  }

  public void setDiscardedBy(User discardedBy) {
    this.discardedBy = discardedBy;
  }

  public Request getIssuedTo() {
    return issuedTo;
  }

  public void setIssuedTo(Request issuedTo) {
    this.issuedTo = issuedTo;
  }

  public Date getIssuedOn() {
    return issuedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
  }
}
