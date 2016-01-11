package model.bloodtesting;

import javax.persistence.Column;
import javax.persistence.Entity;

import model.BaseEntity;

import org.hibernate.envers.Audited;

@Audited
@Entity
public class WellType extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Column(length=30)
  private String wellType;

  private Boolean requiresSample;

  private Boolean isDeleted;

  public WellType() {
  }

  public String getWellType() {
    return wellType;
  }

  public void setWellType(String wellType) {
    this.wellType = wellType;
  }

  public Boolean getRequiresSample() {
    return requiresSample;
  }

  public void setRequiresSample(Boolean requiresSample) {
    this.requiresSample = requiresSample;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
