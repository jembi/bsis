package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
import org.springframework.stereotype.Service;

@Service
public class DeferralReasonFactory {

  public List<DeferralReasonViewModel> createViewModels(List<DeferralReason> deferralReasons) {
    List<DeferralReasonViewModel> viewModels = new ArrayList<>();
    if (deferralReasons != null) {
      for (DeferralReason deferralReason : deferralReasons) {
        viewModels.add(createViewModel(deferralReason));
      }
    }
    return viewModels;
  }
  
  public DeferralReasonViewModel createViewModel(DeferralReason deferralReason) {
    DeferralReasonViewModel viewModel = new DeferralReasonViewModel();
    viewModel.setReason(deferralReason.getReason());
    viewModel.setDefaultDuration(deferralReason.getDefaultDuration());
    viewModel.setDurationType(deferralReason.getDurationType());
    viewModel.setId(deferralReason.getId());
    if (deferralReason.getIsDeleted() != null) {
      viewModel.setIsDeleted(deferralReason.getIsDeleted());
    }
    return viewModel;
  }
}