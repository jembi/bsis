

package backingform;

import java.util.Map;

/**
 *
 * @author srikanth
 */
public class TTITestResultBackingForm {
    
    private Long donationId;
    private Map<String, String> testResult;

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public Map<String, String> getTestResult() {
        return testResult;
    }

    public void setTestResult(Map<String, String> testResult) {
        this.testResult = testResult;
    }

}
