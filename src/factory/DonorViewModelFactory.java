package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.donor.Donor;
import service.DonorConstraintChecker;
import valueobject.DuplicateDonorValueObject;
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
   * @param duplicateDonorValueObjects the duplicate donor value objects
   * @return the list< duplicate donor view model>
   */
  public List<DuplicateDonorViewModel> createDuplicateDonorViewModels(
      List<DuplicateDonorValueObject> duplicateDonorValueObjects) {
    List<DuplicateDonorViewModel> duplicateDonorViewModels = new ArrayList<>();
    for (DuplicateDonorValueObject duplicateDonorValueObject : duplicateDonorValueObjects) {
      duplicateDonorViewModels.add(createDuplicateDonorViewModel(duplicateDonorValueObject));
    }
    return duplicateDonorViewModels;
  }

  /**
   * Creates a new DonorViewModel object.
   *
   * @param duplicateDonorValueObject the duplicate donor value object
   * @return the duplicate donor view model
   */
  private DuplicateDonorViewModel createDuplicateDonorViewModel(DuplicateDonorValueObject duplicateDonorValueObject) {

    DuplicateDonorViewModel duplicateDonorViewModel = new DuplicateDonorViewModel();
    duplicateDonorViewModel.setGroupKey(duplicateDonorValueObject.getGroupKey());
    duplicateDonorViewModel.setBirthDate(duplicateDonorValueObject.getBirthDate());
    duplicateDonorViewModel.setCount(duplicateDonorValueObject.getCount());
    duplicateDonorViewModel.setFirstName(duplicateDonorValueObject.getFirstName());
    duplicateDonorViewModel.setLastName(duplicateDonorValueObject.getLastName());
    duplicateDonorViewModel.setGender(duplicateDonorValueObject.getGender());

    return duplicateDonorViewModel;

  }

}
