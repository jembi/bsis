package model.request;

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
import javax.validation.constraints.NotNull;

import model.location.Location;
import model.location.LocationExists;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.producttype.ProductTypeExists;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Request implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @NotBlank
  @Column(length=30, nullable=false)
  @Index(name="request_requestNumber_index")
  private String requestNumber;

  @NotNull
  @DateTimeFormat(pattern="mm/dd/yyyy")
  @Temporal(TemporalType.DATE)
  @Index(name="request_requestDate_index")
  private Date requestDate;

  @NotNull
  @DateTimeFormat(pattern="mm/dd/yyyy")
  @Temporal(TemporalType.DATE)
  @Index(name="request_requiredDate_index")
  private Date requiredDate;

  @NotNull
  private Integer requestedQuantity;

  private Boolean fulfilled;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="request_bloodAbo_index")
  private BloodAbo bloodAbo;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="request_bloodRhd_index")
  private BloodRhd bloodRhd;

  // fetch type eager to check how many products issued
  @OneToMany(mappedBy="issuedTo", fetch=FetchType.EAGER)
  private List<Product> issuedProducts;

  @Lob
  private String notes;

  @Valid
  private RowModificationTracker modificationTracker;

  @ProductTypeExists
  @ManyToOne
  private ProductType productType;

  @LocationExists
  @ManyToOne
  private Location requestSite;

  private String patientName;

  private Boolean isDeleted;

  public Request() {
    modificationTracker = new RowModificationTracker();
  }

  public void copy(Request request) {
    assert (this.getId() == request.getId());
    this.bloodAbo = request.bloodAbo;
    this.bloodRhd = request.bloodRhd;
    this.requestSite = request.requestSite;
    this.patientName = request.patientName;
    this.productType = request.productType;
    this.requestDate = request.requestDate;
    this.requiredDate = request.requiredDate;
    this.notes = request.notes;
  }

  public Long getId() {
    return id;
  }

  public String getRequestNumber() {
    return requestNumber;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public Date getRequiredDate() {
    return requiredDate;
  }

  public Integer getRequestedQuantity() {
    return requestedQuantity;
  }

  public BloodAbo getBloodAbo() {
    return bloodAbo;
  }

  public BloodRhd getBloodRhd() {
    return bloodRhd;
  }

  public String getNotes() {
    return notes;
  }

  public RowModificationTracker getModificationTracker() {
    return modificationTracker;
  }

  public ProductType getProductType() {
    return productType;
  }

  public Location getRequestSite() {
    return requestSite;
  }

  public String getPatientName() {
    return patientName;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setRequestNumber(String requestNumber) {
    this.requestNumber = requestNumber;
  }

  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }

  public void setRequiredDate(Date requiredDate) {
    this.requiredDate = requiredDate;
  }

  public void setRequestedQuantity(Integer requestedQuantity) {
    this.requestedQuantity = requestedQuantity;
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    this.bloodRhd = bloodRhd;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    this.modificationTracker = modificationTracker;
  }

  public void setProductType(ProductType productType) {
    this.productType = productType;
  }

  public void setRequestSite(Location requestSite) {
    this.requestSite = requestSite;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public List<Product> getIssuedProducts() {
    return issuedProducts;
  }

  public void setIssuedProducts(List<Product> issuedProducts) {
    this.issuedProducts = issuedProducts;
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

  public Boolean getFulfilled() {
    return fulfilled;
  }

  public void setFulfilled(Boolean fulfilled) {
    this.fulfilled = fulfilled;
  }
}