package viewmodel;

import java.util.Arrays;
import java.util.Date;

import model.donation.Donation;
import model.product.Product;
import model.product.ProductStatus;
import model.user.User;

import org.joda.time.DateTime;
import org.joda.time.Days;

import utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
  public Donation getDonation() {
    return product.getDonation();
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

  public String getDonationIdentificationNumber() {
    if (getProduct() == null || getProduct().getDonation() == null ||
        getProduct().getDonation().getDonationIdentificationNumber() == null
       )
      return "";
    return getProduct().getDonation().getDonationIdentificationNumber();
  }
  
  public String getDonationID() {
    if (getProduct() == null || getProduct().getDonation() == null ||
        getProduct().getDonation().getId() == null
       )
      return "";
    return getProduct().getDonation().getId().toString();
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
    if (product == null || product.getDonation() == null ||
        product.getDonation().getDonationIdentificationNumber() == null
       )
      return "";
    DonationViewModel collectionViewModel = new DonationViewModel(product.getDonation());
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
