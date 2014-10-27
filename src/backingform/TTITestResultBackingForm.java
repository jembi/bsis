package backingform;

import java.util.Map;

public class TTITestResultBackingForm {
    
    private Long donationId;
    private String donationIdentificationNumber; 
    private Map<Long, String> ttiTestResults;

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

    public Map<Long, String> getTTITestResults() {
        return ttiTestResults;
    }

    public void setTTITestResults(Map<Long, String> ttiTestResults) {
        this.ttiTestResults = ttiTestResults;
    }
    
}
