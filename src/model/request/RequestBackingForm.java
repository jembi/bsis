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
import model.requesttype.RequestType;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;
import model.util.Gender;
import repository.RequestRepository;


public class RequestBackingForm {

  @NotNull
  @Valid
  private Request request;

  private String requestDate;
  private String requiredDate;

  private String patientBirthDate;

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

  public BloodAbo getPatientBloodAbo() {
    return request.getPatientBloodAbo();
  }

  public BloodRhd getPatientBloodRhd() {
    return request.getPatientBloodRhd();
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
      return productType.getProductType();
  }

  public String getRequestType() {
    RequestType requestType = request.getRequestType();
    if (requestType == null)
      return "";
    else
      return requestType.getRequestType();
  }


  public String getRequestSite() {
    Location site = request.getRequestSite();
    if (site == null || site.getId() == null)
      return null;
    return site.getId().toString();
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

  public void setPatientBloodAbo(BloodAbo bloodAbo) {
    getRequest().setPatientBloodAbo(bloodAbo);
  }

  public void setPatientBloodRhd(BloodRhd bloodRhd) {
    getRequest().setPatientBloodRhd(bloodRhd);
  }

  public void setNotes(String notes) {
    getRequest().setNotes(notes);
  }

  public void setProductType(String productTypeId) {
    if (productTypeId == null) {
      request.setProductType(null);
    }
    else {
      ProductType pt = new ProductType();
      pt.setId(Integer.parseInt(productTypeId));
      request.setProductType(pt);
    }
  }

  public void setRequestType(String requestTypeId) {
    if (requestTypeId == null) {
      request.setRequestType(null);
    }
    else {
      RequestType rt = new RequestType();
      rt.setId(Integer.parseInt(requestTypeId));
      request.setRequestType(rt);
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
    if (request.getPatientBloodAbo() == null || request.getPatientBloodRhd() == null)
      return null;
    return new BloodGroup(request.getPatientBloodAbo(), request.getPatientBloodRhd()).toString();
  }

  public void setBloodGroup(String bloodGroupStr) {

    if (bloodGroupStr == null || bloodGroupStr.isEmpty()) {
      request.setPatientBloodAbo(null);
      request.setPatientBloodRhd(null);
    } else {
      BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
      request.setPatientBloodAbo(bloodGroup.getBloodAbo());
      request.setPatientBloodRhd(bloodGroup.getBloodRhd());
    }
  }

  public String getPatientNumber() {
    return request.getPatientNumber();
  }

  public void setDonorNumber(String patientNumber) {
    request.setPatientNumber(patientNumber);
  }

  public String getPatientFirstName() {
    return request.getPatientFirstName();
  }

  public void setPatientFirstName(String patientFirstName) {
    request.setPatientFirstName(patientFirstName);
  }

  public String getPatientLastName() {
    return request.getPatientLastName();
  }

  public void setPatientLastName(String patientLastName) {
    request.setPatientLastName(patientLastName);
  }

  public String getPatientBirthDate() {
    if (patientBirthDate != null)
      return patientBirthDate;
    if (request == null)
      return "";
    return CustomDateFormatter.getDateString(request.getPatientBirthDate());
  }

  public void setBirthDate(String patientBirthDate) {
    this.patientBirthDate = patientBirthDate;
    try {
      request.setPatientBirthDate(CustomDateFormatter.getDateFromString(patientBirthDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      request.setPatientBirthDate(null);
    }
  }

  public String getPatientGender() {
    if (request == null || request.getPatientGender() == null)
      return null;
    return request.getPatientGender().toString();
  }

  public void setPatientGender(String patientGender) {
    request.setPatientGender(Gender.valueOf(patientGender));
  }

  public Integer getPatientAge() {
    return request.getPatientAge();
  }

  public void setPatientAge(Integer patientAge) {
    request.setPatientAge(patientAge);
  }

  public void setPatientBloodGroup(String patientBloodGroupStr) {
    BloodGroup bloodGroup = new BloodGroup(patientBloodGroupStr);
    request.setPatientBloodAbo(bloodGroup.getBloodAbo());
    request.setPatientBloodRhd(bloodGroup.getBloodRhd());
  }

  public String getPatientBloodGroup() {
    return new BloodGroup(request.getPatientBloodAbo(), request.getPatientBloodRhd()).toString();
  }

  public String getPatientDiagnosis() {
    return request.getPatientDiagnosis();
  }

  public void setPatientDiagnosis(String patientDiagnosis) {
    request.setPatientDiagnosis(patientDiagnosis);
  }

  public String getRequestedBy() {
    return request.getRequestedBy();
  }

  public void setRequestedBy(String requestedBy) {
    request.setRequestedBy(requestedBy);
  }

  public Integer getVolume() {
    return request.getVolume();
  }

  public void setVolume(Integer volume) {
    request.setVolume(volume);
  }

  public String getWard() {
    return request.getWard();
  }

  public void setWard(String ward) {
    request.setWard(ward);
  }

  public String getHospital() {
    return request.getHospital();
  }

  public void setHospital(String hospital) {
    request.setHospital(hospital);
  }

  public String getDepartment() {
    return request.getDepartment();
  }

  public void setDepartment(String department) {
    request.setDepartment(department);
  }
}
