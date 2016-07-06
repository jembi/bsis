package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.viewmodel.LabellingViewModel;
import org.springframework.stereotype.Service;

@Service
public class LabellingFactory {

  public List<LabellingViewModel> createViewModels(List<Component> components) {
    List<LabellingViewModel> viewModels = new ArrayList<>();
    if (components != null) {
      for (Component component : components) {
        viewModels.add(createViewModel(component));
      }
    }
    return viewModels;
  }

  public LabellingViewModel createViewModel(Component component) {
    LabellingViewModel viewModel = new LabellingViewModel();
    viewModel.setId(component.getId());
    viewModel.setComponentCode(component.getComponentCode());
    viewModel.setComponentName(component.getComponentType().getComponentTypeName());
    viewModel.setPermissions(new HashMap<String, Boolean>());
    // TODO: Set permissions from constraint checker
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canPrintDiscardLabel", false);
    permissions.put("canPrintPackLabel", false);
    viewModel.setPermissions(permissions);
    return viewModel;
  }

}
