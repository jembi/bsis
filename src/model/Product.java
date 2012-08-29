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


    public Product(String productNumber, Collection collection, String type, Boolean isDeleted, Boolean isIssued) {
        this.productNumber = productNumber;
        this.isDeleted = isDeleted;
        if (collection != null) {
            this.collectionNumber = collection.getCollectionNumber();
            this.dateCollected = collection.getDateCollected();
        }
        this.type = type;
        this.isIssued = isIssued;
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

    public Product() {
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getCollectionNumber() {
        return collectionNumber;
    }

    public void setCollectionNumber(String collectionNumber) {
        this.collectionNumber = collectionNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateCollected() {
        return dateCollected;
    }

    public String getDateCollectedString() {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(dateCollected);
    }

    public void setDateCollected(Date dateCollected) {
        this.dateCollected = dateCollected;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getAbo() {
        return abo;
    }

    public void setAbo(String abo) {
        this.abo = abo;
    }

    public String getRhd() {
        return rhd;
    }

    public void setRhd(String rhd) {
        this.rhd = rhd;
    }

    public Boolean getIssued() {
        return isIssued;
    }

    public void setIssued(Boolean issued) {
        isIssued = issued;
    }
}
