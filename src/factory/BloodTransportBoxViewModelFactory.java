package factory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import model.componentbatch.BloodTransportBox;

import org.springframework.stereotype.Service;

import viewmodel.BloodTransportBoxViewModel;

@Service
public class BloodTransportBoxViewModelFactory {
  
  public List<BloodTransportBoxViewModel> createBloodTransportBoxViewModels(Set<BloodTransportBox> bloodTransportBoxes) {
    List<BloodTransportBoxViewModel> viewModels = new ArrayList<>();
    if (bloodTransportBoxes != null) {
      Iterator<BloodTransportBox> it = bloodTransportBoxes.iterator();
      while (it.hasNext()) {
        viewModels.add(createBloodTransportBoxViewModel(it.next()));
      }
    }
    return viewModels;
  }

  public BloodTransportBoxViewModel createBloodTransportBoxViewModel(BloodTransportBox bloodTransportBox) {
    BloodTransportBoxViewModel viewModel = new BloodTransportBoxViewModel();
    viewModel.setId(bloodTransportBox.getId());
    viewModel.setTemperature(bloodTransportBox.getTemperature());
    return viewModel;
  }
}
