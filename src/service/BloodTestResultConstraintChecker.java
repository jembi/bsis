package service;

import java.util.List;

import model.bloodtesting.BloodTestCategory;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import repository.bloodtesting.BloodTestingRuleResultSet;
import repository.bloodtesting.BloodTypingStatus;


@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Service
public class BloodTestResultConstraintChecker {
	
	/**
	 * Determines if the BloodTestResult may be edited.
	 */
	public boolean canEdit(BloodTestingRuleResultSet bloodTestingRuleResultSet, BloodTestResult bloodTestResult) {
		if (BloodTestCategory.BLOODTYPING.equals(bloodTestResult.getBloodTest().getCategory())) {
			if (bloodTestResult.getDonation().getBloodTypingStatus().equals(BloodTypingStatus.COMPLETE)) {
				// if this is a blood typing test and the blood typing is complete, then the computer says no
				return false;
			}
		} else if (BloodTestCategory.TTI.equals(bloodTestResult.getBloodTest().getCategory())) {
			if (bloodTestResult.getDonation().getTTIStatus().equals(TTIStatus.NOT_DONE)) {
				// return quickly if the status is not done
				return true;
			} else if (bloodTestingRuleResultSet.getPendingTtiTestsIds().size() > 0) {
				List<Integer> extraTests = bloodTestingRuleResultSet.getPendingTtiTests().get(
				    bloodTestResult.getBloodTest().getId());
				if (extraTests == null || extraTests.size() == 0) {
					// if there are pending TTI tests for this TTI test
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
