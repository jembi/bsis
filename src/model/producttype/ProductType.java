package model.producttype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ProductType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @Column(length=50)
  private String productType;

  private Integer shelfLife;

  @Column(length=30)
  private String shelfLifeUnits;

  @Column
  private Boolean isSubdivided;
  
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
    return productType;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getShelfLifeUnits() {
    return shelfLifeUnits;
  }

  public void setShelfLifeUnits(String shelfLifeUnits) {
    this.shelfLifeUnits = shelfLifeUnits;
  }

  public Integer getShelfLife() {
    return shelfLife;
  }

  public void setShelfLife(Integer shelfLife) {
    this.shelfLife = shelfLife;
  }

  public boolean equals(ProductType pt) {
    return this.id == pt.id;
  }

  public Boolean getIsSubdivided() {
    return isSubdivided;
  }

  public void setIsSubdivided(Boolean isSubdivided) {
    this.isSubdivided = isSubdivided;
  }
}
