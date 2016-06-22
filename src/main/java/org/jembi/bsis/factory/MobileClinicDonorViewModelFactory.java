package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileClinicDonorViewModelFactory {

  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  public MobileClinicLookUpDonorViewModel createMobileClinicDonorViewModel(MobileClinicDonorDTO donor, Date clinicDate) {
    MobileClinicLookUpDonorViewModel donorViewModel = new MobileClinicLookUpDonorViewModel(donor);
    donorViewModel.setEligibility(donorConstraintChecker.isDonorEligibleToDonateOnDate(donor.getId(), clinicDate));
    return donorViewModel;
  }

  public List<MobileClinicLookUpDonorViewModel> createMobileClinicDonorViewModels(List<MobileClinicDonorDTO> donors, Date clinicDate) {
    List<MobileClinicLookUpDonorViewModel> donorViewModels = new ArrayList<>();
    if (donors != null) {
      for (MobileClinicDonorDTO d : donors) {
        donorViewModels.add(createMobileClinicDonorViewModel(d, clinicDate));
      }
    }
    return donorViewModels;
  }
}
