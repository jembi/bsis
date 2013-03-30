package model.usage;

import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.CustomDateFormatter;
import model.modificationtracker.RowModificationTracker;
import model.product.Product;
import model.producttype.ProductType;
import model.user.User;

import org.apache.commons.lang3.StringUtils;

public class ProductUsageBackingForm {

  public static final int ID_LENGTH = 12;

  @NotNull
  @Valid
  private ProductUsage usage;

  private String collectionNumber;

  private String usageDate;

  public ProductUsageBackingForm() {
    usage = new ProductUsage();
  }

  public ProductUsageBackingForm(ProductUsage usage) {
    this.usage = usage;
  }

  public ProductUsage getUsage() {
    return usage;
  }

  public void setUsage(ProductUsage usage) {
    this.usage = usage;
  }

  public Date getLastUpdated() {
    return usage.getLastUpdated();
  }

  public Date getCreatedDate() {
    return usage.getCreatedDate();
  }

  public User getCreatedBy() {
    return usage.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return usage.getLastUpdatedBy();
  }

  public Long getId() {
    return usage.getId();
  }

  public String getHospital() {
    return usage.getHospital();
  }

  public String getPatientName() {
    return usage.getPatientName();
  }

  public String getWard() {
    return usage.getWard();
  }

  public String getUseIndication() {
    return usage.getUseIndication();
  }

  public String getUsageDate() {
    if (usageDate != null)
      return usageDate;
    if (usage == null)
      return "";
    return CustomDateFormatter.getDateTimeString(usage.getUsageDate());
  }

  public String getNotes() {
    return usage.getNotes();
  }

  public Product getProduct() {
    return usage.getProduct();
  }

  public RowModificationTracker getModificationTracker() {
    return usage.getModificationTracker();
  }

  public Boolean getIsDeleted() {
    return usage.getIsDeleted();
  }

  public int hashCode() {
    return usage.hashCode();
  }

  public void setLastUpdated(Date lastUpdated) {
    usage.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    usage.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    usage.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    usage.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setId(Long id) {
    usage.setId(id);
  }

  public void setHospital(String hospital) {
    usage.setHospital(hospital);
  }

  public void setPatientName(String patientName) {
    usage.setPatientName(patientName);
  }

  public void setWard(String ward) {
    usage.setWard(ward);
  }

  public void setUseIndication(String useIndication) {
    usage.setUseIndication(useIndication);
  }

  public void setUsageDate(String usageDate) {
    this.usageDate = usageDate;
    try {
      usage.setUsageDate(CustomDateFormatter.getDateTimeFromString(usageDate));
    } catch (ParseException ex) {
      ex.printStackTrace();
      usage.setUsageDate(null);
    }
  }

  public void setNotes(String notes) {
    usage.setNotes(notes);
  }

  public void setProduct(Product product) {
    usage.setProduct(product);
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    usage.setModificationTracker(modificationTracker);
  }

  public void setIsDeleted(Boolean isDeleted) {
    usage.setIsDeleted(isDeleted);
  }

  public String getCollectionNumber() {
    return collectionNumber;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public String getProductType() {
    ProductType productType = usage.getProductType();
    if (productType == null)
      return "";
    else
      return productType.getId().toString();
  }

  public void setProductType(String productTypeId) {
    if (StringUtils.isBlank(productTypeId)) {
      usage.setProductType(null);
    }
    else {
      ProductType pt = new ProductType();
      try {
        pt.setId(Integer.parseInt(productTypeId));
        usage.setProductType(pt);
      } catch (Exception ex) {
        ex.printStackTrace();
        usage.setProductType(null);
      }
    }
  }

}