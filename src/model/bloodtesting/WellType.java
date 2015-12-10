package model.bloodtesting;

import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
public class WellType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 30)
  private String wellType;

  private Boolean requiresSample;

  private Boolean isDeleted;

  public WellType() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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
