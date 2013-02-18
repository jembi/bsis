package model.crossmatch;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.annotations.Index;

import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.product.ProductExists;
import model.request.Request;
import model.request.RequestExists;
import model.user.User;

@Entity
public class CrossmatchTest implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @RequestExists
  @ManyToOne
  private Request forRequest;

  @ProductExists
  @ManyToOne
  private Product testedProduct;

  @Temporal(TemporalType.TIMESTAMP)
  @Index(name="compatibilityTest_crossmatchTestDate_index")
  private Date crossmatchTestDate;

  private Boolean transfusedBefore;

  private CompatibilityResult compatibilityResult;

  @ManyToOne
  private CrossmatchType crossmatchType;

  private String testedBy;

  @Lob
  private String notes;
  
  @Valid
  private RowModificationTracker modificationTracker;

  public CrossmatchTest() {
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

  public Product getTestedProduct() {
    return testedProduct;
  }

  public void setTestedProduct(Product testedProduct) {
    this.testedProduct = testedProduct;
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

  public Date getCrossmatchTestDate() {
    return crossmatchTestDate;
  }

  public void setCrossmatchTestDate(Date crossmatchTestDate) {
    this.crossmatchTestDate = crossmatchTestDate;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }


}
