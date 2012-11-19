package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.collectedsample.CollectedSample;
import model.user.User;
import model.util.BloodAbo;
import model.util.BloodRhd;

public class ProductBackingForm {
  private Product product;
  private List<String> types;
  private List<String> availability;

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

  public Product getProduct() {
    return product;
  }

  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }

  public List<String> getAvailability() {
    return availability;
  }

  public void setAvailability(List<String> availability) {
    this.availability = availability;
  }

  public boolean equals(Object obj) {
    return product.equals(obj);
  }

  public BloodAbo getBloodAbo() {
    return product.getBloodAbo();
  }

  public BloodRhd getBloodRhd() {
    return product.getBloodRhd();
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
    return product.getProductType();
  }

  public Date getExpiryDate() {
    return product.getExpiryDate();
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

  public void setProductNumber(String productNumber) {
    product.setProductNumber(productNumber);
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    product.setCollectedSample(collectedSample);
  }

  public void setProductType(String type) {
    product.setProductType(type);
  }

  public void setExpiryDate(Date expiryDate) {
    product.setExpiryDate(expiryDate);
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

  public String toString() {
    return product.toString();
  }
}