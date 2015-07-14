package backingform;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.donation.Donation;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;
import model.user.User;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.CustomDateFormatter;

public class ProductBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private Product product;

  private String createdOn;

  private String expiresOn;

  private List<String> productTypes;

  public ProductBackingForm() {
    setProduct(new Product());
  }

  public ProductBackingForm(boolean autoGenerate) {
    setProduct(new Product());
  }

  public ProductBackingForm(Product product) {
    this.setProduct(product);
  }

  public Long getId() {
    return product.getId();
  }

  public Donation getDonation() {
    return product.getDonation();
  }

  public String getProductType() {
    ProductType productType = product.getProductType();
    if (productType == null)
      return "";
    else
      return productType.getId().toString();
  }
  
  @JsonIgnore
  public Date getLastUpdated() {
    return product.getLastUpdated();
  }

  @JsonIgnore
  public Date getCreatedDate() {
    return product.getCreatedDate();
  }

  @JsonIgnore
  public User getCreatedBy() {
    return product.getCreatedBy();
  }

  @JsonIgnore
  public User getLastUpdatedBy() {
    return product.getLastUpdatedBy();
  }

  public String getNotes() {
    return product.getNotes();
  }

  public Boolean getIsDeleted() {
    return product.getIsDeleted();
  }

  public int hashCode() {
    return product.hashCode();
  }

  public void setId(Long id) {
    product.setId(id);
  }

  public void setDonation(Donation donation) {
    product.setDonation(donation);
  }

  public void setProductType(String productTypeId) {
    if (StringUtils.isBlank(productTypeId)) {
      product.setProductType(null);
    }
    else {
      ProductType pt = new ProductType();
      try {
        pt.setId(Integer.parseInt(productTypeId));
        product.setProductType(pt);
      } catch (Exception ex) {
        ex.printStackTrace();
        product.setProductType(null);
      }
    }
  }

  public String getCreatedOn() {
    if (createdOn != null)
      return createdOn;
    if (getProduct() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(product.getCreatedOn());
  }

  public String getExpiresOn() {
    if (expiresOn != null)
      return expiresOn;
    if (getProduct() == null)
      return "";
    return CustomDateFormatter.getDateString(product.getExpiresOn());
  }
  
  public void setLastUpdated(Date lastUpdated) {
    product.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    product.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    product.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    product.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    product.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    product.setIsDeleted(isDeleted);
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
    try {
      product.setCreatedOn(CustomDateFormatter.getDateTimeFromString(createdOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      product.setCreatedOn(null);
    }
  }

  public void setExpiresOn(String expiresOn) {
    this.expiresOn = expiresOn;
    try {
      product.setExpiresOn(CustomDateFormatter.getDateTimeFromString(expiresOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      product.setExpiresOn(null);
    }
  }

  public String toString() {
    return product.toString();
  }

  public List<String> getProductTypes() {
    return productTypes;
  }

  public void setProductTypes(List<String> productTypes) {
    this.productTypes = productTypes;
  }

  public String getCollectionNumber() {
    if (product == null || product.getDonation() == null ||
        product.getDonation().getCollectionNumber() == null
       )
      return "";
    return product.getDonation().getCollectionNumber();
  }

  public void setCollectionNumber(String collectionNumber) {
    Donation donation = new Donation();
    donation.setCollectionNumber(collectionNumber);
    product.setDonation(donation);
  }

  @JsonIgnore
  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  @JsonIgnore
  public String getStatus() {
    ProductStatus status = product.getStatus();
    if (status == null)
      return "";
    else
      return product.getStatus().toString();
  }

  public void setStatus(String status) {
    product.setStatus(ProductStatus.valueOf(status));
  }
}