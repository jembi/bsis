package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.PackTypeFullViewModel;

public class PackTypeFullViewModelBuilder extends AbstractBuilder<PackTypeFullViewModel> {

  private UUID id;
  private String packType;
  private Boolean countAsDonation;
  private int periodBetweenDonations;
  private Boolean testSampleProduced;
  private Boolean isDeleted;
  private ComponentTypeViewModel componentType;
  private Integer maxWeight;
  private Integer minWeight;
  private Integer lowVolumeWeight;

  public PackTypeFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public PackTypeFullViewModelBuilder withPackType(String packType) {
    this.packType = packType;
    return this;
  }

  public PackTypeFullViewModelBuilder withTestSampleProduced(boolean testSampleProduced) {
    this.testSampleProduced = testSampleProduced;
    return this;
  }

  public PackTypeFullViewModelBuilder withCountAsDonation(boolean countAsDonation) {
    this.countAsDonation = countAsDonation;
    return this;
  }

  public PackTypeFullViewModelBuilder withPeriodBetweenDonations(int periodBetweenDonations) {
    this.periodBetweenDonations = periodBetweenDonations;
    return this;
  }

  public PackTypeFullViewModelBuilder thatIsNotDeleted() {
    this.isDeleted = false;
    return this;
  }

  public PackTypeFullViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public PackTypeFullViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  public PackTypeFullViewModelBuilder withMaxWeight(Integer maxWeight) {
    this.maxWeight = maxWeight;
    return this;
  }

  public PackTypeFullViewModelBuilder withMinWeight(Integer minWeight) {
    this.minWeight = minWeight;
    return this;
  }

  public PackTypeFullViewModelBuilder withLowVolumeWeight(Integer lowVolumeWeight) {
    this.lowVolumeWeight = lowVolumeWeight;
    return this;
  }

  @Override
  public PackTypeFullViewModel build() {
    PackTypeFullViewModel viewModel = new PackTypeFullViewModel();
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

  public static PackTypeFullViewModelBuilder aPackTypeViewFullModel() {
    return new PackTypeFullViewModelBuilder();
  }

}
