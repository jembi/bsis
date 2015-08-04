package model.productmovement;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;


@Entity
@Audited
public class ProductStatusChangeReason {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=100)
  private String statusChangeReason;

  @Enumerated(EnumType.STRING)
  @Column(length=30)
  private ProductStatusChangeReasonCategory category;

  private Boolean isDeleted;

  public ProductStatusChangeReason() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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

  public void copy(ProductStatusChangeReason productStatusChangeReason){
    this.setId(productStatusChangeReason.getId());
    this.setCategory(productStatusChangeReason.getCategory());
    this.setStatusChangeReason(productStatusChangeReason.getStatusChangeReason());
    this.setIsDeleted(productStatusChangeReason.getIsDeleted());
  }
}
