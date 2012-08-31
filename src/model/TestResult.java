package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class TestResult {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long testResultId;
	private String collectionNumber;
	private Date dateCollected;
	private Date dateTested;

	private String hiv;
	private String hbv;
	private String hcv;
	private String syphilis;
	private String abo;
	private String rhd;
	private String comments;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	public TestResult() {
	}

	public TestResult(String collectionNumber, Date dateCollected,
			Date dateTested, String hiv, String hbv, String hcv,
			String syphilis, String abo, String rhd, Boolean deleted,
			String comments) {
		this.collectionNumber = collectionNumber;
		this.dateCollected = dateCollected;
		this.dateTested = dateTested;
		this.comments = comments;
		this.hiv = hiv;
		this.hbv = hbv;
		this.hcv = hcv;
		this.syphilis = syphilis;
		this.abo = abo;
		this.rhd = rhd;
		isDeleted = deleted;
	}

	public void copy(TestResult otherTestResult) {
		this.collectionNumber = otherTestResult.collectionNumber;
		this.dateCollected = otherTestResult.dateCollected;
		this.dateTested = otherTestResult.dateTested;
		this.comments = otherTestResult.comments;
		this.hiv = otherTestResult.hiv;
		this.hbv = otherTestResult.hbv;
		this.hcv = otherTestResult.hcv;
		this.syphilis = otherTestResult.syphilis;
		this.abo = otherTestResult.abo;
		this.rhd = otherTestResult.rhd;
		isDeleted = otherTestResult.isDeleted;
	}

	public Long getTestResultId() {
		return testResultId;
	}

	public String getCollectionNumber() {
		return collectionNumber;
	}

	public void setCollectionNumber(String collectionNumber) {
		this.collectionNumber = collectionNumber;
	}

	public Date getDateCollected() {
		return dateCollected;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}

	public Date getDateTested() {
		return dateTested;
	}

	public void setDateTested(Date dateTested) {
		this.dateTested = dateTested;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getHiv() {
		return hiv;
	}

	public void setHiv(String hiv) {
		this.hiv = hiv;
	}

	public String getHbv() {
		return hbv;
	}

	public void setHbv(String hbv) {
		this.hbv = hbv;
	}

	public String getHcv() {
		return hcv;
	}

	public void setHcv(String hcv) {
		this.hcv = hcv;
	}

	public String getSyphilis() {
		return syphilis;
	}

	public void setSyphilis(String syphilis) {
		this.syphilis = syphilis;
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

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean deleted) {
		isDeleted = deleted;
	}
}
