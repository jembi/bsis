package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.PackTypeViewFullModel;

public class PackTypeViewFullModelBuilder {

  private Long id;
  private String packType;
  private Boolean countAsDonation;
  private int periodBetweenDonations;
  private Boolean testSampleProduced;
  private Boolean isDeleted;
  private ComponentTypeViewModel componentType;
  private Integer maxWeight;
  private Integer minWeight;
  private Integer lowVolumeWeight;

  public PackTypeViewFullModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PackTypeViewFullModelBuilder withPackType(String packType) {
    this.packType = packType;
    return this;
  }

  public PackTypeViewFullModelBuilder withTestSampleProduced(boolean testSampleProduced) {
    this.testSampleProduced = testSampleProduced;
    return this;
  }

  public PackTypeViewFullModelBuilder withCountAsDonation(boolean countAsDonation) {
    this.countAsDonation = countAsDonation;
    return this;
  }

  public PackTypeViewFullModelBuilder withPeriodBetweenDonations(int periodBetweenDonations) {
    this.periodBetweenDonations = periodBetweenDonations;
    return this;
  }

  public PackTypeViewFullModelBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }

  public PackTypeViewFullModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public PackTypeViewFullModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  public PackTypeViewFullModelBuilder withMaxWeight(Integer maxWeight) {
    this.maxWeight = maxWeight;
    return this;
  }

  public PackTypeViewFullModelBuilder withMinWeight(Integer minWeight) {
    this.minWeight = minWeight;
    return this;
  }

  public PackTypeViewFullModelBuilder withLowVolumeWeight(Integer lowVolumeWeight) {
    this.lowVolumeWeight = lowVolumeWeight;
    return this;
  }

  public PackTypeViewFullModel build() {
    PackTypeViewFullModel viewModel = new PackTypeViewFullModel();
    viewModel.setId(id);
    viewModel.setComponentType(componentType);
    viewModel.setPackType(packType);
    viewModel.setIsDeleted(isDeleted);
    viewModel.setPeriodBetweenDonations(periodBetweenDonations);
    viewModel.setTestSampleProduced(testSampleProduced);
    viewModel.setCountAsDonation(countAsDonation);
    viewModel.setMaxWeight(maxWeight);
    viewModel.setMinWeight(minWeight);
    viewModel.setLowVolumeWeight(lowVolumeWeight);
    return viewModel;
  }

  public static PackTypeViewFullModelBuilder aPackTypeViewFullModel() {
    return new PackTypeViewFullModelBuilder();
  }

}
