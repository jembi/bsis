package model.donor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DonorType {
  @Id
  @Column(length=30, nullable=false)
  private String donorType;

  public String getDonorType() {
    return donorType;
  }

  public void setDonorType(String donorType) {
    this.donorType = donorType;
  }
}