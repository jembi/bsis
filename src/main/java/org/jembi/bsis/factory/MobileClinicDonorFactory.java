package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.dto.MobileClinicDonorDTO;
import org.jembi.bsis.model.util.Gender;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;
import org.jembi.bsis.viewmodel.MobileClinicLookUpDonorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileClinicDonorFactory {

  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  @Autowired
  private LocationFactory locationFactory;

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

  public MobileClinicExportDonorViewModel createMobileClinicExportDonorViewModel(
      MobileClinicDonorDTO mobileClinicDonorDTO, Date clinicDate) {
    MobileClinicExportDonorViewModel viewModel = new MobileClinicExportDonorViewModel();
    populateBasicFieldsForExportDonorViewModel(viewModel, mobileClinicDonorDTO);
    viewModel
        .setEligibility(donorConstraintChecker.isDonorEligibleToDonateOnDate(mobileClinicDonorDTO.getId(), clinicDate));
    return viewModel;
  }

  public List<MobileClinicExportDonorViewModel> createMobileClinicExportDonorViewModels(
      List<MobileClinicDonorDTO> donors, Date clinicDate) {
    List<MobileClinicExportDonorViewModel> viewModels = new ArrayList<>();
    for (MobileClinicDonorDTO donor : donors) {
      viewModels.add(createMobileClinicExportDonorViewModel(donor, clinicDate));
    }
    return viewModels;
  }

  private void populateBasicFieldsForExportDonorViewModel(MobileClinicExportDonorViewModel viewModel, MobileClinicDonorDTO donor) {
    viewModel.setBirthDate(getBirthDate(donor.getBirthDate()));
    viewModel.setDonorNumber(donor.getDonorNumber());
    viewModel.setDonorStatus(donor.getDonorStatus());
    viewModel.setFirstName(donor.getFirstName());
    viewModel.setGender(getGender(donor.getGender()));
    viewModel.setId(donor.getId());
    viewModel.setIsDeleted(donor.getIsDeleted());
    viewModel.setLastName(donor.getLastName());
    viewModel.setVenue(locationFactory.createViewModel(donor.getVenue()));
    viewModel.setBloodType(getBloodType(donor));
  }

  private String getGender(Gender gender) {
    return gender == null ? "" : gender.name();
  }

  private String getBirthDate(Date birthDate) {
    return CustomDateFormatter.getDateString(birthDate);
  }

  private String getBloodType(MobileClinicDonorDTO donor) {
    String bloodAbo = donor.getBloodAbo();
    String bloodRh = donor.getBloodRh();
    if (StringUtils.isBlank(bloodAbo) || StringUtils.isBlank(bloodRh))
      return "";
    else
      return bloodAbo + bloodRh;
  }

}
