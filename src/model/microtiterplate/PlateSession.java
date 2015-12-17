package model.microtiterplate;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.BaseEntity;
import model.bloodtesting.BloodTestResult;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

@Audited
@Entity
public class PlateSession extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Temporal(TemporalType.TIMESTAMP)
  private Date plateUsedOn;

  @NotAudited
  @OneToMany
  private List<BloodTestResult> bloodTestsOnPlate;

  private Boolean isDeleted;

  public Date getPlateUsedOn() {
    return plateUsedOn;
  }

  public void setPlateUsedOn(Date plateUsedOn) {
    this.plateUsedOn = plateUsedOn;
  }

  public List<BloodTestResult> getBloodTestsOnPlate() {
    return bloodTestsOnPlate;
  }

  public void setBloodTestsOnPlate(List<BloodTestResult> bloodTestsOnPlate) {
    this.bloodTestsOnPlate = bloodTestsOnPlate;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
}