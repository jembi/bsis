package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.donordeferral.DonorDeferral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DeferralConstraintChecker;
import viewmodel.DonorDeferralViewModel;

@Service
public class DonorDeferralViewModelFactory {
	
    @Autowired
    private DeferralConstraintChecker deferralConstraintChecker;
    
    public List<DonorDeferralViewModel> createDonorDeferralViewModels(List<DonorDeferral> deferrals) {
        List<DonorDeferralViewModel> donorDeferralViewModels = new ArrayList<>();
        for (DonorDeferral deferral : deferrals) {
        	donorDeferralViewModels.add(createDonorDeferralViewModel(deferral));
        }
        return donorDeferralViewModels;
    }
    
    public DonorDeferralViewModel createDonorDeferralViewModel(DonorDeferral donorDeferral) {
    	DonorDeferralViewModel donorDeferralViewModel = new DonorDeferralViewModel(donorDeferral);

    	// Populate permissions
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("canDelete", deferralConstraintChecker.canDeleteDonorDeferral(donorDeferral.getId()));
        donorDeferralViewModel.setPermissions(permissions);

        return donorDeferralViewModel;
    }

}
