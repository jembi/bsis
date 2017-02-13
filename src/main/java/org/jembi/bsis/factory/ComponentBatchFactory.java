package org.jembi.bsis.factory;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;
import org.jembi.bsis.backingform.ComponentBatchBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.componentbatch.BloodTransportBox;
import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.ComponentBatchFullViewModel;
import org.jembi.bsis.viewmodel.ComponentBatchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ComponentBatchFactory {

  @Autowired
  BloodTransportBoxViewModelFactory bloodTransportBoxViewModelFactory;
  
  @Autowired
  ComponentFactory componentFactory;
  
  @Autowired
  DonationBatchViewModelFactory donationBatchViewModelFactory;
  
  public List<ComponentBatchViewModel> createComponentBatchViewModels(List<ComponentBatch> componentBatches) {
    List<ComponentBatchViewModel> viewModels = new ArrayList<>();
    if (componentBatches != null) {
      for (ComponentBatch componentBatch : componentBatches) {
        viewModels.add(createComponentBatchViewModel(componentBatch));
      }
    }
    return viewModels;
  }
  
  public ComponentBatchViewModel createComponentBatchViewModel(ComponentBatch componentBatch) {
    return populateViewModel(componentBatch, new ComponentBatchViewModel());
  }
  
  public ComponentBatchFullViewModel createComponentBatchFullViewModel(ComponentBatch componentBatch) {
    ComponentBatchFullViewModel viewModel = new ComponentBatchFullViewModel();
    populateFullViewModel(componentBatch, viewModel);
    return viewModel;
  }

  public ComponentBatch createEntity(ComponentBatchBackingForm form) {
    ComponentBatch componentBatch = new ComponentBatch();
    
    componentBatch.setId(form.getId());
    componentBatch.setStatus(form.getStatus());
    componentBatch.setDeliveryDate(form.getDeliveryDate());
    
    if (form.getLocation() != null) {
      Location location = new Location();
      location.setId(form.getLocation().getId());
      componentBatch.setLocation(location);
    }
    
    Set<BloodTransportBox> boxes = new HashSet<>();
    for (BloodTransportBoxBackingForm boxForm : form.getBloodTransportBoxes()) {
      BloodTransportBox box = boxForm.getBloodTransportBox();
      box.setComponentBatch(componentBatch);
      boxes.add(box);
    }
    componentBatch.setBloodTransportBoxes(boxes);

    if (form.getDonationBatch() != null) {
      DonationBatch donationBatch = form.getDonationBatch().getDonationBatch();
      componentBatch.setDonationBatch(donationBatch);
      donationBatch.setComponentBatch(componentBatch);
    }

    return componentBatch;
  }
  
  private ComponentBatchViewModel populateViewModel(ComponentBatch componentBatch, ComponentBatchViewModel viewModel) {
    viewModel.setId(componentBatch.getId());
    viewModel.setDeliveryDate(componentBatch.getDeliveryDate());
    viewModel.setStatus(String.valueOf(componentBatch.getStatus()));
    viewModel.setLocation(componentBatch.getLocation());
    DonationBatch donationBatch = componentBatch.getDonationBatch();
    if (donationBatch != null) {
      viewModel.setDonationBatch(donationBatchViewModelFactory.createDonationBatchBasicViewModel(donationBatch));
    }
    viewModel.setCollectionDate(componentBatch.getCollectionDate());
    viewModel.setNumberOfBoxes(componentBatch.getBloodTransportBoxes().size());
    int count = 0;
    for (Component component : componentBatch.getComponents()) {
      if (component.isInitialComponent()) {
        count ++;
      }
    }
    viewModel.setNumberOfInitialComponents(count);
    return viewModel;
  }

  private void populateFullViewModel(ComponentBatch componentBatch, ComponentBatchFullViewModel viewModel) {

    populateViewModel(componentBatch, viewModel);
    viewModel.setComponents(componentFactory.createComponentFullViewModels(componentBatch.getComponents()));
    viewModel.setBloodTransportBoxes(
        bloodTransportBoxViewModelFactory.createBloodTransportBoxViewModels(componentBatch.getBloodTransportBoxes()));
  }
    
}
