package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.TransfusionReactionTypeManagementViewModel;

public class TransfusionReactionTypeManagementViewModelBuilder extends AbstractBuilder<TransfusionReactionTypeManagementViewModel> {

  private UUID id;
  private String name;
  private String description;
  boolean isDeleted = false;

  public TransfusionReactionTypeManagementViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionReactionTypeManagementViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TransfusionReactionTypeManagementViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public TransfusionReactionTypeManagementViewModelBuilder withIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public TransfusionReactionTypeManagementViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public TransfusionReactionTypeManagementViewModel build() {
    TransfusionReactionTypeManagementViewModel viewModel = new TransfusionReactionTypeManagementViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setDescription(description);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }

  public static TransfusionReactionTypeManagementViewModelBuilder aTransfusionReactionTypeManagementViewModel() {
    return new TransfusionReactionTypeManagementViewModelBuilder();
  }

}
