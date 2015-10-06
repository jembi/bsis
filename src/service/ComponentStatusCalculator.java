package service;

import java.util.Arrays;
import java.util.List;
import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import org.springframework.stereotype.Service;

@Service
public class ComponentStatusCalculator {

    public boolean shouldComponentsBeDiscarded(List<BloodTestResult> bloodTestResults) {

        for (BloodTestResult bloodTestResult : bloodTestResults) {

            BloodTest bloodTest = bloodTestResult.getBloodTest();
            if (!bloodTest.isFlagComponentsForDiscard()) {
                // This blood test does not flag components for discard
                continue;
            }

            List<String> positiveBloodTestResults = Arrays.asList(bloodTest.getPositiveResults().split(","));
            if (positiveBloodTestResults.contains(bloodTestResult.getResult())) {
                // The blood test result is positive and it flags components for discard
                return true;
            }
        }

        // There are no positive blood tests which flag components for discard
        return false;
    }

}
