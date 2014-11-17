package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import java.util.Date;

import model.collectedsample.CollectedSample;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;
import model.user.User;

import org.joda.time.DateTime;
import org.joda.time.Days;

import utils.CustomDateFormatter;

public class ProductViewModel {

 @JsonIgnore
  private Product product;

  public ProductViewModel() {
  }

  public ProductViewModel(Product product) {
    this.product = product;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Long getId() {
    return product.getId();
  }

  @JsonIgnore
  public CollectedSample getCollectedSample() {
    return product.getCollectedSample();
  }

  public ProductTypeViewModel getProductType() {
    return new ProductTypeViewModel(product.getProductType());
  }
  
  public String getNotes() {
    return product.getNotes();
  }

  public Boolean getIsDeleted() {
    return product.getIsDeleted();
  }

  public String getCreatedOn() {
    if (product.getCreatedOn() == null)
      return ""; 
    return CustomDateFormatter.getDateTimeString(product.getCreatedOn());
  }

  public String getExpiresOn() {
    if (product.getExpiresOn() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(product.getExpiresOn());
  }

  public String getCollectionNumber() {
    if (getProduct() == null || getProduct().getCollectedSample() == null ||
        getProduct().getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return getProduct().getCollectedSample().getCollectionNumber();
  }
  
  public String getCollectedSampleID() {
    if (getProduct() == null || getProduct().getCollectedSample() == null ||
        getProduct().getCollectedSample().getId() == null
       )
      return "";
    return getProduct().getCollectedSample().getId().toString();
  }

  public String getAge() {
    DateTime today = new DateTime();
    DateTime createdOn = new DateTime(product.getCreatedOn().getTime());
    Long age = (long) Days.daysBetween(createdOn, today).getDays();
    return age + " days old";
  }

  public String getStatus() {
    return product.getStatus().toString();
  }

  public String getExpiryStatus() {
    Date today = new Date();
    if (today.equals(product.getExpiresOn()) || today.before(product.getExpiresOn())) {
      DateTime expiresOn = new DateTime(product.getExpiresOn().getTime());
      Long age = (long) Days.daysBetween(expiresOn, new DateTime()).getDays();
      return Math.abs(age) + " days to expire";
    }
    else {
      return "Already expired";
    }
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(product.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(product.getCreatedDate());
  }

  @JsonIgnore
  public String getCreatedBy() {
    User user = product.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  @JsonIgnore
  public String getLastUpdatedBy() {
    User user = product.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getIssuedOn() {
    return CustomDateFormatter.getDateTimeString(product.getIssuedOn());
  }

  @JsonIgnore
  public RequestViewModel getIssuedTo() {
    ProductStatus status = product.getStatus();
    if (status == null)
      return null;
    else if (!status.equals(ProductStatus.ISSUED))
      return null;
    else
    return new RequestViewModel(product.getIssuedTo());
  }

  public String getDiscardedOn() {
    return CustomDateFormatter.getDateTimeString(product.getDiscardedOn());
  }

  @JsonIgnore
  public String getBloodGroup() {
    if (product == null || product.getCollectedSample() == null ||
        product.getCollectedSample().getCollectionNumber() == null
       )
      return "";
    CollectedSampleViewModel collectionViewModel = new CollectedSampleViewModel(product.getCollectedSample());
    return collectionViewModel.getBloodGroup();
  }

  public String getSubdivisionCode() {
    return product.getSubdivisionCode();
  }

  public Boolean getStatusAllowsSplitting() {
    return Arrays.asList(ProductStatus.AVAILABLE, ProductStatus.QUARANTINED)
                 .contains(product.getStatus());
  }
  
  public String getComponentIdentificationNumber() {
    return product.getComponentIdentificationNumber();
  }
  
}
