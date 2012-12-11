package model.donortype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DonorType {
  @Id
  @Column(length=30, nullable=false)
  private String donorType;

  private Boolean isDeleted;

  public String getDonorType() {
    return donorType;
  }

  public void setDonorType(String donorType) {
    this.donorType = donorType;
  }

  public String toString() {
    return donorType;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}