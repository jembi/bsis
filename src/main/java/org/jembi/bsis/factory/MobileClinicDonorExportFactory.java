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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MobileClinicDonorExportFactory {

  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  
  @Autowired
  private LocationFactory locationFactory;


  public MobileClinicExportDonorViewModel createMobileClinicExportDonorViewModel(
    MobileClinicDonorDTO mobileClinicDonorDTO, Date clinicDate) {
    MobileClinicExportDonorViewModel viewModel = new MobileClinicExportDonorViewModel();
    populateBasicFields(viewModel, mobileClinicDonorDTO);
    viewModel
        .setEligibility(donorConstraintChecker.isDonorEligibleToDonateOnDate(mobileClinicDonorDTO.getId(), clinicDate));
    return viewModel;
  }

  public List<MobileClinicExportDonorViewModel> createMobileClinicExportDonorViewModels(List<MobileClinicDonorDTO> donors, Date clinicDate) {
    List<MobileClinicExportDonorViewModel> viewModels = new ArrayList<>();
    for (MobileClinicDonorDTO donor : donors) {
      viewModels.add(createMobileClinicExportDonorViewModel(donor, clinicDate));
    }
    return viewModels;
  }

  private void populateBasicFields(MobileClinicExportDonorViewModel viewModel, MobileClinicDonorDTO donor) {
    viewModel.setBirthDate(getBirthDate(donor.getBirthDate()));
    viewModel.setDonorNumber(donor.getDonorNumber());
    viewModel.setDonorStatus(donor.getDonorStatus());
    viewModel.setFirstName(donor.getFirstName());
    viewModel.setGender(getGender(donor.getGender()));
    viewModel.setId(donor.getId());
    viewModel.setIsDeleted(donor.getIsDeleted());
    viewModel.setLastName(donor.getLastName());
    //viewModel.setVenue(locationFactory.createFullViewModel(donor.getVenue()));
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
