package model.productmovement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.product.Product;
import model.product.ProductExists;
import model.reasons.ProductReturnReason;
import model.request.Request;
import model.user.User;

@Entity
public class ProductReturn {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @ProductExists
  @ManyToOne
  private Product product;

  @Temporal(TemporalType.TIMESTAMP)
  private Date returnedOn;

  @ManyToOne(optional=true)
  private Request returnedForRequest;

  @ManyToOne
  private User returnedBy;

  @ManyToOne
  private ProductReturnReason returnReason;

  public ProductReturn() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Date getIssuedOn() {
    return returnedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.returnedOn = issuedOn;
  }

  public Request getIssuedTo() {
    return returnedForRequest;
  }

  public void setIssuedTo(Request issuedTo) {
    this.returnedForRequest = issuedTo;
  }

  public User getReturnedBy() {
    return returnedBy;
  }

  public void setReturnedBy(User returnedBy) {
    this.returnedBy = returnedBy;
  }

  public ProductReturnReason getReturnReason() {
    return returnReason;
  }

  public void setReturnReason(ProductReturnReason returnReason) {
    this.returnReason = returnReason;
  }
}
