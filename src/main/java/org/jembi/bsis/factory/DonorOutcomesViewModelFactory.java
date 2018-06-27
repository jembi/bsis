package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorOutcomesViewModelFactory {
  
  @Autowired
  private BloodTestResultFactory bloodTestResultFactory;
  
  public DonorOutcomesViewModel createDonorOutcomesViewModel(Donation donation) {
    Donor donor = donation.getDonor();
    
    DonorOutcomesViewModel viewModel = new DonorOutcomesViewModel();
    viewModel.setDonorNumber(donor.getDonorNumber());
    viewModel.setLastName(donor.getLastName());
    viewModel.setFirstName(donor.getFirstName());
    viewModel.setGender(donor.getGender());
    viewModel.setBirthDate(donor.getBirthDate());
    viewModel.setDonationDate(donation.getDonationDate());
    viewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
    viewModel.setBloodAbo(donor.getBloodAbo());
    viewModel.setBloodRh(donor.getBloodRh());
    viewModel.setBloodTestResults(
        bloodTestResultFactory.createFullViewModels(donation.getBloodTestResults()));
    return viewModel;
  }
  
  public List<DonorOutcomesViewModel> createDonorOutcomesViewModels(List<Donation> donations) {
    List<DonorOutcomesViewModel> viewModels = new ArrayList<>();
    for (Donation donation : donations) {
      viewModels.add(createDonorOutcomesViewModel(donation));
    }
    return viewModels;
  }

}
