package factory;

import java.util.ArrayList;
import java.util.List;

import model.component.Component;
import model.componentbatch.ComponentBatch;
import model.donationbatch.DonationBatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viewmodel.ComponentBatchViewModel;

@Service
public class ComponentBatchViewModelFactory {

  @Autowired
  BloodTransportBoxViewModelFactory bloodTransportBoxViewModelFactory;
  
  @Autowired
  ComponentViewModelFactory componentViewModelFactory;
  
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
    ComponentBatchViewModel viewModel = new ComponentBatchViewModel();
    viewModel.setId(componentBatch.getId());
    viewModel.setDeliveryDate(componentBatch.getDeliveryDate());
    viewModel.setStatus(String.valueOf(componentBatch.getStatus()));
    viewModel.setComponents(componentViewModelFactory.createComponentViewModels(componentBatch.getComponents()));
    // FIXME: round about way to get DonationBatch
    if (componentBatch.getComponents() != null && !componentBatch.getComponents().isEmpty()) {
      Component component = componentBatch.getComponents().iterator().next();
      DonationBatch donationBatch = component.getDonation().getDonationBatch();
      viewModel.setDonationBatch(donationBatchViewModelFactory.createDonationBatchBasicViewModel(donationBatch));
    }
    viewModel.setCollectionDate(componentBatch.getCollectionDate());
    viewModel.setBloodTransportBoxes(bloodTransportBoxViewModelFactory.createBloodTransportBoxViewModels(componentBatch.getBloodTransportBoxes()));
    viewModel.setBloodTransportBoxCount(componentBatch.getBloodTransportBoxCount());
    return viewModel;
  }
}
