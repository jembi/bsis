package model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Issue {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long issueId;
	private String productNumber;
	private Date dateIssued;
	private Long siteId;
	private String comments;

	@Type(type = "org.hibernate.type.NumericBooleanType")
	private Boolean isDeleted;

	public Issue() {
	}

	public Issue(String productNumber, Date dateIssued, Long siteId,
			Boolean isDeleted, String comments) {
		this.productNumber = productNumber;
		this.dateIssued = dateIssued;
		this.siteId = siteId;
		this.isDeleted = isDeleted;
		this.comments = comments;
	}

	public void copy(Issue issue) {
		this.productNumber = issue.productNumber;
		this.dateIssued = issue.dateIssued;
		this.siteId = issue.siteId;
		isDeleted = issue.isDeleted;
	}

	public String getComments() {
		return comments;
	}

	public Date getDateIssued() {
		return dateIssued;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public Long getIssueId() {
		return issueId;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
}
