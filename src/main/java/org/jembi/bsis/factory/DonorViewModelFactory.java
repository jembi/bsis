package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.dto.DuplicateDonorDTO;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.service.DonorConstraintChecker;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.DuplicateDonorViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A factory for creating DonorViewModel objects.
 */
@Service
public class DonorViewModelFactory {

  /** The donor constraint checker. */
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;
  
  public List<DonorSummaryViewModel> createDonorSummaryViewModels(List<Donor> donors) {
    List<DonorSummaryViewModel> viewModels = new ArrayList<>();
    if (donors != null) {
      for (Donor donor : donors) {
        DonorSummaryViewModel donorSummaryViewModel = new DonorSummaryViewModel();
        populateDonorSummaryViewModel(donor, donorSummaryViewModel);
        viewModels.add(donorSummaryViewModel);
      }
    }
    return viewModels;
  }

  private void populateDonorSummaryViewModel(Donor donor, DonorSummaryViewModel donorSummaryViewModel) {
    donorSummaryViewModel.setBirthDate(donor.getBirthDate());
    donorSummaryViewModel.setFirstName(donor.getFirstName());
    donorSummaryViewModel.setGender(donor.getGender());
    donorSummaryViewModel.setLastName(donor.getLastName());
    donorSummaryViewModel.setVenueName(donor.getVenue().getName());
    donorSummaryViewModel.setDonorNumber(donor.getDonorNumber());
    donorSummaryViewModel.setId(donor.getId());
  }

  /**
   * Creates an array of DonorViewModels
   *
   * @param donors List of Donors 
   * @return List of DonorViewModels
   */
  public List<DonorViewModel> createDonorViewModels(List<Donor> donors) {
    List<DonorViewModel> viewModels = new ArrayList<>();
    if (donors != null) {
      for (Donor donor : donors) {
        DonorViewModel donorViewModel = new DonorViewModel(donor);
        viewModels.add(donorViewModel);
      }
    }
    return viewModels;
  }

  /**
   * Creates a new DonorViewModel object.
   *
   * @param donor the donor
   * @return the donor view model
   */
  public DonorViewModel createDonorViewModel(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    return donorViewModel;
  }

  /**
   * Creates a new DonorViewModel object with permissions.
   *
   * @param donor the donor
   * @return the donor view model
   */
  public DonorViewModel createDonorViewModelWithPermissions(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);

    // Populate permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donorConstraintChecker.canDeleteDonor(donor.getId()));
    donorViewModel.setPermissions(permissions);

    return donorViewModel;
  }

  /**
   * Creates a new DonorViewModel object.
   *
   * @param duplicateDonorDTOs the duplicate donor value objects
   * @return the list< duplicate donor view model>
   */
  public List<DuplicateDonorViewModel> createDuplicateDonorViewModels(
      List<DuplicateDonorDTO> duplicateDonorDTOs) {
    List<DuplicateDonorViewModel> duplicateDonorViewModels = new ArrayList<>();
    for (DuplicateDonorDTO duplicateDonorDTO : duplicateDonorDTOs) {
      duplicateDonorViewModels.add(createDuplicateDonorViewModel(duplicateDonorDTO));
    }
    return duplicateDonorViewModels;
  }

  /**
   * Creates a new DonorViewModel object.
   *
   * @param duplicateDonorDTO the duplicate donor value object
   * @return the duplicate donor view model
   */
  private DuplicateDonorViewModel createDuplicateDonorViewModel(DuplicateDonorDTO duplicateDonorDTO) {

    DuplicateDonorViewModel duplicateDonorViewModel = new DuplicateDonorViewModel();
    duplicateDonorViewModel.setGroupKey(duplicateDonorDTO.getGroupKey());
    duplicateDonorViewModel.setBirthDate(duplicateDonorDTO.getBirthDate());
    duplicateDonorViewModel.setCount(duplicateDonorDTO.getCount());
    duplicateDonorViewModel.setFirstName(duplicateDonorDTO.getFirstName());
    duplicateDonorViewModel.setLastName(duplicateDonorDTO.getLastName());
    duplicateDonorViewModel.setGender(duplicateDonorDTO.getGender());

    return duplicateDonorViewModel;

  }

}
