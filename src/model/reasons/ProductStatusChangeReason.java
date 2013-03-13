package model.reasons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import model.productmovement.ProductStatusChangeReasonCategory;

@Entity
public class ProductStatusChangeReason {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @Column(length=100)
  private String statusChangeReason;

  @ManyToOne
  private ProductStatusChangeReasonCategory category;

  private Boolean isDeleted;

  public ProductStatusChangeReason() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatusChangeReason() {
    return statusChangeReason;
  }

  public void setStatusChangeReason(String statusChangeReason) {
    this.statusChangeReason = statusChangeReason;
  }

  public ProductStatusChangeReasonCategory getCategory() {
    return category;
  }

  public void setCategory(ProductStatusChangeReasonCategory category) {
    this.category = category;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
