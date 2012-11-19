package model;

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
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;

@Entity
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @Column(nullable=false)
  private String productNumber;

  @ManyToOne
  private CollectedSample collectedSample;

  @Enumerated(EnumType.STRING)
  private ProductType productType;

  @Temporal(TemporalType.TIMESTAMP)
  private Date expiryDate;
  
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdated;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @ManyToOne
  private User createdBy;

  @ManyToOne
  private User lastUpdatedBy;

  @Lob
  private String notes;

  private Boolean isDeleted;

  public Product() {
  }

  public BloodAbo getBloodAbo() {
    BloodAbo abo = BloodAbo.Unknown;
    for (TestResult t : collectedSample.getTestResults()) {
      if (t.getName().equals("bloodAbo"))
        abo = BloodAbo.valueOf(t.getResult());
    }
    return abo;
  }
  

  public BloodRhd getBloodRhd() {
    BloodRhd rhd = BloodRhd.Unknown;
    for (TestResult t : collectedSample.getTestResults()) {
      if (t.getName().equals("bloodRhd"))
        rhd = BloodRhd.valueOf(t.getResult());
    }
    return rhd;
  }

  public void copy(Product product) {
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

  public String getProductType() {
    return productType.name();
  }

  public Date getExpiryDate() {
    return expiryDate;
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

  public void setProductType(String type) {
    this.productType = ProductType.valueOf(type);
  }

  public void setExpiryDate(Date expiryDate) {
    this.expiryDate = expiryDate;
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

}