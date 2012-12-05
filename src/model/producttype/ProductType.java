package model.producttype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProductType {
  @Id
  @Column(length=30, nullable=false)
  private String productType;

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
}
