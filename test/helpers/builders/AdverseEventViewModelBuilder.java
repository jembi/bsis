package helpers.builders;

import viewmodel.AdverseEventTypeViewModel;
import viewmodel.AdverseEventViewModel;

public class AdverseEventViewModelBuilder extends AbstractBuilder<AdverseEventViewModel> {

  private Long id;
  private AdverseEventTypeViewModel type;
  private String comment;

  public static AdverseEventViewModelBuilder anAdverseEventViewModel() {
    return new AdverseEventViewModelBuilder();
  }

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

}
