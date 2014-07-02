package backingform;

import java.util.Date;
import java.util.List;
import model.donationbatch.DonationBatch;

public class FindDonationBatchBackingForm {

  private DonationBatch donationBatch;
  private String din;
  private List<String> donationCenters;
  private List<String> donationSites;
  private String exactDate; 
  private int period;

  public FindDonationBatchBackingForm() {
    donationBatch = new DonationBatch();
  } 
  
  public DonationBatch getBatch() {
    return donationBatch;
  }

  public void setBatch(DonationBatch batch) {
    this.donationBatch = batch;
  }

  public String getNotes() {
    return donationBatch.getNotes();
  }

  public void setNotes(String notes) {
    donationBatch.setNotes(notes);
  }

  public List<String> getDonationCenters() {
    return donationCenters;
  }

  public void setDonationCenters(List<String> donationCenters) {
    this.donationCenters = donationCenters;
  }

  public List<String> getDonationSites() {
    return donationSites;
  }

  public void setDonationSites(List<String> donationSites) {
    this.donationSites = donationSites;
  }

    public String getDin() {
        return din;
    }

    public void setDin(String din) {
        this.din = din;
    }

    public String getExactDate() {
        return exactDate;
    }

    public void setExactDate(String exactDate) {
        this.exactDate = exactDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
  
    
  
}
