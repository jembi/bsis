package model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleExists;
import model.modificationtracker.ModificationTracker;
import model.producttype.ProductType;
import model.producttype.ProductTypeExists;
import model.request.Request;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class Product implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @NotBlank
  @Column(nullable=false)
  private String productNumber;

  @CollectedSampleExists
  @ManyToOne
  private CollectedSample collectedSample;

  @ProductTypeExists
  @ManyToOne
  private ProductType productType;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  @Temporal(TemporalType.TIMESTAMP)
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
  private Date lastUpdated;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @ManyToOne
  private User createdBy;

  @ManyToOne
  private User lastUpdatedBy;

  @ManyToOne
  private Request issuedTo;

  @Temporal(TemporalType.TIMESTAMP)
  private Date issuedOn;

  @Lob
  private String notes;

  private Boolean isDeleted;

  private Boolean isAvailable;

  private Boolean isQuarantined;

  public Product() {
  }

  public void copy(Product product) {
    assert (this.getId().equals(product.getId()));
    this.productNumber = product.productNumber;
    this.collectedSample = product.collectedSample;
    this.productType = product.productType;
    this.createdOn = product.createdOn;
    this.expiresOn = product.expiresOn;
    this.bloodAbo = product.bloodAbo;
    this.bloodRhd = product.bloodRhd;
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

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public User getLastUpdatedBy() {
    return lastUpdatedBy;
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

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setIsQuarantined(Boolean isQuarantined) {
    this.isQuarantined = isQuarantined;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public Boolean getIsQuarantined() {
    return isQuarantined;
  }

  public Boolean getIsAvailable() {
    return isAvailable;
  }

  public void setIsAvailable(Boolean isAvailable) {
    this.isAvailable = isAvailable;
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