package backingform;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.collectedsample.CollectedSample;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductType;
import model.user.User;

import org.apache.commons.lang3.StringUtils;

import utils.CustomDateFormatter;

public class ProductBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private Product product;

  private String createdOn;

  private String expiresOn;

  private List<String> productTypes;

  private BigDecimal unitWeight;
  
  CollectedSample collectedSample = new CollectedSample();
  
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

  public CollectedSample getCollectedSample() {
    return product.getCollectedSample();
  }

  public String getProductType() {
    ProductType productType = product.getProductType();
    if (productType == null)
      return "";
    else
      return productType.getId().toString();
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

  public int hashCode() {
    return product.hashCode();
  }

  public void setId(Long id) {
    product.setId(id);
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    product.setCollectedSample(collectedSample);
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
    return CustomDateFormatter.getDateTimeString(product.getExpiresOn());
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
    if (product == null || product.getCollectedSample() == null ||
        product.getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return product.getCollectedSample().getCollectionNumber();
  }

  public void setCollectionNumber(String collectionNumber) {
    collectedSample.setCollectionNumber(collectionNumber);
    product.setCollectedSample(collectedSample);
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
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

  public BigDecimal getUnitWeight() {
  	if (product == null || product.getCollectedSample() == null ||
        product.getCollectedSample().getCollectionNumber() == null
       )
      return BigDecimal.valueOf(0);
    return product.getCollectedSample().getUnitWeight();
  }

  public void setUnitWeight(BigDecimal unitWeight) {
    collectedSample.setUnitWeight(unitWeight);
    product.setCollectedSample(collectedSample);
  }
}