package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.componentbatch.ComponentBatch;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.viewmodel.ComponentBatchFullViewModel;
import org.jembi.bsis.viewmodel.ComponentBatchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentBatchViewModelFactory {

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
    viewModel.setNumberOfComponents(componentBatch.getComponents().size());
    return viewModel;
  }

  private void populateFullViewModel(ComponentBatch componentBatch, ComponentBatchFullViewModel viewModel) {

    populateViewModel(componentBatch, viewModel);
    viewModel.setComponents(componentFactory.createComponentFullViewModels(componentBatch.getComponents()));
    viewModel.setBloodTransportBoxes(
        bloodTransportBoxViewModelFactory.createBloodTransportBoxViewModels(componentBatch.getBloodTransportBoxes()));
  }
    
}
