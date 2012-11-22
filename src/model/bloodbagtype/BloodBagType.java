package model.bloodbagtype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BloodBagType {
  @Id
  @Column(length=30, nullable=false)
  private String bloodBagType;

  public String getBloodBagType() {
    return bloodBagType;
  }

  public void setBloodBagType(String bloodBagType) {
    this.bloodBagType = bloodBagType;
  }

  @Override
  public String toString() {
    return bloodBagType;
  }
}
