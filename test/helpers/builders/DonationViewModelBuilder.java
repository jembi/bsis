package helpers.builders;

import java.util.HashMap;
import java.util.Map;

import model.donation.Donation;
import viewmodel.DonationViewModel;

public class DonationViewModelBuilder extends AbstractBuilder<DonationViewModel> {

    private Donation donation;
    private Map<String, Boolean> permissions;
    
    public DonationViewModelBuilder withDonation(Donation donation) {
        this.donation = donation;
        return this;
    }
    
    public DonationViewModelBuilder withPermission(String key, Boolean value) {
        if (permissions == null) {
            permissions = new HashMap<>();
        }
        permissions.put(key, value);
        return this;
    }

    @Override
    public DonationViewModel build() {
        DonationViewModel donationViewModel = new DonationViewModel(donation);
        donationViewModel.setPermissions(permissions);
        return donationViewModel;
    }
    
    public static DonationViewModelBuilder aDonationViewModel() {
        return new DonationViewModelBuilder();
    }

}
