package model.packtype;

import model.componenttype.ComponentType;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Entity
@Audited
public class PackType {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "SMALLINT")
  private Integer id;

  @Column(length = 50)
  private String packType;

  /**
   * TODO: Not using the canSplit, canPool fields for now
   */
  private Boolean canSplit;

  private Boolean canPool;

  private Boolean isDeleted;

  @ManyToOne
  private ComponentType componentType;

  @NotNull
  private Boolean countAsDonation;

  @NotNull
  @Column(nullable = false)
  private Boolean testSampleProduced = Boolean.TRUE;
  private Integer periodBetweenDonations;

  @AssertTrue(message = "Component type should be not null when countAsDonation is set to true")
  private boolean isValid() {
    return !this.countAsDonation || componentType != null;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return packType;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getPackType() {
    return packType;
  }

  public void setPackType(String packType) {
    this.packType = packType;
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

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public Boolean getCountAsDonation() {
    return countAsDonation;
  }

  public void setCountAsDonation(Boolean countAsDonation) {
    this.countAsDonation = countAsDonation;
  }

  public Boolean getTestSampleProduced() {
    return testSampleProduced;
  }

  public void setTestSampleProduced(Boolean testSampleProduced) {
    this.testSampleProduced = testSampleProduced;
  }

  public Integer getPeriodBetweenDonations() {
    return periodBetweenDonations;
  }

  public void setPeriodBetweenDonations(Integer periodBetweenDonations) {
    this.periodBetweenDonations = periodBetweenDonations;
  }

  public void copy(PackType packType) {
    this.packType = packType.getPackType();
    this.componentType = packType.getComponentType();
    this.periodBetweenDonations = packType.getPeriodBetweenDonations();
    this.countAsDonation = packType.getCountAsDonation();
    this.isDeleted = packType.getIsDeleted();
    this.testSampleProduced = packType.getTestSampleProduced();
  }

  @Override
  public boolean equals(Object other) {
    return other == this || other instanceof PackType && ((PackType) other).id == id;
  }


}
