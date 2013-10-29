package model.componentprocessing;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import model.producttype.ProductType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ComponentProcessing {

	@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false, columnDefinition="SMALLINT")
  private Integer id;
	
	@Column(length=6)
	private Integer productProcessed;
	
	@Column(length=6)
	private Integer productSource;
	
	@Column(length=11)
	private Integer unitsMin;
	
	@Column(length=11)
	private Integer unitsMax;
	
	private Boolean isDeleted;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="componentProcessing")
	private ProductType productType;
	
	
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductProcessed() {
		return productProcessed;
	}

	public void setProductProcessed(Integer productProcessed) {
		this.productProcessed = productProcessed;
	}

	public Integer getProductSource() {
		return productSource;
	}

	public void setProductSource(Integer productSource) {
		this.productSource = productSource;
	}

	public Integer getUnitsMin() {
		return unitsMin;
	}

	public void setUnitsMin(Integer unitsMin) {
		this.unitsMin = unitsMin;
	}

	public Integer getUnitsMax() {
		return unitsMax;
	}

	public void setUnitsMax(Integer unitsMax) {
		this.unitsMax = unitsMax;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
