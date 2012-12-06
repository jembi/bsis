package model.request;

import java.text.ParseException;
import java.util.List;

import model.CustomDateFormatter;
import model.location.Location;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;
import repository.RequestRepository;


public class RequestBackingForm {

  private Request request;
  private String requestDate;
  private String requiredDate;

  public RequestBackingForm() {
    setRequest(new Request());
  }

  public RequestBackingForm(Request request) {
    this.setRequest(request);
  }

  public RequestBackingForm(boolean autoGenerate) {
    setRequest(new Request());
    if (autoGenerate)
      generateRequestNumber();
  }

  public Long getId() {
    return getRequest().getId();
  }

  public String getRequestNumber() {
    return getRequest().getRequestNumber();
  }

  public String getRequestDate() {
    if (requestDate != null)
      return requestDate;
    if (getRequest() == null)
      return "";
    return CustomDateFormatter.getDateString(getRequest().getRequestDate());
  }

  public String getRequiredDate() {
    if (requiredDate != null)
      return requiredDate;
    if (getRequest() == null)
      return "";
    return CustomDateFormatter.getDateString(getRequest().getRequiredDate());
  }

  public Integer getRequestedQuantity() {
    return getRequest().getRequestedQuantity();
  }

  public RequestStatus getRequestStatus() {
    return getRequest().getRequestStatus();
  }

  public BloodAbo getBloodAbo() {
    return getRequest().getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return getRequest().getBloodRhd();
  }

  public String getNotes() {
    return getRequest().getNotes();
  }

  public RowModificationTracker getModificationTracker() {
    return getRequest().getModificationTracker();
  }

  public String getProductType() {
    ProductType productType = getRequest().getProductType();
    if (productType == null)
      return "";
    else
      return productType.toString();
  }

  public Location getRequestSite() {
    return getRequest().getRequestSite();
  }

  public String getPatientName() {
    return getRequest().getPatientName();
  }

  public Boolean getIsDeleted() {
    return getRequest().getIsDeleted();
  }

  public void setId(Long id) {
    getRequest().setId(id);
  }

  public void setRequestNumber(String requestNumber) {
    getRequest().setRequestNumber(requestNumber);
  }

  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
    try {
      getRequest().setRequestDate(CustomDateFormatter.getDateFromString(requestDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      getRequest().setRequestDate(null);
    }
  }

  public void setRequiredDate(String requiredDate) {
    this.requiredDate = requiredDate;
    try {
      getRequest().setRequiredDate(CustomDateFormatter.getDateFromString(requiredDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      getRequest().setRequiredDate(null);
    }
  }

  public void setRequestedQuantity(Integer requestedQuantity) {
    getRequest().setRequestedQuantity(requestedQuantity);
  }

  public void setRequestStatus(RequestStatus requestStatus) {
    getRequest().setRequestStatus(requestStatus);
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    getRequest().setBloodAbo(bloodAbo);
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    getRequest().setBloodRhd(bloodRhd);
  }

  public void setNotes(String notes) {
    getRequest().setNotes(notes);
  }

  public void setProductType(ProductType productType) {
    getRequest().setProductType(productType);
  }

  public void setIsDeleted(Boolean isDeleted) {
    getRequest().setIsDeleted(isDeleted);
  }

  public List<Product> getIssuedProducts() {
    return getRequest().getIssuedProducts();
  }

  public int hashCode() {
    return getRequest().hashCode();
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    getRequest().setModificationTracker(modificationTracker);
  }

  public void setRequestSite(Location requestSite) {
    getRequest().setRequestSite(requestSite);
  }

  public void setPatientName(String patientName) {
    getRequest().setPatientName(patientName);
  }

  public void setIssuedProducts(List<Product> issuedProducts) {
    getRequest().setIssuedProducts(issuedProducts);
  }

  public void generateRequestNumber() {
    getRequest().setRequestNumber(RequestRepository.generateUniqueRequestNumber());
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public String getBloodGroup() {
    return new BloodGroup(request.getBloodAbo(), request.getBloodRhd()).toString();
  }

  public void setBloodGroup(String bloodGroupStr) {
    BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
    request.setBloodAbo(bloodGroup.getBloodAbo());
    request.setBloodRhd(bloodGroup.getBloodRhd());
  }
}
