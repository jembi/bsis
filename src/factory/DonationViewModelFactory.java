package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.donation.Donation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.DonationConstraintChecker;
import viewmodel.AdverseEventViewModel;
import viewmodel.DonationViewModel;

@Service
public class DonationViewModelFactory {
    
    @Autowired
    private DonationConstraintChecker donationConstraintChecker;
    @Autowired
    private AdverseEventViewModelFactory adverseEventViewModelFactory;
    
    public List<DonationViewModel> createDonationViewModelsWithPermissions(List<Donation> donations) {
        List<DonationViewModel> donantionViewModels = new ArrayList<>();
        for (Donation donation : donations) {
            donantionViewModels.add(createDonationViewModelWithPermissions(donation));
        }
        return donantionViewModels;
    }
    
    public DonationViewModel createDonationViewModelWithPermissions(Donation donation) {
        DonationViewModel donationViewModel = new DonationViewModel(donation);
        
        // Populate permissions
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("canDelete", donationConstraintChecker.canDeleteDonation(donation.getId()));
        permissions.put("canUpdateDonationFields", donationConstraintChecker.canUpdateDonationFields(donation.getId()));
        donationViewModel.setPermissions(permissions);
        
        if (donation.getAdverseEvent() != null) {
            AdverseEventViewModel adverseEventViewModel = adverseEventViewModelFactory.createAdverseEventViewModel(
                    donation.getAdverseEvent());
            donationViewModel.setAdverseEvent(adverseEventViewModel);
        }
        
        return donationViewModel;
    }

}
