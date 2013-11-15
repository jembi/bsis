package model.requestedComponents;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.producttype.ProductType;
import model.user.User;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class RequestedComponents implements ModificationTracker  {
	
	@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;
	
	private Long request_id;
	
	private Integer productType;
	
	private String bloodABO;
	
	private String bloodRh;
	
	private Long numUnits;
	
	private Boolean isDeleted;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="productType",insertable =false, updatable =false)
  private ProductType productTypes;
	
	 @Valid
	 private RowModificationTracker modificationTracker;
	 
	 public RequestedComponents(){
		 modificationTracker = new RowModificationTracker();
	 }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the request_id
	 */
	public Long getRequest_id() {
		return request_id;
	}

	/**
	 * @param request_id the request_id to set
	 */
	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	/**
	 * @return the productType
	 */
	public Integer getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	/**
	 * @return the bloodABO
	 */
	public String getBloodABO() {
		return bloodABO;
	}

	/**
	 * @param bloodABO the bloodABO to set
	 */
	public void setBloodABO(String bloodABO) {
		this.bloodABO = bloodABO;
	}

	/**
	 * @return the bloodRh
	 */
	public String getBloodRh() {
		return bloodRh;
	}

	/**
	 * @param bloodRh the bloodRh to set
	 */
	public void setBloodRh(String bloodRh) {
		this.bloodRh = bloodRh;
	}

	/**
	 * @return the numUnits
	 */
	public Long getNumUnits() {
		return numUnits;
	}

	/**
	 * @param numUnits the numUnits to set
	 */
	public void setNumUnits(Long numUnits) {
		this.numUnits = numUnits;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }
	
	public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }
	
	public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }
	
	public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }
	
	public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }
	
	public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }
	
	public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }
	
	public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

	/**
	 * @return the productTypes
	 */
	public ProductType getProductTypes() {
		return productTypes;
	}

	/**
	 * @param productTypes the productTypes to set
	 */
	public void setProductTypes(ProductType productTypes) {
		this.productTypes = productTypes;
	}
}
