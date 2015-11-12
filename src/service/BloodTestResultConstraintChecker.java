package service;

import java.util.List;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import repository.bloodtesting.BloodTestingRuleResultSet;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;


@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class BloodTestResultConstraintChecker {
	
	/**
	 * Determines if the BloodTestResult may be edited.
	 */
	public boolean canEdit(BloodTestingRuleResultSet bloodTestingRuleResultSet, BloodTestResult bloodTestResult) {
		if (BloodTestCategory.BLOODTYPING.equals(bloodTestResult.getBloodTest().getCategory())) {
			BloodTypingStatus bloodTypingStatus = bloodTestingRuleResultSet.getBloodTypingStatus();
			BloodTypingMatchStatus bloodTypingMatchStatus = bloodTestingRuleResultSet.getBloodTypingMatchStatus();
			if (!BloodTypingStatus.COMPLETE.equals(bloodTypingStatus)) {
				// can edit if the blood typing status is not complete
				return true;
			} else if (!BloodTypingStatus.NOT_DONE.equals(bloodTypingStatus) 
					&& (BloodTypingMatchStatus.NO_MATCH.equals(bloodTypingMatchStatus) 
							|| BloodTypingMatchStatus.AMBIGUOUS.equals(bloodTypingMatchStatus))) {
				// the user will be requested to confirm the blood typing results 
				return true;
			} else if (bloodTestingRuleResultSet.getPendingAboTestsIds().size() > 0
					|| bloodTestingRuleResultSet.getPendingRhTestsIds().size() > 0) {
				// there are pending blood tests 
				List<Integer> extraTests = bloodTestingRuleResultSet.getPendingTests().get(
					bloodTestResult.getBloodTest().getId());
				if (extraTests == null || extraTests.size() == 0) {
					// if there are no pending serology tests for this test
					return false;
				}
			}
			else { 
				// no need for confirmation
				return false;
			}
		} else if (BloodTestCategory.TTI.equals(bloodTestResult.getBloodTest().getCategory())) {
			if (bloodTestingRuleResultSet.getTtiStatus().equals(TTIStatus.NOT_DONE)) {
				// return quickly if the status is not done
				return true;
			} else if (bloodTestingRuleResultSet.getPendingTtiTestsIds().size() > 0) {
				List<Integer> extraTests = bloodTestingRuleResultSet.getPendingTests().get(
					bloodTestResult.getBloodTest().getId());
				if (extraTests == null || extraTests.size() == 0) {
					// if there are no pending TTI tests for this TTI test
					return false;
				}
			} else {
				// there aren't any pending tests
				return false;
			}
		}
		return true;
	}
}
