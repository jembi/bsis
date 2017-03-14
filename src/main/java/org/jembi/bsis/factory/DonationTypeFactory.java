package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.springframework.stereotype.Service;

@Service
public class DonationTypeFactory {
  
  public List<DonationTypeViewModel> createViewModels(List<DonationType> donationTypes) {
    List<DonationTypeViewModel> viewModels = new ArrayList<>();
    if (donationTypes != null) {
      for (DonationType donationType : donationTypes) {
        viewModels.add(createViewModel(donationType));
      }
    }
    return viewModels;
  }
  
  public DonationTypeViewModel createViewModel(DonationType donationType) {
    DonationTypeViewModel viewModel = new DonationTypeViewModel();
    viewModel.setId(donationType.getId());
    viewModel.setType(donationType.getDonationType());
    viewModel.setIsDeleted(donationType.getIsDeleted());
    return viewModel;
  }

  public DonationType createEntity(DonationTypeBackingForm form) {
    DonationType donationType = new DonationType();
    donationType.setId(form.getId());
    donationType.setDonationType(form.getType());
    donationType.setIsDeleted(form.getIsDeleted());
    return donationType;
  }

}
