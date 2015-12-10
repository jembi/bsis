package factory;

import model.adverseevent.AdverseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import viewmodel.AdverseEventViewModel;

@Service
public class AdverseEventViewModelFactory {

  @Autowired
  private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;

  public AdverseEventViewModel createAdverseEventViewModel(AdverseEvent adverseEvent) {
    AdverseEventViewModel viewModel = new AdverseEventViewModel();
    viewModel.setId(adverseEvent.getId());
    viewModel.setType(adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEvent.getType()));
    viewModel.setComment(adverseEvent.getComment());
    return viewModel;
  }

}
