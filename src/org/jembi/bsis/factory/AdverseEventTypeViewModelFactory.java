package org.jembi.bsis.factory;

import org.jembi.bsis.model.adverseevent.AdverseEventType;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.springframework.stereotype.Service;

@Service
public class AdverseEventTypeViewModelFactory {

  public AdverseEventTypeViewModel createAdverseEventTypeViewModel(AdverseEventType adverseEventType) {
    AdverseEventTypeViewModel viewModel = new AdverseEventTypeViewModel();
    viewModel.setId(adverseEventType.getId());
    viewModel.setName(adverseEventType.getName());
    viewModel.setDescription(adverseEventType.getDescription());
    viewModel.setIsDeleted(adverseEventType.isDeleted());
    return viewModel;
  }

}
