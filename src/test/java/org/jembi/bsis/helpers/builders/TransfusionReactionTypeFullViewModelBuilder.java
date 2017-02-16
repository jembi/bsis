package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.TransfusionReactionTypeFullViewModel;

public class TransfusionReactionTypeFullViewModelBuilder extends AbstractBuilder<TransfusionReactionTypeFullViewModel> {

  private long id;
  private String name;
  private String description;
  boolean isDeleted;

  public TransfusionReactionTypeFullViewModelBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public TransfusionReactionTypeFullViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public TransfusionReactionTypeFullViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public TransfusionReactionTypeFullViewModelBuilder withIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public TransfusionReactionTypeFullViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  @Override
  public TransfusionReactionTypeFullViewModel build() {
    TransfusionReactionTypeFullViewModel viewModel = new TransfusionReactionTypeFullViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setDescription(description);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }

  public static TransfusionReactionTypeFullViewModelBuilder aTransfusionReactionTypeFullViewModel() {
    return new TransfusionReactionTypeFullViewModelBuilder();
  }

}
