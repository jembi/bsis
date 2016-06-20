package factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;
import viewmodel.DiscardReasonViewModel;

@Service
public class ComponentStatusChangeReasonFactory {
  
  public List<DiscardReasonViewModel> createDiscardReasonViewModels(List<ComponentStatusChangeReason> entities) {
    List<DiscardReasonViewModel> viewModels = new ArrayList<>();
    for (ComponentStatusChangeReason entity : entities) {
      viewModels.add(createDiscardReasonViewModel(entity));
    }
    return viewModels;
  }
  
  public DiscardReasonViewModel createDiscardReasonViewModel(ComponentStatusChangeReason entity) {
    if (entity.getCategory() == null || !entity.getCategory().equals(ComponentStatusChangeReasonCategory.DISCARDED)) {
      throw new IllegalArgumentException("ComponentStatusChangeReason category is not DISCARDED");
    }
    DiscardReasonViewModel viewModel = new DiscardReasonViewModel(entity);
    return viewModel;
  }

}
