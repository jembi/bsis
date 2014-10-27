package backingform;

import java.util.Map;

public class TTITestResultBackingForm {
    
    private Long donationId;
    private String donationIdentificationNumber; 
    private Map<String, String> ttiTestResults;

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

    public Map<String, String> getTTITestResults() {
        return ttiTestResults;
    }

    public void setTTITestResults(Map<String, String> ttiTestResults) {
        this.ttiTestResults = ttiTestResults;
    }
    
}
