package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRuleResultSet;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.Titre;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.service.BloodTestResultConstraintChecker;
import org.jembi.bsis.service.DonationConstraintChecker;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BloodTestingRuleResultViewModelFactory {

  @Autowired
  private BloodTestResultConstraintChecker bloodTestResultConstraintChecker;

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;

  @Autowired
  private DonationFactory donationFactory;

  @Autowired
  private BloodTestResultFactory bloodTestResultFactory;

  public BloodTestingRuleResult createBloodTestResultFullViewModel(BloodTestingRuleResultSet bloodTestingRuleResultSet) {
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();

    // the blood donation
    Donation donation = bloodTestingRuleResultSet.getDonation();
    ruleResult.setDonation(donationFactory.createDonationFullViewModelWithoutPermissions(donation));

    // pending tests
    List<UUID> pendingBloodTypingTestsIds = new ArrayList<>();
    pendingBloodTypingTestsIds.addAll(bloodTestingRuleResultSet.getPendingAboTestsIds());
    pendingBloodTypingTestsIds.addAll(bloodTestingRuleResultSet.getPendingRhTestsIds());
    ruleResult.setPendingBloodTypingTestsIds(pendingBloodTypingTestsIds);
    ruleResult.setPendingConfirmatoryTTITestsIds(bloodTestingRuleResultSet.getPendingConfirmatoryTtiTestsIds());
    ruleResult.setPendingRepeatTTITestsIds(bloodTestingRuleResultSet.getPendingRepeatTtiTestsIds());
    ruleResult.setPendingRepeatAndConfirmatoryTtiTestsIds(bloodTestingRuleResultSet.getPendingRepeatAndConfirmatoryTtiTestsIds());

    // blood typing results
    ruleResult.setBloodTypingMatchStatus(bloodTestingRuleResultSet.getBloodTypingMatchStatus());
    ruleResult.setBloodTypingStatus(bloodTestingRuleResultSet.getBloodTypingStatus());

    ruleResult.setAllBloodAboChanges(bloodTestingRuleResultSet.getBloodAboChanges());
    ruleResult.setAllBloodRhChanges(bloodTestingRuleResultSet.getBloodRhChanges());
    String bloodAbo = donation.getBloodAbo();
    if (bloodTestingRuleResultSet.getBloodAboChanges() != null && bloodTestingRuleResultSet.getBloodAboChanges().size() == 1) {
      bloodAbo = bloodTestingRuleResultSet.getBloodAboChanges().iterator().next();
    }
    ruleResult.setBloodAbo(bloodAbo);
    String bloodRh = donation.getBloodRh();
    if (bloodTestingRuleResultSet.getBloodRhChanges() != null && bloodTestingRuleResultSet.getBloodRhChanges().size() == 1) {
      bloodRh = bloodTestingRuleResultSet.getBloodRhChanges().iterator().next();
    }
    ruleResult.setBloodRh(bloodRh);
    Titre titre = donation.getTitre();
    if (bloodTestingRuleResultSet.getTitreChanges() != null && bloodTestingRuleResultSet.getTitreChanges().size() == 1) {
      String titreValue = bloodTestingRuleResultSet.getTitreChanges().iterator().next();
      if (StringUtils.isBlank(titreValue)) {
        titre = null;
      } else {
        titre = Titre.valueOf(titreValue);
      }
    }
    ruleResult.setTitre(titre);

    // TTI test information
    ruleResult.setTTIStatus(bloodTestingRuleResultSet.getTtiStatus());
    ruleResult.setTTIStatusChanges(bloodTestingRuleResultSet.getTtiStatusChanges());
    ruleResult.setTTIStatus(bloodTestingRuleResultSet.getTtiStatus());

    // Determine if the Donation is released
    TestBatch testBatch = donation.getTestBatch();
    boolean isDonationReleased = donationConstraintChecker.donationIsReleased(testBatch, donation, ruleResult);

    // test data in various formats
    ruleResult.setStoredTestResults(bloodTestingRuleResultSet.getStoredTestResults());
    Map<UUID, BloodTestResultFullViewModel> recentTestResultsViewModel = new HashMap<>();
    for (UUID testId : bloodTestingRuleResultSet.getRecentTestResults().keySet()) {
      recentTestResultsViewModel.put(testId, createBloodTestResultFullViewModel(bloodTestingRuleResultSet, bloodTestingRuleResultSet.getRecentTestResults().get(testId), isDonationReleased));
    }
    ruleResult.setRecentTestResults(recentTestResultsViewModel);
    ruleResult.setAvailableTestResults(bloodTestingRuleResultSet.getAvailableTestResults());

    return ruleResult;
  }

  public BloodTestResultFullViewModel createBloodTestResultFullViewModel(BloodTestingRuleResultSet bloodTestingRuleResultSet, BloodTestResult bloodTestResult, boolean isDonationReleased) {
    BloodTestResultFullViewModel bloodTestResultFullViewModel = bloodTestResultFactory.createFullViewModel(bloodTestResult);
    Map<String, Boolean> permissions = new HashMap<String, Boolean>();
    permissions.put("canEdit", bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult, isDonationReleased));
    bloodTestResultFullViewModel.setPermissions(permissions);
    return bloodTestResultFullViewModel;
  }
}
