package model.producttype;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Filter;

import model.product.Product;

@Entity
public class ProductType {

  @Id
  @Column(length=30, nullable=false)
  private String productType;

  @Column(length=50, nullable=false)
  private String productTypeName;

  private Integer shelfLife;

  @Column(length=30, nullable=false)
  private String shelfLifeUnits;
  
  @Lob
  private String description;
  
  private Boolean isDeleted;

  @Filter(name="availableProductsNotExpiredFilter")
  @OneToMany(mappedBy="productType")
  private List<Product> products;
  
  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  @Override
  public String toString() {
    return productTypeName;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
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
}
