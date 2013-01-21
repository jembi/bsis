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
    return getProduct().getId();
  }

  public String getProductNumber() {
    return getProduct().getProductNumber();
  }

  public CollectedSample getCollectedSample() {
    return getProduct().getCollectedSample();
  }

  public String getProductType() {
    ProductType productType = product.getProductType();
    if (productType == null)
      return "";
    else
      return productType.getProductType();
  }

  public Date getLastUpdated() {
    return getProduct().getLastUpdated();
  }

  public Date getCreatedDate() {
    return getProduct().getCreatedDate();
  }

  public User getCreatedBy() {
    return getProduct().getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return getProduct().getLastUpdatedBy();
  }

  public String getNotes() {
    return getProduct().getNotes();
  }

  public Boolean getIsDeleted() {
    return getProduct().getIsDeleted();
  }

  public Boolean getIsQuarantined() {
    return getProduct().getIsQuarantined();
  }

  public BloodAbo getBloodAbo() {
    return getProduct().getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return getProduct().getBloodRhd();
  }

  public int hashCode() {
    return getProduct().hashCode();
  }

  public void setId(Long id) {
    getProduct().setId(id);
  }

  public void setProductNumber(String productNumber) {
    getProduct().setProductNumber(productNumber);
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    getProduct().setCollectedSample(collectedSample);
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
    return CustomDateFormatter.getDateString(getProduct().getCreatedOn());
  }

  public String getExpiresOn() {
    if (expiresOn != null)
      return expiresOn;
    if (getProduct() == null)
      return "";
    return CustomDateFormatter.getDateString(getProduct().getExpiresOn());
  }

  public void setLastUpdated(Date lastUpdated) {
    getProduct().setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    getProduct().setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    getProduct().setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    getProduct().setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    getProduct().setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    getProduct().setIsDeleted(isDeleted);
  }

  public void setIsQuarantined(Boolean isQuarantined) {
    getProduct().setIsQuarantined(isQuarantined);
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
    getProduct().setBloodAbo(bloodAbo);
  }

  public void setBloodRhd(BloodRhd bloodRhd) {
    getProduct().setBloodRhd(bloodRhd);
  }

  public String toString() {
    return getProduct().toString();
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
    getProduct().setProductNumber(uniqueProductNumber);
  }

  public String getCollectionNumber() {
    if (getProduct() == null || getProduct().getCollectedSample() == null ||
        getProduct().getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return getProduct().getCollectedSample().getCollectionNumber();
  }

  public void setCollectionNumber(String collectionNumber) {
    CollectedSample collectedSample = new CollectedSample();
    collectedSample.setCollectionNumber(collectionNumber);
    getProduct().setCollectedSample(collectedSample);
  }

  public String getBloodGroup() {
    return new BloodGroup(getProduct().getBloodAbo(), getProduct().getBloodRhd()).toString();
  }

  public void setBloodGroup(String bloodGroupStr) {
    BloodGroup bloodGroup = new BloodGroup(bloodGroupStr);
    getProduct().setBloodAbo(bloodGroup.getBloodAbo());
    getProduct().setBloodRhd(bloodGroup.getBloodRhd());
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
}