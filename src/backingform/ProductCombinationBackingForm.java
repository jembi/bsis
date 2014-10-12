package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.collectedsample.CollectedSample;
import model.product.Product;
import model.product.ProductStatus;
import model.producttype.ProductTypeCombination;
import model.user.User;

import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.CustomDateFormatter;

public class ProductCombinationBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  @JsonIgnore
  private Product product;

  private String createdOn;

  private String expiresOn;

  private ProductTypeCombination productTypeCombination;

  Map<String, String> expiresOnByProductTypeId;
  
  public ProductCombinationBackingForm() {
    expiresOnByProductTypeId = new HashMap<String, String>();
    setProduct(new Product());
  }

  public ProductCombinationBackingForm(boolean autoGenerate) {
    expiresOnByProductTypeId = new HashMap<String, String>();
    setProduct(new Product());
  }

  public ProductCombinationBackingForm(Product product) {
    this.setProduct(product);
  }

  public Long getId() {
    return product.getId();
  }

  @JsonIgnore
  public CollectedSample getCollectedSample() {
    return product.getCollectedSample();
  }
  
  public Date getLastUpdated() {
    return product.getLastUpdated();
  }

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

  public void setCollectedSample(CollectedSample collectedSample) {
    product.setCollectedSample(collectedSample);
  }

  public String getCreatedOn() {
    if (createdOn != null)
      return createdOn;
    if (getProduct() == null)
      return "";
    return CustomDateFormatter.getDateTimeString(product.getCreatedOn());
  }

  public String getExpiresOn() {
    return expiresOn;
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

  @SuppressWarnings("unchecked")
  public void setExpiresOn(String expiresOn) {
    this.expiresOn = expiresOn;
    ObjectMapper mapper = new ObjectMapper();
    try {
      expiresOnByProductTypeId = mapper.readValue(expiresOn, HashMap.class);
    } catch (JsonParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String toString() {
    return product.toString();
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

  public String getProductTypeCombination() {
    if (productTypeCombination == null || productTypeCombination.getId() == null)
      return "";
    else
      return productTypeCombination.getId().toString();
  }

  public void setProductTypeCombination(String productTypeCombinationId) {
    if (StringUtils.isBlank(productTypeCombinationId)) {
      productTypeCombination = null;
    }
    else {
      productTypeCombination = new ProductTypeCombination();
      try {
        productTypeCombination.setId(Integer.parseInt(productTypeCombinationId));
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}