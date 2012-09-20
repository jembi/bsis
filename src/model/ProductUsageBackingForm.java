package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ProductUsageBackingForm {

  private ProductUsage productUsage;
  private List<String> useIndications;
  private String dateUsedFrom;
  private String dateUsedTo;

  public ProductUsageBackingForm() {
    productUsage = new ProductUsage();
  }

  public ProductUsageBackingForm(ProductUsage productUsage) {
    this.productUsage = productUsage;
  }

  public void setDateUsed(String dateRequested) {
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      productUsage.setDateUsed(dateFormat.parse(dateRequested));
    } catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void setIsDeleted(Boolean isDeleted) {
    productUsage.setIsDeleted(isDeleted);
  }

  public String getDateUsed() {
    Date dateUsed = productUsage.getDateUsed();
    if (dateUsed == null)
      return null;
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    return dateFormat.format(dateUsed);
  }

  public String getDateUsedFrom() {
    return dateUsedFrom;
  }

  public void setDateUsedFrom(String dateUsedFrom) {
    this.dateUsedFrom = dateUsedFrom;
  }

  public String getDateUsedTo() {
    return dateUsedTo;
  }

  public void setDateUsedTo(String dateUsedTo) {
    this.dateUsedTo = dateUsedTo;
  }

  public List<String> getUseIndications() {
    return useIndications;
  }

  public void setUseIndications(List<String> useIndications) {
    this.useIndications = useIndications;
  }

  public void copy(ProductUsage productUsage) {
    productUsage.copy(productUsage);
  }

  public Long getUsageId() {
    return productUsage.getUsageId();
  }

  public String getProductNumber() {
    return productUsage.getProductNumber();
  }

  public String getDateUsedString() {
    return productUsage.getDateUsedString();
  }

  public String getHospital() {
    return productUsage.getHospital();
  }

  public String getWard() {
    return productUsage.getWard();
  }

  public String getUseIndication() {
    return productUsage.getUseIndication();
  }

  public void setProductNumber(String productNumber) {
    productUsage.setProductNumber(productNumber);
  }

  public void setDateUsed(Date dateUsed) {
    productUsage.setDateUsed(dateUsed);
  }

  public void setHospital(String hospital) {
    productUsage.setHospital(hospital);
  }

  public void setWard(String ward) {
    productUsage.setWard(ward);
  }

  public void setDeleted(Boolean deleted) {
    productUsage.setIsDeleted(deleted);
  }

  public void setUseIndication(String useIndication) {
    productUsage.setUseIndication(useIndication);
  }

  public ProductUsage getUsage() {
    return productUsage;
  }
}
