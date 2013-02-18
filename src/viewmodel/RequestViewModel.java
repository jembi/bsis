package viewmodel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import model.CustomDateFormatter;
import model.location.Location;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.request.Request;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;
import model.util.Gender;

public class RequestViewModel {
	private Request request;

  public RequestViewModel(Request productRequest) {
    this.request = productRequest;
  }

  public Long getId() {
    return request.getId();
  }

  public String getRequestNumber() {
    return request.getRequestNumber();
  }

  public String getRequestDate() {
    if (request.getRequestDate() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(request.getRequestDate());
  }

  public String getRequiredDate() {
    if (request.getRequiredDate() == null)
      return "";
    return CustomDateFormatter.getDateString(request.getRequiredDate());
  }

  public Integer getRequestedQuantity() {
    return request.getRequestedQuantity();
  }

  public BloodAbo getPatientBloodAbo() {
    return request.getPatientBloodAbo();
  }

  public BloodRhd getPatientBloodRhd() {
    return request.getPatientBloodRhd();
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

  public String getBloodGroup() {
    return new BloodGroup(request.getPatientBloodAbo(), request.getPatientBloodRhd()).toString();
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

  public Integer getIssuedQuantity() {
    if (request == null || request.getIssuedProducts() == null)
      return 0;
    return request.getIssuedProducts().size();
  }

  public String getPatientFirstName() {
    return request.getPatientFirstName();
  }

  public String getPatientLastName() {
    return request.getPatientLastName();
  }

  public String getPatientBirthDate() {
    if (request.getPatientBirthDate() == null)
      return ""; 
    return CustomDateFormatter.getDateString(request.getPatientBirthDate());
  }

  public String getPatientGender() {
    if (request == null || request.getPatientGender() == null)
      return null;
    return request.getPatientGender().toString();
  }

  public Integer getPatientAge() {
    return request.getPatientAge();
  }

  public String getPatientBloodGroup() {
    return new BloodGroup(request.getPatientBloodAbo(), request.getPatientBloodRhd()).toString();
  }

  public String getPatientDiagnosis() {
    return request.getPatientDiagnosis();
  }

  public String getRequestedBy() {
    return request.getRequestedBy();
  }

  public Integer getVolume() {
    return request.getVolume();
  }

  public String getWard() {
    return request.getWard();
  }

  public String getHospital() {
    return request.getHospital();
  }

  public String getDepartment() {
    return request.getDepartment();
  }

  public String getPatientNumber() {
    return request.getPatientNumber();
  }
}
