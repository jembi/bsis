package model.producttype;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.Set;

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
  private Set<ProductTypeCombination> productTypeCombinations;
  
  @NotAudited
  @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
  @ManyToMany(fetch=FetchType.EAGER)
  private Set<ProductTypeCombination> producedProductTypeCombinations;

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

  public Set<ProductTypeCombination> getProductTypeCombinations() {
    return productTypeCombinations;
  }

  public void setProductTypeCombinations(Set<ProductTypeCombination> productTypeCombinations) {
    this.productTypeCombinations = productTypeCombinations;
  }
  
  public Set<ProductTypeCombination> getProducedProductTypeCombinations() {
    return producedProductTypeCombinations;
  }

  public void setProducedProductTypeCombinations(Set<ProductTypeCombination> producedProductTypeCombinations) {
    this.producedProductTypeCombinations = producedProductTypeCombinations;
  }

  public ProductType getPediProductType() {
    return pediProductType;
  }

  public void setPediProductType(ProductType pediProductType) {
    this.pediProductType = pediProductType;
  }
}
