package org.jembi.bsis.factory;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.jembi.bsis.model.adverseevent.AdverseEvent;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.viewmodel.AdverseEventViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdverseEventFactory {

  @Autowired
  private AdverseEventTypeViewModelFactory adverseEventTypeViewModelFactory;
  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  public AdverseEvent createEntity(AdverseEventBackingForm backingForm) {
    if (backingForm == null || backingForm.getType() == null) {
      return null;
    }

    AdverseEvent adverseEvent = new AdverseEvent();
    adverseEvent.setId(backingForm.getId());
    adverseEvent.setComment(backingForm.getComment());
    adverseEvent.setType(adverseEventTypeRepository.findById(backingForm.getType().getId()));
    return adverseEvent;
  }

  public AdverseEventViewModel createAdverseEventViewModel(AdverseEvent adverseEvent) {
    AdverseEventViewModel viewModel = new AdverseEventViewModel();
    viewModel.setId(adverseEvent.getId());
    viewModel.setType(adverseEventTypeViewModelFactory.createAdverseEventTypeViewModel(adverseEvent.getType()));
    viewModel.setComment(adverseEvent.getComment());
    return viewModel;
  }

}
