package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Collection {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long collectionId;
	private String collectionNumber;
	private Long centerId;
	private Long siteId;
	private Date dateCollected;
	private Long sampleNumber;
	private Long shippingNumber;
	private String donorNumber;
	private String donorType;
	private String abo;
	private String rhd;
	private String comments;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	public Collection() {
	  collectionNumber = "";
	  dateCollected = new Date();
	  donorNumber = "";
	  donorType = "";
	  abo = "";
	  rhd = "";
	  comments = "";
	}

	public Collection(String collectionNumber, Long centerId, Long siteId,
			Date dateCollected, Long sampleNumber, Long shippingNumber,
			String donorNumber, String donorType, Boolean isDeleted,
			String comments) {
		this.collectionNumber = collectionNumber;
		this.centerId = centerId;
		this.siteId = siteId;
		this.dateCollected = dateCollected;
		this.sampleNumber = sampleNumber;
		this.shippingNumber = shippingNumber;
		this.donorNumber = donorNumber;
		this.donorType = donorType;
		this.comments = comments;
		this.isDeleted = isDeleted;
	}

	public Collection(String collectionNumber, Long centerId, Long siteId,
			Date dateCollected, Long sampleNumber, Long shippingNumber,
			String donorNumber, String donorType, String abo, String rhd,
			Boolean isDeleted, String comments) {
		this.collectionNumber = collectionNumber;
		this.centerId = centerId;
		this.siteId = siteId;
		this.dateCollected = dateCollected;
		this.sampleNumber = sampleNumber;
		this.shippingNumber = shippingNumber;
		this.donorNumber = donorNumber;
		this.donorType = donorType;
		this.comments = comments;
		this.abo = abo;
		this.rhd = rhd;
		this.isDeleted = isDeleted;
	}

	public void copy(Collection collection) {
		this.collectionNumber = collection.collectionNumber;
		this.centerId = collection.centerId;
		this.siteId = collection.siteId;
		this.dateCollected = collection.dateCollected;
		this.sampleNumber = collection.sampleNumber;
		this.shippingNumber = collection.shippingNumber;
		this.donorNumber = collection.donorNumber;
		this.donorType = collection.donorType;
		this.comments = collection.comments;
		this.isDeleted = collection.isDeleted;
		this.abo = collection.abo;
		this.rhd = collection.rhd;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public Long getCenterId() {
		return centerId;
	}

	public Long getSiteId() {
		return siteId;
	}

	public Date getDateCollected() {
		return dateCollected;
	}

	public Long getSampleNumber() {
		return sampleNumber;
	}

	public Long getShippingNumber() {
		return shippingNumber;
	}

	public String getDonorNumber() {
		return donorNumber;
	}

	public String getComments() {
		return comments;
	}

	public String getCollectionNumber() {
		return collectionNumber;
	}

	public void setCollectionNumber(String collectionNumber) {
		this.collectionNumber = collectionNumber;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}

	public void setSampleNumber(Long sampleNumber) {
		this.sampleNumber = sampleNumber;
	}

	public void setShippingNumber(Long shippingNumber) {
		this.shippingNumber = shippingNumber;
	}

	public void setDonorNumber(String donorNumber) {
		this.donorNumber = donorNumber;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getDonorType() {
		return donorType;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
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

  public void setDonorType(String donorType) {
    this.donorType = donorType;
  }
}