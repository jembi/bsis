package model.request;

import java.text.ParseException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

  @NotNull
  @Valid
  private Request request;

  private String requestDate;
  private String requiredDate;

  public RequestBackingForm() {
    setRequest(new Request());
  }

  public RequestBackingForm(Request request) {
    this.setRequest(request);
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

  public String getRequestSite() {
    Location site = request.getRequestSite();
    if (site == null || site.getId() == null)
      return null;
    return site.getId().toString();
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

  public void setBloodAbo(BloodAbo bloodAbo) {
    getRequest().setBloodAbo(bloodAbo);
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    getRequest().setBloodRhd(bloodRhd);
  }

  public void setNotes(String notes) {
    getRequest().setNotes(notes);
  }

  public void setProductType(String productType) {
    if (productType == null) {
      request.setProductType(null);
    }
    else {
      ProductType pt = new ProductType();
      pt.setProductType(productType);
      request.setProductType(pt);
    }
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

  public void setRequestSite(String requestSite) {
    if (requestSite == null) {
      request.setRequestSite(null);
    }
    else {
      Location l = new Location();
      l.setId(Long.parseLong(requestSite));
      request.setRequestSite(l);
    }
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
    if (request.getBloodAbo() == null || request.getBloodRhd() == null)
      return null;
    return new BloodGroup(request.getBloodAbo(), request.getBloodRhd()).toString();
  }

  public void setBloodGroup(String bloodGroupStr) {

    if (bloodGroupStr == null || bloodGroupStr.isEmpty()) {
      request.setBloodAbo(null);
      request.setBloodRhd(null);
    } else {
      BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
      request.setBloodAbo(bloodGroup.getBloodAbo());
      request.setBloodRhd(bloodGroup.getBloodRhd());
    }
  }
}
