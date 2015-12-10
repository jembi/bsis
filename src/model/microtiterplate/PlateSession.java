package model.microtiterplate;

import model.bloodtesting.BloodTestResult;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Audited
@Entity
public class PlateSession {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false)
  private Long id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date plateUsedOn;

  @NotAudited
  @OneToMany
  private List<BloodTestResult> bloodTestsOnPlate;

  private Boolean isDeleted;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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
