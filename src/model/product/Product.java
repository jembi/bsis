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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.compatibility.CompatibilityTest;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.productmovement.ProductStatusChange;
import model.producttype.ProductType;
import model.request.Request;
import model.usage.ProductUsage;
import model.user.User;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import constraintvalidator.CollectedSampleExists;
import constraintvalidator.ProductTypeExists;


@Entity
@Audited
public class Product implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  // A product may not have a corresponding sample. Some products may be
  // imported from another location. In such a case the corresponding collection
  // field is allowed to be null.
  @CollectedSampleExists
  @ManyToOne(optional=true, fetch=FetchType.LAZY)
  private CollectedSample collectedSample;

  @ProductTypeExists
  @ManyToOne
  private ProductType productType;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="product_expiresOn_index")
  private Date expiresOn;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition="DATETIME")
  private Date discardedOn;

  @ManyToOne
  private Request issuedTo;

  @Temporal(TemporalType.TIMESTAMP)
  private Date issuedOn;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private ProductStatus status;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="testedProduct", fetch=FetchType.LAZY)
  private List<CompatibilityTest> compatibilityTests;

  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @OneToMany(mappedBy="product", fetch=FetchType.LAZY)
  private List<ProductStatusChange> statusChanges;

  @Column(length=3)
  private String subdivisionCode;

  @OneToOne(optional=true)
  private Product parentProduct;

  @OneToOne(mappedBy="product")
  private ProductUsage usage;
  
  @Lob
  private String notes;

  private Boolean isDeleted;

  @Valid
  private RowModificationTracker modificationTracker;

  @Column(length=20)
  private String donationIdentificationNumber;
  
  public Product() {
    modificationTracker = new RowModificationTracker();
  }

  public void copy(Product product) {
    assert (this.getId().equals(product.getId()));
    this.collectedSample = product.collectedSample;
    this.productType = product.productType;
    this.createdOn = product.createdOn;
    this.expiresOn = product.expiresOn;
    this.notes = product.notes;
    this.donationIdentificationNumber = product.donationIdentificationNumber;
  }

  public Long getId() {
    return id;
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

  public List<ProductStatusChange> getStatusChanges() {
    return statusChanges;
  }

  public void setStatusChanges(List<ProductStatusChange> statusChanges) {
    this.statusChanges = statusChanges;
  }

  public ProductUsage getUsage() {
    return usage;
  }

  public void setUsage(ProductUsage usage) {
    this.usage = usage;
  }

  public String getSubdivisionCode() {
    return subdivisionCode;
  }

  public void setSubdivisionCode(String subdivisionCode) {
    this.subdivisionCode = subdivisionCode;
  }

  public Product getParentProduct() {
    return parentProduct;
  }

  public void setParentProduct(Product parentProduct) {
    this.parentProduct = parentProduct;
  }

	public String getDonationIdentificationNumber() {
		return donationIdentificationNumber;
	}

	public void setDonationIdentificationNumber(String donationIdentificationNumber) {
		this.donationIdentificationNumber = donationIdentificationNumber;
	}
  
}
