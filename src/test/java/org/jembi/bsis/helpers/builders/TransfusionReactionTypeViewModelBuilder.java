package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;

public class TransfusionReactionTypeViewModelBuilder extends AbstractBuilder<TransfusionReactionTypeViewModel> {

  private UUID id;
  private String name;
  boolean isDeleted = false;

  public TransfusionReactionTypeViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionReactionTypeViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TransfusionReactionTypeViewModelBuilder withIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public TransfusionReactionTypeViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public TransfusionReactionTypeViewModel build() {
    TransfusionReactionTypeViewModel viewModel = new TransfusionReactionTypeViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }

  public static TransfusionReactionTypeViewModelBuilder aTransfusionReactionTypeViewModel() {
    return new TransfusionReactionTypeViewModelBuilder();
  }

}
