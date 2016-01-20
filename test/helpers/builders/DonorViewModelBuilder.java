package helpers.builders;

import model.donor.Donor;
import viewmodel.DonorViewModel;

import java.util.HashMap;
import java.util.Map;

public class DonorViewModelBuilder extends AbstractBuilder<DonorViewModel> {

  private Donor donor;
  private Map<String, Boolean> permissions;

  public static DonorViewModelBuilder aDonorViewModel() {
    return new DonorViewModelBuilder();
  }

  public DonorViewModelBuilder withDonor(Donor donor) {
    this.donor = donor;
    return this;
  }

  public DonorViewModelBuilder withPermission(String key, boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }

  @Override
  public DonorViewModel build() {
    DonorViewModel donorViewModel = new DonorViewModel(donor);
    donorViewModel.setPermissions(permissions);
    return donorViewModel;
  }

}
