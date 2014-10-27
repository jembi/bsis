package backingform;

import java.util.Map;

public class TestResultBackingForm {
    
    private Long donationId;
    private String donationIdentificationNumber; 
    private Map<Long, String> testResults;
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

    public Map<Long, String> getTestResults() {
        return testResults;
    }

    public void setTestResults(Map<Long, String> testResults) {
        this.testResults = testResults;
    }

    public boolean getSaveUninterpretableResults() {
        return saveUninterpretableResults;
    }

    public void setSaveUninterpretableResults(boolean saveUninterpretableResults) {
        this.saveUninterpretableResults = saveUninterpretableResults;
    }
  
}
