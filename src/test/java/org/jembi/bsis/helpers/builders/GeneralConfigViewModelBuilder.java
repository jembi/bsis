package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.viewmodel.GeneralConfigViewModel;

public class GeneralConfigViewModelBuilder extends AbstractBuilder<GeneralConfigViewModel> {

  private UUID id;
  private DataType dataType;
  private String value;
  private String name;
  private String description;

  public GeneralConfigViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public GeneralConfigViewModelBuilder withDataType(DataType dataType) {
    this.dataType = dataType;
    return this;
  }

  public GeneralConfigViewModelBuilder withValue(String value) {
    this.value = value;
    return this;
  }

  public GeneralConfigViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public GeneralConfigViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public GeneralConfigViewModel build() {
    GeneralConfigViewModel viewModel = new GeneralConfigViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setDescription(description);
    viewModel.setDataType(dataType);
    viewModel.setValue(value);

    return viewModel;
  }

  public static GeneralConfigViewModelBuilder aGeneralConfigViewModelBuilder() {
    return new GeneralConfigViewModelBuilder();
  }
}
