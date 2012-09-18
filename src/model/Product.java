package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long productId;
  private String productNumber;
  private String collectionNumber;
  private Date dateCollected;
  private String type;
  private String abo;
  private String rhd;

  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean isIssued;

  @Type(type = "org.hibernate.type.NumericBooleanType")
  private Boolean isDeleted;

  private String comments;

  public Product() {
  }

  public Product(String productNumber, Collection collection, String type,
      Boolean isDeleted, Boolean isIssued, String comments) {
    this.productNumber = productNumber;
    this.isDeleted = isDeleted;
    if (collection != null) {
      this.collectionNumber = collection.getCollectionNumber();
      this.dateCollected = collection.getDateCollected();
    }
    this.type = type;
    this.isIssued = isIssued;
    this.comments = comments;
  }

  public void copy(Product product) {
    this.productNumber = product.productNumber;
    this.collectionNumber = product.collectionNumber;
    this.dateCollected = product.dateCollected;
    this.type = product.type;
    this.isDeleted = product.isDeleted;
    this.abo = product.abo;
    this.rhd = product.rhd;
    this.isIssued = product.isIssued;
  }

  public String getAbo() {
    return abo;
  }

  public String getCollectionNumber() {
    return collectionNumber;
  }

  public String getComments() {
    return comments;
  }

  public Date getDateCollected() {
    return dateCollected;
  }

  public String getDateCollectedString() {
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
    return formatter.format(dateCollected);
  }

  public Boolean getIssued() {
    return isIssued;
  }

  public Long getProductId() {
    return productId;
  }

  public String getProductNumber() {
    return productNumber;
  }

  public String getRhd() {
    return rhd;
  }

  public String getType() {
    return type;
  }

  public void setAbo(String abo) {
    this.abo = abo;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public void setDateCollected(Date dateCollected) {
    this.dateCollected = dateCollected;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setIssued(Boolean issued) {
    isIssued = issued;
  }

  public void setProductNumber(String productNumber) {
    this.productNumber = productNumber;
  }

  public void setRhd(String rhd) {
    this.rhd = rhd;
  }

  public void setType(String type) {
    this.type = type;
  }

  // TODO: Workaround for now. Figure out a better way to do this.
  public void copyNotNull(Product product) {
    if (product.productNumber != null)
      this.productNumber = product.productNumber;
    if (product.collectionNumber != null)
      this.collectionNumber = product.collectionNumber;
    if (product.dateCollected != null)
      this.dateCollected = product.dateCollected;
    if (product.type != null)
      this.type = product.type;
    if (product.isDeleted != null)
      this.isDeleted = product.isDeleted;
    if (product.abo != null)
      this.abo = product.abo;
    if (product.rhd != null)
      this.rhd = product.rhd;
    if (product.isIssued != null)
      this.isIssued = product.isIssued;
    if (product.comments != null)
      this.comments = product.comments;
  }
}
