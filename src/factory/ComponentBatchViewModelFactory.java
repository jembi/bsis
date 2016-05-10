package factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.componentbatch.ComponentBatch;
import model.donationbatch.DonationBatch;
import viewmodel.ComponentBatchBasicViewModel;
import viewmodel.ComponentBatchViewModel;

@Service
public class ComponentBatchViewModelFactory {

  @Autowired
  BloodTransportBoxViewModelFactory bloodTransportBoxViewModelFactory;
  
  @Autowired
  ComponentViewModelFactory componentViewModelFactory;
  
  @Autowired
  DonationBatchViewModelFactory donationBatchViewModelFactory;
  
  public List<ComponentBatchBasicViewModel> createComponentBatchBasicViewModels(List<ComponentBatch> componentBatches) {
    List<ComponentBatchBasicViewModel> viewModels = new ArrayList<>();
    if (componentBatches != null) {
      for (ComponentBatch componentBatch : componentBatches) {
        viewModels.add(createComponentBatchBasicViewModel(componentBatch));
      }
    }
    return viewModels;
  }
  
  public ComponentBatchBasicViewModel createComponentBatchBasicViewModel(ComponentBatch componentBatch) {
    return populateBasicViewModel(componentBatch, new ComponentBatchBasicViewModel());
  }
  
  public ComponentBatchViewModel createComponentBatchFullViewModel(ComponentBatch componentBatch) {
    ComponentBatchViewModel viewModel = new ComponentBatchViewModel();
    populateFullViewModel(componentBatch, viewModel);
    return viewModel;
  }
  
  private ComponentBatchBasicViewModel populateBasicViewModel(ComponentBatch componentBatch, ComponentBatchBasicViewModel viewModel) {
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

  private void populateFullViewModel(ComponentBatch componentBatch, ComponentBatchViewModel viewModel) {

    populateBasicViewModel(componentBatch, viewModel);
    viewModel.setComponents(componentViewModelFactory.createComponentViewModels(componentBatch.getComponents()));
    viewModel.setBloodTransportBoxes(
        bloodTransportBoxViewModelFactory.createBloodTransportBoxViewModels(componentBatch.getBloodTransportBoxes()));
  }
    
}
