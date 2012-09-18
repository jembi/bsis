package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ProductBackingForm {
  private Product product;
  private List<String> types;

  public ProductBackingForm() {
    product = new Product();
    types = new ArrayList<String>();
  }

  public ProductBackingForm(Product product) {
    this.product = product;
  }

  public void copy(Product product) {
    product.copy(product);
  }

  public boolean equals(Object obj) {
    return product.equals(obj);
  }

  public String getAbo() {
    return product.getAbo();
  }

  public String getCollectionNumber() {
    return product.getCollectionNumber();
  }

  public String getComments() {
    return product.getComments();
  }

  public Date getDateCollected() {
    return product.getDateCollected();
  }

  public String getDateCollectedString() {
    return product.getDateCollectedString();
  }

  public Boolean getIssued() {
    return product.getIssued();
  }

  public Long getProductId() {
    return product.getProductId();
  }

  public String getProductNumber() {
    return product.getProductNumber();
  }

  public String getRhd() {
    return product.getRhd();
  }

  public String getType() {
    return product.getType();
  }

  public int hashCode() {
    return product.hashCode();
  }

  public void setAbo(String abo) {
    product.setAbo(abo);
  }

  public void setCollectionNumber(String collectionNumber) {
    product.setCollectionNumber(collectionNumber);
  }

  public void setComments(String comments) {
    product.setComments(comments);
  }

  public void setDateCollected(Date dateCollected) {
    product.setDateCollected(dateCollected);
  }

  public void setIsDeleted(Boolean isDeleted) {
    product.setIsDeleted(isDeleted);
  }

  public void setIssued(Boolean issued) {
    product.setIssued(issued);
  }

  public void setProductNumber(String productNumber) {
    product.setProductNumber(productNumber);
  }

  public void setRhd(String rhd) {
    product.setRhd(rhd);
  }

  public void setType(String type) {
    product.setType(type);
  }

  public String toString() {
    return product.toString();
  }

  public Product getProduct() {
    return product;
  }

  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }
}
