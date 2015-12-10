package model.bloodtesting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.envers.Audited;

@Audited
@Entity
public class WellType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, insertable=false, updatable=false, columnDefinition="SMALLINT")
  private Long id;

  @Column(length=30)
  private String wellType;

  private Boolean requiresSample;

  private Boolean isDeleted;

  public WellType() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
