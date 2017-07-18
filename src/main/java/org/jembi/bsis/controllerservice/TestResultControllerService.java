package org.jembi.bsis.controllerservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.service.BloodTestsService;
import org.jembi.bsis.viewmodel.BloodTestResultViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestResultControllerService {

  @Autowired
  private BloodTestsService bloodTestsService;

  public BloodTestingRuleResult getBloodTestingRuleResult(Donation donation) {
    return bloodTestsService.executeTests(donation);
  }

  public List<BloodTestingRuleResult> getBloodTestingRuleResults(TestBatch testBatch) {
    return bloodTestsService.executeTests(testBatch.getDonations());
  }

  public List<BloodTestingRuleResult> getBloodTestingRuleResults(BloodTestType bloodTestType, TestBatch testBatch) {
    List<BloodTestingRuleResult> results = getBloodTestingRuleResults(testBatch);

    if (bloodTestType != null) {
      List<BloodTestingRuleResult> filteredRuleResults = new ArrayList<>();
      for (BloodTestingRuleResult result : results) {
        Map<UUID, BloodTestResultViewModel> filteredModelMap = new HashMap<>();
        for (UUID key : result.getRecentTestResults().keySet()) {
          BloodTestResultViewModel model = result.getRecentTestResults().get(key);
          if (model.getBloodTest().getBloodTestType().equals(bloodTestType)) {
            filteredModelMap.put(key, model);
          }
        }
        result.setRecentTestResults(filteredModelMap);
        filteredRuleResults.add(result);
      }
      results = filteredRuleResults;
    }

    return results;
  }
}