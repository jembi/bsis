package viewmodel;

import java.util.List;

import model.CustomDateFormatter;
import model.location.Location;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.request.Request;
import model.requesttype.RequestType;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

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

  public Integer getNumUnitsRequested() {
    return request.getNumUnitsRequested();
  }

  public Integer getNumUnitsIssued() {
    return request.getNumUnitsIssued();
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

  public String getBloodGroup() {
    return new BloodGroup(request.getPatientBloodAbo(), request.getPatientBloodRhd()).toString();
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

  public RequestType getRequestType() {
    return request.getRequestType();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(request.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(request.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = request.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = request.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }
}
