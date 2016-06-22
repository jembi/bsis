package org.jembi.bsis.helpers.builders;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.viewmodel.DonorViewModel;

public class DonorViewModelBuilder extends AbstractBuilder<DonorViewModel> {

  private Donor donor;
  private Map<String, Boolean> permissions;

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

  public static DonorViewModelBuilder aDonorViewModel() {
    return new DonorViewModelBuilder();
  }

}
