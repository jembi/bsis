package factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.donor.MobileClinicDonor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.DonorConstraintChecker;
import viewmodel.MobileClinicLookUpDonorViewModel;

@Service
public class MobileClinicDonorViewModelFactory {

  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  public MobileClinicLookUpDonorViewModel createMobileClinicDonorViewModel(MobileClinicDonor donor, Date clinicDate) {
    MobileClinicLookUpDonorViewModel donorViewModel = new MobileClinicLookUpDonorViewModel(donor);
    donorViewModel.setEligibility(donorConstraintChecker.isDonorEligibleToDonateOnDate(donor.getId(), clinicDate));
    return donorViewModel;
  }

  public List<MobileClinicLookUpDonorViewModel> createMobileClinicDonorViewModels(List<MobileClinicDonor> donors, Date clinicDate) {
    List<MobileClinicLookUpDonorViewModel> donorViewModels = new ArrayList<>();
    if (donors != null) {
      for (MobileClinicDonor d : donors) {
        donorViewModels.add(createMobileClinicDonorViewModel(d, clinicDate));
      }
    }
    return donorViewModels;
  }
}
