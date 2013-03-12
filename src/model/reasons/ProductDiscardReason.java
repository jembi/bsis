package model.reasons;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class ProductDiscardReason {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  private Long id;

  @Column(length=100)
  private String discardReason;

  @Column(length=200)
  private String discardReasonDetails;

  public ProductDiscardReason() {
  }
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDiscardReason() {
    return discardReason;
  }

  public void setDiscardReason(String discardReason) {
    this.discardReason = discardReason;
  }

  public String getDiscardReasonDetails() {
    return discardReasonDetails;
  }

  public void setDiscardReasonDetails(String discardReasonDetails) {
    this.discardReasonDetails = discardReasonDetails;
  }
}
