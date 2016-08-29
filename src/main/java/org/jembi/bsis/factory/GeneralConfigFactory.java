package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.viewmodel.GeneralConfigViewModel;
import org.springframework.stereotype.Service;

@Service
public class GeneralConfigFactory {

  public GeneralConfigViewModel createViewModel(GeneralConfig generalConfig) {
    GeneralConfigViewModel viewModel = new GeneralConfigViewModel();
    viewModel.setId(generalConfig.getId());
    viewModel.setName(generalConfig.getName());
    viewModel.setDescription(generalConfig.getDescription());
    viewModel.setValue(getValue(generalConfig));
    viewModel.setDataType(generalConfig.getDataType());

    return viewModel;
  }

  private String getValue(GeneralConfig generalConfig) {
    // Check for password dataType then return empty string 
    if ("password".equalsIgnoreCase(generalConfig.getDataType().getDatatype())) {
      return "";
    }
    return generalConfig.getValue();
  }

  public List<GeneralConfigViewModel> createViewModels(List<GeneralConfig> entities) {
    List<GeneralConfigViewModel> viewModels = new ArrayList<>();
    if (entities != null) {
      for (GeneralConfig entity : entities) {
        viewModels.add(createViewModel(entity));
      }
    }
    return viewModels;
  }
}
