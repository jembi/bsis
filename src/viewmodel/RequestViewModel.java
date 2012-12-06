package viewmodel;

import java.util.Date;
import java.util.List;

import model.location.Location;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.request.Request;
import model.request.RequestStatus;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;

public class RequestViewModel {
	private Request request;

  public Long getId() {
    return request.getId();
  }

  public String getRequestNumber() {
    return request.getRequestNumber();
  }

  public Date getRequestDate() {
    return request.getRequestDate();
  }

  public Date getRequiredDate() {
    return request.getRequiredDate();
  }

  public Integer getRequestedQuantity() {
    return request.getRequestedQuantity();
  }

  public RequestStatus getRequestStatus() {
    return request.getRequestStatus();
  }

  public BloodAbo getBloodAbo() {
    return request.getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return request.getBloodRhd();
  }

  public String getNotes() {
    return request.getNotes();
  }

  public RowModificationTracker getModificationTracker() {
    return request.getModificationTracker();
  }

  public ProductType getProductType() {
    return request.getProductType();
  }

  public Location getRequestSite() {
    return request.getRequestSite();
  }

  public String getPatientName() {
    return request.getPatientName();
  }

  public Boolean getIsDeleted() {
    return request.getIsDeleted();
  }

  public List<Product> getIssuedProducts() {
    return request.getIssuedProducts();
  }

  public Date getLastUpdated() {
    return request.getLastUpdated();
  }

  public Date getCreatedDate() {
    return request.getCreatedDate();
  }

  public User getCreatedBy() {
    return request.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return request.getLastUpdatedBy();
  }

  public int hashCode() {
    return request.hashCode();
  }

  public void setId(Long id) {
    request.setId(id);
  }

  public void setRequestNumber(String requestNumber) {
    request.setRequestNumber(requestNumber);
  }

  public void setRequestDate(Date requestDate) {
    request.setRequestDate(requestDate);
  }

  public void setRequiredDate(Date requiredDate) {
    request.setRequiredDate(requiredDate);
  }

  public void setRequestedQuantity(Integer requestedQuantity) {
    request.setRequestedQuantity(requestedQuantity);
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    request.setRequestStatus(requestStatus);
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    request.setBloodAbo(bloodAbo);
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    request.setBloodRhd(bloodRhd);
  }

  public void setNotes(String notes) {
    request.setNotes(notes);
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    request.setModificationTracker(modificationTracker);
  }

  public void setProductType(ProductType productType) {
    request.setProductType(productType);
  }

  public void setRequestSite(Location requestSite) {
    request.setRequestSite(requestSite);
  }

  public void setPatientName(String patientName) {
    request.setPatientName(patientName);
  }

  public void setIsDeleted(Boolean isDeleted) {
    request.setIsDeleted(isDeleted);
  }

  public void setIssuedProducts(List<Product> issuedProducts) {
    request.setIssuedProducts(issuedProducts);
  }

  public void setLastUpdated(Date lastUpdated) {
    request.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    request.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    request.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    request.setLastUpdatedBy(lastUpdatedBy);
  }
}
