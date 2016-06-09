package factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import model.donationtype.DonationType;
import viewmodel.DonationTypeViewModel;

@Service
public class DonationTypeFactory {
  
  public List<DonationTypeViewModel> createDonationTypeViewModels(List<DonationType> donationTypes) {
    List<DonationTypeViewModel> viewModels = new ArrayList<>();
    for (DonationType donationType : donationTypes) {
      viewModels.add(createDonationTypeViewModel(donationType));
    }
    return viewModels;
  }
  
  public DonationTypeViewModel createDonationTypeViewModel(DonationType donationType) {
    DonationTypeViewModel viewModel = new DonationTypeViewModel(donationType);
    return viewModel;
  }

}
