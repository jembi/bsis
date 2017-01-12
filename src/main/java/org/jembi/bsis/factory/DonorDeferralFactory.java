package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DeferralConstraintChecker;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorDeferralFactory {

  @Autowired
  private DeferralConstraintChecker deferralConstraintChecker;
  
  @Autowired
  private DeferralReasonRepository deferralReasonRepository;
  
  @Autowired
  private DonorRepository donorRepository;
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private DeferralReasonFactory deferralReasonFactory;
  

  public DonorDeferral createEntity(DeferralBackingForm backingForm) {
    DonorDeferral donorDeferral = new DonorDeferral();
    donorDeferral.setDeferralDate(backingForm.getDeferralDate());
    donorDeferral.setDeferralReason(deferralReasonRepository.getDeferralReasonById(backingForm.getDeferralReason().getId()));
    donorDeferral.setDeferredDonor(donorRepository.findDonorById(backingForm.getDeferredDonor().getId()));
    donorDeferral.setVenue(locationRepository.getLocation(backingForm.getVenue().getId()));
    donorDeferral.setDeferredUntil(backingForm.getDeferredUntil());
    donorDeferral.setId(backingForm.getId());
    donorDeferral.setDeferralReasonText(backingForm.getDeferralReasonText());
    return donorDeferral;
  }

  public List<DonorDeferralViewModel> createDonorDeferralViewModels(List<DonorDeferral> deferrals) {
    List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<>();
    for (DonorDeferral deferral : deferrals) {
      donorDeferralViewModels.add(createDonorDeferralViewModel(deferral));
    }
    return donorDeferralViewModels;
  }

  public DonorDeferralViewModel createDonorDeferralViewModel(DonorDeferral donorDeferral) {
    DonorDeferralViewModel viewModel = new DonorDeferralViewModel();
    if (donorDeferral.getCreatedBy() != null) {
      viewModel.setCreatedBy(donorDeferral.getCreatedBy().getUsername());
    }
    viewModel.setDeferralDate(donorDeferral.getDeferralDate());
    viewModel.setDeferralReason(deferralReasonFactory.createViewModel(donorDeferral.getDeferralReason()));
    viewModel.setDeferredUntil(donorDeferral.getDeferredUntil());
    viewModel.setDonorNumber(donorDeferral.getDeferredDonor().getDonorNumber());
    viewModel.setDeferredDonor(new DonorViewModel(donorDeferral.getDeferredDonor()));
    viewModel.setVenue(new LocationFullViewModel(donorDeferral.getVenue()));
    viewModel.setId(donorDeferral.getId());
    viewModel.setDeferralReasonText(donorDeferral.getDeferralReasonText());

    // Populate permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canEdit", deferralConstraintChecker.canEditDonorDeferral(donorDeferral.getId()));
    permissions.put("canEnd", deferralConstraintChecker.canEndDonorDeferral(donorDeferral.getId()));
    viewModel.setPermissions(permissions);

    return viewModel;
  }

}
