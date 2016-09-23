package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileClinicDonorDTOFactory {

  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  public MobileClinicExportDonorViewModel createMobileClinicDonorExportViewModel(MobileClinicDonorDTO donor, Date clinicDate) {
    MobileClinicExportDonorViewModel donorExportViewModel = new MobileClinicExportDonorViewModel(donor);
    donorExportViewModel.setEligibility(donorConstraintChecker.isDonorEligibleToDonateOnDate(donor.getId(), clinicDate));
    return donorExportViewModel;
  }

  public List<MobileClinicExportDonorViewModel> createMobileClinicDonorExportViewModels(List<MobileClinicDonorDTO> donors, Date clinicDate) {
    List<MobileClinicExportDonorViewModel> donorViewModels = new ArrayList<>();
    for (MobileClinicDonorDTO d : donors) {
      donorViewModels.add(createMobileClinicDonorExportViewModel(d, clinicDate));
    }
    return donorViewModels;
  }
}
