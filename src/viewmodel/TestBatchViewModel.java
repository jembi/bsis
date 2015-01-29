
package viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.collectionbatch.CollectionBatch;
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
    	for(CollectionBatch cb: testBatch.getCollectionBatches()){
    		count += cb.getCollectionsInBatch().size();
    	}
    	return count;
    }
    
    public String getNotes(){
        return testBatch.getNotes();
    }
    
    public  List<CollectionBatchViewModel> getCollectionBatches(){
        return getCollectionBatchViewModels(testBatch.getCollectionBatches());
    }
    
    public static List<CollectionBatchViewModel> getCollectionBatchViewModels(List<CollectionBatch> collectionBatches) {
	    if (collectionBatches == null)
	      return Arrays.asList(new CollectionBatchViewModel[0]);
	    List<CollectionBatchViewModel> collectionBatchViewModels = new ArrayList<CollectionBatchViewModel>();
	    for (CollectionBatch collectionBatch : collectionBatches) {
	      collectionBatchViewModels.add(new CollectionBatchViewModel(collectionBatch));
	    }
	    return collectionBatchViewModels;
	}
    
    public String getLastUpdated() {
    	return CustomDateFormatter.getDateTimeString(testBatch.getLastUpdated());
    }

}
