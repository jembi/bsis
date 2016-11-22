package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.service.LabellingConstraintChecker;
import org.jembi.bsis.viewmodel.LabellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabellingFactory {
  
  @Autowired
  private LabellingConstraintChecker labellingConstraintChecker;

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
    viewModel.setHasComponentBatch(component.hasComponentBatch());
    viewModel.setPermissions(new HashMap<String, Boolean>());
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canPrintDiscardLabel", labellingConstraintChecker.canPrintDiscardLabel(component));
    permissions.put("canPrintPackLabel", labellingConstraintChecker.canPrintPackLabel(component));
    viewModel.setPermissions(permissions);
    return viewModel;
  }

}
