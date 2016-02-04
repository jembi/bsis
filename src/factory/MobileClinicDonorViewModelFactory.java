package factory;

import java.util.ArrayList;
import java.util.List;

import model.donor.MobileClinicDonor;

import org.springframework.stereotype.Service;

import viewmodel.MobileClinicLookUpDonorViewModel;

@Service
public class MobileClinicDonorViewModelFactory {
    
    public MobileClinicLookUpDonorViewModel createMobileClinicDonorViewModel(MobileClinicDonor donor) {
      return new MobileClinicLookUpDonorViewModel(donor);
    }

    public List<MobileClinicLookUpDonorViewModel> createMobileClinicDonorViewModels(List<MobileClinicDonor> donors) {
      List<MobileClinicLookUpDonorViewModel> donorViewModels = new ArrayList<>();
      if (donors != null) {
        for (MobileClinicDonor d : donors) {
          donorViewModels.add(createMobileClinicDonorViewModel(d));
        }
      }
      return donorViewModels;
    }
}
