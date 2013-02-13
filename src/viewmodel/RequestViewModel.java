package viewmodel;

import java.util.Date;
import java.util.List;

import model.location.Location;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.request.Request;
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

  public Date getRequestDate() {
    return request.getRequestDate();
  }

  public Date getRequiredDate() {
    return request.getRequiredDate();
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
}
