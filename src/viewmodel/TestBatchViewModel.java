
package viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

import org.hibernate.Hibernate;

import utils.CustomDateFormatter;

public class TestBatchViewModel {
   
    private TestBatch testBatch;

    public TestBatchViewModel(TestBatch testBatch) {
        this.testBatch = testBatch;
    }
    
	public String getCreatedDate() {
		if (testBatch.getCreatedDate() == null)
		      return "";
		return CustomDateFormatter.getDateTimeString(testBatch.getCreatedDate());
	}
    
    public Long getId(){
        return testBatch.getId();
    }
    
    public TestBatchStatus getStatus(){
        return testBatch.getStatus();
    }
    
    public String getBatchNumber(){
        return testBatch.getBatchNumber();
    }
    
    public Integer getNumSamples(){
    	Integer count = 0;
    	for(DonationBatch cb: testBatch.getDonationBatches()){
    		count += cb.getDonations().size();
    	}
    	return count;
    }
    
    public String getNotes(){
        return testBatch.getNotes();
    }
    
    public  List<DonationBatchViewModel> getDonationBatches(){
        return getDonationBatchViewModels(testBatch.getDonationBatches());
    }
    
    public static List<DonationBatchViewModel> getDonationBatchViewModels(List<DonationBatch> donationBatches) {
	    if (donationBatches == null)
	      return Arrays.asList(new DonationBatchViewModel[0]);
	    List<DonationBatchViewModel> donationBatchViewModels = new ArrayList<DonationBatchViewModel>();
	    for (DonationBatch donationBatch : donationBatches) {
	      donationBatchViewModels.add(new DonationBatchViewModel(donationBatch));
	    }
	    return donationBatchViewModels;
	}
    
    public String getLastUpdated() {
    	return CustomDateFormatter.getDateTimeString(testBatch.getLastUpdated());
    }

}
