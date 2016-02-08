package factory;

import model.adverseevent.AdverseEventType;

import org.springframework.stereotype.Service;

import viewmodel.AdverseEventTypeViewModel;

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
