package model.donationtype;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
public class DonationType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 50)
  private String donationType;

  private Boolean isDeleted;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String toString() {
    return donationType;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getDonationType() {
    return donationType;
  }

  public void setDonationType(String donorType) {
    this.donationType = donorType;
  }
}