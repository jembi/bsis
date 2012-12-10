package model.producttype;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Filter;

import model.product.Product;

@Entity
public class ProductType {
  @Id
  @Column(length=30, nullable=false)
  private String productType;

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
    return productType;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
  }
}
