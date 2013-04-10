package viewmodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.collectedsample.CollectedSample;
import model.compatibility.CompatibilityResult;
import model.compatibility.CompatibilityTest;
import model.product.Product;
import model.producttype.ProductType;
import model.user.User;

import org.joda.time.DateTime;
import org.joda.time.Days;

public class MatchingProductViewModel {

	private Product product;
	private CompatibilityTest compatibilityTest;

	public MatchingProductViewModel(Product product) {
	  this.product = product;
	}

  public MatchingProductViewModel(Product product,
      CompatibilityTest crossmatchTest) {
    this.product = product;
    this.compatibilityTest = crossmatchTest;
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

  public CollectedSample getCollectedSample() {
    return product.getCollectedSample();
  }

  public ProductType getProductType() {
    return product.getProductType();
  }

  public Date getLastUpdated() {
    return product.getLastUpdated();
  }

  public Date getCreatedDate() {
    return product.getCreatedDate();
  }

  public User getCreatedBy() {
    return product.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return product.getLastUpdatedBy();
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
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(product.getCreatedOn());
  }

  public String getExpiresOn() {
    if (product.getExpiresOn() == null)
      return "";
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(product.getExpiresOn());
  }

  public String getCollectionNumber() {
    if (getProduct() == null || getProduct().getCollectedSample() == null ||
        getProduct().getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return getProduct().getCollectedSample().getCollectionNumber();
  }

  public Long getAge() {
    DateTime today = new DateTime();
    DateTime createdOn = new DateTime(product.getCreatedOn().getTime());
    Long age = (long) Days.daysBetween(createdOn, today).getDays();
    return age;
  }

  public String getIsCompatible() {
    if (compatibilityTest == null)
      return CompatibilityResult.NOT_KNOWN.toString();
    return compatibilityTest.getCompatibilityResult().toString();
  }
}
