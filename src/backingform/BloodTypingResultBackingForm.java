

package backingform;

import java.util.Map;

/**
 *
 * @author srikanth
 */
public class BloodTypingResultBackingForm {
    
    private Long donationId;
    private Map<String, String> typingResult;
    private boolean saveUninterpretableResults;

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public Map<String, String> getTypingResult() {
        return typingResult;
    }

    public void setTypingResult(Map<String, String> typingResult) {
        this.typingResult = typingResult;
    }

    public boolean isSaveUninterpretableResults() {
        return saveUninterpretableResults;
    }

    public void setSaveUninterpretableResults(boolean saveUninterpretableResults) {
        this.saveUninterpretableResults = saveUninterpretableResults;
    }
    
    
    
    
    
}
