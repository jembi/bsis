package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.CustomDateFormatter;
import model.collectedsample.CollectedSample;
import model.product.Product;
import model.producttype.ProductType;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class ProductViewModel {

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

  public String getProductNumber() {
    return product.getProductNumber();
  }

  public CollectedSample getCollectedSample() {
    return product.getCollectedSample();
  }

  public ProductType getProductType() {
    return product.getProductType();
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
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(product.getExpiresOn());
  }

  public BloodAbo getBloodAbo() {
    return product.getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return product.getBloodRhd();
  }

  public String getBloodGroup() {
    return new BloodGroup(product.getBloodAbo(), product.getBloodRhd()).toString();
  }

  public String getCollectionNumber() {
    if (getProduct() == null || getProduct().getCollectedSample() == null ||
        getProduct().getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return getProduct().getCollectedSample().getCollectionNumber();
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

  public String getCreatedBy() {
    User user = product.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = product.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getIssuedOn() {
    return CustomDateFormatter.getDateTimeString(product.getIssuedOn());
  }

  public RequestViewModel getIssuedTo() {
    if (product.getIssuedTo() == null)
      return null;
    else
      return new RequestViewModel(product.getIssuedTo());
  }

  public String getDiscardedOn() {
    return CustomDateFormatter.getDateTimeString(product.getDiscardedOn());
  }
}
