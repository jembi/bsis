package model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class SubdividedProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  // eg. A product can be divided into products A0, B0
  // then further split into Aa, Ab, Ac
  @Column(length=5)
  private String divisionCode;

  @ManyToOne
  private Product parentProduct;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getParentProduct() {
    return parentProduct;
  }

  public void setParentProduct(Product parentProduct) {
    this.parentProduct = parentProduct;
  }

  public String getDivisionCode() {
    return divisionCode;
  }

  public void setDivisionCode(String divisionCode) {
    this.divisionCode = divisionCode;
  }
}
