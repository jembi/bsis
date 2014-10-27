package backingform;

import java.util.Map;

public class BloodTypingResultBackingForm {
    
    private Long donationId;
    private String donationIdentificationNumber; 
    private Map<Long, String> bloodTypingTestResults;
    private boolean saveUninterpretableResults;

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }
    
    public String getDonationIdentificationNumber() {
        return donationIdentificationNumber;
    }

    public void setDonationIdentificationNumber(String donationIdentificationNumber) {
        this.donationIdentificationNumber = donationIdentificationNumber;
    }

    public Map<Long, String> getBloodTypingTestResults() {
        return bloodTypingTestResults;
    }

    public void setBloodTypingTestResults(Map<Long, String> bloodTypingTestResults) {
        this.bloodTypingTestResults = bloodTypingTestResults;
    }

    public boolean getSaveUninterpretableResults() {
        return saveUninterpretableResults;
    }

    public void setSaveUninterpretableResults(boolean saveUninterpretableResults) {
        this.saveUninterpretableResults = saveUninterpretableResults;
    }
  
}
