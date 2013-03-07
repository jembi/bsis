package model.productmovement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.product.Product;
import model.product.ProductExists;
import model.request.Request;
import model.request.RequestExists;
import model.user.User;

@Entity
public class ProductIssue {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @ProductExists
  @ManyToOne
  private Product product;

  @Temporal(TemporalType.TIMESTAMP)
  private Date issuedOn;

  @RequestExists
  @ManyToOne
  private Request issuedTo;

  @ManyToOne
  private User issuedBy;

  @Lob
  private String notes;

  public ProductIssue() {
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
    return issuedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
  }

  public Request getIssuedTo() {
    return issuedTo;
  }

  public void setIssuedTo(Request issuedTo) {
    this.issuedTo = issuedTo;
  }

  public User getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(User issuedBy) {
    this.issuedBy = issuedBy;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }
}
