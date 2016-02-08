package factory;

import java.util.HashMap;
import java.util.Map;

import model.donor.Donor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.DonorConstraintChecker;
import viewmodel.DonorViewModel;

@Service
public class DonorViewModelFactory {

  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  public DonorViewModel createDonorViewModelWithPermissions(Donor donor) {
    DonorViewModel donorViewModel = new DonorViewModel(donor);

    // Populate permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donorConstraintChecker.canDeleteDonor(donor.getId()));
    donorViewModel.setPermissions(permissions);

    return donorViewModel;
  }

}
