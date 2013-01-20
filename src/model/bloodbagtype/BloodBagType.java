package model.bloodbagtype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BloodBagType {
  @Id
  @Column(length=30, nullable=false)
  private String bloodBagType;

  @Column(length=50)
  private String bloodBagTypeName;

  private Boolean isDeleted;
  
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getBloodBagTypeName() {
    return bloodBagTypeName;
  }

  public void setBloodBagTypeName(String bloodBagTypeName) {
    this.bloodBagTypeName = bloodBagTypeName;
  }
}
