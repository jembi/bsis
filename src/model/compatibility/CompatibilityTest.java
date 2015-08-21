package model.compatibility;

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
import javax.validation.Valid;

import model.component.Component;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.request.Request;
import model.user.User;

import org.hibernate.annotations.Index;
import org.hibernate.envers.Audited;

import constraintvalidator.ComponentExists;
import constraintvalidator.RequestExists;


@Entity
@Audited
public class CompatibilityTest implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @RequestExists
  @ManyToOne
  private Request forRequest;

  @ComponentExists
  @ManyToOne
  private Component testedComponent;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="compatibilityTest_crossmatchTestDate_index")
  private Date compatibililityTestDate;

  /**
   * TODO: This field is not used right now. Used in Zambia database.
   */
  private Boolean transfusedBefore;

  @Enumerated(EnumType.STRING)
  @Column(length=15)
  private CompatibilityResult compatibilityResult;

  @ManyToOne
  private CrossmatchType crossmatchType;

  /**
   * TODO: Should be a ManyToOne mapping to the User table.
   */
  private String testedBy;

  private Boolean isDeleted;

  @Lob
  private String notes;
  
  @Valid
  private RowModificationTracker modificationTracker;

  public CompatibilityTest() {
    modificationTracker = new RowModificationTracker();
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Request getForRequest() {
    return forRequest;
  }

  public void setForRequest(Request forRequest) {
    this.forRequest = forRequest;
  }

  public Component getTestedComponent() {
    return testedComponent;
  }

  public void setTestedComponent(Component testedComponent) {
    this.testedComponent = testedComponent;
  }

  public Boolean getTransfusedBefore() {
    return transfusedBefore;
  }

  public void setTransfusedBefore(Boolean transfusedBefore) {
    this.transfusedBefore = transfusedBefore;
  }

  public CompatibilityResult getCompatibilityResult() {
    return compatibilityResult;
  }

  public void setCompatibilityResult(CompatibilityResult compatibilityResult) {
    this.compatibilityResult = compatibilityResult;
  }

  public CrossmatchType getCrossmatchType() {
    return crossmatchType;
  }

  public void setCrossmatchType(CrossmatchType crossmatchType) {
    this.crossmatchType = crossmatchType;
  }

  public String getTestedBy() {
    return testedBy;
  }

  public void setTestedBy(String testedBy) {
    this.testedBy = testedBy;
  }

  public Date getCompatibilityTestDate() {
    return compatibililityTestDate;
  }

  public void setCompatibilityTestDate(Date compatiblityTestDate) {
    this.compatibililityTestDate = compatiblityTestDate;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getRequestNumber() {
    if (forRequest == null)
      return null;
    return forRequest.getRequestNumber();
  }

  public String getDonationIdentificationNumber() {
    return "";
  }
}
