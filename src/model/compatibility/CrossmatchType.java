package model.compatibility;

import javax.persistence.Entity;

import model.BaseEntity;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class CrossmatchType extends BaseEntity {

  private static final long serialVersionUID = 1L;

  private String crossmatchType;

  private Boolean isDeleted;

  public String getCrossmatchType() {
    return crossmatchType;
  }

  public void setCrossmatchType(String crossmatchType) {
    this.crossmatchType = crossmatchType;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}
