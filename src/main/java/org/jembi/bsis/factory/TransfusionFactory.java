package org.jembi.bsis.factory;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.viewmodel.TransfusionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransfusionFactory {

  @Autowired
  private PatientFactory patientFactory;

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;

  @Autowired
  private LocationFactory locationFactory;

  public Transfusion createEntity(TransfusionBackingForm form) {
    Transfusion transfusion = new Transfusion();
    transfusion.setId(form.getId());
    transfusion.setDonationIdentificationNumber(form.getDonationIdentificationNumber());
    // note: currently we always create a new patient entity because we only support creating new Transfusion data
    // and we don't attempt patient lookups.
    transfusion.setPatient(patientFactory.createEntity(form.getPatient()));
    if (form.getComponentCode() != null) {
      // the user scanned a component code - we need to use that
      transfusion.setComponentType(componentTypeRepository.findComponentTypeByCode(form.getComponentCode()));
    } else {
      // they selected the component type
      ComponentTypeBackingForm selectedComponentType = form.getComponentType();
      transfusion.setComponentType(componentTypeRepository.findComponentTypeByCode(selectedComponentType.getComponentTypeCode()));
    }
    transfusion.setReceivedFrom(locationRepository.getLocation(form.getReceivedFrom().getId()));
    if (form.getTransfusionReactionType() != null) {
      transfusion.setTransfusionReactionType(transfusionReactionTypeRepository.findById(form.getTransfusionReactionType().getId()));
    }
    transfusion.setTransfusionOutcome(form.getTransfusionOutcome());
    transfusion.setDateTransfused(form.getDateTransfused());
    transfusion.setNotes(form.getNotes());
    transfusion.setIsDeleted(form.getIsDeleted()); // note: the validator must ensure this is not null
    return transfusion;
  }

  public TransfusionViewModel createViewModel(Transfusion transfusion) {
    TransfusionViewModel viewModel = new TransfusionViewModel();
    viewModel.setId(transfusion.getId());
    viewModel.setComponentType(componentTypeFactory.createViewModel(transfusion.getComponentType()));
    viewModel.setDateTransfused(transfusion.getDateTransfused());
    viewModel.setDonationIdentificationNumber(transfusion.getDonationIdentificationNumber());
    viewModel.setPatient(patientFactory.createViewModel(transfusion.getPatient()));
    viewModel.setTransfusionOutcome(transfusion.getTransfusionOutcome());
    viewModel.setTransfusionReactionType(transfusionReactionTypeFactory.createTransfusionReactionTypeViewModel(
        transfusion.getTransfusionReactionType()));
    viewModel.setUsageSite(locationFactory.createViewModel(transfusion.getReceivedFrom()));
    viewModel.setIsDeleted(transfusion.getIsDeleted());
    viewModel.setNotes(transfusion.getNotes());
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
}
