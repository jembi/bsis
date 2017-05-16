package org.jembi.bsis.backingform;

import java.util.UUID;

public class PackTypeBackingForm {

  private UUID id;
  private String packType;
  private ComponentTypeBackingForm componentType;
  private Boolean canPool;
  private Boolean canSplit;
  private Boolean isDeleted;
  private Boolean countAsDonation;
  private Boolean testSampleProduced;
  private Integer periodBetweenDonations;
  private Integer maxWeight;
  private Integer minWeight;
  private Integer lowVolumeWeight;
  
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getPackType() {
    return packType;
  }

  public void setPackType(String packType) {
    this.packType = packType;
  }
  
  public ComponentTypeBackingForm getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
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

  public Integer getMaxWeight() {
    return maxWeight;
  }

  public void setMaxWeight(Integer maxWeight) {
    this.maxWeight = maxWeight;
  }

  public Integer getMinWeight() {
    return minWeight;
  }

  public void setMinWeight(Integer minWeight) {
    this.minWeight = minWeight;
  }

  public Integer getLowVolumeWeight() {
    return lowVolumeWeight;
  }

  public void setLowVolumeWeight(Integer lowVolumeWeight) {
    this.lowVolumeWeight = lowVolumeWeight;
  }

}
