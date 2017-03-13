package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.viewmodel.TransfusionFullViewModel;
import org.jembi.bsis.viewmodel.TransfusionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransfusionFactory {

  @Autowired
  private PatientFactory patientFactory;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;

  @Autowired
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;

  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private ComponentFactory  componentFactory;

  /**
   * Creates a ransfusion from the specified Backing form. NOTE: It does not set the component
   * linked to the Transfusion.
   * 
   * @param form
   * @return Transfusion
   */
  public Transfusion createEntity(TransfusionBackingForm form) {
    Transfusion transfusion = new Transfusion();
    transfusion.setId(form.getId());
    
    // note: currently we always create a new patient entity because we only support creating new Transfusion data
    // and we don't attempt patient lookups.
    transfusion.setPatient(patientFactory.createEntity(form.getPatient()));
    transfusion.setReceivedFrom(locationRepository.getLocation(form.getReceivedFrom().getId()));
    if (form.getTransfusionReactionType() != null) {
      transfusion.setTransfusionReactionType(transfusionReactionTypeRepository.findById(form.getTransfusionReactionType().getId()));
    }
    transfusion.setTransfusionOutcome(form.getTransfusionOutcome());
    transfusion.setDateTransfused(form.getDateTransfused());
    transfusion.setNotes(form.getNotes());
    return transfusion;
  }

  public TransfusionViewModel createViewModel(Transfusion transfusion) {
    TransfusionViewModel viewModel = new TransfusionViewModel();
    viewModel.setId(transfusion.getId());
    viewModel.setComponentCode(transfusion.getComponent().getComponentCode());
    viewModel.setComponentType(transfusion.getComponent().getComponentType().getComponentTypeName());
    viewModel.setDateTransfused(transfusion.getDateTransfused());
    viewModel.setTransfusionOutcome(transfusion.getTransfusionOutcome());
    viewModel.setReceivedFrom(locationFactory.createViewModel(transfusion.getReceivedFrom()));
    viewModel.setDonationIdentificationNumber(transfusion.getComponent().getDonationIdentificationNumber());
    return viewModel;
  }

  public List<TransfusionViewModel> createViewModels(List<Transfusion> transfusions) {
    List<TransfusionViewModel> viewModels = new ArrayList<>();
    if (transfusions != null) {
      for (Transfusion transfusion : transfusions) {
        viewModels.add(createViewModel(transfusion));
      }
    }
    return viewModels;
  }

  public TransfusionFullViewModel createFullViewModel(Transfusion transfusion) {
    TransfusionFullViewModel viewModel = new TransfusionFullViewModel();
    viewModel.setId(transfusion.getId());
    viewModel.setComponent(componentFactory.createComponentViewModel(transfusion.getComponent()));
    viewModel.setDateTransfused(transfusion.getDateTransfused());
    viewModel.setPatient(patientFactory.createViewModel(transfusion.getPatient()));
    viewModel.setTransfusionOutcome(transfusion.getTransfusionOutcome());
    if (transfusion.getTransfusionReactionType() == null) {
      viewModel.setTransfusionReactionType(null);
    } else {
      viewModel.setTransfusionReactionType(transfusionReactionTypeFactory.createTransfusionReactionTypeViewModel(
          transfusion.getTransfusionReactionType()));
    }
    viewModel.setReceivedFrom(locationFactory.createViewModel(transfusion.getReceivedFrom()));
    viewModel.setNotes(transfusion.getNotes());
    viewModel.setDonationIdentificationNumber(transfusion.getComponent().getDonationIdentificationNumber());
    return viewModel;
  }

  public List<TransfusionFullViewModel> createFullViewModels(List<Transfusion> transfusions) {
    List<TransfusionFullViewModel> viewModels = new ArrayList<>();
    if (transfusions != null) {
      for (Transfusion transfusion : transfusions) {
        viewModels.add(createFullViewModel(transfusion));
      }
    }
    return viewModels;
  }
}
