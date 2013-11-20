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
import model.request.Request;
import model.user.User;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class RequestedComponents implements ModificationTracker  {
	
	@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;
	
	@ManyToOne
	private Request request;
	
	//private Integer productType;
	
	private String bloodABO;
	
	private String bloodRh;
	
	private Long numUnits;
	
	private Boolean isDeleted;
	
	
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="productType", updatable =false)
  private ProductType productType;
	
	 @Valid
	 private RowModificationTracker modificationTracker;
	 
	 public RequestedComponents(){
		 modificationTracker = new RowModificationTracker();
	 }
	 
	 
	 public void copy(RequestedComponents requestedComponents) {
	    assert (this.getId() == requestedComponents.getId());
	    this.bloodABO = requestedComponents.bloodABO;
	    this.bloodRh = requestedComponents.bloodRh;
	    this.numUnits = requestedComponents.numUnits;
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
	 * @return the productType
	 */
	public ProductType getProductType() {
		return productType;
	}


	/**
	 * @param productType the productType to set
	 */
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}


	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(Request request) {
		this.request = request;
	}
	
}
