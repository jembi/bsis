package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.DiscardReasonBackingForm;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.viewmodel.DiscardReasonViewModel;
import org.springframework.stereotype.Service;

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
    DiscardReasonViewModel viewModel = new DiscardReasonViewModel();
    viewModel.setId(entity.getId());
    viewModel.setReason(entity.getStatusChangeReason());
    viewModel.setIsDeleted(entity.getIsDeleted());
    return viewModel;
  }

  public ComponentStatusChangeReason createDiscardReasonEntity(DiscardReasonBackingForm form) {
    ComponentStatusChangeReason discardReason = new ComponentStatusChangeReason();
    discardReason.setId(form.getId());
    discardReason.setCategory(ComponentStatusChangeReasonCategory.DISCARDED);
    discardReason.setStatusChangeReason(form.getReason());
    discardReason.setIsDeleted(form.getIsDeleted());
    return discardReason;
  }
}
