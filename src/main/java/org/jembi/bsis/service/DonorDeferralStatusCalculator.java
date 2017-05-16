package org.jembi.bsis.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.DonorDeferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonorDeferralStatusCalculator {

  @Autowired
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Autowired
  private DonorDeferralRepository donorDeferralRepository;

  /**
   * Determine whether or not a donor should be deferred based on the test results of an unsafe
   * donation. It takes into account the results of confirmatory tests and country policy.
   *
   * @param bloodTestResults The {@link BloodTestResult}s for the {@link TTIStatus#UNSAFE}
   *                         donation.
   * @return <code>true</code> if the donor should be deferred, otherwise <code>false</code>.
   */
  public boolean shouldDonorBeDeferred(List<BloodTestResult> bloodTestResults) {

    for (BloodTestResult bloodTestResult : bloodTestResults) {

      BloodTest bloodTest = bloodTestResult.getBloodTest();
      if (BloodTestType.isPendingTTI(bloodTest.getBloodTestType())) {

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

  public boolean isDonorCurrentlyDeferred(UUID donorId) {
    return donorDeferralRepository.countCurrentDonorDeferralsForDonor(donorId) > 0;
  }

  public boolean isDonorDeferredOnDate(UUID donorId, Date date) {
    return donorDeferralRepository.countDonorDeferralsForDonorOnDate(donorId, date) > 0;
  }

}
