package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.bloodtesting.BloodTestResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.bloodtesting.BloodTestingRuleResultSet;
import service.BloodTestResultConstraintChecker;
import viewmodel.BloodTestResultViewModel;
import viewmodel.BloodTestingRuleResult;

@Service
public class BloodTestingRuleResultViewModelFactory {
	
	@Autowired
	BloodTestResultConstraintChecker bloodTestResultConstraintChecker;

    public BloodTestingRuleResult createBloodTestResultViewModel(BloodTestingRuleResultSet bloodTestingRuleResultSet) {
		BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
		
		// the blood donation
		ruleResult.setDonation(bloodTestingRuleResultSet.getDonation());
		
		// test data in various formats
		ruleResult.setStoredTestResults(bloodTestingRuleResultSet.getStoredTestResults());
		Map<String, BloodTestResultViewModel> recentTestResultsViewModel = new HashMap<String, BloodTestResultViewModel>();
		for (Integer testId : bloodTestingRuleResultSet.getRecentTestResults().keySet()) {
			recentTestResultsViewModel.put(testId.toString(), createBloodTestResultViewModel(bloodTestingRuleResultSet, bloodTestingRuleResultSet.getRecentTestResults().get(testId)));
		}
		ruleResult.setRecentTestResults(recentTestResultsViewModel);
		ruleResult.setAvailableTestResults(bloodTestingRuleResultSet.getAvailableTestResults());
		
		// pending tests
		List<String> pendingBloodTypingTestsIds = new ArrayList<String>();
		pendingBloodTypingTestsIds.addAll(bloodTestingRuleResultSet.getPendingAboTestsIds());
		pendingBloodTypingTestsIds.addAll(bloodTestingRuleResultSet.getPendingRhTestsIds());
		ruleResult.setPendingBloodTypingTestsIds(bloodTestingRuleResultSet.getPendingAboTestsIds());
		ruleResult.setPendingTTITestsIds(bloodTestingRuleResultSet.getPendingTtiTestsIds());
		
		// blood typing results
		ruleResult.setBloodTypingMatchStatus(bloodTestingRuleResultSet.getBloodTypingMatchStatus());
		ruleResult.setBloodTypingStatus(bloodTestingRuleResultSet.getBloodTypingStatus());
		
		// determine if there are any uninterpretable results
		if (bloodTestingRuleResultSet.getBloodAboChanges().isEmpty() && bloodTestingRuleResultSet.getPendingAboTestsIds().isEmpty() && bloodTestingRuleResultSet.getAboUninterpretable()) {
			// there was an attempt to match a rule for blood ABO
			ruleResult.setAboUninterpretable(true);
		}
		if (bloodTestingRuleResultSet.getBloodRhChanges().isEmpty() && bloodTestingRuleResultSet.getPendingRhTestsIds().isEmpty() && bloodTestingRuleResultSet.getRhUninterpretable()) {
			// there was an attempt to match a rule for blood Rh
			ruleResult.setRhUninterpretable(true);
		}
		
		ruleResult.setAllBloodAboChanges(bloodTestingRuleResultSet.getBloodAboChanges());
		ruleResult.setAllBloodRhChanges(bloodTestingRuleResultSet.getBloodRhChanges());
		String bloodAbo = "";
		if (bloodTestingRuleResultSet.getBloodAboChanges() != null && bloodTestingRuleResultSet.getBloodAboChanges().size() == 1) {
			bloodAbo = bloodTestingRuleResultSet.getBloodAboChanges().iterator().next();
		}
		ruleResult.setBloodAbo(bloodAbo);
		String bloodRh = "";
		if (bloodTestingRuleResultSet.getBloodRhChanges() != null && bloodTestingRuleResultSet.getBloodRhChanges().size() == 1) {
			bloodRh = bloodTestingRuleResultSet.getBloodRhChanges().iterator().next();
		}
		ruleResult.setBloodRh(bloodRh);
		ruleResult.setExtraInformation(bloodTestingRuleResultSet.getExtraInformation());
		
		// TTI test information
		ruleResult.setTTIStatus(bloodTestingRuleResultSet.getTtiStatus());
		ruleResult.setTTIStatusChanges(bloodTestingRuleResultSet.getTtiStatusChanges());
		ruleResult.setTTIStatus(bloodTestingRuleResultSet.getTtiStatus());
		ruleResult.setTtiUninterpretable(false);
		
		return ruleResult;
    }
    
	public BloodTestResultViewModel createBloodTestResultViewModel(BloodTestingRuleResultSet bloodTestingRuleResultSet, BloodTestResult bloodTestResult) {
    	BloodTestResultViewModel bloodTestResultViewModel = new BloodTestResultViewModel(bloodTestResult);
    	Map<String, Boolean> permissions = new HashMap<String, Boolean>();
    	permissions.put("canEdit", bloodTestResultConstraintChecker.canEdit(bloodTestingRuleResultSet, bloodTestResult));
    	bloodTestResultViewModel.setPermissions(permissions);
    	return bloodTestResultViewModel;
    }
}
