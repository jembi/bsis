package model.bloodbagtype;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class BloodBagType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Integer id;

  @Column(length=50)
  private String bloodBagType;

  /**
   * TODO: Not using the canSplit, canPool fields for now
   */
  private Boolean canSplit;

  private Boolean canPool;

  private Boolean isDeleted;
  
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public String getBloodBagType() {
    return bloodBagType;
  }

  public void setBloodBagType(String bloodBagType) {
    this.bloodBagType = bloodBagType;
  }

  public Boolean getCanPool() {
    return canPool;
  }

  public void setCanPool(Boolean canPool) {
    this.canPool = canPool;
  }

  public Boolean getCanSplit() {
    return canSplit;
  }

  public void setCanSplit(Boolean canSplit) {
    this.canSplit = canSplit;
  }
}
