package model;

import java.util.Date;
import java.util.List;


public class CollectionBackingForm {
  private Collection collection;
  List<Location> centers;

  public CollectionBackingForm() {
    collection = new Collection();
  }
  
  public void copy(Collection collection) {
    collection.copy(collection);
  }

  public boolean equals(Object obj) {
    return collection.equals(obj);
  }

  public Long getCollectionId() {
    return collection.getCollectionId();
  }

  public Long getCenterId() {
    return collection.getCenterId();
  }

  public Long getSiteId() {
    return collection.getSiteId();
  }

  public Date getDateCollected() {
    return collection.getDateCollected();
  }

  public Long getSampleNumber() {
    return collection.getSampleNumber();
  }

  public Long getShippingNumber() {
    return collection.getShippingNumber();
  }

  public String getDonorNumber() {
    return collection.getDonorNumber();
  }

  public String getComments() {
    return collection.getComments();
  }

  public String getCollectionNumber() {
    return collection.getCollectionNumber();
  }

  public String getDonorType() {
    return collection.getDonorType();
  }

  public Boolean getIsDeleted() {
    return collection.getIsDeleted();
  }

  public String getAbo() {
    return collection.getAbo();
  }

  public String getRhd() {
    return collection.getRhd();
  }

  public int hashCode() {
    return collection.hashCode();
  }

  public void setCollectionNumber(String collectionNumber) {
    collection.setCollectionNumber(collectionNumber);
  }

  public void setCenterId(Long centerId) {
    collection.setCenterId(centerId);
  }

  public void setSiteId(Long siteId) {
    collection.setSiteId(siteId);
  }

  public void setDateCollected(Date dateCollected) {
    collection.setDateCollected(dateCollected);
  }

  public void setSampleNumber(Long sampleNumber) {
    collection.setSampleNumber(sampleNumber);
  }

  public void setShippingNumber(Long shippingNumber) {
    collection.setShippingNumber(shippingNumber);
  }

  public void setDonorNumber(String donorNumber) {
    collection.setDonorNumber(donorNumber);
  }

  public void setComments(String comments) {
    collection.setComments(comments);
  }

  public void setIsDeleted(Boolean isDeleted) {
    collection.setIsDeleted(isDeleted);
  }

  public void setAbo(String abo) {
    collection.setAbo(abo);
  }

  public void setRhd(String rhd) {
    collection.setRhd(rhd);
  }

  public String toString() {
    return collection.toString();
  }

  public List<Location> getCenters() {
    return centers;
  }

  public void setCenters(List<Location> centers) {
    this.centers = centers;
  }
}
