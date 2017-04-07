package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;

public class AdverseEventTypeViewModelBuilder extends AbstractBuilder<AdverseEventTypeViewModel> {

  private UUID id;
  private String name;
  private String description;
  private boolean deleted;

  public AdverseEventTypeViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public AdverseEventTypeViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public AdverseEventTypeViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public AdverseEventTypeViewModelBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  @Override
  public AdverseEventTypeViewModel build() {
    AdverseEventTypeViewModel viewModel = new AdverseEventTypeViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setDescription(description);
    viewModel.setIsDeleted(deleted);
    return viewModel;
  }

  public static AdverseEventTypeViewModelBuilder anAdverseEventTypeViewModel() {
    return new AdverseEventTypeViewModelBuilder();
  }

}
