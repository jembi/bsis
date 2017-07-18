package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.PackTypeViewModel;

public class PackTypeViewModelBuilder extends AbstractBuilder<PackTypeViewModel> {

  private UUID id;
  private String packType;

  public PackTypeViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public PackTypeViewModelBuilder withPackType(String packType) {
    this.packType = packType;
    return this;
  }

  @Override
  public PackTypeViewModel build() {
    PackTypeViewModel viewModel = new PackTypeViewModel();
    viewModel.setId(id);
    viewModel.setPackType(packType);
    return viewModel;
  }

  public static PackTypeViewModelBuilder aPackTypeViewModel() {
    return new PackTypeViewModelBuilder();
  }

}
