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

  public String getBloodGroup() {
    return new BloodGroup(request.getBloodAbo(), request.getBloodRhd()).toString();
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
