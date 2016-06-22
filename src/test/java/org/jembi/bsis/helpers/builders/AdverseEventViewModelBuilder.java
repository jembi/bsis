package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;

public class AdverseEventViewModelBuilder extends AbstractBuilder<AdverseEventViewModel> {

  private Long id;
  private AdverseEventTypeViewModel type;
  private String comment;

  public AdverseEventViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public AdverseEventViewModelBuilder withType(AdverseEventTypeViewModel type) {
    this.type = type;
    return this;
  }

  public AdverseEventViewModelBuilder withComment(String comment) {
    this.comment = comment;
    return this;
  }

  @Override
  public AdverseEventViewModel build() {
    AdverseEventViewModel viewModel = new AdverseEventViewModel();
    viewModel.setId(id);
    viewModel.setType(type);
    viewModel.setComment(comment);
    return viewModel;
  }

  public static AdverseEventViewModelBuilder anAdverseEventViewModel() {
    return new AdverseEventViewModelBuilder();
  }

}
