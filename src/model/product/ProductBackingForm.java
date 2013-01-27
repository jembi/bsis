package model.product;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.CustomDateFormatter;
import model.collectedsample.CollectedSample;
import model.producttype.ProductType;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodGroup;
import model.util.BloodRhd;

import org.apache.commons.lang3.RandomStringUtils;

public class ProductBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private Product product;

  private String createdOn;

  private String expiresOn;

  private List<String> productTypes;

  private List<String> bloodGroups;

  public ProductBackingForm() {
    setProduct(new Product());
  }

  public ProductBackingForm(boolean autoGenerate) {
    setProduct(new Product());
    if (autoGenerate)
      generateProductNumber();
  }

  public ProductBackingForm(Product product) {
    this.setProduct(product);
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

  public String getProductType() {
    ProductType productType = product.getProductType();
    if (productType == null)
      return "";
    else
      return productType.getProductType();
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

  public Boolean getIsQuarantined() {
    return product.getIsQuarantined();
  }

  public BloodAbo getBloodAbo() {
    return product.getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return product.getBloodRhd();
  }

  public int hashCode() {
    return product.hashCode();
  }

  public void setId(Long id) {
    product.setId(id);
  }

  public void setProductNumber(String productNumber) {
    product.setProductNumber(productNumber);
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    product.setCollectedSample(collectedSample);
  }

  public void setProductType(String productType) {
    if (productType == null) {
      product.setProductType(null);
    }
    else {
      ProductType pt = new ProductType();
      pt.setProductType(productType);
      product.setProductType(pt);
    }
  }

  public String getCreatedOn() {
    if (createdOn != null)
      return createdOn;
    if (getProduct() == null)
      return "";
    return CustomDateFormatter.getDateString(product.getCreatedOn());
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

  public void setIsQuarantined(Boolean isQuarantined) {
    product.setIsQuarantined(isQuarantined);
  }

  public void setCreatedOn(String createdOn) {
    this.createdOn = createdOn;
    try {
      product.setCreatedOn(CustomDateFormatter.getDateFromString(createdOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      product.setCreatedOn(null);
    }
  }

  public void setExpiresOn(String expiresOn) {
    this.expiresOn = expiresOn;
    try {
      product.setExpiresOn(CustomDateFormatter.getDateFromString(expiresOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      product.setExpiresOn(null);
    }
  }

  public void setBloodAbo(BloodAbo bloodAbo) {
    product.setBloodAbo(bloodAbo);
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    product.setBloodRhd(bloodRhd);
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

  public void generateProductNumber() {
    String uniqueProductNumber;
    uniqueProductNumber = "P-" +
                        RandomStringUtils.randomNumeric(ID_LENGTH).toUpperCase();
    product.setProductNumber(uniqueProductNumber);
  }

  public String getCollectionNumber() {
    if (product == null || product.getCollectedSample() == null ||
        product.getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return product.getCollectedSample().getCollectionNumber();
  }

  public void setCollectionNumber(String collectionNumber) {
    CollectedSample collectedSample = new CollectedSample();
    collectedSample.setCollectionNumber(collectionNumber);
    product.setCollectedSample(collectedSample);
  }

  public String getBloodGroup() {
    return new BloodGroup(product.getBloodAbo(), product.getBloodRhd()).toString();
  }

  public void setBloodGroup(String bloodGroupStr) {
    BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
    product.setBloodAbo(bloodGroup.getBloodAbo());
    product.setBloodRhd(bloodGroup.getBloodRhd());
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Boolean getIsAvailable() {
    return product.getIsAvailable();
  }

  public void setIsAvailable(Boolean isAvailable) {
    product.setIsAvailable(isAvailable);
  }

  public List<String> getBloodGroups() {
    return bloodGroups;
  }

  public void setBloodGroups(List<String> bloodGroups) {
    this.bloodGroups = bloodGroups;
  }

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