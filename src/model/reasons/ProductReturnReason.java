package model.reasons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ProductReturnReason {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @Column(length=150)
  private String returnReason;

  @Lob
  private String reasonDetails;

  public ProductReturnReason() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReturnReason() {
    return returnReason;
  }

  public void setReturnReason(String returnReason) {
    this.returnReason = returnReason;
  }

  public String getReasonDetails() {
    return reasonDetails;
  }

  public void setReasonDetails(String reasonDetails) {
    this.reasonDetails = reasonDetails;
  }
}
