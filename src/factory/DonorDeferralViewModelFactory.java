package factory;

import java.util.ArrayList;
import java.util.List;

import model.donordeferral.DonorDeferral;

import org.springframework.stereotype.Service;

import viewmodel.DonorDeferralViewModel;

@Service
public class DonorDeferralViewModelFactory {
    
    public List<DonorDeferralViewModel> createDonorDeferralViewModels(List<DonorDeferral> deferrals) {
        List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<>();
        for (DonorDeferral deferral : deferrals) {
        	donorDeferralViewModels.add(createDonorDeferralViewModel(deferral));
        }
        return donorDeferralViewModels;
    }
    
    public DonorDeferralViewModel createDonorDeferralViewModel(DonorDeferral donorDeferral) {
    	return new DonorDeferralViewModel(donorDeferral);
    }

}
