package model.request;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import model.compatibility.CompatibilityTest;
import model.location.Location;
import model.location.LocationExists;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.producttype.ProductTypeExists;
import model.requesttype.RequestType;
import model.requesttype.RequestTypeExists;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;
import model.util.Gender;

import org.hibernate.annotations.Index;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Request implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  @Column(length=30)
  @Index(name="request_requestNumber_index")
  private String requestNumber;

  @DateTimeFormat(pattern="mm/dd/yyyy")
  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="request_requestDate_index")
  private Date requestDate;

  @DateTimeFormat(pattern="mm/dd/yyyy")
  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="request_requiredDate_index")
  private Date requiredDate;

  private Boolean fulfilled;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="request_bloodAbo_index")
  private BloodAbo patientBloodAbo;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  @Index(name="request_bloodRhd_index")
  private BloodRhd patientBloodRhd;

  // fetch type eager to check how many products issued
  @OneToMany(mappedBy="issuedTo")
  private List<Product> issuedProducts;

  @OneToMany(mappedBy="forRequest")
  private Set<CompatibilityTest> crossmatchTests;
  
  @Column(length=30)
  private String patientNumber;

  @Column(length=30)
  private String patientFirstName;

  @Column(length=30)
  private String patientLastName;

  @DateTimeFormat(pattern="mm/dd/yyyy")
  @Temporal(TemporalType.DATE)
  private Date patientBirthDate;

  @Column(length=50)
  private String indicationForUse;
  
  @Column
  private Integer patientAge;

  @Column
  private Gender patientGender;

  @Column(length=100)
  private String patientDiagnosis;

  @Column(length=20)
  private String ward;

  @Column(length=30)
  private String hospital;

  @Column(length=30)
  private String department;

  @Column(length=30)
  private String requestedBy;

  @Column
  private Integer numUnitsRequested;

  @Column
  private Integer numUnitsIssued;

  @Lob
  private String notes;

  @Valid
  private RowModificationTracker modificationTracker;

  @ProductTypeExists
  @ManyToOne
  private ProductType productType;

  @RequestTypeExists
  @ManyToOne
  private RequestType requestType;

  @LocationExists
  @ManyToOne
  private Location requestSite;

  private Boolean isDeleted;

  public Request() {
    modificationTracker = new RowModificationTracker();
  }

  public void copy(Request request) {
    assert (this.getId() == request.getId());
    this.patientBloodAbo = request.patientBloodAbo;
    this.patientBloodRhd = request.patientBloodRhd;
    this.requestSite = request.requestSite;
    this.patientFirstName = request.patientFirstName;
    this.patientLastName = request.patientLastName;
    this.patientAge = request.patientAge;
    this.patientBirthDate = request.patientBirthDate;
    this.department = request.department;
    this.requestType = request.requestType;
    this.requestedBy = request.requestedBy;
    this.hospital = request.hospital;
    this.patientDiagnosis = request.patientDiagnosis;
    this.patientGender = request.patientGender;
    this.ward = request.ward;
    this.patientNumber = request.patientNumber;
    this.productType = request.productType;
    this.requestDate = request.requestDate;
    this.requiredDate = request.requiredDate;
    this.numUnitsRequested = request.numUnitsRequested;
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

  public Integer getNumUnitsRequested() {
    return numUnitsRequested;
  }

  public BloodAbo getPatientBloodAbo() {
    return patientBloodAbo;
  }

  public BloodRhd getPatientBloodRhd() {
    return patientBloodRhd;
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

  public void setNumUnitsRequested(Integer numUnitsRequested) {
    this.numUnitsRequested = numUnitsRequested;
  }

  public void setPatientBloodAbo(BloodAbo patientBloodAbo) {
    this.patientBloodAbo = patientBloodAbo;
  }

  public void setPatientBloodRhd(BloodRhd patientBloodRhd) {
    this.patientBloodRhd = patientBloodRhd;
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

  public BloodGroup getBloodGroup() {
    return new BloodGroup(patientBloodAbo, patientBloodRhd);
  }

  public String getPatientNumber() {
    return patientNumber;
  }

  public void setPatientNumber(String patientNumber) {
    this.patientNumber = patientNumber;
  }

  public String getPatientFirstName() {
    return patientFirstName;
  }

  public void setPatientFirstName(String patientFirstName) {
    this.patientFirstName = patientFirstName;
  }

  public String getPatientLastName() {
    return patientLastName;
  }

  public void setPatientLastName(String patientLastName) {
    this.patientLastName = patientLastName;
  }

  public Date getPatientBirthDate() {
    return patientBirthDate;
  }

  public void setPatientBirthDate(Date patientBirthDate) {
    this.patientBirthDate = patientBirthDate;
  }

  public Integer getPatientAge() {
    return patientAge;
  }

  public void setPatientAge(Integer patientAge) {
    this.patientAge = patientAge;
  }

  public Gender getPatientGender() {
    return patientGender;
  }

  public void setPatientGender(Gender patientGender) {
    this.patientGender = patientGender;
  }

  public String getPatientDiagnosis() {
    return patientDiagnosis;
  }

  public void setPatientDiagnosis(String patientDiagnosis) {
    this.patientDiagnosis = patientDiagnosis;
  }

  public String getWard() {
    return ward;
  }

  public void setWard(String ward) {
    this.ward = ward;
  }

  public String getHospital() {
    return hospital;
  }

  public void setHospital(String hospital) {
    this.hospital = hospital;
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  public String getRequestedBy() {
    return requestedBy;
  }

  public void setRequestedBy(String requestedBy) {
    this.requestedBy = requestedBy;
  }

  public RequestType getRequestType() {
    return requestType;
  }

  public void setRequestType(RequestType requestType) {
    this.requestType = requestType;
  }

  public Set<CompatibilityTest> getCrossmatchTests() {
    return crossmatchTests;
  }

  public void setCrossmatchTests(Set<CompatibilityTest> crossmatchTests) {
    this.crossmatchTests = crossmatchTests;
  }

  public String getIndicationForUse() {
    return indicationForUse;
  }

  public void setIndicationForUse(String indicationForUse) {
    this.indicationForUse = indicationForUse;
  }

  public Integer getNumUnitsIssued() {
    return numUnitsIssued;
  }

  public void setNumUnitsIssued(Integer numUnitsIssued) {
    this.numUnitsIssued = numUnitsIssued;
  }
}