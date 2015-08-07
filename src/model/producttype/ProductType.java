package model.producttype;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Audited
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
public class ProductType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=50)
  private String productTypeName;

  @Column(length=30)
  private String productTypeNameShort;

  private Integer expiresAfter;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private ProductTypeTimeUnits expiresAfterUnits;

  @NotAudited
  @ManyToMany(mappedBy="productTypes", fetch = FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ProductTypeCombination> productTypeCombinations;
  
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany(fetch=FetchType.EAGER)
  @Fetch(FetchMode.SELECT)
  private List<ProductTypeCombination> producedProductTypeCombinations;

  /**
   * TODO: Not used for now. Some product types like Cryoprecipitate may not require
   * blood group match before issuing. Usecase not clear yet.
   */
  private Boolean hasBloodGroup;

  /**
   * Pedi Product Type for this product type.
   * can be null. This allows us to determine the target type after splitting.
   */
  @OneToOne(optional=true)
  private ProductType pediProductType;

  @Lob
  private String description;

  private Boolean isDeleted;
  
  private Integer lowStorageTemperature;
  
  private Integer highStorageTemperature;
  
  private Integer lowTransportTemperature;
  
  private Integer highTransportTemperature;
  
  private String preparationInfo;
  
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return productTypeName;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean equals(ProductType pt) {
    return this.id == pt.id;
  }

  public String getProductTypeNameShort() {
    return productTypeNameShort;
  }

  public void setProductTypeNameShort(String productTypeShortName) {
    this.productTypeNameShort = productTypeShortName;
  }

  public ProductTypeTimeUnits getExpiresAfterUnits() {
    return expiresAfterUnits;
  }

  public void setExpiresAfterUnits(ProductTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
  }

  public Integer getExpiresAfter() {
    return expiresAfter;
  }

  public void setExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
  }

  public Boolean getHasBloodGroup() {
    return hasBloodGroup;
  }

  public void setHasBloodGroup(Boolean hasBloodGroup) {
    this.hasBloodGroup = hasBloodGroup;
  }

  @JsonIgnore
  public int getExpiryIntervalMinutes() {
    int factor = 1;
    switch (expiresAfterUnits) {
    case HOURS: factor = 60;
                break;
    case DAYS:  factor = 24 * 60;
                break;
    case YEARS: factor = 365 * 24 * 60;
                break;
    }
    return expiresAfter * factor;
  }

  public List<ProductTypeCombination> getProductTypeCombinations() {
    return productTypeCombinations;
  }

  public void setProductTypeCombinations(List<ProductTypeCombination> productTypeCombinations) {
    this.productTypeCombinations = productTypeCombinations;
  }
  
  public List<ProductTypeCombination> getProducedProductTypeCombinations() {
    return producedProductTypeCombinations;
  }

  public void setProducedProductTypeCombinations(List<ProductTypeCombination> producedProductTypeCombinations) {
    this.producedProductTypeCombinations = producedProductTypeCombinations;
  }

  public ProductType getPediProductType() {
    return pediProductType;
  }

  public void setPediProductType(ProductType pediProductType) {
    this.pediProductType = pediProductType;
  }

    public Integer getLowStorageTemperature() {
        return lowStorageTemperature;
    }

    public void setLowStorageTemperature(Integer lowStorageTemperature) {
        this.lowStorageTemperature = lowStorageTemperature;
    }

    public Integer getHighStorageTemperature() {
        return highStorageTemperature;
    }

    public void setHighStorageTemperature(Integer highStorageTemperature) {
        this.highStorageTemperature = highStorageTemperature;
    }

    public Integer getLowTransportTemperature() {
        return lowTransportTemperature;
    }

    public void setLowTransportTemperature(Integer lowTransportTemperature) {
        this.lowTransportTemperature = lowTransportTemperature;
    }

    public Integer getHighTransportTemperature() {
        return highTransportTemperature;
    }

    public void setHighTransportTemperature(Integer highTransportTemperature) {
        this.highTransportTemperature = highTransportTemperature;
    }

    public String getPreparationInfo() {
        return preparationInfo;
    }

    public void setPreparationInfo(String preparationInfo) {
        this.preparationInfo = preparationInfo;
    }
    
    
    public void copy(ProductType productType){
    	this.productTypeName = productType.getProductTypeName();
    	this.productTypeNameShort = productType.getProductTypeNameShort();
    	this.expiresAfter = productType.getExpiresAfter();
    	this.expiresAfterUnits = productType.getExpiresAfterUnits();
    	this.description = productType.getDescription();
    	this.hasBloodGroup = productType.getHasBloodGroup();
    	this.productTypeCombinations = productType.getProductTypeCombinations();
    	this.producedProductTypeCombinations = productType.getProducedProductTypeCombinations();
    	this.highStorageTemperature =  productType.getHighStorageTemperature();
    	this.lowStorageTemperature =  productType.getLowStorageTemperature();
    	this.lowTransportTemperature = productType.getLowTransportTemperature();
    	this.highTransportTemperature =  productType.getHighTransportTemperature();
    	this.preparationInfo = productType.getPreparationInfo();
    }
    
  
}
