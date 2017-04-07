package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.PackTypeBackingForm;

public class PackTypeBackingFormBuilder extends AbstractBuilder<PackTypeBackingForm> {
  
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

  public PackTypeBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public PackTypeBackingFormBuilder withPackType(String packType) {
    this.packType = packType;
    return this;
  }
  
  public PackTypeBackingFormBuilder withComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public PackTypeBackingFormBuilder withCanPool(Boolean canPool) {
    this.canPool = canPool;
    return this;
  }
  
  public PackTypeBackingFormBuilder withCanSplit(Boolean canSplit) {
    this.canSplit = canSplit;
    return this;
  }
  
  public PackTypeBackingFormBuilder withIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }
  
  public PackTypeBackingFormBuilder withCountAsDonation(Boolean countAsDonation) {
    this.countAsDonation = countAsDonation;
    return this;
  }
  
  public PackTypeBackingFormBuilder withTestSampleProduced(Boolean testSampleProduced) {
    this.testSampleProduced = testSampleProduced;
    return this;
  }
  
  public PackTypeBackingFormBuilder withPeriodBetweenDonations(Integer periodBetweenDonations) {
    this.periodBetweenDonations = periodBetweenDonations;
    return this;
  }
  
  public PackTypeBackingFormBuilder withMinWeight(Integer minWeight) {
    this.minWeight = minWeight;
    return this;
  }
  
  public PackTypeBackingFormBuilder withMaxWeight(Integer maxWeight) {
    this.maxWeight = maxWeight;
    return this;
  }
  
  public PackTypeBackingFormBuilder withLowVolumeWeight(Integer lowVolumeWeight) {
    this.lowVolumeWeight = lowVolumeWeight;
    return this;
  }
  
  @Override
  public PackTypeBackingForm build() {
    PackTypeBackingForm form = new PackTypeBackingForm();
    form.setId(id);
    form.setPackType(packType);
    form.setComponentType(componentType);
    form.setCanPool(canPool);
    form.setCanSplit(canSplit);
    form.setIsDeleted(isDeleted);
    form.setCountAsDonation(countAsDonation);
    form.setTestSampleProduced(testSampleProduced);
    form.setPeriodBetweenDonations(periodBetweenDonations);
    form.setMinWeight(minWeight);
    form.setMaxWeight(maxWeight);
    form.setLowVolumeWeight(lowVolumeWeight);
    return form;
  }
  
  public static PackTypeBackingFormBuilder aPackTypeBackingForm() {
    return new PackTypeBackingFormBuilder();
  }

}
