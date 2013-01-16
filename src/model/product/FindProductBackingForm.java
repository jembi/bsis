package model.product;

import java.util.List;

public class FindProductBackingForm {

  private String searchBy;
  private String productNumber;
  private String collectionNumber;

  private List<String> productTypes;

  private List<String> available;
  private List<String> quarantined;
  
  private String dateExpiresFrom;
  private String dateExpiresTo;

  public String getSearchBy() {
    return searchBy;
  }
  public String getProductNumber() {
    return productNumber;
  }
  public String getCollectionNumber() {
    return collectionNumber;
  }
  public List<String> getProductTypes() {
    return productTypes;
  }
  public String getDateExpiresFrom() {
    return dateExpiresFrom;
  }
  public String getDateExpiresTo() {
    return dateExpiresTo;
  }
  public void setSearchBy(String searchBy) {
    this.searchBy = searchBy;
  }
  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }
  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }
  public void setProductTypes(List<String> productTypes) {
    this.productTypes = productTypes;
  }
  public void setDateExpiresFrom(String dateExpiresFrom) {
    this.dateExpiresFrom = dateExpiresFrom;
  }
  public void setDateExpiresTo(String dateExpiresTo) {
    this.dateExpiresTo = dateExpiresTo;
  }

  public List<String> getAvailable() {
    return available;
  }

  public void setAvailable(List<String> available) {
    this.available = available;
  }

  public List<String> getQuarantined() {
    return quarantined;
  }

  public void setQuarantined(List<String> quarantined) {
    this.quarantined = quarantined;
  }

}