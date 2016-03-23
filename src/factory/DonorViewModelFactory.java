package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dto.DuplicateDonorDTO;
import model.donor.Donor;
import service.DonorConstraintChecker;
import viewmodel.DonorViewModel;
import viewmodel.DuplicateDonorViewModel;

/**
 * A factory for creating DonorViewModel objects.
 */
@Service
public class DonorViewModelFactory {

  /** The donor constraint checker. */
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  /**
   * Creates a new DonorViewModel object.
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
