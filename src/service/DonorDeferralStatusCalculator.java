package service;

import java.util.Arrays;
import java.util.List;
import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.bloodtesting.TTIStatus;
import model.donor.Donor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.DonorDeferralRepository;
import constant.GeneralConfigConstants;

@Service
public class DonorDeferralStatusCalculator {
    
    @Autowired
    private GeneralConfigAccessorService generalConfigAccessorService;
    @Autowired
    private DonorDeferralRepository donorDeferralRepository;

    /**
     * Determine whether or not a donor should be deferred based on the test results of an unsafe donation. It takes
     * into account the results of confirmatory tests and country policy.
     * 
     * @param bloodTestResults The {@link BloodTestResult}s for the {@link TTIStatus#UNSAFE} donation.
     * @return <code>true</code> if the donor should be deferred, otherwise <code>false</code>.
     */
    public boolean shouldDonorBeDeferred(List<BloodTestResult> bloodTestResults) {

        for (BloodTestResult bloodTestResult : bloodTestResults) {

            BloodTest bloodTest = bloodTestResult.getBloodTest();
            if (bloodTest.getBloodTestType() == BloodTestType.CONFIRMATORY_TTI) {

                List<String> positiveBloodTestResults = Arrays.asList(bloodTest.getPositiveResults().split(","));
                if (positiveBloodTestResults.contains(bloodTestResult.getResult())) {
                    // This donation has at least one positive confirmatory result so defer the donor
                    return true;
                }
            }
        }

        // Use the general config to determine whether or not the donor should be deferred
        return generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.DEFER_DONORS_WITH_NEG_REPEAT_OUTCOMES);
    }
    
    public boolean isDonorCurrentlyDeferred(Donor donor) {
        return donorDeferralRepository.countCurrentDonorDeferralsForDonor(donor) > 0;
    }

}
