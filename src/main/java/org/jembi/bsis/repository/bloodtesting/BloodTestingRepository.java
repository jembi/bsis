package org.jembi.bsis.repository.bloodtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.service.BloodTestingRuleEngine;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BloodTestingRepository {

  @Autowired
  private DonationRepository donationRepository;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  @Autowired
  private BloodTestingRuleEngine ruleEngine;

  public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatches(
      List<Long> donationBatchIds) {

    List<BloodTestingRuleResult> bloodTestingRuleResults = new ArrayList<BloodTestingRuleResult>();

    for (Long donationBatchId : donationBatchIds) {
      List<Donation> donations = donationBatchRepository.findDonationsInBatch(donationBatchId);

      for (Donation donation : donations) {

        if (!donation.getPackType().getTestSampleProduced()) {
          // This donation did not produce a test sample so skip it
          continue;
        }

        BloodTestingRuleResult ruleResult = ruleEngine.applyBloodTests(
            donation, new HashMap<Long, String>());
        bloodTestingRuleResults.add(ruleResult);
      }
    }

    return bloodTestingRuleResults;
  }

  public List<BloodTestingRuleResult> getAllTestsStatusForDonationBatchesByBloodTestType(List<Long> donationBatchIds,
                                                                                         BloodTestType bloodTestType) {

    List<BloodTestingRuleResult> bloodTestingRuleResults = getAllTestsStatusForDonationBatches(donationBatchIds);
    List<BloodTestingRuleResult> filteredRuleResults = new ArrayList<BloodTestingRuleResult>();
    for (BloodTestingRuleResult result : bloodTestingRuleResults) {
      Map<String, BloodTestResultViewModel> modelMap = result.getRecentTestResults();
      Map<String, BloodTestResultViewModel> filteredModelMap = new HashMap<String, BloodTestResultViewModel>();
      for (String key : modelMap.keySet()) {
        BloodTestResultViewModel model = modelMap.get(key);
        if (model.getBloodTest().getBloodTestType().equals(bloodTestType)) {
          filteredModelMap.put(key, model);
        }
      }
      result.setRecentTestResults(filteredModelMap);
      filteredRuleResults.add(result);
    }
    bloodTestingRuleResults = filteredRuleResults;

    return bloodTestingRuleResults;
  }

  public BloodTestingRuleResult getAllTestsStatusForDonation(
      Long donationId) {
    Donation donation = donationRepository
        .findDonationById(donationId);
    return ruleEngine.applyBloodTests(donation,
        new HashMap<Long, String>());
  }
}

